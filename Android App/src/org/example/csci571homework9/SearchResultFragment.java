package org.example.csci571homework9;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class SearchResultFragment extends Fragment implements OnClickListener {
	String address, homedetails, lastSoldPrice, estimateValueChangeSign, estimateValueChange, picture;
	View rootView;
	// facebook{
	private UiLifecycleHelper uiHelper;
	
	SessionStatusCallback statusCallback = new SessionStatusCallback();

	public void shareOnFacebook() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
		} else {
			Session.openActiveSession(getActivity(), this, true, statusCallback);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (exception != null) {
			exception.printStackTrace();
		}else{
			if (state.isOpened()) {
				publishFeedDialog();
			} else if (state.isClosed()) {
			}
		}
	}
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.zillow_search_result, container, false);
		
		uiHelper = new UiLifecycleHelper(getActivity(), null);
	    uiHelper.onCreate(savedInstanceState);

		String JSONString = getArguments().getString("JSONresponse");
		try {
			JSONObject jsonObj = new JSONObject(JSONString);

			picture = jsonObj.getJSONObject("chart").getString(
					"oneYearChartURL");

			JSONObject header = jsonObj.getJSONObject("header");
			homedetails = header.getString("homedetails");
			address = header.getString("street") + ", "
					+ header.getString("city") + ", "
					+ header.getString("state") + ", "
					+ header.getString("zipcode");
			String headerLink = "<a href=\"" + homedetails + "\">" + address
					+ "</a>";
			TextView cur = (TextView) rootView
					.findViewById(R.id.addressLinkTextView);
			cur.setText(Html.fromHtml(headerLink));
			cur.setMovementMethod(LinkMovementMethod.getInstance());

			Button fbButton = (Button) rootView.findViewById(R.id.fbButton);
			fbButton.setOnClickListener(this);

			JSONObject content = jsonObj.getJSONObject("content");
			String useCode = content.getString("useCode");
			cur = (TextView) rootView.findViewById(R.id.propTypeValueTextView);
			cur.setText(useCode);

			lastSoldPrice = content.getString("lastSoldPrice");
			if (!lastSoldPrice.equals("N/A"))
				lastSoldPrice = "$" + lastSoldPrice;
			cur = (TextView) rootView.findViewById(R.id.lspValueTextView);
			cur.setText(lastSoldPrice);

			String yearBuilt = content.getString("yearBuilt");
			cur = (TextView) rootView.findViewById(R.id.yearBuiltValueTextView);
			cur.setText(yearBuilt);

			String lastSoldDate = content.getString("lastSoldDate");
			cur = (TextView) rootView.findViewById(R.id.lsdValueTextView);
			cur.setText(lastSoldDate);

			String lotSizeSqFt = content.getString("lotSizeSqFt");
			if (!lotSizeSqFt.equals("N/A"))
				lotSizeSqFt = lotSizeSqFt + " sq. ft.";
			cur = (TextView) rootView.findViewById(R.id.lotSizeValueTextView);
			cur.setText(lotSizeSqFt);

			String estimateLastUpdate = content.getString("estimateLastUpdate");
			cur = (TextView) rootView.findViewById(R.id.zpeTextView);
			cur.setText(cur.getText() + " " + estimateLastUpdate);
			String estimateAmount = content.getString("estimateAmount");
			if (!estimateAmount.equals("N/A"))
				estimateAmount = "$" + estimateAmount;
			cur = (TextView) rootView.findViewById(R.id.zpeValueTextView);
			cur.setText(estimateAmount);

			String finishedSqFt = content.getString("finishedSqFt");
			if (!finishedSqFt.equals("N/A"))
				finishedSqFt = finishedSqFt + " sq. ft.";
			cur = (TextView) rootView
					.findViewById(R.id.finishedAreaValueTextView);
			cur.setText(finishedSqFt);

			estimateValueChangeSign = content
					.getString("estimateValueChangeSign");
			if (estimateValueChangeSign.equals("+")) {
				ImageView img = new ImageView(rootView.getContext());
				img.setImageResource(R.drawable.up);
				RelativeLayout layout = (RelativeLayout) rootView
						.findViewById(R.id.docLayout);
				layout.addView(img);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img
						.getLayoutParams();
				params.addRule(RelativeLayout.LEFT_OF, R.id.docTextViewValue);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				img.setLayoutParams(params);
			} else if (estimateValueChangeSign.equals("-")) {
				ImageView img = new ImageView(rootView.getContext());
				img.setImageResource(R.drawable.down);
				RelativeLayout layout = (RelativeLayout) rootView
						.findViewById(R.id.docLayout);
				layout.addView(img);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img
						.getLayoutParams();
				params.addRule(RelativeLayout.LEFT_OF, R.id.docTextViewValue);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				img.setLayoutParams(params);
			}
			estimateValueChange = content.getString("estimateValueChange");
			if (!estimateValueChange.equals("N/A"))
				estimateValueChange = "$" + estimateValueChange;
			cur = (TextView) rootView.findViewById(R.id.docTextViewValue);
			cur.setText(estimateValueChange);

			String bathrooms = content.getString("bathrooms");
			cur = (TextView) rootView.findViewById(R.id.bathroomsValueTextView);
			cur.setText(bathrooms);

			String estimateValuationRangeLow = content
					.getString("estimateValuationRangeLow");
			String estimateValuationRangeHigh = content
					.getString("estimateValuationRangeHigh");
			String estimateValuationRange = "N/A";
			if (!estimateValuationRangeLow.equals("N/A"))
				if (!estimateValuationRangeHigh.equals("N/A"))
					estimateValuationRange = "$" + estimateValuationRangeLow
							+ " - $" + estimateValuationRangeHigh;
				else
					estimateValuationRange = "$" + estimateValuationRangeLow;
			else if (!estimateValuationRangeHigh.equals("N/A"))
				estimateValuationRange = "$" + estimateValuationRangeHigh;
			else
				estimateValuationRange = "N/A";
			cur = (TextView) rootView.findViewById(R.id.atprValueTextView);
			cur.setText(estimateValuationRange);

			String bedrooms = content.getString("bedrooms");
			cur = (TextView) rootView.findViewById(R.id.bedroomsValueTextView);
			cur.setText(bedrooms);

			String restimateLastUpdate = content
					.getString("restimateLastUpdate");
			cur = (TextView) rootView.findViewById(R.id.rzvTextView);
			cur.setText(cur.getText() + " " + restimateLastUpdate);
			String restimateAmount = content.getString("restimateAmount");
			if (!restimateAmount.equals("N/A"))
				restimateAmount = "$" + restimateAmount;
			cur = (TextView) rootView.findViewById(R.id.rzvValueTextView);
			cur.setText(restimateAmount);

			String taxAssessmentYear = content.getString("taxAssessmentYear");
			cur = (TextView) rootView.findViewById(R.id.tayValueTextView);
			cur.setText(taxAssessmentYear);

			String restimateValueChangeSign = content
					.getString("restimateValueChangeSign");
			if (restimateValueChangeSign.equals("+")) {
				ImageView img = new ImageView(rootView.getContext());
				img.setImageResource(R.drawable.up);
				RelativeLayout layout = (RelativeLayout) rootView
						.findViewById(R.id.drcLayout);
				layout.addView(img);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img
						.getLayoutParams();
				params.addRule(RelativeLayout.LEFT_OF, R.id.drcValueTextView);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				img.setLayoutParams(params);
			} else if (restimateValueChangeSign.equals("-")) {
				ImageView img = new ImageView(rootView.getContext());
				img.setImageResource(R.drawable.down);
				RelativeLayout layout = (RelativeLayout) rootView
						.findViewById(R.id.drcLayout);
				layout.addView(img);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img
						.getLayoutParams();
				params.addRule(RelativeLayout.LEFT_OF, R.id.drcValueTextView);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				img.setLayoutParams(params);
			}
			String restimateValueChange = content
					.getString("restimateValueChange");
			if (!restimateValueChange.equals("N/A"))
				restimateValueChange = "$" + restimateValueChange;
			cur = (TextView) rootView.findViewById(R.id.drcValueTextView);
			cur.setText(restimateValueChange);

			String taxAssessment = content.getString("taxAssessment");
			if (!taxAssessment.equals("N/A"))
				taxAssessment = "$" + taxAssessment;
			cur = (TextView) rootView.findViewById(R.id.taValueTextView);
			cur.setText(taxAssessment);

			String restimateValuationRangeLow = content
					.getString("restimateValuationRangeLow");
			String restimateValuationRangeHigh = content
					.getString("restimateValuationRangeHigh");
			String restimateValuationRange = "N/A";
			if (!restimateValuationRangeLow.equals("N/A"))
				if (!restimateValuationRangeHigh.equals("N/A"))
					restimateValuationRange = "$" + restimateValuationRangeLow
							+ " - $" + restimateValuationRangeHigh;
				else
					restimateValuationRange = "$" + restimateValuationRangeLow;
			else if (!restimateValuationRangeHigh.equals("N/A"))
				restimateValuationRange = "$" + restimateValuationRangeHigh;
			else
				restimateValuationRange = "N/A";
			cur = (TextView) rootView.findViewById(R.id.atrrValueTextView);
			cur.setText(restimateValuationRange);

			cur = (TextView) rootView.findViewById(R.id.footerTextView1);
			cur.setText("\u00A9 Zillow, Inc., 2006-2014");

			cur = (TextView) rootView.findViewById(R.id.footerTextView2);
			cur.setText(Html
					.fromHtml("Use is subject to <a target=_blank href=\"http://www.zillow.com/corp/Terms.htm\" class=\"my-text-color my-links\">Terms of Use</a>"));
			cur.setMovementMethod(LinkMovementMethod.getInstance());

			cur = (TextView) rootView.findViewById(R.id.footerTextView3);
			cur.setText(Html
					.fromHtml("<a target=_blank href=\"http://www.zillow.com/wikipages/What-is-a-Zestimate/\" class=\"my-text-color my-links\">What's a Zestimate?</a>"));
			cur.setMovementMethod(LinkMovementMethod.getInstance());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootView;
	}

	// facebook{
	@Override
	public void onClick(View v) {
		new AlertDialog.Builder(rootView.getContext())
				.setMessage("Post to Facebook")
				.setPositiveButton(R.string.facebook_alert_yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int whichButton) {
								shareOnFacebook();
							}
						})
				.setNegativeButton(R.string.facebook_alert_cancel, 
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int whichButton) {
								Toast.makeText(rootView.getContext(), "Post Cancelled", Toast.LENGTH_SHORT).show();
							}
						})
				.show();
	}

	private void publishFeedDialog() {
		Bundle params = new Bundle();
		params.putString("name", address);
		params.putString("link", homedetails);
		params.putString("picture", picture);
		params.putString("caption", "Property information from Zillow.com");
		params.putString("description", "Last Sold Price: " + lastSoldPrice
				+ ",  30 Days Overall Change: " + estimateValueChangeSign
				+ estimateValueChange);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(), Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {
					@Override
					public void onComplete(Bundle values, FacebookException error) {
						if (error == null) {
							final String fbPostId = values.getString("post_id");
							if (fbPostId != null) {
								Toast.makeText(rootView.getContext(), "Posted Story, ID: " + fbPostId, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(rootView.getContext(), "Post Cancelled", Toast.LENGTH_SHORT).show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							Toast.makeText(rootView.getContext(), "Post Cancelled", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(rootView.getContext(), "Error posting story", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.build();
		
		feedDialog.show();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	// }
}
