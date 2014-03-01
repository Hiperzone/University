
package cpu_scheduler;

import java.util.LinkedList;

/**
 * Gere os processos do stage run.
 *
 */
public class StageRun extends PipelineStage 
{    
    public static int CPU_CYCLES_PER_PROCESS = 4;
    private int currentProcessCyles = 0;
   
    
    public StageRun(int stageType)
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
    public void update(int cycles) throws Exception
    {
       scheduler(cycles);
    } 
    
    /**
     * CPU scheduler.
     * @param cycles
     * @throws Exception 
     */
    public void scheduler(int cycles) throws Exception
    {
        //fazer trabalho no cpu para o processo que esta na frente da fila se este
        //esta a espera ha um ciclo atras.
        if(this.queue[0].size() > 0 && queue[0].getFirst().getCurrentPipelineCycle() < cycles)
        {
            PCB process = queue[0].getFirst();
            //incrementar o tempo de cpu para este processo.
            currentProcessCyles = inc(currentProcessCyles);
            process.setCurrentPipelineCycle(cycles);
            process.decreaseRemainingCycles();
            
            //o processo ja nao tem ciclos de cpu para processar
            //envia-lo para o stage exit.
            if( process.getCurrentCycles() == 0)
            {
                process.nextPCBData();
                if(process.isFinished())
                {
                    process.setState(PipelineI.PROCESS_STATE_EXIT);
                    Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_EXIT].addProcessToQueue(queue[0].removeFirst());
                    resetCPUCycles();
                }
                else if(process.hasDeviceData())
                {
                    process.setState(PipelineI.PROCESS_STATE_BLOCKED);
                    Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_BLOCKED].addProcessToQueue(queue[0].removeFirst(), process.getDevice());
                    resetCPUCycles();
                }      
            }
            //numero de ciclos de cpu para este processo chegou ao limite
            //envia-lo para o stage ready.
            else if(currentProcessCyles == CPU_CYCLES_PER_PROCESS)
            {
                resetCPUCycles();
                process.setState(PipelineI.PROCESS_STATE_READY);
                Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_READY].addProcessToQueue(queue[0].removeFirst());
            }
            else
            {
                //nao fazer nada porque o processo ainda tem ciclos de cpu por processar.
            }
        }
    }
        
    public int inc(int i)
    {
        return i + 1 % CPU_CYCLES_PER_PROCESS;
    }
    
    /**
     * Faz um reset aos ciclos do cpu.
     */
    public void resetCPUCycles()
    {
        currentProcessCyles = 0;
    }
}
