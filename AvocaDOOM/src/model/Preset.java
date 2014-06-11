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
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exceptions.FileAlreadyExistsException;

public class Preset 
{
	private static final String 
	PRESET_VALIDATION_LABEL="[JAGGERY_PRESET]",
	PRESET_WADS_LABEL="[WADS]",
	PRESET_INFO_LABEL="[INFO]",
	PRESET_NAME_TAG="name", 
	PRESET_DESC_TAG="description",
	PRESET_IMGPATH_TAG="image",
	PRESET_ENGINES_TAG="engines";
	
	public File file;
	public String name, description, imagePath;
	public List<String> engines;
	public List<Mod> mods;
	
	public Preset(File file) throws FileNotFoundException, Exception
	{
		if( ! isValidPreset(file) ) throw new Exception("File is not a valid preset!");
		else this.file = file;
		load();
	}
	
	public Preset(String filename) throws FileNotFoundException, Exception
	{
		this(new File(filename));
	}
	
	private void load() throws FileNotFoundException, Exception
	{
		//starts a scanner for the file
		Scanner scanner1 = new Scanner(file);
		
		//if file is not empty, start parsing
		if(scanner1.hasNextLine())
		{	
			//file needs to present this label for validation
			if(scanner1.nextLine().equalsIgnoreCase(PRESET_VALIDATION_LABEL) )
			{
				//initialize to default values
				this.name="<unnamed preset>";
				this.description="<no description>";
				this.imagePath = null;
				this.mods = new ArrayList<Mod>();
				this.engines = new ArrayList<String>();
				String str=null;
				
				//needed when a token is accidently already in str variable
				boolean alreadyRead = false;
				
				while(scanner1.hasNextLine())
				{
					//needed when a token is accidently already in str variable
					if( ! alreadyRead ) str = scanner1.nextLine();
					else alreadyRead = false;
					
					//label WADS found. Read a sequence of '@' prefixed paths
					if(str.equalsIgnoreCase(PRESET_WADS_LABEL))
					{
						while(scanner1.hasNextLine())
						{
							str = scanner1.nextLine();
							if(str.startsWith("@"))
							{
								mods.add(new Mod(str.substring(1)));
							}
							// it is another label found, need to set the alreadyRead flag because the label is already on the str variable
							else if( str.startsWith("[") ) 
							{
								alreadyRead = true;
								break;
							}
							else
							{
								//should I throw an exception???
							}
						}
					}
					
					//label INFO found, read simple data
					if(str.equalsIgnoreCase(PRESET_INFO_LABEL))
					{
						while(scanner1.hasNextLine())
						{
							str = scanner1.nextLine();
							
							if(str.trim().startsWith(PRESET_NAME_TAG) && str.contains("="))
							{
								this.name = str.substring(str.indexOf('=')+1).trim();
							}
							else if(str.trim().startsWith(PRESET_DESC_TAG) && str.contains("="))
							{
								this.description = str.substring(str.indexOf('=')+1).trim();
							}
							else if(str.trim().startsWith(PRESET_IMGPATH_TAG) && str.contains("="))
							{
								this.imagePath = str.substring(str.indexOf('=')+1).trim();
							}
							else if(str.trim().startsWith(PRESET_ENGINES_TAG) && str.contains("="))
							{
								String[] parts = str.substring(str.indexOf('=')+1).trim().split(",");
								for(String s : parts) if( ! s.trim().equals("") ) this.engines.add( s.trim() );
							}
							// it is another label found, need to set the alreadyRead flag because the label is already on the str variable
							else if( str.startsWith("[") ) 
							{
								alreadyRead = true;
								break;
							}
							else
							{
								//should I throw an exception???
							}
						}
					}
				}
				scanner1.close();
				scanner1=null;
				System.gc();
			}
			else throw new Exception("Invalid preset file \""+file.getName()+"\"! (at load() method)");	
		}
		else throw new Exception("Empty preset file \""+file.getName()+"\"! (at load() method)");
	}
	
	public void reload() throws FileNotFoundException, Exception
	{
		load();
	}
	
	public String getStatusText()
	{
		return file.getName() + ", " + mods.size() + " files included";
	}
	
	@Override
	public String toString()
	{
		return name+"  -  "+file.getAbsolutePath();
	}
	
	public static final void createAndSave(File file, String name, String desc, String imgPath, List<String> engines, List<Mod> modsPaths) throws IOException, FileAlreadyExistsException, Exception
	{
		createAndSave(file, name, desc, imgPath, engines, modsPaths, false);
	}
	
	public static final void createAndSave(File file, String name, String desc, String imgPath, List<String> engines, List<Mod> modsPaths, boolean overwrite) throws IOException, FileAlreadyExistsException, Exception
	{
		if(file.isDirectory()) throw new Exception("A folder with name "+file.getName()+" already exists!");
		if(file.isFile() && ! overwrite) throw new FileAlreadyExistsException("A file with name "+file.getName()+" already exists!");
		
		file.createNewFile();
		
		FileWriter fw = new FileWriter(file);

		fw.write(PRESET_VALIDATION_LABEL+'\n');
		
		fw.write(PRESET_INFO_LABEL+'\n');
		fw.write(PRESET_NAME_TAG+'='+name+'\n');
		fw.write(PRESET_DESC_TAG+'='+desc+'\n');
		fw.write(PRESET_IMGPATH_TAG+'='+imgPath+'\n');
		fw.write(PRESET_ENGINES_TAG+'=');
		for(String str : engines) fw.write(str+",");
		fw.write('\n');
		
		fw.write(PRESET_WADS_LABEL+'\n');
		for(Mod m : modsPaths) fw.write('@'+m.file.getPath()+'\n');

		fw.close();
		fw=null;
		System.gc();
	}
	
	public static synchronized final boolean isValidPreset(File file) throws FileNotFoundException
	{
		
		if(file == null) return false;
			
		if( file.getName().toLowerCase().endsWith(".txt") ) //needs toLowerCase because sometimes people save as .TXT
		{
			Scanner scanner1 = new Scanner(file);
			if(scanner1.hasNextLine()) if(scanner1.nextLine().equalsIgnoreCase(PRESET_VALIDATION_LABEL)) 
			{
				scanner1.close();
				scanner1=null;
				System.gc();
				return true;
			}
			scanner1.close();
			scanner1=null;
			System.gc();
		}
		return false;
	}
	
}
