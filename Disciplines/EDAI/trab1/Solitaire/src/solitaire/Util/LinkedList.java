/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solitaire.Util;

/**
 *
 * @author Daniel
 */
public interface LinkedList<T> 
{
    void add(T e);
    void add(int index, T e) throws IndexOutOfBoundsException;
    void set(int index, T e) throws IndexOutOfBoundsException;
    T remove(int index) throws IndexOutOfBoundsException;
    boolean remove(T e);
    void clear();
    T get(int index) throws NoSuchElementException, IndexOutOfBoundsException;
    int size();
}
