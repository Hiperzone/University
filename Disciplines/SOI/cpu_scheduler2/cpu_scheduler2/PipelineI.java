/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu_scheduler2;

public interface PipelineI {
    public static final int PROCESS_STATE_NOT_CREATED = -1;
    public static final int PROCESS_STATE_NEW = 0;
    public static final int PROCESS_STATE_READY = 1;
    public static final int PROCESS_STATE_RUN = 2;
    public static final int PROCESS_STATE_BLOCKED = 3;
    public static final int PROCESS_STATE_EXIT = 4;
    
    public static final int PIPELINE_STAGE_NEW = 0;
    public static final int PIPELINE_STAGE_READY = 1;
    public static final int PIPELINE_STAGE_RUN = 2;
    public static final int PIPELINE_STAGE_BLOCKED = 3;
    public static final int PIPELINE_STAGE_EXIT = 4;
    
    public static String strStage[] = { "NEW", "WAIT/READY", "RUN", "BLOCKED", "EXIT" };
    
    public void run() throws Exception;
}
