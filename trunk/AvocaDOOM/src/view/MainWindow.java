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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.*;
import controler.*;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.Box;

public class MainWindow implements ActionListener, ListSelectionListener, WindowListener
{
	private JFrame window;
	private Settings options;
	
	private JToolBar bottomToolBar;
	private JButton btnRun, btnCreateNewPreset, btnEditPreset, btnRemovePreset, btnSettings, btnAbout;
	private Component horizontalGlue;
	private JComboBox comboBox;
	
	private JList presets_jlist, content_jlist;
	private JScrollPane scrollPane;
	
	private JPanel panelEngineJLabels;
	private JLabel lblSelectedEnginePic;
	private JLabel lblPresetInfo, lblPresetName, lblPresetDescription, lblPresetFilename, lblPresetimage;
	
	public MainWindow(Settings options) 
	{
		window = new JFrame();
		this.options = options;
		
		window.setSize(640, 480);
		if( ! Main.isDebugBuild ) window.setSize(options.window_size_x, options.window_size_y);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setTitle("AvocaDOOM v"+Main.APP_VERSION);
		window.setIconImage(new ImageIcon("image/avocadoom.png").getImage());
		
		JSplitPane splitPane = new JSplitPane();
		window.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel infoPanel = new JPanel();
		splitPane.setRightComponent(infoPanel);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		lblPresetimage = new JLabel("");
		infoPanel.add(lblPresetimage);
		
		lblPresetName = new JLabel("Preset name");
		infoPanel.add(lblPresetName);
		
		lblPresetDescription = new JLabel("Preset description");
		infoPanel.add(lblPresetDescription);
		
		lblPresetFilename = new JLabel("Preset filename");
		infoPanel.add(lblPresetFilename);
		
		scrollPane = new JScrollPane();
		infoPanel.add(scrollPane);
		
		panelEngineJLabels = new JPanel();
		scrollPane.setViewportView(panelEngineJLabels);
		panelEngineJLabels.setLayout(new BoxLayout(panelEngineJLabels, BoxLayout.Y_AXIS));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);
		
		presets_jlist = new JList();
		presets_jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		presets_jlist.addListSelectionListener(this);
		refreshPresetList();
		
		content_jlist = new JList();
		content_jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Vector<Mod> lista = new Vector<Mod>();
		content_jlist.setListData(lista);
		
		JScrollPane scrollPane_1 = new JScrollPane(presets_jlist);
		splitPane_1.setLeftComponent(scrollPane_1);
		
		JScrollPane scrollPane2 = new JScrollPane(content_jlist);
		splitPane_1.setRightComponent(scrollPane2);
		
		splitPane_1.setDividerLocation(100);
		splitPane.setDividerLocation(400);
		
		JToolBar toolBar = new JToolBar();
		window.getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		toolBar.setFloatable(false);
		
		//btnRun = new JButton("Run!");
		btnRun = new JButton(new ImageIcon("image/play.png"));
		btnRun.setToolTipText("Run!");
		btnRun.addActionListener(this);
		toolBar.add(btnRun);

		//btnCreateNewPreset = new JButton("Create new preset");
		btnCreateNewPreset = new JButton(new ImageIcon("image/create-preset.png"));
		btnCreateNewPreset.setToolTipText("Create new preset");
		btnCreateNewPreset.addActionListener(this);
		toolBar.add(btnCreateNewPreset);
		
		//btnEditPreset = new JButton("Edit preset");
		btnEditPreset = new JButton(new ImageIcon("image/edit-preset.png"));
		btnEditPreset.setToolTipText("Edit preset");
		btnEditPreset.addActionListener(this);
		toolBar.add(btnEditPreset);
		
		//btnRemovePreset = new JButton("Remove preset");
		btnRemovePreset = new JButton(new ImageIcon("image/delete-preset.png"));
		btnRemovePreset.setToolTipText("Remove preset");
		btnRemovePreset.addActionListener(this);
		toolBar.add(btnRemovePreset);
		
		btnSettings = new JButton("Settings");
		btnSettings = new JButton(new ImageIcon("image/settings.png"));
		btnSettings.setToolTipText("Settings");
		btnSettings.addActionListener(this);
		toolBar.add(btnSettings);
		
