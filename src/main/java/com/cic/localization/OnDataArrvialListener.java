package com.cic.localization;

import java.util.Map;

public interface OnDataArrvialListener {
	public void handleSerialData(int id,double locationX,double locationY,double temperature,double voltage, double angle);
}
