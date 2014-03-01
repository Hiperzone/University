/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trab0;

public class CursorNode {
    private Object element;

    public Object getElement() {
        return element;
    }

    @Override
    public String toString() {
        return "[" + element + "," + next + ']';
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
    private int next;
    
    public CursorNode( Object o, int next)
    {
        element = o;
        this.next = next;
    }
}
