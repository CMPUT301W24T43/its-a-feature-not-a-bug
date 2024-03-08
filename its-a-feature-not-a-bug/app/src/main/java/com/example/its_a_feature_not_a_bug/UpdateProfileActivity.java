package com.example.its_a_feature_not_a_bug;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference profilesRef;
    private EditText editTextContactInfo;
    private EditText editTextFullName;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        editTextContactInfo = findViewById(R.id.editTextContactInfo);
        editTextFullName = findViewById(R.id.editTextFullName);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the action to perform when the back button is clicked
                finish(); // This will close the current activity and return to the previous one
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                profilesRef = db.collection("profiles");

                Map<String,Object> data = new HashMap<>();
                data.put("contactInfo", editTextContactInfo.getText().toString());
                data.put("fullName", editTextFullName.getText().toString());

                profilesRef.document(editTextFullName.getText().toString()).set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProfileActivity.this, "Submit successful", Toast.LENGTH_SHORT).show();
                            }
                        });

                finish();

//                String name = editTextName.getText().toString();
//                String homepage = editTextHomepage.getText().toString();
//                String contact = editTextContact.getText().toString();
//                Toast.makeText(UpdateProfileActivity.this, "Submit successful", Toast.LENGTH_SHORT).show();
//
//
//                // Use these values to update the user's profile
//                 //This will depend on how your user profiles are stored
//                // For example, if you are using Firebase, you would do something like:
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(name)
//                        .setPhotoUri(Uri.parse(homepage))
//                        .build();
            }
        });
    }
}
