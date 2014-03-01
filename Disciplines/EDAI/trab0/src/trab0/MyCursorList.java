/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trab0;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyCursorList<E> implements Iterable<E> {
    public static CursorNode myMemory[];
    
    static void printMemory()
    {
        for( int i = 0; i < myMemory.length; i++)
        {
            System.out.print( myMemory[i].toString());
        }
        System.out.print("\n");
    }
    
    static void initialize(int s)
    {
        myMemory = new CursorNode[s];
        for ( int i = 0; i < myMemory.length; i++)
        {
            myMemory[i] = new CursorNode(null, i + 1);
        }
        //last node fix
        myMemory[myMemory.length - 1].setNext(0);
    }
    
    static int alloc() throws CursorListFullException
    {
        //verifica se a memoria esta cheia
        if(myMemory[0].getNext() == 0) throw new CursorListFullException();
        
        //nao esta cheia, retornar o seguinte node livre
        int freeNode =  myMemory[0].getNext();
        myMemory[0].setNext( myMemory[freeNode].getNext() );
        return freeNode;
    }
    
    static void dealloc ( int p ) throws CursorListOutOfBoundsException
    {
        //out of bounds?
        if(p >= myMemory.length) throw new CursorListOutOfBoundsException();
        
        //obter o node anterior a p
        CursorNode prev = null;
        for( int i = 0; i < myMemory.length; i++)
        {
            if(myMemory[i].getNext() == p)
            {
                prev = myMemory[i];
            }
        }
        
        //elimina o elemento actual do node
        myMemory[p].setElement(null);
        
        //guarda o node seguinte que esta livre
        int nextFreeNode = myMemory[0].getNext();
        
        //header da memoria livre aponta para o novo
        //node libertado
        myMemory[0].setNext(p);
        
        //liga o node anterior ao node que segue o node a ser removido
        prev.setNext(myMemory[p].getNext());  
        
        //o node libertado aponta para o seguinte node livre que estava na
        //header.
        myMemory[p].setNext(nextFreeNode);  
    }
    
    private int header;
    private int current; //ultimo indice da lista
    private int currSize;
    
    public Iterator iterator()
    {
        return new CursorListIterator( header, currSize );
    }
    
    public MyCursorList() throws CursorListFullException
    {
        header = alloc();
        myMemory[header].setNext(0);
        current = header;
        currSize = 0;
    }
    
    public boolean isEmpty()
    {
        return currSize == 0;
    }
    
    public int size()
    {
       return currSize; 
    }
    
    public void add( E x ) throws CursorListFullException
    {
        int node = alloc(); //alocar novo node da memoria
        myMemory[node].setElement(x); //atribuir elemento
        myMemory[node].setNext(0); //fim da lista
        myMemory[current].setNext(node); //ligar o node actual ao novo node
        current = node; //actualizar o node actual para o novo node
        currSize++; //incrementar o tamanho
    }
    
    public CursorNode get(int i) throws CursorListOutOfBoundsException
    {
        //out of bounds?
        if(i >= myMemory.length) throw new CursorListOutOfBoundsException();
        return myMemory[i];
    }
    
    public void remove( E x )
    {
        try 
        {
            CursorNode current = myMemory[header];
            int Index = 0;
            while( current.getNext() != 0)
            {
                Index = current.getNext();
                current = myMemory[current.getNext()];
                if(current.getElement().equals(x))
                {
                    //obter o node anterior que esta ligado ao ultimo node da 
                    //lista caso o index a ser removido coicida com o current
                    if(Index == this.current)
                    {
                        for( int i = 0; i < myMemory.length; i++)
                        {
                            if(myMemory[i].getNext() == Index )
                            {
                               this.current = i;
                               break;
                            }
                        }
                    }
                    
                    dealloc(Index);               
                    currSize--;
                    break;
                }              
            }
        } 
        catch (CursorListOutOfBoundsException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
}
