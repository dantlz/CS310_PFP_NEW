package ObjectClasses;

import java.util.GregorianCalendar;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Booking {
    private Space space;
<<<<<<< HEAD
    private GregorianCalendar start;
    private GregorianCalendar end;
=======
    private Date start;
    private Date end;
    private boolean done;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
>>>>>>> 84c06d1c6c19843ebd249ffcc3ac33457445eb3b

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public GregorianCalendar getEnd() {
        return end;
    }

    public void setEnd(GregorianCalendar end) {
        this.end = end;
    }

    public GregorianCalendar getStart() {
        return start;
    }

    public void setStart(GregorianCalendar start) {
        this.start = start;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    private boolean repeat;

}
