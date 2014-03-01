
package sorting;

class RandomArrayException extends Exception 
{
    public static final String RANGE_NOT_ENOUGH = 
            "Alcance insuficiente para o tamanho do array";
    public RandomArrayException()
    {
        super();
    }
    
    public RandomArrayException(String s)
    {
        super(s);
    }
    
}
