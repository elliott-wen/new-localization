package com.cic.localization;


import java.io.InputStream;
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
			byte[] readBuffer = new byte[44];
			int numBytesRead=0;
			int tagID=0;
			try 
			{
				inputStream = serialPort.getInputStream();
				
				if (inputStream.available() >= 44) 
				{
					numBytesRead = inputStream.read(readBuffer);
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
			if(numBytesRead!=44)
			{
				logger.error("Data From Serial Port Seems Bad! Give Up");
				return;
			}
			tagID=readBuffer[1]&0XFF;
			Map<Integer,Double> distanceMap=new HashMap<Integer,Double>();
			for(int t=0;t<4;t++)
			{
					int n=t*5+2;
					int anchorID=readBuffer[n]&0XFF;
					anchorID=anchorID%112;
					int distance=0;
		            distance = readBuffer[n + 4];
		            distance &= 0xff;
		            distance |= ((long) readBuffer[n + 3] << 8);
		            distance &= 0xffff;
		            distance |= ((long) readBuffer[n + 2] << 16);
		            distance &= 0xffffff;
		            distance |= ((long) readBuffer[n + 1] << 24);
		            double distanceD=Float.intBitsToFloat(distance);
		            logger.debug("Tag="+tagID+":Distance between anchor "+anchorID+" :"+(distanceD));       
		            distanceMap.put(anchorID, distanceD);
			}
			int tempX=0;
			tempX=readBuffer[25];
			tempX &= 0xff;
			tempX |= ((long) readBuffer[24] << 8);
			tempX &= 0xffff;
			tempX |= ((long) readBuffer[23] << 16);
			tempX &= 0xffffff;
			tempX |= ((long) readBuffer[22] << 24);
			double positionFromTagX=Float.intBitsToFloat(tempX);
			int tempY=0;
			tempY=readBuffer[29];
			tempY &= 0xff;
			tempY |= ((long) readBuffer[28] << 8);
			tempY &= 0xffff;
			tempY |= ((long) readBuffer[27] << 16);
			tempY &= 0xffffff;
			tempY |= ((long) readBuffer[26] << 24);
			double positionFromTagY=Float.intBitsToFloat(tempY);
			int gyro=0;
			gyro=readBuffer[33];
			gyro &= 0xff;
			gyro |= ((long) readBuffer[32] << 8);
			gyro &= 0xffff;
			gyro |= ((long) readBuffer[31] << 16);
			gyro &= 0xffffff;
			gyro |= ((long) readBuffer[30] << 24);
			double gyroX=Float.intBitsToFloat(gyro);
			gyro=0;
			gyro=readBuffer[37];
			gyro &= 0xff;
			gyro |= ((long) readBuffer[36] << 8);
			gyro &= 0xffff;
			gyro |= ((long) readBuffer[35] << 16);
			gyro &= 0xffffff;
			gyro |= ((long) readBuffer[34] << 24);
			double gyroY=Float.intBitsToFloat(gyro);
			gyro=0;
			gyro=readBuffer[41];
			gyro &= 0xff;
			gyro |= ((long) readBuffer[40] << 8);
			gyro &= 0xffff;
			gyro |= ((long) readBuffer[39] << 16);
			gyro &= 0xffffff;
			gyro |= ((long) readBuffer[38] << 24);
			double gyroZ=Float.intBitsToFloat(gyro);
			int sequence=0;
			sequence=readBuffer[43];
			sequence &= 0xff;
			sequence|=((long)readBuffer[42] << 8);
			if(lastSequence!=sequence)
			{
				lastSequence=sequence;
				if(listener!=null)
				{
					double gyroData[]={gyroX,gyroY,gyroZ};
					listener.handleSerialData(tagID, distanceMap, gyroData);
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
