package engine.opengl;

import engine.util.*;
import java.nio.*;
import org.lwjgl.opengl.*;

public class Fog extends DrawableObject
{
	private Vector3f color = null;
	
	public Fog(boolean modeExp, Vector3f color, float density)
	{
		this.color = color;
		setExp(modeExp, color, density);
	}
	
	public Fog(Vector3f color, float start, float end)
	{
		this.color = color;
		setLinear(color, start, end);
	}
	
	public void setColor(Vector3f setColor)
	{
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		
		FloatBuffer color = (FloatBuffer)temp.asFloatBuffer().put(setColor.getFloats()).flip();
		
		GL11.glClearColor(setColor.getX(), setColor.getY(), setColor.getZ(), setColor.getAlpha());
		GL11.glFog(GL11.GL_FOG_COLOR, color);
	}
	
	public void setLinear(Vector3f color, float start, float end)
	{
		setColor(color);
		
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
		GL11.glFogf(GL11.GL_FOG_START, start);
		GL11.glFogf(GL11.GL_FOG_END, end);
	}
	
	public void setExp(boolean modeExp, Vector3f color, float density)
	{
		setColor(color);
		
		GL11.glFogi(GL11.GL_FOG_MODE, (modeExp ? GL11.GL_EXP : GL11.GL_EXP2));
		GL11.glFogf(GL11.GL_FOG_DENSITY, density);
	}
	
	public void render()
	{
		GL11.glEnable(GL11.GL_FOG);
		GL11.glClearColor(color.getX(), color.getY(), color.getZ(), color.getAlpha());
	}
}
