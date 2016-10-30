package ObjectClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Parcelable;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
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
    private String address;
    private int pricePerHour;
    private CancellationPolicy policy;
    private String description;
    //TODO Make this a list of images
    private MyCalendar availableStartDateAndTime;
    private MyCalendar availableEndDateAndTime;
    private String picture;

    //    private List<MyCalendar> bookingStartDates;
//    private List<MyCalendar> bookingEndDates;
    private int spaceRating;
    private String spaceReview;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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

    public Bitmap getPicture() {
        byte [] encodeByte =Base64.decode(picture,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public void setPicture(Drawable drawable) {
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
//
//    public List<MyCalendar> getBookingStartDates() {
//        return bookingStartDates;
//    }
//
//    public void setBookingStartDates(List<MyCalendar> bookingStartDates) {
//        this.bookingStartDates = bookingStartDates;
//    }
//
//    public List<MyCalendar> getBookingEndDates() {
//        return bookingEndDates;
//    }
//
//    public void setBookingEndDates(List<MyCalendar> bookingEndDates) {
//        this.bookingEndDates = bookingEndDates;
//    }

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

}
