package engine.core;

import engine.opengl.*;
import java.awt.*;
import java.util.*;

public abstract class BasicGameState
{
	protected long timeLoading = 0L;
	protected BasicGameState nextState = null;
	
	public abstract void init();
	
	public abstract void render();
	
	public void keyPressed() { }
	public void keyReleased() { }
	
	public void loading()
	{
		this.timeLoading = System.currentTimeMillis();
	}
	
	public void inactive()
	{
		renderObjects();
		render();	
	}
	
	public BasicGameState getNextState()
	{
		if(this.timeLoading != 0L)
		{
			System.err.println("init(): " + (System.currentTimeMillis() - this.timeLoading) + " ms");
			this.timeLoading = 0L;
		}
		
		BasicGameState state = this.nextState;
		this.nextState = null;
		return state;	
	}
	
	public void setNextState(BasicGameState newState)
	{
		this.nextState = newState;
	}
	
	//Drawable
	protected ArrayList<DrawableObject> objects = new ArrayList<DrawableObject>();
	
	public synchronized void addObject(DrawableObject o)
	{
		objects.add(o);
	}
	
	public synchronized void removeObject(DrawableObject o)
	{
		objects.remove(o);
	}
	
	public synchronized void renderObjects()
	{
		Iterator itr = objects.iterator();
		while(itr.hasNext())
		{
			DrawableObject obj = (DrawableObject) itr.next();
			if(obj.isDone())
				itr.remove();
			else
				obj.render();
		}
	}
}
