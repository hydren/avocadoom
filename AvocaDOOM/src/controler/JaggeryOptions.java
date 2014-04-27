/*      
 *      Copyright 2012 Carlos F. M. Faruolo (aka Hydren) E-mail: 5carlosfelipe5@gmail.com
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
package controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

import model.EngineInfo;

public class JaggeryOptions 
{
	
	//############ TAG NAMING ############
	static final String
	JAGGERY_PRESETS_LABEL="[PRESETS_PATHS]",
	
	JAGGERY_WADS_LABEL="[WADS_PATHS]",
	
	JAGGERY_ENGINES_LABEL="[EXTRA_ENGINES]",
	JAGGERY_ENGINE_TAG="engine",
	JAGGERY_ENGINE_NAME_TAG="enginename",
	JAGGERY_ENGINE_EXECPATH_TAG = "command",
	JAGGERY_ENGINE_IMGPATH_TAG = "image",
	
	JAGGERY_SESSION_LABEL="[SESSION]",
	JAGGERY_SESSION_LASTWINDOWSIZE_X="lastwindowsize_x",
	JAGGERY_SESSION_LASTWINDOWSIZE_Y="lastwindowsize_y",
	JAGGERY_SESSION_ALERTUSERDIFFENGINE="alertuserdiffengine",
	JAGGERY_SESSION_USEOSLOOKANDFEEL="useoslf",
	JAGGERY_SESSION_SHOWAUXCONSOLE="showauxconsole",
	JAGGERY_SESSION_LASTVISITEDFOLDER="lastvisitedfolder", //NOT IMPLEMENTED, TODO
	JAGGERY_SESSION_MAXIMIZED="maximized", //NOT IMPLEMENTED, TODO
	
	JAGGERY_OPTIONS_FILE="settings.ini";
	
	static final String[] JAGGERY_INI_TAGS={
		JAGGERY_SESSION_LABEL,
		JAGGERY_PRESETS_LABEL,
		JAGGERY_WADS_LABEL,
		JAGGERY_ENGINES_LABEL
	};
	
	//############ REAL FIELDS ##############
	public int window_size_x;
	public int window_size_y;
	public String lastVisitedFolder;
	public boolean alertIncompEngine, useOSLF, showAuxConsole;
	public Vector<String> pathsToSearchForPresets, pathsToSearchForWads;
	public Vector<EngineInfo> customEngines;
	
	public JaggeryOptions()
	{
		//first set to default values
		window_size_x = 800;
		window_size_y = 480;
		lastVisitedFolder="";
		alertIncompEngine = useOSLF = true;
		pathsToSearchForPresets = new Vector<String>();
		pathsToSearchForPresets.add(".");
		pathsToSearchForWads = new Vector<String>();
		pathsToSearchForWads.add(".");
		customEngines = new Vector<EngineInfo>();
		showAuxConsole = !System.getProperty("os.name").equalsIgnoreCase("Windows");
		
		//then override with file specifications
		if( new File(JAGGERY_OPTIONS_FILE).exists() && new File(JAGGERY_OPTIONS_FILE).isFile() )
		{
			//TODO Parse the file for more info
			try
			{
				String str="";
				boolean isTagInStr=false;
				Scanner scanner = new Scanner(new File(JAGGERY_OPTIONS_FILE));
				while(scanner.hasNextLine() )
				{
					if(!isTagInStr) str = scanner.nextLine();
					else isTagInStr = false;
					
					//check for session info
					if(str.trim().equalsIgnoreCase(JAGGERY_SESSION_LABEL) && scanner.hasNextLine()) inner: do
					{   //entering session info reading mode
						str = scanner.nextLine();
						
						if(str.toLowerCase().startsWith(JAGGERY_SESSION_ALERTUSERDIFFENGINE+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) alertIncompEngine = true;
							else alertIncompEngine = false;
						}
						
						else if(str.toLowerCase().startsWith(JAGGERY_SESSION_USEOSLOOKANDFEEL+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) useOSLF = true;
							else useOSLF = false;
						}
						
						else if(str.toLowerCase().startsWith(JAGGERY_SESSION_SHOWAUXCONSOLE+'='))
						{
							String value = str.substring(str.indexOf('=')+1);
							if( value.trim().equalsIgnoreCase("true") || value.trim().equals("1") ) showAuxConsole = true;
							else showAuxConsole = false;
						}
						
						else if(str.toLowerCase().startsWith(JAGGERY_SESSION_LASTVISITEDFOLDER+'='))
						{
							String path = str.substring(str.indexOf('=')+1).trim();
							if( new File(path).isDirectory() ) lastVisitedFolder = path;
						}
						
						else if(str.toLowerCase().startsWith(JAGGERY_SESSION_LASTWINDOWSIZE_X+'=')) try
						{
							window_size_x = Integer.parseInt( str.substring(str.indexOf('=')+1) );
						} catch(NumberFormatException e1) { JOptionPane.showConfirmDialog(null, "Warning! No valid number specified in \""+JAGGERY_SESSION_LASTWINDOWSIZE_X+"\" \nValue:"+str.substring(str.indexOf('=')+1)); }
						
						else if(str.toLowerCase().startsWith(JAGGERY_SESSION_LASTWINDOWSIZE_Y+'=')) try
						{
							window_size_y = Integer.parseInt( str.substring(str.indexOf('=')+1) );
						} catch(NumberFormatException e1) { JOptionPane.showConfirmDialog(null, "Warning! No valid number specified in \""+JAGGERY_SESSION_LASTWINDOWSIZE_Y+"\" \nValue:"+str.substring(str.indexOf('=')+1)); }
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : JAGGERY_INI_TAGS)
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
					if(str.trim().equalsIgnoreCase(JAGGERY_PRESETS_LABEL) && scanner.hasNextLine()) inner2: do
					{   //entering presets paths reading mode
						str = scanner.nextLine();
						
						if(str.startsWith("@"))
						{
							pathsToSearchForPresets.add(str.substring(1));
						}
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : JAGGERY_INI_TAGS)
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
					if(str.trim().equalsIgnoreCase(JAGGERY_WADS_LABEL) && scanner.hasNextLine()) inner3: do
					{   //entering presets paths reading mode
						str = scanner.nextLine();
						
						if(str.startsWith("@"))
						{
							pathsToSearchForWads.add(str.substring(1));
						}
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : JAGGERY_INI_TAGS)
						{
							if(str.equalsIgnoreCase(s))
							{
								isTagInStr = true;
								break inner3;
							}
						}
						
						//if no valid tag is found, just ignore the line
						
					}while(scanner.hasNextLine()); //check for lines
					
					if(str.trim().equalsIgnoreCase(JAGGERY_ENGINES_LABEL) && scanner.hasNextLine()) inner4: do
					{
						str = scanner.nextLine();
						
						if(str.trim().startsWith(JAGGERY_ENGINE_TAG+' ') && str.contains(":"))
						{
							if( str.trim().length() == JAGGERY_ENGINE_TAG.length()+2 ) continue;
							
							String code = str.trim().substring(str.trim().indexOf(' ')+1, str.trim().lastIndexOf(':'));
							
							if( ! scanner.hasNextLine()) continue;
							str = scanner.nextLine();
							if( ! str.trim().startsWith(JAGGERY_ENGINE_NAME_TAG) && str.contains("=")) continue;
							String name = str.trim().substring(str.trim().indexOf('=')+1);
							
							if( ! scanner.hasNextLine()) continue;
							str = scanner.nextLine();
							if( ! str.trim().startsWith(JAGGERY_ENGINE_EXECPATH_TAG) && str.contains("=")) continue;
							String execpath = str.trim().substring(str.trim().indexOf('=')+1);
							
							if( ! scanner.hasNextLine()) continue;
							str = scanner.nextLine();
							if( ! str.trim().startsWith(JAGGERY_ENGINE_IMGPATH_TAG) && str.contains("=")) continue;
							String img = str.trim().substring(str.trim().indexOf('=')+1);
							if(img.trim().equalsIgnoreCase("null")) img = null;
							
							customEngines.add(new EngineInfo(name, code, execpath, img) );
						}
						
						//if no session info is found, check for main tags and break when it is
						else for(String s : JAGGERY_INI_TAGS)
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
	public JaggeryOptions(JaggeryOptions jo)
	{
		this.window_size_x = jo.window_size_x;
		this.window_size_y = jo.window_size_y;
		this.pathsToSearchForPresets = new Vector<String>(jo.pathsToSearchForPresets);
		this.pathsToSearchForWads = new Vector<String>(jo.pathsToSearchForWads);
	}
	
	public void saveOptionsToFile()
	{
		//TODO save other info
		
		File file = new File(JAGGERY_OPTIONS_FILE);
		
		if(file.exists())
		{
			if(file.isFile())
			{
				if(!file.canWrite())
				{
					JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: application is not allowed to write to file "+JAGGERY_OPTIONS_FILE, "Saving options error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else 
			{
				JOptionPane.showConfirmDialog(null, "A folder with named \""+JAGGERY_OPTIONS_FILE+"\" exists in the same folder!\n" +
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
			JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: application could not create file "+JAGGERY_OPTIONS_FILE+"\n"+e.getLocalizedMessage(), "Saving options error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		FileWriter fw;
		
		try
		{
			fw = new FileWriter(file);
		}
		catch (IOException e) 
		{
			JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: application could not write to file "+JAGGERY_OPTIONS_FILE+"\n"+e.getLocalizedMessage(), "Saving options error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//writing to an text file
		try
		{	
			//writing session options
			fw.write(JAGGERY_SESSION_LABEL+'\n');
			fw.write(JAGGERY_SESSION_ALERTUSERDIFFENGINE+'='+alertIncompEngine+'\n');
			fw.write(JAGGERY_SESSION_USEOSLOOKANDFEEL+'='+useOSLF+'\n');
			fw.write(JAGGERY_SESSION_SHOWAUXCONSOLE+'='+showAuxConsole+'\n');
			fw.write(JAGGERY_SESSION_LASTVISITEDFOLDER+'='+lastVisitedFolder+'\n');
			fw.write(JAGGERY_SESSION_LASTWINDOWSIZE_X+'='+window_size_x+'\n');
			fw.write(JAGGERY_SESSION_LASTWINDOWSIZE_Y+'='+window_size_y+'\n');
			
			//writing wads paths
			fw.write(JAGGERY_WADS_LABEL+'\n');
			for(String str : pathsToSearchForWads) if( !str.equals(".") ) fw.write('@'+str+'\n');
			
			//writing presets paths
			fw.write(JAGGERY_PRESETS_LABEL+'\n');
			for(String str : pathsToSearchForPresets) if( !str.equals(".") ) fw.write('@'+str+'\n');
			
			//writing custom engines info
			fw.write(JAGGERY_ENGINES_LABEL+'\n');
			for(EngineInfo ei : customEngines) 
			{
				fw.write(JAGGERY_ENGINE_TAG + " " +ei.code+':'+'\n');
				fw.write(JAGGERY_ENGINE_NAME_TAG+'='+ei.name+'\n');
				fw.write(JAGGERY_ENGINE_EXECPATH_TAG+'='+ei.executablePath+'\n');
				fw.write(JAGGERY_ENGINE_IMGPATH_TAG+'='+(ei.iconFileName==null?"null":ei.iconFileName)+'\n'+'\n');
			}
			
			fw.close();
		}
		catch(IOException e)
		{
			JOptionPane.showConfirmDialog(null, "Could not save options to file! \nError: I/O error while writing to file "+JAGGERY_OPTIONS_FILE+"\n"+e.getLocalizedMessage(), "Saving options error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//end writing text file
	}
	
}

