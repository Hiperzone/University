
package sorting;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Sorting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try 
        {
            int a[] = { 33, 34, 12, 22, 30};
           Sort.quickSort(a);
           
           Testes teste = new Testes("Teste_10.txt", 10, 10);
           Testes testeb = new Testes("Teste_100.txt", 100, 10);
           Testes testec = new Testes("Teste_1000.txt", 1000, 10);
           Testes tested = new Testes("Teste_10000.txt", 10000, 10);
           Testes testee = new Testes("Teste_100000.txt", 100000, 10); 
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(Sorting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
