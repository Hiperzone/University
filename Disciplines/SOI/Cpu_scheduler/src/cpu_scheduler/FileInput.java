package cpu_scheduler;

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
    ArrayList<String> dados = new ArrayList<String>();

    public FileInput(String caminho) throws IOException 
    {
        this.file = new FileReader(caminho);
        this.br = new BufferedReader(file);
        init();
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
        }
    }

    /**
     * Retorna o numero de processos.
     * @return 
     */
    public int getNumberOfProcess()
    {
        String lineOne = dados.get(0);
        String[] inf = lineOne.split(" ");
        return Integer.valueOf(inf[0]);
    }

    /**
     * Retorna o numero de dispositivos.
     * @return 
     */
    public int getNumberOfDevices()
    {
        String lineOne = dados.get(0);
        String[] inf = lineOne.split(" ");
        return Integer.valueOf(inf[1]);
    }

    /**
     * Retorna o tempo de cada dispositivo
     * dev varia entre 0 e o numero de dispositivos - 1, inclusive
     * @param dev
     * @return
     * @throws Exception 
     */
    public int getTimeOfDevice(int dev) throws Exception
    {
        if(dev < 0 || dev >= getNumberOfDevices())
            throw new Exception();

        int startAtIndex = 2;
        int index = dev + startAtIndex;
  		
        String lineOne = dados.get(0);
        String[] devicesTime = lineOne.split(" ");
        return Integer.valueOf(devicesTime[index]);
    }
    
    /**
     * Retorna o tempo do processo quando chega ao stage NEW
     * o valor de n varia entre 1 e o numero de dispositivos, inclusive
     * caso contrario retorna 0
     * @param n
     * @return
     */
    public int getTimeProcess(int n)
    {
        String aux = dados.get(n);
        String[] time = aux.split(" ");
        return Integer.valueOf(time[0]);
    }

    /**
     * Retorna os dados associados a um processo n.
     * @param n
     * @return 
     */
    public int[] getDadosOfProcess(int n)
    {
        n++;
        String aux = this.dados.get(n);
        String[] dados = aux.split(" ");
        int[] result = new int[dados.length];
        for(int i = 0; i < result.length; i++){
                result[i] = Integer.valueOf(dados[i]);
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
        int numD = getNumberOfDevices();
        Pipeline.numDevices = numD;
        StageBlocked stage = 
                (StageBlocked) Pipeline.pipelineStages[
                PipelineI.PIPELINE_STAGE_BLOCKED];
        
        stage.setupDevices();
        for(int i = 0; i < numP; i++)
        {
            int dados[] = this.getDadosOfProcess(i);
            PCB pcb = new PCB();
            pcb.setDados(dados);
            Cpu_scheduler.pipeline.queueProcessCreation(pcb);
        }
        
        for(int i = 0; i< numD; i++)
        {
        	int time_device = getTimeOfDevice(i);
        	System.out.println("device "+i+", time: "+time_device);
        	Device device = new Device(time_device);
        	System.out.println("device: "+device);
        	StageBlocked.devices.add(device);
        }
    }
}
