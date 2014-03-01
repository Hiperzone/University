package cpu_scheduler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Cpu_scheduler {
    public static boolean running = true;
    public static Pipeline pipeline;


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
            Logger.getLogger(
                    Cpu_scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
