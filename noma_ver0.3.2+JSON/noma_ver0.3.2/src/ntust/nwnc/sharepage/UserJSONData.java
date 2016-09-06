package ntust.nwnc.sharepage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import ntust.nwnc.noma.R;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.ConditionVariable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class UserJSONData{
	protected String url;
	private String imgUrl;
	private InputStream ImgInputStream;
	
	private JSONArray jsonarray;
	private JSONObject jsonobject;
	
	private String Title;
	private int Star;
	private String Desc;
	private int Rating;
	private int id;
	
	private ConditionVariable cv;
	private boolean findJSONData;
	private boolean taskfinish;
	
	private ProgressBar progressBar;
	
	public UserJSONData(SharePagerActivity context){
		url="http://192.168.1.103:26080/user/";	//伺服器的IP位址
		progressBar = (ProgressBar) context.findViewById(R.id.progressBar1);
		cv=new ConditionVariable();
		taskfinish=false;
		getJSONData();							//取得伺服器上的JSONArray
		while(!taskfinish){
			cv.block(100);
		}
		if(jsonarray==null)
			findJSONData=false;
		else
			findJSONData=true;					//取得伺服器上的JSONArray
	}
	
	protected void getJSONData(){
		new HttpGetTask().execute();			//取得伺服器上的JSONArray
	}
	
	public boolean isFindJSONData(){
		return findJSONData;
	}
	
	public int length(){
		return jsonarray.length();						//回傳JSONArray
	}
	
	
	public JSONArray getJSONArray(){
		return jsonarray;						//回傳JSONArray的長度
	}
	
	public JSONObject getJSONObject(int id){		//回傳JSONArray的第i個元素
		JSONObject jsonobject;
		try{
			jsonobject= jsonarray.getJSONObject(id);
		}catch(Exception e){
			Log.i("error","getJSONObject failed");
			jsonobject= null;
		}
		return jsonobject;
	}
	
	public String getTitle(int id){		
		String Title;
		try{
			Title= (String) jsonarray.getJSONObject(id).get("Title");
		}catch(Exception e){
			Title= null;
		}
		return Title;
	}
	
	public String getImgString(int id){		
		String Img;
		try{
			Img= (String) jsonarray.getJSONObject(id).get("Img");
		}catch(Exception e){
			Log.i("error","getImgString failed");
			Img= null;
		}
		return Img;
	}
	
	public byte[] getImgByte(int id){		
		byte[] ImgByte;
		try{
			String ImgString=(String)jsonarray.getJSONObject(id).get("Img");
			ImgByte=Base64.decode(ImgString, Base64.DEFAULT);
		}catch(Exception e){
			ImgByte= null;
		}
		return ImgByte;
	}
	
	public InputStream getImgInputStream(int id){		
		InputStream ImgInputStream;
		try{
			byte[] ImgByte=this.getImgByte(id);
			ImgInputStream=convertByteToInputStream(ImgByte);
		}catch(Exception e){
			ImgInputStream= null;
		}
		return ImgInputStream;
	}
	
	public int getStar(int id){		
		int Star;
		try{
			Star= (int) jsonarray.getJSONObject(id).get("Star");
		}catch(Exception e){
			Star= -1;
		}
		return Star;
	}
	
	public String getDesc(int id){		
		String Desc;
		try{
			Desc= (String) jsonarray.getJSONObject(id).get("Desc");
		}catch(Exception e){
			Desc= null;
		}
		return Desc;
	}
	
	public int getRating(int id){		
		int Rating;
		try{
			Rating= (int) jsonarray.getJSONObject(id).get("Rating");
		}catch(Exception e){
			Rating= -1;
		}
		return Rating;
	}
	
	public void putJSONObject(String Title,String Img,int Star,String Desc,int Rating){	
		jsonobject=new JSONObject();
		try{
			//jsonobject.put("id", jsonarray.length());
			jsonobject.put("Title", Title);
			jsonobject.put("Img", Img);
			jsonobject.put("Star", Star);
			jsonobject.put("Desc", Desc);
			jsonobject.put("Rating", Rating);
			jsonobject.put("id", String.valueOf(jsonarray.length()));
			//Log.i("id",String.valueOf(jsonarray.length()));
			jsonarray.put(jsonobject);
			new HttpPostTask().execute();
		}catch(Exception e){
			Log.i("error","put JSONObject error");
		}
		
	}
	
	public void putJSONObject(String Title,byte[] ImgByte,int Star,String Desc,int Rating){	
		ImgByte=compressImgByte(ImgByte);
		String Img=Base64.encodeToString(ImgByte, Base64.DEFAULT);
		this.putJSONObject(Title, Img, Star, Desc, Rating);
		
	}
	
	public void putJSONObject(String Title,InputStream ImgInputStream,int Star,String Desc,int Rating){
		this.Title=Title;
		this.ImgInputStream=ImgInputStream;
		this.Star=Star;
		this.Desc=Desc;
		this.Rating=Rating;
		new HttpPostTaskImgInputStream().execute();
		
	}

	public void putJSONObjectByImgURL(String Title,String ImgUrl,int Star,String Desc,int Rating){	
		this.Title=Title;
		this.imgUrl=ImgUrl;
		this.Star=Star;
		this.Desc=Desc;
		this.Rating=Rating;
		new HttpPostTaskImgUrl().execute();
	}

	public void addJSONObjectRating(int id){
		try {
			this.id=id;
			jsonarray.getJSONObject(id).put("Rating", getRating(id)+1);
			new HttpPutRatingTask().execute();
		} catch (JSONException e) {
			Log.e("error","addJSONObjectRating failed");
		}
	}
	
	private class HttpGetTask extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.INVISIBLE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			HttpGet();
			return null;
		}

    }
	
	private class HttpPostTask extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpPost();
			return null ;
		}
    }
	
	private class HttpPostTaskImgInputStream extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			byte[] ImgByte=convertInputStreamToByte(ImgInputStream);
			String Img=Base64.encodeToString(ImgByte, Base64.DEFAULT);
			jsonobject=new JSONObject();
			try{
				//jsonobject.put("id", jsonarray.length());
				jsonobject.put("Title", Title);
				jsonobject.put("Img", Img);
				jsonobject.put("Star", Star);
				jsonobject.put("Desc", Desc);
				jsonobject.put("Rating", Rating);
				jsonobject.put("id", String.valueOf(jsonarray.length()));
				//Log.i("id",String.valueOf(jsonarray.length()));
				jsonarray.put(jsonobject);
				HttpPost();
			}catch(Exception e){
				Log.i("error","put JSONObject error");
			}
			
			return null ;
		}
    }
	
	private class HttpPostTaskImgUrl extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpImag();
			byte[] ImgByte=convertInputStreamToByte(ImgInputStream);
			ImgByte=compressImgByte(ImgByte);
			String Img=Base64.encodeToString(ImgByte, Base64.DEFAULT);
			jsonobject=new JSONObject();
			try{
				//jsonobject.put("id", jsonarray.length());
				jsonobject.put("Title", Title);
				jsonobject.put("Img", Img);
				jsonobject.put("Star", Star);
				jsonobject.put("Desc", Desc);
				jsonobject.put("Rating", Rating);
				jsonobject.put("id", String.valueOf(jsonarray.length()));
				//Log.i("id",String.valueOf(jsonarray.length()));
				jsonarray.put(jsonobject);
				HttpPost();
			}catch(Exception e){
				Log.i("error","put JSONObject error");
			}
			
			return null ;
		}
    }
	
	private class HttpImageTask extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			HttpImag();
			return null;
		}
    }
	
	private class HttpPutRatingTask extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			DefaultHttpClient httpclient =getHttpClient();
			HttpPut httpputreq = new HttpPut(url+String.valueOf(id));
			StringEntity se;
			InputStream inputStream = null;
			
			try {
				
				se = new StringEntity(jsonarray.getJSONObject(id).toString());
				se.setContentType("application/json;charset=UTF-8");
	    		se.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
	    		httpputreq.setEntity(se);
	    		httpputreq.setHeader("Accept", "application/json");
	    		httpputreq.setHeader("Content-type", "application/json");
	    		HttpResponse httpresponse = httpclient.execute(httpputreq);
	    		
	    		inputStream = httpresponse.getEntity().getContent();
	    		
	    		Log.e("response", httpresponse.getStatusLine().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("response", "put failed");
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }  
	
	private void HttpGet(){
		HttpGet httpget=new HttpGet(url);
		
    	try{
    		HttpResponse httpresponse=getHttpClient().execute(httpget);
    		String result=EntityUtils.toString(httpresponse.getEntity());
    		Log.i("result",result);
    		jsonarray=new JSONArray(result);
    		
    		
    	}catch(Exception err){
    		Log.i("error","fail get jsondata");

    	}
		Log.i("task status","task finished");
		taskfinish=true;
	}
	
	private void HttpPost(){
		//listResult.setAdapter(listAdapter);
		DefaultHttpClient httpclient =getHttpClient();
		HttpPost httppostreq = new HttpPost(url);
		StringEntity se;
		InputStream inputStream = null;
		
		try {
			se = new StringEntity(jsonobject.toString());
			Log.e("jsonobject", jsonobject.toString());
			se.setContentType("application/json;charset=UTF-8");
    		se.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
    		Log.e("error", String.valueOf(se.getContentLength()));
    		
    		httppostreq.setEntity(se);
    		httppostreq.setHeader("Accept", "application/json");
    		httppostreq.setHeader("Content-type", "application/json");
    		HttpResponse httpresponse = httpclient.execute(httppostreq);
    		
    		inputStream = httpresponse.getEntity().getContent();
    		
    		Log.e("response", httpresponse.getStatusLine().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("response", "post failed");
			e.printStackTrace();
		}
	}
	
	private void HttpImag(){
		try{
    		URL url=new URL(imgUrl);
    		ImgInputStream=(InputStream)url.getContent();
    		Log.i("HttpImageTask","success get image");
    	}catch(Exception err){
    		Log.i("error","fail get image");
    	}
	}
	
	private static byte[] compressImgByte(byte[] bytes){
    	BitmapFactory.Options options =new BitmapFactory.Options();	
    	//只讀取圖片的寬和高
		options.inJustDecodeBounds=true;
		Bitmap bm=BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		//計算取樣的分母，但Bitmap.compress只會取1/2的倍數
    	options.inSampleSize = calculateInSampleSize(options,256,256)*2; 
		Log.e("sample", String.valueOf((options.inSampleSize)));
		//options.inSampleSize=4;
    	options.inJustDecodeBounds=false;
		//^讀取寬和高結束
		Log.e("error", bm==null?"yes":"no");
		
		Log.e("error", String.valueOf(options.outHeight)+"x"+String.valueOf(options.outWidth));
		//真正讀取圖片的寬和高、像素
		bm=BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);
		Log.e("error", bm==null?"yes":"no");
		
		Log.e("error", String.valueOf(options.outHeight)+"x"+String.valueOf(options.outWidth));
		
		//把Bitmap轉成ByteArrayOutputStream再轉成byte[]
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 0 , bos); 
		byte[] bitmapdata = bos.toByteArray();
    	
		return bitmapdata;
    }
    
	private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {  
        // Raw height and width of image  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
  
        if (height > reqHeight || width > reqWidth) {  
  
            // Calculate ratios of height and width to requested height and  
            // width  
            final int heightRatio = (int)Math.ceil((double) height / (double) reqHeight);  
            final int widthRatio = (int)Math.ceil((double) width / (double) reqWidth);  
            
            // Choose the smallest ratio as inSampleSize value, this will  
            // guarantee  
            // a final image with both dimensions larger than or equal to the  
            // requested height and width.  
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;  
            //Log.e("sample", String.valueOf(inSampleSize));
        }  
  
        return inSampleSize;  
    }  
    
	
	@SuppressLint("NewApi")
	private static byte[] convertInputStreamToByte(InputStream is){
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();)
	    {
	        byte[] buffer = new byte[0xFFFF];

	        for (int len; (len = is.read(buffer)) != -1;)
	            os.write(buffer, 0, len);

	        os.flush();

	        return os.toByteArray();
	    }
	    catch (IOException e)
	    {
	        return null;
	    }
	}
	
	private static InputStream convertByteToInputStream(byte[] bytes){
	   return (InputStream) new ByteArrayInputStream(bytes);
	}
	
	private DefaultHttpClient getHttpClient(){
    	HttpParams httpparams=new BasicHttpParams();
    	int timeoutConnection=5000;
    	HttpConnectionParams.setConnectionTimeout(httpparams,
    			timeoutConnection);
    	int timeoutSocket=3000;
    	HttpConnectionParams.setSoTimeout(httpparams, 
    			timeoutSocket);
    	DefaultHttpClient dfaultHC=new DefaultHttpClient(httpparams);
    	return dfaultHC;
    	
    }
	
}
