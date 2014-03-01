package solitaire.Zonas;

import solitaire.Exception.GameException;
import solitaire.Baralho.Carta;
import solitaire.Util.StackArray;
import solitaire.Util.StackIsEmptyException;
import solitaire.Util.StackIsFullException;

public class ZonaA
{
    private StackArray<Carta> cartas;
    private StackArray<Carta> cartasViradas;
    
    /**
     * Inicializa as pilhas, pondo 24 cartas na pilha das cartas viradas.
     * @param lista 
     */
    public ZonaA(StackArray<Carta> lista)
    {
      cartas = lista;
      cartasViradas = new StackArray<Carta>(24);
    }
    
    /**
     * Retorna a carta que esta no topo da pilha das cartas viradas.
     * Caso não exista cartas, será lançada uma excepção.
     * @return Carta
     * @throws GameException 
     */
    public Carta getCartaVirada() throws GameException
    {
        try
        {
            return cartasViradas.top();
        }
        catch(StackIsEmptyException e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_NO_TURNED_CARDS);
        }
    }
    /**
     * Vira uma carta e adiciona-a na pilha das cartas viradas.
     * Caso não exista cartas para virar, o baralho será recolhido e as
     * cartas serão movidas para a pilha das cartas voltadas para baixo.
     * Depois de recolhido o baralho, serão então virada uma carta.
     * @throws GameException 
     */
    public void virar() throws GameException
    {
        try
        {
            //nao existe cartas para virar, repor o baralho e virar uma carta
            if(cartas.getSize() == 0)
            {
                while(cartasViradas.getSize() != 0)
                {
                    Carta c = cartasViradas.pop();
                    c.setVoltadaParaCima(false);
                    cartas.push(c);
                }
                
                Carta c = cartas.pop();
                c.setVoltadaParaCima(true);
                cartasViradas.push(c);
            }
            else
            {
                //existem cartas na pilha, virar uma carta
                Carta c = cartas.pop();
                c.setVoltadaParaCima(true);
                cartasViradas.push(c);
            } 
        }
        catch( StackIsEmptyException | StackIsFullException e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_TURN_NEW_CARD);
        } 
    }
  
    /**
     * Retira uma carta do topo da pilha das cartas viradas para cima
     * @return Carta
     * @throws GameException 
     */
    public Carta retirar() throws GameException
    {
        try
        {
            return cartasViradas.pop();
        }
        catch(StackIsEmptyException e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_NO_TURNED_CARDS);
            
        }
    }
    
    @Override
    public String toString()
    {
       String output = new String();
       System.out.printf("Zona A:\n");
       if(cartas.getSize() > 0)
       {
           System.out.printf("Proxima Carta: ?????????????\n");;
       }
       else
       {
          System.out.printf("Proxima Carta: nenhuma, necessario virar o baralho\n");
       }

       if(cartasViradas.getSize() > 0)
       {
            System.out.printf("Carta Virada:");;
            try 
            {
                System.out.printf("%-18s\n", cartasViradas.top());
            } 
            catch (StackIsEmptyException ex) 
            {
                System.out.println("Impossivel obter carta virada, stack vazia");
            }
            
           
       }
       System.out.printf("--------------------------------\n");     
       return output;
        
    }
}
