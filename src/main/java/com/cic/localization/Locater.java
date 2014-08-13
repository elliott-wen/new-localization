package com.cic.localization;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cic.localization.algorithm.LocationCalculator;

public class Locater implements OnDataArrvialListener{
	Map<Integer,LocationCalculator> locationCaluators=null;
	//Map<Integer, Map<Integer,Double>> lastDistances=null; 
	SerialController serialController=null;
	LocationEventListener el=null;
	public Locater()
	{
		
	}
	
	public void initLocater()
	{
		locationCaluators=new HashMap<Integer,LocationCalculator>();
		//lastDistances=new HashMap<Integer,Map<Integer,Double>>();
		serialController=new SerialController();
		serialController.setListener(this);
		serialController.initCommunication();
	}
	/*public void handleSerialData(int id, Map<Integer, Double> distanceMap,
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
		writeResultToDatabase(id,distanceMap,gyro,result);
		if(el!=null)
		{
			if(isInDangerousZone(result))
			{
				el.onLocationChange(id,distanceMap,gyro,result,true);
			}
			el.onLocationChange(id,distanceMap,gyro,result,false);
		}
	}*/
	
	/*private void writeResultToDatabase(int id, Map<Integer, Double> distanceMap,double[] gyro,Point2D p)
	{
		
	}*/
	
	private boolean isInDangerousZone(Point2D p)
	{
		List<List<Point2D>> zones=Config.dangerousZones;
		for(int i=0;i<zones.size();i++)
		{
			List<Point2D> inner=zones.get(i);
			Point2D lpoint=inner.get(0);
			Point2D rpoint=inner.get(1);
			if(p.getX()>lpoint.getX()&&p.getY()>lpoint.getY()&&p.getX()<rpoint.getX()&&p.getY()<rpoint.getY())
			{
				return true;
			}
		}
		return false;
	}
	
	/*public void lowpassFilter(Map<Integer,Double> last,Map<Integer,Double> now)
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
	}*/

	public LocationEventListener getLocationEventListener() {
		return el;
	}

	public void setLocationEventListener(LocationEventListener el) {
		this.el = el;
	}

	public void handleSerialData(int id, Map<Integer,Double> distanceMap, double velocity[],double locationX, double locationY,
			double temperature, double voltage, double angle) {
		//System.out.println("Debug Symbol1");
		//writeResultToDatabase(id,distanceMap,angle,result);
		if(!locationCaluators.containsKey(id))
		{
			LocationCalculator locationCaluator=new LocationCalculator();
			locationCaluator.setAnchorPositionMap(Config.anchorPositionMap);
			locationCaluator.init();
			locationCaluators.put(id, locationCaluator);
		}
		LocationCalculator lc=locationCaluators.get(id);
		//System.out.println("Debug Symbol2");
		Point2D result=lc.calculate(distanceMap, velocity);
		//System.out.println(result);
		//writeResultToDatabase(id,distanceMap,gyro,result);
		if(el!=null)
		{
			if(isInDangerousZone(result))
			{
				el.onLocationChange(id,result,0,true);
			}
			el.onLocationChange(id,result,0,false);
		}
		
		
		/*if(el!=null)
		{
			Point2D result=new Point2D.Double(locationX,locationY);
			if(isInDangerousZone(result))
			{
				el.onLocationChange(id, result, angle, true);
			}
			el.onLocationChange(id, result, angle, false);
		}*/
		
	}
	
}
