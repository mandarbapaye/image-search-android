package com.mb.image_search;

import com.mb.image_search.Constants;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mb.image_search.model.SearchPreferences;


//public class SearchMainActivity extends Activity {

public class SearchMainActivity extends SherlockFragmentActivity implements SearchPrefsPickerFragment.OnPreferencesSetListener {
	
	private static final String GOOGLE_IMG_SEARCH_API_URL = "https://ajax.googleapis.com/ajax/services/search/images";
	private static final String COLOR_PREF_KEY = "color";
	private static final String TYPE_PREF_KEY = "type";
	private static final String SIZE_PREF_KEY = "size";
	private static final String SITE_PREF_KEY = "site";
	
	GridView gvImages;
	
	private ArrayList<ImageResult> imageResultsList = new ArrayList<ImageResult>();
	private ImageResultsArrayAdapter imageResultsAdapter;
	
	private String searchQuery;
	private SearchPreferences searchPrefs = new SearchPreferences();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_main);
		setupViews();
		
		imageResultsAdapter = new ImageResultsArrayAdapter(this, imageResultsList);
		gvImages.setAdapter(imageResultsAdapter);
		
		setupHandlers();
		loadPreferences();
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
		Bundle args = new Bundle();
		args.putSerializable(Constants.PREFS_ARG_KEY, searchPrefs);
		prefsFragment.setArguments(args);
		prefsFragment.show(this.getSupportFragmentManager(), "test");
	}
	
	private void setupHandlers() {
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageResult imageResult = imageResultsList.get(position);
				Intent intentFullImage = new Intent(getApplicationContext(), FullImageActivity.class);
				intentFullImage.putExtra(Constants.IMAGE_URL_PARAM, imageResult.getFullUrl());
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
//		Toast.makeText(getApplicationContext(), "Loading " + page, Toast.LENGTH_SHORT).show();
		if (searchQuery == null || searchQuery.trim().isEmpty()) {
			return;
		}
		
		int offset = page * 8;
		String searchUrl = GOOGLE_IMG_SEARCH_API_URL + "?rsz=8&start=" + offset + "&v=1.0&q=" + Uri.encode(searchQuery);
		if (searchPrefs.getColorFilter() != null &&
			!searchPrefs.getColorFilter().equals(SearchPreferences.Color.any)) {
			searchUrl += "&imgcolor=" + searchPrefs.getColorFilter().toString();
		}
		if (searchPrefs.getSizeFilter() != null &&
			!searchPrefs.getSizeFilter().equals(SearchPreferences.Size.any)) {
			searchUrl += "&imgsz=" + searchPrefs.getSizeFilter().toString();
		}
		if (searchPrefs.getTypeFilter() != null &&
			!searchPrefs.getTypeFilter().equals(SearchPreferences.Type.any)) {
			searchUrl += "&imgtype=" + searchPrefs.getTypeFilter().toString();
		}
		if (searchPrefs.getSitefilter() != null) {
			searchUrl += "&as_sitesearch=" + searchPrefs.getSitefilter();
		}

		Log.d("img-search-debug", "SEARCH_URL : " + searchUrl);
		
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


	@Override
	public void onPrefsSet(SearchPreferences preferences) {
		if (preferences != null) {
			searchPrefs.setTypeFilter(preferences.getTypeFilter());
			searchPrefs.setSizeFilter(preferences.getSizeFilter());
			searchPrefs.setColorFilter(preferences.getColorFilter());
			searchPrefs.setSitefilter(preferences.getSitefilter());
		}

		savePreferences();
	}

	private void loadPreferences() {
		SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		String color = sharedPref.getString(COLOR_PREF_KEY, null);
		String size = sharedPref.getString(SIZE_PREF_KEY, null);
		String type = sharedPref.getString(TYPE_PREF_KEY, null);
		String site = sharedPref.getString(SITE_PREF_KEY, null);
		
		if (color != null) {
			searchPrefs.setColorFilter(SearchPreferences.Color.valueOf(color));
		}
		if (size != null) {
			searchPrefs.setSizeFilter(SearchPreferences.Size.valueOf(size));
		}
		if (type != null) {
			searchPrefs.setTypeFilter(SearchPreferences.Type.valueOf(type));
		}
		searchPrefs.setSitefilter(site);
	}

	private void savePreferences() {
		SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = sharedPref.edit();
		prefEditor.putString(SIZE_PREF_KEY, searchPrefs.getSizeFilter().toString());
		prefEditor.putString(COLOR_PREF_KEY, searchPrefs.getColorFilter().toString());
		prefEditor.putString(TYPE_PREF_KEY, searchPrefs.getTypeFilter().toString());
		prefEditor.putString(SITE_PREF_KEY, searchPrefs.getSitefilter());
		prefEditor.commit();
	}
}
