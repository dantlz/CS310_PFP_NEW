package ObjectClasses;

import android.graphics.Picture;
import android.location.Address;

import java.util.Date;
import java.util.List;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Space {
    private Picture picture;
    private int pricePerHour;
    private String ownerEmail;
    private Address address;
    private List<Date> bookingStartTimes;
    private List<Date> bookingEndTimes;
    private SpaceType type;
    private int ownerRating;
    private String spaceReview;
    private String name;

    public CancellationPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(CancellationPolicy policy) {
        this.policy = policy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpaceReview() {
        return spaceReview;
    }

    public void setSpaceReview(String spaceReview) {
        this.spaceReview = spaceReview;
    }

    public int getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(int ownerRating) {
        this.ownerRating = ownerRating;
    }

    public SpaceType getType() {
        return type;
    }

    public void setType(SpaceType type) {
        this.type = type;
    }

    public List<Date> getBookingEndTimes() {
        return bookingEndTimes;
    }

    public void setBookingEndTimes(List<Date> bookingEndTimes) {
        this.bookingEndTimes = bookingEndTimes;
    }

    public List<Date> getBookingStartTimes() {
        return bookingStartTimes;
    }

    public void setBookingStartTimes(List<Date> bookingStartTimes) {
        this.bookingStartTimes = bookingStartTimes;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    private CancellationPolicy policy;

}
