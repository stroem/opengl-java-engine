package engine.opengl;

import engine.util.*;
import org.lwjgl.opengl.*;

public class Sphere extends DrawableObject
{
	protected Vector3f position;
	protected float radius;
	protected Vector3f color;
	
	public Sphere(Vector3f position, float radius)
	{
		this(position, radius, null);
	}
	
	public Sphere(Vector3f position, float radius, Vector3f color)
	{
		this.position = position;
		this.radius = radius;
		this.color = color;
	}
	
	public Vector3f getPosition()
	{
		return this.position;
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}
	
	public Vector3f getColor()
	{
		return this.color;
	}
	
	public void setColor(Vector3f color)
	{
		this.color = color;
		this.compileID = 0;
	}
	
	public float getRadius()
	{
		return this.radius;
	}
	
	public void setRadius(float Radius)
	{
		this.radius = radius;
		this.compileID = 0;
	}
	
	public void renderRealtime()
	{
		if(this.color != null)
			GL11.glColor3f(this.color.getX(), this.color.getY(), this.color.getZ());
		
		new org.lwjgl.util.glu.Sphere().draw(this.radius, 10, 10);
	}
	
	public void render()
	{
		GL11.glPushMatrix();
			GL11.glTranslatef(this.position.getX(), this.position.getY(), this.position.getZ());
		
			renderObject();
				
		GL11.glPopMatrix();
	}
}
