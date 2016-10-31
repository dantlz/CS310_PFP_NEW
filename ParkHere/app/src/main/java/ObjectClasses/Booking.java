package ObjectClasses;

public class Booking {
    private String spaceName;
    private String bookingSpaceOwnerEmail;
    private MyCalendar startCalendarDate;
    private MyCalendar endCalendarDate;
    private boolean done;

    public MyCalendar getStartCalendarDate() {
        return startCalendarDate;
    }

    public void setStartCalendarDate(MyCalendar startCalendarDate) {
        this.startCalendarDate = startCalendarDate;
    }

    public MyCalendar getEndCalendarDate() {
        return endCalendarDate;
    }

    public void setEndCalendarDate(MyCalendar endCalendarDate) {
        this.endCalendarDate = endCalendarDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }


    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    private boolean repeat;

    public String getBookingSpaceOwnerEmail() {
        return bookingSpaceOwnerEmail;
    }

    public void setBookingSpaceOwnerEmail(String bookingSpaceOwnerEmail) {
        this.bookingSpaceOwnerEmail = bookingSpaceOwnerEmail;
    }
}
