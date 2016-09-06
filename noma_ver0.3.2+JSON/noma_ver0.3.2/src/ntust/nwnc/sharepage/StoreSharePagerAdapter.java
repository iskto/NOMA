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



public class StoreSharePagerAdapter extends BaseAdapter {
	SharePagerActivity context;                         
	private ArrayList<HashMap<String, Object>> listItems;
	LayoutInflater listContainer;              
	LinearLayout ll;
	
	public final class ViewHolder {
		public TextView ss_title, ss_desc, ss_price;
		public RatingBar ss_rtbar;
		public ImageView ss_img;		
	}
	
	public StoreSharePagerAdapter(SharePagerActivity context,ArrayList<HashMap<String, Object>> listItems){
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
		View rowView = inflater.inflate(R.layout.store_share_cell, null);
		
		ViewHolder holder = null;
		holder = new ViewHolder();

		holder.ss_title = (TextView)rowView.findViewById(R.id.ss_title);
		holder.ss_price = (TextView)rowView.findViewById(R.id.ss_price);
		holder.ss_desc = (TextView)rowView.findViewById(R.id.ss_desc);
		holder.ss_rtbar = (RatingBar)rowView.findViewById(R.id.ss_rtbar);
		holder.ss_img = (ImageView)rowView.findViewById(R.id.ss_img);
		
		if(!listItems.isEmpty()){ 
			holder.ss_title.setText((String)listItems.get(selectID).get("StoreShareTitle")); //title
			holder.ss_price.setText((String)listItems.get(selectID).get("StoreSharePrice")); //price
			holder.ss_desc.setText((String)listItems.get(selectID).get("StoreShareDesc")); //description
			holder.ss_rtbar.setRating(Integer.parseInt((String) listItems.get(selectID).get("StoreShareStar"))); 	
			//holder.ss_img.setImageResource(R.drawable.vancouver);
			if(listItems.get(selectID).get("StoreShareImg")!=null){
    			byte[] bytes=(byte[])listItems.get(selectID).get("StoreShareImg");
    			Bitmap bm=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        		
				holder.ss_img.setImageBitmap(bm);
			}
		}
		
		return rowView;

	}
	
	    
}