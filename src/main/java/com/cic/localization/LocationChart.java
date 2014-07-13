package com.cic.localization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class LocationChart {
	
	private Map<Integer,XYSeries> computerSeries=null;
	
	private JFreeChart localJFreeChart=null;
	private XYSeriesCollection xyseriescollection=null;
	private XYPlot localXYPlot=null;
	private ChartPanel localChartPanel=null;
	
	public ChartPanel initPanel()
	{
		createDataset();
		createChart();
		drawBackGround();
		adjustAnnotaionSize();
		adjustRange();
		drawDangerousZone();
		createPanel();
		return localChartPanel;
	}
	private void createChart()
	{
	    localJFreeChart = ChartFactory.createScatterPlot("Position", "X", "Y", xyseriescollection, PlotOrientation.VERTICAL, true, false, false);
	    localXYPlot = (XYPlot)localJFreeChart.getPlot();
	    localXYPlot.setNoDataMessage("NO DATA");
	    localXYPlot.setDomainGridlinesVisible(true);
	    localXYPlot.setRangeGridlinesVisible(true);   
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        localXYPlot.setRenderer(renderer);
	    
	}
	private void drawDangerousZone()
	{
		List<List<Point2D>> zones=Config.dangerousZones;
		for(int i=0;i<zones.size();i++)
		{
			
			Rectangle2D rec=new Rectangle2D.Double();
			List<Point2D> inner=zones.get(i);
			Point2D lpoint=inner.get(0);
			Point2D rpoint=inner.get(1);
			rec.setRect(lpoint.getX(), lpoint.getY(), rpoint.getX()-lpoint.getX(), rpoint.getY()-lpoint.getY());
			XYShapeAnnotation anno=new XYShapeAnnotation(rec, new BasicStroke(2.0f), Color.red);
			localXYPlot.addAnnotation(anno);
			
		}
		
	}
	private void adjustAnnotaionSize()
	{
		double size = 20.0;
	    double delta = size / 2.0;
	    Shape shape1 = new Rectangle2D.Double(-delta, -delta, size, size);
	    Shape shape2 = new Rectangle2D.Double(-delta, -delta, size, size);
	    Shape shape3 = new Rectangle2D.Double(-delta, -delta, size, size);
	    Shape shape4 = new Rectangle2D.Double(-delta, -delta, size, size);
	    //Shape shape5 = new Ellipse2D.Double(-delta, -delta, size, size);
	    localXYPlot.getRenderer().setSeriesShape(0, shape1);
	    localXYPlot.getRenderer().setSeriesShape(1, shape2);
	    localXYPlot.getRenderer().setSeriesShape(2, shape3);
	    localXYPlot.getRenderer().setSeriesShape(3, shape4);
	    //localXYPlot.getRenderer().setSeriesShape(4, shape5);
	    
	}
	private void adjustRange()
	{
		double rangeTick=1;
		
		NumberAxis localNumberAxis1 = (NumberAxis)localXYPlot.getDomainAxis();
	    localNumberAxis1.setAutoRange(true);
	    localNumberAxis1.setTickUnit(new NumberTickUnit(rangeTick)); 
	    NumberAxis localNumberAxis2 = (NumberAxis)localXYPlot.getRangeAxis();
	    localNumberAxis2.setAutoRange(true);
	    localNumberAxis2.setTickUnit(new NumberTickUnit(rangeTick)); 
	    double maxX=0;
	    double maxY=0;
	    Iterator<Entry<Integer,Point2D>> iter=Config.anchorPositionMap.entrySet().iterator();
		while(iter.hasNext())
		{
			  Entry<Integer,Point2D> entry=iter.next();
			  if(entry.getValue().getX()>maxX) maxX=entry.getValue().getX();
			  if(entry.getValue().getY()>maxY) maxY=entry.getValue().getY();
		}
		localNumberAxis1.setRange(-2, maxX+2);
		localNumberAxis2.setRange(-2, maxY+2);
	}
	private void drawBackGround()
	{
		if(Config.CHARTBG!=null&&!Config.CHARTBG.isEmpty())
		try {
			localXYPlot.setBackgroundImage((ImageIO.read(new File(Config.CHARTBG))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void createDataset() 
    {
	  //tagSeries = new XYSeries("Position from Tag"); 
		computerSeries=new HashMap<Integer,XYSeries>();
		
		  //computerSeries = new XYSeries("New Position from Computer");
		  //oldComputerSeries =new XYSeries("Histroy Position from Computer");
		  xyseriescollection = new XYSeriesCollection();  
		  Iterator<Entry<Integer,Point2D>> iter=Config.anchorPositionMap.entrySet().iterator();
		  while(iter.hasNext())
		  {
			  Entry<Integer,Point2D> entry=iter.next();
			  XYSeries series=new XYSeries("Anchor "+entry.getKey());
			  series.add(entry.getValue().getX(), entry.getValue().getY());
			  xyseriescollection.addSeries(series);
		  }           
		  //xyseriescollection.addSeries(tagSeries);
		  //xyseriescollection.addSeries(computerSeries); 
		  //xyseriescollection.addSeries(oldComputerSeries); 
		  
	}
	public void createPanel()
	{
			
			localChartPanel = new ChartPanel(localJFreeChart);
		    localChartPanel.setVerticalAxisTrace(true);		   
		    localChartPanel.setHorizontalAxisTrace(true);
		    //localChartPanel.setPopupMenu(null);
		    localChartPanel.setDomainZoomable(true);
		    localChartPanel.setRangeZoomable(true);
		    //localChartPanel.setPreferredSize(new Dimension(812, 679));
		    //localChartPanel.setPreferredSize(new Dimension(600, 600));
		    
		    //RefineryUtilities.positionFrameRandomly(this);
	}
	public void updateLocation(final int tagId,final Point2D point,final boolean warning)
	{
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
			    	if(!computerSeries.containsKey(tagId))
			    	{
			    		XYSeries lcomputerSeries=new XYSeries("Current:"+tagId,false);
			    		
			    		computerSeries.put(tagId, lcomputerSeries);
			    		
			    		xyseriescollection.addSeries(lcomputerSeries); 
			  		  	
		
			    	}
			    	XYSeries lcomputerSeries=computerSeries.get(tagId);
					lcomputerSeries.add(point.getX(), point.getY());
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
