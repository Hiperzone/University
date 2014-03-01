/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorting;

public class DoubleNode<T> 
{
    private T element;
    private DoubleNode<T> next;
    private DoubleNode<T> prev;
    
    public DoubleNode( T element, DoubleNode<T> prev, DoubleNode<T> next)
    {
        this.element = element;
        this.next = next;
        this.prev = prev;
    }
    
    /**
     * Retorna o elemento.
     * @return element
     */
    public T getElement() {
        return element;
    }

    /**
     * Atribui o elemento.
     * @param element 
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * Retorna o node seguinte.
     * @return DoubleNode<T>
     */
    public DoubleNode<T> getNext() {
        return next;
    }

    /**
     * Atribui o node seguinte.
     * @param next 
     */
    public void setNext(DoubleNode<T> next) {
        this.next = next;
    }

    /**
     * Obtem o node anterior.
     * @return DoubleNode<T>
     */
    public DoubleNode<T> getPrev() {
        return prev;
    }

    /**
     * Atribui o node anterior.
     * @param prev 
     */
    public void setPrev(DoubleNode<T> prev) {
        this.prev = prev;
    } 
}
