/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorting;

import java.util.Iterator;

public class DoubleLinkedList<T> implements Iterable<T>, LinkedList<T>
{
    private DoubleNode<T> header;
    private DoubleNode<T> tail;
    private int size;
    
    /**
     * Inicializa a lista com tamanho zero e liga o cabeçalho com a cauda.
     */
    public DoubleLinkedList()
    {
        header = new DoubleNode<T>(null,null,null);
        tail = new DoubleNode<T>(null,header,null);
        header.setNext(tail);
        size = 0;
    }
    
    /**
     * Retorna a cauda da lista.
     * @return DoubleNode<T>
     */
    public DoubleNode<T> getTail() {
        return tail;
    }

    /**
     * Retorna o tamanho actual da lista.
     * @return 
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Adiciona um node ligando o node anterior a este e ligando este node
     * ao seguinte. O node a ser adicionado devera conter previamente o node
     * anterior e o node seguinte, bem como o seu elemento.
     * @param node 
     */
    private void addNode(DoubleNode<T> node)
    {
        node.getNext().getPrev().setNext(node);
        node.getNext().setPrev(node); //mudar para prev?
        size++;
    }
    
    /**
     * Adiciona um elemento.
     * @param element 
     */
    @Override
    public void add(T element)
    {
        DoubleNode<T> node = new DoubleNode<T>(element, tail.getPrev(), tail);
        addNode(node);
    }
    
    /**
     * Remove um elemento se existir.
     * @param element 
     */
    @Override
    public boolean remove(T element)
    {
        DoubleNode<T> node = getNode(element);
        if(node != null)
        {
            removeNode(node);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Adiciona um elemento a um indice especifico.
     * @param index
     * @param e 
     * @throws IndexOutOfBoundsException 
     */
    @Override
    public void add(int index, T e ) throws IndexOutOfBoundsException
    {
        DoubleNode<T> node = getNode(index);
        if(node == null) //index valid but there is no element there, add to tail
        {
           add(e);
        }
        else
        {
            DoubleNode<T> newNode = new DoubleNode<T>(e, node.getPrev(), node);
            addNode(newNode);   
        }
    }
    
    /**
     * Remove o elemento que esta num determinado indice e retorna-o.
     * @param index
     * @return Element
     * @throws IndexOutOfBoundsException 
     */
    @Override
    public T remove(int index) throws IndexOutOfBoundsException
    {
        DoubleNode<T> node = getNode(index);
        T element = node.getElement();
        removeNode(node);
        return element;
    }
    
    /**
     * Retorna o node que contem o indice especificado se possivel.
     * @param index
     * @return DoubleNode<E>
     * @throws IndexOutOfBoundsException 
     */
    private DoubleNode<T> getNode(int index) throws IndexOutOfBoundsException
    {
        if(index < 0 || index > size())
        {
            throw new IndexOutOfBoundsException();
        }
        
        int currIndex = 0;
        DoubleNode<T> current = header.getNext();
        while(current != tail)
        {
            if(currIndex == index)
            {
                return current;
            }
            currIndex++;
            current = current.getNext();
        }

        return null;
    }
    
    /**
     * Retorna o node que contem o elemento se ele existir.
     * @param element
     * @return DoubleNode<E>
     */
    private DoubleNode<T> getNode( T element)
    {
        DoubleNode<T> current = header.getNext();
        while(current != tail)
        {
            if(current.getElement().equals(element))
            {
                return current;
            }
            current = current.getNext();
        }
        
        return null;
    }
    
    /** Remove um node, desligando-o do node anterior e do node seguinte.
     * 
     * @param node 
     */
    public void removeNode(DoubleNode<T> node)
    {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        node.setElement(null);
        node.setNext(null);
        node.setPrev(null);
        size--;
    }
    
    /**
     * Retorna o iterator da lista.
     * @return 
     */
    @Override
    public Iterator iterator()
    {
        return new DoubleLinkedListIterator<T>(header, this);
    } 

    /**
     * Substitui o elemento num indice especifico caso este exista.
     * @param index
     * @param e
     * @throws IndexOutOfBoundsException 
     */
    @Override
    public void set(int index, T e) throws IndexOutOfBoundsException
    {
        if(index < 0 || index > size())
        {
            throw new IndexOutOfBoundsException();
        }
        
        int currIndex = 0;
        DoubleNode<T> current = header.getNext();
        while(current != tail)
        {
            if(currIndex == index)
            {
                current.setElement(e);
                break;
            }
            currIndex++;
            current = current.getNext();
        }
    }
    
    /**
     * Obtem o ultimo elemento se existir.
     * @return 
     */
    public T getLast() throws NoSuchElementException
    {
        DoubleNode<T> node = tail.getPrev();
        if(node == null) { throw new NoSuchElementException(); }
        return node.getElement();
    }
    
    /**
     * Obtem o primeiro elemento se existir.
     * @return 
     */
    public T getFirst() throws NoSuchElementException
    {
        DoubleNode<T> node = header.getNext();
        if(node == null) { throw new NoSuchElementException(); }
        return node.getElement();
    }
    
    /*
     * Remove o ultimo elemento da lista.
     */
    public T removeLast() throws IndexOutOfBoundsException
    {
        return this.remove(size - 1);
        
    }

    /**
     * Faz um reset a lista.
     */
    @Override
    public void clear() 
    {
        header = new DoubleNode<T>(null,null,null);
        tail = new DoubleNode<T>(null,header,null);
        header.setNext(tail);
        size = 0;
    }

    /**
     * Obtem o elemento de um indice especifico caso exista.
     * @param index
     * @return
     * @throws IndexOutOfBoundsException 
     */
    @Override
    public T get(int index) throws IndexOutOfBoundsException 
    {
        DoubleNode<T> node = getNode(index);
        return node.getElement();
    }
    
    @Override
    public String toString()
    {
        String output = new String();
        
        DoubleNode<T> current = header.getNext();
        while(current != tail)
        {
            output += "[" + current.getElement() + "]";
            current = current.getNext();
        }
        return output;
    }
    
    /**
     * Verifica se o elemento existe na lista.
     * @param e
     * @return 
     */
    public boolean contains(T e)
    {
        DoubleNode<T> node = getNode(e);
        if(node == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