		//btnAbout = new JButton("About");
		btnAbout = new JButton(new ImageIcon("image/about.png"));
		btnAbout.setToolTipText("About AvocaDOOM");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(window, Main.APP_ABOUT_TEXT+"Version "+Main.APP_VERSION+"\n"+Main.APP_LICENCE, "About AvocaDOOM", JOptionPane.PLAIN_MESSAGE, new ImageIcon("image/avocadoom.png"));
			}
		});
		toolBar.add(btnAbout);
		
		horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		
		//the combo box use the structure internally, so we need to pass a new, independent vector to it, so it doesnt screw with our data
		comboBox = new JComboBox(new Vector<EngineInfo>(options.customEngines));
		comboBox.addActionListener(this);
		
		lblSelectedEnginePic = new JLabel("");
		//lblSelectedEnginePic.setIcon( new ImageIcon("image/engine.png") );
		if( comboBox.getSelectedIndex() >= 0)
		{
			if( ((EngineInfo) comboBox.getSelectedItem()).iconFileName != null && new File(((EngineInfo) comboBox.getSelectedItem()).iconFileName).isFile() )
			lblSelectedEnginePic.setIcon( new ImageIcon( ((EngineInfo) comboBox.getSelectedItem()).iconFileName ) );
			else lblSelectedEnginePic.setIcon( new ImageIcon("image/engine.png") );
		}
		
		toolBar.add(lblSelectedEnginePic);
		toolBar.add(comboBox);
		
		bottomToolBar = new JToolBar();
		window.getContentPane().add(bottomToolBar, BorderLayout.SOUTH);
		
		lblPresetInfo = new JLabel("Preset info");
		bottomToolBar.add(lblPresetInfo);
		
		//last thing to do
		window.addWindowListener(this);
		window.setLocationByPlatform(true);
		window.setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//run! button
		if(arg0.getSource().equals(btnRun))
		{
			Runtime runtime = Runtime.getRuntime();
			Process process = null;
			try {
				if(presets_jlist.getSelectedIndex() > 0)
				{
					if( ! ((Preset) presets_jlist.getSelectedValue()).engines.contains( ((EngineInfo) comboBox.getSelectedItem()).code ) && options.alertIncompEngine ) 
						if( JOptionPane.showConfirmDialog(window, "The selected preset probably is not compatible with the engine "+
					((EngineInfo) comboBox.getSelectedItem()).code + ". Are you sure you want to execute it anyway?", 
					"Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) return;
					process = runtime.exec(getCommandAndArguments( (EngineInfo) comboBox.getSelectedItem(), (Preset) presets_jlist.getSelectedValue() ));
				}
				else if(presets_jlist.getSelectedIndex() == 0 && content_jlist.getSelectedIndex() >-1)
				{
					//start with an specific wad/pk3
					process = runtime.exec( ((EngineInfo) comboBox.getSelectedItem()).executablePath+" "+ ((Mod) content_jlist.getSelectedValue()).file.getPath());
				}
				else
				{
					process = runtime.exec(((EngineInfo) comboBox.getSelectedItem()).executablePath);
				}

				if(process != null) 
				{
					if(options.showAuxConsole)
					{
						RunningConsoleWindow aux = new RunningConsoleWindow(process, "Executing "+((EngineInfo) comboBox.getSelectedItem()).executablePath+"...");
						Thread anotherThread = new Thread(aux);
						anotherThread.start();

						//int exitVal = process.waitFor();
						//System.out.println("Executable exited with error code "+exitVal);
					}
				}
				else JOptionPane.showMessageDialog(window, "Error: null process", "Execution problem", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(window, "Error: "+e1.getLocalizedMessage(), "Execution problem", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		else if(arg0.getSource().equals(btnCreateNewPreset))
		{
			new PresetDialog(options, window);
			refreshPresetList();
			content_jlist.setListData(new Vector<Mod>());
		}
		
		else if(arg0.getSource().equals(btnEditPreset) && presets_jlist.getSelectedIndex()>0)
		{
			new PresetDialog(options, window, (Preset) presets_jlist.getSelectedValue());
			refreshPresetList();
			content_jlist.setListData(new Vector<Mod>());
		}
		
		else if(arg0.getSource().equals(btnRemovePreset) && presets_jlist.getSelectedIndex()>0)
		{
			File file = ((Preset) presets_jlist.getSelectedValue()).file;
			if( JOptionPane.showConfirmDialog(window, "This will delete the preset "+file.getName()+" from the system.\nAre you sure?", "Remove preset", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION )
			{
				try{
				if(!file.canWrite()) JOptionPane.showMessageDialog(window, file.getName()+" cannot be deleted!\nThis application cannot delete this file.", "Delete error!", JOptionPane.ERROR_MESSAGE);
				else if(file.delete()==true) JOptionPane.showMessageDialog(window, file.getName()+" removed!");
				else JOptionPane.showMessageDialog(window, file.getName()+" could not be removed!", "Delete error!", JOptionPane.ERROR_MESSAGE);
				}
				catch(SecurityException e)
				{
					JOptionPane.showMessageDialog(window, "This application does not have permission to modify "+file.getName()+"\n"+e.getLocalizedMessage(), "Delete error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			refreshPresetList();
			content_jlist.setListData(new Vector<Mod>());
		}
		
		else if(arg0.getSource().equals(btnSettings))
		{
			new SettingsDialog(options, window);
			refreshPresetList();
			content_jlist.setListData(new Vector<Mod>());
			
			comboBox.removeAllItems();
			for(EngineInfo ei : options.customEngines) comboBox.addItem(ei);
		}
		
		else if( arg0.getSource().equals(comboBox) )
		{
			if( comboBox.getSelectedIndex() >= 0)
			{
				if( ((EngineInfo) comboBox.getSelectedItem()).iconFileName != null && new File(((EngineInfo) comboBox.getSelectedItem()).iconFileName).isFile() )
				lblSelectedEnginePic.setIcon( new ImageIcon( ((EngineInfo) comboBox.getSelectedItem()).iconFileName ) );
				else lblSelectedEnginePic.setIcon( new ImageIcon("image/engine.png") );
			}
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		//selected preset changed
		if(e.getSource().equals(presets_jlist))
		{
			//if(e.getFirstIndex() == 0) //means all pk3
			if( ((JList) e.getSource()).getSelectedIndex() == 0) //means all pk3
			{
				content_jlist.setListData(Mod.getAllAvailableMods(options));
				lblPresetInfo.setText("All wads");
				lblPresetName.setText( "All wads");
				lblPresetDescription.setText("");
				lblPresetFilename.setText("");
				lblPresetimage.setIcon(null);
				panelEngineJLabels.removeAll();
			}
			//else if(e.getFirstIndex() > 0) //means a preset
			else if( ((JList) e.getSource()).getSelectedIndex() > 0) //means a preset
			{
				Preset preset = (Preset) presets_jlist.getSelectedValue();
				showPresetContent(preset);
				lblPresetInfo.setText( preset.file.getName() + ", " + preset.mods.size() + " files included" );
				lblPresetName.setText( preset.name);
				lblPresetDescription.setText(preset.description);
				lblPresetFilename.setText(preset.file.getName());
				lblPresetimage.setIcon(new ImageIcon(preset.imagePath));
				
				panelEngineJLabels.removeAll();
				for(String str : preset.engines)
				{
					boolean match = false;
					for( EngineInfo ei : options.customEngines )
					{
						if( ei.code.equalsIgnoreCase(str) )
						{
							JLabel label = new JLabel(ei.code);
							if( ei.iconFileName != null && new File(ei.iconFileName).isFile() )
								label.setIcon(new ImageIcon(ei.iconFileName) );
							else label.setIcon( new ImageIcon("image/engine.png") );
							
							panelEngineJLabels.add(label);
							match=true;
							break;
						}
					}
					if( ! match )
					{
						JLabel label = new JLabel(str+" (UNREGISTERED)");
						label.setIcon( new ImageIcon("image/unknown.png") );
						panelEngineJLabels.add(label);
					}
					
				}
			}
		}
		
	}
	
	//updates the presets shown in the presets list
	public void refreshPresetList()
	{
		Vector<Object> listaNova = new Vector<Object>();
		listaNova.add("all");
		for(String str : options.pathsToSearchForPresets)
		{
			if(str==null) JOptionPane.showMessageDialog(window, "Error: null directory! Ignoring...");
			else if(str.equals("")) JOptionPane.showMessageDialog(window, "Error: Empty path! Ignoring...");
			else if(new File(str).isDirectory())
			{
				File someFolder = new File(str);
				for( File f : someFolder.listFiles() )
				{
					if(f.isFile())
					{
						try {
							if( Preset.isValidPreset(f) )
							{
								listaNova.add( new Preset( f ) );
							}
						} catch (FileNotFoundException e) {
							JOptionPane.showMessageDialog(window, "Error: file not found! Yet it passes isFile()!\n"+e.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(window, "Error loading preset!\n"+e.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			else JOptionPane.showMessageDialog(window, "Error: Path \""+str+"\" is not a diretory! Ignoring...");
		}
		presets_jlist.setListData(listaNova);
	}
	
	public void showPresetContent(Preset preset)
	{
		if(preset != null)
		{
			try {
				preset.reload();
				content_jlist.setListData(preset.mods);
			} catch (FileNotFoundException e1) {
				JOptionPane.showConfirmDialog(window, "Could not open preset file for read: "+e1.getLocalizedMessage());
				content_jlist.setListData(new Vector<Mod>());
			} catch (Exception e1) {
				JOptionPane.showConfirmDialog(window, "Could not open preset file for read: "+e1.getLocalizedMessage());
				content_jlist.setListData(new Vector<Mod>());
			}		
		}
		else
		{
			//when something goes wrong, show a empty list
			content_jlist.setListData(new Vector<Mod>());
		}
	}
	
	public String[] getCommandAndArguments(EngineInfo info, Preset preset)
	{
		if(info == null) return null;
		
		String[] str = new String[1];
		Vector<String> commandLine = new Vector<String>();
		commandLine.add(info.executablePath);
		
		if(preset != null) for(Mod m : preset.mods)
		{
			commandLine.add(m.file.getPath());
		}
		
		return commandLine.toArray(str);
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		options.window_size_x = window.getWidth();
		options.window_size_y = window.getHeight();
		options.saveOptionsToFile();
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		window.dispose();
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	@Override
	public void windowOpened(WindowEvent arg0) {	
	}
	
	

	protected JButton getBtnCreateNewPreset() {
		return btnCreateNewPreset;
	}
	protected JButton getBtnRemovePreset() {
		return btnRemovePreset;
	}
	protected JButton getBtnEditPreset() {
		return btnEditPreset;
	}
	protected JLabel getLblPresetInfo() {
		return lblPresetInfo;
	}
}

