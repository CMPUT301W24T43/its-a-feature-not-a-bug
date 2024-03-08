package com.example.its_a_feature_not_a_bug;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents an event that a user can sign up for.
 */
public class Event implements Serializable {
    private Uri imageId; // event poster
    private String title; // name of the event
    private String host; // user that created the event
    private Date date; // date of the event
    private ArrayList<User> signedAttendees; // list of users signed up for the event

    private ArrayList<User> checkedAttendees; //list of users checked into the event
    private ArrayList<Announcement> announcements;
    private String description; // short description of the event posted by the host
    private int attendeeLimit;

    private int attendeeCount;

    public Event(Uri imageId, String title, String host, Date date, String description, int attendeeLimit) {
        this.imageId = imageId;
        this.title = title;
        this.host = host;
        this.date = date;
        this.description = description;
        this.attendeeLimit = attendeeLimit;
    }

    public Event(Uri imageId, String title, String host, Date date, String description) {
        this.imageId = imageId;
        this.title = title;
        this.host = host;
        this.date = date;
        this.description = description;
    }

    public Uri getImageId() {
        return this.imageId;
    }

    public void setImageId(Uri imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
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

    public void setDescription(String description) {
        this.description = description;
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