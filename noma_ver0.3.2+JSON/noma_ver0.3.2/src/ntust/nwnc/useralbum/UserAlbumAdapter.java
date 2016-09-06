package ntust.nwnc.useralbum;

import java.util.List;

import ntust.nwnc.noma.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class UserAlbumAdapter extends BaseAdapter {
	UserAlbumActivity context;                         
	//private ArrayList<HashMap<String, Object>> listItems;
	LayoutInflater listContainer;              
	LinearLayout ll;
	List<String> listItems;
	private ViewGroup layout;
	
	public final class ViewHolder {
		public ImageView img1, img2, img3;		
	}
	
	public UserAlbumAdapter(UserAlbumActivity context,List<String> listItems){
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
	
	
	public View getView(int position, View convertView, ViewGroup parent) {	
		final int selectID = position;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.album_user_cell, null);
		
		ViewHolder holder = null;
		holder = new ViewHolder();
		
		layout = (ViewGroup) rowView.findViewById(R.id.rl_item_photo);
        holder.img1 = (ImageView) rowView.findViewById(R.id.imageView1);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float screenWidth = dm.widthPixels;
        //int newWidth = (int) (screenWidth) / 3;

        layout.setLayoutParams(new GridView.LayoutParams((int) (screenWidth), (int) (screenWidth / 1.5)));
        holder.img1.setId(position);
        
        
        BitmapFactory.Options myOption = new Options();
        myOption.inPurgeable = true;
        myOption.inInputShareable = true;
		try {
		BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(myOption,true);
		} catch (Exception ex) {}
		/*
		Bitmap bm = BitmapFactory.decodeFile(listItems.get(selectID), myOption);
        Bitmap newBit = Bitmap.createScaledBitmap(bm, newWidth, newWidth,true);*/

        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context
                .getApplicationContext().getContentResolver(), Long
                .parseLong((String) listItems.get(selectID)),
                MediaStore.Images.Thumbnails.MINI_KIND, myOption);

        holder.img1.setImageBitmap(bm);
        holder.img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        System.gc();
        
        
		return rowView;

	}
	
	    
}