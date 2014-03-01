package solitaire.Zonas;


import solitaire.Baralho.Carta;
import solitaire.Exception.GameException;
import solitaire.Util.DoubleLinkedList;
import solitaire.Util.StackArray;
import solitaire.Util.StackIsEmptyException;
import solitaire.Util.IndexOutOfBoundsException;
import solitaire.Util.NoSuchElementException;

public class ZonaC
{
    private DoubleLinkedList<Carta>[] pilhas;
  
    /* Inicializa as 7 pilhas, distribuindo as cartas recebidas pelas 7 pilhas.
     * 
     */
    public ZonaC(StackArray<Carta> l) throws GameException
    {
        try
        {
            pilhas = new DoubleLinkedList[7];
            for(int i = 0; i < 7; i++)
            {
                pilhas[i] = new DoubleLinkedList<Carta>();
            }
            //distribuir as cartas
            for(int i = 0; i < 7; i++)
            {
                //adicionar cartas viradas para baixo
                for(int j = 0; j < i; j++)
                {
                    pilhas[i].add(l.pop());
                }
                Carta c = l.pop();
                c.setVoltadaParaCima(true);
                pilhas[i].add(c);
            } 
        }
        catch(StackIsEmptyException e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        }  
    }
    
    /**
     * Verifica se pode adicionar uma carta a uma pilha especifica.
     * @param x
     * @param index
     * @return
     * @throws GameException 
     */
    public boolean canPut(Carta x, int index) throws GameException
    {
        //pilha vazia, verificar se é um rei
        try
        {
            if(!x.isVoltadaParaCima()) { return false; }
            if(pilhas[index].size() == 0)
            {
                if(x.valor() == Carta.Valor.REI)
                {
                    return true;
                }
            }
            else
            {
                //contem cartas, verificar se pode ser adicionada
              if( pilhas[index].size() != 0 && x.valor().ordinal() == pilhas[index].getLast().valor().ordinal() - 1
                && x.naipe().ordinal() % 2 != pilhas[index].getLast().naipe().ordinal() % 2)
              {
                  return true;
              }
            }
            return false;
            
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEC);
        }
    }
    
    /**
     * Verifica se pode adicionar uma sequencia de cartas a uma pilha.
     * @param s
     * @param index
     * @return True/False
     * @throws GameException 
     */
    public boolean canPut(DoubleLinkedList<Carta> s, int index) throws GameException
    {
        try
        {
            boolean verificar = false;
            if(s.size() == 0) return false;
            //verificar a primeira carta antes de verificar as restantes
            if(pilhas[index].size() != 0 
                    && s.getFirst().valor().ordinal() == pilhas[index].getLast().valor().ordinal() - 1 
                    &&  s.getFirst().naipe().ordinal() % 2 != pilhas[index].getLast().naipe().ordinal() % 2 
                    || pilhas[index].size() == 0 && s.getFirst().valor() == Carta.Valor.REI)
            {
                verificar = true;
            }

            if(verificar)
            {
                //verificar as seguintes
                int valInicial = s.getFirst().valor().ordinal();
                int naipeInicial = s.getFirst().naipe().ordinal()  % 2;
                for( Carta c : s)
                {
                    //naipe e valor estao de acordo com os valores actuais requeridos
                    if(!c.isVoltadaParaCima()) { return false; }
                    if(c.valor().ordinal() == valInicial && c.naipe().ordinal() % 2 == naipeInicial)
                    {
                        //actualizar a cor do naipe requerida na seguinte carta
                        //decrementar o valor da carta seguinte visto que é em
                        //ordem decrescente, K até AS
                        valInicial = valInicial - 1;
                        naipeInicial = (naipeInicial + 1) % 2;
                    }
                    else
                    {
                        return false;
                    }
                }
                //cartas estão na ordem correcta.
                return true; 
            }
        
            return false;  
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_PUT_CARD_IN_ZONEC);
        }
    }
    
    /**
     * Adiciona uma carta a uma pilha especifica.
     * @param x
     * @param i 
     */
    public void add(Carta x, int index) throws GameException
    {
        //verificacao feita previamente.
       // if(canPut(x, index))
       // {
            //adicionar a carta
        pilhas[index].add(x);
       // }  
    }

    /**
     * Adiciona uma sequencia de cartas a uma pilha especifica.
     * @param s
     * @param index
     * @throws GameException 
     */
    public void add(DoubleLinkedList<Carta> s, int index) throws GameException
    {
        //verificacao feita previamente.
       // if(canPut(s, index))
       // {
        //adicionar as cartas
        for( Carta c : s)
        {
            pilhas[index].add(c);
        }
       // }
    }

    /**
     * Obtem um conjunto de cartas de uma pilha especifica.
     * As cartas não são removidas.
     * @param indexCarta
     * @param index
     * @return DoubleLinkedList<Carta>
     */
    public DoubleLinkedList<Carta> getListSnapshot( int indexCarta, int index) throws GameException
    {
        try
        {
            DoubleLinkedList<Carta> list = new DoubleLinkedList<Carta>();
            for(int j = indexCarta; j < pilhas[index].size(); j++)
            {
                list.add(pilhas[index].get(j));
            }
            return list; 
        }
        catch( Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_GET_SNAPSHOT); 
        }
    }
    
    /**
     * Retira um conjunto de cartas de uma pilha especifica.
     * @param indexCarta
     * @param index
     * @return DoubleLinkedList<Carta>
     * @throws GameException 
     */
    public DoubleLinkedList<Carta> retira( int indexCarta, int index) throws GameException
    {
        try
        {
            DoubleLinkedList<Carta> list = new DoubleLinkedList<Carta>();
            //while akki
            int j = indexCarta;
            while(j < pilhas[index].size())
            {
                list.add(pilhas[index].remove(j));
            }

            virar(index);
            return list; 
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        }  
    }
    
    /**
     * Retita uma carta de uma pilha especifica
     * @param index
     * @return Carta
     * @throws GameException 
     */
    public Carta retira(int index) throws GameException
    {
        try
        {
            Carta c = pilhas[index].removeLast(); 
            virar(index);
            return c;
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        }
    }
    
    /**
     * Vira uma carta numa pilha especifica.
     * @param index
     * @throws GameException 
     */
    private void virar(int index) throws GameException
    {
        try
        {
            if(pilhas[index].size() > 0)
            {
               if(pilhas[index].getLast().isVoltadaParaCima())
               {
                    
               }
               else
               {
                   pilhas[index].getLast().setVoltadaParaCima(true);
               }
            }
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_CANT_TURN_NEW_CARD);
        }
    }
    
    /**
     * Obtem a carta que está no topo de uma pilha especifica.
     * @param index
     * @return Carta
     * @throws GameException 
     */
    public Carta getCarta(int index) throws GameException
    {
        try
        {
            return pilhas[index].getLast();
        }
        catch(Exception e)
        {
            throw new GameException(GameException.GAME_EXCEPTION_WITHOUT_ENOUGH_CARDS);
        }
    }

    public String toString()
    {
        String output = new String();
        try
        {
           
            System.out.printf("Zona C:\n");

            System.out.printf("|                |                 |                 |                 |                 |                 |                |\n");
            for(int i = 0; i < 19; i++)
            {
                if( i < pilhas[0].size())
                {
                    if(pilhas[0].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s", pilhas[0].get(i));
                    }
                    else
                    {
                        System.out.printf("????????????????? ");
                    }

                }
                else
                {
                    System.out.printf("                  ");
                }

                if( i < pilhas[1].size())
                {
                    if(pilhas[1].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s", pilhas[1].get(i));
                    }
                    else
                    {
                        System.out.printf("????????????????? ");
                    }
                }
                else
                {
                    System.out.printf("                  ");
                }

                if( i < pilhas[2].size())
                {
                    if(pilhas[2].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s", pilhas[2].get(i));
                    }
                    else
                    {
                        System.out.printf("????????????????? ");
                    }
                }
                else
                {
                    System.out.printf("                  ");
                }

                if( i < pilhas[3].size())
                {
                   if(pilhas[3].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s", pilhas[3].get(i));
                    }
                    else
                    {
                        System.out.printf("????????????????? ");
                    }
                }
                else
                {
                    System.out.printf("                  ");
                }

                if( i < pilhas[4].size())
                {
                  if(pilhas[4].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s", pilhas[4].get(i));
                    }
                    else
                    {
                        System.out.printf("????????????????? ");
                    }
                }
                else
                {
                    System.out.printf("                  ");
                }

                if( i < pilhas[5].size())
                {
                   if(pilhas[5].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s", pilhas[5].get(i));
                    }
                    else
                    {
                        System.out.printf("????????????????? ");
                    }
                }
                else
                {
                    System.out.printf("                  ");
                }

                if( i < pilhas[6].size())
                {
                   if(pilhas[6].get(i).isVoltadaParaCima())
                    {

                        System.out.printf("%-18s\n", pilhas[6].get(i));
                    }
                    else
                    {
                        System.out.printf("?????????????????\n");
                    }
                }
                else
                {
                    System.out.printf("                  \n");
                }


            }
        System.out.printf("--------------------------------\n"); 
        System.out.printf("------------------------------------------------\n");
       

        }
        catch(IndexOutOfBoundsException e)
        {

        }
        return output;
    }
}

