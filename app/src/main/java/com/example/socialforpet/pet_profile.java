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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class pet_profile extends AppCompatActivity {
    EditText ten, loai, giong, trongluong, ngaysinh;
    RadioButton rdDuc, rdCai;
    Button btnCapNhat;

    private ImageButton btnCaNhan ;
    private ImageButton btnKhamPha ;
    private ImageButton btnPet ;

    Pet thucung ;
    private static final int CHANGE_AVATAR = 101;
    private CircleImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
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
        Pet pet = new Pet();
        pet.setTen(addten);
        pet.setGiong(addgiong);
        pet.setLoai(addloai);
        pet.setTrongLuong(addtrongluong);
        pet.setNgaySinh(addngaysinh);
        pet.setGioiTinh(addgioitinh);
        return pet;
    }
}