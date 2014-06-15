package com.mb.image_search;

import static com.mb.image_search.Constants.IMAGE_URL_PARAM;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mb.image_search.model.ImageResult;

//public class SearchMainActivity extends Activity {

public class SearchMainActivity extends SherlockFragmentActivity {
	
	private static final String GOOGLE_IMG_SEARCH_API_URL = "https://ajax.googleapis.com/ajax/services/search/images";
	
	GridView gvImages;
	
	private ArrayList<ImageResult> imageResultsList = new ArrayList<ImageResult>();
	private ImageResultsArrayAdapter imageResultsAdapter;
	
	private String searchQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_main);
		setupViews();
		
		imageResultsAdapter = new ImageResultsArrayAdapter(this, imageResultsList);
		gvImages.setAdapter(imageResultsAdapter);
		
		setupHandlers();
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.action_bar_menu, menu);
		
	    MenuItem searchItem = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) searchItem.getActionView();
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
	       @Override
	       public boolean onQueryTextSubmit(String query) {
	    	    imageResultsAdapter.clear();
	    	    searchQuery = query;
	    	    loadImages(0);
	            return true;
	       }

	       @Override
	       public boolean onQueryTextChange(String newText) {
	           return false;
	       }
	   });
	    
	   return super.onCreateOptionsMenu(menu);
	}
	
	public void onSettingsClicked(MenuItem menuItem) {
		SearchPrefsPickerFragment prefsFragment = SearchPrefsPickerFragment.newInstance();
		prefsFragment.show(this.getSupportFragmentManager(), "test");
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
//		Toast.makeText(getApplicationContext(), "Loading " + page, Toast.LENGTH_SHORT).show();
		if (searchQuery == null || searchQuery.trim().isEmpty()) {
			return;
		}
		
		String searchUrl = GOOGLE_IMG_SEARCH_API_URL + "?rsz=8&start=" + page + "&v=1.0&q=" + Uri.encode(searchQuery);
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
