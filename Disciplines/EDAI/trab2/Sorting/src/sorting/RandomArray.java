
package sorting;
import java.util.Random;

public class RandomArray 
{
    private Random randomizer;
    
    private int array[];
    
    /**
     * Gera um array de inteiros aleatoriamente dado o seu range(k1,k2).
     * @param n
     * @param k1
     * @param k2 
     */
    public RandomArray(int n,int k1,int k2) throws RandomArrayException
    {
        if( (k2 - k1) < (n - 1) ) { throw new 
                RandomArrayException(RandomArrayException.RANGE_NOT_ENOUGH); }
                
        randomizer = new Random( System.currentTimeMillis() );
        array = new int[n];

        int i = 0;
        while(i < array.length)
        {
            int randomNum = randomizer.nextInt(k2 - k1 + 1) + k1;
            if(!contains(randomNum, i))
            {
                array[i] = randomNum;
                i++;
            } 
        }
    }
    
    /**
     * Verifica se um determinado inteiro existe no array.
     * @param num
     * @return 
     */
    private boolean contains(int num, int index)
    {
        for(int i = 0; i < index; i++)
        {
            if(array[i] == num) { return true; }
        }
        return false;
    }
    
    /**
     * Retorna um array de inteiros escolhidos aleatoriamente.
     * @return 
     */
    public int[] get()
    {
        return array;
    }
}

