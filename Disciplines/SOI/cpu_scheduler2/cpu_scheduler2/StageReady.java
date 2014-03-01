
package cpu_scheduler2;

import java.util.LinkedList;

/**
 * Gere os processos do stage ready.
 *
 */
public class StageReady extends PipelineStage 
{   
    public StageReady(int stageType)
    {
        super(stageType);
        queue = new LinkedList<PCB>();
    }
    
    /**
     * Actualiza e gere os processos que estao em fila de espera no stage.
     * passando-os para o stage seguinte quando for oportuno.
     * @param cycles 
     */
    @Override
    public void update(int cycles)
    {
        //actualizar o tempo de espera de cada processo.
        for(PCB pcb : queue)
        {
            if(pcb.getCurrentPipelineCycle() < cycles)
            {
                pcb.setTempoDeEspera( pcb.getTempoDeEspera() + 1);
            }
        }
        
        //existe processos no estado ready, passar um processo para o cpu
        if(queue.size() > 0 && queue.get(0).getCurrentPipelineCycle() < cycles && 
                Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_RUN].getQueueSize() == 0)
        {
            PCB process = queue.removeFirst();
            process.setState(PipelineI.PROCESS_STATE_RUN);
            process.setCurrentPipelineCycle(cycles);
            Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_RUN].addProcessToQueue(process);
        }  
    } 
}
