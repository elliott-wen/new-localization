package com.cic.localization;

import java.util.Map;

public interface OnDataArrvialListener {
	public void handleSerialData(int id,Map<Integer,Double> distacneMap, double velocity[],double locationX,double locationY,double temperature,double voltage, double angle);
}
