package cpu_scheduler;


/**
 * Contem metodos que permitem manipular um dispositivo. 
 *
 */
public class Device 
{
    public int numOfCycles;
    private int currentCycle;

    public Device(int time) 
    {
        numOfCycles = time;
        currentCycle = 0;
    }

    /**
     * actualiza a quantidade de ciclos que este dispositivo ja processou
     * para um processo que esta no stage blocked a espera.
     */
    public void run()
    {
        if(currentCycle <= numOfCycles)
        {
            currentCycle++;
        }
    }
    
    /**
     * Faz um reset aos ciclos do dispositivo para o valor inicial.
     */
    public void resetDeviceCycles()
    {
        currentCycle = 0;
    }
    
    /**
     * Verifica se o pedido foi concluido.
     * Neste caso o pedido e concluido quando o numero de ciclos processados
     * e igual ao numero de ciclos por pedido do dispositivo.
     * @return 
     */
    public boolean isFinished()
    {
        return currentCycle == numOfCycles;
    }
}
