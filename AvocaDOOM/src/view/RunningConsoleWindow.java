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
package view;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RunningConsoleWindow implements Runnable
{
	JFrame outputWindow;
	JTextArea output;
	JScrollPane scrollingArea;
	String cmd_txt;
	Process proc;

	public RunningConsoleWindow(Process proc, String command_txt)
	{
		outputWindow = new JFrame("Executing " + command_txt + "...");
		output = new JTextArea("");
		output.setEditable(false);
		scrollingArea = new JScrollPane(output);
		scrollingArea.setPreferredSize(new Dimension(250, 250));
		outputWindow.add(scrollingArea);
		outputWindow.setSize(400, 300);
		outputWindow.setLocationByPlatform(true);
		outputWindow.setVisible(true);
		outputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cmd_txt = command_txt;
		this.proc = proc;
	}

	@Override
	public void run() {
		BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line=null;
		try {
			output.append(cmd_txt+"\n\n");
			while((line=input.readLine()) != null) 
			{ 
				output.append(line+"\n");
				//System.out.println(line);
				output.setCaretPosition(output.getDocument().getLength());
			}
			output.append("Executable exited with error code "+proc.waitFor());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

