package ntust.nwnc.homepage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ntust.nwnc.noma.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;


public class AlarmActivity extends Activity {	
	private CustomNumberPicker mDateSpinner;  
    private CustomNumberPicker mHourSpinner;  
    private CustomNumberPicker mMinuteSpinner;  
    private Calendar mDate;  
    private int mHour, mMinute, mMonth, mDay;  //Value of Date  
    private String[] mDateDisplayValues = new String[7];  
    private OnDateTimeChangedListener mOnDateTimeChangedListener; 
    private Button btnDone;
    private ToggleButton isSetOnAlarm;
    private AlarmDataBase alarmDataBase;
    private ArrayList<Integer> idList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.alarm_set);

        btnDone = (Button) this.findViewById(R.id.btnDone);
        isSetOnAlarm = (ToggleButton) this.findViewById(R.id.tbAlarm);
        
        /*********************my set time init*********************/
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();  
        mySetTimeInit();
        
        /*********************press Done button*********************/
        btnDone.setOnTouchListener(new OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)  //按下的時候
					btnDone.setBackgroundColor(Color.GRAY);
		        if (event.getAction() == MotionEvent.ACTION_UP){   //起來的時候
		        	btnDone.setBackgroundColor(Color.parseColor("#21E1FE"));
		        	if(isSetOnAlarm.isChecked()) {
		        		myAlarmNotification();
		        		Toast.makeText(getApplicationContext(),"提醒將在"+mMonth+"月"+mDay+"日"+mHour+":"+mMinute+"啟動", Toast.LENGTH_LONG).show();
		        	}
		        	else {
		        		cancelAlarmNotification();
		        	}
		        	
		        	setContentView(R.layout.bonvouage);
		        	new Handler().postDelayed(new Runnable() {
		    			@Override
		    			public void run() {
		    				startActivity(new Intent(AlarmActivity.this, ntust.nwnc.homepage.HomePageActivity.class));
		    				AlarmActivity.this.finish();
		    			}
		    		}, 1000);
		    	
		        }
		        return false;
			}
		});
    }
    /*********************my Alarm Notification*********************/
    private void myAlarmNotification() { 
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, mMonth-1);
		calendar.set(Calendar.DAY_OF_MONTH, mDay);
		calendar.set(Calendar.HOUR_OF_DAY, mHour);
		calendar.set(Calendar.MINUTE, mMinute);
		
		AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent();
		intent.setClass(AlarmActivity.this, myAlarmNotificationService.class);
		
		int id = (int)System.currentTimeMillis();
		alarmDataBase = new AlarmDataBase(this);
		alarmDataBase.createDataBaseTable();
		alarmDataBase.insert(id);
		alarmDataBase.close();
		//set value to notification
		intent.putExtra("ServiceType", "ALARM");
		intent.putExtra("Title", "My Title");
		intent.putExtra("Content", mMonth+"月"+mDay+"日"+mHour+":"+mMinute+"的提醒");
		intent.putExtra("Info", "My Info");
		intent.putExtra("Id", String.format("%d", id));
		
		PendingIntent pendingIntent = PendingIntent.getService(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		//startService(intent);
		
    }
    /*********************cancel Alarm Notification*********************/
    private void cancelAlarmNotification() {
    	alarmDataBase = new AlarmDataBase(this);
    	idList = new ArrayList<Integer>();
    	alarmDataBase.createDataBaseTable();
    	idList = alarmDataBase.getMyData();
    	for(int i=0; i<idList.size(); i++) {
    		AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
    		Intent intent = new Intent();
    		intent.setClass(AlarmActivity.this, myAlarmNotificationService.class);
    		PendingIntent pendingIntent = PendingIntent.getService(this, idList.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    		alarmManager.cancel(pendingIntent);
    		alarmDataBase.delete(0);
    		//System.out.println("ABC:"+idList.get(i));
    	}
    	alarmDataBase.close();
    }
    
    private void mySetTimeInit() {
    	mDate = Calendar.getInstance();  
        mHour = mDate.get(Calendar.HOUR_OF_DAY);  
        mMinute = mDate.get(Calendar.MINUTE);  
				
        mDateSpinner = (CustomNumberPicker) this.findViewById(R.id.np_date);  
        mDateSpinner.getChildAt(0).setFocusable(false);
        mDateSpinner.setMinValue(0);  
        mDateSpinner.setMaxValue(6);  
        updateDateControl();  
        mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);  
  
        mHourSpinner = (CustomNumberPicker) this.findViewById(R.id.np_hour);  
        mHourSpinner.getChildAt(0).setFocusable(false);
        mHourSpinner.setMaxValue(23);  
        mHourSpinner.setMinValue(0);  
        mHourSpinner.setValue(mHour);  
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);  
  
        mMinuteSpinner = (CustomNumberPicker) this.findViewById(R.id.np_minute);  
        mMinuteSpinner.getChildAt(0).setFocusable(false);
        mMinuteSpinner.setMaxValue(59);  
        mMinuteSpinner.setMinValue(0);  
        mMinuteSpinner.setValue(mMinute);  
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener); 
    }
    
    private NumberPicker.OnValueChangeListener mOnDateChangedListener = new OnValueChangeListener() {  
        @Override  
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {  
            mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);  

            updateDateControl();  

            onDateTimeChanged();  
        }  
    };  
    
    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new OnValueChangeListener() {  
        @Override  
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {  
            mHour = mHourSpinner.getValue();  
            onDateTimeChanged();  
        }  
    };  
  
    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new OnValueChangeListener() {  
        @Override  
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {  
            mMinute = mMinuteSpinner.getValue();  
            onDateTimeChanged();  
        }  
    };  
  
    private void updateDateControl() {    
        Calendar cal = Calendar.getInstance();  
        cal.setTimeInMillis(mDate.getTimeInMillis());  
        cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);  
        mDateSpinner.setDisplayedValues(null);  
        for (int i = 0; i < 7; ++i) {  
            cal.add(Calendar.DAY_OF_YEAR, 1);  
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd",  
                    Locale.ENGLISH);  
            mDateDisplayValues[i] = (String) dateFormat.format(cal.getTime());  
        }  
        /*********************get my month and day*********************/
        Calendar mycal = cal; //tmp cal
        //Month Value
        mycal.add(Calendar.DAY_OF_YEAR, -3);
        mMonth = Integer.parseInt((String)DateFormat.format("MM",  
        		mycal)); 
        //Day Value
        mDay = Integer.parseInt((String)DateFormat.format("dd",  
        		mycal));
        /**************************************************************/
        mDateSpinner.setDisplayedValues(mDateDisplayValues);  
        mDateSpinner.setValue(7/2);  
        mDateSpinner.invalidate();  
    }  
      
    public interface OnDateTimeChangedListener {  
        void onDateTimeChanged(AlarmActivity view, int year, int month,  
                int day, int hour, int minute);  
    }  
  
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {  
        mOnDateTimeChangedListener = callback;  
    }  
      
    private void onDateTimeChanged() {  
        if (mOnDateTimeChangedListener != null) {  
            mOnDateTimeChangedListener.onDateTimeChanged(this,mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),  
                mDate.get(Calendar.DAY_OF_MONTH), mHour, mMinute);           
        }     
    }  
    /*********************end*********************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.noma_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        return super.onOptionsItemSelected(item);
    }
}
