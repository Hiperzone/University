/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;

/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class ConsumidorJaExisteNaDBException extends Exception{
    
    public ConsumidorJaExisteNaDBException()
    {
        super();
    }
    
    public ConsumidorJaExisteNaDBException(String s)
    {
        super(s);
    }
}
