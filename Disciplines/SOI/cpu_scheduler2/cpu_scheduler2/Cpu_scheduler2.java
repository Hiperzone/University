
package cpu_scheduler2;


import java.util.logging.Level;
import java.util.logging.Logger;


public class Cpu_scheduler2 
{
	
    public static boolean running = true;
    public static Pipeline pipeline;
    
    public static int PRINT_MODE_NORMAL = 0;
    public static int PRINT_MODE_DEBUG = 1;
    public static int PRINT_MODE_SPECIAL_DEBUG = 2;
    
    public static int printMode = PRINT_MODE_NORMAL;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
  		
        try
        {
            pipeline = new Pipeline();
            FileInput file = new FileInput("dados.cpu");
            file.carregarDados();
            
            //executar a logica da pipeline
            while(running)
            {
                pipeline.run();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger( Cpu_scheduler2.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
}
