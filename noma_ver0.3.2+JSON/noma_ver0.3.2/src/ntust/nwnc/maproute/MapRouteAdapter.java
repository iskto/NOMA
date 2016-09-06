package ntust.nwnc.maproute;

import java.util.ArrayList;
import java.util.HashMap;

import ntust.nwnc.noma.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MapRouteAdapter extends BaseAdapter {
	MapRouteActivity context;                         
	private ArrayList<HashMap<String, Object>> listItems;
	LayoutInflater listContainer;              
	LinearLayout ll;
	
	public final class ViewHolder {
		public TextView RouteLoc, RouteTime;
		public TextView txtRoute, RouteDay; 
	}
	
	public MapRouteAdapter(MapRouteActivity context,ArrayList<HashMap<String, Object>> listItems){
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
		View rowView;
		if(selectID==0) {
			rowView = inflater.inflate(R.layout.map_route_celltitle, null);
			
			ViewHolder holder = null;
			holder = new ViewHolder();
			
			holder.txtRoute = (TextView) rowView.findViewById(R.id.txtRoute);
			holder.RouteDay = (TextView) rowView.findViewById(R.id.RouteDay);

			holder.txtRoute.setText("Route");
			holder.RouteDay.setText((String)listItems.get(selectID).get("RouteDay"));
			
		}
		else {
			rowView = inflater.inflate(R.layout.map_route_cell, null);
		
			ViewHolder holder = null;
			holder = new ViewHolder();

			holder.RouteLoc = (TextView) rowView.findViewById(R.id.RouteLoc);
			holder.RouteTime = (TextView) rowView.findViewById(R.id.RouteTime);
			
			if(!listItems.isEmpty()){ 
				holder.RouteLoc.setText((String)listItems.get(selectID).get("RouteLoc"));
				holder.RouteTime.setText((String)listItems.get(selectID).get("RouteTime"));
			}
		
		}
		
		return rowView;

	}
	
	    
}