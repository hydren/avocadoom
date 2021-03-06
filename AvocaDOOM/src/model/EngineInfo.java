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
import java.util.Comparator;

public class EngineInfo 
{
	public String name, code, executablePath, iconFileName;
	public boolean modified=false;
	public File file;
	
	public static final
	Comparator<EngineInfo> COMPARATOR = new Comparator<EngineInfo>() 
	{
		@Override
		public int compare(EngineInfo o1, EngineInfo o2) {
			return o1.code.compareTo(o2.code);
		}
	};

	public EngineInfo(String name, String code, String executablePath, String iconFileName, File file) 
	{
		this.name = name;
		this.code = code;
		this.executablePath = executablePath;
		this.iconFileName = iconFileName;
		this.file = file;
	}
	
	@Override
	public String toString()
	{
		return this.code + " - "+this.name + " - " + this.executablePath;
	}

}
