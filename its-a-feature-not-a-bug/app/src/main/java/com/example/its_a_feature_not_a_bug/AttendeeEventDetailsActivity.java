// This source code file implements the functionality for a user to view event details.
// No outstanding issues.

package com.example.its_a_feature_not_a_bug;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;


/**
 * An activity that allows users to view the details of an event.
 * This activity extends AppCompatActivity to inherit its basic functionalities.
 */
public class AttendeeEventDetailsActivity extends AppCompatActivity {
    // Firebase attributes
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference usersRef;
    private CollectionReference announcementsRef;

    // View attributes
    private TextView eventTitle;
    private TextView eventDate;
    private TextView eventHost;
    private TextView eventDescription;
    private ImageView eventPoster;
//    private RecyclerView attendeesRecyclerView;
    private RecyclerView announcementRecyclerView;
    private Button signUpButton;

    private AnnouncementAdapter announcementAdapter;
    private ArrayList<Announcement> announcements;

    // Current attributes
    private User currentUser;
    private Event currentEvent;

    // Local device attributes
    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_details);

        db = FirebaseFirestore.getInstance();

        // Enable the action bar and display the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#368C6E"));
            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\"><b>" + "EVENT DETAILS" + "</b></font>"));

        }

        // System.out.print("reached this point");
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent intent = getIntent();
        currentEvent = (Event) intent.getSerializableExtra("event");

        eventsRef = db.collection("events");
        usersRef = db.collection("users");
        announcementsRef = db.collection("announcements");

        eventPoster = findViewById(R.id.eventImage);
        eventTitle = findViewById(R.id.eventTitle);
        eventHost = findViewById(R.id.eventHost);
        eventDate = findViewById(R.id.eventDate);
        eventDescription = findViewById(R.id.eventDescription);

        // get user data from database
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (androidId.equals(document.getId())) {
                            currentUser = document.toObject(User.class);
                            Log.d("Brayden", "currentUser: " + currentUser.getUserId());
                            break;
                        }
                    }
                } else {
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });

        announcements = new ArrayList<>();
        if (currentEvent.getAnnouncements() != null && !currentEvent.getAnnouncements().isEmpty()) {
            populateAnnouncements();
        }
        announcementAdapter = new AnnouncementAdapter(announcements);
        announcementRecyclerView = findViewById(R.id.announcementsRecyclerView);
        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementRecyclerView.setAdapter(announcementAdapter);

        // Initialize and set OnClickListener for the Show QR Code button
        signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpForEvent();
            }
        });

        displayInfo();

        if (currentEvent.getImageId() != null) {
            Glide.with(this)
                    .load(currentEvent.getImageId())
                    .centerCrop()
                    .into(eventPoster);
        } else {
            // Set a placeholder image if no image is available
            eventPoster.setImageResource(R.drawable.default_poster);
        }
    }

    /**
     * This displays the information of the event.
     */
    public void displayInfo() {
        eventTitle.setText(currentEvent.getTitle());

        // fetch host name from firebase
        usersRef.document(currentEvent.getHost()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            eventHost.setText(user.getFullName());
                        } else {
                            Log.d("Firestore", "Document does not exist");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Failed to fetch host name", e);
                    }
                });
        // Format and display the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentEvent.getDate());

        // Format and display the time
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String formattedTime = timeFormat.format(currentEvent.getDate());

        // Assuming 'date' TextView is used to show both date and time together
        // You might want to separate them or adjust according to your layout needs
        eventDate.setText(String.format("%s at %s", formattedDate, formattedTime));

        eventDescription.setText(currentEvent.getDescription());
    }

    /**
     * This allows a user to sign up for an event.
     */
    private void signUpForEvent() {
        if (currentUser != null) {
            if (currentEvent.getAttendeeLimit() == null || currentEvent.getNumberSignedAttendees() < currentEvent.getAttendeeLimit()) {
                if (currentEvent.getSignedAttendees() == null || !currentEvent.getSignedAttendees().contains(currentUser.getUserId())) {
                    currentEvent.addSignedAttendee(currentUser.getUserId());

                    // Update the Firestore document for the event with the names of attendees and attendee count
                    eventsRef.document(currentEvent.getTitle())
                            .update("signedAttendees", currentEvent.getSignedAttendees())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
//                                    currentUser.signUpForEvent(event);

                                    // Successfully updated the list of attendees and attendee count in the database
                                    Toast.makeText(AttendeeEventDetailsActivity.this, "Signed up for event", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to update the list of attendees and attendee count in the database
                                    Toast.makeText(AttendeeEventDetailsActivity.this, "Failed to sign up for event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AttendeeEventDetailsActivity.this, "Already signed up for this event", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Attendee limit reached
                Toast.makeText(AttendeeEventDetailsActivity.this, "Attendee limit reached for this event", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This populates the list of announcements.
     */
    public void populateAnnouncements() {
        ArrayList<String> announcementsData = currentEvent.getAnnouncements();
        announcementsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Announcement announcement = document.toObject(Announcement.class);
                        if (announcementsData.contains(announcement.getAnnouncementId())) {
                            announcements.add(announcement);
                        }
                    }
                    announcementAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * This implements the back button functionality for the action bar.
     * @param item The menu item that was selected
     * @return whether the back button was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
