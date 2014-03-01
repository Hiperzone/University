
package sorting;
import sorting.Timer;

public class Sort 
{
    /**
     * Algoritmo MergeSort.
     * Baseado no livro de Mark Allen Weiss
     * @param a
     * @return 
     */
    public static long mergeSort(int[] a)
    {
        Timer timer = new Timer();
        timer.start();
        int[] result = new int[a.length];
        mergeSort(a, result, 0, a.length - 1 );
        timer.end();
        return timer.timeElapsed(); 
    }
    
    /**
     * Metodo privado para o algoritmo MergeSort
     * Divide a array em duas partes recursivamente e funde os resultados.
     * @param a
     * @param result
     * @param inicio
     * @param fim
     */
    private static void mergeSort( int[] a, int[] result, int inicio, int fim)
    {
        //validacao: apenas partir se o inicio for menor que o fim
        if(inicio < fim)
        {
            //achar o centro
            int centro = (inicio + fim) / 2;
            mergeSort(a, result, inicio, centro); //primeira parte
            mergeSort(a, result, centro + 1, fim); //segunda parte
            //fundir
            mergeSortFundir(a, result, inicio, centro, centro + 1, fim);
        }
    }
    
    /**
     * Metodo interno para o algoritmo MergeSort em que funde o lado direito
     * da array com o lado esquerdo da array e ordena-os.
     * @param a
     * @param result //array temporaria para guardar os resultados da fusao
     * @param inicioA
     * @param fimA
     * @param inicioB
     * @param fimB 
     */
    private static void mergeSortFundir(int[] a, int[] result, int inicioA, 
            int fimA, int inicioB, int fimB)
    {
        int tmpIndex = inicioA;
        int elements =  fimB - inicioA + 1;
        //sortear enquanto nenhum dos lados ficar out of bounds
        while( inicioA <= fimA && inicioB <= fimB)
        {
            //A < B
            if(a[inicioA] <= a[inicioB])
            {
                //inserir o valor de A na array temporaria e incrementar o 
                //indice de A
                result[tmpIndex++] = a[inicioA++];
            }
            else
            {
                //A > B, inserir o valor de B na array temporaria e incrementar
                //o indice de B
                result[tmpIndex++] = a[inicioB++];
            }
        }
        //fim de troca, verificar se existem valores restantes em A e B
        //existem valores ainda no lado esquerdo da array
        if(inicioA <= fimA)
        {
            for(; inicioA <= fimA; inicioA++)
            {
                result[tmpIndex++] = a[inicioA];
            }
        }
        //existem valores ainda no lado direito da array
        if(inicioB <= fimB)
        {
            for(; inicioB <= fimB; inicioB++)
            {
                result[tmpIndex++] = a[inicioB];
            }
        }
        
        //copiar os dados de volta a array original
        for(int i = 0; i < elements; i++, fimB--)
        {
            a[fimB] = result[fimB];
        }      
    }
    
    /**
     * Algoritmo BubbleSort
     * @param a
     * @return 
     */
    public static long bubbleSort(int[] a)
    {
        Timer timer = new Timer();
        timer.start();
        
        //percorrer todos os valores da array
        for(int i = 1; i < a.length; i++)
        {
            //percorrer todos os valores da array menos o ultimo
            //porque ja foi verificado pelo penultimo valor.
            for(int j = 0; j < a.length-1; j++)
            {
                //verificar se o valor seguinte e menor que o valor actual
                if(a[j + 1] < a[j])
                {
                    //trocar os valores
                    int tmp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = tmp;
                }
            }
        }
        timer.end();
        return timer.timeElapsed();
    }
    
    /**
     * Algoritmo ShellSort.
     * @param a
     * @return 
     */
    public static long shellSort( int[] a)
    {
        Timer timer = new Timer();
        timer.start();
        shellSort(a, a.length / 2);
        timer.end();
        return timer.timeElapsed();
    }
    
    /**
     * Metodo interno para o algoritmo ShellSort.
     * @param a
     * @param gap 
     */
    private static void shellSort( int [] a, int gap)
    {
        //percorrer os valores a partir do gap ate ao fim
        int j = 0;
        for(int i = gap; i < a.length; i++)
        {
            int tmp = a[i];
            for(j = i; j >= gap; j-=gap)
            {
                if(tmp < a[j-gap])
                {
                    //trocar os valores.
                    a[j] = a[j-gap];
                }
            }
            a[j] = tmp;
        }
        //dividir o gap por 2 caso seja maior que 1, a divisao para quando
        //a array for percorrida 1 a 1.
        if( gap > 1) shellSort(a, gap /2); 
    }
    
