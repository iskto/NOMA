package ntust.nwnc.sharepage;

import java.util.ArrayList;
import java.util.HashMap;

import ntust.nwnc.noma.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;



public class UserSharePagerAdapter extends BaseAdapter {
	SharePagerActivity context;                         
	private ArrayList<HashMap<String, Object>> listItems;
	LayoutInflater listContainer;              
	LinearLayout ll;
	
	public final class ViewHolder {
		public TextView us_title, us_desc;
		public RatingBar us_rtbar;
		public ImageView us_img;		
	}
	
	public UserSharePagerAdapter(SharePagerActivity context,ArrayList<HashMap<String, Object>> listItems){
		this.context = context;             
		listContainer = LayoutInflater.from(context);    
		this.listItems = listItems;	
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public void addItem(HashMap<String, Object> position){
		listItems.add(position);
	    this.notifyDataSetChanged();
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {	
		final int selectID = position;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.user_share_cell, null);
		
		ViewHolder holder = null;
		holder = new ViewHolder();

		holder.us_title = (TextView)rowView.findViewById(R.id.us_title);
		holder.us_desc = (TextView)rowView.findViewById(R.id.us_desc);
		holder.us_rtbar = (RatingBar)rowView.findViewById(R.id.us_rtbar);
		holder.us_img = (ImageView)rowView.findViewById(R.id.us_img);
		
		if(!listItems.isEmpty()){ 
			holder.us_title.setText((String)listItems.get(selectID).get("UserShareTitle")); //title
			holder.us_desc.setText((String)listItems.get(selectID).get("UserShareDesc")); //description
			holder.us_rtbar.setRating(Integer.parseInt((String) listItems.get(selectID).get("UserShareStar"))); 	
			//holder.us_img.setImageResource(R.drawable.yellowstone);
			if(listItems.get(selectID).get("UserShareImg")!=null){
    			byte[] bytes=(byte[])listItems.get(selectID).get("UserShareImg");
    			Bitmap bm=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        		
				holder.us_img.setImageBitmap(bm);
			}
		}
		
		return rowView;

	}
	
	    
}