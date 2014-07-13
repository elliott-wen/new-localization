package com.cic.localization.algorithm;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;


public class LocationCalculator {
	private static Logger logger = Logger.getLogger(LocationCalculator.class);
	private Map<Integer,Point2D> anchorPositionMap;
	private RealMatrix matrixA=null;
	private RealMatrix matrixR=null;
	private RealMatrix matrixK=null;
	
	private double updateFrequency=0.2;
	private RealMatrix matrixX=null;
	
	private RealMatrix matrixP=null;
	
	private RealMatrix matrixQ=null;
	private RealMatrix matrixH=null;
	private RealMatrix matrixI=null;
	private double defaultA[][]={{1,0,updateFrequency,0},{ 0,1,0,updateFrequency},{ 0,0,1,0}, {0,0,0,1}};
	private double defaultR[]={2,2,2,2,10};
	private double defaultQ[]={2,2,10,10};
	private double defaultX[]={0.01,0.01,0.5,0.0001};
	private double defaultH[][]={{1,0,0,0},{0,1,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
	private Map<Integer,Double> systemDistance=null;
	private double THRESOLDDISTANCE=2;
	public Map<Integer, Point2D> getAnchorPositionMap() {
		return anchorPositionMap;
	}
	public void setAnchorPositionMap(Map<Integer, Point2D> anchorPositionMap) {
		this.anchorPositionMap = anchorPositionMap;
	}
	public void init()
	{
		//Q=diag([2 2 1 1]);
		//R=diag([2 2 2 2 10]);
		this.matrixQ=MatrixUtils.createRealDiagonalMatrix(defaultQ);
		//this.matrixR=MatrixUtils.createRealDiagonalMatrix(defaultR);.
		this.matrixX=MatrixUtils.createColumnRealMatrix(defaultX);
		
		this.matrixP=MatrixUtils.createRealIdentityMatrix(4);
		this.matrixI=MatrixUtils.createRealIdentityMatrix(4);
		this.matrixA=MatrixUtils.createRealMatrix(defaultA);
		this.systemDistance=new HashMap<Integer,Double>();
	}
	public Point2D calculate(Map<Integer,Double> distanceData,double angle)
	{
		Iterator<Integer> keyIt=distanceData.keySet().iterator();
		matrixR=MatrixUtils.createRealDiagonalMatrix(defaultR);
		for(int j=0;j<distanceData.size();j++)
		{
				Integer key=keyIt.next();
				if(distanceData.get(key)<THRESOLDDISTANCE)
				{
					matrixR.setEntry(j, j, 0.2*distanceData.get(key)+0.01);
				}
		}
		this.matrixX=this.matrixA.multiply(this.matrixX);
		this.matrixP=this.matrixA.multiply(this.matrixP).multiply(this.matrixA.transpose()).add(this.matrixQ);
		this.matrixH=MatrixUtils.createRealMatrix(defaultH);
		keyIt=distanceData.keySet().iterator();
		for(int j=0;j<distanceData.size();j++)
		{
				Integer key=keyIt.next();
				Point2D lp=anchorPositionMap.get(key);
				double temp=Math.sqrt(Math.pow(matrixX.getEntry(0, 0)-lp.getX(), 2)+Math.pow(matrixX.getEntry(1, 0)-lp.getY(), 2));
				this.systemDistance.put(key, temp);
				double h1=(matrixX.getEntry(0, 0)-lp.getX())/temp;
				this.matrixH.setEntry(j,0,h1);
				double h2=(matrixX.getEntry(1, 0)-lp.getY())/temp;
				this.matrixH.setEntry(j,1,h2);
		}
		double h53=-this.matrixX.getEntry(3, 0)/(Math.pow(this.matrixH.getEntry(2, 0), 2)+Math.pow(this.matrixH.getEntry(3, 0), 2));
		this.matrixH.setEntry(4, 2, h53);
		double h54=this.matrixX.getEntry(2, 0)/(Math.pow(this.matrixH.getEntry(2, 0), 2)+Math.pow(this.matrixH.getEntry(3, 0), 2));
		this.matrixH.setEntry(4, 3, h54);
		
		keyIt=distanceData.keySet().iterator();
		double z[]=new double[distanceData.size()+1];
		z[z.length-1]=angle/57.6;
		for(int j=0;j<distanceData.size();j++)
		{
			Integer key=keyIt.next();
			z[j]=distanceData.get(key);
		}
		RealMatrix matrixtinZ=MatrixUtils.createColumnRealMatrix(z);
		keyIt=distanceData.keySet().iterator();
		double h[]=new double[distanceData.size()+1];
		h[h.length-1]=Math.atan(this.matrixX.getEntry(3, 0)/this.matrixX.getEntry(2, 0));
		for(int j=0;j<distanceData.size();j++)
		{
			Integer key=keyIt.next();
			h[j]=this.systemDistance.get(key);
		}
		RealMatrix matrixtinH=MatrixUtils.createColumnRealMatrix(h);
		RealMatrix dominate=this.matrixH.multiply(this.matrixP).multiply(this.matrixH.transpose()).add(this.matrixR);
		RealMatrix dominateInv=new LUDecomposition(dominate).getSolver().getInverse();
		this.matrixK=this.matrixP.multiply(this.matrixH.transpose()).multiply(dominateInv);
		
		this.matrixX=this.matrixX.add(this.matrixK.multiply(matrixtinZ.subtract(matrixtinH)));
		this.matrixP=this.matrixI.subtract(this.matrixK.multiply(this.matrixH)).multiply(this.matrixP);
		Point2D point=new Point2D.Double(matrixX.getEntry(0, 0),matrixX.getEntry(1, 0));
		return point;
		
	}
	
	/*public void main(String args[])
	{
		Localization l=new Localization();
		l.init();
		Map<Integer,Point2D> anchorMap=new HashMap<Integer,Point2D>();
		anchorMap.put(1, new Point2D.Double(0,0));
		anchorMap.put(2, new Point2D.Double(100,0));
		anchorMap.put(3, new Point2D.Double(100,100));
		anchorMap.put(4, new Point2D.Double(0,100));
		l.setAnchorPositionMap(anchorMap);
		for(int j=0;j<200;j+=1)
		{
			Map<Integer,Double> distanceMap=new HashMap<Integer,Double>();
			Point2D np=new Point2D.Double((double)(j)*0.5,(double)(j)*0.5);
			System.out.println("Current Point:"+np.getX()+" "+np.getY());
			for(int i=1;i<=4;i++)
			{
				distanceMap.put(i,np.distance(anchorMap.get(i)));
				//System.out.print("Distance between "+i+":"+distanceMap.get(i));
			}
			Point2D cp=l.calculate(distanceMap, 45);
			//System.out.println();
			System.out.println("Result:"+cp.getX()+" "+cp.getY());
		}
	}*/
}