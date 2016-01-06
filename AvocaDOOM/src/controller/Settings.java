package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import model.EngineInfo;

public class Settings 
{
	//application settings
	
	/** Main window width. */
	public int window_size_x;

	/** Main window width. */
	public int window_size_y;

	/** Last visited folder in file chooser. */
	public String lastVisitedFolder;
	
	/** Flag to indicate whether is to alert if the user is running an preset not set as compatible with the chosen engine. */
	public boolean alertIncompEngine;
	
	/** Flag to indicate whether to show an auxiliary console window with the engine's output. */
	public boolean showAuxConsole;
	
	/** Flag to indicate whether Swing should use OpenGL render or not. Fix some glitches in linux, but may not work on systems with an OpenGL version older than 1.2 */
	public boolean useOGLgui;
	
	/** List of paths to search for presets. */
	public List<String> pathsToSearchForPresets;
	
	/** List of paths to search for mods/wads. */
	public List<String> pathsToSearchForWads;
	
	/** List of registered engines. */
	public List<EngineInfo> customEngines;
	
	/** Position of the main splitpane, from the main window. */
	public int mainsplitpane_pos;
	
	/** Position of the sub/second splitpane, from the main window. */
	public int subsplitpane_pos;
	
	/** Last selected engine in main window's combo box. */
	public String lastSelectedEngine;
	
	/** Look & Feel to use on the app's Swing GUI. */
	public String appLookAndFeel;
	
	/** Icon theme to use. */
	public String iconTheme;
	
	// === file parsing static fields
	
	public static final String 
		SESSION_LASTWINDOWSIZE_X="lastwindowsize_x",
		SESSION_LASTWINDOWSIZE_Y="lastwindowsize_y",
		SESSION_MAINSPLITPANE_POS = "mainsplitpane_pos",
		SESSION_SUBSPLITPANE_POS = "subsplitpane_pos",
		SESSION_ALERTUSERDIFFENGINE="alertuserdiffengine",
		SESSION_USEOGLGUI="useoglgui",
		SESSION_SHOWAUXCONSOLE="showauxconsole",
		SESSION_LASTVISITEDFOLDER="lastvisitedfolder",
		SESSION_LASTSELECTEDCENGINE="lastselectedengine",
		SESSION_LOOKANDFEEL="lookandfeel",
		SESSION_ICONTHEME="icontheme",
		
		SESSION_MAXIMIZED="maximized"; //NOT IMPLEMENTED, TODO
	
	public static File SETTINGS_FILE=new File("settings.properties");
	
	public Settings()
	{
		//first set to default values
		window_size_x = 800;
		window_size_y = 480;
		mainsplitpane_pos = 400;
		subsplitpane_pos = 100;
		lastVisitedFolder="";
		alertIncompEngine = true;
		useOGLgui = false;
		showAuxConsole = !System.getProperty("os.name").equalsIgnoreCase("Windows");
		pathsToSearchForPresets = new ArrayList<String>();
		pathsToSearchForPresets.add(".");
		pathsToSearchForWads = new ArrayList<String>();
		pathsToSearchForWads.add(".");
		customEngines = new ArrayList<EngineInfo>();
		lastSelectedEngine = null;
		appLookAndFeel = "system";
		iconTheme = "doom";
	}
	
