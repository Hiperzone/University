package solitaire.Util;

public class StackIsEmptyException extends Exception 
{
    public StackIsEmptyException()
    {
            super();
    }

    public StackIsEmptyException(String s)
    {
            super(s);
    }
}
