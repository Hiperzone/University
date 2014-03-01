
package cpu_scheduler2;

class ProcessException extends Exception 
{
    public static final String PROCESS_EXCEPTION_RUNTIME_ERROR = "Ocurreu um erro ao correr o processo, tempo invalido";
    public static final String PROCESS_EXCEPTION_RUNTIME_ERROR_UNKNOWN_OPCODE = "Ocurreu um erro ao correr o processo, cpu opcode desconhecido";
    public static final String PROCESS_EXCEPTION_RUNTIME_ERROR_OUT_OF_MEMORY = "Erro ao alocar memoria, memoria insuficiente";
    
    public ProcessException() 
    {
        super();
    }
    
    public ProcessException(String s)
    {
        super(s);
    }
}
