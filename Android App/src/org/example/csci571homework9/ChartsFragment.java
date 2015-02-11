package org.example.csci571homework9;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class ChartsFragment extends Fragment {
	String[] chartCaption = {" 1 year", " 5 years", " 10 years"};
	Drawable[] charts = new Drawable[3];
	int curIndex = 0;
	TextSwitcher chartCaptionTextSwitcher; 
	ImageSwitcher chartImageSwitcher;
	String address;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.zillow_result_chart, container, false);
        
        String JSONString = getArguments().getString("JSONresponse");
        try {
			JSONObject jsonObj = new JSONObject(JSONString);
			
			JSONObject chartURLs = jsonObj.getJSONObject("chart");
			String[] bitmapURLs = {chartURLs.getString("oneYearChartURL"), chartURLs.getString("fiveYearsChartURL"), chartURLs.getString("tenYearsChartURL")};

            new GetChartsFromZillow().execute(bitmapURLs);
            
            chartImageSwitcher = (ImageSwitcher)rootView.findViewById(R.id.chartsImageSwitcher);
            chartImageSwitcher.setFactory(new ViewFactory() {
				@Override
				public View makeView() {
					ImageView chartsImage = new ImageView(rootView.getContext());
					chartsImage.setAdjustViewBounds(true);
                    return chartsImage;
				}
			});
			
			JSONObject header = jsonObj.getJSONObject("header");
			address = header.getString("street")+", "+header.getString("city")+", "+header.getString("state")+", "+header.getString("zipcode");
			
			chartCaptionTextSwitcher = (TextSwitcher)rootView.findViewById(R.id.chartCaptionTextSwitcher);
			chartCaptionTextSwitcher.setFactory(new ViewFactory() {
				public View makeView() {
					TextView myText = new TextView(rootView.getContext());
                    myText.setGravity(Gravity.CENTER_HORIZONTAL);
                    myText.setTextSize(18);
                    myText.setTypeface(null, Typeface.BOLD);
                    myText.setText("Historical Zestimate for the past"+chartCaption[curIndex]);
                    return myText;
				}
			}); 
			
			((TextView)rootView.findViewById(R.id.addressTextView)).setText(address);
			
            ((Button)rootView.findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					chartCaptionTextSwitcher.setInAnimation(rootView.getContext(), R.animator.slide_in_left);
					chartCaptionTextSwitcher.setOutAnimation(rootView.getContext(), R.animator.slide_out_left);
					chartImageSwitcher.setInAnimation(rootView.getContext(), R.animator.slide_in_left);
					chartImageSwitcher.setOutAnimation(rootView.getContext(), R.animator.slide_out_left);
					curIndex++;
					if(curIndex==chartCaption.length)	curIndex=0;
					chartCaptionTextSwitcher.setText("Historical Zestimate for the past"+chartCaption[curIndex]);
					chartImageSwitcher.setImageDrawable(charts[curIndex]);
				}
			});
            
            ((Button)rootView.findViewById(R.id.prevButton)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					chartCaptionTextSwitcher.setInAnimation(rootView.getContext(), R.animator.slide_in_right);
					chartCaptionTextSwitcher.setOutAnimation(rootView.getContext(), R.animator.slide_out_right);
					chartImageSwitcher.setInAnimation(rootView.getContext(), R.animator.slide_in_right);
					chartImageSwitcher.setOutAnimation(rootView.getContext(), R.animator.slide_out_right);
					curIndex--;
					if(curIndex==-1)	curIndex=chartCaption.length-1;
					chartCaptionTextSwitcher.setText("Historical Zestimate for the past"+chartCaption[curIndex]);
					chartImageSwitcher.setImageDrawable(charts[curIndex]);
				}
			});
            
            TextView cur = (TextView)rootView.findViewById(R.id.footerTextView1);
			cur.setText("\u00A9 Zillow, Inc., 2006-2014");
			
			cur = (TextView)rootView.findViewById(R.id.footerTextView2);
			cur.setText(Html.fromHtml("Use is subject to <a target=_blank href=\"http://www.zillow.com/corp/Terms.htm\" class=\"my-text-color my-links\">Terms of Use</a>"));
			cur.setMovementMethod(LinkMovementMethod.getInstance());
			
			cur = (TextView)rootView.findViewById(R.id.footerTextView3);
			cur.setText(Html.fromHtml("<a target=_blank href=\"http://www.zillow.com/wikipages/What-is-a-Zestimate/\" class=\"my-text-color my-links\">What's a Zestimate?</a>"));
			cur.setMovementMethod(LinkMovementMethod.getInstance());
            
		} catch (JSONException e) {
			e.printStackTrace();
		} 

        return rootView;        
	}
	private class GetChartsFromZillow extends AsyncTask<String, Void, Void> {
		private ProgressDialog pDialog;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(rootView.getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @SuppressWarnings("deprecation")
		@Override
        protected Void doInBackground(String... urls) {
			try {
				for(int i=0; i<3; i++)
					charts[i] = new BitmapDrawable(BitmapFactory.decodeStream((InputStream)new URL(urls[i]).getContent()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
			chartImageSwitcher.setImageDrawable(charts[curIndex]);
        }
 
    }
}
