package engine.opengl;

import engine.util.*;
import org.lwjgl.opengl.*;

public class Grid extends DrawableObject
{
	private float space = 1.0f;
	private float y = 0.0f;
	private float size = 100.0f;
	private int compileID = 0;
	
	public Grid(float y)
	{
		this(y, 1.0f);
	}
	
	public Grid(float y, float space)
	{
		this(y, space, 100.0f);
	}
	
	public Grid(float y, float space, float size)
	{
		this.y = y;
		this.space = space;
		this.size = size;
	}
	
	public void renderRealtime()
	{
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_LINES);
			for(float x = -this.size; x < this.size; x += this.space)
			{
				GL11.glVertex3f(x, this.y, -this.size);
				GL11.glVertex3f(x, this.y,  this.size);
			} 
			
			for(float y = -this.size; y < this.size; y += this.space)
			{
				GL11.glVertex3f(-this.size, this.y, y);
				GL11.glVertex3f( this.size, this.y, y);
			} 
		GL11.glEnd();
	}
}
