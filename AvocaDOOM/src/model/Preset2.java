package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import exceptions.FileAlreadyExistsException;
import exceptions.InvalidPresetException;

public class Preset2 
{
	public File file;
	public String name, description, imagePath;
	public List<String> engines;
	public List<Mod> mods;
	
	public Preset2(File file) throws FileNotFoundException, IOException, InvalidPresetException
	{
		if(file.isFile())
			this.file = file;
		load();
	}
	
	public static final String
		PRESET_NAME_TAG="name", 
		PRESET_ENGINES_TAG="engines",
		PRESET_DESC_TAG="description",
		PRESET_IMGPATH_TAG="image";
	
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
		
		//TODO load mods (see as was done with loading preset paths)
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
	
	//TODO
	public static final void createAndSave(File file, String name, String desc, String imgPath, List<String> engines, List<Mod> modsPaths, boolean overwrite) throws IOException, FileAlreadyExistsException, Exception
	{}
	
	//TODO
	//convert old presets to new format
	
	
}
