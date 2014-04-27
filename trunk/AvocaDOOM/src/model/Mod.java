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
import java.util.Vector;

import controler.Settings;

public class Mod 
{
	public File file;
	
	public Mod(File file)
	{
		this.file = file;
	}
	
	public Mod(String filename) throws NullPointerException
	{
		this.file = new File(filename);
	}
	
	@Override
	public String toString()
	{
		return file.getName()+"  -  "+file.getAbsolutePath();
	}
	
	public static Vector<Mod> getAllAvailableMods(Settings options)
	{
		Vector<Mod> filesToShow = new Vector<Mod>();
		for(String str : options.pathsToSearchForWads)
		{
			//System.out.println("analysing "+str+"...");
			if(new File(str).isDirectory())
			{
				//System.out.println(str+" is directory! Checking for files inside...");
				for(File f : new File(str).listFiles())
				{
					//System.out.println("  analysing "+f.getName()+"...");
					if( Mod.isValidMod(f) )
					{
						//System.out.println("  "+f.getName()+" is file with valid extension! Adding...");
						filesToShow.add(new Mod(f));
					}
				}
			}
		}
		return filesToShow;
	}
	
	public static synchronized final boolean isValidMod(File file)
	{
		if( file.isFile() && ( 
				file.getName().toLowerCase().endsWith(".wad") //needs toLowerCase because sometimes people use .WAD instead of .wad 
			||  file.getName().toLowerCase().endsWith(".pk3") 
			||  file.getName().toLowerCase().endsWith(".pk7") 
		)) return true; 
		else return false;
	}

}
