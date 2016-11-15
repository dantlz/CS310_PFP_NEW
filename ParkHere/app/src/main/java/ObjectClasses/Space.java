package ObjectClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Space {

    private String spaceName;
    private String ownerEmail;
    private SpaceType type;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private int pricePerHour;
    private CancellationPolicy policy;
    private String description;
    private MyCalendar availableStartDateAndTime;
    private MyCalendar availableEndDateAndTime;
    private String picture; //TODO Make this a list of images
    private int spaceRating; //TODO This needs to be a double, but too much to change
    //Reviews are on database only

    //The key is currentBookingOwnerEmails, the value is a list of currentBookingsIdentifiers
    private List<MyCalendar> bookingStartDates;
    private List<MyCalendar> bookingEndDates;


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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyCalendar getAvailableStartDateAndTime() {
        return availableStartDateAndTime;
    }

    public void setAvailableStartDateAndTime(MyCalendar availableStartDateAndTime) {
        this.availableStartDateAndTime = availableStartDateAndTime;
    }

    public MyCalendar getAvailableEndDateAndTime() {
        return availableEndDateAndTime;
    }

    public void setAvailableEndDateAndTime(MyCalendar availableEndDateAndTime) {
        this.availableEndDateAndTime = availableEndDateAndTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Bitmap retrievePicNonFireBase() {
        byte [] encodeByte =Base64.decode(picture,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public void setDPNonFireBase(Drawable drawable) {
        Bitmap copySelectedImage = getResizedBitmap(((BitmapDrawable)drawable).getBitmap(), 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copySelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        picture = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public int getSpaceRating() {
        return spaceRating;
    }

    public void setSpaceRating(int spaceRating) {
        this.spaceRating = spaceRating;
    }

    public List<MyCalendar> getBookingStartDates() {
        return bookingStartDates;
    }

    public void setBookingStartDates(List<MyCalendar> bookingStartDates) {
        this.bookingStartDates = bookingStartDates;
    }

    public List<MyCalendar> getBookingEndDates() {
        return bookingEndDates;
    }

    public void setBookingEndDates(List<MyCalendar> bookingEndDates) {
        this.bookingEndDates = bookingEndDates;
    }
}
