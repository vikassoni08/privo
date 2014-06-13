package com.example.privo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {

	Button send;
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				try
				{
					JSONObject jsonObj=new JSONObject();
					jsonObj.put("city", "Orlando");
					jsonObj.put("country", "US"); 
					jsonObj.put("email", "someone@somewhere.com");
					jsonObj.put("firstName", "Jacky");
					jsonObj.put("driverLicenseNumber", "F255-921-50-403-0");
					jsonObj.put("lastName", "mouse");
					jsonObj.put("postalCode", "328105928");
					jsonObj.put("stateProvince", "FL");
					jsonObj.put("streetAddress1", "1234 Main St");
				 
				
				
				Log.e("json data",jsonObj.toString());
				/*String str=parent.toString();
				
				String one=str;
				*/
				new RequestTask(jsonObj).execute();
				
				}
				catch(Exception e)
				{
					
				}
			}
		});
	}
	
	
	class RequestTask extends AsyncTask<String, String, String> {
		ProgressDialog pd = new ProgressDialog(MainActivity.this);

		JSONObject jsonObj=new JSONObject();
		
		public RequestTask(JSONObject parent) {
			jsonObj = parent;
	    }
		
		@Override
		protected void onPreExecute() {
			pd.setMessage("SignUp ...");
			pd.getWindow().setGravity(Gravity.BOTTOM);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... uri) {
			
			String serverUrl="https://privohub-int.privo.com/api/verification/verifyPersonByDriversLicense";
			int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			
			HttpClient client = new DefaultHttpClient(httpParams);
			
			HttpPost request = new HttpPost(serverUrl);
			
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-Type", "application/json");
			request.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOlsiaWRlYXVzYSJdLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6ODA4MVwvb3BlbmlkLWNvbm5lY3Qtc2VydmVyLXdlYmFwcFwvIiwianRpIjoiZDRhMzNhM2ItNzg5Zi00OTU1LWI1NjAtZjM4MzVhNDM5N2Y5IiwiaWF0IjoxMzk5MDA2OTE0fQ.BYmNiXY-4y0Fc3tEIuvW1US0qmeJqcMFomuWJOHnSELhwb4phGBSFwG4k3URwYRGoQ5RrTnBdhkfaqZBaH3JGuGqJ5SGBWvvh3iQh4jCHTMiwYn1_EQpiGQK2phJ6CF7K1L0Nlm1RJL40f-wpGO8gszTiT2g13QVU1dOWrOVzG8");
			
			try {
				request.setEntity(new ByteArrayEntity(jsonObj.toString().getBytes("UTF8")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			InputStream content = null;
			
			try {
				content = response.getEntity().getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				System.err.println("DATA RECVD === "+IOUtils.toString(content));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return serverUrl;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
			pd.dismiss();
			
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
