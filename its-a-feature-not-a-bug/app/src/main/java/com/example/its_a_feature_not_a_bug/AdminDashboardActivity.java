package com.example.its_a_feature_not_a_bug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity that serves as the dashboard for administrators.
 * This activity extends AppCompatActivity.
 *
 * <p>In this activity, administrators are presented with two buttons: Events and Profiles.</p>
 *
 * <p>Clicking the Events button navigates to the AdminBrowseEventsActivity where administrators can browse events.</p>
 *
 * <p>Clicking the Profiles button navigates to the AdminBrowseProfilesActivity where administrators can browse user profiles.</p>
 *
 * <p>The navigation is handled by setting onClickListeners on the buttons and starting the appropriate activity using intents.</p>
 */
public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button buttonEvents = findViewById(R.id.buttonEvents);
        Button buttonProfiles = findViewById(R.id.buttonProfiles);

        buttonEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the events management activity
                Intent intent = new Intent(AdminDashboardActivity.this, AdminBrowseEventsActivity.class);
                startActivity(intent);
            }
        });

        buttonProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the profiles management activity
                // You'll need to create this activity if it doesn't exist
                Intent intent = new Intent(AdminDashboardActivity.this, AdminBrowseProfilesActivity.class);
                startActivity(intent);
            }
        });
    }
}
