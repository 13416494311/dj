
$(function () {
  map();

})

function map() {
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('map_1'));
  var data =[];
    /*[
    {name: '广州', value: 200},
    {name: '深圳', value: 100},
    {name: '惠州', value: 200},
    {name: '汕头', value: 100},
    {name: '乐平', value: 100},
    {name: '海口', value: 100},
    {name: '东莞', value: 100},
    {name: '万安', value: 100},
    {name: '河源', value: 100}
  ];*/
  var geoCoordMap ={}
  /*{
    '广州':[113.23,23.16],
    '深圳':[114.07,22.62],
    '惠州':[114.4,23.09],
    '汕头':[116.69,23.39],
    '乐平':[117.15,28.98],
    '海口':[110.35,20.02],
    '东莞':[113.75,23.04],
    '万安':[114.77,26.46],
    '河源':[114.70,23.74]
  };*/

  $.ajax({type:'get',
    url:basePath + 'map',
    async:false, //同步
    data:{},
    success:function(res){
      console.log(res)
      if(res.code == 200){
        data = res.data.data
        geoCoordMap = res.data.geoCoordMap
      }
    }
  })

  var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
      var geoCoord = geoCoordMap[data[i].name];
      if (geoCoord) {
        res.push({
          name: data[i].name,
          value: geoCoord.concat(data[i].value)
        });
      }
    }
    return res;
  };

  option = {
    // backgroundColor: '#404a59',
    /***  title: {
        text: '实时行驶车辆',
        subtext: 'data from PM25.in',
        sublink: 'http://www.pm25.in',
        left: 'center',
        textStyle: {
            color: '#fff'
        }
    },**/
    tooltip : {
      trigger: 'item',
      formatter: function (params) {
        if(typeof(params.value)[2] == "undefined"){
          return params.name + ' : ' + params.value;
        }else{
          return params.name + ' : ' + params.value[2];
        }
      }
    },

    geo: {
      map: 'china',
      label: {
        emphasis: {
          show: false
        }
      },
      roam: false,//禁止其放大缩小
      itemStyle: {
        normal: {
          areaColor: '#4c60ff',
          borderColor: '#002097'
        },
        emphasis: {
          areaColor: '#293fff'
        }
      }
    },
    series : [
      {
        name: '组织机构数',
        type: 'scatter',
        coordinateSystem: 'geo',
        data: convertData(data),
        label: {
          normal: {
            formatter: '{b}',
            position: 'right',
            show: false
          },
          emphasis: {
            show: true
          }
        },
        itemStyle: {
          normal: {
            color: '#ffeb7b'
          }
        }
      }
      
    ]
  };

  myChart.setOption(option);
  window.addEventListener("resize",function(){
    myChart.resize();
  });
}
