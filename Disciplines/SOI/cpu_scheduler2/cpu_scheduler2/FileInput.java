package cpu_scheduler2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Le o conteudo de um ficheiro que contem os dados
 * a serem processados pelo simulador.
 */
public class FileInput 
{
    FileReader file;
    BufferedReader br;
    public static ArrayList<String> dados = new ArrayList<String>();
    int numProcesses = 0;

    public FileInput(String caminho) throws IOException 
    {
        this.file = new FileReader(caminho);
        this.br = new BufferedReader(file);
        init();
    }

    /**
     * Retorna o tempo do processo quando chega ao stage NEW.
     * @param n
     * @return 
     */
    public int getCreationTimeOfProcess(int n)
    {
        String aux = dados.get(n);
        String[] dados = aux.split(" ");
        return Integer.valueOf(dados[0]);
        
    }
        
    /**
     * Inicializa a leitura do ficheiro, lendo o conteudo inteiro
     * do ficheiro.
     * @throws IOException 
     */
    private void init() throws IOException
    {
        String out = "";
        while((out = br.readLine()) != null)
        {
            dados.add(out);
            numProcesses++;
        }
    }

    /**
     * Retorna o numero de processos.
     * @return 
     */
    public int getNumberOfProcess()
    {
       return numProcesses;
    }

    /**
     * Retorna os dados associados a um processo n.
     * @param n
     * @return 
     */
    public static int[] getDadosOfProcess(int n)
    {
        String aux = dados.get(n);
        String[] dados = aux.split(" ");
        int[] result = new int[dados.length - 1];
        for(int i = 1; i < dados.length; i++){
                result[i - 1] = Integer.valueOf(dados[i]);
        }
        return result;
    }

    /**
     * Carrega os dados lidos para a pipeline.
     * @throws Exception 
     */
    public void carregarDados() throws Exception
    {
        int numP = getNumberOfProcess();
        Pipeline.numProcesses = numP;

        for(int i = 0; i < numP; i++)
        {
            int dados[] = this.getDadosOfProcess(i);
            PCB pcb = new PCB();
            pcb.setDataid(i);
            pcb.setCreationTime( getCreationTimeOfProcess(i));
            
            Cpu_scheduler2.pipeline.queueProcessCreation(pcb);
        }
    }
}
