package com.cic.localization;

import java.awt.geom.Point2D;
import java.util.Map;

public interface LocationEventListener {
	public void onLocationChange(int id, Point2D p, double angle, boolean onDangerousZone);
	public void onVoltageChange(double voltage);
	public void onTemperatureChange(double temperature);
}
