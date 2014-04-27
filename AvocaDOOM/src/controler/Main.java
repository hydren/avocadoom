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

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import view.MainWindow;


public class Main {
	
	public static final String APP_VERSION="0.9.3.1 Beta",
			APP_ABOUT_TEXT="AvocaDOOM - a simple manager for Doom engines.\n",
			APP_LICENCE =   "Copyright (C) 2012 Carlos F. M. Faruolo (aka Hydren)\nE-mail: 5carlosfelipe5@gmail.com\n\n" +
							"Doom graphics (C) Id Software - Buttons \n" +
							"Buttons graphics edit - Joseph Hicks \n"+
							"This program is free software: you can redistribute it and/or modify\n" +
							"it under the terms of the GNU General Public License as published by\n" +
							"the Free Software Foundation, either version 3 of the License, or\n" +
							"(at your option) any later version.\n\nThis program is distributed in the hope that it will be useful,\n" +
							"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
							"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
							"GNU General Public License for more details.\n\n" +
							"You should have received a copy of the GNU General Public License\n" +
							"along with this program.  If not, see <http://www.gnu.org/licenses/>.";
	
	public static final boolean isDebugBuild=false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		JaggeryOptions jo = new JaggeryOptions();
		if(jo.useOSLF) try //TO FORCE TO USE THE SYSTEM LOOK & FEEL 
		{
			if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
			{
				for(LookAndFeelInfo lfi : UIManager.getInstalledLookAndFeels())
				{
					if(lfi.getName().equalsIgnoreCase("GTK+"))
					{
						UIManager.setLookAndFeel(lfi.getClassName());
						break;
					}
				}
			}
			else UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Could not change application look and feel. Using a cross-platform one...");
		}
		new MainWindow( jo );
	}
}
