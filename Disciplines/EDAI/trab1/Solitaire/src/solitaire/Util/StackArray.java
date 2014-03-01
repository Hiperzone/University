package solitaire.Util;

import solitaire.Exception.GameException;

public class StackArray<T> implements Stack<T>
{
    private T[] stack;
    private int size;
    private int maxSize;
    
    public StackArray(int size)
    {
        stack = (T[]) new Object[size];
        maxSize = size;
        size = 0;
    }

    /**
     * Verifica se a stack esta vazia.
     * @return boolean
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    /**
     * Verifica se a stack esta cheia
     * @return boolean
     */
    public boolean isFull()
    {
        return size == maxSize;
    }
    
    /**
     * Adiciona um objecto ao topo da stack
     * @param Object
     * @throws StackIsFullException 
     */
    @Override
    public void push(T o) throws StackIsFullException
    {
        if(isFull()) { throw new StackIsFullException(); }
        stack[size++] = o;
    }
    
    /**
     * Retorna o objecto que está no topo da stack
     * @return Object
     * @throws StackIsEmptyException 
     */
    @Override
    public T top() throws StackIsEmptyException
    {
        if(isEmpty()) { throw new StackIsEmptyException(); }
        return stack[size - 1];
    }
    
    /**
     * Remove o objecto que esta no topo da stack e retorna-o
     * @return Object
     * @throws StackIsEmptyException 
     */
    @Override
    public T pop() throws StackIsEmptyException
    {
        if(isEmpty()) { throw new StackIsEmptyException(); }
        T o = stack[--size];
        stack[size] = null;
        return o;
    }
    
    /**
     * retorna o objecto que esta num indice especifico
     * @param index
     * @return Object
     * @throws ArrayIndexOutOfBoundsException 
     */
    public T get(int index) throws ArrayIndexOutOfBoundsException
    {
        return stack[index];
    }

    /**
     * Obtem o tamanho actual da stack
     * @return size
     */
    @Override
    public int getSize() 
    {
        return size;
    }
    
    @Override
    public String toString()
    {
        String output = new String();
        for(int i = 0; i < maxSize; i++)
        {
            output += "[" + stack[i] + "]";
        }
        return output;
        
    }
}
