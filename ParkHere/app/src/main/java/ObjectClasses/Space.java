package ObjectClasses;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Space {

    private String spaceName;
    private String ownerEmail;
    private SpaceType type;
    private LatLng latlng;
    private Address address;
    private int pricePerHour;
    private CancellationPolicy policy;
    private int spaceRating;
    private String spaceReview;
    private String description;
    private GregorianCalendar availableStartDateAndTime;
    private GregorianCalendar availableEndDateAndTime;
    private List<GregorianCalendar> bookingStartDates;
    private List<GregorianCalendar> bookingEndDates;
    //TODO Make this a list of images
    private Drawable picture;

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public SpaceType getType() {
        return type;
    }

    public void setType(SpaceType type) {
        this.type = type;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public CancellationPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(CancellationPolicy policy) {
        this.policy = policy;
    }

    public int getSpaceRating() {
        return spaceRating;
    }

    public void setSpaceRating(int spaceRating) {
        this.spaceRating = spaceRating;
    }

    public String getSpaceReview() {
        return spaceReview;
    }

    public void setSpaceReview(String spaceReview) {
        this.spaceReview = spaceReview;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getAvailableStartDateAndTime() {
        return availableStartDateAndTime;
    }

    public void setAvailableStartDateAndTime(GregorianCalendar availableStartDateAndTime) {
        this.availableStartDateAndTime = availableStartDateAndTime;
    }

    public GregorianCalendar getAvailableEndDateAndTime() {
        return availableEndDateAndTime;
    }

    public void setAvailableEndDateAndTime(GregorianCalendar availableEndDateAndTime) {
        this.availableEndDateAndTime = availableEndDateAndTime;
    }

    public List<GregorianCalendar> getBookingStartDates() {
        return bookingStartDates;
    }

    public void setBookingStartDates(List<GregorianCalendar> bookingStartDates) {
        this.bookingStartDates = bookingStartDates;
    }

    public List<GregorianCalendar> getBookingEndDates() {
        return bookingEndDates;
    }

    public void setBookingEndDates(List<GregorianCalendar> bookingEndDates) {
        this.bookingEndDates = bookingEndDates;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }
}
