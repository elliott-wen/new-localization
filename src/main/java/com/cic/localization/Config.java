package com.cic.localization;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


public class Config {
	public static Map<Integer, Point2D> anchorPositionMap=null;
	public static List<List<Point2D>> dangerousZones=null;
	public static String PORTNAME=null;
	public static int PORTBAUDRATE=57600;
	public static String CHARTBG="";
	private static Logger logger = Logger.getLogger(Config.class);
	public static boolean loadConfig(File xmlFile)
	{
		try
		{
		  SAXBuilder builder = new SAXBuilder();
		  InputStream fileInputStream = new FileInputStream(xmlFile);
		  Document document = builder.build(fileInputStream);//获得文档对象
		  Element root = document.getRootElement();//获得根节点
		  logger.info("Loading Communication Detail");
		  Element com=root.getChild("Communication");
		  PORTNAME=com.getChildText("Port");
		  logger.info("Use port:"+PORTNAME);
		  PORTBAUDRATE=Integer.valueOf(com.getChildText("BaudRate"));
		  logger.info("Port BaudRate:"+PORTBAUDRATE);
		  logger.info("Loading Anchor Details");
		  anchorPositionMap=new HashMap<Integer, Point2D>();
		  Element anchors=root.getChild("Anchors");
		  List<Element> anchor=anchors.getChildren();
		  for(Element e:anchor)
		  {
			  int id=Integer.valueOf(e.getChildText("id"));
			  double x=Double.valueOf(e.getChildText("X"));
			  double y=Double.valueOf(e.getChildText("Y"));
			  anchorPositionMap.put(id, new Point2D.Double(x,y));
			  logger.info("Found an anchor=》ID="+id+" X="+x+" Y="+y);
		  }
		  Element chart=root.getChild("Chart");
		  CHARTBG=chart.getChildText("bgimg");
		  dangerousZones=new ArrayList<List<Point2D>>();
		  Element dangers=root.getChild("DangerousZones");
		  List<Element> zones=dangers.getChildren();
		  for(Element e:zones)
		  {
			  List<Point2D> inner=new ArrayList<Point2D>();
			  double lx=Double.valueOf(e.getChildText("LX"));
			  double ly=Double.valueOf(e.getChildText("LY"));
			  double rx=Double.valueOf(e.getChildText("RX"));
			  double ry=Double.valueOf(e.getChildText("RY"));
			  Point2D lpoint=new Point2D.Double(lx, ly);
			  Point2D rPoint=new Point2D.Double(rx, ry);
			  inner.add(lpoint);
			  inner.add(rPoint);
			  dangerousZones.add(inner);
		  }
		  return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Error when loading config file", e);
			return false;
		}
	}
}

