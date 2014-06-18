/*      
 *      Copyright 2012-2014 Carlos F. M. Faruolo (aka Hydren) E-mail: 5carlosfelipe5@gmail.com
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

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import util.UIManager2;
import view.MainWindow;


public class Main {
	
	public static final String APP_VERSION="0.9.6 Beta",
			APP_ABOUT_TEXT="AvocaDOOM - a simple manager for Doom engines.\n",
			APP_LICENCE =   "Copyright (C) 2014 Carlos F. M. Faruolo (aka Hydren)\nE-mail: 5carlosfelipe5@gmail.com\n\n" +
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
		Settings settings = new Settings();
		try 
		{
			settings.load();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "No settings file found. Use default settings.", "Oops!", JOptionPane.WARNING_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Error while loading settings: "+e1.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getLocalizedMessage(), "Fatal error!", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		if(settings.useOGLgui)
			System.setProperty("sun.java2d.opengl","true"); //fix some visual artifacts
		
		try
		{
			UIManager2.setLookAndFeelByName(settings.appLookAndFeel);
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Could not change application look and feel. Using a cross-platform one...");
		}

		new MainWindow( settings );
	}
}
