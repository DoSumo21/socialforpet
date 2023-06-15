package com.example.socialforpet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class pet_profile extends AppCompatActivity {
    private static final int CHANGE_AVATAR = 101;
    private CircleImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        avatar = findViewById(R.id.imageView3);
        avatar.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType("image/*");
            startActivityForResult(gallery, CHANGE_AVATAR);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHANGE_AVATAR && resultCode == RESULT_OK && null != data) {
            Uri uri  =data.getData();
            Bitmap avatarBitmap;
            try {
                avatarBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), uri);
                avatar.setImageBitmap(avatarBitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}