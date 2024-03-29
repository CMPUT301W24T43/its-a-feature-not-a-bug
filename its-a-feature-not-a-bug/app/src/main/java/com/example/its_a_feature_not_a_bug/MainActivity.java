// This is the entry point into the program. It acts as a startup screen for the user.
// No outstanding issues.

package com.example.its_a_feature_not_a_bug;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * The main activity of the app.
 * This activity extends AppCompatActivity to inherit its basic functionalities.
 */
public class MainActivity extends AppCompatActivity {
    private Button adminButton;
    private Button userButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set buttons
        adminButton = findViewById(R.id.button_admin_login);
        userButton = findViewById(R.id.button_user_login);

        // Request camera permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }

        // Set action bar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#368C6E"));
            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\"><b>" + "QRCHECKIN" + "</b></font>"));
        }



        // set button listeners
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                startActivity(myIntent);
                // do not finish as this is the launch screen and the back button should bring us back here
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, BrowseEventsActivity.class);
                startActivity(myIntent);
                // do not finish as this is the launch screen and the back button should bring us back here
            }
        });
    }

//    public void scanQRCode() {
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        integrator.setPrompt("Scan a QR code");
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(false);
//        integrator.setBarcodeImageEnabled(true);
//        integrator.initiateScan();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//            } else {
//                // Use the scanned QR code to check the attendee into the event
//                String scannedQRCode = result.getContents();
//                checkInAttendee(scannedQRCode);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

//    private void checkInAttendee(String qrCode) {
//        // Use the QR code to find the corresponding event and check the attendee in
//        // This will depend on how your events and attendees are stored
//    }
//    private static final int PICK_IMAGE = 1;

//    public void uploadProfilePicture() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE) {
//            Uri imageUri = data.getData();
//            // Upload the image to a server and get the image URL
//            String imageUrl = uploadImage(imageUri);
//            // Set the image URL as the profile picture of the attendee
//            attendee.setProfilePicture(imageUrl);
//        } else {
//            // Handle other activity results
//        }
//    }

//    private String uploadImage(Uri imageUri) {
//        // This method should handle the image upload and return the image URL
//        // The implementation will depend on your server and how you handle file uploads
//        return null;
//    }

}