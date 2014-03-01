
package cpu_scheduler2;
import java.util.LinkedList;

/**
 * Classe abstracta que contem metodos partilhados
 * em todos os stages da pipeline.
 */
public abstract class PipelineStage {
    
     protected LinkedList<PCB> queue;
     protected int pipelineStageType;
     
     public PipelineStage(int stageType)
     {
         pipelineStageType = stageType;
     }
     
     /**
      * Adiciona um processo a lista de espera.
      * @param process 
      */
     public void addProcessToQueue(PCB process)
     {
         queue.add(process);
     }
     
     /**
      * Obtem o tamanho da lista de processos.
      * @return 
      */
     public int getQueueSize()
     {
         return queue.size();
     }
     
     /**
     * Actualiza e gere os processos que estao em fila de espera no stage.
     * passando-os para o stage seguinte quando for oportuno.
     * @param cycles 
     */
     public abstract void update(int cycles) throws Exception;
     
     /**
      * Faz o output do estado dos processos.
      * @return 
      */
     public String outputProcessStates()
     {
        String output = "";

        for(PCB pcb : queue)
        {
           output += "Processo id=" + pcb.getPID() + " estado=" + PipelineI.strStage[pcb.getState()] + " ";
            output += "\n";

        }
         
        return output;
     }
     
     /**
      * Faz o output do tempo de espera no estado ready de cada processo.
      * @return 
      */
     public String outputProcessWaitTime()
     {
         String output = "";
         
         return output;
     }
}
