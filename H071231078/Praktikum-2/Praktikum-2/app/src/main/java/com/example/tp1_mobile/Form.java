// Form.java
package com.example.tp1_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Form extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etName, etNickname, etStatus, etBio, etCompany, etAddress;
    Spinner genderSpinner;
    private ImageView profilePhoto;
    private MaterialCardView tvChangePhoto;
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etName = findViewById(R.id.et_name);
        etNickname = findViewById(R.id.et_nickname);
        genderSpinner = findViewById(R.id.sp_gender);
        etStatus = findViewById(R.id.et_status);
        etBio = findViewById(R.id.et_bio);
        etCompany = findViewById(R.id.et_company);
        etAddress = findViewById(R.id.et_address);
        profilePhoto = findViewById(R.id.profilePhoto);
        tvChangePhoto = findViewById(R.id.tv_change_photo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        String selectedGender = null;


        Intent intent = getIntent();
        if (intent != null) {
            etName.setText(intent.getStringExtra("NAME"));
            etNickname.setText(intent.getStringExtra("NICKNAME"));
            selectedGender = intent.getStringExtra("GENDER");
            etStatus.setText(intent.getStringExtra("STATUS"));
            etBio.setText(intent.getStringExtra("BIO"));
            etCompany.setText(intent.getStringExtra("COMPANY"));
            etAddress.setText(intent.getStringExtra("ADDRESS"));
        }

        if (selectedGender != null) {
            int spinnerPosition = adapter.getPosition(selectedGender);
            genderSpinner.setSelection(spinnerPosition);
        }

        profilePhoto.setOnClickListener(v -> openImageChooser());
        tvChangePhoto.setOnClickListener(v -> openImageChooser());

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> saveProfileChanges());
    }



    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            handleSelectedImage(data.getData());
        }
    }

    private void handleSelectedImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            profilePhoto.setImageBitmap(bitmap);
            currentImagePath = saveImageToStorage(bitmap);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveImageToStorage(Bitmap bitmap) {
        String fileName = "profile_" + System.currentTimeMillis() + ".jpg";
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveProfileChanges() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NAME", etName.getText().toString());
        resultIntent.putExtra("NICKNAME", etNickname.getText().toString());
        resultIntent.putExtra("GENDER", genderSpinner.getSelectedItem().toString()); // Diubah ke Spinner
        resultIntent.putExtra("STATUS", etStatus.getText().toString());
        resultIntent.putExtra("BIO", etBio.getText().toString());
        resultIntent.putExtra("COMPANY", etCompany.getText().toString());
        resultIntent.putExtra("ADDRESS", etAddress.getText().toString());

        if (currentImagePath != null) {
            resultIntent.putExtra("PROFILE_IMAGE_PATH", currentImagePath);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}