package com.example.tp1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editNama, editUsername;
    private ImageView imageProfile;
    private TextView gantiAvatar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Ambil referensi view
        editNama = findViewById(R.id.edit_nama);
        editUsername = findViewById(R.id.edit_username);
        imageProfile = findViewById(R.id.profile_image);
        gantiAvatar = findViewById(R.id.ganti_avatar);

        // Ambil data dari Intent
        Intent intent = getIntent();
        editNama.setText(intent.getStringExtra("nama"));
        editUsername.setText(intent.getStringExtra("username"));
        String imageUri = intent.getStringExtra("imageUri");
        if (imageUri != null) {
            imageProfile.setImageURI(Uri.parse(imageUri));
            imageProfile.setTag(imageUri); // Simpan URI di tag
        }

        // Klik gambar untuk buka galeri
        imageProfile.setOnClickListener(v -> openGallery());
        gantiAvatar.setOnClickListener(v -> openGallery());

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Tombol selesai
        TextView btnSelesai = findViewById(R.id.btn_selesai);
        btnSelesai.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nama", editNama.getText().toString().trim());
            resultIntent.putExtra("username", editUsername.getText().toString().trim());
            resultIntent.putExtra("imageUri", imageProfile.getTag() != null ? imageProfile.getTag().toString() : null);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // Buka galeri
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                imageProfile.setImageURI(imageUri);
                imageProfile.setTag(imageUri.toString());
            }
        }
    }

}
