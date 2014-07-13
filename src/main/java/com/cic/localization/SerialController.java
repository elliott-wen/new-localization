package com.cic.localization;


import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialController  implements SerialPortEventListener{

private static Logger logger = Logger.getLogger(SerialController.class);
	
	private SerialPort serialPort=null;
	private int lastSequence=-1;
	private OnDataArrvialListener listener=null;
	private DecimalFormat df = new DecimalFormat("#0.00");
	private String truncateDouble(double d)
	{
		return df.format(d);
	}
	public void initCommunication()
	{
			try
			{
				CommPortIdentifier port=CommPortIdentifier.getPortIdentifier(Config.PORTNAME);
	        	serialPort = (SerialPort) port.open("Location", Config.PORTBAUDRATE);
	        	//inputStream=serialPort.getInputStream();
	            serialPort.setSerialPortParams(Config.PORTBAUDRATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	            serialPort.notifyOnDataAvailable(true);
	            serialPort.addEventListener(this);
	            logger.info("Open Serial Successful!");
			}
			catch(Exception e)
			{
				logger.error("Open Communication Failed",e);
				logger.error("Ready to quit");
				System.exit(-1);
			}
	}
	
	/*private void handleDataFromSerialPort(int tagID,Map<Integer,Double> distanceData, double positionFromTagX, double positionFromTagY,double gyroX,double gyroY,double gyroZ,int sequence)
	{
		
		Point2D computerResult=argorithm.calculate(distanceData, gyroZ);
		//logger.debug("Computer Location Result: X="+format(computerResult.getX())+" Y="+format(computerResult.getY()));
		//this.positionChart.updatePlotData(computerResult.getX(), computerResult.getY(), positionFromTagX, positionFromTagY);	
		//logger.debug("Tag Location Result: X="+format(positionFromTagX)+" Y="+format(positionFromTagY));
		//this.distanceChart.updatePlotData(distanceData);
		logger.debug("=========================================");
		String log=""+sequence+" ";
		Iterator<Entry<Integer,Double>> iter=distanceData.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<Integer,Double> entry=iter.next();
			log+=entry.getKey()+","+format(entry.getValue())+",";
			
		}
		log+=format(computerResult.getX())+","+format(computerResult.getY())+","+format(gyroX)+","+format(gyroY)+","+format(gyroZ)+". ";
		logger.info(log);
	}*/
	
  
	public void serialEvent(SerialPortEvent arg0) 
	{
		
			InputStream inputStream=null;
			byte[] readBuffer = new byte[48];
			int numBytesRead=0;
			int tagID=0;
			try 
			{
				inputStream = serialPort.getInputStream();
				if (inputStream.available() >= 48) 
				{
					numBytesRead = inputStream.read(readBuffer);
		        }
				else
				{
					return;
				}
			}
			catch (Exception e) 
			{
				logger.error("Read Bytes From Serial Port Failed",e);
				return;
			}
			finally
			{
				try
				{
					inputStream.close();
				}
				catch(Exception e)
				{
					
				}
			}
			logger.debug("Read Bytes From Serial Port:"+numBytesRead);
			if(numBytesRead!=48)
			{
				logger.error("Data From Serial Port Seems Bad! Give Up");
				return;
			}
			int magicNum=readBuffer[0]& 0x000000FF;
			if(magicNum!=0xa5)
			{
				logger.error("Magic Num Data From Serial Port Seems Bad! Give Up");
				return;
			}
			tagID=readBuffer[1]&0x000000FF;
			int sequence=0;
			sequence=readBuffer[3];
			sequence &= 0xff;
			sequence|=((long)readBuffer[2] << 8);
			//logger.info("Sequence:"+sequence);
			int tempX=0;
			tempX=readBuffer[4];
			tempX &= 0xff;
			tempX |= ((long) readBuffer[5] << 8);
			tempX &= 0xffff;
			tempX |= ((long) readBuffer[6] << 16);
			tempX &= 0xffffff;
			tempX |= ((long) readBuffer[7] << 24);
			double positionFromTagX=Float.intBitsToFloat(tempX);
			int tempY=0;
			tempY=readBuffer[8];
			tempY &= 0xff;
			tempY |= ((long) readBuffer[9] << 8);
			tempY &= 0xffff;
			tempY |= ((long) readBuffer[10] << 16);
			tempY &= 0xffffff;
			tempY |= ((long) readBuffer[11] << 24);
			double positionFromTagY=Float.intBitsToFloat(tempY);
			int tempTemp=0;
			tempTemp=readBuffer[12];
			tempTemp &= 0xff;
			tempTemp |= ((long) readBuffer[13] << 8);
			tempTemp &= 0xffff;
			tempTemp |= ((long) readBuffer[14] << 16);
			tempTemp &= 0xffffff;
			tempTemp |= ((long) readBuffer[15] << 24);
			double temperature=Float.intBitsToFloat(tempTemp);
			int tempVoltage=0;
			tempVoltage=readBuffer[16];
			tempVoltage &= 0xff;
			tempVoltage |= ((long) readBuffer[17] << 8);
			tempVoltage &= 0xffff;
			tempVoltage |= ((long) readBuffer[18] << 16);
			tempVoltage &= 0xffffff;
			tempVoltage |= ((long) readBuffer[19] << 24);
			double voltage=Float.intBitsToFloat(tempVoltage);	
			int tempAngle=0;
			tempAngle=readBuffer[20];
			tempAngle &= 0xff;
			tempAngle |= ((long) readBuffer[21] << 8);
			tempAngle &= 0xffff;
			tempAngle |= ((long) readBuffer[22] << 16);
			tempAngle &= 0xffffff;
			tempAngle |= ((long) readBuffer[23] << 24);
			double angle=Float.intBitsToFloat(tempAngle);
			int tempDistance=0;
			tempDistance=readBuffer[24];
			tempDistance &= 0xff;
			tempDistance |= ((long) readBuffer[25] << 8);
			tempDistance &= 0xffff;
			tempDistance |= ((long) readBuffer[26] << 16);
			tempDistance &= 0xffffff;
			tempDistance |= ((long) readBuffer[27] << 24);
			double distance1=Float.intBitsToFloat(tempDistance);
			
			tempDistance=readBuffer[28];
			tempDistance &= 0xff;
			tempDistance |= ((long) readBuffer[29] << 8);
			tempDistance &= 0xffff;
			tempDistance |= ((long) readBuffer[30] << 16);
			tempDistance &= 0xffffff;
			tempDistance |= ((long) readBuffer[31] << 24);
			double distance2=Float.intBitsToFloat(tempDistance);
			tempDistance=readBuffer[32];
			tempDistance &= 0xff;
			tempDistance |= ((long) readBuffer[33] << 8);
			tempDistance &= 0xffff;
			tempDistance |= ((long) readBuffer[34] << 16);
			tempDistance &= 0xffffff;
			tempDistance |= ((long) readBuffer[35] << 24);
			double distance3=Float.intBitsToFloat(tempDistance);
			tempDistance=readBuffer[36];
			tempDistance &= 0xff;
			tempDistance |= ((long) readBuffer[37] << 8);
			tempDistance &= 0xffff;
			tempDistance |= ((long) readBuffer[38] << 16);
			tempDistance &= 0xffffff;
			tempDistance |= ((long) readBuffer[39] << 24);
			double distance4=Float.intBitsToFloat(tempDistance);
			int tempVelocity=0;
			tempVelocity=readBuffer[40];
			tempVelocity &= 0xff;
			tempVelocity |= ((long) readBuffer[41] << 8);
			tempVelocity &= 0xffff;
			tempVelocity |= ((long) readBuffer[42] << 16);
			tempVelocity &= 0xffffff;
			tempVelocity |= ((long) readBuffer[43] << 24);
			double velocity1=Float.intBitsToFloat(tempVelocity);
		
			tempVelocity=readBuffer[44];
			tempVelocity &= 0xff;
			tempVelocity |= ((long) readBuffer[45] << 8);
			tempVelocity &= 0xffff;
			tempVelocity |= ((long) readBuffer[46] << 16);
			tempVelocity &= 0xffffff;
			tempVelocity |= ((long) readBuffer[47] << 24);
			double velocity2=Float.intBitsToFloat(tempVelocity);
			if(lastSequence!=sequence)
			{
				lastSequence=sequence;
				if(listener!=null)
				{
					StringBuilder builder=new StringBuilder();
					builder.append(sequence);
					builder.append(",");
					builder.append(tagID);
					builder.append(",");
					builder.append(truncateDouble(distance1));
					builder.append(",");
					builder.append(truncateDouble(distance2));
					builder.append(",");
					builder.append(truncateDouble(distance3));
					builder.append(",");
					builder.append(truncateDouble(distance4));
					builder.append(",");
					builder.append(truncateDouble(velocity1));
					builder.append(",");
					builder.append(truncateDouble(velocity2));
					builder.append(",");
					builder.append(truncateDouble(positionFromTagX));
					builder.append(",");
					builder.append(truncateDouble(positionFromTagY));
					builder.append(",");
					builder.append(truncateDouble(temperature));
					builder.append(",");
					builder.append(truncateDouble(voltage));
					builder.append(",");
					builder.append(truncateDouble(angle));
					logger.info(builder.toString());
					//logger.info(sequence+","+tagID+distance1+","+distance2+","+distance3+","+distance4+","+velocity1+","+velocity2+","+tagID+","+positionFromTagX+","+positionFromTagY+","+temperature+","+voltage+","+angle);
					listener.handleSerialData(tagID, positionFromTagX, positionFromTagY, temperature, voltage, angle);
				}
				//this.handleDataFromSerialPort(tagID, distanceMap, positionFromTagX, positionFromTagY,gyroX,gyroY,gyroZ,sequence);
			}
	}

	public OnDataArrvialListener getListener() {
		return listener;
	}

	public void setListener(OnDataArrvialListener listener) {
		this.listener = listener;
	}
	
}
