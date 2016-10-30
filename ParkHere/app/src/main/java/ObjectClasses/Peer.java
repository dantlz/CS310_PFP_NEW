package ObjectClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by tianlinz on 10/16/16.
 */

public class Peer {
    private String emailAddress;
//    Password
    private Preferences preferences;
    private PaymentInfo paymentInfo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
}

