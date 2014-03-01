package cpu_scheduler;

/**
 * Classe abstracta para o pcb.
 * Contem a geracao do pid.
 */
public abstract class AbstractPCB 
{
	public static int pidNumber = 0;
        
        /**
         * Obtem o id do processo.
         * @return 
         */
	public abstract int getPID();
        
        /**
         * Obtem o estado do processo.
         * @return 
         */
	public abstract int getState();
        
        /**
         * Atribui o estado do processo.
         * @param value 
         */
	public abstract void setState(int value);
}
