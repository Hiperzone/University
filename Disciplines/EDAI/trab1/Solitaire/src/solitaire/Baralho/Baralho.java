package solitaire.Baralho;


import solitaire.Exception.GameException;
import solitaire.Util.StackArray;
import solitaire.Util.StackIsFullException;
import solitaire.Util.IndexOutOfBoundsException;
import java.util.Random;
import solitaire.Util.DoubleLinkedList;

public class Baralho
{
    DoubleLinkedList<Carta> baralho;
    Random randomizer;
  
    public Baralho()
    {
        //tempo actual como semente
        randomizer = new Random( System.currentTimeMillis() );
        baralho = new DoubleLinkedList<Carta>();
        criarBaralho();
    }
    /**
     * Preenche o baralho com 52 cartas.
     */ 
    private void criarBaralho()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 13; j++)
            {
                Carta c = new Carta(Carta.Valor.values()[j], Carta.Naipe.values()[i]);
                baralho.add(c);
            }
        }  
    }
    
    /**
     * Retorna uma carta aleatoria do baralho.
     * @return Carta
     * @throws IndexOutOfBoundsException 
     */
    public Carta get() throws IndexOutOfBoundsException 
    {
        int index = randomizer.nextInt(baralho.size());
        Carta c = baralho.get(index);
        baralho.remove(index);
     
        return c;
    }
    
    /**
     * Retorna uma pilha de n cartas.
     * @param num
     * @return StackArray<Carta>
     * @throws GameException 
     */
    public StackArray<Carta> getPilhaCartas(int num) throws GameException
    {
        try
        {
            StackArray<Carta> lista = new StackArray<Carta>(num);
            for( int i = 0; i < num; i++)
            {
                //lista.push(get());
                lista.push(baralho.get(0));
                baralho.remove(0);
            }
             return lista;
        }
        catch( Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        } 
    }
  
    @Override
    public String toString()
    {
        String strBaralho = new String();
        for( Carta c :  baralho)
        {
          strBaralho += c.toString() + "\n";
        }
        return strBaralho;
    }
  
    /**
     * verifica se o baralho contem a carta c.
     * @param c
     * @return True/False
     */
    public boolean verifica(Carta c)
    {
        return baralho.contains(c);
    }  
}
