/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;

/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class FacturaNaoExisteException extends Exception{
    
    public FacturaNaoExisteException()
    {
        super();
    }
    
    public FacturaNaoExisteException(String s)
    {
        super(s);
    }
    
}
