package com.ruoyi.project.system.controller;

import com.ruoyi.common.utils.RegionAnalyzeUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysRegion;
import com.ruoyi.project.system.service.ISysRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地区代码Controller
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@RestController
@RequestMapping("/system/region")
public class SysRegionController extends BaseController
{
    @Autowired
    private ISysRegionService sysRegionService;

    /**
     * 查询地区代码列表
     */
    @GetMapping("/list")
    public AjaxResult list(SysRegion sysRegion)
    {
        return AjaxResult.success(RegionAnalyzeUtil.getRegionList()) ;
    }



    /**
     * 获取地区代码详细信息
     */
    @GetMapping(value = "/{regionCode}")
    public AjaxResult getInfo(@PathVariable("regionCode") String regionCode)
    {
        return AjaxResult.success(RegionAnalyzeUtil.regionAnalyze(regionCode));
    }


}
