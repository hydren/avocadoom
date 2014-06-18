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
package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.Settings;
import model.EngineInfo;

public class EngineDialog extends JDialog implements ActionListener
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5688506610684190834L;
	
	private JTextField txtEngineName, txtEngineCode, txtExecutablePath, textFieldImagePath;
	private JButton btnButtonnavigate, btnImagepath, btnOk, btnCancel;
	private JLabel lblImagepreview;
	private File file=null;
	
	EngineInfo resultingInfo;

	public EngineDialog(JDialog owner)
	{
		this(owner, null);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public EngineDialog(JDialog owner, EngineInfo engine)
	{
		super(owner, "Engine", true);
		
		setResizable(false);
		setSize(new Dimension(420, 234));
		getContentPane().setLayout(null);
		
		btnOk = new JButton("Ok");
		btnOk.setBounds(208, 168, 81, 26);
		btnOk.addActionListener(this);
		getContentPane().add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(301, 168, 81, 26);
		btnCancel.addActionListener(this);
		getContentPane().add(btnCancel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 10, 60, 16);
		getContentPane().add(lblName);
		
		txtEngineName = new JTextField();
		txtEngineName.setText("Engine name");
		txtEngineName.setBounds(56, 5, 233, 26);
		getContentPane().add(txtEngineName);
		txtEngineName.setColumns(10);
		
		txtEngineCode = new JTextField();
		txtEngineCode.setText("Engine code");
		txtEngineCode.setBounds(56, 39, 233, 26);
		getContentPane().add(txtEngineCode);
		txtEngineCode.setColumns(10);
		
		JLabel lblCode = new JLabel("Code:");
		lblCode.setBounds(12, 44, 60, 16);
		getContentPane().add(lblCode);
		
		JLabel lblExecutablePathcommandLine = new JLabel("Executable path/command line:");
		lblExecutablePathcommandLine.setBounds(12, 77, 240, 16);
		getContentPane().add(lblExecutablePathcommandLine);
		
		txtExecutablePath = new JTextField();
		txtExecutablePath.setText("executable path");
		txtExecutablePath.setBounds(12, 97, 370, 26);
		getContentPane().add(txtExecutablePath);
		txtExecutablePath.setColumns(10);
		
		btnButtonnavigate = new JButton("...");
		btnButtonnavigate.setBounds(386, 97, 26, 26);
		btnButtonnavigate.addActionListener(this);
		getContentPane().add(btnButtonnavigate);
		
		JLabel lblImageoptional = new JLabel("Image (Optional):");
		lblImageoptional.setBounds(12, 135, 124, 16);
		getContentPane().add(lblImageoptional);
		
		textFieldImagePath = new JTextField();
		textFieldImagePath.setBounds(142, 130, 240, 26);
		getContentPane().add(textFieldImagePath);
		textFieldImagePath.setColumns(10);
		
		btnImagepath = new JButton("...");
		btnImagepath.setBounds(386, 130, 26, 26);
		btnImagepath.addActionListener(this);
		getContentPane().add(btnImagepath);
		
		lblImagepreview = new JLabel("image preview");
		lblImagepreview.setBounds(301, 5, 105, 80);
		getContentPane().add(lblImagepreview);
		
		//TODO
		
		if(engine != null) //means that we are editing an engine
		{
			if(engine.iconFileName != null)
			{
				lblImagepreview.setIcon(new ImageIcon(engine.iconFileName));
				lblImagepreview.setText("");
			}
			
			txtEngineCode.setText(engine.code);
			txtEngineName.setText(engine.name);
			txtExecutablePath.setText(engine.executablePath);
			textFieldImagePath.setText(engine.iconFileName);
			file = engine.file;
		}
		
		
		setLocationByPlatform(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		if(event.getSource().equals(btnCancel))
		{
			this.resultingInfo = null;
			this.dispose();
		}
		
		else if(event.getSource().equals(this.btnOk))
		{
			//TODO save all stuff
			this.resultingInfo = new EngineInfo( txtEngineName.getText(), txtEngineCode.getText() , txtExecutablePath.getText(), textFieldImagePath.getText(), (file==null?new File(Settings.ENGINE_CONFIGS_FOLDER+txtEngineCode.getText().toLowerCase()+".properties"):file));
			this.dispose();
		}
		
		else if(event.getSource().equals(this.btnButtonnavigate))
		{
			JFileChooser filechooser = new JFileChooser();
			filechooser.setMultiSelectionEnabled(false);
			
			if( filechooser.showDialog(this, "Choose executable") == JFileChooser.APPROVE_OPTION)
			{
				if(!filechooser.getSelectedFile().canExecute())
				{
					if( JOptionPane.showConfirmDialog(this, "This choosen file seems to not be an executable this application can run, or its not an valid executable.\n" +
							"Are you sure you want to choose this file:\""+filechooser.getSelectedFile().getName()+"\"?"
							, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) 
								return;
				}
				txtExecutablePath.setText(filechooser.getSelectedFile().getPath());	
			}
				
		}
		
		else if(event.getSource().equals(this.btnImagepath))
		{			
			JFileChooser filechooser = new JFileChooser();
			filechooser.setMultiSelectionEnabled(false);
			
			if( filechooser.showDialog(this, "Choose file") == JFileChooser.APPROVE_OPTION)
			{
				if( filechooser.getSelectedFile().getName().lastIndexOf(".") >= 0 )
				{
					String extension = filechooser.getSelectedFile().getName().substring(filechooser.getSelectedFile().getName().lastIndexOf("."));
					if( ! ( extension.equalsIgnoreCase(".JPEG") || extension.equalsIgnoreCase(".JPG")
					 || extension.equalsIgnoreCase(".WBMP")
					 ||	extension.equalsIgnoreCase(".BMP")
					 || extension.equalsIgnoreCase(".GIF") 
					 || extension.equalsIgnoreCase(".PNG")
					))
					if( JOptionPane.showConfirmDialog(this, "This choosen file seems to not be an image file this application can load.\n" +
								"Are you sure you want to choose this file:\""+filechooser.getSelectedFile().getName()+"\"?"
								, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) 
									return;	
				}
				else
				{
					if( JOptionPane.showConfirmDialog(this, "The choosen file has no extension. This application may not be able to load it.\n" +
							"Are you sure you want to choose this file:\""+filechooser.getSelectedFile().getName()+"\"?"
							, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) 
								return;
				}

				textFieldImagePath.setText(filechooser.getSelectedFile().getPath());
				lblImagepreview.setIcon(new ImageIcon(textFieldImagePath.getText()));
				lblImagepreview.setText("");
			}
		}
		
		//TODO maybe refresh everything
	}
	
}
