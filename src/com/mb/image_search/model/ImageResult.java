package com.mb.image_search.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult {
	
	private String fullUrl;
	private String thumbUrl;
	
	public ImageResult() {
		fullUrl = null;
		thumbUrl = null;
	}
	
	public ImageResult(JSONObject jsonImageResult) {
		try {
			fullUrl = jsonImageResult.getString("url");
			thumbUrl = jsonImageResult.getString("tbUrl");
		} catch (JSONException e) {
			fullUrl = null;
			thumbUrl = null;
		}
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	
	public String getThumbUrl() {
		return thumbUrl;
	}
	
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	
	@Override
	public String toString() {
		return this.thumbUrl;
	}
	
	public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonImageArray) {
		ArrayList<ImageResult> imageResultList = new ArrayList<ImageResult>();
		if (jsonImageArray != null && jsonImageArray.length() > 0) {
			for (int i = 0; i < jsonImageArray.length(); i++) {
				try {
					imageResultList.add(new ImageResult(jsonImageArray.getJSONObject(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return imageResultList;
	}

}
