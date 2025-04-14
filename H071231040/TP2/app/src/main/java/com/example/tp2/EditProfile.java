package com.example.tp2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfile extends AppCompatActivity {

    private EditText nameEditText, usernameEditText;
    private ImageView profileImage;
    private Button changePhotoBtn, saveBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        profileImage = findViewById(R.id.profileImage);
        changePhotoBtn = findViewById(R.id.changePhotoBtn);
        saveBtn = findViewById(R.id.saveBtn);

        // Get current values from intent
        String currentName = getIntent().getStringExtra("current_name");
        String currentUsername = getIntent().getStringExtra("current_username");

        // Set EditText values to current values or defaults
        if (currentName != null && !currentName.isEmpty()) {
            nameEditText.setText(currentName);
        } else {
            nameEditText.setText(getString(R.string.name));
        }

        if (currentUsername != null && !currentUsername.isEmpty()) {
            usernameEditText.setText(currentUsername);
        } else {
            usernameEditText.setText(getString(R.string.username));
        }

        // Get current profile image URI if available
        String currentProfileImage = getIntent().getStringExtra("current_profile_image");
        if (currentProfileImage != null && !currentProfileImage.isEmpty()) {
            try {
                selectedImageUri = Uri.parse(currentProfileImage);
                profileImage.setImageURI(selectedImageUri);
                Log.d("EditProfile", "Loaded image URI: " + selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to load profile image", Toast.LENGTH_SHORT).show();
            }
        }

        // Handle photo change button
        changePhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Handle save button
        saveBtn.setOnClickListener(v -> {
            // Validate inputs
            String name = nameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();

            boolean isValid = true;

            // Validate name
            if (name.isEmpty()) {
                ((com.google.android.material.textfield.TextInputLayout) nameEditText.getParent().getParent())
                        .setError("Name cannot be empty");
                isValid = false;
            } else if (name.length() < 3) {
                ((com.google.android.material.textfield.TextInputLayout) nameEditText.getParent().getParent())
                        .setError("Name must be at least 3 characters");
                isValid = false;
            } else {
                ((com.google.android.material.textfield.TextInputLayout) nameEditText.getParent().getParent())
                        .setError(null);
            }

            // Validate username
            if (username.isEmpty()) {
                ((com.google.android.material.textfield.TextInputLayout) usernameEditText.getParent().getParent())
                        .setError("Username cannot be empty");
                isValid = false;
            } else if (username.length() < 5) {
                ((com.google.android.material.textfield.TextInputLayout) usernameEditText.getParent().getParent())
                        .setError("Username must be at least 5 characters");
                isValid = false;
            } else {
                ((com.google.android.material.textfield.TextInputLayout) usernameEditText.getParent().getParent())
                        .setError(null);
            }

            // If all fields are valid, save changes
            if (isValid) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", name);
                resultIntent.putExtra("username", username);

                if (selectedImageUri != null) {
                    resultIntent.putExtra("profile_image", selectedImageUri.toString());
                }

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the image URI
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Take read permission for the current session
                    getContentResolver().takePersistableUriPermission(
                            selectedImageUri, 
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    
                    // Set the image URI to the ImageView
                    profileImage.setImageURI(selectedImageUri);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Cannot access image. Please try another.", Toast.LENGTH_SHORT).show();
                    profileImage.setImageResource(R.drawable.profile);
                }
            }
        }
    }
}
