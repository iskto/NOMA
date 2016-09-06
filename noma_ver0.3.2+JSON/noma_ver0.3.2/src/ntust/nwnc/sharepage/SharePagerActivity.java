package ntust.nwnc.sharepage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ntust.nwnc.noma.R;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class SharePagerActivity extends Activity{
	//user share
	private ListView lvUserShare; //user share ListView
	Context context; //Context
	ArrayList<HashMap<String, Object>> us_list = null; //user share list
	private UserSharePagerAdapter us_adapter; //custom adapter
	//store share
	private ListView lvStoreShare; //store share ListView
	ArrayList<HashMap<String, Object>> ss_list = null; //store share list
	private StoreSharePagerAdapter ss_adapter; //custom adapter
	//viewpager
	private boolean backhome = false;
	private int currentView = 0;
	private ViewPager viewpager; //pager view
	private List<ListView> mListViews = new ArrayList<ListView>(); //viewpager's list
	//animation
	private View animation; 
	int touchSlop = 10;
	private SearchView searchView;
	private Spinner SortStyle;
	private String[] sort_styles = {"最新", "評價"};
	int select_sortstyle0 = 0, select_sortstyle1 = 0;  //0最新 1評價
	private Button btnGoTop;
	//--------StoreJSONData物件--------//
	UserJSONData userJsondata;
	StoreJSONData storeJsonData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.share_pager);
		/*********************init*********************/
		context = this;
		touchSlop = (int) (ViewConfiguration.get(SharePagerActivity.this).getScaledTouchSlop() * 0.9);
		animation = findViewById(R.id.animation);
		viewpager = (ViewPager) this.findViewById(R.id.viewpager);
		searchView = (SearchView) this.findViewById(R.id.searchView);
		SortStyle = (Spinner) this.findViewById(R.id.SortStyle);
		btnGoTop = (Button) this.findViewById(R.id.btnGoTop);
        us_list = new ArrayList<HashMap<String,Object>>();
        ss_list = new ArrayList<HashMap<String,Object>>();
        /*********************產生UserJSONData物件*********************/
        userJsondata=new UserJSONData(SharePagerActivity.this);
      	/*********************判斷連接到server是否成功，isFindJSONData()*********************/
      	
        if(!userJsondata.isFindJSONData()){
      		Intent intent = new Intent();
      		intent.setClass(this, ntust.nwnc.homepage.HomePageActivity.class);     		
    		startActivity(intent); 
    		Toast.makeText(this,"Connection time out, please check server IP is valid.", Toast.LENGTH_LONG).show();
    		this.finish();
    		return;
      	}
		/*********************load data from json*********************/
      	us_loadData();
		/*********************custom Listview*********************/
		//user share
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.store_pager_header, null);
		View view1 = inflater.inflate(R.layout.user_share_list, null);
        us_adapter = new UserSharePagerAdapter(this, us_list);
        lvUserShare = (ListView) view1.findViewById(R.id.lvUserShare);
        lvUserShare.addHeaderView(headerView);
        lvUserShare.setAdapter(us_adapter); 
        //store share
        View view2 = inflater.inflate(R.layout.store_share_list, null);
        ss_adapter = new StoreSharePagerAdapter(this, ss_list);
        lvStoreShare = (ListView) view2.findViewById(R.id.lvStoreShare);
        lvStoreShare.addHeaderView(headerView);
        lvStoreShare.setAdapter(ss_adapter);   
        /*********************viewpager*********************/
        initPagerData(); //load data
		viewpager.setAdapter(new PagerAdapter()  
        {  
            @Override  
            public Object instantiateItem(ViewGroup container, int position)  
            {  
                container.addView(mListViews.get(position));  
                return mListViews.get(position);  
            }  
  
            @Override  
            public void destroyItem(ViewGroup container, int position,  
                    Object object)  
            {  
  
                container.removeView(mListViews.get(position));  
            }  
  
            @Override  
            public boolean isViewFromObject(View view, Object object)  
            {  
                return view == object;  
            }  
  
            @Override  
            public int getCount()  
            {  
                return mListViews.size();  
            }
        });  
		//change page style
		viewpager.setPageTransformer(true, new DepthPageTransformer()); 
		/*********************back and action*********************/
        backSelfAction();
        /*********************Listview Cell*********************/
        //click user share cell
        lvUserShare.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				final int pos = position-1;
				if(pos>=0)
					toAlbum(pos);
			}
		});
        //click store share cell
        lvStoreShare.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				final int pos = position-1;
				if(pos>=0)
					toStorePhoto(pos, (String) ss_list.get(pos).get("StoreShareLoc"));
			}
		});
		/*********************viewpaper page change*********************/
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentView = arg0;
				//選取的sort style
				if(currentView==0)
					SortStyle.setSelection(select_sortstyle0);
				else if(currentView==1) {
					SortStyle.setSelection(select_sortstyle1);
					if(ss_list.isEmpty()) {
						/*********************產生StoreJSONData物件*********************/
						storeJsonData=new StoreJSONData(SharePagerActivity.this);
						/*********************判斷連接到server是否成功，isFindJSONData()*********************/
						if(!storeJsonData.isFindJSONData()){
				      		Intent intent = new Intent();				      		
				      		intent.setClass(SharePagerActivity.this, ntust.nwnc.homepage.HomePageActivity.class);			      		
				    		startActivity(intent); 
				    		Toast.makeText(SharePagerActivity.this,"Connection time out, please check server IP is valid.", Toast.LENGTH_LONG).show();
				    		SharePagerActivity.this.finish();
				    		return;
				      	}
						/*********************load data from json*********************/
						ss_loadData();	
					}
				}
			}	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if(arg0==0 && arg1==0.0f && arg2==0 && backhome==true)
					backHomePage();
				else if(arg0==0 && arg1==0.0f && arg2==0) 
					backhome = true;
				else if (arg0!=0 || arg1!=0.0f || arg2!=0) 
					backhome = false;
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		}); 
		/*********************animation*********************/
		lvUserShare.setOnScrollListener(onScrollListener);
		lvUserShare.setOnTouchListener(onTouchListener);
		lvStoreShare.setOnScrollListener(onScrollListener);
		lvStoreShare.setOnTouchListener(onTouchListener);
		//search view
		
		
		//sort style spinner
		ArrayAdapter<String> sortList = new ArrayAdapter<String>(SharePagerActivity.this, R.drawable.myspinner, sort_styles);
		sortList.setDropDownViewResource(R.drawable.myspinner);
		SortStyle.setAdapter(sortList);
		SortStyle.setOnItemSelectedListener(new OnItemSelectedListener() {
			//選擇排序的方式
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "View"+currentView+"你選的是"+sort_styles[position], Toast.LENGTH_SHORT).show();
				if(currentView==0)
					select_sortstyle0 = position;
				else if(currentView==1)
					select_sortstyle1 = position;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}
		});
		/*********************go to top button*********************/
		//button go top
		btnGoTop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentView==0) {
					lvUserShare.smoothScrollToPosition(0);
			        final Handler handler = new Handler();
			        handler.postDelayed(new Runnable()
			        {
			            @Override
			            public void run()
			            {
			                if (lvUserShare.getFirstVisiblePosition() > 0)
			                {
			                	lvUserShare.smoothScrollToPosition(0);
			                    handler.postDelayed(this, 100);
			                }
			            }
			        }, 100);
				}
				else if(currentView==1) {
					lvStoreShare.smoothScrollToPosition(0);
			        final Handler handler = new Handler();
			        handler.postDelayed(new Runnable()
			        {
			            @Override
			            public void run()
			            {
			                if (lvStoreShare.getFirstVisiblePosition() > 0)
			                {
			                	lvStoreShare.smoothScrollToPosition(0);
			                    handler.postDelayed(this, 100);
			                }
			            }
			        }, 100);
				}
			}
		});	
		
	}
	/*********************load user share Listview*********************/
	private void us_loadData() {
		//--------把JSONObject放入HashMap--------//
    	for(int i=0;i<userJsondata.length();i++){
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put("UserShareTitle", userJsondata.getTitle(i));
            map.put("UserShareImg", userJsondata.getImgByte(i));
            map.put("UserShareStar", String.valueOf(userJsondata.getStar(i)));
            map.put("UserShareDesc", "\t\t"+userJsondata.getDesc(i));
            us_list.add(map); 
    	}
	}
	/*********************load store share Listview*********************/
	private void ss_loadData() {
		//--------把JSONObject放入HashMap--------//
    	for(int i=0;i<storeJsonData.length();i++){
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put("StoreShareTitle", storeJsonData.getTitle(i));
            map.put("StoreShareImg", storeJsonData.getImgByte(i));
            map.put("StoreShareStar", String.valueOf(storeJsonData.getStar(i)));
            map.put("StoreShareDesc", "\t\t"+storeJsonData.getDesc(i));
            map.put("StoreSharePrice", "$"+String.valueOf(storeJsonData.getPrice(i)));
            map.put("StoreShareLoc", storeJsonData.getLoc(i));
            ss_list.add(map); 
    	}
	}
	/*********************init viewpager*********************/
	private void initPagerData()  
    {  
        mListViews.add(lvUserShare);
        mListViews.add(lvStoreShare);
    }  
	/*********************click at Listview to album*********************/
	private void toAlbum(int pos) {
		Intent intent = new Intent();

    	intent.setClass(SharePagerActivity.this, ntust.nwnc.useralbum.UserAlbumActivity.class);	
    	Bundle bundle = new Bundle();
    	bundle.putInt("Position", pos);
    	intent.putExtras(bundle);
    	startActivity(intent); 
    	SharePagerActivity.this.finish();	
	}
	/*********************click at Listview to photo*********************/
	private void toStorePhoto(int pos, String loc_title) {
		Intent intent = new Intent();

    	intent.setClass(SharePagerActivity.this, ntust.nwnc.storephoto.StorePhotoActivity.class);	
    	Bundle bundle = new Bundle();
    	bundle.putString("StoreShareLoc", loc_title);
    	bundle.putInt("Position", pos);
    	intent.putExtras(bundle);
    	startActivity(intent); 
    	SharePagerActivity.this.finish();	
		
	}
	/*********************back and action*********************/
	private void backSelfAction() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle!=null){
			if((bundle.getString("ShareType")).equals("user")) {
				viewpager.setCurrentItem(0);
				lvUserShare.setSelection(bundle.getInt("Position")+1);
			}
			if((bundle.getString("ShareType")).equals("store")) {
				viewpager.setCurrentItem(1);
				if(ss_list.isEmpty()) {
					/*********************產生StoreJSONData物件*********************/
					storeJsonData=new StoreJSONData(SharePagerActivity.this);
					/*********************判斷連接到server是否成功，isFindJSONData()*********************/
					if(!storeJsonData.isFindJSONData()){    	
			      		intent.setClass(SharePagerActivity.this, ntust.nwnc.homepage.HomePageActivity.class);
			      		
			    		startActivity(intent); 
			    		Toast.makeText(SharePagerActivity.this,"Connection time out, please check server IP is valid.", Toast.LENGTH_LONG).show();
			    		SharePagerActivity.this.finish();
			    		return;
			      	}
					/*********************load data from json*********************/
					ss_loadData();	
				}
				lvStoreShare.setSelection(bundle.getInt("Position")+1);
			}
		}
	}
	/*********************back to HomePage*********************/
	private void backHomePage() {
		Intent intent = new Intent();
		
    	intent.setClass(SharePagerActivity.this, ntust.nwnc.homepage.HomePageActivity.class);	
    	startActivity(intent); 
    	SharePagerActivity.this.finish();	
	}
	/*********************animation*********************/
	 AnimatorSet backAnimatorSet;
	 private void animateBack() {
	        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
	            hideAnimatorSet.cancel();
	        }
	        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
	   
	        } else {
	            backAnimatorSet = new AnimatorSet();
	            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(animation, "translationY", animation.getTranslationY(), 0f);
	            ArrayList<Animator> animators = new ArrayList<>();
	            animators.add(footerAnimator);
	            backAnimatorSet.setDuration(300);
	            backAnimatorSet.playTogether(animators);
	            backAnimatorSet.start();
	        }
	    }
	 
	 AnimatorSet hideAnimatorSet;
	 private void animateHide() {
	        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
	            backAnimatorSet.cancel();
	        }
	        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
	        } else {
	            hideAnimatorSet = new AnimatorSet();
	            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(animation, "translationY", animation.getTranslationY(), -animation.getHeight());
	            ArrayList<Animator> animators = new ArrayList<>();
	            animators.add(footerAnimator);
	            hideAnimatorSet.setDuration(200);
	            hideAnimatorSet.playTogether(animators);
	            hideAnimatorSet.start();
	        }
	    }
	 
	 View.OnTouchListener onTouchListener = new View.OnTouchListener() {
	        float lastY = 0f;
	        float currentY = 0f;
	        int lastDirection = 0;
	        int currentDirection = 0;
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                    lastY = event.getY();
	                    currentY = event.getY();
	                    currentDirection = 0;
	                    lastDirection = 0;
	                    break;
	                case MotionEvent.ACTION_MOVE:
	                    //if (lvUserShare.getFirstVisiblePosition() > 0) {
   	                        float tmpCurrentY = event.getY();
	                        if (Math.abs(tmpCurrentY - lastY) > touchSlop) {
	                            currentY = tmpCurrentY;
	                            currentDirection = (int) (currentY - lastY);
	                            if (lastDirection != currentDirection) {
	                                if (currentDirection < 0) {
	                                    animateHide();
	                                } else {
	                                    animateBack();
	                                }
	                            }
	                            lastY = currentY;
	                        }
	                    //}
	                    break;
	                case MotionEvent.ACTION_CANCEL:
	                case MotionEvent.ACTION_UP:
	                    currentDirection = 0;
	                    lastDirection = 0;
	                    break;
	            }
	            return false;
	        }
	    };
	    
	 AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
	        int lastPosition = 0;
	        int state = SCROLL_STATE_IDLE;
	         
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	            state = scrollState;
	            //hide keyboard
	            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	            searchView.clearFocus();
	        }
	 
	        @Override
	        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	            if (firstVisibleItem > 0) {
	            	btnGoTop.setVisibility(view.VISIBLE);
	                if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
	                    animateHide();   
	                }
	                if (firstVisibleItem < lastPosition && state == SCROLL_STATE_FLING) {
	                    animateBack();  
	                }     
	            }
	            else {
	            	btnGoTop.setVisibility(view.GONE);
				}
	            lastPosition = firstVisibleItem;
	        }
	    };
	 
	 
	 
	 
	 
	 

	 
	 
}