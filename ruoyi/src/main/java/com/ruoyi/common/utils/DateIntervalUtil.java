package com.ruoyi.common.utils;

import com.ruoyi.project.activity.domain.DateInterval;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

public class DateIntervalUtil {


    /**
     * 计算年初年末
     * @param startYear
     * @param endYear
     */
    public static List<DateInterval> getYearDateInterval(int startYear, int endYear ){
        if(startYear>endYear){
            return null ;
        }
        Calendar currentMonth = Calendar.getInstance();
        List<DateInterval> list = new ArrayList<DateInterval>();
        for(int i=startYear;i<=endYear;i++){
            DateInterval dateInterval = new DateInterval();
            currentMonth.set(i,1,0);
            currentMonth.set(currentMonth.get(Calendar.YEAR), 0, 1,0,0,0);
            dateInterval.setStartDate(currentMonth.getTime());
            currentMonth.set(currentMonth.get(Calendar.YEAR)+1, 0, 0,0,0,0);
            dateInterval.setEndDate(currentMonth.getTime());
            list.add(dateInterval);
        }
        return list;
    }

    /**
     * 获得月初月末
     *
     * 方法	获得月初月末,算法思路
     *
     *月初的日字段设为1,得到月初
     *月份先加一个月,得到下个月
     *在此基础上-1天,即日字段设为 1-1 = 0,得到本月月末
     *
     *此处Java容错性强大,一般日期设置不正常也不会错,会自动调整
     *
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     */
    public static List<DateInterval> getMonthDateInterval(int startYear,int startMonth,int endYear,int endMonth ){
        if(startYear>endYear){
            return null ;
        }
        if(startYear==endYear && startMonth>endMonth){
            return null ;
        }
        Calendar currentMonth = Calendar.getInstance();
        List<DateInterval> list = new ArrayList<DateInterval>();
        if(startYear==endYear){
            for(int i=startMonth;i<=endMonth;i++){
                DateInterval dateInterval = new DateInterval();
                currentMonth.set(startYear,i-1,1,0,0,0);
                dateInterval.setStartDate(currentMonth.getTime());
                currentMonth.set(startYear,i,0,0,0,0);
                dateInterval.setEndDate(currentMonth.getTime());
                list.add(dateInterval);
            }
        }else{
            for(int i=startYear;i<=endYear;i++){
                int start,end;
                if(i==endYear){
                    start= 1;end = endMonth;
                }else if(i==startYear){
                    start= startMonth;end = 12;
                }else{
                    start= 1;end = 12;
                }
                for(int j=start;j<=end;j++){
                    DateInterval dateInterval = new DateInterval();
                    currentMonth.set(i,j-1,1,0,0,0);
                    dateInterval.setStartDate(currentMonth.getTime());
                    currentMonth.set(i,j,0,0,0,0);
                    dateInterval.setEndDate(currentMonth.getTime());
                    list.add(dateInterval);
                }

            }
        }
        return list;
    }

    /**
     * 计算季度初季度末
     * @param startYear
     * @param startQuarter
     * @param endYear
     * @param endQuarter
     */
    public static List<DateInterval> getQuarterDateInterval(int startYear,int startQuarter,int endYear,int endQuarter ){
        if(startYear>endYear){
            return null ;
        }
        if(startYear==endYear && startQuarter>endQuarter){
            return null ;
        }
        Calendar currentMonth = Calendar.getInstance();
        List<DateInterval> list = new ArrayList<DateInterval>();

        if(startYear==endYear){
            for(int i=startQuarter;i<=endQuarter;i++){
                DateInterval dateInterval = new DateInterval();
                currentMonth.set(startYear,3*(i-1),1,0,0,0);
                dateInterval.setStartDate(currentMonth.getTime());
                currentMonth.set(startYear,3*i,0,0,0,0);
                dateInterval.setEndDate(currentMonth.getTime());
                list.add(dateInterval);
            }
        }else{
            for(int i=startYear;i<=endYear;i++){
                int start,end;
                if(i==endYear){
                    start= 1;end = endQuarter;
                }else if(i==startYear){
                    start= startQuarter;end = 4;
                }else{
                    start= 1;end = 4;
                }
                for(int j=start;j<=end;j++){
                    DateInterval dateInterval = new DateInterval();
                    currentMonth.set(i,3*(j-1),1,0,0,0);
                    dateInterval.setStartDate(currentMonth.getTime());
                    currentMonth.set(i,3*j,0,0,0,0);
                    dateInterval.setEndDate(currentMonth.getTime());
                    list.add(dateInterval);
                }

            }
        }
        return list;
    }

