package engine.util;

public class Timing
{
	public static long start = 0L;
	public static long end = 0L;
	
	public static void start()
	{
		Timing.start = System.currentTimeMillis();
	}
	
	public static void end()
	{
		Timing.end = System.currentTimeMillis();
		System.out.println("Timing: " + (Timing.end - Timing.start) + " ms");
	}
}
