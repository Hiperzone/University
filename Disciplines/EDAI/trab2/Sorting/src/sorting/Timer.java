
package sorting;

public class Timer 
{
    private long start = 0;
    private long end = 0;
    
    public Timer()
    {
        
    }
    
    public void start()
    {
        start = System.nanoTime();
        
    }
    
    public void end()
    {
        end = System.nanoTime();
        
    }
    
    public long timeElapsed()
    {
        return (end - start);
    }
    
    @Override
    public String toString()
    {
        
        return Long.toString(timeElapsed()) + " ns";
    }
    
    public static String convertToMS(long t)
    {
       return Long.toString(t) + " ns"; 
    }
}
