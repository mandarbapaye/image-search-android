package com.mb.image_search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mb.image_search.model.ImageResult;

public class SearchMainActivity extends Activity {

	private static final String GOOGLE_IMG_SEARCH_API_URL = "https://ajax.googleapis.com/ajax/services/search/images";
	
	GridView gvImages;
	
	private ArrayList<ImageResult> imageResultsList = new ArrayList<ImageResult>();
	private ImageResultsArrayAdapter imageResultsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_main);
		setupViews();
		
		imageResultsAdapter = new ImageResultsArrayAdapter(this, imageResultsList);
		gvImages.setAdapter(imageResultsAdapter);
		
		displayImages("android");
	}

	private void setupViews() {
		gvImages = (GridView) findViewById(R.id.gvImages);
	}

	private void displayImages(String imageSearchStr) {
		// TODO Auto-generated method stub
		
		String searchUrl = GOOGLE_IMG_SEARCH_API_URL + "?rsz=8&start=0&v=1.0&q=" + Uri.encode(imageSearchStr);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(searchUrl, 
			new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						JSONArray imageResultsArray = response.getJSONObject("responseData").getJSONArray("results");
						imageResultsList.clear();
//						imageResultsList.addAll(ImageResult.fromJSONArray(imageResultsArray));
						imageResultsAdapter.addAll(ImageResult.fromJSONArray(imageResultsArray));
						Log.d("mandar", imageResultsList.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		
	}
}
