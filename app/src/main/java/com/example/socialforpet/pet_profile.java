package com.example.socialforpet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class pet_profile extends AppCompatActivity {
    EditText ten, loai, giong, trongluong, ngaysinh;
    RadioButton rdDuc, rdCai;
    Button btnCapNhat;

    Bitmap avatarBitmap;
    private ImageButton btnCaNhan ;
    private ImageButton btnKhamPha ;
    private ImageButton btnPet ;

    private boolean DaThemHinh = false;

    private FirebaseFirestore db;

    private FirebaseUser user;
    Pet thucung ;
    private static final int CHANGE_AVATAR = 101;
    private CircleImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnCaNhan = findViewById(R.id.imgBtn_CaNhan);
        btnKhamPha = findViewById(R.id.imgBtn_KhamPha);
        btnPet = findViewById(R.id.imgBtn_Pet);
        avatar = findViewById(R.id.imageView3);
        btnCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pet_profile.this, edit_profile.class );
                startActivity(intent);
            }
        });
        btnPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pet_profile.this, pet_profile.class );
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

    @Override
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
    private void AddControl(){
        ten = findViewById(R.id.edtTen);
        loai = findViewById(R.id.edtLoai);
        giong = findViewById(R.id.edtGiong);
        trongluong = findViewById(R.id.edtTrongLuong);
        ngaysinh = findViewById(R.id.editTextDate);
        rdDuc = findViewById(R.id.rdDuc);
        rdCai = findViewById(R.id.rdCai);
        btnCapNhat = findViewById(R.id.btnUpdate);
    }
    private void AddEvents(){
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thucung = new Pet();
                try {
                    thucung = AddPet();
                    db.collection("users").document(user.getUid()).collection("pets").document(thucung.getId() + "").set(thucung)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(pet_profile.this, " Đã thêm thành công vào database ", Toast.LENGTH_SHORT).show();
                                }
                            });
                    if (DaThemHinh) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference image = storageReference.child(user.getUid() + "/pets/" + thucung.getId() + ".jpg");
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(),  avatarBitmap, thucung.getId() + "", "image");
                        Uri uri = Uri.parse(path);
                        image.putFile(uri).addOnCompleteListener(task -> {
                            File fdelete = new File(getRealPathFromURI(pet_profile.this, uri));
                            if (fdelete.exists()) {
                                fdelete.delete();
                            }
                        });
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Log.e( "AAA", "onClick: " + thucung.getTen() + thucung.getLoai() + thucung.getGiong() + thucung.getNgaySinh() + thucung.isGioiTinh() + thucung.getTrongLuong());
            }
        });
    }
    private Pet AddPet () throws ParseException {
        String addten = ten.getText().toString();
        String addloai= loai.getText().toString();
        String addgiong= giong.getText().toString();
        Float addtrongluong = Float.parseFloat(trongluong.getText().toString());
        String addngaysinh = ngaysinh.getText().toString()  ;
        Boolean addgioitinh;
        if (rdDuc.isEnabled()){
            addgioitinh = true;
        } else if (rdCai.isEnabled()) {
            addgioitinh = false;
        }else {
            addgioitinh = true;
        }

        if(addten.isEmpty()){
            ten.setError("Không được để trống");
            ten.requestFocus();
        }
        else if (addgiong.isEmpty())
        {
            giong.setError("Không được để trống");
            giong.requestFocus();
        }
        else if(addloai.isEmpty()){
            loai.setError("Không được để trống");
            loai.requestFocus();
        }
        else if (addngaysinh.isEmpty())
        {
            ngaysinh.setError("Không được để trống");
            ngaysinh.requestFocus();
        }
        else if (addtrongluong <= 1 || addtrongluong >= 200)
        {
            trongluong.setError("Nhập trọng lượng thú cưng 1 - 200kg");
            trongluong.requestFocus();
        }


        Pet pet = new Pet();
        pet.setId(System.currentTimeMillis());
        pet.setTen(addten);
        pet.setGiong(addgiong);
        pet.setLoai(addloai);
        pet.setTrongLuong(addtrongluong);
        pet.setNgaySinh(addngaysinh);
        pet.setGioiTinh(addgioitinh);
        return pet;
    }
}