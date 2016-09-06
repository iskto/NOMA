package ntust.nwnc.maproute;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import ntust.nwnc.noma.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;


public class MapRouteActivity extends Activity {	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private MapRouteAdapter mr_adapter; //custom adapter
	private ArrayList<HashMap<String, Object>> mr_list = null; //map route list
	private Button btnAddRoute; //add button
	private int tmpHour, tmpMin; //tmp time
	MapRouteDataBase MPdatabase; //my SQLite
	private String RouteDay = "July 20"; //要從地圖傳入的日期
	private String tempTable = "julytwenty"; //創建的TABLE NAME
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.map_list_drawer);
        /*********************init*********************/
        initDrawer();
        btnAddRoute = (Button) this.findViewById(R.id.btnAddRoute);
        mr_list = new ArrayList<HashMap<String,Object>>();
        MPdatabase = new MapRouteDataBase(this); 
        //input route day
        MPdatabase.createDataBaseTable(tempTable);
        if(MPdatabase.getMyData(tempTable).isEmpty()) {
        	MPdatabase.insert(tempTable, RouteDay, "", "");
        	MPdatabase.close();
        }
        
        mr_list = MPdatabase.getMyData(tempTable);
        
        
        //mr_renew();
   
        /*********************custom Listview*********************/
        mr_adapter = new MapRouteAdapter(this, mr_list);
        mDrawerList.setAdapter(mr_adapter); 

        /*********************add button action*********************/
        btnAddRoute.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)  //按下的時候
					btnAddRoute.setBackgroundResource(R.drawable.btnaddroute_down);
		        if (event.getAction() == MotionEvent.ACTION_UP){   //起來的時候
		        	btnAddRoute.setBackgroundResource(R.drawable.btnaddroute);
		        	
		        	btnAddRoutePressed();
		        }
				return false;
			}
		});
        /*********************delete item action*********************/
        mDrawerList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
				 int position, long id) {
				// TODO Auto-generated method stub
				if(position!=0)
					DelRoutePressed(position);
			return false;
		}});
        
        
    }
    
    /*********************init drawerlayout*********************/    
    private void initDrawer() {
    	/*********************init drawer*********************/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.map_DrawerLayout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);  
    }
    /*********************btnAddRoute Pressed AlertDialog*********************/
    private void btnAddRoutePressed() {
    	/*********************AlertDialog*********************/
    	Builder MsgAlertDialog = new AlertDialog.Builder(MapRouteActivity.this);	
    	//LayoutInflate
    	LayoutInflater inflater = getLayoutInflater();
    	//View
    	@SuppressWarnings("unused")
		final View editView = inflater.inflate(R.layout.map_route_add, null);
    	//edittext
    	final EditText editAdd = (EditText) editView.findViewById(R.id.editRouteLoc);
    	//times
		Calendar TodayDate = Calendar.getInstance();    
	    int hour = TodayDate.get(Calendar.HOUR_OF_DAY);       
	    int minute  = TodayDate.get(Calendar.MINUTE);  
	    tmpHour = hour; tmpMin = minute;  //set time
	    TimePicker timePickerRoute = (TimePicker) editView.findViewById(R.id.timePickerRoute);        
	    timePickerRoute.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
	    timePickerRoute.setIs24HourView(true);
	    timePickerRoute.setOnTimeChangedListener(new OnTimeChangedListener(){
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				tmpHour = hourOfDay; tmpMin = minute;  //set time
			}});
	    //AlertDialog
    	MsgAlertDialog
    	.setView(editView)
    	.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String tmpLoc = editAdd.getText().toString();
				mr_add(tmpLoc, tmpHour, tmpMin);
				mDrawerList.setSelection(mr_adapter.getCount()+1);
			}})
		.setNegativeButton("Cancel", null)
		.show();	
    }
    /*********************delete Route Pressed AlertDialog*********************/
    private void DelRoutePressed(int position) {
    	final int pos = position;
		if(pos!=0){ //is not zero cell
			new AlertDialog.Builder(MapRouteActivity.this)
            .setMessage("Delete?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                	mr_del(pos);
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
		}
    }
    /*********************add map route listview*********************/
    public void mr_add(String loc, int hour, int min) {
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("RouteLoc", loc);
    	map.put("RouteTime", String.format("%02d:%02d", hour, min));
    	mr_list.add(map); 
    	MPdatabase.insert(tempTable, RouteDay, loc, String.format("%02d:%02d", hour, min));
    	MPdatabase.close();
    	mDrawerList.setAdapter(mr_adapter); 
    	mDrawerList.setSelection(mr_adapter.getCount());
    }
    /*********************delete map route listview*********************/
    public void mr_del(int pos) {
    	mr_list.remove(pos);
    	MPdatabase.delete(tempTable, pos);
    	MPdatabase.close();
    	mDrawerList.setAdapter(mr_adapter); 
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
