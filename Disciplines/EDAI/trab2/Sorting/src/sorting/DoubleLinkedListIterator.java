/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorting;

import java.util.Iterator;

public class DoubleLinkedListIterator<T> implements Iterator<T> 
{
    private DoubleNode<T> current;
    private DoubleLinkedList<T> list;
    
    public DoubleLinkedListIterator(DoubleNode<T> header, DoubleLinkedList<T> list)
    {
        current = header.getNext();
        this.list = list;
    }
    
    /**
     * Retorna verdadeiro de ouver um elemento seguinte.
     * @return True/False
     */
    @Override
    public boolean hasNext()
    {
        return (current != list.getTail());
    }
    
    /**
     * Retorna o elemento actual.
     * @return 
     */
    @Override
    public T next()
    {
        T element = current.getElement();
        current = current.getNext();
        return element;
    }

    /**
     * Remove o elemento actual.
     */
    @Override
    public void remove()
    {
        list.removeNode(current.getPrev());
        
    }
}

