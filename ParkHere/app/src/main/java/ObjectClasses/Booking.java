package ObjectClasses;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Booking {
    private Space space;
    private GregorianCalendar startCalendarDate;
    private GregorianCalendar endCalendarDate;
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

    public GregorianCalendar getEnd() {
        return endCalendarDate;
    }

    public void setEnd(GregorianCalendar end) {
        this.endCalendarDate = end;
    }

    public GregorianCalendar getStart() {
        return startCalendarDate;
    }

    public void setStart(GregorianCalendar start) {
        this.startCalendarDate = start;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    private boolean repeat;

}
