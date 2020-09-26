package com.ruoyi.project.system.controller;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.exception.GlobalExceptionHandler;
import com.ruoyi.project.system.domain.MultipartFileParam;
import com.ruoyi.project.system.domain.SysFile;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.UUID;

/**
 *  大文件上传
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/upload")
public class SysUploadController extends BaseController
{

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ApiOperation("大文件分片上传")
    @RequestMapping("/bigFile")
    public AjaxResult upload(MultipartFileParam param) throws Exception
    {

        String path= "/bigFile/"+ DateUtils.datePath()+"/";
        String newFileName = UUID.randomUUID().toString().replace("-","")
                +"."+FileUploadUtils.getExtension(param.getFile());;
        // response.setStatus对接前端插件
        //        200, 201, 202: 当前块上传成功，不需要重传。
        //        404, 415. 500, 501: 当前块上传失败，会取消整个文件上传。
        //        其他状态码: 出错了，但是会自动重试上传。

        try {
            param.setTaskId(param.getIdentifier());
            chunkUploadByMappedByteBuffer(param,path,newFileName);
            return AjaxResult.success(Constants.RESOURCE_PREFIX+path+newFileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.error(HttpStatus.UNSUPPORTED_TYPE,"上传文件失败");
        }
    }



    public String chunkUploadByMappedByteBuffer(MultipartFileParam param, String filePath,String newFileName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if(param.getTaskId() == null || "".equals(param.getTaskId())){
            param.setTaskId(UUID.randomUUID().toString());
        }
        /**
         *
         * 1：创建临时文件，和源文件一个路径
         * 2：如果文件路径不存在重新创建
         */
        String fileName = param.getFile().getOriginalFilename();
        String tempFileName = param.getTaskId() + fileName.substring(fileName.lastIndexOf(".")) + "_tmp";
        filePath = RuoYiConfig.getProfile() + filePath;
        File fileDir = new File(filePath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File tempFile = new File(filePath,tempFileName);
        //第一步
        RandomAccessFile raf = new RandomAccessFile(tempFile,"rw");
        //第二步
        FileChannel fileChannel = raf.getChannel();
        //第三步 计算偏移量
        long position = (param.getChunkNumber()-1) * param.getChunkSize();
        //第四步
        byte[] fileData = param.getFile().getBytes();
        //第五步
        long end=position+fileData.length-1;
        fileChannel.position(position);
        fileChannel.write(ByteBuffer.wrap(fileData));
        //使用 fileChannel.map的方式速度更快，但是容易产生IO操作，无建议使用
//        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,position,fileData.length);
//        //第六步
//        mappedByteBuffer.put(fileData);
        //第七步
//        freedMappedByteBuffer(mappedByteBuffer);
//        Method method = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
//        method.setAccessible(true);
//        method.invoke(FileChannelImpl.class, mappedByteBuffer);
        fileChannel.force(true);
        fileChannel.close();
        raf.close();
        //第八步
        boolean isComplete = checkUploadStatus(param,fileName,filePath);
        if(isComplete){
            renameFile(tempFile,newFileName);
        }
        return param.getTaskId();
    }

    /**
     * 文件重命名
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @return
     */
    public void renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            System.out.println("文件不存在");
            return;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        toBeRenamed.renameTo(newFile);
    }

    /**
     * 检查文件上传进度
     * @return
     */
    public boolean checkUploadStatus(MultipartFileParam param,String fileName,String filePath) throws IOException {
        File confFile = new File(filePath,fileName+".conf");
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile,"rw");
        //设置文件长度
        confAccessFile.setLength(param.getTotalChunks());
        //设置起始偏移量
        confAccessFile.seek(param.getChunkNumber()-1);
        //将指定的一个字节写入文件中 127，
        confAccessFile.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        confAccessFile.close();//不关闭会造成无法占用
        //这一段逻辑有点复杂，看的时候思考了好久，创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是Byte.MAX_VALUE 127
        for(int i = 0; i<completeStatusList.length; i++){
            if(completeStatusList[i]!=Byte.MAX_VALUE){
                return false;
            }
        }
        //如果全部文件上传完成，删除conf文件
        confFile.delete();
        return true;
    }

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     * @param mappedByteBuffer
     */
    public static void freedMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }
            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        //可以访问private的权限
                        getCleanerMethod.setAccessible(true);
                        //在具有指定参数的 方法对象上调用此 方法对象表示的底层方法
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer,
                                new Object[0]);
                        cleaner.clean();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("清理缓存出错!!!"+e.getMessage());
                    }
                    System.out.println("缓存清理完毕!!!");
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
