package com.ruoyi.common.utils.file;

import com.ruoyi.common.utils.FileUtil;
import com.ruoyi.common.utils.ip.AddressUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author:万登枫
 * @date: 2021/4/19
 */

public class FileDownloadUtils {

    private static final Logger log = LoggerFactory.getLogger(FileDownloadUtils.class);

    public static void download(HttpServletRequest request, HttpServletResponse response, File file){
        try {
            byte[] data= FileUtil.readFileByBytes(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename="
                    + FileUtils.setFileDownloadHeader(request,file.getName()));
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            if(file.exists()){
                file.delete();
            }
        }
    }



}
