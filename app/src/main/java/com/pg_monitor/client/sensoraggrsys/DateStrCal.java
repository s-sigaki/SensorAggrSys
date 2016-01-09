package com.pg_monitor.client.sensoraggrsys;

/**
 * Created by sunyanan on 1/9/16.
 */
public class DateStrCal {

    public static double strToDouble(String date_str){
        String split_date_time[] = date_str.split(" ");
        String split_date[] = split_date_time[0].split("-");
        String split_time[] = split_date_time[1].split(":");

        Byte sec = Byte.valueOf(split_time[2]);
        Byte min = Byte.valueOf(split_time[1]);
        Byte hour = Byte.valueOf(split_time[0]);
        Byte day = Byte.valueOf(split_date[2]);
        Byte month = Byte.valueOf(split_date[1]);
        Short year = Short.valueOf(split_date[0]);
        return (year&0xFF)<<40|(month&0xFF)<<32|(day&0xFF)<<24|(hour&0xFF)<<16|(min&0xFF)<<8|(sec&0xFF);
    }

    public static String doubleToStr(Double date_db){
        long l = Double.doubleToRawLongBits(date_db);
        String year = ""+(byte)((l >> 48) & 0xff)+(byte)((l >> 40) & 0xff);
        String month = ""+(byte)((l >> 32) & 0xff);
        String day = ""+(byte)((l >> 24) & 0xff);
        String hour = ""+(byte)((l >> 16) & 0xff);
        String min = ""+(byte)((l >> 8) & 0xff);
        String sec = ""+(byte)((l >> 0) & 0xff);
        return year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
    }

    public static String sub_sec_time(String date_time_str, int total_sec_sub){

        int sub_sec_int = total_sec_sub%60;
        int total_min_sub = total_sec_sub/60;
        int sub_min_int = (total_min_sub)%60;
        int total_hour_sub = total_min_sub/60;
        int sub_hour_int = (total_hour_sub)%24;

        int sub_day_int = (int)(total_hour_sub)/24;

        String sub_time_str = int_to_2char(sub_hour_int) + ":" + int_to_2char(sub_min_int) + ":" + int_to_2char(sub_sec_int);

        return sub_day_time(date_time_str, sub_day_int, sub_time_str);

    }
    private static String int_to_2char(int num){
        return (num<10)?"0"+num:""+num;
    }

    public static String sub_day_time(String date_time_str, int sub_day, String sub_time){ // day, hh:mm:ss
        String split_date_time[] = date_time_str.split(" ");
        String split_date[] = split_date_time[0].split("-");
        String split_time[] = split_date_time[1].split(":");
        String split_sub_time[] = sub_time.split(":");

        int sec = Integer.parseInt(split_time[2]);
        int min = Integer.parseInt(split_time[1]);
        int hour = Integer.parseInt(split_time[0]);
        int sub_sec = Integer.parseInt(split_sub_time[2]);
        int sub_min = Integer.parseInt(split_sub_time[1]);
        int sub_hour = Integer.parseInt(split_sub_time[0]);

        int sec_int = sec-sub_sec;
        int min_int = min-sub_min;
        int hour_int = hour-sub_hour;
        int year_int = Integer.parseInt(split_date[0]);
        int month_int = Integer.parseInt(split_date[1]);
        int day_int = Integer.parseInt(split_date[2]) - sub_day;

        if(sec_int < 0){
            sec_int += 60;
            min_int -= 1;
        }
        if(min_int < 0){
            min_int += 60;
            hour_int -=1;
        }
        if(hour_int < 0){
            hour_int +=24;
            day_int -=1;
        }
        while(day_int <= 0){
            day_int +=  days_in_month(month_int, year_int);
            month_int -= 1;
            if(month_int <= 0){
                month_int = 12;
                year_int -=1;
            }
        }
        return year_int+"-"+int_to_2char(month_int)+"-"+int_to_2char(day_int)+" "+int_to_2char(hour_int)+":"+int_to_2char(min_int)+":"+int_to_2char(sec_int);
    }

    private static int days_in_month(int month, int year)
    {
        // calculate number of days in a month
        return month == 2 ? ((year%4==0) ? 28 : ((year%100==0)? 29 : ((year%400==0) ? 28 : 29))) : (((month - 1) % 7 % 2 ==0)? 30 : 31);
    }
}


