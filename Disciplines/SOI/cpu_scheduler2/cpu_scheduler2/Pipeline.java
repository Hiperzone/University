
package cpu_scheduler2;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe responsavel por actualizar os varios stages e
 * incrementar o numero de ciclos.
 *
 */
public class Pipeline implements PipelineI
{
    public static int READY_STAGE_PROCESS_LIMIT = 2;
    public static int numProcesses = 0;
    private int numCycles = 0;
    private LinkedList<PCB> preProcessCreationQueue;
    public static PipelineStage[] pipelineStages;
    
    public Pipeline()
    {
        preProcessCreationQueue = new LinkedList<PCB>();
        
        //gestao de espaco livre em memoria
        MemBlock memblock = new MemBlock();
        memblock.endAddress = 299;
        memblock.memPoolSize = 300;
        MemFactory.getInstance();
        MemFactory.getInstance().getMemBlock().add(memblock);
        
        
        //inicializar os diferentes estados do pipeline.
        //array com todos os stages com listas para as queues.
        pipelineStages = new PipelineStage[5];
        pipelineStages[0] = new StageNew(PipelineI.PIPELINE_STAGE_NEW);
        pipelineStages[1] = new StageReady(PipelineI.PIPELINE_STAGE_READY) ;
        pipelineStages[2] = new StageRun(PipelineI.PIPELINE_STAGE_RUN) ;
        pipelineStages[3] = new StageBlocked(PipelineI.PIPELINE_STAGE_BLOCKED);
        pipelineStages[4] = new StageExit(PipelineI.PIPELINE_STAGE_EXIT) ;
    }
    
    /**
     * Adiciona um processo para ser admitido.
     * @param pcb
     */
    public void queueProcessCreation(PCB pcb)
    {
        preProcessCreationQueue.add(pcb);
    }
    
    /**
     * Obtem a queue de processos que precisam de ser admitidos.
     * @return
     */
    LinkedList<PCB> getQueue()
    {
        return preProcessCreationQueue;
    }
    
    /**
     * Permite executar a logica da pipeline.
     * @throws Exception
     */
    @Override
    public void run() throws Exception
    {
        //admissao de processos para o stage new
        for(Iterator<PCB> it = preProcessCreationQueue.iterator(); it.hasNext();)
        {
            PCB pcb = it.next();
            if(pcb.getCreationTime() == numCycles)
            {
                pcb.setState(PipelineI.PROCESS_STATE_NEW);
                pcb.setCurrentPipelineCycle(numCycles);
                
                //alocar memoria.
                int data[] = FileInput.getDadosOfProcess( pcb.getDataid());
                int address = MemFactory.getInstance().memAlloc(data.length + 10);
                
                MemFactory.getInstance().memCopy(address + MemFactory.MEM_VARIABLE_SIZE, data);
                pcb.setBaseVarAddress(address);
                pcb.setBaseAddress( address + MemFactory.MEM_VARIABLE_SIZE);
                pcb.setPC(address + MemFactory.MEM_VARIABLE_SIZE);
                pcb.setAllocatedMemSize(data.length + MemFactory.MEM_VARIABLE_SIZE);
                
                pipelineStages[Pipeline.PIPELINE_STAGE_NEW].addProcessToQueue(pcb);
                
                if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_NORMAL)
                	System.out.print("Inicio do processo "+pcb.getPID()+", "+MemFactory.getInstance()+"\n");
                
                it.remove();
            }
        }
        
        //actualizar cada stage com prioridades baseado nos processos que entram
        //no stage ready
        //BLOCKED -> NEW -> RUN
        //dar prioridade aos processos que estao nos dispositivos que queiram
        //passar para o stage ready
        //os processos que vem do stage new tem prioridade sobre os processos
        //que estao no CPU
        //os processos que saiem do CPU passam para o stage ready quando der
        //timeout.
        
        pipelineStages[PipelineI.PIPELINE_STAGE_BLOCKED].update(numCycles);
        pipelineStages[PipelineI.PIPELINE_STAGE_NEW].update(numCycles);
        pipelineStages[PipelineI.PIPELINE_STAGE_RUN].update(numCycles);
        pipelineStages[PipelineI.PIPELINE_STAGE_READY].update(numCycles);
        pipelineStages[PipelineI.PIPELINE_STAGE_EXIT].update(numCycles);
        
        if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_SPECIAL_DEBUG)
        {
            System.out.print(this);
            System.out.print(MemFactory.getInstance());
        }
        if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_DEBUG)
        {
            if(numCycles < 10)
            {
                System.out.println(MemFactory.getInstance());
            }
        }
        numCycles++;
    }
    
    @Override
    public String toString()
    {
        String output = "-------------------------------\n";
        output += "Ciclo: " + numCycles + "\n";
        //output dos estados de cada processo.
        for(int i = 0; i < Pipeline.pipelineStages.length; i++)
        {
            output += Pipeline.pipelineStages[i].outputProcessStates();
        }
        //output do tempo de espera
        //output dos estados de cada processo.
        for(int i = 0; i < Pipeline.pipelineStages.length; i++)
        {
            output += Pipeline.pipelineStages[i].outputProcessWaitTime();
        }
        output += "-------------------------------\n";
        return output;
    }
}
