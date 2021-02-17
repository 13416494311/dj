
var basePath = "http://127.0.0.1/prod-api/bigDataShow/";
//var basePath = "http://202.96.185.154:5000/prod-api/" ;

$(function () {
  //活动数
  count();
  //党支部地域分布
  echarts_1();
  //各支部党员人数
  echarts_2();
  //各支部活动组织完成率排名
  echarts_4();
  //党员年龄比例
  echarts_31();
  //男女比例
  echarts_32();
  //党员学历情况
  echarts_33();
  //年度党建工作计划
  yearWorkPlan();
  //月度党建工作计划
  monthWorkPlan();
})

//党支部地域分布
function echarts_1() {
  let xData = [];//['广州','深圳','惠州','汕头','河源','海口','东莞','乐平','万安'];
  let yData = [];//[10, 2, 2, 1, 1, 1, 1, 1, 1];
  $.ajax({type:'get',
    url:basePath + 'echarts_1',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        xData = [];
        yData = [];
        let data = res.data;
        for (let i = 0; i < data.length; i++) {
          xData.push(data[i].name);
          yData.push(data[i].value);
        }
      }
    }
  })

  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echart1'));

  option = {
    //  backgroundColor: '#00265f',

    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '0%',
      top:'16Px',
      right: '0%',
      bottom: '0%',
      containLabel: true
    },
    xAxis: [{
      type: 'category',
      data: xData,
      axisLine: {
        show: false,
        lineStyle: {
          color: "rgba(255,255,255,.1)",
          width: 1,
          type: "solid"
        },
      },

      axisTick: {
        show: true,
      },
      axisLabel:  {
        interval: 0,
        // rotate:50,
        show: true,
        splitNumber: 15,
        textStyle: {
          color: "rgba(255,255,255,.6)",
          fontSize: '12',
        },
      },
    }],
    yAxis: [{
      type: 'value',
      axisLabel: {
        //formatter: '{value} %'
        show:false,
        textStyle: {
          color: "rgba(255,255,255,.6)",
          fontSize: '12',
        },
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: "rgba(255,255,255,.1	)",
          width: 1,
          type: "solid"
        },
      },
      splitLine: {
        lineStyle: {
          color: "rgba(255,255,255,.1)",
        }
      }
    }],
    series: [
      {
        type: 'bar',
        data: yData,
        barWidth:'35%', //柱子宽度
        // barGap: 1, //柱子之间间距
        itemStyle: {
          normal: {
            color:'#27d08a',
            opacity: 1,
            barBorderRadius: 4,
            label: {
              show: true,		//开启显示
              position: 'top',	//在上方显示
              textStyle: {	    //数值样式
                color: 'write',
                fontSize: 11
              }
            }
          },
          label: {
            show: true,
            textStyle: {
              color: 'black',
              fontSize: 16
            }
          }
        },
        animationDuration:8000
      }

    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}
//各支部党员人数
function echarts_2() {
  let xData = [] //['机关一支部', '机关二支部', '第一工程段党支部', '第二工程段党支部', '第三工程段党支部','第四工程段党支部','第五工程段党支部','第六工程段党支部','机械段党支部','市政工程段党支部','东莞地铁1号线项目党支部','综合管廊项目党支部','赣深2标项目党支部','惠州分公司党支部','深圳市政项目党支部','城轨工程第一分公司党支部','水务分公司党支部','广湛项目党支部'];
  let yData = [] //[28,24,13,8,9,9,6,7,6,8,7,8,11,20,4,16,5,7];
  $.ajax({type:'get',
    url:basePath + 'echarts_2',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        xData = [];
        yData = [];
        let data = res.data;
        for (let i = 0; i < data.length; i++) {
          xData.push(data[i].name);
          yData.push(data[i].value);
        }
      }
    }
  })

  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echart2'));

  option = {
    //  backgroundColor: '#00265f',
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow'}
    },
    grid: {
      left: '0%',
      top:'17px',
      right: '0%',
      bottom: '0%',
      containLabel: true
    },
    xAxis: [{
      type: 'category',
      data: xData,

      axisLine: {
        show: true,
        lineStyle: {
          color: "rgba(255,255,255,.1)",
          width: 1,
          type: "solid"
        },
      },

      axisTick: {
        show: false,
      },
      axisLabel:  {
        interval: 0,
        // rotate:50,
        show: true,
        splitNumber: 15,
        textStyle: {
          color: "rgba(255,255,255,.6)",
          fontSize: '12',
        },
        formatter: function (value) {
          //x轴的文字改为竖版显示
          var str = value.split("");
          return str.join("\n");
        },
      },
    }],
    yAxis: [{
      type: 'value',
      axisLabel: {
        //formatter: '{value} %'
        show:false,
        textStyle: {
          color: "rgba(255,255,255,.6)",
          fontSize: '12',
        },
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: "rgba(255,255,255,.1	)",
          width: 1,
          type: "solid"
        },
      },
      splitLine: {
        lineStyle: {
          color: "rgba(255,255,255,.1)",
        }
      }
    }],
    series: [
      {

        type: 'bar',
        data: yData,
        barWidth:'85%', //柱子宽度
        // barGap: 1, //柱子之间间距
        itemStyle: {
          normal: {
            color:'#27d08a',
            opacity: 1,
            barBorderRadius: 4,
            label: {
              show: true,		//开启显示
              position: 'top',	//在上方显示
              textStyle: {	    //数值样式
                color: 'write',
                fontSize: 8
              }
            }
          },
        },
        animationDuration:8000
      }

    ]
  };

  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}
