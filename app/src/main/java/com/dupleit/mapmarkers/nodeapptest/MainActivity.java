package com.dupleit.mapmarkers.nodeapptest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button button,galleryBtn;
    ImageView textView;
    private static final int GALLERY_REQUEST = 100;
    private static final int REQUEST= 112;
    public Client socketConn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        galleryBtn = findViewById(R.id.galleryBtn);

        socketConn = new Client("192.168.0.15", 1234);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton("Ha ha ha");
            }
        });

        Glide.with(this).load("http://192.168.0.15:3200/users/image/838605bcf04ea3871ec4bca10dfc2868.jpg").into(textView);

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPremission();
            }
        });

        socketConn.setClientCallback(new Client.ClientCallback () {
            @Override
            public void onMessage(String message) {
                Log.d("message",""+message);
                notification(message);
            }

            @Override
            public void onConnect(Socket socket) {


                JSONObject object=new JSONObject();
                try {
                    object.put("istyping",true);
                    object.put("data","Harry is typing");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //socket.getChannelByName("MyClassroom").publish(object);
                socketConn.send(String.valueOf(object));
               /* socket.send("Hello World!\n");
                socket.disconnect();*/
                //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisconnect(Socket socket, String message) {

            }

            @Override
            public void onConnectError(Socket socket, String message) {
            }
        });

        socketConn.connect();
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

    private void clickButton(String message) {
        /*APIService service = ApiClient.getClient().create(APIService.class);
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
        });*/
        JSONObject object=new JSONObject();
        Random random = new Random();
        try {
            object.put("user","Khalil");
            object.put("id",""+(random.nextInt(10) + 1));
            object.put("data","Hello BAck"+(random.nextInt(500) + 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socketConn.send(object.toString());

    }

    public void notification(String message){
        //Log.d("ServerMessage",json);
        Log.d("ServerMessage",message);
        JSONObject jsonObject;
        String UserMessage="",userName="";
        int id = 0;
        //JsonObject obj = new JsonParser().parse(message).getAsJsonObject();
        Log.d("Jsonmessage",""+message);
        try {
            message = message.replaceAll("\\\\","");
            message = message.substring(1, message.length() - 1);
            jsonObject = new JSONObject(message);
            userName = jsonObject.getString("user");
            UserMessage = jsonObject.getString("data");
            id = Integer.parseInt(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("errormessage",""+e.getMessage());
            //System.out.print(e.getCause().getMessage());
            Log.d("errormessage",stackTraceToString(e));
        }
        Log.d("decodedmessage",""+userName+"----"+UserMessage);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // The id of the channel.
            String id = "my_channel_01";
            // The user-visible name of the channel.
            CharSequence name = "ChannelName";
            // The user-visible description of the channel.
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            // Configure the notification channel.
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }else{*/
        long[] pattern = {0, 100, 1000};
            // The id of the channel.
                int count = 0;
                String CHANNEL_ID = "my_channel_01";
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.smartphone)
                .setContentTitle(userName)
                .setContentText(UserMessage)
                .setNumber(count)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(this, Main2Activity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your app to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(Main2Activity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // mNotificationId is a unique integer your app uses to identify the
                // notification. For example, to cancel the notification, you can pass its ID
                // number to NotificationManager.cancel().

                mNotificationManager.notify(id, mBuilder.build());

            //  That's it. Your user has now been notified.

      //  }
    }
    public String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
