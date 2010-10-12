package engine;

import engine.core.*;
import engine.example.*;
import org.lwjgl.*;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			Launcher launcher = new Launcher("Demo", 1024, 768);
			launcher.enterState(new CollisionPointTriangle());//SphereSphere());
			launcher.runGameLoop();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Sys.alert("Error", "An error occured and the game will exit.\n\nStacktrace:\n" + e.toString());
		}
	}
}
