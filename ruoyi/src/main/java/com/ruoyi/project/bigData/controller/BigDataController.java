package com.ruoyi.project.bigData.controller;

import com.ruoyi.common.utils.BaiduMapUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.project.bigData.domain.DjBigWorkPlan;
import com.ruoyi.project.bigData.service.IDjBigWorkPlanService;
import com.ruoyi.project.party.service.IDjPartyMemberService;
import com.ruoyi.project.party.service.IDjPartyOrgService;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/bigDataShow")
public class BigDataController extends BaseController {

    @Autowired
    private IDjPartyOrgService djPartyOrgService;
    @Autowired
    private IDjPartyMemberService djPartyMemberService;
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;
    @Autowired
    private IDjBigWorkPlanService djBigWorkPlanService;
    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 活动数
     *
     * @return
     */
    @GetMapping("/count")
    public AjaxResult count() {
        return AjaxResult.success(djActivityDetailedService.count());
    }


    /**
     * 党支部地域分布
     *
     * @return
     */
    @GetMapping("/echarts_1")
    public AjaxResult echarts_1() {
        return AjaxResult.success(djPartyOrgService.echarts_1());
    }

    /**
     * 党支部地域分布
     *
     * @return
     */
    @GetMapping("/map")
    public AjaxResult map() {
        Map<String, Object> map = new HashMap<>();

        Map<String, Double[]> geoCoordMap = new HashMap<>();
        List<Map<String, Object>> dataList = djPartyOrgService.echarts_1();
        dataList.stream().forEach(dataMap -> {
            dataMap.put("value", Integer.parseInt(dataMap.get("value").toString()));
            String address = dataMap.get("name") + "市";
            Double[] coordinate = BaiduMapUtils.getCoordinate(address);
            geoCoordMap.put(dataMap.get("name").toString(), coordinate);
        });
        map.put("data", dataList);
        map.put("geoCoordMap", geoCoordMap);
        return AjaxResult.success(map);
    }


    /**
     * 各支部党员人数
     *
     * @return
     */
    @GetMapping("/echarts_2")
    public AjaxResult echarts_2() {
        return AjaxResult.success(djPartyOrgService.echarts_2());
    }

    /**
     * 各支部活动组织完成率排名
     *
     * @return
     */
    @GetMapping("/echarts_4")
    public AjaxResult echarts_4() {
        return AjaxResult.success(djActivityDetailedService.echarts_4());
    }

    /**
     * 党员年龄比例
     *
     * @return
     */
    @GetMapping("/echarts_31")
    public AjaxResult echarts_31() {
        return AjaxResult.success(djPartyMemberService.echarts_31());
    }

    /**
     * 男女比例
     *
     * @return
     */
    @GetMapping("/echarts_32")
    public AjaxResult echarts_32() {
        return AjaxResult.success(djPartyMemberService.echarts_32());
    }

    /**
     * 党员学历情况
     *
     * @return
     */
    @GetMapping("/echarts_33")
    public AjaxResult echarts_33() {
        return AjaxResult.success(djPartyMemberService.echarts_33());
    }


    @GetMapping("/yearWorkPlan")
    public AjaxResult yearWorkPlan() {
        String type = "1";
        Map<String, Object> map = new HashMap<>();
        String title = sysDictDataService.selectDictLabel("work_plan",type);
        map.put("title",title);

        DjBigWorkPlan bigWorkPlan = new DjBigWorkPlan();
        bigWorkPlan.setType(type);
        List<DjBigWorkPlan> workPlanList = djBigWorkPlanService.selectDjBigWorkPlanList(bigWorkPlan);
        Collections.sort(workPlanList, new Comparator<DjBigWorkPlan>() {
            @Override
            public int compare(DjBigWorkPlan o1, DjBigWorkPlan o2) {
                int i = (int) (o1.getOrderNum() - o2.getOrderNum());
                if(i == 0){
                    return (int) (o1.getOrderNum() - o2.getOrderNum());
                }
                return i;
            }
        });
        map.put("data",workPlanList);
        return AjaxResult.success(map);
    }

    @GetMapping("/monthWorkPlan")
    public AjaxResult monthWorkPlan() {
        String type = "2";
        Map<String, Object> map = new HashMap<>();
        String title = sysDictDataService.selectDictLabel("work_plan",type);
        map.put("title",title);

        DjBigWorkPlan bigWorkPlan = new DjBigWorkPlan();
        bigWorkPlan.setType(type);
        List<DjBigWorkPlan> workPlanList = djBigWorkPlanService.selectDjBigWorkPlanList(bigWorkPlan);
        Collections.sort(workPlanList, new Comparator<DjBigWorkPlan>() {
            @Override
            public int compare(DjBigWorkPlan o1, DjBigWorkPlan o2) {
                int i = (int) (o1.getOrderNum() - o2.getOrderNum());
                if(i == 0){
                    return (int) (o1.getOrderNum() - o2.getOrderNum());
                }
                return i;
            }
        });
        map.put("data",workPlanList);
        return AjaxResult.success(map);
    }

}
