
package cpu_scheduler;

/**
 * Contem os dados de um processo.
 * 
 */
public class PCB extends AbstractPCB 
{
    private int pid;
    private int arrayPosition;
    private int currentPipelineCycle;
    private int tempoDeEspera;
    private int state = PipelineI.PROCESS_STATE_NOT_CREATED;
    int[] dados;

    public PCB() 
    {
        this.pid = PCB.pidNumber++;
        this.arrayPosition = 1;
        this.currentPipelineCycle = 0;
        this.tempoDeEspera = 0;
    }

    public PCB(int state) 
    {
        this.pid = PCB.pidNumber++;
        this.state = state;
        this.arrayPosition = 1;
        this.currentPipelineCycle = 0;
    }
    
    /**
     * Obtem o ultimo ciclo em que ouve actividade no processo.
     * @return 
     */
    public int getCurrentPipelineCycle() 
    {
        return currentPipelineCycle;
    }

    /**
     * Atribui o ultimo ciclo em que ouve actividade no processo.
     * @param currentPipelineCycle 
     */
    public void setCurrentPipelineCycle(int currentPipelineCycle) 
    {
        this.currentPipelineCycle = currentPipelineCycle;
    }

    /**
     * Obtem o tempo de espera no estado ready.
     * @return 
     */
    public int getTempoDeEspera() 
    {
        return tempoDeEspera;
    }

    /**
     * Atribui o tempo de espera no estado ready.
     * @param tempoDeEspera 
     */
    public void setTempoDeEspera(int tempoDeEspera) 
    {
        this.tempoDeEspera = tempoDeEspera;
    }
    
    /**
     * Obtem o id do processo.
     * @return 
     */
    @Override
    public int getPID() 
    {
        return pid;
    }

    /**
     * Verifica se o processonão contem mais dados a serem processados.
     * @return 
     */
    public boolean isFinished()
    {
        return arrayPosition >= dados.length;    
    }
    
    /**
     * Obtem o estado do processo.
     * @return 
     */
    @Override
    public int getState() 
    {
        return this.state;
    }

    /**
     * Atribui o estado do processo.
     * @param value 
     */
    @Override
    public void setState(int value) 
    {
        this.state = value;
    }

    /**
     * Obtem os dados a serem processados.
     * @return 
     */
    public int[] getDados()
    {
        return this.dados;
    }

    /**
     * Atribui os dados a serem processados.
     * @param dados 
     */
    public void setDados(int[] dados)
    {
        this.dados = dados;
    }

    /**
     * Obtem o tempo em que o processo e criado.
     * @return 
     */
    public int getCreationTime() 
    {
        return dados[0];
    }

    /**
     * Atribui o tempo em que o processo e criado.
     * @param creationTime 
     */
    public void setCreationTime(int creationTime) 
    {
        this.dados[0] = creationTime;
    }

    /**
     * Obtem os ciclos que precisam ainda de ser processados pelo cpu.
     * @return 
     */
    public int getCurrentCycles()
    {
        return dados[arrayPosition];
    }
    
    /**
     * Obtem os dados seguintes a serem processados.
     */
    public void nextPCBData()
    {
        arrayPosition++;
    }

    /**
     * Reduz a quantidade de ciclos que precisam de ser processados pelo cpu.
     * @throws ProcessException 
     */
    public void decreaseRemainingCycles() throws ProcessException
    {
        if(dados[arrayPosition] == 0) { throw new ProcessException(ProcessException.PROCESS_EXCEPTION_RUNTIME_ERROR); }
        dados[arrayPosition]--;
    }
    
    /**
     * Verifica se o pcb contem dados de cpu actualmente.
     * @return 
     */
    public boolean hasCPUData()
    {
       return isOdd(arrayPosition);
    }
    
    /**
     * Verifica se o pcb contem dados de um dispositivo actualmente.
     * @return 
     */
    public boolean hasDeviceData()
    {
        return !(isOdd(arrayPosition));
    }

    /**
     * Obtem o id do dispositivo.
     * @return
     * @throws Exception 
     */
    public int getDevice() throws Exception
    {
        if(!hasDeviceData()) { throw new Exception(); }
            
        return dados[arrayPosition];
    }

    /**
     * Verifica se é par ou impar.
     * @param i
     * @return 
     */
    public boolean isOdd(int i)
    {
        return i % 2 != 0 ? true : false;
    }

    @Override
    public String toString()
    {
        String result = "PID:"+this.pid+"\narrayPosition:"+this.arrayPosition+
                        "\ncreationTime:"+dados[0]+"\nState:"+this.state+"\nDados: ";
        for(int i = 0; i< dados.length; i++){
                result+= dados[i]+", ";
        }
        return result+"\n";
    }
}
