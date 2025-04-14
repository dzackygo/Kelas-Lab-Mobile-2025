package com.example.t1mobile_h071231080;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editUsername, editBio;
    private Button btnSave;
    private ImageView photo;
    private TextView editPhotoText;
    private Uri imageUri = null;

    // kode untuk identifikasi permintaan izin akses storage.
    private final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        photo = findViewById(R.id.photo);
        editPhotoText = findViewById(R.id.editPhotoText);
        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        editBio = findViewById(R.id.editBio);
        btnSave = findViewById(R.id.btnSave);

        // Memuat data yang dikirim dari MainActivity (seperti nama, username, dll).
        loadExistingData();

        // Minta  Izin Akses Storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }

        ActivityResultLauncher<Intent> openGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            imageUri = result.getData().getData();
                            if (imageUri != null) {
                                try {
                                    getContentResolver().takePersistableUriPermission(imageUri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    photo.setImageURI(imageUri);
                                    makeImageCircular(); //membulatkan gambar agar tampil seperti foto profil.

                                } catch (Exception e) {
                                    Toast.makeText(EditProfileActivity.this,
                                            "Gagal memuat gambar, silakan pilih ulang.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );


        photo.setOnClickListener(v -> openGalleryPicker(openGallery));
        editPhotoText.setOnClickListener(v -> openGalleryPicker(openGallery));

        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", editName.getText().toString().trim());
            resultIntent.putExtra("username", editUsername.getText().toString().trim());
            resultIntent.putExtra("bio", editBio.getText().toString().trim());

            if (imageUri != null) {
                resultIntent.putExtra("imageUri", imageUri.toString());
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openGalleryPicker(ActivityResultLauncher<Intent> launcher) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openGalleryIntent.setType("image/*");
        openGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openGalleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        launcher.launch(Intent.createChooser(openGalleryIntent, "Pilih gambar profil"));
    }

    //Mengubah gambar persegi menjadi bulat menggunakan RoundedBitmapDrawable
    private void makeImageCircular() {
        try {
            if (photo.getDrawable() instanceof BitmapDrawable) {
                BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    roundedDrawable.setCircular(true);
                    photo.setImageDrawable(roundedDrawable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Mengisi form dengan data sebelumnya (jika ada) dan menampilkan gambar yang sebelumnya dipilih.
    private void loadExistingData() {
        Intent intent = getIntent();
        if (intent != null) {
            editName.setText(intent.getStringExtra("current_name"));
            editUsername.setText(intent.getStringExtra("current_username"));
            editBio.setText(intent.getStringExtra("current_bio"));

            String imageUriStr = intent.getStringExtra("current_image_uri");
            if (imageUriStr != null && !imageUriStr.isEmpty()) {
                try {
                    imageUri = Uri.parse(imageUriStr);
                    getContentResolver().takePersistableUriPermission(imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    photo.setImageURI(imageUri);
                    photo.post(this::makeImageCircular);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
