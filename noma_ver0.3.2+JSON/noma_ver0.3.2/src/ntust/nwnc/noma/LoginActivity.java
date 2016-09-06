package ntust.nwnc.noma;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.oauth_login);

		Bundle bundle = this.getIntent().getExtras();
		((TextView)findViewById(R.id.tvShowLoginType)).setText(bundle.getString("type"));
		((EditText)findViewById(R.id.etNameVal)).setText(bundle.getString("name"));
		((EditText)findViewById(R.id.etEmailVal)).setText(bundle.getString("email"));
	}
	
}