    /**
     * 计算时间区间内一周时间
     * @param dataBegin
     * @param dataEnd
     * @return
     */
    public static List<DateInterval> getWeekDateInterval(Date dataBegin, Date dataEnd) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dataBegin);
        int startYear = cal.get(Calendar.YEAR);

        cal.setMinimalDaysInFirstWeek(7);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int startWeek = cal.get(Calendar.WEEK_OF_YEAR);

        cal.setTime(dataEnd);
        int endYear = cal.get(Calendar.YEAR);

        cal.setMinimalDaysInFirstWeek(7);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int endWeek = cal.get(Calendar.WEEK_OF_YEAR);

        if(endWeek==52){
            int endYearMonth = cal.get(Calendar.MONTH);
            if(endYearMonth==0){
                endYear=endYear-1;
            }
        }
        if(startYear>endYear){return null;}
        if(startYear==endYear&&startWeek>endWeek){return null ;}
        List<DateInterval> list = new ArrayList<DateInterval>();
        if(startYear==endYear){  //未跨年
            for(int i=startWeek;i<=endWeek;i++){
                DateInterval dateInterval = new DateInterval();
                if(startWeek==endWeek){
                    dateInterval.setStartDate(dataBegin);
                    dateInterval.setEndDate(dataEnd);
                }else if(i==startWeek){
                    dateInterval.setStartDate(dataBegin);
                    cal.set(Calendar.YEAR, startYear);
                    cal.set(Calendar.WEEK_OF_YEAR, i);
                    cal.set(Calendar.DAY_OF_WEEK, 1);
                    dateInterval.setEndDate(cal.getTime());
                }else if(i==endWeek){
                    cal.set(Calendar.YEAR, startYear);
                    cal.set(Calendar.WEEK_OF_YEAR, i);
                    cal.set(Calendar.DAY_OF_WEEK, 2);
                    dateInterval.setStartDate(cal.getTime());
                    dateInterval.setEndDate(dataEnd);
                }else{
                    cal.set(Calendar.YEAR, startYear);
                    cal.set(Calendar.WEEK_OF_YEAR, i);
                    cal.set(Calendar.DAY_OF_WEEK, 2);
                    dateInterval.setStartDate(cal.getTime());
                    cal.set(Calendar.YEAR, startYear);
                    cal.set(Calendar.WEEK_OF_YEAR, i);
                    cal.set(Calendar.DAY_OF_WEEK, 1);
                    dateInterval.setEndDate(cal.getTime());
                }
                list.add(dateInterval);
            }
        }else if(startYear<endYear){   //跨年
            for(int i=startYear;i<=endYear;i++){
                int start,end;
                if(i==endYear){
                    start= 1;end = endWeek;
                }else if(i==startYear){
                    cal.setFirstDayOfWeek(Calendar.MONDAY);
                    cal.setMinimalDaysInFirstWeek(7); //设置在一年中第一个星期所需最少天数
                    cal.set(i, Calendar.DECEMBER, 31, 23, 59, 59);
                    start= startWeek;end= cal.get(Calendar.WEEK_OF_YEAR);
                }else{
                    cal.setFirstDayOfWeek(Calendar.MONDAY);
                    cal.setMinimalDaysInFirstWeek(7);
                    cal.set(i, Calendar.DECEMBER, 31, 23, 59, 59);
                    start= 1;end= cal.get(Calendar.WEEK_OF_YEAR);
                }
                for(int j=start;j<=end;j++){
                    DateInterval dateInterval = new DateInterval();
                    if(i==startYear&&j==startWeek){
                        dateInterval.setStartDate(dataBegin);
                        cal.set(Calendar.YEAR, startYear);
                        cal.set(Calendar.WEEK_OF_YEAR, j);
                        cal.set(Calendar.DAY_OF_WEEK, 1);
                        dateInterval.setEndDate(cal.getTime());
                    }else if(i==endYear&&j==endWeek){
                        cal.set(Calendar.YEAR, endYear);
                        cal.set(Calendar.WEEK_OF_YEAR, j);
                        cal.set(Calendar.DAY_OF_WEEK, 2);
                        dateInterval.setStartDate(cal.getTime());
                        dateInterval.setEndDate(dataEnd);
                    }else{
                        cal.set(Calendar.YEAR, i);
                        cal.set(Calendar.WEEK_OF_YEAR, j);
                        cal.set(Calendar.DAY_OF_WEEK, 2);
                        dateInterval.setStartDate(cal.getTime());
                        cal.set(Calendar.YEAR, i);
                        cal.set(Calendar.WEEK_OF_YEAR, j);
                        cal.set(Calendar.DAY_OF_WEEK, 1);
                        dateInterval.setEndDate(cal.getTime());
                    }
                    list.add(dateInterval);
                }

            }
        }
        return list;

    }

    public static void main(String[] args) {

        Date startDate = DateUtils.parseDate("2020-12-22");
        Date endDate = DateUtils.parseDate("2022-1-1");
        List<DateInterval> list = getWeekDateInterval(startDate,endDate);

        //List<DateInterval> list = getMonthDateInterval(2020,12,2021,6);

        //List<DateInterval> list = getYearDateInterval(2021,2022);

        //List<DateInterval> list = getQuarterDateInterval(2021,1,2022,2);

        list.stream().forEach(dateInterval->{
            System.out.print(DateFormatUtils.format(dateInterval.getStartDate(),"yyyy-MM-dd"));
            System.out.print("---");
            System.out.println(DateFormatUtils.format(dateInterval.getEndDate(),"yyyy-MM-dd"));
        });

    }
}
