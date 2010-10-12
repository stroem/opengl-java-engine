package engine.example;

import engine.util.*;
import engine.opengl.*;
import engine.core.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class TerrainTest extends BasicGameState
{	
	Terrain terrain = null;
	
	public TerrainTest()
	{
		
	}
	
	public void init()
	{
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		Artist.enabled3D();
		
		//GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
		//GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
		
		initObjects();
	}
	
	public void initObjects()
	{
		//Fog fog = new Fog(new Vector3f(0.5f, 0.5f, 0.5f, 1.0f), 1.0f, 20.0f);
		//addObject(fog);
		
		Light light = new Light();
		light.setAmbientColor(new Vector3f(0.2f, 0.2f, 0.2f, 1.0f));
		light.setLight(GL11.GL_LIGHT0, new Vector3f(4.0f, 5.0f, -6.0f, 1.0f), new Vector3f(0.6f, 0.6f, 0.6f, 1.0f));
		
		//terrain = new Terrain("img/heightmap.png", 1.3f, 0.7f, 0.1f);
		//terrain = new Terrain("img/article_terraintexture01.jpg", 1.3f, 0.7f, 0.1f);
		//terrain = new Terrain("img/Heightmap2.png", 1.3f, 0.4f, 0.4f);
		terrain = new Terrain("img/GF_Height_Map.jpg", 1.3f, 0.7f, 0.1f);
		addObject(terrain);
	}
	
	public void render()
	{
		//System.out.println(FPS.get());
	}
	
	public void keyPressed()
	{
		if(terrain == null)
			return;
			
		int keycode = Keyboard.getEventKey();
		switch(keycode)
		{
			case Keyboard.KEY_UP:
				terrain.setHeightRatio(terrain.getHeightRatio() + 0.05f);
				break;
				
			case Keyboard.KEY_DOWN:
				terrain.setHeightRatio(terrain.getHeightRatio() - 0.05f);
				break;
				
			case Keyboard.KEY_LEFT:
				terrain.setFalloutRatio(terrain.getFalloutRatio() - 0.05f);
				break;
				
			case Keyboard.KEY_RIGHT:
				terrain.setFalloutRatio(terrain.getFalloutRatio() + 0.05f);
				break;
		}
		
		System.out.println("FalloutRatio: " + terrain.getFalloutRatio() + ", HeightRatio: " + terrain.getHeightRatio());
	}
}
