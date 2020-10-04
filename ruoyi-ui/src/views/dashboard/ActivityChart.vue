<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'

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
        default: '350px'
      },
      autoResize: {
        type: Boolean,
        default: true
      },
      chartData: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        chart: null
      }
    },
    watch: {
      chartData: {
        deep: true,
        handler(val) {
          this.setOptions(val)
        }
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
        this.setOptions(this.chartData)
      },
      setOptions({ planData, actualData ,ingData,otherData } = {}) {
        this.chart.setOption({
          color: ['#45dedb', '#36a3f7', '#ff8e2c', '#f4516c'],
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            padding: [5, 10]
          },
          legend: {
            data: ['计划数', '已归档',"进行中","未启动"]
          },
          toolbox: {
            show: true,
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type:  ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          calculable: true,
          xAxis: [
            {
              type: 'category',
              data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']

            }
          ],
          grid: {
            left: 10,
            right: 10,
            bottom: 20,
            top: 30,
            containLabel: true
          },
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '计划数',
              type: 'bar',
              data: planData,
            },
            {
              name: '已归档',
              type: 'bar',
              data: actualData,
            },
            {
              name: '进行中',
              type: 'bar',
              data: ingData,
            },
            {
              name: '未启动',
              type: 'bar',
              data: otherData,
            }
          ]
        })
      }
    }
  }
</script>
