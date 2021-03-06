package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import exceptions.FileAlreadyExistsException;
import exceptions.InvalidPresetException;

public class Preset 
{
	public File file;
	public String name="<unnamed preset>", description="<no description>", imagePath=null;
	public List<String> engines = new ArrayList<String>();
	public List<Mod> mods = new ArrayList<Mod>();
	
	public Preset(File file) throws FileNotFoundException, IOException, InvalidPresetException
	{
		if(file.isFile())
			this.file = file;
		load();
	}
	
	public Preset(String filename) throws FileNotFoundException, IOException, InvalidPresetException
	{
		this(new File(filename));
	}
	
	private Preset() {}
	
	public static final String
		PRESET_NAME_TAG="name", 
		PRESET_ENGINES_TAG="engines",
		PRESET_DESC_TAG="description",
		PRESET_IMGPATH_TAG="image",
		PRESET_MOD_PATH_TAG="mod.path";
	
	private void load() throws FileNotFoundException, IOException, InvalidPresetException
	{
		FileInputStream fis = new FileInputStream(file);
		Properties p = new Properties();
		
		p.load(fis);
		fis.close();
		if(p.getProperty(PRESET_NAME_TAG)==null)
			throw new InvalidPresetException("File "+file.getName()+" is not a valid preset! Missing name!");
		
		if( p.getProperty(PRESET_ENGINES_TAG)==null)
			throw new InvalidPresetException("File "+file.getName()+" is not a valid preset! Missing engines!");
		
		name = p.getProperty(PRESET_NAME_TAG);
		description = p.getProperty(PRESET_DESC_TAG);
		imagePath = p.getProperty(PRESET_IMGPATH_TAG);
		engines = new ArrayList<String>();
		for(String s : p.getProperty(PRESET_ENGINES_TAG).split(","))
			engines.add(s.trim());
		
		parsePaths();
	}
	
	private void store() throws FileNotFoundException, IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		Properties p = new Properties();
		p.setProperty(PRESET_NAME_TAG, name);
		p.setProperty(PRESET_DESC_TAG, description);
		p.setProperty(PRESET_IMGPATH_TAG, imagePath);

		String engines_codes="";
		for(String eng_code : engines)
			engines_codes += eng_code+",";
		if(engines_codes.length()>0) //if any engines added
			engines_codes = engines_codes.substring(0, engines_codes.length()-1); //remove bogus last comma
		p.setProperty(PRESET_ENGINES_TAG, engines_codes);
		
