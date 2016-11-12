package ObjectClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Peer {
    private String emailAddress;
    private String reformattedEmail;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private Status status;
    private Status preferredStatus;
    private String photoID;
    private int ownerRating; //TODO This needs to be a double, but too much to change.
    //Reviews/ListedSpaces/Bookings all such lists are on database only.

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getReformattedEmail() {
        return reformattedEmail;
    }

    public void setReformattedEmail(String reformattedEmail) {
        this.reformattedEmail = reformattedEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //This is for firebase purposes.
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    //This is for firebase purposes.
    public String getProfilePicture(){
        return this.profilePicture;
    }

    public void setDPNonFirebaseRelated(Drawable dpDrawable) {
        Bitmap copySelectedImage = getResizedBitmap(((BitmapDrawable) dpDrawable).getBitmap(), 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copySelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        profilePicture = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap retrieveDPBitmap() {
        byte [] encodeByte =Base64.decode(profilePicture,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getPreferredStatus() {
        return preferredStatus;
    }

    public void setPreferredStatus(Status preferredStatus) {
        this.preferredStatus = preferredStatus;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public void setIDNonFirebaseRelated(Drawable dpDrawable) {
        Bitmap copySelectedImage = getResizedBitmap(((BitmapDrawable) dpDrawable).getBitmap(), 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copySelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        photoID = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap retrieveIDBitmap() {
        byte [] encodeByte =Base64.decode(photoID,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public int getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(int ownerRating) {
        this.ownerRating = ownerRating;
    }
}

