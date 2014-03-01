
package cpu_scheduler;

import java.util.LinkedList;

/**
 * Gere os processos do stage blocked.
 *
 */
public class StageBlocked extends PipelineStage 
{    
    public static LinkedList<Device> devices;
    public StageBlocked(int stageType)
    {
        super(stageType);
    }
    
    public void setupDevices()
    {
        queue = new LinkedList[Pipeline.numDevices];
        for(int i = 0; i < queue.length; i++)
        {
            queue[i] = new LinkedList<PCB>();
        }
        //criar a lista de dispositivos
        devices = new LinkedList<Device>();
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
        for(int i = 0; i < queue.length; i++)
        {
            //existe processos neste device
            if(queue[i].size() > 0)
            {
                PCB pcb = queue[i].getFirst();
                //processo esta no ciclo correcto
                if(pcb.getCurrentPipelineCycle() < cycles)
                {
                    Device dev = devices.get(i);
                    dev.run();
                    if(dev.isFinished())
                    {
                        pcb.setState(PipelineI.PROCESS_STATE_READY);
    			pcb.setCurrentPipelineCycle(cycles);
                        pcb.nextPCBData();
    			Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_READY].addProcessToQueue( queue[i].removeFirst() );
                        dev.resetDeviceCycles();
                    } 
                }
            }
        }
    }  
}
