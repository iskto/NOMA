package ntust.nwnc.storephoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ntust.nwnc.noma.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class StorePhotoActivity extends Activity{
	//photo
	private GridView gvPhoto;
	Context context; //Context
	ArrayList<HashMap<String, Object>> PhotoList = null; //list
	private StorePhotoAdapter photo_adapter; //custom adapter
	private List<String> thumbs;  //存放縮圖的id
	//action
	private ViewGroup layout;
	TextView txtPhotoTitle;
	Button btnBuy, btnBackTitle;
	int pos;
	float x1, x2, y1, y2; //滑動的起始與末點
	DisplayMetrics dm;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.photo_store);
		/*********************init*********************/
		context = this;
		ContentResolver cr = getContentResolver();
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        layout = (ViewGroup) this.findViewById(R.id.gvAlbum);
        txtPhotoTitle = (TextView) this.findViewById(R.id.txtPhotoTitle);
		btnBuy = (Button) this.findViewById(R.id.btnBuy);
		btnBackTitle = (Button) this.findViewById(R.id.btnBackTitle);
		//螢幕解析
		dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

		/*********************set title name*********************/
		 Intent intent = this.getIntent();
		 Bundle bundle = intent.getExtras();
		 txtPhotoTitle.setText(bundle.getString("StoreShareLoc")); 
		 pos = bundle.getInt("Position");
        /*********************SD image*********************/
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        thumbs = new ArrayList<String>();

        for (int i = 0; i < cursor.getCount(); i++) {
             cursor.moveToPosition(i);
             /*String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑

             thumbs.add(filepath);*/
             
             int id = cursor.getInt(cursor
                       .getColumnIndex(MediaStore.Images.Media._ID));// ID
             thumbs.add(id + "");
        }
        cursor.close();

		/*********************custom Listview*********************/
		photo_adapter = new StorePhotoAdapter(this, thumbs);
		gvPhoto = (GridView)this.findViewById(R.id.gvAlbum);
		gvPhoto.setAdapter(photo_adapter); 
		
		/*********************Button btnBuy pressed*********************/
		btnBuy.setOnTouchListener(new OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)  //按下的時候
					btnBuy.setBackgroundColor(Color.GRAY);
		        if (event.getAction() == MotionEvent.ACTION_UP){   //起來的時候
		        	btnBuy.setBackgroundColor(Color.parseColor("#FF5E4C"));
		        	
		        }
		        return false;
			}
		});
		/*********************Button btnBackTitle pressed*********************/
		btnBackTitle.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backStoreShare(pos);
			}
		});
		/*********************slide back*********************/
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN) {  //按下的時候
					x1 = event.getX();
					y1 = event.getY();
				}
		        if (event.getAction() == MotionEvent.ACTION_UP) {   //起來的時候
		        	x2 = event.getX();
		        	y2 = event.getY();  
		        	if((x2 - x1 > dm.widthPixels/4 && Math.abs(y2 - y1) <80) || x2 - x1 > dm.widthPixels/3) 
		        		backStoreShare(pos);
		        }
				return false;
			}
		});
	}
	/*********************back to StoreShare ListView*********************/
	private void backStoreShare(int pos) {
		Intent intent = new Intent();
		
    	intent.setClass(StorePhotoActivity.this, ntust.nwnc.sharepage.SharePagerActivity.class);	
    	Bundle bundle = new Bundle();
    	bundle.putString("ShareType", "store");
    	bundle.putInt("Position", pos);
    	intent.putExtras(bundle);
    	startActivity(intent); 
    	StorePhotoActivity.this.finish();	
	}

	
}