/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solitaire.Exception;

/**
 *
 * @author Daniel
 */
public class GameException extends Exception 
{
    public static final String GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS = "A pilha não contem cartas suficientes";
    public static final String GAME_EXCEPTION_INVALID_MOVE = "Jogada inválida";
    public static final String GAME_EXCEPTION_NO_TURNED_CARDS = "Impossivel retirar carta, não existe cartas viradas";
    public static final String GAME_EXCEPTION_CANT_TURN_NEW_CARD = "Impossivel virar uma nova carta, não existe cartas para virar";
    public static final String GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEB = "Impossivel adicionar carta a pilha na zona B";
    public static final String GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEC = "Impossivel adicionar carta a pilha na zona C";
    public static final String GAME_EXCEPTION_CANT_GET_SNAPSHOT = "Não existe cartas suficientes na pilha para obter o snapshot requerido";
    
    public GameException()
    {
        super();
    }
    
    public GameException(String s)
    {
        super(s);
    }
}
