package com.cic.localization;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.cic.localization.algorithm.LocationCalculator;

public class Locater implements OnDataArrvialListener{
	Map<Integer,LocationCalculator> locationCaluators=null;
	Map<Integer, Map<Integer,Double>> lastDistances=null; 
	SerialController serialController=null;
	public Locater()
	{
		
	}
	
	public void initLocater()
	{
		locationCaluators=new HashMap<Integer,LocationCalculator>();
		lastDistances=new HashMap<Integer,Map<Integer,Double>>();
		serialController=new SerialController();
		serialController.setListener(this);
		serialController.initCommunication();
	}
	public void handleSerialData(int id, Map<Integer, Double> distanceMap,
			double[] gyro) {
		if(lastDistances.containsKey(id))
		{
			lowpassFilter(lastDistances.get(id),distanceMap);
		}
		lastDistances.put(id, distanceMap);
		if(!locationCaluators.containsKey(id))
		{
			LocationCalculator locationCaluator=new LocationCalculator();
			locationCaluator.setAnchorPositionMap(Config.anchorPositionMap);
			locationCaluator.init();
			locationCaluators.put(id, locationCaluator);
		}
		LocationCalculator lc=locationCaluators.get(id);
		Point2D result=lc.calculate(distanceMap, gyro[2]);
	}
	
	private void writeResultToDatabase(int id, Map<Integer, Double> distanceMap,double[] gyro,Point2D p)
	{
		
	}
	
	
	public void lowpassFilter(Map<Integer,Double> last,Map<Integer,Double> now)
	{
		Set<Integer> keys=last.keySet();
		Iterator<Integer> it=keys.iterator();
		while(it.hasNext())
		{
			int tid=it.next();
			if(!now.containsKey(tid)) continue;
			double nvalue=now.get(tid);
			double lvalue=now.get(tid);
			if(nvalue-lvalue>1) nvalue=lvalue+1;
			else if(lvalue-nvalue>1) nvalue=lvalue-1;
			now.put(tid, nvalue);
		}
	}
	
}
