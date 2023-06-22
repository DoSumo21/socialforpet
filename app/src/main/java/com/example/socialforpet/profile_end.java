package com.example.socialforpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class profile_end extends AppCompatActivity {

    ImageView img_avatar;
    TextView txt_Ten;
    FirebaseUser user;
    FirebaseStorage storageRef;
    FirebaseFirestore dataRef;

    long petId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_end);
        img_avatar = findViewById(R.id.img_avatarPet);
        txt_Ten = findViewById(R.id.txt_Ten);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance();
        dataRef = FirebaseFirestore.getInstance();

        petId = getIntent().getLongExtra("petId", 0);

        String duongdan = user.getUid() +"/pets/"+ petId +".jpg";
        storageRef.getReference().child(duongdan)
                .getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Glide.with(profile_end.this)
                        .load(uri.toString())
                        .into(img_avatar));
        dataRef.collection("users").document(user.getUid())
                .collection("pets").document(petId + "")
                .get().addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       DocumentSnapshot document = task.getResult();
                       Pet pet = document.toObject(Pet.class);
                       txt_Ten.setText(pet.getTen());
                   }
                });

    }
}