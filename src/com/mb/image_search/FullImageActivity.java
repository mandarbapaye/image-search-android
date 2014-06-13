package com.mb.image_search;

import static com.mb.image_search.Constants.IMAGE_URL_PARAM;
import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.image.SmartImageView;

public class FullImageActivity extends Activity {
	
	SmartImageView sivFullImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);
		setupViews();
		
		String fullImageUrl = getIntent().getStringExtra(IMAGE_URL_PARAM);
		sivFullImage.setImageUrl(fullImageUrl);
	}

	private void setupViews() {
		sivFullImage = (SmartImageView) findViewById(R.id.sivFullImage);
	}
}
