package ntust.nwnc.homepage;

import ntust.nwnc.noma.NomaMainActivity;
import ntust.nwnc.noma.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;


public class HomePageActivity extends Activity {	
	Button btnNewPlan, btnRecord, btnShare;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.homepage);
        /*********************init*********************/  
        btnNewPlan = (Button) this.findViewById(R.id.btnGoogle);
        btnRecord = (Button) this.findViewById(R.id.btnTwitter);
        btnShare = (Button) this.findViewById(R.id.btnShare);
        /*********************Touch Button*********************/
        //new plan button
        btnNewPlan.setOnTouchListener(new OnTouchListener(){  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)  //按下的時候
					btnNewPlan.setBackgroundResource(R.drawable.newplan_down);
				if (event.getAction() == MotionEvent.ACTION_UP){   //起來的時候
		        	btnNewPlan.setBackgroundResource(R.drawable.newplan);
		        	Intent intent = new Intent();
			    	intent.setClass(HomePageActivity.this, ntust.nwnc.homepage.AlarmActivity.class);	
			    	startActivity(intent); 
			    	HomePageActivity.this.finish();
		        }
				return false;
			}});
        //record button
        btnRecord.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)  //按下的時候
					btnRecord.setBackgroundResource(R.drawable.record_down);
		        if (event.getAction() == MotionEvent.ACTION_UP){   //起來的時候
		        	btnRecord.setBackgroundResource(R.drawable.record);
		        	Intent intent = new Intent();
		        	intent.putExtra("lastPage", "HomePageActivity");
			    	intent.setClass(HomePageActivity.this, ntust.nwnc.useralbum.UserAlbumActivity.class);	
			    	startActivity(intent); 
			    	HomePageActivity.this.finish();
		        }
				return false;
			}});
        //share button
        btnShare.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)  //按下的時候
					btnShare.setBackgroundResource(R.drawable.share_down);
		        if (event.getAction() == MotionEvent.ACTION_UP){   //起來的時候
		        	btnShare.setBackgroundResource(R.drawable.share);
		        	Intent intent = new Intent();
			    	intent.setClass(HomePageActivity.this, ntust.nwnc.sharepage.SharePagerActivity.class);	
			    	startActivity(intent); 
			    	HomePageActivity.this.finish();
		        }
				return false;
			}});
        
    }


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
