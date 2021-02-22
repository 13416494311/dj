package com.ruoyi.project.party.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.domain.PartyOrgTreeData;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 党组织架构Controller
 *
 * @author ruoyi
 * @date 2020-08-03
 */
@RestController
@RequestMapping("/party/org")
public class DjPartyOrgController extends BaseController
{
    @Autowired
    private IDjPartyOrgService djPartyOrgService;

    @Autowired
    private IDjPartyOrgService partyOrgService;

    @GetMapping("/getOrgMemberChartData")
    public AjaxResult getOrgMemberChartData()
    {

        List<Map<String,Object>> list = new ArrayList<>();
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if(sysUser.getDjPartyMember()!=null&&sysUser.getDjPartyMember().getPartyOrgId().longValue()==(long)52){
            list = djPartyOrgService.getRegistOrgMemberChartData();
        }else{
            list = djPartyOrgService.getOrgMemberChartData();
        }

        return AjaxResult.success(list);
    }

    @GetMapping("/getOrgCount")
    public AjaxResult getOrgCount()
    {
        return AjaxResult.success(djPartyOrgService.getOrgCount());
    }
    /**
     * 查询党组织架构列表
     */
    @GetMapping("/list")
    public AjaxResult list(DjPartyOrg djPartyOrg)
    {
        List<DjPartyOrg> list = djPartyOrgService.selectDjPartyOrgList(djPartyOrg);
        return AjaxResult.success(list);
    }

    /**
     * 查询党组织架构列表
     */
    @GetMapping("/childrenList/{partyOrgId}")
    public AjaxResult childrenList(@PathVariable("partyOrgId") Long partyOrgId)
    {
        List<DjPartyOrg> list = djPartyOrgService.selectChildrenPartyOrgById(partyOrgId);
        return AjaxResult.success(list);
    }

    /**
     * 查询党组织架构列表tree
     */
    @GetMapping("/tree")
    public AjaxResult tree()
    {
        return AjaxResult.success(djPartyOrgService.getPartyOrgTreeData());
    }

    @RequestMapping("/treeselect")
    public AjaxResult treeselect()
    {
        List<PartyOrgTreeData> list =  new ArrayList<PartyOrgTreeData>();

        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if(sysUser.getDjPartyMember()!=null&&sysUser.getDjPartyMember().getPartyOrgId().longValue()==(long)52){
            PartyOrgTreeData partyOrgTreeData = new PartyOrgTreeData();
            DjPartyOrg partyOrg = partyOrgService.selectDjPartyOrgById((long)52);
            partyOrgTreeData.setId(partyOrg.getPartyOrgId());
            partyOrgTreeData.setLabel(partyOrg.getPartyOrgName());
            list.add(partyOrgTreeData);
        }else{
            list = djPartyOrgService.getPartyOrgTreeDataList();
        }
        return AjaxResult.success(list);
    }

    @RequestMapping("/treeselectByEdit")
    public AjaxResult treeselectByEdit()
    {
        List<PartyOrgTreeData>  list = djPartyOrgService.getPartyOrgTreeDataListByEdit();
        return AjaxResult.success(list);
    }
    /**
     * 导出党组织架构列表
     */
    @Log(title = "党组织架构", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjPartyOrg djPartyOrg)
    {
        List<DjPartyOrg> list = djPartyOrgService.selectDjPartyOrgList(djPartyOrg);
        ExcelUtil<DjPartyOrg> util = new ExcelUtil<DjPartyOrg>(DjPartyOrg.class);
        return util.exportExcel(list, "partyOrg");
    }

    /**
     * 获取党组织架构详细信息
     */
    @GetMapping(value = "/getPartyOrgs/{partyOrgIds}")
    public AjaxResult getInfo(@PathVariable("partyOrgIds") Long[] partyOrgIds)
    {

        List<DjPartyOrg> list = new ArrayList<DjPartyOrg>();
        for(Long partyOrgId:partyOrgIds){
            list.add(djPartyOrgService.selectDjPartyOrgById(partyOrgId));
        }
        return AjaxResult.success(list);
    }