		p.store(fos, "avocadoom preset file v2");
		fos.close();
		fos=null;
		System.gc();
		storePaths();
	}
	
	private void parsePaths() throws FileNotFoundException
	{
		Scanner scan = new Scanner(file);
		
		mods = new ArrayList<Mod>();
		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			if(line.trim().startsWith(PRESET_MOD_PATH_TAG) && line.contains("=") && line.indexOf('=') != line.length()-1)
			{
				String tmp = line.substring(line.indexOf('=')+1).trim();
				if( !tmp.isEmpty() && Mod.isValidMod(tmp))
					mods.add(new Mod(tmp));
			}
		}
		
		scan.close();
	}
	
	private void storePaths() throws IOException
	{
		BufferedWriter w = new BufferedWriter(new FileWriter(file, true));
		w.write("\n");
		for(Mod mod : mods)
			w.write(PRESET_MOD_PATH_TAG + "=" + mod.file.getPath()+"\n");
		w.close();
		w=null;
		System.gc();
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
	
	public void reload() throws FileNotFoundException, IOException, InvalidPresetException
	{
		load();
	}
	
	public static final 
	void createAndSave(File file, String name, String desc, String imgPath, List<String> engines, List<Mod> modsPaths) throws IOException, FileAlreadyExistsException, Exception
	{
		createAndSave(file, name, desc, imgPath, engines, modsPaths, false);
	}
	
	public static final 
	void createAndSave(File file, String name, String desc, String imgPath, List<String> engines, List<Mod> modsPaths, boolean overwrite) throws IOException, FileAlreadyExistsException, Exception
	{
		if(file.isDirectory()) throw new Exception("A folder with name "+file.getName()+" already exists!");
		if(file.isFile() && ! overwrite) throw new FileAlreadyExistsException("A file with name "+file.getName()+" already exists!");
		
		file.createNewFile();
		
		Preset p = new Preset();
		p.file = file;
		p.description = desc;
		p.engines = engines;
		p.imagePath = imgPath;
		p.mods = modsPaths;
		p.name = name;
		p.store();
	}
	
	public static synchronized final 
	boolean isValidPreset(File file) throws IOException
	{
		if(file == null) return false;
		
		//removed restriction. 
		//TODO need a consensus about file extension...
//		if( file.getName().toLowerCase().endsWith(".txt") == false ) return false;
			
		Scanner scanner1 = new Scanner(file);
		if(scanner1.hasNextLine() == false)
		{
			scanner1.close();
			scanner1=null;
			System.gc();
			return false;
		}
		
		if(scanner1.nextLine().equalsIgnoreCase("#avocadoom preset file v2") == false)
		{
			scanner1.close();
			scanner1=null;
			System.gc();
			return false;
		}
		
		scanner1.close();
		scanner1=null;
		System.gc();
		
		FileInputStream fis = new FileInputStream(file);
		Properties p = new Properties();
		
		p.load(fis);
		fis.close();
		if(p.getProperty(PRESET_NAME_TAG)==null)
			return false;
		
		if( p.getProperty(PRESET_ENGINES_TAG)==null)
			return false;
		
		return true; //TODO add further validation
	}
	
	@Deprecated
	public static
	Preset loadOldFormatPreset(File file) throws FileNotFoundException, Exception
	{
		Preset p = new Preset();
		p.file = file;
		//starts a scanner for the file
		Scanner scanner1 = new Scanner(file);
		
		//if file is not empty, start parsing
		if(scanner1.hasNextLine())
		{	
			//file needs to present this label for validation
			if(scanner1.nextLine().equalsIgnoreCase("[JAGGERY_PRESET]") )
			{
				String str=null;
				
				//needed when a token is accidently already in str variable
				boolean alreadyRead = false;
				
				while(scanner1.hasNextLine())
				{
					//needed when a token is accidently already in str variable
					if( ! alreadyRead ) str = scanner1.nextLine();
					else alreadyRead = false;
					
					//label WADS found. Read a sequence of '@' prefixed paths
					if(str.equalsIgnoreCase("[WADS]"))
					{
						while(scanner1.hasNextLine())
						{
							str = scanner1.nextLine();
							if(str.startsWith("@"))
							{
								p.mods.add(new Mod(str.substring(1)));
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
					if(str.equalsIgnoreCase("[INFO]"))
					{
						while(scanner1.hasNextLine())
						{
							str = scanner1.nextLine();
							
							if(str.trim().startsWith(PRESET_NAME_TAG) && str.contains("="))
							{
								p.name = str.substring(str.indexOf('=')+1).trim();
							}
							else if(str.trim().startsWith(PRESET_DESC_TAG) && str.contains("="))
							{
								p.description = str.substring(str.indexOf('=')+1).trim();
							}
							else if(str.trim().startsWith(PRESET_IMGPATH_TAG) && str.contains("="))
							{
								p.imagePath = str.substring(str.indexOf('=')+1).trim();
							}
							else if(str.trim().startsWith(PRESET_ENGINES_TAG) && str.contains("="))
							{
								String[] parts = str.substring(str.indexOf('=')+1).trim().split(",");
								for(String s : parts) if( ! s.trim().equals("") ) p.engines.add( s.trim() );
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
			else 
			{
				scanner1.close();
				scanner1=null;
				System.gc();
				throw new Exception("Invalid preset file \""+file.getName()+"\"! (at load() method)");	
			}
		}
		else 
		{
			scanner1.close();
			scanner1=null;
			System.gc();
			throw new Exception("Empty preset file \""+file.getName()+"\"! (at load() method)");
		}
		
		return p;
	}
	
	@Deprecated
	public static synchronized final 
	boolean isValidOldPreset(File file) throws FileNotFoundException
	{
		if(file == null) return false;
			
		if( file.getName().toLowerCase().endsWith(".txt") ) //needs toLowerCase because sometimes people save as .TXT
		{
			Scanner scanner1 = new Scanner(file);
			if(scanner1.hasNextLine()) if(scanner1.nextLine().equalsIgnoreCase("[JAGGERY_PRESET]")) 
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
