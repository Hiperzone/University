
package RFP.IO;

public class RFPException extends Exception 
{
    public static final String EXCEPTION_VERSION_MISMATCH = "Versão invalida";
    public static final String EXCEPTION_FILE_CORRUPTED = 
            "Ficheiro rfp corrupto";
    public static final String EXCEPTION_FILE_ALREADY_EXISTS = "O ficheiro já "
            + "Existe";
    
    public static final String EXCEPTION_DIRECTORY_ALREADY_EXISTS = 
            "A directoria já existe";
    
    public static final String EXCEPTION_ERROR_WHILE_EXTRACTING = "Ocorreu um"
            + "erro ao extrair o ficheiro.";
    public RFPException()
    {
        super();
    }
    
    public RFPException(String s)
    {
        super(s);
    }
}
