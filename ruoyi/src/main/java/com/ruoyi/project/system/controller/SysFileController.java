package com.ruoyi.project.system.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.FileUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.SysFile;
import com.ruoyi.project.system.service.ISysFileService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 附件Controller
 *
 * @author ruoyi
 * @date 2020-08-11
 */
@RestController
@RequestMapping("/system/file")
public class SysFileController extends BaseController
{
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 查询附件列表
     */

    @GetMapping("/list")
    public TableDataInfo list(SysFile sysFile)
    {
        startPage();
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        return getDataTable(list);
    }

    /**
     * 导出附件列表
     */

    @Log(title = "附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysFile sysFile)
    {
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        ExcelUtil<SysFile> util = new ExcelUtil<SysFile>(SysFile.class);
        return util.exportExcel(list, "file");
    }

    /**
     * 获取附件详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysFileService.selectSysFileById(id));
    }

    /**
     * 新增附件
     */

    @Log(title = "附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFile sysFile)
    {
        return toAjax(sysFileService.insertSysFile(sysFile));
    }

    /**
     * 修改附件
     */

    @Log(title = "附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFile sysFile)
    {
        return toAjax(sysFileService.updateSysFile(sysFile));
    }

    /**
     * 删除附件
     */

    @Log(title = "附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysFileService.deleteSysFileByIds(ids));
    }


    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file,
                             SysFile sysFile) throws IOException
    {
        if (!file.isEmpty())
        {
            String filePath = FileUploadUtils.upload(
                    RuoYiConfig.getProfile()+"/"+sysFile.getFileType(),file);
            sysFile.setFilePath(filePath);
            sysFile.setFileName(file.getOriginalFilename());
            sysFileService.insertSysFile(sysFile);
            return AjaxResult.success(sysFile);
        }else{
            return AjaxResult.error("上传文件异常，请联系管理员!");
        }

    }

    @RequestMapping(value = "/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response)
    {
        SysFile sysFile = sysFileService.selectSysFileById(id);
        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        String downloadPath = localPath + StringUtils.substringAfter(sysFile.getFilePath(),
                Constants.RESOURCE_PREFIX);
        File file =new File(downloadPath);
        String fileName= null;
        try {
            fileName = URLEncoder.encode(sysFile.getFileName(), "utf-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("Content-Length", "" + FileUtil.getBytesByFile(file).length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(FileUtil.getBytesByFile(file), response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
