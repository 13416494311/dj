<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
    <meta name="format-detection" content="telephone=no,email=no,date=no,address=no">
    <title>AUI快速完成布局</title>
    <link rel="stylesheet" type="text/css" href="../css/aui/aui.css"/>
</head>
<style>
    html,
    body {

    }

</style>
<body>
<section class="aui-content">
    <div class="aui-card-list">
        <div class="aui-card-list-header">
            各党组织党员比例
        </div>
        <div class="aui-card-list-content" id="orgMemberChart" style="width: 100%;height:400px;">

        </div>

    </div>

    <div class="aui-card-list">
        <div class="aui-card-list-header">
            活动召开情况
        </div>
        <div class="aui-card-list-content" id="activityChart" style="width: 100%;height:400px;">

        </div>

    </div>
</section>

</body>
<script type="text/javascript" src="../script/api.js"></script>
<script type="text/javascript" src="../script/request.js"></script>
<script type="text/javascript" src="../script/echarts/echarts.js"></script>

<script type="text/javascript">

    apiready = function () {
        api.parseTapmode();
        //
        initActivityChart();
        //各党组织党员比例  饼图
        initOrgMemberChart();

    }

    function initOrgMemberChart() {
        var orgMemberChart = echarts.init($api.byId('orgMemberChart'));
        fnPost('/party/org/getOrgMemberChartData', {}, 'get', true, function (ret, err) {
            if (ret.code == '200') {
                let data = ret.data;
                var option = {

                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
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
                            radius: '70%',
                            center: ['50%', '50%'],
                            data: data,
                            animationEasing: 'cubicInOut',
                            animationDuration: 2600
                        }
                    ]
                };
                orgMemberChart.setOption(option);
            } else {
                api.toast({
                    msg: '网络错误',
                    duration: 2000,
                    location: 'bottom'
                });
            }
        });
    }

    function initActivityChart() {
        var activityChart = echarts.init($api.byId('activityChart'));
        fnPost('/party/org/getOrgMemberChartData', {}, 'get', true, function (ret, err) {
            if (ret.code == '200') {
                let data = ret.data;
                var option = {
                    color: ['#45dedb', '#36a3f7', '#ff8e2c', '#f4516c'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        },
                        padding: [5, 10]
                    },
                    legend: {
                        data: ['计划数', '已归档', "进行中", "未启动"]
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
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
                };
                activityChart.setOption(option);
            } else {
                api.toast({
                    msg: '网络错误',
                    duration: 2000,
                    location: 'bottom'
                });
            }
        });

    }


</script>
</html>
