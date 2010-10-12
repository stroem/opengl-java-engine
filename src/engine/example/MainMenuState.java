package engine.example;

import engine.util.*;
import engine.opengl.*;
import engine.core.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class MainMenuState extends BasicGameState
{
	private float angle;
	
	public MainMenuState()
	{
		
	}
	
	public void init()
	{
		Artist.enabled2D();
	}
	
	public void render()
	{
		//System.out.println(Util.getFPS());
		
		int size = 200;
		angle += 1.0f % 360;
 
		// center square according to screen size
		GL11.glPushMatrix();
		GL11.glTranslatef(Display.getDisplayMode().getWidth() / 2, Display.getDisplayMode().getHeight() / 2, 0.0f);
	 
	 		GL11.glPushMatrix();
		  // rotate square according to angle
		  GL11.glRotatef(angle, 0, 0, 1.0f);
	 
		  // render the square
		  GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(-size, -size);
			GL11.glVertex2i(size, -size);
			GL11.glVertex2i(size, size);
			GL11.glVertex2i(-size, size);
		  GL11.glEnd();
		  GL11.glPopMatrix();
		  
		GL11.glPopMatrix();
		
		//System.out.println(angle);		
	}
}
