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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.EngineInfo;
import model.Mod;
import model.Preset;
import controller.Settings;
import exceptions.FileAlreadyExistsException;

public class PresetDialog extends JDialog implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4233144360464878838L;
	private JList listAvailable;
	private JList listIncluded;
	private JButton buttonInclude, buttonExclude;
	private JButton btnSave, btnCancel;
	JComboBox comboBox;
	
	Vector<Mod> addedFiles;
	Vector<String> engines;
	Settings options;
	private JLabel lblPresetFileName;
	private JTextField textFieldFileNamePreset;
	private JTextField txtPresetname;
	private JTextArea txtPresetDescription;
	private JPanel panelEngines;
	private JPanel panelMods;
	private JScrollPane scrollPane_3;
	private JPanel panelEnginesList;
	private JTextField txtImagePath;
	JButton btnImagePath;
	JLabel lblImagePreview;
	
	private Vector<JCheckBox> customEnginesCheckBoxes;
	
	public PresetDialog(Settings options, JFrame owner)
	{
		this(options, owner, null);
	}
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public PresetDialog(Settings options, JFrame owner, Preset preset)
	{
		super( owner, "Create a preset", true ); 
		setResizable(false);
		setSize(new Dimension(610, 450));
		
		addedFiles = new Vector<Mod>();
		this.options = options;
		
		getContentPane().setLayout(null);
		
		JLabel lblSavePresetIn = new JLabel("Save preset in:");
		lblSavePresetIn.setBounds(12, 360, 107, 15);
		getContentPane().add(lblSavePresetIn);
		
		comboBox = new JComboBox(options.pathsToSearchForPresets);
		comboBox.setBounds(133, 355, 463, 24);
		comboBox.setSelectedIndex(0);
		getContentPane().add(comboBox);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(416, 386, 85, 25);
		btnSave.addActionListener(this);
		getContentPane().add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(513, 386, 85, 25);
		btnCancel.addActionListener(this);
		getContentPane().add(btnCancel);
		
		lblPresetFileName = new JLabel("Preset filename:");
		lblPresetFileName.setBounds(12, 391, 107, 15);
		getContentPane().add(lblPresetFileName);
		
		textFieldFileNamePreset = new JTextField();
		textFieldFileNamePreset.setText("unnamed_preset.txt");
		textFieldFileNamePreset.setBounds(133, 386, 242, 24);
		getContentPane().add(textFieldFileNamePreset);
		textFieldFileNamePreset.setColumns(10);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 12, 586, 336);
		getContentPane().add(tabbedPane);
		
		//==================================== GENERAL INFO TAB
		JPanel panelGeneralInfo = new JPanel();
		tabbedPane.addTab("General info", null, panelGeneralInfo, null);
		panelGeneralInfo.setLayout(null);
		
		JLabel lblPresetName = new JLabel("Name:");
		lblPresetName.setBounds(12, 12, 95, 18);
		panelGeneralInfo.add(lblPresetName);
		
		txtPresetname = new JTextField();
		txtPresetname.setText("Unnamed preset");
		txtPresetname.setBounds(76, 7, 490, 28);
		panelGeneralInfo.add(txtPresetname);
		txtPresetname.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(12, 52, 85, 18);
		panelGeneralInfo.add(lblDescription);
		
		txtPresetDescription = new JTextArea();
		txtPresetDescription.setBounds(98, 42, 250, 28);
		txtPresetDescription.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(109, 49, 457, 128);
		scrollPane_2.setViewportView(txtPresetDescription);
		panelGeneralInfo.add(scrollPane_2);
		
		JLabel lblImagePath = new JLabel("(Optional) Image path:");
		lblImagePath.setBounds(12, 189, 143, 16);
		panelGeneralInfo.add(lblImagePath);
		
		txtImagePath = new JTextField();
		txtImagePath.setText("Image Path");
		txtImagePath.setBounds(176, 184, 361, 26);
		panelGeneralInfo.add(txtImagePath);
		txtImagePath.setColumns(10);
		
		btnImagePath = new JButton("...");
		btnImagePath.addActionListener(this);
		btnImagePath.setBounds(540, 184, 26, 26);
		panelGeneralInfo.add(btnImagePath);
		
		JLabel lblPreview = new JLabel("Preview:");
		lblPreview.setBounds(12, 243, 60, 16);
		panelGeneralInfo.add(lblPreview);
		
		lblImagePreview = new JLabel("");
		lblImagePreview.setBounds(76, 217, 490, 71);
		panelGeneralInfo.add(lblImagePreview);
		
		
		
		//========================== ENGINES TAB
		panelEngines = new JPanel();
		tabbedPane.addTab("Engines", null, panelEngines, null);
		panelEngines.setLayout(null);
		
		JLabel lblEngine = new JLabel("Engines:");
		lblEngine.setBounds(12, 12, 57, 16);
		panelEngines.add(lblEngine);
		
		panelEnginesList = new JPanel();
		panelEnginesList.setBounds(22, 40, 284, 175);
		panelEnginesList.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.customEnginesCheckBoxes = new Vector<JCheckBox>();
		for(EngineInfo ei : options.customEngines)
		{
			customEnginesCheckBoxes.add(new JCheckBox(""));
			JPanel groupPanel = new JPanel();
			groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));
			groupPanel.add(customEnginesCheckBoxes.lastElement());
			groupPanel.add(new JLabel(new ImageIcon(ei.iconFileName)));
			groupPanel.add(new JLabel(ei.toString()));
			panelEnginesList.add(groupPanel);
		}
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(12, 40, 554, 248);
		scrollPane_3.setViewportView(panelEnginesList);
		panelEngines.add(scrollPane_3);
		
		//===========================  MODS TAB
		panelMods = new JPanel();
		tabbedPane.addTab("Mods", null, panelMods, null);
		panelMods.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 39, 240, 249);
		panelMods.add(scrollPane);
		
		listAvailable = new JList();
		listAvailable.setListData(Mod.getAllAvailableMods(options));
		scrollPane.setViewportView(listAvailable);
		
		JLabel lblWadsAvailable = new JLabel("Mods available:");
		lblWadsAvailable.setBounds(12, 12, 200, 15);
		panelMods.add(lblWadsAvailable);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(335, 39, 231, 249);
		panelMods.add(scrollPane_1);
		
		listIncluded = new JList();
		listIncluded.setListData(addedFiles);
		scrollPane_1.setViewportView(listIncluded);
		
		JLabel lblWadspkIncluded = new JLabel("Mods included:");
		lblWadspkIncluded.setBounds(343, 12, 209, 15);
		panelMods.add(lblWadspkIncluded);
		
		//buttonInclude = new JButton("=>");
		buttonInclude = new JButton(new ImageIcon("image/arrow-right.png"));
		buttonInclude.setToolTipText("Add to preset");
		buttonInclude.addActionListener(this);
		buttonInclude.setBounds(266, 79, 57, 25);
		panelMods.add(buttonInclude);
		
		//buttonExclude = new JButton("<=");
		buttonExclude = new JButton(new ImageIcon("image/arrow-left.png"));
		buttonExclude.setToolTipText("Remove from preset");
		buttonExclude.addActionListener(this);
		buttonExclude.setBounds(266, 219, 57, 25);
		panelMods.add(buttonExclude);
		
		
		
		//update editing preset values
		if(preset != null)
		{	
			//reload preset data, just to be safe
			try 
			{ 
				preset.reload(); 
				textFieldFileNamePreset.setText(preset.file.getName());
				txtPresetname.setText(preset.name);
				txtPresetDescription.setText( preset.description);
				txtImagePath.setText( preset.imagePath);
				
				addedFiles = new Vector<Mod>(preset.mods);
				listIncluded.setListData(addedFiles);
				listAvailable.setListData(getNotIncludedModsList());
				
				engines = new Vector<String>(preset.engines);
				
				for(String str : engines)
				{
					boolean match=false;
					for(EngineInfo ei : options.customEngines)
					{
						if( ei.code.equalsIgnoreCase(str) )
						{
							this.customEnginesCheckBoxes.get( options.customEngines.indexOf(ei) ).setSelected(true);
							match=true;
						}
					}
					if( ! match)
					{
						//if the engine is in the file but is not registered in setting file, still shows it, but alert the user
						customEnginesCheckBoxes.add(new JCheckBox(str));
						customEnginesCheckBoxes.lastElement().setSelected(true);
						JPanel groupPanel = new JPanel();
						groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));
						groupPanel.add(customEnginesCheckBoxes.lastElement());
						groupPanel.add(new JLabel("(UNREGISTERED)"));
						panelEnginesList.add(groupPanel);
					}
				}
				
			} 
			catch (FileNotFoundException e) 
			{
				JOptionPane.showConfirmDialog(this, "Could not open file for read: "+e.getLocalizedMessage());
			}
			catch (Exception e) 
			{
				JOptionPane.showConfirmDialog(this, "Error while opening file: "+e.getLocalizedMessage());
			}
		}
		
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	protected Vector<Mod> getNotIncludedModsList()
	{
		Vector<Mod> filesToShow = new Vector<Mod>();
		for( Mod m : Mod.getAllAvailableMods(options))
		{
			boolean contains = false;
			for( Mod n : addedFiles )
			{
				if( m.file.equals(n.file) ){ contains = true; break; } 
			}
			if( ! contains) filesToShow.add(m);
		}
		return filesToShow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// BUTTON INCLUDE
		if(e.getSource().equals(buttonInclude) && listAvailable.getSelectedIndex()>-1)
		{
			addedFiles.add( (Mod) listAvailable.getSelectedValue() );
		}
		
		//BUTTON REMOVE
		else if(e.getSource().equals(buttonExclude) && listIncluded.getSelectedIndex()>-1)
		{
			addedFiles.remove(listIncluded.getSelectedIndex());
		}
		
		//BUTTON SAVE PRESET
		else if(e.getSource().equals(btnSave))
		{
			if( !textFieldFileNamePreset.getText().equals(""))
			{
				String prepath = (String) comboBox.getSelectedItem();
				if(!prepath.endsWith(File.separator)) prepath+=File.separator;
				engines = new Vector<String>();
				
				int i;
				for(i=0 ; i < options.customEngines.size() ; i++)
				{
					if( customEnginesCheckBoxes.get(i).isSelected() )
					{
						engines.add(options.customEngines.get(i).code);
					}
				}
				
				//have some unregistered engines
				if( customEnginesCheckBoxes.size() > options.customEngines.size() ) for( ; i < customEnginesCheckBoxes.size() ; i++ )
				{
					engines.add( customEnginesCheckBoxes.get(i).getText() );
				}
				
				try 
				{
					Preset.createAndSave(new File( prepath+textFieldFileNamePreset.getText()), txtPresetname.getText(), txtPresetDescription.getText(), txtImagePath.getText(), engines, addedFiles);
					this.dispose();
				} 
				catch (IOException e2) 
				{
					JOptionPane.showMessageDialog(this, "Could not save file "+textFieldFileNamePreset.getText()+"\nIO error: "+e2.getLocalizedMessage(), "Error saving file", JOptionPane.ERROR_MESSAGE); 
					return;
				}
				catch (FileAlreadyExistsException e2)
				{
					if(JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "A file with name "+textFieldFileNamePreset.getText()+" already exists!\nDo you want to overwrite?", "Same name!", JOptionPane.YES_NO_OPTION))
					return;
					else try
					{
						Preset.createAndSave(new File( prepath+textFieldFileNamePreset.getText()), txtPresetname.getText(), txtPresetDescription.getText(), txtImagePath.getText(), engines, addedFiles, true);
						this.dispose();
					}
					catch (IOException e3) 
					{
						JOptionPane.showMessageDialog(this, "Could not save file "+textFieldFileNamePreset.getText()+"\nIO error: "+e3.getLocalizedMessage(), "Error saving file", JOptionPane.ERROR_MESSAGE); 
						return;
					}
					catch (Exception e3) 
					{
						JOptionPane.showMessageDialog(this, "Could not save file "+textFieldFileNamePreset.getText()+"\n"+e3.getLocalizedMessage(), "Error saving file", JOptionPane.ERROR_MESSAGE); 
						return;
					}
					
				}
				catch (Exception e2) 
				{
					JOptionPane.showMessageDialog(this, "Could not save file "+textFieldFileNamePreset.getText()+"\n"+e2.getLocalizedMessage(), "Error saving file", JOptionPane.ERROR_MESSAGE); 
					return;
				}
			}
			else JOptionPane.showMessageDialog(this, "Preset filename is empty!", "Error", JOptionPane.WARNING_MESSAGE);
		}
		
		//BUTTON CANCEL CREATING PRESET
		else if(e.getSource().equals(btnCancel))
		{
			this.dispose();
		}
		
		//BUTTON BROWSE OPTIONAL IMAGE PATH
		else if(e.getSource().equals(btnImagePath))
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

				txtImagePath.setText(filechooser.getSelectedFile().getPath());
				lblImagePreview.setIcon(new ImageIcon(txtImagePath.getText()));
				lblImagePreview.setText("");
			}
			
		}
		
		listAvailable.setListData(getNotIncludedModsList());
		listIncluded.setListData(addedFiles);
	}
	
	protected JList getListAvailable() {
		return listAvailable;
	}
	protected JList getListIncluded() {
		return listIncluded;
	}
	protected JButton getButtonInclude() {
		return buttonInclude;
	}
	protected JButton getButtonExclude() {
		return buttonExclude;
	}
	protected JTextField getTextFieldNamePreset() {
		return textFieldFileNamePreset;
	}
}

