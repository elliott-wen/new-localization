package com.cic.localization;

import java.io.File;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;



/**
 * Hello world!
 *
 */
public class App 
{
	private static Logger logger = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	loadConfig(args);
    	Locater locater=new Locater();
    	//locater.initLocater();
    	MainUI mainInst=new MainUI();
    	locater.setLocationEventListener(mainInst);
    	LoginUI inst = new LoginUI();
    	inst.setMainUI(mainInst);
    	inst.setVisible(true);
    	

    }
    
    private static void loadConfig(String []args)
	{
		if(args.length==0)
        {
        	logger.info("No config file specified, use default config file");
        	Config.loadConfig(new File("src/main/resources/config.xml"));
        }
        else
        {
        	File configFile=new File(args[0]);
        	if(configFile.exists())
        	{
        		if(Config.loadConfig(configFile))
        		{
        			logger.info("External Config File Loaded.");
        		}
        		else
        		{
        			logger.error("Bad config file! Please Check the configuration");
        			System.exit(-1);
        		}
        	}
        	else
        	{
        		logger.error("Config file don't exist! Please Check the configuration");
        		System.exit(-1);
        	}
        }
	}
}
