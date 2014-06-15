package com.mb.image_search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.mb.image_search.model.SearchPreferences;

public class SearchPrefsPickerFragment extends SherlockDialogFragment implements OnItemSelectedListener {
	
	private SearchPreferences searchPrefs;
	
	Spinner spnImgSize;
	Spinner spnColor;
	Spinner spnType;
	EditText etFilter;
	Button btnSave;
	
	ArrayAdapter<CharSequence> spnImgSizeAdapter;
	ArrayAdapter<CharSequence> spnColorAdapter;
	ArrayAdapter<CharSequence> spnTypeAdapter;
	
	OnPreferencesSetListener listener;
	
	public static SearchPrefsPickerFragment newInstance() {
		SearchPrefsPickerFragment searchPrefsFragment = new SearchPrefsPickerFragment();
		return searchPrefsFragment;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = this.getArguments();
		searchPrefs = (SearchPreferences) args.get(Constants.PREFS_ARG_KEY);
		
		View view = inflater.inflate(R.layout.settings_layout, container);
		
		spnImgSizeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.size_array, android.R.layout.simple_spinner_item);
		spnImgSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnImgSize = (Spinner) view.findViewById(R.id.spnImgSize);
		spnImgSize.setAdapter(spnImgSizeAdapter);

		spnColorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.color_array, android.R.layout.simple_spinner_item);
		spnColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnColor = (Spinner) view.findViewById(R.id.spnColor);
		spnColor.setAdapter(spnColorAdapter);
		
		spnTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, android.R.layout.simple_spinner_item);
		spnTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnType = (Spinner) view.findViewById(R.id.spnType);
		spnType.setAdapter(spnTypeAdapter);
		
		etFilter = (EditText) view.findViewById(R.id.etFilter);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		
		getDialog().setTitle(getResources().getString(R.string.adv_search_options_label));
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		loadPreferencesInDialog();
		
		spnImgSize.setOnItemSelectedListener(this);
		spnColor.setOnItemSelectedListener(this);
		spnType.setOnItemSelectedListener(this);
		
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				searchPrefs.setSitefilter(etFilter.getText().toString());
				listener.onPrefsSet(searchPrefs);
			}
		});
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnPreferencesSetListener) {
			listener = (OnPreferencesSetListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement SearchPrefsPickerFragment.OnPreferencesSetListener");
		}
	}
	
//	public void savePreferences(View v) {
//	}
	
	private void loadPreferencesInDialog() {
		if (searchPrefs != null) {
			if (searchPrefs.getColorFilter() != null) {
				spnColor.setSelection(spnColorAdapter.getPosition(searchPrefs.getColorFilter().toString()));
			}
			
			if (searchPrefs.getTypeFilter() != null) {
				spnType.setSelection(spnTypeAdapter.getPosition(searchPrefs.getTypeFilter().toString()));
			}
			
			if (searchPrefs.getSizeFilter() != null) {
				spnImgSize.setSelection(spnImgSizeAdapter.getPosition(searchPrefs.getSizeFilter().toString()));
			}
			
			etFilter.setText(searchPrefs.getSitefilter());
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String value = (String) parent.getItemAtPosition(position);
		if (R.id.spnColor == parent.getId()) {
			searchPrefs.setColorFilter(SearchPreferences.Color.valueOf(value));
		} else if (R.id.spnImgSize == parent.getId()) {
			searchPrefs.setSizeFilter(SearchPreferences.Size.valueOf(value));
		} else if (R.id.spnType == parent.getId()) {
			searchPrefs.setTypeFilter(SearchPreferences.Type.valueOf(value));
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {

		
	}
	
	public interface OnPreferencesSetListener {
		public void onPrefsSet(SearchPreferences preferences);
	}


}
