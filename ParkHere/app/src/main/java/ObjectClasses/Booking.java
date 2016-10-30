package ObjectClasses;

import java.util.GregorianCalendar;
import java.util.Date;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Booking {
    private Space space;
    private MyCalendar startCalendarDate;
    private MyCalendar endCalendarDate;
    private Date start;
    private Date end;
    private boolean done;

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

    public void setStart(MyCalendar start) {
        this.startCalendarDate = start;
    }

    public void setEnd(MyCalendar end) {
        this.endCalendarDate = end;
    }

    public MyCalendar getStart() {
        return startCalendarDate;
    }

    public MyCalendar getEnd(){
        return endCalendarDate;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    private boolean repeat;

}
