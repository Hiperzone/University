
package cpu_scheduler;

import java.util.LinkedList;

/**
 * Gere os processos do stage new.
 *
 */
public class StageNew extends PipelineStage 
{
    public StageNew(int stageType)
    {
        super(stageType);
        queue = new LinkedList[1];
        queue[0] = new LinkedList<PCB>();
    }
    
    /**
     * Actualiza e gere os processos que estao em fila de espera no stage.
     * passando-os para o stage seguinte quando for oportuno.
     * @param cycles 
     */
    @Override
    public void update(int cycles)
    {
        //existe processos
        //adicionar processos do stage new ao stage ready
        //se tiver 2 ou menos processos na queue do stage ready.
        if(queue[0].size() > 0 && Cpu_scheduler.pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_READY].getQueueSize() <= Pipeline.READY_STAGE_PROCESS_LIMIT)
        {
            //apenas avancar o processo se for o ciclo seguinte
            if(queue[0].getFirst().getCurrentPipelineCycle() < cycles)
            {
                PCB Process = queue[0].removeFirst();
                Process.setCurrentPipelineCycle(cycles);
                Process.setState(PipelineI.PROCESS_STATE_READY);
                Cpu_scheduler.pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_READY].addProcessToQueue( Process );
                Process = null;
            }
        }      
     } 
}
