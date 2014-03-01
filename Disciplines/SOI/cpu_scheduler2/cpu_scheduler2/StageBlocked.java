
package cpu_scheduler2;

import java.util.LinkedList;

/**
 * Gere os processos do stage blocked.
 *
 */
public class StageBlocked extends PipelineStage 
{    
    public static int DEVICE_CYCLES = 3;
    public static Device dev;
    public StageBlocked(int stageType)
    {
        super(stageType);
        queue = new LinkedList<PCB>();
        dev  = new Device(DEVICE_CYCLES);
    }
    
    /**
    * Actualiza e gere os processos que estao em fila de espera no stage.
    * passando-os para o stage seguinte quando for oportuno.
    * @param cycles 
    */
    @Override
    public void update(int cycles)
    {
        //percorrer as filas dos dispositivos e verificar os processos
        //que estao na frente da fila.
           //existe processos neste device
            if(queue.size() > 0)
            {
                PCB pcb = queue.getFirst();
                //processo esta no ciclo correcto
                if(pcb.getCurrentPipelineCycle() < cycles)
                {
                    
                    dev.run();
                    if(dev.isFinished())
                    {
                        pcb.setState(PipelineI.PROCESS_STATE_READY);
    			pcb.setCurrentPipelineCycle(cycles);
    			Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_READY].addProcessToQueue( queue.removeFirst() );
                        dev.resetDeviceCycles();
                    } 
                }
            }
        
    }  
}
