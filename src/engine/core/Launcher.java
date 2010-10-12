package engine.core;

import java.io.*;
import javax.swing.*;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;


/**
 * Class Lofy
 * Makes a JFrame and change between different Canvases.
 */
public class Launcher extends GameState
{	
	private static DisplayMode displayMode = null;
	
	public Launcher()
		throws Exception
	{
		initFrame("Untitled", 640, 480, false);
	}
	
	public Launcher(String title)
		throws Exception
	{
		initFrame(title, 640, 480, false);
	}
	
	public Launcher(String title, int width, int height)
		throws Exception
	{
		initFrame(title, width, height, false);
	}
	
	public Launcher(String title, int width, int height, boolean fullscreen)
		throws Exception
	{
		initFrame(title, width, height, fullscreen);
	}
	
	public static DisplayMode getDisplay()
	{
		return displayMode;
	}
	
	public static void setDisplay(DisplayMode mode)
	{
		displayMode = mode;
	}
	
	private void initFrame(String title, int width, int height, boolean fullscreen)
		throws Exception
	{
		try
		{
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			for(int i = 0; i < modes.length; i++)
			{
				if(modes[i].getWidth() == width && modes[i].getHeight() == height)
				{
					Launcher.setDisplay(modes[i]);
					break;
				}
			}
		}
		catch(LWJGLException e)
		{
			Sys.alert("Error", "Unable to determine display modes.");
			System.exit(0);
		}

		if(displayMode == null)
		{
			Sys.alert("Error", "Unable to find appropriate display mode.");
			System.exit(0);
		}

		try
		{
			Display.setTitle(title);
			Display.setFullscreen(fullscreen);
			Display.setDisplayMode(Launcher.getDisplay());
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch(LWJGLException e)
		{
			Sys.alert("Error", "Unable to create display");
			System.exit(0);
		}
	}
}
