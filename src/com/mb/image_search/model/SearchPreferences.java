package com.mb.image_search.model;

import java.io.Serializable;

public class SearchPreferences implements Serializable {
	
	public enum Size {any, small, medium, large, xlarge};
	public enum Type {any, face, photo, clipart, lineart};
	public enum Color {any, black, blue, brown, gray, green, orange, purple, white, yellow}
	
	Size sizeFilter;
	Type typeFilter;
	Color colorFilter;
	String sitefilter;
	
	public SearchPreferences() {
		sizeFilter = null;
		typeFilter = null;
		colorFilter = null;
		sitefilter = null;
	}
	
	public Size getSizeFilter() {
		return sizeFilter;
	}
	
	public void setSizeFilter(Size sizeFilter) {
		this.sizeFilter = sizeFilter;
	}
	
	public Type getTypeFilter() {
		return typeFilter;
	}
	
	public void setTypeFilter(Type typeFilter) {
		this.typeFilter = typeFilter;
	}
	
	public Color getColorFilter() {
		return colorFilter;
	}
	
	public void setColorFilter(Color colorFilter) {
		this.colorFilter = colorFilter;
	}
	
	public String getSitefilter() {
		return sitefilter;
	}
	
	public void setSitefilter(String sitefilter) {
		this.sitefilter = sitefilter;
	}

}