	public void load() throws FileNotFoundException, IOException, Exception
	{	
		parseEngines();
		parsePaths();
		
		Collections.sort(customEngines, EngineInfo.COMPARATOR);
		
		FileInputStream fis = new FileInputStream(SETTINGS_FILE);
		Properties p = new Properties();
		p.load(fis);
		fis.close();
		
		String tmp=null;
		if((tmp = p.getProperty(SESSION_LASTWINDOWSIZE_X)) != null) try{
			window_size_x = Integer.parseInt(tmp);
		}catch(NumberFormatException nfe){}
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_LASTWINDOWSIZE_Y)) != null) try{
			window_size_y = Integer.parseInt(tmp);
		}catch(NumberFormatException nfe){}
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_MAINSPLITPANE_POS)) != null) try{
			mainsplitpane_pos = Integer.parseInt(tmp);
		}catch(NumberFormatException nfe){}
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_SUBSPLITPANE_POS)) != null) try{
			subsplitpane_pos = Integer.parseInt(tmp);
		}catch(NumberFormatException nfe){}
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_LASTVISITEDFOLDER)) != null) if(new File(tmp).isFile())
			lastVisitedFolder = tmp;
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_ALERTUSERDIFFENGINE)) != null)
			alertIncompEngine = Boolean.parseBoolean(tmp);
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_USEOGLGUI)) != null)
			useOGLgui = Boolean.parseBoolean(tmp);
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_SHOWAUXCONSOLE)) != null)
			showAuxConsole = Boolean.parseBoolean(tmp);
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_LASTSELECTEDCENGINE)) != null)
			lastSelectedEngine = tmp;
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_LOOKANDFEEL)) != null)
			appLookAndFeel = tmp;
		
		tmp=null;
		if((tmp = p.getProperty(SESSION_ICONTHEME)) != null)
			iconTheme = tmp;
	}
	
	public void store() throws FileNotFoundException, IOException
	{
		Properties p = new Properties();
		p.setProperty(SESSION_LASTWINDOWSIZE_X, window_size_x+"");
		p.setProperty(SESSION_LASTWINDOWSIZE_Y, window_size_y+"");
		p.setProperty(SESSION_MAINSPLITPANE_POS, mainsplitpane_pos+"");
		p.setProperty(SESSION_SUBSPLITPANE_POS, subsplitpane_pos+"");
		p.setProperty(SESSION_LASTVISITEDFOLDER, lastVisitedFolder+"");
		p.setProperty(SESSION_ALERTUSERDIFFENGINE, alertIncompEngine+"");
		p.setProperty(SESSION_USEOGLGUI, useOGLgui+"");
		p.setProperty(SESSION_SHOWAUXCONSOLE, showAuxConsole+"");
		p.setProperty(SESSION_LASTSELECTEDCENGINE, lastSelectedEngine);
		p.setProperty(SESSION_LOOKANDFEEL, appLookAndFeel);
		p.setProperty(SESSION_ICONTHEME, iconTheme);
		FileOutputStream fos = new FileOutputStream(SETTINGS_FILE);
		p.store(fos, "avocadoom settings v2");
		fos.close();
		storePaths();
		storeEngines();
	}
	
	public static final String 
		SESSION_PRESET_PATH="preset.path",
		SESSION_WAD_PATH="wad.path";
	
	private void parsePaths() throws FileNotFoundException
	{
		Scanner scan = new Scanner(SETTINGS_FILE);
		
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			if(line.trim().startsWith(SESSION_PRESET_PATH) && line.contains("=") && line.indexOf('=') != line.length()-1)
			{
				String tmp = line.substring(line.indexOf('=')+1).trim();
				if( !tmp.isEmpty() && new File(tmp).isDirectory() )
					pathsToSearchForPresets.add(tmp);
			}
			else if(line.trim().startsWith(SESSION_WAD_PATH) && line.contains("=") && line.indexOf('=') != line.length()-1)
			{
				String tmp = line.substring(line.indexOf('=')+1).trim();
				if( !tmp.isEmpty() && new File(tmp).isDirectory() )
					pathsToSearchForWads.add(tmp);
			}
		}
		
		scan.close();
	}
	
	private void storePaths() throws IOException
	{
		BufferedWriter w = new BufferedWriter(new FileWriter(SETTINGS_FILE, true));
		w.write("\n");
		for(String path : pathsToSearchForPresets) if(path.equals(".")==false)
			w.write(SESSION_PRESET_PATH + "=" + path+"\n");

		w.write("\n");
		for(String path : pathsToSearchForWads) if(path.equals(".")==false)
			w.write(SESSION_WAD_PATH + "=" + path+"\n");
		
		w.close();
	}
	
	public static final String
		ENGINE_CONFIGS_FOLDER = "configs/",
		ENGINE_ID = "id",
		ENGINE_NAME = "name",
		ENGINE_COMMAND = "command",
		ENGINE_ICON = "icon";
	
	private EngineInfo parseEngine(File file) throws FileNotFoundException, IOException
	{
		FileInputStream fis = new FileInputStream(file);
		Properties p = new Properties();
		p.load(fis);
		fis.close();
		
		if(p.getProperty(ENGINE_ID)==null || p.getProperty(ENGINE_NAME)==null || p.getProperty(ENGINE_COMMAND)==null )
		{
			System.out.println("Ignoring incomplete properties: "+file.getName());
			return null;
		}
		
		return new EngineInfo(p.getProperty(ENGINE_NAME), p.getProperty(ENGINE_ID), p.getProperty(ENGINE_COMMAND), p.getProperty(ENGINE_ICON), file);
	}
	
	private void storeEngine(EngineInfo ei) throws IOException
	{
		Properties p = new Properties();
		p.setProperty(ENGINE_ID, ei.code);
		p.setProperty(ENGINE_NAME, ei.name);
		p.setProperty(ENGINE_COMMAND, ei.executablePath);
		p.setProperty(ENGINE_ICON, ei.iconFileName);
		if(ei.file.exists()==false)
			ei.file.createNewFile();
		else if(ei.file.isDirectory())
		{
			System.out.println("Can't save properties for engine "+ei.code+": a folder with the name "+ei.file.getName()+" already exists!");
			return;
		}
		FileOutputStream fos = new FileOutputStream(ei.file);
		p.store(fos, "avocadoom engine config v2");
		fos.close();
	}
	
	private void parseEngines() throws Exception
	{
		File configPath = new File(ENGINE_CONFIGS_FOLDER);
		if(configPath.isDirectory()==false)
			throw new Exception(ENGINE_CONFIGS_FOLDER+" folder is missing!");
		
		for(File f : configPath.listFiles())
		{
			if(f.isFile() && f.getName().endsWith(".properties"))
				try{
					customEngines.add(parseEngine(f));
				}catch (IOException e) {
					System.out.println("ignoring corrupt file \""+f.getName()+"\": "+e.getLocalizedMessage());
				}
		}
	}
	
	private void storeEngines()
	{
		for(EngineInfo ei : customEngines)
		{
			if(ei.modified) try {
				storeEngine(ei);
			} catch (IOException e) 
			{
				System.out.println("error while saving engine properties for "+ei.code+":"+e.getLocalizedMessage());
			}
		}
	}

}
