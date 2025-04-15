// MainActivity.java
package com.example.tp1_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Form.class);

                TextView name = findViewById(R.id.name);
                TextView username = findViewById(R.id.username);
                TextView mainQuote = findViewById(R.id.mainQuote);
                TextView secondQuote = findViewById(R.id.secondQuote);
                TextView company = findViewById(R.id.buildingText);
                TextView textView = findViewById(R.id.textView);

                String[] usernameParts = username.getText().toString().split(" • ");

                intent.putExtra("NAME", name.getText().toString());
                intent.putExtra("NICKNAME", usernameParts[0]);
                intent.putExtra("GENDER", usernameParts.length > 1 ? usernameParts[1] : "");
                intent.putExtra("STATUS", mainQuote.getText().toString());
                intent.putExtra("BIO", secondQuote.getText().toString());
                intent.putExtra("COMPANY", company.getText().toString());
                intent.putExtra("ADDRESS", textView.getText().toString());

                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            updateViews(data);
            updateProfileImage(data);
        }
    }

    private void updateViews(Intent data) {
        TextView nameTV = findViewById(R.id.name);
        TextView usernameTV = findViewById(R.id.username);
        TextView mainQuote = findViewById(R.id.mainQuote);
        TextView secondQuote = findViewById(R.id.secondQuote);
        TextView company = findViewById(R.id.buildingText);
        TextView textView = findViewById(R.id.textView);

        nameTV.setText(data.getStringExtra("NAME"));
        usernameTV.setText(data.getStringExtra("NICKNAME") + " • " + data.getStringExtra("GENDER"));
        mainQuote.setText(data.getStringExtra("STATUS"));
        secondQuote.setText(data.getStringExtra("BIO"));
        company.setText(data.getStringExtra("COMPANY"));
        textView.setText(data.getStringExtra("ADDRESS"));
    }

    private void updateProfileImage(Intent data) {
        if (data.hasExtra("PROFILE_IMAGE_PATH")) {
            String imagePath = data.getStringExtra("PROFILE_IMAGE_PATH");
            try {
                FileInputStream fis = openFileInput(imagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                ImageView profileImage = findViewById(R.id.profilePhoto);
                profileImage.setImageBitmap(bitmap);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}