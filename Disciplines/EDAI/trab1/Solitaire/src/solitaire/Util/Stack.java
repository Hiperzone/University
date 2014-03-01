package solitaire.Util;

public interface Stack<T> 
{
	public void push(T e) throws StackIsFullException;
	public T pop() throws StackIsEmptyException;
	public T top() throws StackIsEmptyException;
	public int getSize();
	public boolean isEmpty();
}

