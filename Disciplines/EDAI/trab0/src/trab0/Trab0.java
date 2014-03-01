/*
 * Realizado por Daniel Ramos(29423) e Simao Ramos(29035)
 * and open the template in the editor.
 */
package trab0;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Trab0 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        try {
            //inicializar a memoria com tamanho 10
            MyCursorList.initialize(10);
            //estado da memoria actual
            MyCursorList.printMemory();

            //inicializar lista1
            MyCursorList<String> lista1 = new MyCursorList<String>();
            //estado da memoria actual
            MyCursorList.printMemory();
            
             //is empty?
            System.err.println("Empty: " + lista1.isEmpty());
            
            //adicionar elementos a lista1
            lista1.add(new String("Maria"));
            lista1.add(new String("MariaB"));
            lista1.add(new String("MariaC"));
            
            //estado da memoria actual
            MyCursorList.printMemory();
            
            //iterador 1
            for(String s : lista1)
            {
                System.out.println(s);
                
            }
            //remover o elemento;
            lista1.remove("MariaC");
            
            //estado da memoria actual
            MyCursorList.printMemory();
            
            //inicializar lista2
            MyCursorList<String> lista2 = new MyCursorList<String>();
            //estado da memoria actual
            MyCursorList.printMemory();
            
            //adicionar elementos a lista2
            lista2.add(new String("Ze"));
            lista2.add(new String("ZeB"));
            lista2.add(new String("ZeC"));
            lista2.add(new String("ZeD"));
             MyCursorList.printMemory();
            lista1.add(new String("MariaD"));
            
            //lista1.add(new String("MariaD"));
            //estado da memoria actual
            MyCursorList.printMemory();
            
            //iterator 1
            for(String s : lista1)
            {
                System.out.println(s);
                
            }
             
            //iterador 2
            for(String s : lista2)
            {
                System.out.println(s);
                
            }
            //remocao total dos elementos da lista 1
            lista1.remove("Maria");
            //estado da memoria actual
            MyCursorList.printMemory();
            lista1.remove("MariaB");
            //estado da memoria actual
            MyCursorList.printMemory();
            lista1.remove("MariaD");
            //estado da memoria actual
            MyCursorList.printMemory();
            
             //iterator 1
            for(String s : lista1)
            {
                System.out.println(s);
                
            }
             
            //iterador 2
            for(String s : lista2)
            {
                System.out.println(s);
                
            }
            
            //adicionar elementos a lista1
            lista1.add(new String("Maria"));
             //estado da memoria actual
            MyCursorList.printMemory();
            lista1.add(new String("MariaB"));
             //estado da memoria actual
            MyCursorList.printMemory();
            lista1.add(new String("MariaC"));
             //estado da memoria actual
            MyCursorList.printMemory();
            
             //iterator 1
            for(String s : lista1)
            {
                System.out.println(s);
                
            }
             
            //iterador 2
            for(String s : lista2)
            {
                System.out.println(s);
                
            }
            
            //remocao aleatoria
            lista1.remove("MariaB");
            //estado da memoria actual
            MyCursorList.printMemory();
            
            lista2.remove("ZeC");
            //estado da memoria actual
            MyCursorList.printMemory();
            
            lista2.remove("Ze");
            //estado da memoria actual
            MyCursorList.printMemory();
            
            lista1.remove("Maria");
            //estado da memoria actual
            MyCursorList.printMemory();
            
            
             //iterator 1
            for(String s : lista1)
            {
                System.out.println(s);
                
            }
             
            //iterador 2
            for(String s : lista2)
            {
                System.out.println(s);
                
            }
            
            //insercao de outros nomes
            lista2.add("Joao");
            lista2.add("Ana");
            lista2.add("Daniel");
            lista2.add("Jose");
            //estado da memoria actual
            MyCursorList.printMemory();
            
             //iterator 1
            for(String s : lista1)
            {
                System.out.println(s);
                
            }
             
            //iterador 2
            for(String s : lista2)
            {
                System.out.println(s);
                
            }
            //is empty?
            System.err.println("Empty: " + lista1.isEmpty());
            
            //iterator removal test
            for(Iterator<String> itr = lista2.iterator(); itr.hasNext();)
            {
                String element = itr.next();
                itr.remove(); //remove o elemento actual
                MyCursorList.printMemory();
            }
            lista1.remove("MariaC");
            MyCursorList.printMemory();
            
            MyCursorList<Integer> listac = new MyCursorList<Integer>();
            MyCursorList.printMemory();
            listac.add(new Integer(100));
            MyCursorList.printMemory();
            
        } 
        catch (CursorListFullException ex) 
        {
            Logger.getLogger(Trab0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
