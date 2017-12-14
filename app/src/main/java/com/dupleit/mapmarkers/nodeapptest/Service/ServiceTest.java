package com.dupleit.mapmarkers.nodeapptest.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dupleit.mapmarkers.nodeapptest.Client;
import com.dupleit.mapmarkers.nodeapptest.Main2Activity;
import com.dupleit.mapmarkers.nodeapptest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceTest extends Service {

	public Client socketConn;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mTimer = new Timer();
		mTimer.schedule(timerTask, 2000, 2 * 1000);
		Toast.makeText(this, "I am Started", Toast.LENGTH_SHORT).show();
		socketConn = new Client("192.168.0.7", 1234);
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

	public void notification(String message){
		//Log.d("ServerMessage",json);
		Log.d("ServerMessage",message);
		JSONObject jsonObject;
		String UserMessage="",userName="";
		int count = 0;
		//JsonObject obj = new JsonParser().parse(message).getAsJsonObject();
		Log.d("Jsonmessage",""+message);
		try {
			message = message.replaceAll("\\\\","");
			message = message.substring(1, message.length() - 1);
			jsonObject = new JSONObject(message);
			userName = jsonObject.getString("user");
			UserMessage = jsonObject.getString("data");
			//id = Integer.parseInt(jsonObject.getString("id"));
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
		Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
		long[] pattern = {0, 100, 1000};
		// The id of the channel.

		String CHANNEL_ID = "my_channel_01";
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.smartphone)
				.setContentTitle(UserMessage)
				.setContentText(userName)
				// .setNumber(count++)
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
		mBuilder.setContentText(UserMessage).setNumber(++count);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationId is a unique integer your app uses to identify the
		// notification. For example, to cancel the notification, you can pass its ID
		// number to NotificationManager.cancel().
               /* inboxStyle.setBigContentTitle("Enter Content Text");
                inboxStyle.addLine("hi events "+(count++));*/
		mNotificationManager.notify("Node App",5, mBuilder.build());

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
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private Timer mTimer;

	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			Log.e("Log", "Running");
		}
	};

	public void onDestroy() {
		try {
			mTimer.cancel();
			timerTask.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent("com.android.techtrainner");
		intent.putExtra("yourvalue", "torestore");
		sendBroadcast(intent);
	}
}
