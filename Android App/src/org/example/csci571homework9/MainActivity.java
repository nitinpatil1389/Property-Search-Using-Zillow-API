package org.example.csci571homework9;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


public class MainActivity extends Activity implements OnItemSelectedListener{

	Spinner stateSpinner;
	Button searchButton;
	String state = "Choose State";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        stateSpinner = (Spinner) findViewById(R.id.spinnerState);
        stateSpinner.setOnItemSelectedListener(this);
        
        ImageView zillowIcon = (ImageView)findViewById(R.id.imageView1);
        zillowIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browser = new Intent("android.intent.action.VIEW",Uri.parse("http://www.zillow.com/"));
				startActivity(browser);
			}
		});
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		state = stateSpinner.getItemAtPosition(position).toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	public void onSubmit(View v) {
		String address = ((EditText)findViewById(R.id.editTextAddress)).getText().toString();
		String city = ((EditText)findViewById(R.id.editTextCity)).getText().toString();
		
		//validate inputs
		boolean flag = false;
		if(address.equals("")){
			findViewById(R.id.textViewAddressError).setVisibility(View.VISIBLE);
			flag = true;
		}
		else{
			findViewById(R.id.textViewAddressError).setVisibility(View.INVISIBLE);
		}
		if(city.equals("")){
			findViewById(R.id.textViewCityError).setVisibility(View.VISIBLE);
			flag = true;
		}
		else{
			findViewById(R.id.textViewCityError).setVisibility(View.INVISIBLE);
		}
		if(state.equals("Choose State")){
			findViewById(R.id.textViewStateError).setVisibility(View.VISIBLE);
			flag = true;
		}
		else{
			findViewById(R.id.textViewStateError).setVisibility(View.INVISIBLE);
		}
		
		if(flag)
			return;
		
		final ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("streetAddress", address));
		params.add(new BasicNameValuePair("city", city));
		params.add(new BasicNameValuePair("state", state));
		String paramString = URLEncodedUtils.format(params, "utf-8");
		String url = "http://nitinpatilapp1.elasticbeanstalk.com/?"+paramString;
		
		new GetDetailsFromZillow().execute(url);
	}
	
	private class GetDetailsFromZillow extends AsyncTask<String, Void, String> {
		private ProgressDialog pDialog;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected String doInBackground(String... urls) {
        	
        	String url=urls[0];
    		String response = null;
    		try {
    			DefaultHttpClient myHttpClient = new DefaultHttpClient();
                HttpResponse zillowHttpResponse = myHttpClient.execute(new HttpGet(url));
                if(zillowHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    response = EntityUtils.toString(zillowHttpResponse.getEntity());
                } else{
                    throw new IOException(zillowHttpResponse.getStatusLine().getReasonPhrase());
                }
    		} catch (ClientProtocolException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
 
            return response;
        }
 
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                     
                    JSONObject message = jsonObj.getJSONObject("message");
                    String code = message.getString("code");
                    
                    if(code.equals("0")){
                    	findViewById(R.id.textViewZillowError).setVisibility(View.INVISIBLE);
                    	Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                    	intent.putExtra("JSONresponse", result);
                		startActivity(intent);
                    }
                    else{                    
                    	findViewById(R.id.textViewZillowError).setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            	findViewById(R.id.textViewZillowError).setVisibility(View.VISIBLE);
            }
        }
    }
}
