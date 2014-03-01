
package cpu_scheduler;
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
    public static int READY_STAGE_PROCESS_LIMIT = 1;
    public static int numProcesses = 0;
    public static int numDevices = 0;
    private int numCycles = 0;
    private LinkedList<PCB> preProcessCreationQueue;
    public static PipelineStage[] pipelineStages;
    
    
    public Pipeline()
    {
        preProcessCreationQueue = new LinkedList<PCB>();
        
            
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
                pipelineStages[Pipeline.PIPELINE_STAGE_NEW].addProcessToQueue(pcb);
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
        pipelineStages[PipelineI.PIPELINE_STAGE_READY].update(numCycles);
        pipelineStages[PipelineI.PIPELINE_STAGE_RUN].update(numCycles);
        pipelineStages[PipelineI.PIPELINE_STAGE_EXIT].update(numCycles);   

        System.out.print(this);
        numCycles++;
        //output dos processos por ciclo.
       
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
         output += "-------------------------------";
        return output;
    }
}
