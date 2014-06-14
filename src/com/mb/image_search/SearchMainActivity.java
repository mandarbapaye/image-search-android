package com.mb.image_search;

import static com.mb.image_search.Constants.IMAGE_URL_PARAM;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		
		setupHandlers();
	}
	
	private void setupHandlers() {
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageResult imageResult = imageResultsList.get(position);
				Intent intentFullImage = new Intent(getApplicationContext(), FullImageActivity.class);
				intentFullImage.putExtra(IMAGE_URL_PARAM, imageResult.getFullUrl());
				startActivity(intentFullImage);
			}
		});
		
		gvImages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadImages(page);
			}
		});
	}

	private void setupViews() {
		gvImages = (GridView) findViewById(R.id.gvImages);
	}

	private void loadImages(int page) {	
		// TODO Auto-generated method stub
		
		String searchUrl = GOOGLE_IMG_SEARCH_API_URL + "?rsz=8&start=" + page + "&v=1.0&q=" + Uri.encode("android");
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(searchUrl, 
			new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						JSONArray imageResultsArray = response.getJSONObject("responseData").getJSONArray("results");
						//imageResultsList.clear();
						imageResultsAdapter.addAll(ImageResult.fromJSONArray(imageResultsArray));
						Log.d("mydebug", imageResultsList.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		
	}
}
