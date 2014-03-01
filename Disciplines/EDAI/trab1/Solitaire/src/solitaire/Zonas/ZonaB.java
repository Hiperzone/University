package solitaire.Zonas;

import solitaire.Baralho.Carta;
import solitaire.Exception.GameException;
import solitaire.Util.StackArray;
import solitaire.Util.StackIsEmptyException;
import solitaire.Util.StackIsFullException;

public class ZonaB
{
    private StackArray<Carta>[] pilhas;
    
    /**
     * Inicializa as 4 pilhas da zona B, permitindo 13 cartas em cada pilha.
     */
    public ZonaB()
    {
        pilhas = new StackArray[4];
        pilhas[0] = new StackArray<Carta>(13);
        pilhas[1] = new StackArray<Carta>(13);
        pilhas[2] = new StackArray<Carta>(13);
        pilhas[3] = new StackArray<Carta>(13);
    }
    
    /**
     * Retira uma carta de uma pilha da zona B.
     * @param index
     * @return Carta
     * @throws GameException 
     */
    public Carta retirar( int index ) throws GameException
    {
        try
        {
            return pilhas[index].pop();
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        }
    }
    
    /**
     * Obtem uma referencia da carta que está no topo de uma pilha na ZonaB caso
     * esta exista.
     * A carta retornada não é removida.
     * @param index
     * @return Carta
     * @throws GameException 
     */
    public Carta get(int index) throws GameException
    {
        try
        {
            return pilhas[index].top();
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        }
    }
    
    /**
     * Verifica se pode por uma carta numa pilha especifica.
     * A carta tem que ser do mesmo naipe e o peso da carta tem que ser superior
     * a carta que esta no topo da pilha em questão.
     * @param x
     * @param pilhaIndex
     * @return True/False
     * @throws GameException 
     */
    public boolean canPut(Carta x, int pilhaIndex) throws GameException
    {
        
        try
        {
            if(!x.isVoltadaParaCima()) { return false; }
            //verificar a pilha A, se estiver vazia e a carta for um As
            //adicionar a pilha

            if(pilhas[pilhaIndex].getSize() == 0 && x.valor() == Carta.Valor.ÁS)
            {
                return true;
            }
            else
            {
                //a pilha contem cartas, verificar se é do mesmo naipe e é a
                //carta a seguir.
                if(pilhas[pilhaIndex].getSize() != 0 && x.naipe() == pilhas[pilhaIndex].top().naipe() && 
                    x.valor().ordinal() == (pilhas[pilhaIndex].top().valor().ordinal() + 1))
                {
                    return true;
                }
            }
            return false;
        
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEB);
        }
    }
  
    /**
     * Tenta por automaticamente a carta desejada numa das pilhas disponiveis
     * @param x
     * @throws GameException 
     */
    public void put(Carta x) throws GameException
    {
        try
        {
            for(int i = 0; i < pilhas.length; i++)
            {
                if(canPut(x, i)) 
                { 
                    pilhas[i].push(x);
                    break;
                }
            }
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEB);
        }
    }

    
    /**
     * Adiciona uma carta a uma pilha especifica da zona B.
     * @param x
     * @param i
     * @throws GameException 
     */
    public void put(Carta x, int i) throws GameException
    {
        //coloca a carta x especificamente na iésima pilha
        try
        {
            //nao se verifica se a jogada e valida porque foi verificada antes
           // if(canPut(x, i))
           // {
            pilhas[i].push(x);  
            //}
        }
        catch( Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEB);
        }
    }
    
    @Override
    public String toString()
    {
        String output = new String();

            System.out.printf("Zona B:\n");
            System.out.printf("|                |                 |                 |\n");
            //beta test
            for(int j = 0; j < 13; j++)
            {
                if( j < pilhas[0].getSize())
                {
                    System.out.printf("%-18s", pilhas[0].get(j));
                }
                else
                {
                    System.out.printf("                  ");
                }
                
                if( j < pilhas[1].getSize())
                {
                    System.out.printf("%-18s", pilhas[1].get(j));
                }
                else
                {
                    System.out.printf("                  ");
                }
                
                if( j < pilhas[2].getSize())
                {
                    System.out.printf("%-18s", pilhas[2].get(j));
                }
                else
                {
                    System.out.printf("                  ");
                }
                
                if( j < pilhas[3].getSize())
                {
                    System.out.printf("%-18s\n", pilhas[3].get(j));
                }
                else
                {
                    System.out.printf("                  \n");
                }
            }
        
        System.out.printf("--------------------------------\n");     
        return output;
    }
}
