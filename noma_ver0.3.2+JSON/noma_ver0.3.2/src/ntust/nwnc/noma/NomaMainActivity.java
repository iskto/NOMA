package ntust.nwnc.noma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;


public class NomaMainActivity extends Activity {
	private ViewGroup layout;
	Button btnFB, btnGoogle, btnTwitter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.noma_first);
        /*********************init*********************/
        layout = (ViewGroup) this.findViewById(R.id.firstpage_bg_Layout);
        btnFB = (Button) this.findViewById(R.id.btnFB);
        btnGoogle = (Button) this.findViewById(R.id.btnGoogle);
        btnTwitter = (Button) this.findViewById(R.id.btnTwitter);
        /*********************ClickBG and Change Page*********************/
        layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

		    	intent.setClass(NomaMainActivity.this, ntust.nwnc.homepage.HomePageActivity.class);	
		    	startActivity(intent); 
		    	NomaMainActivity.this.finish();	
			}
		});
        
        btnFB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(NomaMainActivity.this, OAuthFacebook.class));
		    	NomaMainActivity.this.finish();	
			}
		});
        btnGoogle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(NomaMainActivity.this, OAuthGooglePlus.class));
		    	NomaMainActivity.this.finish();
			}
		});
        btnTwitter.setOnClickListener(new OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		startActivity(new Intent(NomaMainActivity.this, ntust.nwnc.maproute.MapRouteActivity.class));
		    	NomaMainActivity.this.finish();	
        	}
        });

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