//各支部活动组织完成率排名
function echarts_4() {

  let xData = []// ['第一工程段党支部','第二工程段党支部','第四工程段党支部','机械段党支部','综合管廊项目党支部','赣深2标项目党支部','惠州分公司党支部','第五工程段党支部','机关一支部','城轨工程第一分公司党支部','机关二支部','第六工程段党支部','市政工程段党支部','东莞地铁1号线项目党支部','深圳市政项目党支部','水务分公司党支部'];
  let yData = []// [100,100,100,100,100,100,100,96,60,60,57,54,48,45,24,24];
  $.ajax({type:'get',
    url:basePath + 'echarts_4',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      xData = [];
      yData = [];
      if(res.code == 200){
        let data = res.data;
        for (let i = 0; i < data.length; i++) {
          xData.push(data[i].name);
          yData.push(data[i].value);
        }
      }
    }
  })

  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echart4'));

  option = {
    //  backgroundColor: '#00265f',
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow'}
    },
    grid: {
      left: '0%',
      top:'17px',
      right: '0%',
      bottom: '0%',
      containLabel: true
    },
    xAxis: [{
      type: 'category',
      data: xData,

      axisLine: {
        show: true,
        lineStyle: {
          color: "rgba(255,255,255,.1)",
          width: 1,
          type: "solid"
        },
      },

      axisTick: {
        show: false,
      },
      axisLabel:  {
        interval: 0,
        // rotate:50,
        show: true,
        splitNumber: 15,
        textStyle: {
          color: "rgba(255,255,255,.6)",
          fontSize: '12',
        },
        formatter: function (value) {
          //x轴的文字改为竖版显示
          var str = value.split("");
          return str.join("\n");
        },
      },
    }],
    yAxis: [{
      type: 'value',
      axisLabel: {
        //formatter: '{value} %'
        show:false,
        textStyle: {
          color: "rgba(255,255,255,.6)",
          fontSize: '12',
        },
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: "rgba(255,255,255,.1	)",
          width: 1,
          type: "solid"
        },
      },
      splitLine: {
        lineStyle: {
          color: "rgba(255,255,255,.1)",
        }
      }
    }],
    series: [
      {

        type: 'bar',
        data: yData ,
        barWidth:'85%', //柱子宽度
        barGap: 2, //柱子之间间距
        itemStyle: {
          normal: {
            color:'#27d08a',
            opacity: 1,
            barBorderRadius: 4,
            label: {
              show: true,		//开启显示
              position: 'top',	//在上方显示
              //formatter:'{c}%',
              textStyle: {	    //数值样式
                color: 'write',
                fontSize: 6
              }
            }
          },
        },
        animationDuration:8000
      }

    ]
  };

  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}
//党员年龄比例
function echarts_31() {
  let legendData =  []//['30岁以下','30-39岁','40-49岁','50岁以上'];
  let seriesData = []// [ {value:37, name:'30岁以下'}, {value:48, name:'30-39岁'}, {value:79, name:'40-49岁'},{value:32, name:'50岁以上'},];
  $.ajax({type:'get',
    url:basePath + 'echarts_31',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        seriesData = res.data;
        legendData =[];
        for (let i = 0; i < seriesData.length; i++) {
          legendData.push(seriesData[i].name);
        }
      }
    }
  })
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('fb1'));
  option = {
    title: [{
      text: '党员年龄比例',
      left: 'center',
      textStyle: {
        color: '#fff',
        fontSize:'15'
      }

    }],
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b}: {c} ({d}%)",
      position:function(p){   //其中p为当前鼠标的位置
        return [p[0] + 10, p[1] - 10];
      }
    },
    legend: {
      top:'50%',
      itemWidth: 10,
      itemHeight: 10,
      data:legendData ,
      textStyle: {
        color: 'rgba(255,255,255,.5)',
        fontSize:'12',
      }
    },
    series: [
      {
        name:'年龄分布',
        type:'pie',
        center: ['50%', '30%'],
        radius: ['40%', '60%'],
        color: ['#066eab', '#0682ab', '#0696ab', '#06a0ab'],
        label: {show:false},
        labelLine: {show:false},
        data:seriesData,
        animationDuration:8000
      }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}
