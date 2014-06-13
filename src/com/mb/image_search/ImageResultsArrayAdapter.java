package com.mb.image_search;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;
import com.mb.image_search.model.ImageResult;

public class ImageResultsArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsArrayAdapter(Context context, List<ImageResult> imageResults) {
		super(context, R.layout.item_image_result, imageResults);
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	// getView is called for each item in the list associated with this adapter
    	// and is responsible for returning the "view" for that item.
    	ImageResult imageResult = this.getItem(position);
    	
    	SmartImageView svImage;
    	if (convertView != null) {
    		svImage = (SmartImageView) convertView;
    		svImage.setImageResource(android.R.color.transparent);
    	} else {
    		svImage = (SmartImageView) LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
    	}
    	
    	svImage.setImageUrl(imageResult.getThumbUrl());
    	return svImage;
    }
	
}
