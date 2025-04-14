package com.example.tp2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 1;
    private Uri currentProfileImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Start EditProfile activity
        ImageView settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfile.class);

            // Get current values from TextViews
            TextView nameView = findViewById(R.id.nameView);
            TextView usernameView = findViewById(R.id.usernameView);

            // Pass current values to EditProfile
            intent.putExtra("current_name", nameView.getText().toString());
            intent.putExtra("current_username", usernameView.getText().toString());

            // Pass current image URI if available
            if (currentProfileImageUri != null) {
                intent.putExtra("current_profile_image", currentProfileImageUri.toString());
            }

            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Update profile information
            String newName = data.getStringExtra("name");
            String newUsername = data.getStringExtra("username");
            String profileImageUri = data.getStringExtra("profile_image");

            TextView nameView = findViewById(R.id.nameView);
            TextView usernameView = findViewById(R.id.usernameView);

            if (newName != null && !newName.isEmpty()) {
                nameView.setText(newName);
            }

            if (newUsername != null && !newUsername.isEmpty()) {
                usernameView.setText(newUsername);
            }

            if (profileImageUri != null && !profileImageUri.isEmpty()) {
                try {
                    currentProfileImageUri = Uri.parse(profileImageUri);
                    // Get the specific ImageView
                    ImageView profileImageView = ((ImageView) ((RelativeLayout) findViewById(R.id.layout_image)).getChildAt(0));
                    profileImageView.setImageURI(currentProfileImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
