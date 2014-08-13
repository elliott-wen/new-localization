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
	
	private double updateFrequency=1;
	private RealMatrix matrixX=null;
	
	private RealMatrix matrixP=null;
	
	private RealMatrix matrixQ=null;
	private RealMatrix matrixH=null;
	private RealMatrix matrixI=null;
	private RealMatrix matrixER=null;
	private double defaultA[][]={{1,0,0,updateFrequency,0,0},{ 0,1,0,0,updateFrequency,0},{0,0,1,0,0,updateFrequency},{ 0,0,0,1,0,0}, {0,0,0,0,1,0},{0,0,0,0,0,1}};
	private double defaultR[]={2,2,2,2,0.2,0.2,0.01};
	private double defaultQ[]={2,2,2,0.5,0.5,0.01};
	private double defaultX[]={0.01,0.01,0.01,0.01,0.01,0.01};
	private double defaultP[]={1000000,1000000,1000000,1000000,1000000,1000000};
	private double defaultH[][]={{1,1,1,0,0,0},{1,1,1,0,0,0},{1,1,1,0,0,0},{1,1,1,0,0,0},{ 0,0,0,1,0,0 },{0,0,0,0,1,0},{0,0,0,0,0,1}};
	private double defaultER[]={0,0,0,0};
	private Map<Integer,Double> systemDistance=null;
	//private double THRESOLDDISTANCE=2;
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
		
		this.matrixP=MatrixUtils.createRealDiagonalMatrix(defaultP);
		this.matrixI=MatrixUtils.createRealIdentityMatrix(6);
		this.matrixA=MatrixUtils.createRealMatrix(defaultA);
		this.systemDistance=new HashMap<Integer,Double>();
		this.matrixER=MatrixUtils.createRowRealMatrix(defaultER);
	}
	public Point2D calculate(Map<Integer,Double> distanceData, double velocity[])
	{
		/*Iterator<Integer> keyIt=distanceData.keySet().iterator();
		
		for(int j=0;j<distanceData.size();j++)
		{
				Integer key=keyIt.next();
				if(distanceData.get(key)<THRESOLDDISTANCE)
				{
					matrixR.setEntry(j, j, 0.2*distanceData.get(key)+0.01);
				}
		}*/
		Iterator<Integer> keyIt=distanceData.keySet().iterator();
		matrixR=MatrixUtils.createRealDiagonalMatrix(defaultR);
		this.matrixX=this.matrixA.multiply(this.matrixX);
		
		this.matrixP=this.matrixA.multiply(this.matrixP).multiply(this.matrixA.transpose()).add(this.matrixQ);
		
		this.matrixH=MatrixUtils.createRealMatrix(defaultH);
		keyIt=distanceData.keySet().iterator();
		//System.out.println(matrixX);
		for(int j=0;j<distanceData.size();j++)
		{
				Integer key=keyIt.next();
				Point2D lp=anchorPositionMap.get(key);
				double temp=Math.sqrt(Math.pow(matrixX.getEntry(0, 0)-lp.getX(), 2)+Math.pow(matrixX.getEntry(1, 0)-lp.getY(), 2)+Math.pow(matrixX.getEntry(2, 0)-0, 2));
				//System.out.println(temp);
				this.systemDistance.put(key, temp);
				//System.out.println(matrixX.getEntry(0, 0)-lp.getX());
				double h1=(matrixX.getEntry(0, 0)-lp.getX())/temp;
				//System.out.println(matrixX.getEntry(0, 0)+"-"+lp.getX()+"-"+temp+"-"+h1);
				this.matrixH.setEntry(j,0,h1);
				double h2=(matrixX.getEntry(1, 0)-lp.getY())/temp;
				this.matrixH.setEntry(j,1,h2);
				this.matrixH.setEntry(j, 2, 0);
				//System.out.println(h1+":"+h2);
				//==============
				//System.out.println(temp);
				//System.out.println(key+":"+Math.abs(temp-distanceData.get(key)));
				this.matrixER.setEntry(0, j, Math.abs(temp-distanceData.get(key)));
				if(this.matrixER.getEntry(0, j)>1)
				{
					this.matrixR.setEntry(j, j,this.matrixER.getEntry(0, j)*this.matrixER.getEntry(0, j));
					
					if(distanceData.get(key)<5)
					{
						this.matrixR.setEntry(j, j,0.1*distanceData.get(key));
					}
				}
				else
				{
					this.matrixR.setEntry(j, j,0.15);
				}
				//===============
		}
		//System.out.println(matrixER);
		//System.out.println(matrixH);
		/*double h53=-this.matrixX.getEntry(3, 0)/(Math.pow(this.matrixH.getEntry(2, 0), 2)+Math.pow(this.matrixH.getEntry(3, 0), 2));
		this.matrixH.setEntry(4, 2, h53);
		double h54=this.matrixX.getEntry(2, 0)/(Math.pow(this.matrixH.getEntry(2, 0), 2)+Math.pow(this.matrixH.getEntry(3, 0), 2));
		this.matrixH.setEntry(4, 3, h54);*/
		//System.out.println(matrixER);
	
		RealMatrix dominate=this.matrixH.multiply(this.matrixP).multiply(this.matrixH.transpose()).add(this.matrixR);
		RealMatrix dominateInv=new LUDecomposition(dominate).getSolver().getInverse();
		this.matrixK=this.matrixP.multiply(this.matrixH.transpose()).multiply(dominateInv);
		
		
		keyIt=distanceData.keySet().iterator();
		double z[]=new double[distanceData.size()+3];
		//z[z.length-1]=angle/57.6;
		int tj=0;
		for(tj=0;tj<distanceData.size();tj++)
		{
			Integer key=keyIt.next();
			z[tj]=distanceData.get(key);
		}
		z[tj]=velocity[0];
		z[tj+1]=velocity[1];
		z[tj+2]=velocity[2];
		RealMatrix matrixtinZ=MatrixUtils.createColumnRealMatrix(z);
		
		keyIt=distanceData.keySet().iterator();
		double h[]=new double[distanceData.size()+3];
		h[h.length-1]=this.matrixX.getEntry(5, 0);
		h[h.length-2]=this.matrixX.getEntry(4, 0);
		h[h.length-3]=this.matrixX.getEntry(3, 0);
		for(int j=0;j<distanceData.size();j++)
		{
			Integer key=keyIt.next();
			h[j]=this.systemDistance.get(key);
		}
		RealMatrix matrixtinH=MatrixUtils.createColumnRealMatrix(h);
		
		RealMatrix matrixE=matrixtinZ.subtract(matrixtinH);
		//System.out.println(matrixE);
		this.matrixX=this.matrixX.add(this.matrixK.multiply(matrixE));
		//System.out.println(matrixX);
		this.matrixP=this.matrixI.subtract(this.matrixK.multiply(this.matrixH)).multiply(this.matrixP);
		Point2D point=new Point2D.Double(matrixX.getEntry(0, 0),matrixX.getEntry(1, 0));
		return point;
		
	}
	
/*	public static void main(String args[])
	{
		LocationCalculator l=new LocationCalculator();
		l.init();
		Map<Integer,Point2D> anchorMap=new HashMap<Integer,Point2D>();
		anchorMap.put(1, new Point2D.Double(0,0));
		anchorMap.put(2, new Point2D.Double(100,0));
		anchorMap.put(3, new Point2D.Double(100,100));
		anchorMap.put(4, new Point2D.Double(0,100));
		l.setAnchorPositionMap(anchorMap);
		for(int j=0;j<10;j+=1)
		{
			Map<Integer,Double> distanceMap=new HashMap<Integer,Double>();
			Point2D np=new Point2D.Double(50.0,50.0);
			System.out.println("Current Point:"+np.getX()+" "+np.getY());
			for(int i=1;i<=4;i++)
			{
				distanceMap.put(i,np.distance(anchorMap.get(i)));
				
				//System.out.print("Distance between "+i+":"+distanceMap.get(i));
			}
			double v[]={0,0,0};
			Point2D cp=l.calculate(distanceMap,v);
			System.out.println();;
			System.out.println("Result:"+cp.getX()+" "+cp.getY());
		}
	}*/
}