package ntust.nwnc.homepage;

import ntust.nwnc.noma.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;



public class myAlarmNotificationService extends Service{
	//Service Type Name
	private static final String ALARM_TYPE="ALARM";
	//My Databsae
	AlarmDataBase alarmDataBase;

	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Toast.makeText(getApplicationContext(), "onDestory()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		//Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
		
		if(intent!=null)
			if(intent.getStringExtra("ServiceType").equals(ALARM_TYPE)) {	
				NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				
				Notification.Builder builder = new Notification.Builder(this);
				Intent notificationIntent = new Intent(this, AlarmActivity.class);
			    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			    //my strings
			    String contentTitle = "Title", contentText = "Content", contentInfo = "Info";
			    String Id = "id";
			    contentTitle = intent.getStringExtra("Title");
			  	contentText = intent.getStringExtra("Content");
			  	contentInfo = intent.getStringExtra("Info");
			  	Id = intent.getStringExtra("Id");
			  	
			    //builder
			    builder
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setTicker("一個新的提醒")
			    .setContentTitle(contentTitle)
			    .setContentText(contentText)
			    .setContentInfo(contentInfo)
			    .setDefaults(Notification.DEFAULT_SOUND)
			    .setLights(0xFFFFFFFF, 1000, 1000)
			    //vibrate
			    //long[] vibrate = {100, 300, 400, 300};
			    //.setVibrate(vibrate)
			    .setContentIntent(pendingIntent)
			    .setOnlyAlertOnce(true)
			    .setAutoCancel(true);
			    
			    Notification notification = builder.getNotification();
			    notificationManager.notify(R.drawable.ic_launcher, notification); 
			    //delete id from database
			    alarmDataBase = new AlarmDataBase(this);
			    alarmDataBase.createDataBaseTable();
			    alarmDataBase.deleteId(Integer.parseInt(Id));
			    //System.out.println("ABC: del "+Integer.parseInt(Id));    
			}
		
		return START_STICKY;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