    /**
     * 获取党组织架构详细信息
     */
    @GetMapping(value = "/{partyOrgId}")
    public AjaxResult getInfo(@PathVariable("partyOrgId") Long partyOrgId)
    {
        return AjaxResult.success(djPartyOrgService.selectDjPartyOrgById(partyOrgId));
    }

    /**
     * 获取党组织架构详细信息
     */
    @GetMapping(value = "/getPartyOrgArchives/{partyOrgId}")
    public AjaxResult getPartyOrgArchives(@PathVariable("partyOrgId") Long partyOrgId)
    {
        return AjaxResult.success(djPartyOrgService.getPartyOrgInfo(Long.valueOf(partyOrgId)));
    }

    /**
     * 获取党组织架构详细信息
     */
    @GetMapping(value = "/getPartyOrgInfo/{partyOrgId}")
    public AjaxResult getPartyOrgInfo(@PathVariable("partyOrgId") Long partyOrgId)
    {
        return AjaxResult.success(djPartyOrgService.getPartyOrgInfo(Long.valueOf(partyOrgId)));
    }

    /**
     * 新增党组织架构
     */
    @Log(title = "党组织架构", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DjPartyOrg djPartyOrg)
    {
        if (UserConstants.NOT_UNIQUE.equals(djPartyOrgService.checkPartyOrgNameUnique(djPartyOrg)))
        {
            return AjaxResult.error("新增党组织架构'" + djPartyOrg.getPartyOrgName() + "'失败，党组织架构名称已存在");
        }
        djPartyOrg.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());;
        djPartyOrg.setCreateTime(DateUtils.getNowDate());
        return toAjax(djPartyOrgService.insertDjPartyOrg(djPartyOrg));
    }

    /**
     * 修改党组织架构
     */
    @Log(title = "党组织架构", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjPartyOrg djPartyOrg)
    {
        if (UserConstants.NOT_UNIQUE.equals(djPartyOrgService.checkPartyOrgNameUnique(djPartyOrg)))
        {
            return AjaxResult.error("修改党组织架构'" + djPartyOrg.getPartyOrgName() + "'失败，党组织架构名称已存在");
        }
        else if (djPartyOrg.getParentId().equals(djPartyOrg.getPartyOrgId()))
        {
            return AjaxResult.error("修改党组织架构'" + djPartyOrg.getPartyOrgName() + "'失败，上级党组织架构不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, djPartyOrg.getStatus())
                && djPartyOrgService.selectNormalChildrenPartyOrgById(djPartyOrg.getPartyOrgId()) > 0)
        {
            return AjaxResult.error("该党组织架构包含未停用的子党组织架构！");
        }
        djPartyOrg.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djPartyOrg.setUpdateTime(DateUtils.getNowDate());
        return toAjax(djPartyOrgService.updateDjPartyOrg(djPartyOrg));
    }

    /**
     * 删除党组织架构
     */
    @Log(title = "党组织架构", businessType = BusinessType.DELETE)
	@DeleteMapping("/{partyOrgId}")
    public AjaxResult remove(@PathVariable Long partyOrgId)
    {
        if (djPartyOrgService.hasChildByPartyOrgId(partyOrgId))
        {
            return AjaxResult.error("存在下级党组织架构,不允许删除");
        }
        if (djPartyOrgService.checkOrgExistMember(partyOrgId))
        {
            return AjaxResult.error("党组织架构存在党员,不允许删除");
        }

        DjPartyOrg djPartyOrg = new DjPartyOrg();
        djPartyOrg.setPartyOrgId(partyOrgId);
        djPartyOrg.setDelFlag("1");

        return toAjax(djPartyOrgService.updateDjPartyOrg(djPartyOrg));
    }
}
