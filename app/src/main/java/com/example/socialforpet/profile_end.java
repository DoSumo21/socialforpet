package com.example.socialforpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
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
    FirebaseUser pet;
    FirebaseStorage storageRef;
    FirebaseFirestore dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_end);
        img_avatar = findViewById(R.id.img_avatarPet);
        txt_Ten = findViewById(R.id.txt_Ten);
        pet = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance();
        dataRef = FirebaseFirestore.getInstance();

        dataRef.collection("users").document(pet.getUid()).collection("pets").document("Information")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            txt_Ten.setText(documentSnapshot.getData().get("ten").toString());
                            String duongdan = pet.getUid() +"/pets/avatar1.jpg";
                            storageRef.getReference().child(duongdan)
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(profile_end.this)
                                                    .load(uri.toString())
                                                    .into(img_avatar);                                        }
                                    });
                        }
                    }
                });
    }
}