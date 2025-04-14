package com.example.t1mobile_h071231080;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textName, textUsername, textBio;
    private ImageView profileImage, bottomProfileImage;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    private String currentImageUriString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inisialisasi elemen UI
        textName = findViewById(R.id.textView);
        textUsername = findViewById(R.id.textView5);
        textBio = findViewById(R.id.textView3);
        profileImage = findViewById(R.id.imageView);
        bottomProfileImage = findViewById(R.id.imageView);



        // Launcher untuk menerima hasil dari EditProfileActivity
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        updateProfileWithNewData(data);
                    }
                }
        );
    }

    // Method untuk membuka EditProfileActivity dan mengirim data terbaru
    public void goToEditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);

        // Kirim data terbaru ke EditProfileActivity
        intent.putExtra("current_name", textName.getText().toString());
        intent.putExtra("current_username", textUsername.getText().toString());
        intent.putExtra("current_bio", textBio.getText().toString());
        intent.putExtra("current_image_uri", currentImageUriString != null ? currentImageUriString : "");

        editProfileLauncher.launch(intent);
    }

    // Method untuk menerima dan menampilkan data dari EditProfileActivity
    private void updateProfileWithNewData(Intent data) {
        if (data != null) {
            // Ambil data dari intent
            String name = data.getStringExtra("name");
            String username = data.getStringExtra("username");
            String bio = data.getStringExtra("bio");
            String imageUri = data.getStringExtra("imageUri");

            // Update UI jika data tersedia
            if (name != null && !name.isEmpty()) {
                textName.setText(name);
            }

            if (username != null && !username.isEmpty()) {
                textUsername.setText(username);
            }

            if (bio != null) {
                textBio.setText(bio);
            }

            if (imageUri != null && !imageUri.isEmpty()) {
                try {
                    Uri uri = Uri.parse(imageUri);
                    profileImage.setImageURI(uri);
                    bottomProfileImage.setImageURI(uri);

                    // Bikin bundar setelah gambar dimuat
                    profileImage.post(() -> makeImageCircular(profileImage));
                    bottomProfileImage.post(() -> makeImageCircular(bottomProfileImage));


                    currentImageUriString = imageUri;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Fungsi bantu untuk membuat gambar jadi bundar
    private void makeImageCircular(ImageView imageView) {
        try {
            if (imageView.getDrawable() != null && imageView.getDrawable() instanceof BitmapDrawable) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    roundedDrawable.setCircular(true);
                    imageView.setImageDrawable(roundedDrawable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
