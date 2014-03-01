/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;

/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class ConsumidorNaoExisteException extends Exception{
    
    public ConsumidorNaoExisteException()
    {
        super();
    }
    
    public ConsumidorNaoExisteException(String s)
    {
        super(s);
    }
    
}
