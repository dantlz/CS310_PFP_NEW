package ObjectClasses;

public class MyCalendar {
    int year;
    int month;
    int day;
    int hour;
    int minute;

    public MyCalendar(){

    }

    public MyCalendar(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    public boolean isMonthValid(){
        return month > 0 && month < 13;
    }
    public boolean isDayValid(){
        if(day < 1 || day > 31) {
            return false;
        }
        if(month == 2){
            if(isLeapYear()){
                return day < 30;
            }
            else{
                return day < 29;
            }
        }
        else if(month == 4 || month == 6 || month == 8 || month == 9 || month == 11){
            return day < 31;
        }
        else{
            return day < 32;
        }
    }
    public boolean isHourValid(){
        return hour > 0 && hour < 25;
    }
    public boolean isMinuteValid(){
        return minute >= 0 && minute <= 60;
    }
    private boolean isLeapYear() {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else {
            return true;
        }
    }
}
