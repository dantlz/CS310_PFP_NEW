package ObjectClasses;

/**
 * Created by Alex on 11/25/2016.
 */

public class Post {

    private String postName;

    private String parentSpaceName;
    private String parentOwnerEmail;

    private CancellationPolicy policy;
    private MyCalendar availableStartDateAndTime;
    private MyCalendar availableEndDateAndTime;



    public String getParentSpaceName() {
        return parentSpaceName;
    }

    public void setParentSpaceName(String parentSpaceName) {
        this.parentSpaceName = parentSpaceName;
    }

    public String getParentOwnerEmail() {
        return parentOwnerEmail;
    }

    public void setParentOwnerEmail(String parentOwnerEmail) {
        this.parentOwnerEmail = parentOwnerEmail;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public CancellationPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(CancellationPolicy policy) {
        this.policy = policy;
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

}
