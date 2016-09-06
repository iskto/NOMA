package ntust.nwnc.noma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;


public class OAuthGooglePlus extends Activity {

	private Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.oauth_googleplus);
		pickUserAccount();
	}

	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

	private void pickUserAccount() {
	    String[] accountTypes = new String[]{"com.google"};
	    Intent intent = AccountPicker.newChooseAccountIntent(null, null,
	            accountTypes, false, null, null, null, null);
	    startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}
	
	String mEmail; // Received from newChooseAccountIntent(); passed to getToken()

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
	        // Receiving a result from the AccountPicker
	        if (resultCode == RESULT_OK) {
	            mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	            // With the account name acquired, go get the auth token
	            getUsername();
	        } else if (resultCode == RESULT_CANCELED) {
	            // The account picker dialog closed without selecting an account.
	            // Notify users that they must pick an account to proceed.
	            Toast.makeText(this, "？", Toast.LENGTH_SHORT).show();
	        }
	    }
	    // Later, more code will go here to handle the result from some exceptions...
	}
	
	private static final String SCOPE =
	        "oauth2:https://www.googleapis.com/auth/userinfo.profile";

	/**
	 * Attempts to retrieve the username.
	 * If the account is not yet known, invoke the picker. Once the account is known,
	 * start an instance of the AsyncTask to get the auth token and do work with it.
	 */
	private void getUsername() {
	    if (mEmail == null) {
	        pickUserAccount();
	    } else {
	        //if (isDeviceOnline()) {
	            new GetUsernameTask(OAuthGooglePlus.this, mEmail, SCOPE).execute();
	        //} else {
	        //    Toast.makeText(this, "not on line", Toast.LENGTH_LONG).show();
	        //}
	    }
	}
	
	
	public class GetUsernameTask extends AsyncTask<String,String,String>{
	    Activity mActivity;
	    String mScope;
	    String mEmail;

	    GetUsernameTask(Activity activity, String name, String scope) {
	        this.mActivity = activity;
	        this.mScope = scope;
	        this.mEmail = name;
	    }

	    /**
	     * Executes the asynchronous job. This runs when you call execute()
	     * on the AsyncTask instance.
	     */
	    protected String doInBackground(String... params) {
	        try {
	            String token = fetchToken();
	            if (token != null) {
	                // Insert the good stuff here.
	                // Use the token to access the user's Google data.
	            	Log.e("Googleplustoken", token);
	            	URL urUserInfo =  new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+token); 
	            		 HttpURLConnection connObtainUserInfo =  (HttpURLConnection) urUserInfo.openConnection(); 
	            		 
	            		 if (connObtainUserInfo.getResponseCode() == HttpURLConnection.HTTP_OK){
	            			 StringBuilder sbLines   = new StringBuilder("");
	            			 
	            			 BufferedReader reader =  new BufferedReader(new InputStreamReader(connObtainUserInfo.getInputStream(),"utf-8"));
	            			 String strLine = "";
	            			 while((strLine=reader.readLine())!=null){
	            				 	sbLines.append(strLine);
	            			 }  
	            		  try {
	            		   
	            		   JSONObject jo = new JSONObject(sbLines.toString());  
	            		   
	            		   intent.setClass(OAuthGooglePlus.this, LoginActivity.class);
	            		   Bundle bundle = new Bundle();
	            		   bundle.putString("type", "Google+");
					       bundle.putString("name", jo.getString("name"));
					       bundle.putString("email", mEmail);
					       intent.putExtras(bundle);
					        
	            		   //Toast.makeText(OAuthGooglePlus.this, jo.getString("name"), Toast.LENGTH_LONG).show(); 
	            		    
	            		  } catch (Exception e) {
	            			 intent.setClass(OAuthGooglePlus.this, ntust.nwnc.homepage.HomePageActivity.class);
	            		  }
	            		 }
	            } else {
	            	intent.setClass(OAuthGooglePlus.this, ntust.nwnc.homepage.HomePageActivity.class);
	            }
	        } catch (IOException e) {
	        	intent.setClass(OAuthGooglePlus.this, ntust.nwnc.homepage.HomePageActivity.class);
	            // The fetchToken() method handles Google-specific exceptions,
	            // so this indicates something went wrong at a higher level.
	            // TIP: Check for network connectivity before starting the AsyncTask.
	        }
	        startActivity(intent);
            OAuthGooglePlus.this.finish();
	        return null;
	    }

	    /**
	     * Gets an authentication token from Google and handles any
	     * GoogleAuthException that may occur.
	     */
	    protected String fetchToken() throws IOException {
	        try {
	            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
	        } catch (UserRecoverableAuthException userRecoverableException) {
	            // GooglePlayServices.apk is either old, disabled, or not present
	            // so we need to show the user some UI in the activity to recover.
	            //mActivity.handleException(userRecoverableException);
	        } catch (GoogleAuthException fatalException) {
	            // Some other type of unrecoverable exception has occurred.
	            // Report and log the error as appropriate for your app.
	            
	        }
	        return null;
	    }
	    
	}

}