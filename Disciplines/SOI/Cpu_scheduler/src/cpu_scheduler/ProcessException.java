
package cpu_scheduler;

class ProcessException extends Exception 
{
    public static final String PROCESS_EXCEPTION_RUNTIME_ERROR = "Ocurreu um erro ao correr o processo, tempo invalido";

    public ProcessException() 
    {
        super();
    }
    
    public ProcessException(String s)
    {
        super(s);
    }
}
