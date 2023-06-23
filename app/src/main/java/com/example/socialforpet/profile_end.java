package com.example.socialforpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class profile_end extends AppCompatActivity implements IOnclick{

    ImageView img_avatar;
    TextView txt_Ten;
    FirebaseUser user;
    FirebaseStorage storageRef;
    FirebaseFirestore dataRef;

    long petId;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_end);

        rcvUser = findViewById(R.id.rcv_user);
        userAdapter = new UserAdapter(this,this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvUser.setLayoutManager(linearLayoutManager);

        userAdapter.setData(getListUser());

        rcvUser.setAdapter(userAdapter);


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
    private List<OJUser> getListUser(){
        List<OJUser> list = new ArrayList<>();

        list.add(new OJUser(R.drawable.pet1, "Cooper"));
        list.add(new OJUser(R.drawable.pet2, "Lola"));
        list.add(new OJUser(R.drawable.pet3, "Stella"));
        list.add(new OJUser(R.drawable.pet4, "Zoey"));
        list.add(new OJUser(R.drawable.pet5, "Henry"));

        list.add(new OJUser(R.drawable.pet6, "Ruby"));
        list.add(new OJUser(R.drawable.pet7, "Tucker"));
        list.add(new OJUser(R.drawable.pet8, "Winston"));

        list.add(new OJUser(R.drawable.pet9, "Willow"));
        list.add(new OJUser(R.drawable.pet10, "Louie"));
        list.add(new OJUser(R.drawable.pet11, "Ziggy"));
        list.add(new OJUser(R.drawable.pet12, "Penny"));



        return list;
    }

    @Override
    public void onclick(OJUser a) {
        img_avatar.setImageResource(a.getResourceId());
        txt_Ten.setText(a.getName());
    }
}