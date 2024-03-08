// This source code file implements the Event class.
// No outstanding issues.

package com.example.its_a_feature_not_a_bug;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class representing an event.
 */
public class Event implements Serializable {
    private String imageId; // event poster
    private String title; // name of the event
    private String host; // user that created the event
    private Date date; // date of the event
    private ArrayList<User> signedAttendees; // list of users signed up for the event
    private ArrayList<User> checkedAttendees; //list of users checked into the event
    private ArrayList<Announcement> announcements;
    private String description; // short description of the event posted by the host
    private int attendeeLimit;
    private int attendeeCount;

    /**
     * This is a constructor for the Event class.
     * @param imageId the image (event poster)
     * @param title the name of the event
     * @param host the organizer of the event
     * @param date the date the event will take place
     * @param description a short description describing the event
     * @param attendeeLimit the max number of attendees
     */
    public Event(String imageId, String title, String host, Date date, String description, int attendeeLimit) {
        this.imageId = imageId;
        this.title = title;
        this.host = host;
        this.date = date;
        this.description = description;
        this.attendeeLimit = attendeeLimit;
    }

    /**
     * This is a constructor the the Event class.
     * @param imageId the image (event poster)
     * @param title the name of the event
     * @param host the organizer of the event
     * @param date the date the event will take place
     * @param description a short description describing the event
     */
    public Event(String imageId, String title, String host, Date date, String description) {
        this.imageId = imageId;
        this.title = title;
        this.host = host;
        this.date = date;
        this.description = description;
    }

    /**
     * This is a constructor the the Event class.
     * @param title the name of the event
     * @param host the organizer of the event
     * @param date the date the event will take place
     * @param description a short description describing the event
     */
    public Event(String title, Date date, String host, String description) {
        this.title = title;
        this.date = date;
        this.host = host;
        this.description = description;
    }

    /**
     * This returns the uri of the event image.
     * @return the event uri
     */
    public String getImageId() {
        return this.imageId;
    }

    /**
     * This sets the image of the event to a new value.
     * @param imageId the new image
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * This returns the name of the event
     * @return the event name
     */
    public String getTitle() {
        return title;
    }

    /**
     * This sets the name of the event to a new value.
     * @param title the new name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This returns the organizer of the event.
     * @return the event organizer
     */
    public String getHost() {
        return host;
    }

    /**
     * This sets the organizer of the event to a new value.
     * @param host the new organizer
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * This returns the date of the event.
     * @return the event date
     */
    public Date getDate() {
        return date;
    }

    /**
     * This sets the date of the event to a new value.
     * @param date the new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This returns the description of the event.
     * @return the event description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This sets the description of the event to a new value.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<User> getSignedAttendees() {
        return signedAttendees;
    }

    public void setSignedAttendees(ArrayList<User> attendees) {
        this.signedAttendees = attendees;
    }

    public ArrayList<User> getCheckedAttendees() {
        return checkedAttendees;
    }

    public void setCheckedAttendees(ArrayList<User> checkedAttendees) {
        this.checkedAttendees = checkedAttendees;
    }

    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    public int getAttendeeLimit() {
        return attendeeLimit;
    }

    public void setAttendeeLimit(int attendeeLimit) {
        this.attendeeLimit = attendeeLimit;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }


}