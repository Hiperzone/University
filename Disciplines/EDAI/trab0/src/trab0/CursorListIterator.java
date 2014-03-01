/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trab0;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursorListIterator<T> implements Iterator<T>{
    
    private int current; //indice actual
    private int prev; //indice anterior a ser removido
    private int size; //tamanho da lista
    
    public CursorListIterator(int header, int size)
    {
        current = MyCursorList.myMemory[header].getNext();
        this.size = size;
        prev = current;
    }
     
    public boolean hasNext()
    {
        return (size != 0);
    }
    
    public T next()
    {
        //retorna o elemento actual e obtem o next para a seguinte
        //iteracao
        T element = (T)MyCursorList.myMemory[current].getElement();
        prev = current;
        current = MyCursorList.myMemory[current].getNext();
        size--;

        return element;
    }
    
    public void remove()
    {
        try 
        {
            //teste para ver se o next foi usado antes de remover
            if(prev == current) throw new IllegalStateException();
             MyCursorList.dealloc(prev); 
        } 
        catch (CursorListOutOfBoundsException ex) 
        {
            //cheat para que seja lançada uma excepcao, crashando o programa
            throw new IllegalStateException();
        }
        
    }
}
