package engine.util;

public class FPS
{
	public static int fps = 0;
	
    public static  int frames = 0;
	public static  long startTick = 0;
	public static  long currentTick = 0;
	
	public static void update()
	{
		frames++;
        currentTick = System.currentTimeMillis();
		if ((currentTick - startTick) >= 1000)
		{
			fps = (int)((float) frames / (currentTick - startTick) * 1000.0f);
			frames = 0;
			startTick = currentTick;
		}
	}
	
	public static int get()
	{
		return fps;
	}
}
