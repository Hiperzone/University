
package sorting;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Testes 
{
    private long tempoMedio = 0;
    private long maiorTempo = 0;
    private long menorTempo = 0;
    private long tempo = 0;
    
    public Testes( String ficheiroOutput, int tamanhoArray, int numIteracoes) throws FileNotFoundException, IOException, RandomArrayException
    {
        FileOutput output = new FileOutput(ficheiroOutput);
        testarBubblesort(output, tamanhoArray, numIteracoes);
        testarMergesort(output, tamanhoArray, numIteracoes);
        testarShellsort(output, tamanhoArray, numIteracoes);
        testarRadixsort(output, tamanhoArray, numIteracoes);
        testarRanksort(output, tamanhoArray, numIteracoes);
        testarHeapsort(output, tamanhoArray, numIteracoes);
        testarQuicksort(output, tamanhoArray, numIteracoes);
        testarInsertionsort(output, tamanhoArray, numIteracoes);
        testarSelectionsort(output, tamanhoArray, numIteracoes);
        output.finish();   
    }
    
    public void resetTempos()
    {
        tempoMedio = 0;
        maiorTempo = 0;
        menorTempo = 0;
        tempo = 0;
    }
    
    public void testarSelectionsort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Selectionsort");
        resetTempos();
        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.selectionSort(array.get());
            minMaxTimer(tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
    
    }
    
    public void testarInsertionsort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Insertionsort");
        resetTempos();
        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.insertionSort(array.get());
            minMaxTimer(tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);
        
        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
    
    }
    
    
    
    public void testarQuicksort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Quicksort");
        resetTempos();
        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.quickSort(array.get());
            minMaxTimer(tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
    
    }
    
    
    public void testarHeapsort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Heapsort");
        resetTempos();
        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.heapSort(array.get());
            minMaxTimer(tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
    }
    
    
    public void testarRanksort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Ranksort");
        resetTempos();
        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.rankSort(array.get());
            minMaxTimer(tempoAlgo, i);
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
    }
    
    public void testarRadixsort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
         output.initiate("Radixsort");
         resetTempos();
        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.radixSort(array.get());
            minMaxTimer(tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
        
    }
    
    
    public void testarShellsort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
         output.initiate("Shellsort");
         resetTempos();

        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.shellSort(array.get());
            minMaxTimer(tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
        
    }
    
    public void testarMergesort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Mergesort");
        resetTempos();

        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.mergeSort(array.get());
            minMaxTimer( tempoAlgo, i);
   
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
        
    }
    
    public void testarBubblesort(FileOutput output, int tamanhoArray, int numIteracoes) throws IOException, RandomArrayException
    {
        output.initiate("Bubblesort");
        resetTempos();

        //comecar as iteracoes
        for(int i = 1; i <= numIteracoes; i++)
        {
            RandomArray array = new RandomArray(tamanhoArray, 0, tamanhoArray);
            long tempoAlgo = Sort.bubbleSort(array.get());
            minMaxTimer( tempoAlgo, i);
        }
         //media
        averageTime(numIteracoes);

        //output
        output.writeResult( tamanhoArray, maiorTempo, menorTempo, tempoMedio);
    }
    
    private void averageTime(int numIteracoes)
    {
    	tempo -= maiorTempo;
    	tempo -= menorTempo;
    	tempoMedio = tempo / (numIteracoes - 2);
    }
    
    private void minMaxTimer(long tempoAlgo, int i)
    {
    	if(tempoAlgo > maiorTempo)
    	{
    		maiorTempo = tempoAlgo;
    	}
    	if(i == 1) 
    	{
    		menorTempo = tempoAlgo;
    	}
    	if(tempoAlgo < menorTempo)
    	{
    		menorTempo = tempoAlgo;
    	}

    	tempo += tempoAlgo;
    }
}
