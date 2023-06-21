package com.example.socialforpet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profile extends AppCompatActivity {
    EditText hoten, sdt, email, gioitinh;
    CheckBox rdNam, rdNu;
    Bitmap avatarBitmap;
    Button btnCapNhat;
    ImageButton btnPet;
    User NguoiDung;
    private FirebaseFirestore db;

    private boolean DaThemHinh = false;
    private FirebaseUser user;
    private CircleImageView avatar;

    private static final int CHANGE_AVATAR = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        avatar = findViewById(R.id.imageView);
        btnPet = findViewById(R.id.btnPet);
        btnCapNhat = findViewById(R.id.btn_CapNhat);
        btnPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_profile.this, pet_profile.class );
                startActivity(intent);
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_profile.this, profile_end.class );
                startActivity(intent);
            }
        });

        avatar.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType("image/*");
            startActivityForResult(gallery, CHANGE_AVATAR);
        });
        AddControl();
        AddEvents();
    }
    public void change(View view){

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHANGE_AVATAR && resultCode == RESULT_OK && null != data) {
            Uri uri  =data.getData();

            try {
                avatarBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), uri);
                avatar.setImageBitmap(avatarBitmap);
                DaThemHinh = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void AddEvents(){
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NguoiDung = new User();
                try {
                    NguoiDung = AddUser();
                    db.collection("users").document(user.getUid()).collection("users").document(NguoiDung.getId() + "").set(NguoiDung)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(edit_profile.this, " Đã thêm thành công vào database ", Toast.LENGTH_SHORT).show();
                                }
                            });
                    if (DaThemHinh) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference image = storageReference.child(user.getUid() + "/users/" + NguoiDung.getId() + ".jpg");
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(),  avatarBitmap, NguoiDung.getId() + "", "image");
                        Uri uri = Uri.parse(path);
                        image.putFile(uri).addOnCompleteListener(task -> {
                            File fdelete = new File(getRealPathFromURI(edit_profile.this, uri));
                            if (fdelete.exists()) {
                                fdelete.delete();
                            }
                        });
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Log.e( "AAA", "onClick: " + NguoiDung.getHoTen() + NguoiDung.getEmail() + NguoiDung.getPhoneNumber() + NguoiDung.isGioiTinh());
            }
        });
    }

    private void AddControl(){
        hoten = findViewById(R.id.edtHoTen);
        sdt = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.edtEmail);
        rdNam = findViewById(R.id.cbNam);
        rdNu = findViewById(R.id.cbNu);
        btnCapNhat = findViewById(R.id.btn_CapNhat);
    }
    private User AddUser () throws ParseException {
        String addten = hoten.getText().toString();
        String addsdt= sdt.getText().toString();
        String addemail= email.getText().toString();



        Boolean addgioitinh;
        if (rdNam.isEnabled()){
            addgioitinh = true;
        } else if (rdNu.isEnabled()) {
            addgioitinh = false;
        }else {
            addgioitinh = true;
        }

        if(addten.isEmpty()){
            hoten.setError("Không được để trống");
            hoten.requestFocus();
        }
        else if (addsdt.isEmpty())
        {
            sdt.setError("Không được để trống");
            sdt.requestFocus();
        }
        else if(addemail.isEmpty()){
            email.setError("Không được để trống");
            email.requestFocus();
        }

        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setHoTen(addten);
        user.setEmail(addemail);
        user.setPhoneNumber(addsdt);
        user.setGioiTinh(addgioitinh);
        return user;
    }
}