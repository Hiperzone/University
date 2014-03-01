
package cpu_scheduler;
import java.util.LinkedList;

/**
 * Classe abstracta que contem metodos partilhados
 * em todos os stages da pipeline.
 */
public abstract class PipelineStage {
    
     protected LinkedList<PCB> queue[];
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
         queue[0].add(process);
     }
     
      /**
      * Adiciona um processo a lista de espera de um dispositivo.
      * Apenas pode ser usado no stage blocked.
      * @param process 
      */
     public void addProcessToQueue(PCB process, int device)
     {
         queue[device - 1].add(process);
     }
     
     /**
      * Obtem o tamanho da lista de processos.
      * @return 
      */
     public int getQueueSize()
     {
         return queue[0].size();
     }
     
     /**
      * Obtem o tamanho da fila para um dispositivo.
      * @param device
      * @return 
      */
     public int getDeviceQueueSize(int device)
     {
         return queue[device].size();
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
         for(int i = 0; i < queue.length; i++)
         {
            for(PCB pcb : queue[i])
            {
               output += "Processo id=" + pcb.getPID() + " estado=" + PipelineI.strStage[pcb.getState()] + " ";
                output += "\n";
                
            }
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
         for(int i = 0; i < queue.length; i++)
         {
            for(PCB pcb : queue[i])
            {
               output += "Processo id=" + pcb.getPID() + " wait cycles=" + pcb.getTempoDeEspera()+ " ";
               output += "\n";
            }
         }
         return output;
     }
}
