
var daySecs=24*60*60*1000;
var hourSecs=60*60*1000;
var minSecs=60*1000;

var startWorkBefNoon = 9;
var endWorkBefNoon = 12;
var startWorkAftNoon = 13;
var endWorkAftNoon = 18;


//精确到分
export function getDifDate(date1,date2){


  if(date1>date2)return 0;

  var dateF=new Date(date1);
  var dateN=new Date(date2);

  var validTime=dateN-dateF;
  var totalTime=0;
  console.log("总的时间差："+validTime);

  //前段时间的时和分
  var dateFH=dateF.getHours();
  var dateFM=dateF.getMinutes();
  //后段时间的时和分
  var dateNH=dateN.getHours();
  var dateNM=dateN.getMinutes();

  var time=0;

  while (Math.floor(dateF.getTime()/minSecs)<Math.floor(dateN.getTime()/minSecs)){
    //前段时间的时和分
    dateFH=dateF.getHours();
    dateFM=dateF.getMinutes();
    //后段时间的时和分
    dateNH=dateN.getHours();
    dateNM=dateN.getMinutes();
    validTime=dateN-dateF;
    /*if(dateN.getDay()==0||dateN.getDay()==6){//结束时间是周末
      time=dateNH*hourSecs+dateNM*minSecs;
      dateN.setTime(dateN.getTime()-time-6*hourSecs-minSecs);
      totalTime+=minSecs;
      continue;
    }

    time=daySecs-dateFH*hourSecs-dateFM*minSecs;

    if(dateF.getDay()==0||dateF.getDay()==6){//开始时间是周末
      dateF.setTime(dateF.getTime()+time+startWorkBefNoon*hourSecs);
      continue;
    }*/

    if(Math.abs(dateF.getDate()-dateN.getDate())>=1){//跨天

      if(dateF.getHours()<startWorkBefNoon){//九点前开始
        totalTime+=8*hourSecs;
        dateF.setTime(dateF.getTime()+time+startWorkBefNoon*hourSecs);
        continue;
      }

      if(dateF.getHours()>=startWorkBefNoon&&dateF.getHours()<endWorkBefNoon){//上午开始
        totalTime+=endWorkAftNoon*hourSecs-(dateFH*hourSecs+dateFM*minSecs+hourSecs);
        dateF.setTime(dateF.getTime()+time+startWorkBefNoon*hourSecs);
        continue;
      }

      if(dateF.getHours()>=endWorkBefNoon&&dateF.getHours()<startWorkAftNoon){//中午休息时间开始
        totalTime+=5*hourSecs;
        dateF.setTime(dateF.getTime()+time+startWorkBefNoon*hourSecs);
        continue;
      }

      if(dateF.getHours()>=startWorkAftNoon&&dateF.getHours()<endWorkAftNoon){//下午开始
        totalTime+=endWorkAftNoon*hourSecs-(dateFH*hourSecs+dateFM*minSecs);
        dateF.setTime(dateF.getTime()+time+startWorkBefNoon*hourSecs);
        continue;
      }
      if(dateF.getHours()>=endWorkAftNoon){//加班时间
        dateF.setTime(dateF.getTime()+time+startWorkBefNoon*hourSecs);
      }
    }else {//不跨天

      if(judgeSection(dateNH,dateNM)>startWorkBefNoon&&judgeSection(dateFH,dateFM)<=endWorkAftNoon){

        if(dateFH<startWorkBefNoon&&judgeSection(dateNH,dateNM)<=endWorkBefNoon){ //startWorkBefNoon-endWorkBefNoon
          totalTime+=(dateNH*hourSecs+dateNM*minSecs)-startWorkBefNoon*hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if(dateFH<startWorkBefNoon&&judgeSection(dateNH,dateNM)<=startWorkAftNoon){//startWorkBefNoon-startWorkAftNoon
          totalTime+=3*hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if(dateFH<startWorkBefNoon&&judgeSection(dateNH,dateNM)<=endWorkAftNoon){//startWorkBefNoon-endWorkAftNoon
          totalTime+=(dateNH*hourSecs+dateNM*minSecs)-10*hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }
        if(dateFH<startWorkBefNoon&&judgeSection(dateNH,dateNM)>endWorkAftNoon){//startWorkBefNoon-加班
          totalTime+=8*hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if((dateFH>=startWorkBefNoon&&judgeSection(dateNH,dateNM)<=endWorkBefNoon)||(dateF.getHours()>=startWorkAftNoon&&judgeSection(dateNH,dateNM)<=endWorkAftNoon)){
          totalTime+=(dateNH*hourSecs+dateNM*minSecs)-(dateFH*hourSecs+dateFM*minSecs);
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if(dateFH>=startWorkBefNoon&&judgeSection(dateNH,dateNM)<=startWorkAftNoon){ //startWorkBefNoon-endWorkBefNoon
          totalTime+=endWorkBefNoon*hourSecs-(dateFH*hourSecs+dateFM*minSecs);
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if(dateFH>=startWorkBefNoon&&dateFH<=endWorkBefNoon&&judgeSection(dateNH,dateNM)<=endWorkAftNoon){
          totalTime+=(dateNH*hourSecs+dateNM*minSecs)-(dateFH*hourSecs+dateFM*minSecs)-hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if(dateFH>=startWorkBefNoon&&dateFH<=endWorkBefNoon&&judgeSection(dateNH,dateNM)>endWorkAftNoon){
          totalTime+=endWorkAftNoon*hourSecs-(dateFH*hourSecs+dateFM*minSecs)-hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }

        if(dateFH>=endWorkBefNoon&&dateFH<startWorkAftNoon&&judgeSection(dateNH,dateNM)<=endWorkAftNoon&&judgeSection(dateNH,dateNM)>startWorkAftNoon){//startWorkAftNoon-endWorkAftNoon
          totalTime+=(dateNH*hourSecs+dateNM*minSecs)-startWorkAftNoon*hourSecs;
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }
        if(dateFH>=startWorkAftNoon&&judgeSection(dateNH,dateNM)<=endWorkAftNoon){
          totalTime+=(dateNH*hourSecs+dateNM*minSecs)-(dateFH*hourSecs+dateFM*minSecs);
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }
        if(dateFH>=startWorkAftNoon&&judgeSection(dateNH,dateNM)>endWorkAftNoon){
          totalTime+=endWorkAftNoon*hourSecs-(dateFH*hourSecs+dateFM*minSecs);
          dateF.setTime(dateF.getTime()+daySecs);
          continue;
        }
      }
      dateF.setTime(dateF.getTime()+daySecs);//非工作区间
    }
  }
  return Math.floor(totalTime/minSecs) ;
}

function judgeSection(hours,mins) {
  var section=hours*hourSecs+mins*minSecs;
  return Math.ceil(section/hourSecs);
}
