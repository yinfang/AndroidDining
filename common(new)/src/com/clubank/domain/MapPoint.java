package com.clubank.domain;

public class MapPoint extends Point {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7524027948225171758L;

	public MapPoint(double latitude, double longitude) {
		super(latitude, longitude);
	}

	public MapPoint(double latitude, double longitude, int iconMark) {
		super(latitude, longitude);
		this.iconMark = iconMark;
	}

	public MapPoint(double latitude, double longitude, String name) {
		super(latitude, longitude, name);
	}

	public MapPoint(double latitude, double longitude, String name, int iconMark) {
		super(latitude, longitude, name);
		this.iconMark = iconMark;
	}

	public int iconMark;

}
