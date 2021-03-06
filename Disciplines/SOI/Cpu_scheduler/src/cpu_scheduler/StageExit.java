
package cpu_scheduler;

import java.util.LinkedList;

/**
 * Gere os processos do stage exit.
 *
 */
public class StageExit extends PipelineStage 
{    
    public StageExit(int stageType)
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
        //terminar o simulador se todos os processos estiverem no estado exit.
        //percorrer todos os processos para ver se estao no ciclo correcto
        int numProcessesOnCorrectCycle = 0;
        for(PCB pcb : queue[0])
        {
            //processo esta a espera a pelo menos 1 ciclo
            if(pcb.getCurrentPipelineCycle() < cycles)
            {
                pcb.setCurrentPipelineCycle(cycles);
                numProcessesOnCorrectCycle++;
            }
        }
 
        if(numProcessesOnCorrectCycle == Pipeline.numProcesses)
        {
            Cpu_scheduler.running = false;
            queue[0].clear();
        }
    }   
}
