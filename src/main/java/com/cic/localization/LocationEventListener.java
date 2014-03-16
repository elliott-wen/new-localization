package com.cic.localization;

import java.awt.geom.Point2D;
import java.util.Map;

public interface LocationEventListener {
	public void onLocationChange(int id, Map<Integer, Double> distanceMap,double[] gyro,Point2D p,boolean onDangerousZone);
	
}
