
package sorting;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class FileOutput 
{
    private String filename = "";
    DataOutputStream file = null;
    PrintStream printStream = null;;
    
    public FileOutput(String filename) throws FileNotFoundException
    {
        this.filename = filename;
        file = new DataOutputStream( new FileOutputStream(filename));
        printStream  = new PrintStream(file);
    }
    
    public void initiate(String headerName) throws IOException
    {
        writeResult("--------------------------------------------------" );
        writeResult(headerName);
        writeResult("--------------------------------------------------" );
        writeResult("N            |T. Maximo     | T. Minimo    | T. Medio  " );
        
    }
    
    public void writeResult( long numIteracoes, long tempoMaior, 
            long tempoMenor, long tempoMedio) throws IOException
    {
         writeResult( String.format("%-12d | %-12d | %-12d | %-12d", 
                 numIteracoes, tempoMaior, tempoMenor, tempoMedio));
    }
    
    public void writeResult( int iteracao, String result ) throws IOException
    {
         printStream.append(iteracao + "         " + result + 
                 System.getProperty("line.separator"));
    }
    
    public void writeResult( String result ) throws IOException
    {
        printStream.append(result + System.getProperty("line.separator"));
    }
    
    public void finish() throws IOException
    {
        file.close();
    }
    
}
