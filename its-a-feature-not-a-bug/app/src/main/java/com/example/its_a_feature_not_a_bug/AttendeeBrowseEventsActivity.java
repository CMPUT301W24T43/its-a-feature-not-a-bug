// This source code file implements the functionality for a user to browse and event page.
// No outstanding issues.

package com.example.its_a_feature_not_a_bug;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is an Activity that allows a user to navigate listed events
 */
public class AttendeeBrowseEventsActivity extends AppCompatActivity {
    // Firebase attributes
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    // View attributes
    private ListView eventList;
//    private FloatingActionButton fab;
    private Button cameraButton;
    private Button myEventButton;

    // Adapter attributes
    private EventAdapter eventAdapter;

    // Local device attributes
    private String androidId;

    // Other attributes
    ActivityResultLauncher<ScanOptions> barLauncher;
    private ArrayList<Event> eventDataList;
    private ArrayList<String> signedAttendees = new ArrayList<>();
    private ArrayList<String> attendees = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_browse_events);

        // Enable the action bar and display the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#368C6E"));
            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\"><b>" + "EVENTS" + "</b></font>"));

        }

        // connect to database
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // listview
        eventList = findViewById(R.id.list_view_events_list);
        eventDataList = new ArrayList<>();

        cameraButton = findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setOrientationLocked(true);
                options.setCaptureActivity(QRCodeScanner.class);
                barLauncher.launch(options);
            }
        });

        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                // Parse the URI
                Uri uri = Uri.parse(result.getContents());

                // Extract the handlerType from the URI
                String handlerType = uri.getHost();

                // Handle the QR code
                Class<?> targetActivityClass;
                if ("promotional".equals(handlerType)) {
                    targetActivityClass = HandlePromotionalQRActivity.class;
                } else if ("checkin".equals(handlerType)) {
                    targetActivityClass = HandleCheckInQRActivity.class;
                } else {
                    targetActivityClass = AttendeeBrowseEventsActivity.class;
                }


                // Start the appropriate activity with the parsed URI
                Intent intent = new Intent(this, targetActivityClass);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        // adapter
        eventAdapter = new EventAdapter(this, eventDataList);
        eventList.setAdapter(eventAdapter);

        // add on click listener to click event
        eventList.setOnItemClickListener((parent, view, position, id) -> {
            Event event = eventDataList.get(position);
            Intent intent = new Intent(this, AttendeeEventDetailsActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        });


        Button profileButton = findViewById(R.id.button_profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendeeBrowseEventsActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });


        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventDataList.clear();
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        Event event = doc.toObject(Event.class);
                        event.setTitle(doc.getId());
                        Log.d("Firestore", String.format("Event(%s, %s) fetched", event.getTitle(), event.getHost()));

                        // Get reference to AttendeeLocations subcollection
                        CollectionReference attendeeLocationsRef = doc.getReference().collection("Attendee Locations");

                        // Retrieve data asynchronously
                        attendeeLocationsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot attendeeLocationsSnapshot = task.getResult();
                                    if (attendeeLocationsSnapshot != null && !attendeeLocationsSnapshot.isEmpty()) {
                                        Map<String, List<Double>> idLocationMap = new HashMap<>();
                                        for (QueryDocumentSnapshot attendeeLocationDoc : attendeeLocationsSnapshot) {
                                            AttendeeLocationInformation locationInformation = attendeeLocationDoc.toObject(AttendeeLocationInformation.class);
                                            locationInformation.setTitle(attendeeLocationDoc.getId());
                                            Double latitude = Double.valueOf(locationInformation.getLatitude() != null ? locationInformation.getLatitude().trim() : "0.0");
                                            Double longitude = Double.valueOf(locationInformation.getLongitude() != null ? locationInformation.getLongitude().trim() : "0.0");

                                            List<Double> gpsCoordinates = Arrays.asList(latitude, longitude);

                                            idLocationMap.put(locationInformation.getTitle(), gpsCoordinates);
                                            Log.d("Location Information", "Even title" + event.getTitle() + "Attendee ID: " + locationInformation.getTitle()
                                            + " Coordinates = " + locationInformation.getLatitude() + " , " + locationInformation.getLongitude());
                                        }
                                        event.setAttendeeLocations(idLocationMap);
                                        Log.d("Firebase", "attendee locations added");
                                    } else {
                                        Log.d("", "AttendeeLocations subcollection is empty for event: " + event.getTitle());
                                    }
                                } else {
                                    Log.e("Firestore", "Error getting attendee locations: ", task.getException());
                                }
                            }
                        });
                        eventDataList.add(event);
                    }
                    eventAdapter.notifyDataSetChanged();
                    Log.d("", "ATTENDEE LIST "  + attendees);
                }
            }
        });


        myEventButton = findViewById(R.id.button_events); // Ensure this ID matches your layout
        myEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                ArrayList<Event> myEventsList = getMyEvents(eventDataList);
                Intent intent = new Intent(AttendeeBrowseEventsActivity.this, MyEventsActivity.class);
                intent.putExtra("myEventsList", myEventsList);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Event> getMyEvents(ArrayList <Event> eventDataList) {
        ArrayList<Event> myEvents = new ArrayList<>();
        for (Event event : eventDataList) {
            if (event.getSignedAttendees() != null) {
                if (event.hasSignedAttendee(androidId)) {
                    myEvents.add(event);
                }
            }
        }
        return myEvents;
    }
}