    /**
     * Algoritmo RadixSort.
     * @param a
     * @return 
     */
    public static long radixSort(int [] a)
    {
        Timer timer = new Timer();
        timer.start();
        
        //iniciar as listas antes de comecar a sortear.
        DoubleLinkedList<Integer>[] pos = new DoubleLinkedList[10];
        for( int i = 0; i < pos.length; i++)
        {
            pos[i] = new DoubleLinkedList<Integer>();
        }
        
        DoubleLinkedList<Integer>[] neg = new DoubleLinkedList[10];
        for( int i = 0; i < neg.length; i++)
        {
            neg[i] = new DoubleLinkedList<Integer>();
        }
        
        radixSort(a, 1, 10, pos, neg);
        timer.end();
        return timer.timeElapsed();
    }
    
    /**
     * Metodo privado para o algoritmo RadixSort.
     * @param a Array
     * @param n Divisor
     * @param m Modulos
     * @param l DoubleLinkedList
     */
    private static void radixSort(int [] a, int n, int m, 
            DoubleLinkedList<Integer> pos[], DoubleLinkedList<Integer> neg[])
    {
        //soma de todos os restos calculados, se forem todos zero, é porque
        //nao existem mais digitos a calcular.
        //como nao se achou um melhor metodo de paragem, no final da ordenacao
        //ha sempre mais uma verificacao extra.
        int totalRemain = 0;
        
        //percorrer a array
        for(int i = 0; i < a.length; i++)
        {
            //calcular o resto para o digito actual
            int remain = a[i] % m / n;
            //detectar numeros negativos
            if(a[i] < 0)
            {
                //converter o resto para um numero positivo
                remain = Math.abs(remain);
                //adicionar o resto calculado ao resto total.
                totalRemain += remain;
                //adicionar o numero na lista dos negativos onde o indice/chave 
                //e o resto.
                neg[remain].add(a[i]);
            }
            else
            {
                //adicionar o resto calculado ao resto total.
                totalRemain += remain;
                //adicionar o numero na lista dos positivos onde o indice/chave 
                //e o resto.
                pos[remain].add(a[i]);
            }
        }

        //repor os novos valores na array
        int idx = 0;
        //negativos primeiro desta vez invertidos se não os numeros negativos
        //ficam por ordem decrescente.
        for(int i = neg.length - 1; i >= 0; i--)
        {
            for( int j : neg[i])
            {
                a[idx++] = j;
            }
        }
  
        //adicionar positivos
        for(int i = 0; i < pos.length; i++)
        {
            for( int j : pos[i])
            {
                a[idx++] = j;
            }
        }
        //resto total = 0 e porque nao existe mais digitos a calcular.
        if(totalRemain == 0) 
        {
        }
        else
        {
            //apagar os dados das listas.
            for( int i = 0; i < pos.length; i++)
            {
                pos[i].clear();
            }
            
            for( int i = 0; i < neg.length; i++)
            {
                neg[i].clear();
            }
            
            radixSort(a, n * 10, m * 10, pos, neg);
        }  
    }
    
    /**
     * Algoritmo RankSort.
     * @param a
     * Pagina de consulta do algoritmo: 
     * @return 
     */
    public static long rankSort(int[] a)
    {
        Timer timer = new Timer();
        timer.start();
        //array temporario onde serao guardados os valores sorteados.
        int[] tmp = new int[a.length];
        //percorrer a array ate ao fim
        for(int i = 0; i < a.length; i++)
        {
            //variavel que identifica o indice onde o valor do indice i ira
            //ser inserido na array temporaria.
            int c = 0;
            //percorrer a array pelo indice j ate ao fim
            //incrementado c se existir valores menores que i
            for( int j = 0; j < a.length; j++)
            {
                if( a[i] > a[j]) c++; 
            }
            //inserir o valor no indice calculado.
            tmp[c] = a[i];
        }
        
        //actualizar a array
        for(int i = 0; i < tmp.length; i++)
        {
            a[i] = tmp[i];
        }
        
        timer.end();
        return timer.timeElapsed();
    }
   
