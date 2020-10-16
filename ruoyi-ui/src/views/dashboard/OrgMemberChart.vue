<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getOrgMemberChartData } from "@/api/party/org";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '500px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      getOrgMemberChartData().then((response)=>{
        let data = response.data;
        let legendData = [];
        for (let i = 0; i < data.length; i++) {
          legendData.push(data[i].name);
        }
        this.chart.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 10,
            top: 20,
            bottom: 20,
            data: legendData,
          },
          toolbox: {
            show: true,
            feature: {
              dataView: {show: true, readOnly: false},
              saveAsImage: {show: true}
            }
          },
          series: [
            {
              name: '各党组织党员人数',
              type: 'pie',
              radius: '90%',
              center: ['40%', '50%'],
              data: data,
              animationEasing: 'cubicInOut',
              guan: 2600
            }
          ]

        })
      })


    }
  }
}
</script>
