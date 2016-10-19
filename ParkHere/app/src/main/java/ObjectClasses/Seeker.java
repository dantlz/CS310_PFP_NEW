package ObjectClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Seeker extends Peer {
    private List<Booking> completedBookings = new ArrayList<Booking>();

    public List<Booking> getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(List<Booking> completedBookings) {
        this.completedBookings = completedBookings;
    }
}
