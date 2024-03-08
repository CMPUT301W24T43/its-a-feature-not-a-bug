package com.example.its_a_feature_not_a_bug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventDetailsActivity extends AppCompatActivity {
    private Event event;
    private TextView name;
    private TextView date;
    private TextView location;
    private TextView description;
    private ImageView qrCode;
    private RecyclerView attendeesRecyclerView;
    private AttendeeAdapter attendeeAdapter;
    private RecyclerView announcementRecyclerView;
    private AnnouncementAdapter announcementAdapter;

    private Button signUpButton;

    private User currentUser;
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        name = findViewById(R.id.eventTitle);
        date = findViewById(R.id.eventDate);
        description = findViewById(R.id.eventDescription);
        currentUser = new User("YourName");

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("event");


        attendeesRecyclerView = findViewById(R.id.attendeesRecyclerView);
        attendeesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementRecyclerView = findViewById(R.id.announcementsRecyclerView);
        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpForEvent();
            }
        });
        displayInfo();
    }
        public void displayInfo() {
            name.setText(event.getTitle());
            // convert date to string
            date.setText(event.getDate().toString());

            description.setText(event.getDescription());
            ArrayList<String> attendees = event.getAttendees();
            if (attendees == null) {
                attendees = new ArrayList<>();
            }
            attendeeAdapter = new AttendeeAdapter(attendees);
            attendeeAdapter = new AttendeeAdapter(event.getAttendees());
            attendeesRecyclerView.setAdapter(attendeeAdapter);
            List<Announcement> announcements = event.getAnnouncements();
            if (announcements == null) {
                announcements = new ArrayList<>();
            }
            announcementAdapter = new AnnouncementAdapter(announcements);
            announcementRecyclerView.setAdapter(announcementAdapter);
        }
        private void signUpForEvent() {
            if (currentUser != null) {
                currentUser.signUpForEvent(event);

                Toast.makeText(EventDetailsActivity.this, "Signed up for event", Toast.LENGTH_SHORT).show();
                attendeeAdapter.notifyDataSetChanged();
            }
        }
    }


