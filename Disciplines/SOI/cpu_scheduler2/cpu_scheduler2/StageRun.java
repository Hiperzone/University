
package cpu_scheduler2;

import java.util.LinkedList;

/**
 * Gere os processos do stage run.
 *
 */
public class StageRun extends PipelineStage
{
    public static int CPU_CYCLES_PER_PROCESS = 4;
    
    public static final int OPCODE_ZERO      = 0x0;
    public static final int OPCODE_ADD       = 0x1;
    public static final int OPCODE_SUB       = 0x2;
    public static final int OPCODE_IF        = 0x3;
    public static final int OPCODE_BACK      = 0x4;
    public static final int OPCODE_FORW      = 0x5;
    public static final int OPCODE_FORK      = 0x6;
    public static final int OPCODE_DISK_SAVE = 0x7;
    public static final int OPCODE_COPY      = 0x8;
    public static final int OPCODE_EXIT      = 0x9;
    
    private int currentProcessCyles = 0;
    
    public StageRun(int stageType)
    {
        super(stageType);
        queue = new LinkedList<PCB>();
    }
    
    /**
     * Atribui a quantidade de ciclos que o processador deve fazer.
     * @param cycles 
     */
    public static void setCpuCycles(int cycles)
    {
        CPU_CYCLES_PER_PROCESS = cycles;
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
    public void scheduler(int cycles) throws ProcessException
    {
        //fazer trabalho no cpu para o processo que esta na frente da fila se este
        //esta a espera ha um ciclo atras.
        if(this.queue.size() > 0 &&
           queue.getFirst().getCurrentPipelineCycle() < cycles)
        {
            PCB process = queue.getFirst();
            
            //incrementar o tempo de cpu para este processo.
            currentProcessCyles = inc(currentProcessCyles);
            process.setCurrentPipelineCycle(cycles);
            
            Integer opcode = MemFactory.MEM[ process.getPC() ];
            int high = (opcode % 100 / 10);
            int low = (opcode % 10);
            switch(high)
            {
                case OPCODE_ADD:
                {
                    MemFactory.MEM[ process.getBaseVarAddress() + low]++;
                    process.setPC( process.getPC() + 1);
                }break;
                case OPCODE_ZERO:
                {
                    MemFactory.MEM[ process.getBaseVarAddress() + low] = 0;
                    process.setPC( process.getPC() + 1);
                }break;
                case OPCODE_SUB:
                {
                    MemFactory.MEM[ process.getBaseVarAddress() + low]--;
                    process.setPC( process.getPC() + 1);
                }break;
                case OPCODE_IF:
                {
                    if(MemFactory.MEM[ process.getBaseVarAddress() + low] == 0)
                    {
                        process.setPC( process.getPC() + 1);
                    }
                    else
                    {
                        process.setPC( process.getPC() + 2);
                    }
                }break;
                case OPCODE_BACK:
                {
                    process.setPC( process.getPC() - low);
                }break;
                case OPCODE_FORW:
                {
                    process.setPC( process.getPC() + low);
                }break;
                case OPCODE_FORK:
                {
                    // Incrementar primeiro o PC para evitar
                    // que o fork seja feito infinitamente pelo processo filho
                    process.setPC( process.getPC() + 1);
                    PCB newProcess = process.fork();
                    newProcess.setCurrentPipelineCycle(cycles);
                    Pipeline.numProcesses++;
                    
                    Pipeline.pipelineStages[Pipeline.PIPELINE_STAGE_NEW].addProcessToQueue(newProcess);
                    
                    if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_NORMAL)
                    	System.out.print("Inicio do processo "+newProcess.getPID()+", "+MemFactory.getInstance()+"\n");
                }break;
                case OPCODE_DISK_SAVE:
                {
                    process.setState(PipelineI.PROCESS_STATE_BLOCKED);
                    Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_BLOCKED].addProcessToQueue(queue.removeFirst());
                    resetCPUCycles();
                    process.setPC( process.getPC() + 1);
                }break;
                case OPCODE_COPY:
                {
                    MemFactory.MEM[ process.getBaseVarAddress()] = low;
                    process.setPC( process.getPC() + 1);
                }break;
                case OPCODE_EXIT:
                {
                    process.setState(PipelineI.PROCESS_STATE_EXIT);
                    Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_EXIT].addProcessToQueue(queue.removeFirst());
                    resetCPUCycles();
                    
                    
                    
                    //libertar a memoria aqui?
                    MemFactory.getInstance().memfree( process.getAllocatedMemSize(), process);
                    
                    if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_NORMAL)
                    	System.out.println("Fim do processo "+process.getPID()+", "+MemFactory.getInstance());
                }break;
                    
                default:
                    throw new ProcessException( ProcessException.PROCESS_EXCEPTION_RUNTIME_ERROR_UNKNOWN_OPCODE);
            }
            
            //fim de cpu burst, enviar para o estado ready.
            if(currentProcessCyles == CPU_CYCLES_PER_PROCESS)
            {
                resetCPUCycles();
                process.setState(PipelineI.PROCESS_STATE_READY);
                Pipeline.pipelineStages[PipelineI.PIPELINE_STAGE_READY].addProcessToQueue(queue.removeFirst());
                return;
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
