package ntust.nwnc.noma;

import java.security.MessageDigest;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import ntust.nwnc.noma.LoginActivity;
import ntust.nwnc.noma.R;
import ntust.nwnc.noma.NomaMainActivity;

public class OAuthFacebook extends Activity {

	private Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.oauth_facebook);

		// getPackageHash();

		// get Facebook taken

		Session.openActiveSession(this, true, Arrays.asList("user_birthday", "email", "user_location") , new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					//Log.e("kenyang", session.getAccessToken()); // get token
					Request.newMeRequest(session, new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user, Response response) {
								if (user != null) {
									// Display the parsed user info
									String fb_name = user.getName();
									String fb_email = (String) user.getProperty("email");
									intent.setClass(OAuthFacebook.this, LoginActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("type", "FaceBook");
							        bundle.putString("name", fb_name);
							        bundle.putString("email", fb_email);
							        intent.putExtras(bundle);
									//Log.e("kenyang", user.getInnerJSONObject().toString()); // get JSON
								} else {
									intent.setClass(OAuthFacebook.this, ntust.nwnc.homepage.HomePageActivity.class);
								}
								startActivity(intent);
								OAuthFacebook.this.finish();
							}
					}).executeAsync();
				} else {
					Log.e("kenyang", state.toString()); // get state
					if (state.toString().equals("CLOSED_LOGIN_FAILED")) {
						intent.setClass(OAuthFacebook.this, ntust.nwnc.homepage.HomePageActivity.class);
						startActivity(intent);
						OAuthFacebook.this.finish();
					}				
				}

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Session.getActiveSession().onActivityResult(OAuthFacebook.this, requestCode, resultCode, data);
	}

	private void getPackageHash() {
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}