//男女比例
function echarts_32() {
  let legendData = []// ['男性','女性'];
  let seriesData =  [];// [ {value:159, name:'男性'}, {value:37, name:'女性'}, ];
  $.ajax({type:'get',
    url:basePath + 'echarts_32',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        seriesData = res.data;
        legendData =[];
        for (let i = 0; i < seriesData.length; i++) {
          legendData.push(seriesData[i].name);
        }
      }
    }
  })

  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('fb2'));
  option = {

    title: [{
      text: '男女比例',
      left: 'center',
      textStyle: {
        color: '#fff',
        fontSize:'15'
      }

    }],
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b}: {c} ({d}%)",
      position:function(p){   //其中p为当前鼠标的位置
        return [p[0] + 10, p[1] - 10];
      }
    },
    legend: {

      top:'50%',
      itemWidth: 10,
      itemHeight: 10,
      data:legendData ['男性','女性'],
      textStyle: {
        color: 'rgba(255,255,255,.5)',
        fontSize:'12',
      }
    },
    series: [
      {
        name:'男女分布',
        type:'pie',
        center: ['50%', '30%'],
        radius: ['40%', '60%'],
        color: ['#065aab', '#066eab'],
        label: {show:false},
        labelLine: {show:false},
        data:seriesData,
        animationDuration:8000
      }
    ]
  };

  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}
//党员学历情况
function echarts_33() {
  let legendData =  [] ;//['本科以上','本科','大专中专','高中以下'];
  let seriesData =  [];  //[  {value:14, name:'本科以上'}, {value:105, name:'本科'}, {value:62, name:'大专中专'}, {value:15, name:'高中以下'}];
  $.ajax({type:'get',
    url:basePath + 'echarts_33',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        seriesData = res.data;
        legendData =[];
        for (let i = 0; i < seriesData.length; i++) {
          legendData.push(seriesData[i].name);
        }
      }
    }
  })

  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('fb3'));
  option = {
    title: [{
      text: '党员学历情况',
      left: 'center',
      textStyle: {
        color: '#fff',
        fontSize:'15'
      }

    }],
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b}: {c} ({d}%)",
      position:function(p){   //其中p为当前鼠标的位置
        return [p[0] + 10, p[1] - 10];
      }
    },
    legend: {
      top:'50%',
      itemWidth: 10,
      itemHeight: 10,
      data:legendData ,
      textStyle: {
        color: 'rgba(255,255,255,.5)',
        fontSize:'12',
      }
    },
    series: [
      {
        name:'学历分布',
        type:'pie',
        center: ['50%', '30%'],
        radius: ['40%', '60%'],
        color: ['#065aab', '#066eab', '#0682ab', '#0696ab'],
        label: {show:false},
        labelLine: {show:false},
        data:seriesData,
        animationDuration:8000
      }
    ]
  };


  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}

//活动数
function count(){
  $.ajax({type:'get',
    url:basePath + 'count',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        $("#finishedCount").html(res.data.finishedCount)
        $("#allCount").html(res.data.allCount)
      }
    }
  })
}

// 年度党建工作计划
function yearWorkPlan(){
  var title = "" // 年度党建工作计划
  var data = [];//
  $.ajax({type:'get',
    url:basePath + 'yearWorkPlan',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        title = res.data.title
        data = res.data.data
      }
    }
  })

  let html = "<div class=\"alltitle\">"+title+"</div>"
  for(var i in data){
    html+= "<div class=\"alltitle1\">"+data[i].content+"</div>"
  }
  $("#yearWorkPlan").html(html);

}

// 月度党建工作计划
function monthWorkPlan(){
  var title = "" // 年度党建工作计划
  var data = [];//
  $.ajax({type:'get',
    url:basePath + 'monthWorkPlan',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        title = res.data.title
        data = res.data.data
      }
    }
  })

  let html = "<div class=\"alltitle\">"+title+"</div>"
  for(var i in data){
    html+= "<div class=\"alltitle1\">"+data[i].content+"</div>"
  }
  $("#monthWorkPlan").html(html);

}






