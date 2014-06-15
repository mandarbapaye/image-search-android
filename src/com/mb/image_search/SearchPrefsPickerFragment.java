package com.mb.image_search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.mb.image_search.model.SearchPreferences;

public class SearchPrefsPickerFragment extends SherlockDialogFragment {
	
	public static SearchPrefsPickerFragment newInstance() {
		SearchPrefsPickerFragment searchPrefsFragment = new SearchPrefsPickerFragment();
//		Bundle args = new Bundle();
//		args.putLong(REMINDER_TIME_ARG, currentReminderTime);
//		timePickerFragment.setArguments(args);
		return searchPrefsFragment;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_layout, container);
		
		ArrayAdapter<CharSequence> spnImgSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.size_array, android.R.layout.simple_spinner_item);
		spnImgSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spnImgSize = (Spinner) view.findViewById(R.id.spnImgSize);
		spnImgSize.setAdapter(spnImgSizeAdapter);
		
		ArrayAdapter<CharSequence> spnColorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.color_array, android.R.layout.simple_spinner_item);
		spnColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spnColor = (Spinner) view.findViewById(R.id.spnColor);
		spnColor.setAdapter(spnColorAdapter);
		
		ArrayAdapter<CharSequence> spnTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, android.R.layout.simple_spinner_item);
		spnTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spnType = (Spinner) view.findViewById(R.id.spnType);
		spnType.setAdapter(spnTypeAdapter);
		
//		
//		EditText mEditText = (EditText) view.findViewById(R.id.txt_your_name);
////		String title = getArguments().getString("title", "Enter Name");
//		// Show soft keyboard automatically
//		mEditText.requestFocus();
		getDialog().setTitle(getResources().getString(R.string.adv_search_options_label));
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}
	
	public interface OnPreferencesSetListener {
		public void onPrefsSet(SearchPreferences preferences);
	}

}
