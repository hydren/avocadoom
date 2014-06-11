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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.EngineInfo;
import util.UIManager2;
import controller.Settings;

public class SettingsDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5095508074515360521L;
	JList wadPk3JList, presetsJList;
	JButton btnApply, btnCancel;
	JButton btnAddEntryWadPk3, btnAddbrowseWadPk3, btnEditWadPk3, btnRemoveWadPk3;
	JButton btnAddEntryPresets, btnAddbrowsePresets, btnEditPresets, btnRemovePresets;
	JButton btnAddCustomEngine, btnEditCustomEngine, btnRemoveCustomEngine;
	
	Settings original;
	private JPanel panelPresets;
	private JPanel panelSettings;
	private JPanel panel;
	
	private Vector<String> pathsToSearchForWads, pathsToSearchForPresets;
	private Vector<EngineInfo> customEngines;
	private JLabel lblEnginesPathcommandLine;
	private JScrollPane scrollPaneListEngines;
	private JList customEnginesJlist;
	private JPanel panel_engines_group;
	private JPanel panel_engineButtons;
	private JPanel panel_customEngines;
	private JCheckBox chckbxUseSystemLook;
	private JCheckBox chckbxAlertWhenLaunching;
	private JCheckBox chckbxShowAConsole;
	private JCheckBox chckbxUseOpenglRender;
	
	public SettingsDialog(Settings options, JFrame owner)
	{	
		super( owner, "Settings", true ); 
		pathsToSearchForPresets = new Vector<String>(options.pathsToSearchForPresets);
		pathsToSearchForWads = new Vector<String>(options.pathsToSearchForWads);
		customEngines = new Vector<EngineInfo>(options.customEngines);
		original = options;
		
		//window = new JFrame("Settings");
		setMinimumSize(new Dimension(320, 320));
		setSize(new Dimension(512, 327));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		panelSettings = new JPanel();
		tabbedPane.addTab("General", null, panelSettings, null);
		panelSettings.setLayout(null);
		
		chckbxUseSystemLook = new JCheckBox("Use system's look and feel");
		chckbxUseSystemLook.setBounds(5, 5, 215, 23);
		chckbxUseSystemLook.setSelected(options.useOSLF);
		panelSettings.add(chckbxUseSystemLook);
		
		chckbxAlertWhenLaunching = new JCheckBox("Alert when using a preset with a different engine");
		chckbxAlertWhenLaunching.setBounds(5, 33, 376, 23);
		chckbxAlertWhenLaunching.setSelected(options.alertIncompEngine);
		panelSettings.add(chckbxAlertWhenLaunching);
		
		chckbxShowAConsole = new JCheckBox("Show a console window with the engine's executable output");
		chckbxShowAConsole.setBounds(5, 61, 457, 23);
		chckbxShowAConsole.setSelected(options.showAuxConsole);
		panelSettings.add(chckbxShowAConsole);
		
		chckbxUseOpenglRender = new JCheckBox("Use OpenGL rendering for Avocadoom (requires restart)");
		chckbxUseOpenglRender.setToolTipText("Fix some visual glitches but may cause problems on machines with OpenGL older than 1.2");
		chckbxUseOpenglRender.setBounds(5, 88, 422, 23);
		chckbxUseOpenglRender.setSelected(options.useOGLgui);
		panelSettings.add(chckbxUseOpenglRender);
		
		JPanel panelWadPk3 = new JPanel();
		tabbedPane.addTab("Wads/PK3s", null, panelWadPk3, null);
		panelWadPk3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAllFoldersHereWadPk3 = new JLabel(" All folders here will be checked for wads, pk3, etc.");
		lblAllFoldersHereWadPk3.setBorder(new EmptyBorder(4, 2, 4, 0));
		panelWadPk3.add(lblAllFoldersHereWadPk3, BorderLayout.NORTH);
		
		wadPk3JList = new JList();
		wadPk3JList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wadPk3JList.setListData(pathsToSearchForWads);
		
		JScrollPane scrollPaneWadPk3 = new JScrollPane();
		scrollPaneWadPk3.setViewportView(wadPk3JList);
		panelWadPk3.add(scrollPaneWadPk3, BorderLayout.CENTER);
		
		JPanel panelWadPk3Buttons = new JPanel();
		panelWadPk3.add(panelWadPk3Buttons, BorderLayout.EAST);
		GridBagLayout gbl_panelWadPk3Buttons = new GridBagLayout();
		gbl_panelWadPk3Buttons.columnWidths = new int[]{141, 0};
		gbl_panelWadPk3Buttons.rowHeights = new int[]{44, 44, 44, 44, 0};
		gbl_panelWadPk3Buttons.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panelWadPk3Buttons.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelWadPk3Buttons.setLayout(gbl_panelWadPk3Buttons);
		
		btnEditWadPk3 = new JButton("Edit entry");
		btnEditWadPk3.addActionListener(this);
		
		btnAddEntryWadPk3 = new JButton("Add entry");
		btnAddEntryWadPk3.addActionListener(this);
		
		btnAddbrowseWadPk3 = new JButton("Add (browsing)");
		btnAddbrowseWadPk3.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAddbrowseWadPk3.addActionListener(this);
		GridBagConstraints gbc_btnAddbrowseWadPk3 = new GridBagConstraints();
		gbc_btnAddbrowseWadPk3.fill = GridBagConstraints.BOTH;
		gbc_btnAddbrowseWadPk3.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddbrowseWadPk3.gridx = 0;
		gbc_btnAddbrowseWadPk3.gridy = 0;
		panelWadPk3Buttons.add(btnAddbrowseWadPk3, gbc_btnAddbrowseWadPk3);
		btnAddEntryWadPk3.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnAddEntryWadPk3 = new GridBagConstraints();
		gbc_btnAddEntryWadPk3.fill = GridBagConstraints.BOTH;
		gbc_btnAddEntryWadPk3.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddEntryWadPk3.gridx = 0;
		gbc_btnAddEntryWadPk3.gridy = 1;
		panelWadPk3Buttons.add(btnAddEntryWadPk3, gbc_btnAddEntryWadPk3);
		btnEditWadPk3.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnEditWadPk3 = new GridBagConstraints();
		gbc_btnEditWadPk3.fill = GridBagConstraints.BOTH;
		gbc_btnEditWadPk3.insets = new Insets(0, 0, 5, 0);
		gbc_btnEditWadPk3.gridx = 0;
		gbc_btnEditWadPk3.gridy = 2;
		panelWadPk3Buttons.add(btnEditWadPk3, gbc_btnEditWadPk3);
		
		btnRemoveWadPk3 = new JButton("Remove");
		btnRemoveWadPk3.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRemoveWadPk3.addActionListener(this);
		GridBagConstraints gbc_btnRemoveWadPk3 = new GridBagConstraints();
		gbc_btnRemoveWadPk3.fill = GridBagConstraints.BOTH;
		gbc_btnRemoveWadPk3.gridx = 0;
		gbc_btnRemoveWadPk3.gridy = 3;
		panelWadPk3Buttons.add(btnRemoveWadPk3, gbc_btnRemoveWadPk3);
		
		panelPresets = new JPanel();
		tabbedPane.addTab("Presets", null, panelPresets, null);
		panelPresets.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAllFoldersHerePresets = new JLabel(" All folders here will be checked for presets.");
		lblAllFoldersHerePresets.setBorder(new EmptyBorder(4, 2, 4, 0));
		panelPresets.add(lblAllFoldersHerePresets, BorderLayout.NORTH);
		
		presetsJList = new JList();
		presetsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		presetsJList.setListData(pathsToSearchForPresets);
		
		JScrollPane scrollPanePresets = new JScrollPane();
		scrollPanePresets.setViewportView(presetsJList);
		panelPresets.add(scrollPanePresets, BorderLayout.CENTER);
		
		JPanel panelPresetsButtons = new JPanel();
		panelPresets.add(panelPresetsButtons, BorderLayout.EAST);
		GridBagLayout gbl_panelPresetsButtons = new GridBagLayout();
		gbl_panelPresetsButtons.columnWidths = new int[]{141, 0};
		gbl_panelPresetsButtons.rowHeights = new int[]{44, 44, 44, 44, 0};
		gbl_panelPresetsButtons.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panelPresetsButtons.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelPresetsButtons.setLayout(gbl_panelPresetsButtons);
		
		btnRemovePresets = new JButton("Remove");
		btnRemovePresets.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRemovePresets.addActionListener(this);
		
		btnAddbrowsePresets = new JButton("Add (browsing)");
		btnAddbrowsePresets.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAddbrowsePresets.addActionListener(this);
		GridBagConstraints gbc_btnAddbrowsePresets = new GridBagConstraints();
		gbc_btnAddbrowsePresets.fill = GridBagConstraints.BOTH;
		gbc_btnAddbrowsePresets.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddbrowsePresets.gridx = 0;
		gbc_btnAddbrowsePresets.gridy = 0;
		panelPresetsButtons.add(btnAddbrowsePresets, gbc_btnAddbrowsePresets);
		
		btnAddEntryPresets = new JButton("Add entry");
		btnAddEntryPresets.addActionListener(this);
		btnAddEntryPresets.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnAddEntryPresets = new GridBagConstraints();
		gbc_btnAddEntryPresets.fill = GridBagConstraints.BOTH;
		gbc_btnAddEntryPresets.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddEntryPresets.gridx = 0;
		gbc_btnAddEntryPresets.gridy = 1;
		panelPresetsButtons.add(btnAddEntryPresets, gbc_btnAddEntryPresets);
		
		btnEditPresets = new JButton("Edit entry");
		btnEditPresets.addActionListener(this);
		btnEditPresets.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnEditPresets = new GridBagConstraints();
		gbc_btnEditPresets.fill = GridBagConstraints.BOTH;
		gbc_btnEditPresets.insets = new Insets(0, 0, 5, 0);
		gbc_btnEditPresets.gridx = 0;
		gbc_btnEditPresets.gridy = 2;
		panelPresetsButtons.add(btnEditPresets, gbc_btnEditPresets);
		GridBagConstraints gbc_btnRemovePresets = new GridBagConstraints();
		gbc_btnRemovePresets.fill = GridBagConstraints.BOTH;
		gbc_btnRemovePresets.gridx = 0;
		gbc_btnRemovePresets.gridy = 3;
		panelPresetsButtons.add(btnRemovePresets, gbc_btnRemovePresets);
		
		panel_customEngines = new JPanel();
		tabbedPane.addTab("Engine configs", null, panel_customEngines, null);
		panel_customEngines.setLayout(new BorderLayout(0, 0));
		
		panel_engines_group = new JPanel();
		panel_customEngines.add(panel_engines_group, BorderLayout.CENTER);
		panel_engines_group.setPreferredSize(new Dimension(300, 200));
		panel_engines_group.setLayout(new BorderLayout(0, 0));
		
		lblEnginesPathcommandLine = new JLabel("Saved configs:");
		lblEnginesPathcommandLine.setBorder(new EmptyBorder(4, 2, 4, 0));
		panel_engines_group.add(lblEnginesPathcommandLine, BorderLayout.NORTH);
		
		
		customEnginesJlist = new JList();
		customEnginesJlist.setListData(customEngines);
		//panelSettings.add(listEngines);
		
		scrollPaneListEngines = new JScrollPane();
		panel_engines_group.add(scrollPaneListEngines);
		scrollPaneListEngines.setViewportView(customEnginesJlist);
		
		panel_engineButtons = new JPanel();
		panel_engineButtons.setPreferredSize(new Dimension(100, 100));
		panel_customEngines.add(panel_engineButtons, BorderLayout.EAST);
		
		btnAddCustomEngine = new JButton("Add");
		btnAddCustomEngine.setBounds(12, 20, 80, 35);
		btnAddCustomEngine.addActionListener(this);
		panel_engineButtons.setLayout(null);
		btnAddCustomEngine.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_engineButtons.add(btnAddCustomEngine);
		
		btnEditCustomEngine = new JButton("Edit");
		btnEditCustomEngine.setBounds(12, 70, 80, 35);
		btnEditCustomEngine.addActionListener(this);
		btnEditCustomEngine.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_engineButtons.add(btnEditCustomEngine);
		
		btnRemoveCustomEngine = new JButton("Remove");
		btnRemoveCustomEngine.setBounds(7, 120, 90, 34);
		btnRemoveCustomEngine.addActionListener(this);
		panel_engineButtons.add(btnRemoveCustomEngine);
			
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		btnApply = new JButton("Apply");
		btnApply.setPreferredSize(new Dimension(90, 30));
		btnApply.addActionListener(this);
		panel.add(btnApply);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setPreferredSize(new Dimension(90, 30));
		btnCancel.addActionListener(this);
		panel.add(btnCancel);
		
		setLocationByPlatform(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//OUTSIDE TAB BUTTONS
		if(e.getSource().equals(btnApply))
		{
			original.pathsToSearchForPresets = pathsToSearchForPresets;
			original.pathsToSearchForWads = pathsToSearchForWads;
			original.customEngines = customEngines;
			original.alertIncompEngine = chckbxAlertWhenLaunching.isSelected();
			original.showAuxConsole = chckbxShowAConsole.isSelected();
			
			if(original.useOSLF != chckbxUseSystemLook.isSelected())
			{
				try
				{
					if(chckbxUseSystemLook.isSelected())
						UIManager2.setLookAndFeelByName("System");
					else
						UIManager2.setLookAndFeel(UIManager2.getCrossPlatformLookAndFeelClassName());

					SwingUtilities.updateComponentTreeUI(this.getOwner());
					original.useOSLF = chckbxUseSystemLook.isSelected();
				} 
				catch(Exception exc)
				{
					JOptionPane.showMessageDialog(this, "Error changing theme:" + exc.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			if(original.useOGLgui != chckbxUseOpenglRender.isSelected())
			{
				if(chckbxUseOpenglRender.isSelected())
					System.setProperty("sun.java2d.opengl","true");
				original.useOGLgui = chckbxUseOpenglRender.isSelected();
			}
			
			dispose();
		}
		
		else if(e.getSource().equals(btnCancel))
		{
			//new MainWindow(original);
			dispose();
		}
		
		//GENERAL BUTTONS
		
		//WAD/PK3 BUTTONS
		else if(e.getSource().equals(btnAddbrowseWadPk3))
		{
			JFileChooser filechooser;
			if(original.lastVisitedFolder.equals("") || !new File(original.lastVisitedFolder).isDirectory()) filechooser = new JFileChooser();
			else filechooser = new JFileChooser(original.lastVisitedFolder);
			filechooser.setMultiSelectionEnabled(true);
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if( filechooser.showDialog(this, "Choose") == JFileChooser.APPROVE_OPTION) for(File f : filechooser.getSelectedFiles())
			{
				System.out.println("adding entry: "+f.getPath());
				pathsToSearchForWads.add(f.getPath());
			}
			if(filechooser.getSelectedFile()!=null) original.lastVisitedFolder = filechooser.getSelectedFile().getParent();
		}
		
		else if(e.getSource().equals(btnAddEntryWadPk3))
		{
			String str = JOptionPane.showInputDialog("Input directory:");
			if(str==null); //JOptionPane.showMessageDialog(this, "Nothing typed...");
			else if(str.equals("")) JOptionPane.showMessageDialog(this, "Empty name given, no directory understood...");
			else if(new File(str).isDirectory())
			{
				pathsToSearchForWads.add(str);
				//refreshPresetList();
			}
			else JOptionPane.showMessageDialog(this, "Given path is not a diretory...");
		}
		
		else if(e.getSource().equals(btnEditWadPk3) && wadPk3JList.getSelectedValue()!=null)
		{
			String str = JOptionPane.showInputDialog(this, "Input directory:", wadPk3JList.getSelectedValue());
			if(str==null);// JOptionPane.showMessageDialog(this, "Nothing typed...");
			else if(str.equals("")) JOptionPane.showMessageDialog(this, "Empty name given, no directory understood...");
			else if(new File(str).isDirectory())
			{
				pathsToSearchForWads.set(wadPk3JList.getSelectedIndex(), str);
				//refreshPresetList();
			}
			else JOptionPane.showMessageDialog(this, "Given path is not a diretory...");
		}
		
		else if(e.getSource().equals(btnRemoveWadPk3) && wadPk3JList.getSelectedValue()!=null)
		{
			pathsToSearchForWads.remove(wadPk3JList.getSelectedIndex());
		}
		
		//PRESETS BUTTONS
		else if(e.getSource().equals(btnAddbrowsePresets))
		{
			JFileChooser filechooser;
			if(original.lastVisitedFolder.equals("") || !new File(original.lastVisitedFolder).isDirectory()) filechooser = new JFileChooser();
			else filechooser = new JFileChooser(original.lastVisitedFolder);
			filechooser.setMultiSelectionEnabled(true);
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if( filechooser.showDialog(this, "Choose") == JFileChooser.APPROVE_OPTION) for(File f : filechooser.getSelectedFiles())
			{
				System.out.println("adding entry: "+f.getPath());
				pathsToSearchForPresets.add(f.getPath());
			}
			if(filechooser.getSelectedFile()!=null) original.lastVisitedFolder = filechooser.getSelectedFile().getParent();
		}
		
		else if(e.getSource().equals(btnAddEntryPresets))
		{
			String str = JOptionPane.showInputDialog("Input directory:");
			if(str==null);// JOptionPane.showMessageDialog(this, "Nothing typed...");
			else if(str.equals("")) JOptionPane.showMessageDialog(this, "Empty name given, no directory understood...");
			else if(new File(str).isDirectory())
			{
				pathsToSearchForPresets.add(str);
				//refreshPresetList();
			}
			else JOptionPane.showMessageDialog(this, "Given path is not a diretory...");
		}
		
		else if(e.getSource().equals(btnEditPresets) && presetsJList.getSelectedValue()!=null)
		{
			String str = JOptionPane.showInputDialog(this, "Input directory:", presetsJList.getSelectedValue());
			if(str==null);// JOptionPane.showMessageDialog(this, "Nothing typed...");
			else if(str.equals("")) JOptionPane.showMessageDialog(this, "Empty name given, no directory understood...");
			else if(new File(str).isDirectory())
			{
				pathsToSearchForPresets.set(presetsJList.getSelectedIndex(), str);
				//refreshPresetList();
			}
			else JOptionPane.showMessageDialog(this, "Given path is not a diretory...");
		}
		
		else if(e.getSource().equals(btnRemovePresets) && presetsJList.getSelectedValue()!=null)
		{
			pathsToSearchForPresets.remove(presetsJList.getSelectedIndex());
		}
		
		//CUSTOM ENGINES BUTTONS
		else if(e.getSource().equals(btnAddCustomEngine))
		{
			EngineDialog dialog = new EngineDialog(this);
			if (dialog.resultingInfo != null) customEngines.add(dialog.resultingInfo);
		}
		else if(e.getSource().equals(btnEditCustomEngine) && customEnginesJlist.getSelectedValue()!=null)
		{
			EngineDialog dialog = new EngineDialog(this, (EngineInfo) customEnginesJlist.getSelectedValue() );
			if (dialog.resultingInfo != null) customEngines.set( customEnginesJlist.getSelectedIndex(), dialog.resultingInfo);
		}
		else if(e.getSource().equals(btnRemoveCustomEngine) && customEnginesJlist.getSelectedValue()!=null)
		{
			customEngines.remove(customEnginesJlist.getSelectedIndex());
		}
		
		wadPk3JList.setListData(pathsToSearchForWads);
		presetsJList.setListData(pathsToSearchForPresets);
		customEnginesJlist.setListData(customEngines);
	}
}
