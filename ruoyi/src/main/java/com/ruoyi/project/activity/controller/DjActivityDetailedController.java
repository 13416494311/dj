package com.ruoyi.project.activity.controller;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FileUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WordUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.activity.domain.*;
import com.ruoyi.project.activity.service.*;
import com.ruoyi.project.system.domain.SysFile;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysFileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 活动详情Controller
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@RestController
@RequestMapping("/activity/detailed")
public class DjActivityDetailedController extends BaseController
{
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;
    @Autowired
    private IDjActivityPlanService djActivityPlanService;
    @Autowired
    private IDjActivitySummaryService djActivitySummaryService;
    @Autowired
    private IDjActivityResolutionService djActivityResolutionService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IDjActivityMemberService djActivityMemberService;
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 查询活动详情列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivityDetailed djActivityDetailed)
    {
        startPage();
        List<DjActivityDetailed> list = djActivityDetailedService.selectDjActivityDetailedList(djActivityDetailed);
        return getDataTable(list);
    }

    /**
     * 查询活动详情列表
     */
    @GetMapping("/listByParam")
    @DataScope(partyOrgAlias = "detailed")
    public TableDataInfo list(DjActivityParams params)
    {
        startPage();
        List<DjActivityDetailed> list = djActivityDetailedService.selectDetailedListByParam(params);
        return getDataTable(list);
    }


    @PostMapping("/listByParamForApp")
    @DataScope(partyOrgAlias = "detailed")
    public AjaxResult listByParamForApp(@RequestBody DjActivityParams params)
    {
        if (StringUtils.isNotNull(params.getPageNum()) && StringUtils.isNotNull(params.getPageSize()))
        {
            PageHelper.startPage(params.getPageNum(), params.getPageSize());
        }
        List<DjActivityDetailed> list = djActivityDetailedService.selectDetailedListByParam(params);
        return AjaxResult.success(list);
    }

    /**
     * 导出活动详情列表
     */
    @Log(title = "活动详情", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityDetailed djActivityDetailed)
    {
        List<DjActivityDetailed> list = djActivityDetailedService.selectDjActivityDetailedList(djActivityDetailed);
        ExcelUtil<DjActivityDetailed> util = new ExcelUtil<DjActivityDetailed>(DjActivityDetailed.class);
        return util.exportExcel(list, "detailed");
    }




    /**
     * 获取活动详情详细信息
     */
    @GetMapping(value = "/{detailedId}")
    public AjaxResult getInfo(@PathVariable("detailedId") Long detailedId)
    {
        return AjaxResult.success(djActivityDetailedService.selectDjActivityDetailedById(detailedId));
    }

    /**
     * 获取活动详情详细信息
     */
    @GetMapping(value = "/getByUuid/{detailedUuid}")
    public AjaxResult getByUuid(@PathVariable("detailedUuid") String detailedUuid)
    {
        return AjaxResult.success(djActivityDetailedService.selectDjActivityDetailedByDetailedUuid(detailedUuid));
    }

    /**
     * 新增活动详情
     */
    @Log(title = "活动详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityDetailed djActivityDetailed)
    {
        return toAjax(djActivityDetailedService.insertDjActivityDetailed(djActivityDetailed));
    }

    /**
     * 修改活动详情
     */
    @Log(title = "活动详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityDetailed djActivityDetailed)
    {
        return toAjax(djActivityDetailedService.updateDjActivityDetailed(djActivityDetailed));
    }

    /**
     * 删除活动详情
     */
    @Log(title = "活动详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailedIds}")
    public AjaxResult remove(@PathVariable Long[] detailedIds)
    {
        return toAjax(djActivityDetailedService.deleteDjActivityDetailedByIds(detailedIds));
    }

    @Log(title = "档案下载", businessType = BusinessType.EXPORT)
    @GetMapping("/exportArchives")
    public void exportArchives(HttpServletRequest request, HttpServletResponse response, String detailedId) throws IOException
    {
        Map<String, Object> dataMap = new HashMap<>();
        DjActivityDetailed activityDetailed = djActivityDetailedService.
                selectDjActivityDetailedById(Long.parseLong(detailedId));

        DjActivityPlan activityPlan =djActivityPlanService.
                selectDjActivityPlanByPlanUuid(activityDetailed.getPlanUuid());
        String activityTypeText = dictDataService.selectDictLabel("activity_type",activityPlan.getActivityType());
        dataMap.put("planTypeText",activityTypeText);
        dataMap.put("planActivityTheme",activityPlan.getActivityTheme());

        dataMap.put("detailedActualStartTime",
                DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,activityDetailed.getActualStartTime()));
        dataMap.put("detailedActualEndTime",
                DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,activityDetailed.getActualEndTime()));
        dataMap.put("detailedVenue",activityDetailed.getVenue()==null?"":activityDetailed.getVenue());
        dataMap.put("detailedPresenter",activityDetailed.getPresenter()==null?"":activityDetailed.getPresenter());
        dataMap.put("detailedRecorder",activityDetailed.getRecorder()==null?"":activityDetailed.getRecorder());
        dataMap.put("detailedMentors",activityDetailed.getMentors()==null?"":activityDetailed.getMentors());

        DjActivityMember activityMember = new DjActivityMember();
        activityMember.setDetailedUuid(activityDetailed.getDetailedUuid());
        List<DjActivityMember> memberList = djActivityMemberService.selectDjActivityMemberList(activityMember);
        dataMap.put("memberTotal",memberList.size());
        StringBuilder memberNames= new StringBuilder();
        StringBuilder memberLeaveNames= new StringBuilder();
        StringBuilder memberTruancyNames= new StringBuilder();
        StringBuilder memberLateNames= new StringBuilder();
        final int[] memberActualNum = {0};
        final int[] memberLateNum = { 0 };
        memberList.stream().forEach(member->{
            memberNames.append(member.getDjPartyMember().getMemberName()+"、");
            switch (member.getStatus()){
                case "1" :
                    memberTruancyNames.append(member.getDjPartyMember().getMemberName()+"、");
                    break;
                case "2" :
                    memberActualNum[0] += 1;
                break;
                case "3" :
                    memberLateNum[0] += 1;
                    memberLateNames.append(member.getDjPartyMember().getMemberName()+"、");
                    break;
                case "4" :
                    memberActualNum[0] += 1;
                    break;
                case "5" :
                    memberLeaveNames.append(member.getDjPartyMember().getMemberName()+"、");
                    break;
                case "6" :
                    memberTruancyNames.append(member.getDjPartyMember().getMemberName()+"、");
                    break;
                default:break;
            }
        });
        dataMap.put("memberActualNum",memberActualNum[0]);
        NumberFormat nt = NumberFormat.getPercentInstance();//获取格式化对象
        nt.setMinimumFractionDigits(0);//设置百分数精确度2即保留两位小数
        dataMap.put("memberAttendanceRate",nt.format((float)memberActualNum[0]/memberList.size()));
        dataMap.put("memberLateNum",memberLateNum[0]);


        dataMap.put("memberNames","".equals(memberNames.toString())?"":memberNames.toString().substring(0,memberNames.toString().length()-1));
        dataMap.put("memberLeaveNames","".equals(memberLeaveNames.toString())?"":memberLeaveNames.toString().substring(0,memberLeaveNames.toString().length()-1));
        dataMap.put("memberTruancyNames","".equals(memberTruancyNames.toString())?"":memberTruancyNames.toString().substring(0,memberTruancyNames.toString().length()-1));
        dataMap.put("memberLateNames","".equals(memberLateNames.toString())?"":memberLateNames.toString().substring(0,memberLateNames.toString().length()-1));

        DjActivitySummary summary = new DjActivitySummary();
        summary.setDetailedUuid(activityDetailed.getDetailedUuid());
        List<DjActivitySummary> summaryList = djActivitySummaryService.selectDjActivitySummaryList(summary);
        final int[] activitySummaryIndex = {0};
        summaryList.stream().forEach(activitySummary->{
            activitySummaryIndex[0] +=1;
            activitySummary.setRecordContent(activitySummaryIndex[0]+"、"+activitySummary.getRecordContent());
        });
        dataMap.put("summaryList",summaryList);
        DjActivityResolution resolution = new DjActivityResolution();
        resolution.setDetailedUuid(activityDetailed.getDetailedUuid());
        List<DjActivityResolution> resolutionList = djActivityResolutionService.selectDjActivityResolutionList(resolution);
        dataMap.put("resolutionList",resolutionList);
        final int[] activityResolutionIndex = {0};
        resolutionList.stream().forEach(activityResolution->{
            activityResolutionIndex[0]+=1;
            activityResolution.setRecordContent(activityResolutionIndex[0]+"、"+activityResolution.getRecordContent());
        });

        SysFile sysFile = new SysFile();
        sysFile.setUuid(activityDetailed.getDetailedUuid());
        sysFile.setFileTypeValue("pic");
        List<SysFile> activityPicSysFileList = sysFileService.selectSysFileList(sysFile);

        List<File> activityPicList = new ArrayList<File>();
        final int[] index = {0};
        activityPicSysFileList.stream().forEach(activityPicSysFile -> {
            File file = new File(RuoYiConfig.getProfile()+
                    activityPicSysFile.getFilePath().replace(Constants.RESOURCE_PREFIX,""));
            byte[] fileByte = FileUtil.getBytesByFile(file);
            String fileName = "image-"+index[0]+".png";  //不能中文
            File targetFile = FileUtil.byteToFile(fileByte,fileName);
            activityPicList.add(targetFile);
            index[0]+=1;
        });
        dataMap.put("activityPicList", activityPicList);

        File docx = null ;
        try {
            String docxName = activityPlan.getActivityTheme() + System.currentTimeMillis() + ".docx";
            docx = new File(docxName);
            WordUtils.createWordDocx("activityDetailed/template.xml",dataMap,
                    "activityDetailed/template.xml.rels", activityPicList,"activityDetailed/template.docx",docx);
            download(request,response, docx);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if(docx.exists()){
                docx.delete();
            }
        }

    }

    /**
     * 生成zip文件
     */
    private void download(HttpServletRequest request,HttpServletResponse response, File file) throws IOException
    {
        byte[] data= FileUtil.readFileByBytes(file);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename="
                + FileUtils.setFileDownloadHeader(request,file.getName()));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
