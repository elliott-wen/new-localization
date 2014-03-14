package com.cic.localization;

import java.util.Map;

public interface OnDataArrvialListener {
	public void handleSerialData(int id, Map<Integer,Double> distanceMap,double[] gyro);
}
