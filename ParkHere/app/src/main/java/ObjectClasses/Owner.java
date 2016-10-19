package ObjectClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Owner extends Peer {
    private List<Space> listedSpaces = new ArrayList<Space>();
    int ownerRating;

    public List<Space> getListedSpaces() {
        return listedSpaces;
    }

    public void setListedSpaces(List<Space> listedSpaces) {
        this.listedSpaces = listedSpaces;
    }

    public int getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(int ownerRating) {
        this.ownerRating = ownerRating;
    }
}
