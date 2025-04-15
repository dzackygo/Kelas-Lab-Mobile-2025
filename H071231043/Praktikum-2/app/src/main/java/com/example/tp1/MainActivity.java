package com.example.tp1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtUsername;
    private ImageView imageProfile;
    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.txt_name);
        txtUsername = findViewById(R.id.txt_username);
        imageProfile = findViewById(R.id.topImage);

        ImageButton btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("nama", txtName.getText().toString());
            intent.putExtra("username", txtUsername.getText().toString());
            intent.putExtra("imageUri", imageProfile.getTag() != null ? imageProfile.getTag().toString() : null);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            txtName.setText(data.getStringExtra("nama"));
            txtUsername.setText(data.getStringExtra("username"));
            String imageUri = data.getStringExtra("imageUri");
            if (imageUri != null) {
                imageProfile.setImageURI(Uri.parse(imageUri));
                imageProfile.setTag(imageUri);
            }
        }
    }
}
