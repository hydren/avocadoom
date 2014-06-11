/*      
 *      Copyright 2014 Carlos F. M. Faruolo (aka Hydren) E-mail: 5carlosfelipe5@gmail.com
 *      
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 *      
 *      
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.EngineInfo;

public class Settings 
{
	
	//############ TAG NAMING ############
	static final String
	PRESETS_LABEL="[PRESETS_PATHS]",
	
	WADS_LABEL="[WADS_PATHS]",
	
	ENGINES_LABEL="[EXTRA_ENGINES]",
	ENGINE_TAG="engine",
	ENGINE_NAME_TAG="enginename",
	ENGINE_EXECPATH_TAG = "command",
	ENGINE_IMGPATH_TAG = "image",
	
	SESSION_LABEL="[SESSION]",
	SESSION_LASTWINDOWSIZE_X="lastwindowsize_x",
	SESSION_LASTWINDOWSIZE_Y="lastwindowsize_y",
	SESSION_MAINSPLITPANE_POS = "mainsplitpane_pos",
	SESSION_SUBSPLITPANE_POS = "subsplitpane_pos",
	SESSION_ALERTUSERDIFFENGINE="alertuserdiffengine",
	SESSION_USEOSLOOKANDFEEL="useoslf",
	SESSION_USEOGLGUI="useoglgui",
	SESSION_SHOWAUXCONSOLE="showauxconsole",
	SESSION_LASTVISITEDFOLDER="lastvisitedfolder", //NOT IMPLEMENTED, TODO
	SESSION_MAXIMIZED="maximized", //NOT IMPLEMENTED, TODO
	
	OPTIONS_FILE="settings.ini";
	
	static final String[] INI_TAGS={
		SESSION_LABEL,
		PRESETS_LABEL,
		WADS_LABEL,
		ENGINES_LABEL
	};
	
	//############ REAL FIELDS ##############
	public int window_size_x;
	public int window_size_y;
	public String lastVisitedFolder;
	public boolean alertIncompEngine, useOSLF, showAuxConsole, useOGLgui;
	public List<String> pathsToSearchForPresets, pathsToSearchForWads;
	public List<EngineInfo> customEngines;
	public int mainsplitpane_pos, subsplitpane_pos;
	
	public Settings()
	{
		//first set to default values
		window_size_x = 800;
		window_size_y = 480;
		mainsplitpane_pos = 400;
		subsplitpane_pos = 100;
		lastVisitedFolder="";
		alertIncompEngine = useOSLF = true;
		useOGLgui = false;
		pathsToSearchForPresets = new ArrayList<String>();
		pathsToSearchForPresets.add(".");
		pathsToSearchForWads = new ArrayList<String>();
		pathsToSearchForWads.add(".");
		customEngines = new ArrayList<EngineInfo>();
		showAuxConsole = !System.getProperty("os.name").equalsIgnoreCase("Windows");
		
		//then override with file specifications
		if( new File(OPTIONS_FILE).exists() && new File(OPTIONS_FILE).isFile() )
		{
			//TODO Parse the file for more info
			try
			{
				String str="";
				boolean isTagInStr=false;
				Scanner scanner = new Scanner(new File(OPTIONS_FILE));
				while(scanner.hasNextLine() )
				{
					if(!isTagInStr) str = scanner.nextLine();
					else isTagInStr = false;
					
					//check for session info
					if(str.trim().equalsIgnoreCase(SESSION_LABEL) && scanner.hasNextLine()) inner: do
					{   //entering session info reading mode
						str = scanner.nextLine();
						
						if(str.toLowerCase().startsWith(SESSION_ALERTUSERDIFFENGINE+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) alertIncompEngine = true;
							else alertIncompEngine = false;
						}
						
						else if(str.toLowerCase().startsWith(SESSION_USEOSLOOKANDFEEL+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) useOSLF = true;
							else useOSLF = false;
						}

						else if(str.toLowerCase().startsWith(SESSION_USEOGLGUI+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) useOGLgui = true;
							else useOGLgui = false;
						}
						
						else if(str.toLowerCase().startsWith(SESSION_SHOWAUXCONSOLE+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) showAuxConsole = true;
							else showAuxConsole = false;
						}
						
						else if(str.toLowerCase().startsWith(SESSION_LASTVISITEDFOLDER+'='))
						{
							String path = str.substring(str.indexOf('=')+1).trim();
							if( new File(path).isDirectory() ) lastVisitedFolder = path;
						}
						
						else if(str.toLowerCase().startsWith(SESSION_LASTWINDOWSIZE_X+'=')) try
						{
							window_size_x = Integer.parseInt( str.substring(str.indexOf('=')+1) );
						} catch(NumberFormatException e1) { JOptionPane.showConfirmDialog(null, "Warning! No valid number specified in \""+SESSION_LASTWINDOWSIZE_X+"\" \nValue:"+str.substring(str.indexOf('=')+1)); }
						
						else if(str.toLowerCase().startsWith(SESSION_LASTWINDOWSIZE_Y+'=')) try
						{
							window_size_y = Integer.parseInt( str.substring(str.indexOf('=')+1) );
						} catch(NumberFormatException e1) { JOptionPane.showConfirmDialog(null, "Warning! No valid number specified in \""+SESSION_LASTWINDOWSIZE_Y+"\" \nValue:"+str.substring(str.indexOf('=')+1)); }
						
						else if(str.toLowerCase().startsWith(SESSION_MAINSPLITPANE_POS+'=')) try
						{
							mainsplitpane_pos = Integer.parseInt( str.substring(str.indexOf('=')+1) );
						} catch(NumberFormatException e1) { JOptionPane.showConfirmDialog(null, "Warning! No valid number specified in \""+SESSION_MAINSPLITPANE_POS+"\" \nValue:"+str.substring(str.indexOf('=')+1)); }
						
						else if(str.toLowerCase().startsWith(SESSION_SUBSPLITPANE_POS+'=')) try
						{
							subsplitpane_pos = Integer.parseInt( str.substring(str.indexOf('=')+1) );
						} catch(NumberFormatException e1) { JOptionPane.showConfirmDialog(null, "Warning! No valid number specified in \""+SESSION_SUBSPLITPANE_POS+"\" \nValue:"+str.substring(str.indexOf('=')+1)); }
						
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : INI_TAGS)
						{
							if(str.equalsIgnoreCase(s))
							{
								isTagInStr = true;
								break inner;
							}
						}
						
						//if no valid tag is found, just ignore the line
						
					}while(scanner.hasNextLine()); //check for lines
					
					//check for presets paths
					if(str.trim().equalsIgnoreCase(PRESETS_LABEL) && scanner.hasNextLine()) inner2: do
					{   //entering presets paths reading mode
						str = scanner.nextLine();
						
						if(str.startsWith("@"))
						{
							pathsToSearchForPresets.add(str.substring(1));
						}
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : INI_TAGS)
						{
							if(str.equalsIgnoreCase(s))
							{
								isTagInStr = true;
								break inner2;
							}
						}
						
						//if no valid tag is found, just ignore the line
						
					}while(scanner.hasNextLine()); //check for lines
					
					//check for wads paths
					if(str.trim().equalsIgnoreCase(WADS_LABEL) && scanner.hasNextLine()) inner3: do
					{   //entering presets paths reading mode
						str = scanner.nextLine();
						
						if(str.startsWith("@"))
						{
							pathsToSearchForWads.add(str.substring(1));
						}
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : INI_TAGS)
						{
							if(str.equalsIgnoreCase(s))
							{
								isTagInStr = true;
								break inner3;
							}
						}
						
						//if no valid tag is found, just ignore the line
						
					}while(scanner.hasNextLine()); //check for lines
					
					if(str.trim().equalsIgnoreCase(ENGINES_LABEL) && scanner.hasNextLine()) inner4: do
					{
						str = scanner.nextLine();
						
						if(str.trim().startsWith(ENGINE_TAG+' ') && str.contains(":"))
						{
							if( str.trim().length() == ENGINE_TAG.length()+2 ) continue;
							
							String code = str.trim().substring(str.trim().indexOf(' ')+1, str.trim().lastIndexOf(':'));
							
							if( ! scanner.hasNextLine()) continue;
							str = scanner.nextLine();
							if( ! str.trim().startsWith(ENGINE_NAME_TAG) && str.contains("=")) continue;
							String name = str.trim().substring(str.trim().indexOf('=')+1);
							
							if( ! scanner.hasNextLine()) continue;
							str = scanner.nextLine();
							if( ! str.trim().startsWith(ENGINE_EXECPATH_TAG) && str.contains("=")) continue;
							String execpath = str.trim().substring(str.trim().indexOf('=')+1);
							
							if( ! scanner.hasNextLine()) continue;
							str = scanner.nextLine();
							if( ! str.trim().startsWith(ENGINE_IMGPATH_TAG) && str.contains("=")) continue;
							String img = str.trim().substring(str.trim().indexOf('=')+1);
							if(img.trim().equalsIgnoreCase("null")) img = null;
							
							customEngines.add(new EngineInfo(name, code, execpath, img) );
						}
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : INI_TAGS)
						{
							if(str.equalsIgnoreCase(s))
							{
								isTagInStr = true;
								break inner4;
							}
						}
						
						//if no valid tag is found, just ignore the line
						
					}while(scanner.hasNextLine()); //check for lines
				}
			}
			catch(FileNotFoundException e) {JOptionPane.showConfirmDialog(null, "FILE PASSES EXISTENCE TEST AND CANT BE FOUND!\n THIS IS NOT SUPPOSED TO HAPPEN!", "ERROR!", JOptionPane.ERROR_MESSAGE);}
		}
	
			
	}
	
	//Copy constructor
	public Settings(Settings jo)
	{
		this.window_size_x = jo.window_size_x;
		this.window_size_y = jo.window_size_y;
		this.pathsToSearchForPresets = new ArrayList<String>(jo.pathsToSearchForPresets);
		this.pathsToSearchForWads = new ArrayList<String>(jo.pathsToSearchForWads);
	}
	
	public void saveOptionsToFile()
	{
		//TODO save other info
		
		File file = new File(OPTIONS_FILE);
		
		if(file.exists())
		{
			if(file.isFile())
			{
				if(!file.canWrite())
				{
					JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: application is not allowed to write to file "+OPTIONS_FILE, "Saving options error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else 
			{
				JOptionPane.showConfirmDialog(null, "A folder with named \""+OPTIONS_FILE+"\" exists in the same folder!\n" +
						" Can't save options to file! Delete or rename the folder to save!", "Naming error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		else try
		{
			file.createNewFile();
		}
		catch (IOException e) 
		{
			JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: application could not create file "+OPTIONS_FILE+"\n"+e.getLocalizedMessage(), "Saving options error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		FileWriter fw;
		
		try
		{
			fw = new FileWriter(file);
		}
		catch (IOException e) 
		{
			JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: application could not write to file "+OPTIONS_FILE+"\n"+e.getLocalizedMessage(), "Saving options error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//writing to an text file
		try
		{	
			//writing session options
			fw.write(SESSION_LABEL+'\n');
			fw.write(SESSION_ALERTUSERDIFFENGINE+'='+alertIncompEngine+'\n');
			fw.write(SESSION_USEOSLOOKANDFEEL+'='+useOSLF+'\n');
			fw.write(SESSION_USEOGLGUI+'='+useOGLgui+'\n');
			fw.write(SESSION_SHOWAUXCONSOLE+'='+showAuxConsole+'\n');
			fw.write(SESSION_LASTVISITEDFOLDER+'='+lastVisitedFolder+'\n');
			fw.write(SESSION_LASTWINDOWSIZE_X+'='+window_size_x+'\n');
			fw.write(SESSION_LASTWINDOWSIZE_Y+'='+window_size_y+'\n');
			fw.write(SESSION_MAINSPLITPANE_POS+'='+mainsplitpane_pos+'\n');
			fw.write(SESSION_SUBSPLITPANE_POS+'='+subsplitpane_pos+'\n');
			
			//writing wads paths
			fw.write('\n'+WADS_LABEL+'\n');
			for(String str : pathsToSearchForWads) if( !str.equals(".") ) fw.write('@'+str+'\n');
			
			//writing presets paths
			fw.write('\n'+PRESETS_LABEL+'\n');
			for(String str : pathsToSearchForPresets) if( !str.equals(".") ) fw.write('@'+str+'\n');
			
			//writing custom engines info
			fw.write('\n'+ENGINES_LABEL+'\n');
			for(EngineInfo ei : customEngines) 
			{
				fw.write(ENGINE_TAG + " " +ei.code+':'+'\n');
				fw.write(ENGINE_NAME_TAG+'='+ei.name+'\n');
				fw.write(ENGINE_EXECPATH_TAG+'='+ei.executablePath+'\n');
				fw.write(ENGINE_IMGPATH_TAG+'='+(ei.iconFileName==null?"null":ei.iconFileName)+'\n'+'\n');
			}
			
			fw.close();
		}
		catch(IOException e)
		{
			JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: I/O error while writing to file "+OPTIONS_FILE+"\n"+e.getLocalizedMessage(), "Saving options error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//end writing text file
	}
	
}

