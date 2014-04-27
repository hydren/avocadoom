package util;

import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public abstract class GUIHandler 
{
	public static void setLookAndFeel(String lookAndFeelName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{		
		if(lookAndFeelName.equalsIgnoreCase("System"))
		{
			String themeClassName = UIManager.getSystemLookAndFeelClassName();

			if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
			{
				for(LookAndFeelInfo lfi : UIManager.getInstalledLookAndFeels())
				{
					if(lfi.getName().equalsIgnoreCase("GTK+"))
					{
						themeClassName = lfi.getClassName();
						break;
					}
				}
			}
			UIManager.setLookAndFeel(themeClassName);
		}
		
		else 
		{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			if(info.getName().equals(lookAndFeelName))
			{
				UIManager.setLookAndFeel(info.getClassName());
				return;
			}
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			throw new UnsupportedLookAndFeelException(lookAndFeelName + " does not match any available theme. Using default theme instead.");
		}
	}
	
	public static Vector<String> getAvailableLookAndFeelNames()
	{
		Vector<String> list = new Vector<String>();
		list.add("System");
		for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			list.add(info.getName());
		
		return list;
	}
	
	public static String getDefaultLookAndFeelName()
	{
		for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			if(info.getClassName().equals(UIManager.getCrossPlatformLookAndFeelClassName()))
				return info.getName();
		
		return null;
	}
	
	/** Apply the OS L&F */
	public static void attemptToUseOSLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
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
	}

}
