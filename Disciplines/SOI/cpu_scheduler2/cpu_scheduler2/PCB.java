
package cpu_scheduler2;

/**
 * Contem os dados de um processo.
 *
 */
public class PCB extends AbstractPCB
{
    private int pid;
    private int dataid;
    private int PC;
    private int creationTime;
    private int baseAddress;
    private int baseVarAddress;
    private int allocatedMemSize;
    private int currentPipelineCycle;
    private int tempoDeEspera;
    private int state = PipelineI.PROCESS_STATE_NOT_CREATED;

    public PCB()
    {
        this.pid = PCB.pidNumber++;
        this.currentPipelineCycle = 0;
        this.tempoDeEspera = 0;
        this.creationTime = 0;
        this.PC = 0;
        this.baseAddress = 0;
        this.baseVarAddress = 0;
        this.allocatedMemSize = 0;
        this.dataid = 0;
    }

    public PCB(int state)
    {
        this.pid = PCB.pidNumber++;
        this.state = state;
        this.currentPipelineCycle = 0;
        this.creationTime = 0;
        this.PC = 0;
        this.baseAddress = 0;
        this.tempoDeEspera = 0;
    }

    /**
     * O novo processo criado pela funcao fork e chamado de processo filho.
     * Todas as variaveis do processo sao duplicadas dentro do sistema 
     * operativo.
     * @return
     */
    public PCB fork()
    {
        // Aloca memoria para o que resta a ser processado
        int newPosition = 0;
        int diff = this.PC - this.baseAddress;
        int newSize = this.allocatedMemSize - diff;
        try 
        {
            newPosition = MemFactory.getInstance().memAlloc(newSize);
        } 
        catch (ProcessException e) 
        {
            e.printStackTrace();
        }
        // Cria o novo processo
        PCB newProcess = new PCB(PipelineI.PROCESS_STATE_NEW);
        newProcess.setBaseVarAddress(newPosition);
        newProcess.setBaseAddress(newPosition + MemFactory.MEM_VARIABLE_SIZE);
        newProcess.setPC(newProcess.getBaseAddress());
        newProcess.setAllocatedMemSize(newSize);

        // Copia as variaveis
        MemFactory.getInstance().copyToAddress(
                                       this.baseVarAddress,
                                       newProcess.getBaseVarAddress(),
                                       MemFactory.MEM_VARIABLE_SIZE
                                       );
        // Copia os dados
        MemFactory.getInstance().copyToAddress(this.PC, newProcess.getBaseAddress(), newSize - MemFactory.MEM_VARIABLE_SIZE);

        return newProcess;
    }

    /**
     * Retorna o tamanho de memoria alocada.
     * @return 
     */
    public int getAllocatedMemSize()
    {
            return allocatedMemSize;
    }

    /**
     * Atribui o espaco ocupado em memoria para este processo.
     * @param allocatedMemSize 
     */
    public void setAllocatedMemSize(int allocatedMemSize)
    {
            this.allocatedMemSize = allocatedMemSize;
    }

    /**
     * Obtem o id da posicao da array que contem os dados com as instrucoes
     * deste processo para processamento.
     * @return 
     */
    public int getDataid()
    {
            return dataid;
    }

    /**
     * Atribui o id da posicao da array que contem os dados com as
     * instrucoes deste processo para processamento.
     * @param dataid 
     */
    public void setDataid(int dataid)
    {
            this.dataid = dataid;
    }

    /**
     * Contem o endereço de memória onde começa a primeira variável.
     * @return 
     */
    public int getBaseVarAddress()
    {
            return baseVarAddress;
    }

    /**
     * Atribui o endereço de memória onde começa a primeira variável.
     * @param baseVarAddress 
     */
    public void setBaseVarAddress(int baseVarAddress)
    {
            this.baseVarAddress = baseVarAddress;
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
     * Obtem o tempo em que o processo e criado.
     * @return
     */
    public int getCreationTime()
    {
            return creationTime;
    }

    /**
     * Atribui o tempo em que o processo e criado.
     * @param creationTime
     */
    public void setCreationTime(int creationTime)
    {
            this.creationTime = creationTime;
    }

    /**
     * Retorna o valor do Program Counter.
     * @return
     */
    public int getPC()
    {
            return PC;
    }

    /**
     * Atribui um endereco de memoria ao Program Counter.
     * @param PC
     */
    public void setPC(int PC)
    {
            assert(PC < baseAddress || PC >= this.allocatedMemSize);
            this.PC = PC;
    }

    /**
     * Obtem o endereco de memoria base do programa.
     * @return
     */
    public int getBaseAddress()
    {
            return baseAddress;
    }

    /**
     * Atribui o endereco de memoria base do programa.
     * @param baseAddress
     */
    public void setBaseAddress(int baseAddress)
    {
            this.baseAddress = baseAddress;
    }

    @Override
    public String toString()
    {
            return new String();
    }
}
