package com.example.tp1;


//import static com.example.tp1.MainActivity.EDIT_PROFILE_REQUEST;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionController;

public class SettingsActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtUsername;
    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView selesaiButton = findViewById(R.id.btn_selesai);
        selesaiButton.setOnClickListener(v -> finish());

        TextView profilText = findViewById(R.id.text_profil);

        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String username = intent.getStringExtra("username");
        String imageUri = intent.getStringExtra("imageUri");

        profilText.setOnClickListener(v -> {
            Intent editIntent = new Intent(SettingsActivity.this, EditProfileActivity.class);
            editIntent.putExtra("nama", nama);
            editIntent.putExtra("username", username);
            editIntent.putExtra("imageUri", imageUri);
            startActivityForResult(editIntent, EDIT_PROFILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            String updatedNama = data.getStringExtra("nama");
            String updatedUsername = data.getStringExtra("username");
            String updatedImageUri = data.getStringExtra("imageUri");

            Intent resultIntent = new Intent();
            resultIntent.putExtra("nama", updatedNama);
            resultIntent.putExtra("username", updatedUsername);
            resultIntent.putExtra("imageUri", updatedImageUri);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
