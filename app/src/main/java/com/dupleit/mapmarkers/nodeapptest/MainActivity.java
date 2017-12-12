package com.dupleit.mapmarkers.nodeapptest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button button,galleryBtn;
    ImageView textView;
    private static final int GALLERY_REQUEST = 100;
    private static final int REQUEST= 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        galleryBtn = findViewById(R.id.galleryBtn);

        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton();
            }
        });

        Glide.with(this).load("http://192.168.0.7:3200/users/image/838605bcf04ea3871ec4bca10dfc2868.jpg").into(textView);


        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPremission();
            }
        });
    }

    private void checkPremission() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (!hasPermissions(this, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST );
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");
                    selectFromGallery();
                }
        } else {
            selectFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    selectFromGallery();
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
            break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                textView.setImageBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Log.d("base64_encodedImage",""+encoded);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
        }

    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void selectFromGallery() {
             /*showToast("Gallery");GALLERY_REQUEST*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    private void clickButton() {
        APIService service = ApiClient.getClient().create(APIService.class);
        Call<UserLogin> userCall = service.getpostcomment_request(1);
        userCall.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
               // Log.d("onSuccess",""+response.body());
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }


}
