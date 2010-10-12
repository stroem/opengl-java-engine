package engine.opengl;

import engine.util.*;
import java.nio.*;
import org.lwjgl.opengl.*;

public class Light
{
	public Light()
	{
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	public Light(Vector3f setAmbientColor)
	{
		GL11.glEnable(GL11.GL_LIGHTING);
		setAmbientColor(setAmbientColor);
	}
	
	public void setAmbientColor(Vector3f setAmbientColor)
	{
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		
		FloatBuffer ambientColor = (FloatBuffer)temp.asFloatBuffer().put(setAmbientColor.getFloats()).flip();
		
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambientColor);
	}
	
	public void setLight(int light, Vector3f setPosition)
	{
		setLight(light, setPosition, new Vector3f(1.0f, 1.0f, 1.0f, 1.0f));
	}
	
	public void setLight(int light, Vector3f setPosition, Vector3f setColor)
	{
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		
		FloatBuffer position = (FloatBuffer)temp.asFloatBuffer().put(setPosition.getFloats()).flip();
		FloatBuffer color = (FloatBuffer)temp.asFloatBuffer().put(setColor.getFloats()).flip();
		
		GL11.glEnable(light);
		GL11.glLight(light, GL11.GL_DIFFUSE, color);
		GL11.glLight(light, GL11.GL_POSITION, position);
	}
}
