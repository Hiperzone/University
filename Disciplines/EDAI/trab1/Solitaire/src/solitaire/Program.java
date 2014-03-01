/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solitaire;


import solitaire.Jogo.Solitaire;
import solitaire.Baralho.Baralho;
import solitaire.Baralho.Carta;
import solitaire.Exception.GameException;
import solitaire.Util.DoubleLinkedList;
import solitaire.Util.StackArray;
import solitaire.Util.StackIsEmptyException;

/**
 *
 * @author Daniel
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws GameException
    {   try
        {
            /////////////teste TAD.///////////////////////
            //Stack Array
            StackArray<Integer> stack = new StackArray<Integer>(2);
            //adicionar valor 1
           // stack.push(new Integer(2));
           // System.out.println(stack);
            //adicionar valor 2
           // stack.push(new Integer(11));
            //System.out.println(stack);
            
            //loop de elementos
           /* for(int i = 0; i < stack.getSize(); i++)
            {
                System.out.println(stack.get(i));
            }*/
            //remover 1 elemento do topo da stack
            //stack.pop();
            //System.out.println(stack);
            //remover 1 elemento do topo da stack
            //stack.pop();
            //System.out.println(stack);
            //remover elemento inexistente da stack
            //stack.pop();
            //System.out.println(stack);
            //teste out of bounds com stack cheia.
             //stack.push(new Integer(1));
             //stack.push(new Integer(2));
             //stack.push(new Integer(3)); 
            
            //DoubleLinkedList
            DoubleLinkedList<Integer> l = new DoubleLinkedList<Integer>();
            //adicionar elemento
           // l.add(new Integer(1));
           // System.out.println(l);
            //adicionar a um indice especifico
           // l.add(1, new Integer(2));
           // System.out.println(l);
            //adicionaar num indice especifico
           // l.add(2, new Integer(3));
           // System.out.println(l);
            //adicionar a um indice out of range
            //l.add(4, new Integer(4));
            //System.out.println(l);
            //obter um elemento de um indice
            //Integer i = l.get(2);
            //System.out.println(l);
            //obter um elemento de um indice out of range
            //Integer i2 = l.get(3);
            //System.out.println(l);
            //oter o primeiro elemento
           // Integer i3 = l.getFirst();
           // System.out.println(l);
            //obter o ultimo elemento
            //Integer i4 = l.getLast();
            //System.out.println(l);
            //mudar o valor no indice 0
           // l.set( 0, new Integer(5));
           // System.out.println(l);
            //remover o ultimo elemento
           // l.removeLast();
           // System.out.println(l);
            //remover um elemento num indice
           // l.remove(0);
           // System.out.println(l);
            //remover um elemento num indice invalido
            //l.remove(4);
            //System.out.println(l);
            //clear
            //l.clear();
           // System.out.println(l);
            //remover um elemento quando esta vazio
            //l.remove(0);
            //System.out.println(l);
            
            //////FIM DE TESTE
        }
        catch(Exception e)
        {
        
            System.out.println("erro na tad"); 
        }
    
        try
        {
            //teste solitario.
            //iniciar novo jogo
            Solitaire sol = new Solitaire(); 
            //imprimir o estado do tabuleiro
            //System.out.print(sol);
            ///////////////TESTE ZONA A///////////////
            //retirar uma carta sem virar cartas
           // Carta c = sol.retiraZonaA();
           // System.out.print(sol);
            //Virar todas as cartas infinitamente para testar recolha de cartas
            /*while(true)
            {
                sol.virar();
                System.out.print(sol);
            }*/
            //testar se consegue mover uma carta de A para B sem cartas viradas
            //sol.canMoveFromAToB(0);
            //testar se consegue mover uma carta de A para C sem cartas viradas
            //sol.canMoveFromAToC(0);
            //virar uma carta
            sol.virar();
            ////////////////TESTE ZONA B///////////////////
            //testar se consegue mover cartas de B para C sem existirem em B
            //sol.canMoveFromBToC(0, 0);
            //retirar uma carta da zona B sem existir cartas
            //sol.retiraZonaB(0);
            //testar metodos internos da zona B
            //sol.testarZonaB();
            
            /////////TESTE ZONA C////////
            //testar metodos internos da zona C
           // sol.testarZonaC();
 
            ////////////TESTE SOLITARIO, JOGADAS/////////////////////
             System.out.print(sol); //tabuleiro inicial
             sol.moveCC(4, 4, 0); //mover dama de paus para rei de copas
             System.out.print(sol);
             /*sol.moveCC(1, 1, 0); //mover valete de copas para dama de paus
             System.out.print(sol);
             sol.moveCC(0, 1, 4); //mover dama de copas para rei de paus
             System.out.print(sol);
             sol.moveCC(3, 4, 1); //mover rei de paus++ para pilha 1
             System.out.print(sol);
             sol.moveCB(4, 0); // mover as de copas para zona B
             System.out.print(sol);
             sol.moveCB(4, 0); //mover dois de copas para zona B
             System.out.print(sol);
             sol.moveCB(4, 0); //mover tres de copas para zona B
             System.out.print(sol);
             sol.moveCB(3, 0); //mover quatro de copas para zona B
             System.out.print(sol);
             sol.moveCB(3, 0); //mover cinco de copas para zona B
             System.out.print(sol);
             sol.moveCB(3, 0); //mover seis de copas para zona B
             System.out.print(sol);
             sol.moveCB(3, 0); //mover sete de copas para zona B
             System.out.print(sol);
             sol.moveCB(2, 0); //mover oito de copas para zona B
             System.out.print(sol);
             sol.moveCB(2, 0); //mover nove de copas para zona B
             System.out.print(sol);
             sol.moveCB(2, 0); //mover dez de copas para zona B
             System.out.print(sol);
             sol.moveCB(0, 0); //mover valete de copas para zona B
             System.out.print(sol);
             sol.moveCB(1, 0); //mover dama de copas para zona B
             System.out.print(sol);
             sol.moveCC(6, 6, 1); //mover dama de ouros para rei de paus
             System.out.print(sol);
             sol.moveCC(5, 6, 2); //mover rei de ouros pilha 2
             System.out.print(sol);
             sol.moveCB(6, 1); //mover as de paus para zona B
             System.out.print(sol);
             sol.moveCB(6, 1); //mover dois de paus para zona B
             System.out.print(sol);
             sol.moveCB(6, 1); //mover tres de paus para zona B
             System.out.print(sol);
             sol.moveCB(6, 1); //mover quatro de paus para zona B
             System.out.print(sol);
             sol.moveCB(6, 1); //mover cinco de paus para zona B
             System.out.print(sol);
             sol.moveCB(5, 1); //mover seis de paus para zona B
             System.out.print(sol);
             sol.moveCB(5, 1); //mover sete de paus para zona B
             System.out.print(sol);
             sol.moveCB(5, 1); //mover oito de paus para zona B
             System.out.print(sol);
             sol.moveCB(5, 1); //mover nove de paus para zona B
             System.out.print(sol);
             sol.moveCB(5, 1); //mover dez de paus para zona B
             System.out.print(sol);
             sol.moveCC(0, 5, 1); //mover valete de paus para a pilha 1
             System.out.print(sol);
             sol.moveCB(1, 1); //mover valete de paus para zona B
             System.out.print(sol);
             sol.moveCB(0, 1); //mover dama de paus para zona B
             System.out.print(sol);
             sol.moveCB(0, 0); //mover rei de copas para zona B
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.virar(); //virar
             System.out.print(sol);
             sol.moveAB(2); //mover as de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover dois de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover tres de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover quatro de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover cinco de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover seis de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover sete de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover oito de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover nove de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover dez de ouros para a zona B
             System.out.print(sol);
             sol.moveAB(2); //mover valete de ouros para a zona B
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.moveCB(1,2); //mover dama de ouros para a zona B
             System.out.print(sol);
             sol.moveCB(2,2); //mover rei de ouros para a zona B
             System.out.print(sol);
             sol.moveCB(1,1); //mover rei de paus para a zona B
             System.out.print(sol);
             sol.moveAC(6); //mover rei de espadas para a zona C
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.virar(); //virar carta
             System.out.print(sol);
             sol.moveAB(3); //mover as de espadas para a zona B
             System.out.print(sol);
             
             //random testing
             sol.moveCC(0, 6, 0); //mover o rei de espadas para a pilha 0
             System.out.print(sol);
             ///
             sol.moveAB(3); //mover dois de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover tres de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover quatro de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover cinco de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover seis de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover sete de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover oito de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover nove de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover dez de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover valete de espadas para a zona B
             System.out.print(sol);
             sol.moveAB(3); //mover dama de espadas para a zona B
             System.out.print(sol);
             sol.moveCB(0, 3); //mover rei de espadas para a zona B
             System.out.print(sol);
             
             //VITORIA
             
             //random testing
             //mover uma carta de B para C
             sol.moveBC(0, 0);
             System.out.print(sol);
             //jogada invalida, mover de B para C
             //sol.moveBC(0, 1);
             //mover uma carta de B para C
             sol.moveBC(1, 1);
             System.out.print(sol);
             //mover uma carta de B para C
             sol.moveBC(2, 2);
             System.out.print(sol);
             //mover uma carta de B para C
             sol.moveBC(3, 3);
             System.out.print(sol);
             //mover uma carta de B para C
             sol.moveBC(1, 0);
             System.out.print(sol);
             //FIM DE TESTE*/
            
        }
        catch(GameException e)
        {
            System.out.println(e.getMessage());
            
        }
       
        
    }
        
}
