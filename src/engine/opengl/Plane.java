package engine.opengl;

import engine.util.*;
import org.lwjgl.opengl.*;

public class Plane extends DrawableObject
{
	private float size = 0;
	
	public Plane(Vector3f position, float size)
	{
		this.size = size;
	}
	
	public float getSize()
	{
		return this.size;
	}
	
	public void setSize(float size)
	{
		this.size = size;
		this.compileID = 0;
	}
	
	public void renderRealtime()
	{
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		
			GL11.glNormal3f(0.0f, 1.0f, 0.0f);
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-this.size / 2, -this.size / 2, -this.size / 2);
			GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(-this.size / 2, -this.size / 2, this.size / 2);
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(this.size / 2, -this.size / 2, this.size / 2);
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(this.size / 2, -this.size / 2, -this.size / 2);
	
		GL11.glEnd();
	}
}