    /**
     * Algoritmo insertionsort
     * @param array
     * @return 
     */
    public static long insertionSort(int[] array)
    {
        Timer timer = new Timer();
        timer.start();
        int j = 0;
        int aux = 0;
        //percorrer a array
        //começa no indice 1 porque a comparacao e feita com o indice anterior
        for (int i=1; i < array.length; i++)
        { 
            //percorrer a array inversamente a partir do indice actual
            //mover os valores maiores que o valor actual no indice i
            //e inserir o valor actual quando o anterior for menor que o actual.
            aux = array[i];
            for(j = i; j > 0 && array[j - 1] > aux; j--)
            {
                array[j] = array[j-1];
               
            }
            array[j] = aux;
        }
        timer.end();
        return timer.timeElapsed();
    }

    /**
     * Algoritmo selectionSort.
     * @param array
     * @return 
     */
    public static long selectionSort(int[] array)
    {
        Timer timer = new Timer();
        timer.start();
        int indexOfSmaller = 0;
        int aux = 0;
        for(int i = 0; i < array.length - 1; i++){
                indexOfSmaller = i;
                for(int j = i; j < array.length; j++){
                        if( array[j] < array[indexOfSmaller]){
                                indexOfSmaller = j;
                        }
                }
                // swap values
                aux = array[indexOfSmaller];
                array[indexOfSmaller] = array[i];
                array[i] = aux;
        }
        timer.end();
        return timer.timeElapsed();
    }
    
    
    /**
     * Algoritmo quicksort.
     * @param a
     * @return 
     */
    public static long quickSort( int a[])
    {
        Timer timer = new Timer();
        timer.start();
        quickSort(a, 0, a.length - 1);
        
        timer.end();
        return timer.timeElapsed();
        
    }
    
    /**
     * Metodo privado do algoritmo quicksort.
     * @param a
     * @param esq
     * @param dir 
     */
    private static void quickSort( int a[], int esq, int dir)
    {
        if(esq < dir)
        {
            //ordenar valores baseados num pivot
            int meio = partition(a, esq, dir);
            //dividir a array em 2 partes.
            quickSort(a, esq, meio);
            quickSort(a, meio + 1, dir);
        }
    }
    
    /**
     * Método do algoritmo quicksort que ordena os valores de uma array baseado
     * num pivot.
     * @param a
     * @param esq
     * @param dir
     * @return 
     */
    private static int partition(int a[], int esq, int dir)
    {
        //obter o pivot
        int i = esq;
        int j = dir;
        int pivot = a[(esq+dir)/2];
        //enquanto o indice do lado esquerdo for menor que o do lado esquerdo
        //continuar a ordenar
        while( true )
        {
            //obter os indices onde os valores tem que ser trocados dependendo
            //do pivot.
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            
            //ambos os indices ja chegaram ao pivot?
            if(i < j)
            {
                //trocar valores.
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;

                //verificar os indices seguintes em cada lado
                i++;
                j--;
            }
            else
            {
                //indices coicidem com o pivot/meio, retornar o indice
                 return j;
            }
        } 
    }

    /**
     * Algpritmo heapsort
     * @param a
     * @return 
     */
    public static long heapSort(int [] a)
    {
        Timer timer = new Timer();
        timer.start();
        construirHeap(a);
        for (int i=a.length-1;i>0;i--)
        {
            troca(a,0,i);
            heapificar(a,0,i-1);
        }
        timer.end();
        return timer.timeElapsed();
    }
    
    /**
     * Constroi a heap dado uma array.
     * Apenas e usado no inicio.
     * @param A 
     */
    private static void construirHeap(int [] a)
    {
        //construir a heap
        for (int i= a.length /2 ; i>=0; i--)
        {
            heapificar(a,i,a.length-1);
        }
    }
    
    /**
     * Troca 2 valores numa array.
     * @param a
     * @param i
     * @param j 
     */
    private static void troca(int[] a, int i, int j )
    {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;  
    }
    
    /**
     * Heapifica a array.
     * @param h
     * @param i
     * @param size 
     */
    private static void heapificar(int[] h, int no, int size)
    {
        //obter o filho esquerdo em relacao ao no
        int j=2*no+1;

        //verificar se o filho esquerdo e menor que o filho direito.
        //incrementar j se for.
        if ((j < size) && (h[j]< h[j+1])) j++;
        //verificar se o filho com o valor maior e maior que o valor do no.
        if ((j <= size) && h[no] < (h[j]))
        {
            //trocar valor do filho para o no
            troca (h,no,j);
            //continuar a heapificar
            heapificar(h,j,size);
        }
    }
}
