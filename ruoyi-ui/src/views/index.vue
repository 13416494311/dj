<template>
  <div class="dashboard-editor-container">

    <panel-group />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div>
        <span class="chart-title">活动召开情况</span>
        <el-select v-model="year" style="float:right" >
          <el-option
            v-for="dict in cycleYearOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
        <el-divider></el-divider>
        <activity-chart :chart-data="activityChartData" />
      </div>
    </el-row>


    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <div class="chart-wrapper">
        <span class="chart-title">各党组织党员比例</span>
        <el-divider></el-divider>
        <org-member-chart />
      </div>
    </el-row>

  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import ActivityChart from './dashboard/ActivityChart'
import OrgMemberChart from './dashboard/OrgMemberChart'
import { getActivityChartData } from "@/api/activity/detailed";

export default {
  name: 'Index',
  components: {
    PanelGroup,
    ActivityChart,
    OrgMemberChart,
  },
  data() {
    return {
      activityChartData: {
        planData: [],
        actualData: [],
        ingData: [],
        otherData: []
      },
      cycleYearOptions: [],
      year:undefined,
    }
  },
  created(){
    let year = new Date().getFullYear();
    this.year = year;
    this.setCycleYearOptions();
    this.getActivityChartData();
  },
  methods: {
    setCycleYearOptions() {
      let year = this.year;
      for (let i = 0; i <= (Number(year)-Number(2020)); i++) {
        let cycleYearOption = {};
        cycleYearOption.dictValue = Number(year) + Number(i);
        cycleYearOption.dictLabel = Number(year) + Number(i) + "年";
        this.cycleYearOptions.push(cycleYearOption);
      }
    },
    getActivityChartData(){
      let year = this.year;
      getActivityChartData(year).then((response)=>{
        this.activityChartData = response.data
      })


    },

  }
}
</script>

<style lang="scss" scoped>
  .chart-title{
    line-height: 18px;
    color: rgba(0,0,0,0.45);
    font-size: 16px;
    margin-bottom: 12px;
    font-weight: bold;
  }
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
