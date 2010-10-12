package engine.core;

import engine.util.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class GameState
{
	private BasicGameState state = null;
	private int syncFPS = 60;
	private int syncInactiveSleep = 100;
	private boolean running = true;
	
	public void enterState(BasicGameState state)
	{
		state.loading();
		state.init();
		this.state = state;
	}
	
	private void setActiveFPS(int fps)
	{
		this.syncFPS = fps;
	}
	
	private void setInactiveFPS(int fps)
	{
		this.syncInactiveSleep = 1000 / fps;
	}
	
	private void renderState()
	{
		if(this.state != null)
		{
			BasicGameState newState = this.state.getNextState();
			if(newState != null)
			{
				enterState(newState);
			}
			else
			{
				this.state.renderObjects();
				this.state.render();
			}
		}
		else
			System.err.println("You need to enter a state, throught: GameState.enterState(BasicGameState)");
	}
	
	public void runGameLoop()
		throws Exception
	{
		GL11.glClearColor(0, 0, 0, 0);
		while(!Display.isCloseRequested() && running)
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
		
			FPS.update();
			updateKeys();
			
			if(Display.isActive())
			{
				renderState();
				Display.sync(this.syncFPS);
			} 
			else
			{
				try
				{
					Thread.sleep(this.syncInactiveSleep);
				} 
				catch (InterruptedException e) {}

				if((Display.isVisible() || Display.isDirty()) && this.state != null)
					this.state.inactive();
			}
			
			Display.update();
		}
		
		Display.destroy();
	}
	
	private void updateKeys()
	{
		if(this.state == null)
			return;
			
		Keyboard.poll();
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			this.running = false;
			
		while(Keyboard.next())
		{
			if(Keyboard.getEventKeyState())
				this.state.keyPressed();
			else
				this.state.keyReleased();
		}
	}
}
