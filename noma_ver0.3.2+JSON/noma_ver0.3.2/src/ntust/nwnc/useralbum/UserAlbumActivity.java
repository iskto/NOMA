package ntust.nwnc.useralbum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ntust.nwnc.noma.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;

public class UserAlbumActivity extends Activity{
	//album
	private GridView gvAlbum;
	Context context; //Context
	ArrayList<HashMap<String, Object>> AlbumList = null; //list
	private UserAlbumAdapter album_adapter; //custom adapter
	private List<String> thumbs;  //存放縮圖的id
	//action
	private ViewGroup layout;
	int pos;
	float x1, x2, y1, y2; //滑動的起始與末點
	DisplayMetrics dm;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.album_user);
		/*********************init*********************/
		context = this;
		layout = (ViewGroup) this.findViewById(R.id.gvAlbum);
		//螢幕解析
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		/*********************get position*********************/
		 Intent intent = this.getIntent();
		 Bundle bundle = intent.getExtras();
		 if(bundle!=null)
			pos = bundle.getInt("Position");

        /*********************SD image*********************/
		ContentResolver cr = getContentResolver();
	        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
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
		album_adapter = new UserAlbumAdapter(this, thumbs);
		gvAlbum = (GridView)this.findViewById(R.id.gvAlbum);
		gvAlbum.setAdapter(album_adapter); 
		
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
		        		backUserShare(pos);
		        }
				return false;
			}
		});
		
	}
	/*********************back to UserShare ListView*********************/
	private void backUserShare(int pos) {
		Intent intent = new Intent();
		
    	intent.setClass(UserAlbumActivity.this, ntust.nwnc.sharepage.SharePagerActivity.class);	
    	Bundle bundle = new Bundle();
    	bundle.putString("ShareType", "user");
    	bundle.putInt("Position", pos);
    	intent.putExtras(bundle);
    	startActivity(intent); 
    	UserAlbumActivity.this.finish();	
	}
	
	
	
}