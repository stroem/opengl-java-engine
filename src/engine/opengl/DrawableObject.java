package engine.opengl;

import org.lwjgl.opengl.*;

public abstract class DrawableObject
{
	protected int compileID = 0;
	protected boolean done = false;
	
	public boolean isDone()
	{
		return this.done;
	}
	
	public void remove()
	{
		this.done = true;
	}
	
	public void renderObject()
	{
		if(compileID == 0)
			renderRealtime();
		else
			GL11.glCallList(compileID);
	}
	
	public void render()
	{
		renderObject();	
	}
	
	public void renderRealtime() { }
	
	public void compile()
	{
		this.compileID = GL11.glGenLists(1);
		GL11.glNewList(this.compileID, GL11.GL_COMPILE);
		
		renderRealtime();
		
		GL11.glEndList();
	}
}
