package solitaire.Jogo;

import solitaire.Baralho.Baralho;
import solitaire.Baralho.Carta;
import solitaire.Exception.GameException;
import solitaire.Util.DoubleLinkedList;
import solitaire.Zonas.ZonaA;
import solitaire.Zonas.ZonaB;
import solitaire.Zonas.ZonaC;

public class Solitaire
{
    private ZonaA zonaA;
    private ZonaB zonaB;
    private ZonaC zonaC;
    
    /**
     * Inicializa o jogo, criando o baralho e distribuindo as cartas pelas
     * diversas zonas.
     * @throws GameException 
     */
    public Solitaire() throws GameException
    {
        Baralho baralho = new Baralho();

        zonaA = new ZonaA( baralho.getPilhaCartas(24) );
        zonaB = new ZonaB();
        zonaC = new ZonaC( baralho.getPilhaCartas(28) );
    }

    /**
     * Vira uma carta da zona A.
     * Caso nao haja cartas para virar, o baralho e recolhido automaticamente
     * e ai entao sera virada uma carta.
     * @throws GameException 
     */
    public void virar() throws GameException
    {
        zonaA.virar();
    }
    
    /**
     * Retira uma carta da zona A.
     * @return Carta
     * @throws GameException 
     */
    public Carta retiraZonaA() throws GameException
    {
        return zonaA.retirar();
    }

    /**
     * Retira uma carta de uma pilha da zona C.
     * @param index
     * @return Carta
     * @throws GameException 
     */
    public Carta retiraZonaC( int index ) throws GameException
    {
        return zonaC.retira(index);
    }
    
    /**
     * Retira um conjunto de cartas da zona C de uma pilha.
     * @param IndexCarta
     * @param index
     * @return DoubleLinkedList<Carta>
     * @throws GameException 
     */
    public DoubleLinkedList<Carta> retiraZonaC( int IndexCarta, int index) throws GameException
    {
        return zonaC.retira(IndexCarta, index);
    }
    
    /**
     * Retira uma carta de uma pilha da zona B.
     * @param index
     * @return Carta
     * @throws GameException 
     */
    public Carta retiraZonaB(int index) throws GameException
    {
        return zonaB.retirar(index);
    }
    
    /**
     * Verifica se pode mover uma ou um conjunto de cartas de uma pilha para
     * outra pilha na zona C.
     * @param indexCarta
     * @param indexSource
     * @param indexDest
     * @return True/False
     * @throws GameException 
     */
    public boolean canMoveFromCToC(int indexCarta, int indexSource, int indexDest) throws GameException
    {
        return zonaC.canPut( zonaC.getListSnapshot(indexCarta, indexSource), indexDest);
    }
    
    /**
     * Verifica se pode mover uma carta da zona A para uma pilha especifica da
     * Zona B.
     * @param pilhaIndex
     * @return True/False
     * @throws GameException 
     */
    public boolean canMoveFromAToB(int pilhaIndex) throws GameException
    {
        return (zonaB.canPut( zonaA.getCartaVirada(), pilhaIndex) ); 
    }
    
    /**
     * Verifica se pode mover uma carta da zona A para uma pilha especifica da
     * zona C.
     * @param pilhaIndex
     * @return True/False
     * @throws GameException 
     */
    public boolean canMoveFromAToC(int pilhaIndex) throws GameException
    {
        return ( zonaC.canPut( zonaA.getCartaVirada(), pilhaIndex));
    }
    
    /**
     * Verifica se pode mover uma carta de uma pilha especifica da zona C para
     * uma pilha especifica da zona B.
     * @param indexSource
     * @param indexDest
     * @return True/False
     * @throws GameException 
     */
    public boolean canMoveFromCToB(int indexSource, int indexDest) throws GameException
    {
        return zonaB.canPut( zonaC.getCarta(indexSource), indexDest);
    }
    
    /**
     * Verifica se pode mover uma carta de uma pilha especifica da zona B para
     * uma pilha especifica da zona C.
     * @param indexSource
     * @param IndexDest
     * @return True/False
     * @throws GameException 
     */
    public boolean canMoveFromBToC(int indexSource, int IndexDest) throws GameException
    {
        return zonaC.canPut( zonaB.get(indexSource) , IndexDest);
    }
    
    ////////////MOVIMENTOS/////////////////
    
    /**
     * Move uma carta da zona A para uma pilha da zona B.
     * @param pilhaIndex
     * @throws GameException 
     */
    public void moveAB(int pilhaIndex) throws GameException
    {
        
        if(this.canMoveFromAToB(pilhaIndex))
        {
            Carta c = this.retiraZonaA();
            zonaB.put(c);
        }
        else
        {
            throw new GameException(GameException.GAME_EXCEPTION_INVALID_MOVE);
        }
    }
    
    /**
     * Move uma carta da zona A para uma pilha da zona C.
     * @param pilhaIndex
     * @throws GameException 
     */
    public void moveAC(int pilhaIndex) throws GameException
    {
        if(this.canMoveFromAToC(pilhaIndex))
        {
            Carta c = this.retiraZonaA();
            zonaC.add(c, pilhaIndex);
        }
        else
        {
            throw new GameException(GameException.GAME_EXCEPTION_INVALID_MOVE);
        }
    }
    
    /**
     * Move uma ou um conjunto de cartas de uma pilha da zona C para outra pilha
     * da zona C.
     * @param indexCarta
     * @param indexSource
     * @param indexDest
     * @throws GameException 
     */
    public void moveCC(int indexCarta, int indexSource, int indexDest) throws GameException
    {
        if(this.canMoveFromCToC(indexCarta, indexSource, indexDest))
        {
            DoubleLinkedList<Carta> c = zonaC.retira(indexCarta, indexSource);
            zonaC.add(c, indexDest);
        }
        else
        {
            throw new GameException(GameException.GAME_EXCEPTION_INVALID_MOVE);
        }
    }
    
    /**
     * Move uma carta de uma pilha da zona C para uma pilha da zona B.
     * @param indexSource
     * @param indexDest
     * @throws GameException 
     */
    public void moveCB(int indexSource, int indexDest) throws GameException
    {
        if(this.canMoveFromCToB(indexSource, indexDest))
        {
            Carta c = this.retiraZonaC(indexSource);
            zonaB.put(c, indexDest);
        }
        else
        {
            throw new GameException(GameException.GAME_EXCEPTION_INVALID_MOVE);
        }
    }
    
    /**
     * Move uma carta de uma pilha da zona B para uma pilha da zona C.
     * @param indexSource
     * @param indexDest
     * @throws GameException 
     */
    public void moveBC(int indexSource, int indexDest) throws GameException
    {
        if(this.canMoveFromBToC(indexSource, indexDest))
        {
            Carta c = this.retiraZonaB(indexSource);
            zonaC.add(c, indexDest);
        }
        else
        {
            throw new GameException(GameException.GAME_EXCEPTION_INVALID_MOVE);
        }
    }
    
    @Override
    public String toString()
    {
        return zonaA.toString() +  zonaB.toString() +  zonaC.toString();
    }
    
    //METODOS DE TESTES
    public void testarZonaB()
    {
        try 
        {
            //testar se pode por um AS num indice valido
            Carta as = new Carta(Carta.Valor.ÁS, Carta.Naipe.COPAS);
            as.setVoltadaParaCima(true);

            if(zonaB.canPut(as, 0))
            {
                zonaB.put(as);
                System.out.print(this);
            }
            //tentar por uma carta fora de ordem em B num indice com um AS
            Carta rei = new Carta(Carta.Valor.REI, Carta.Naipe.COPAS);
            rei.setVoltadaParaCima(true);
            if(zonaB.canPut(rei, 0))
            {
                zonaB.put(rei);
                System.out.print(this);
            }
            //tentar por a carta seguinte ao As em B
            Carta dois = new Carta(Carta.Valor.DOIS, Carta.Naipe.COPAS);
            dois.setVoltadaParaCima(true);
            if(zonaB.canPut(dois, 0))
            {
                zonaB.put(dois);
                System.out.print(this);
            }
            //tentar por um tres de um naipe diferente na pilha 0
            Carta tres = new Carta(Carta.Valor.TRÊS, Carta.Naipe.ESPADAS);
            tres.setVoltadaParaCima(true);
            if(zonaB.canPut(tres, 0))
            {
                zonaB.put(tres);
                System.out.print(this);
            }
            /////
            //tentar por uma carta qualquer numa pilha vazia em B
            if(zonaB.canPut(tres, 2))
            {
                zonaB.put(tres);
                System.out.print(this);
            }
            //tentar por uma carta num indice invalido
           // if(zonaB.canPut(tres, 4))
            //{
            //    zonaB.put(tres);
            //    System.out.print(this);
           // }
            //por uma sequencia completa em B
            Carta as2 = new Carta(Carta.Valor.ÁS, Carta.Naipe.ESPADAS);
            as2.setVoltadaParaCima(true);
            Carta dois2 = new Carta(Carta.Valor.DOIS, Carta.Naipe.ESPADAS);
            dois2.setVoltadaParaCima(true);
            Carta tres3 = new Carta(Carta.Valor.TRÊS, Carta.Naipe.ESPADAS);
            tres3.setVoltadaParaCima(true);
            Carta quatro = new Carta(Carta.Valor.QUATRO, Carta.Naipe.ESPADAS);
            quatro.setVoltadaParaCima(true);
            Carta cinco = new Carta(Carta.Valor.CINCO, Carta.Naipe.ESPADAS);
            cinco.setVoltadaParaCima(true);
            Carta seis = new Carta(Carta.Valor.SEIS, Carta.Naipe.ESPADAS);
            seis.setVoltadaParaCima(true);
            Carta sete = new Carta(Carta.Valor.SETE, Carta.Naipe.ESPADAS);
            sete.setVoltadaParaCima(true);
            Carta oito = new Carta(Carta.Valor.OITO, Carta.Naipe.ESPADAS);
            oito.setVoltadaParaCima(true);
            Carta nove = new Carta(Carta.Valor.NOVE, Carta.Naipe.ESPADAS);
            nove.setVoltadaParaCima(true);
            Carta dez = new Carta(Carta.Valor.DEZ, Carta.Naipe.ESPADAS);
            dez.setVoltadaParaCima(true);
            Carta dama = new Carta(Carta.Valor.DAMA, Carta.Naipe.ESPADAS);
            dama.setVoltadaParaCima(true);
            Carta valete = new Carta(Carta.Valor.VALETE, Carta.Naipe.ESPADAS);
            valete.setVoltadaParaCima(true);
            Carta rei2 = new Carta(Carta.Valor.REI, Carta.Naipe.ESPADAS);
            rei2.setVoltadaParaCima(true);
            
            zonaB.put(as2);
            zonaB.put(dois2);
            zonaB.put(tres3);
            zonaB.put(quatro);
            zonaB.put(cinco);
            zonaB.put(seis);
            zonaB.put(sete);
            zonaB.put(oito);
            zonaB.put(nove);
            zonaB.put(dez);
            zonaB.put(valete);
            zonaB.put(dama);
            zonaB.put(rei2);
            System.out.print(this);  
        } 
        catch (GameException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public void testarZonaC()
    {
        Carta as2 = new Carta(Carta.Valor.ÁS, Carta.Naipe.OUROS);
        as2.setVoltadaParaCima(true);
        Carta dois2 = new Carta(Carta.Valor.DOIS, Carta.Naipe.PAUS);
        dois2.setVoltadaParaCima(true);
        Carta tres3 = new Carta(Carta.Valor.TRÊS, Carta.Naipe.OUROS);
        tres3.setVoltadaParaCima(true);
        Carta quatro = new Carta(Carta.Valor.QUATRO, Carta.Naipe.PAUS);
        quatro.setVoltadaParaCima(true);
        Carta cinco = new Carta(Carta.Valor.CINCO, Carta.Naipe.OUROS);
        cinco.setVoltadaParaCima(true);
        Carta seis = new Carta(Carta.Valor.SEIS, Carta.Naipe.PAUS);
        seis.setVoltadaParaCima(true);
        Carta sete = new Carta(Carta.Valor.SETE, Carta.Naipe.OUROS);
        sete.setVoltadaParaCima(true);
        Carta oito = new Carta(Carta.Valor.OITO, Carta.Naipe.PAUS);
        oito.setVoltadaParaCima(true);
        Carta nove = new Carta(Carta.Valor.NOVE, Carta.Naipe.OUROS);
        nove.setVoltadaParaCima(true);
        Carta dez = new Carta(Carta.Valor.DEZ, Carta.Naipe.PAUS);
        dez.setVoltadaParaCima(true);
        Carta dama = new Carta(Carta.Valor.DAMA, Carta.Naipe.PAUS);
        dama.setVoltadaParaCima(true);
        Carta valete = new Carta(Carta.Valor.VALETE, Carta.Naipe.OUROS);
        valete.setVoltadaParaCima(true);
        try 
        {
            
            
            //testar se pode por uma carta invalida na pilha 0
            if(zonaC.canPut(dez, 0))
            {
                zonaC.add(dez, 0);
            }
             System.out.print(this);  
            //testar sequencia correcta de cartas
            if(zonaC.canPut(dama, 0))
            {
                zonaC.add(dama, 0);
            }
            System.out.print(this);  
            if(zonaC.canPut(valete, 0))
            {
                zonaC.add(valete, 0);
            }
            System.out.print(this);
            if(zonaC.canPut(dez, 0))
            {
                zonaC.add(dez, 0);
            }
            System.out.print(this); 
            if(zonaC.canPut(nove, 0))
            {
                zonaC.add(nove, 0);
            }
            System.out.print(this);  
            if(zonaC.canPut(oito, 0))
            {
                zonaC.add(oito, 0);
            }
            System.out.print(this); 
            if(zonaC.canPut(sete, 0))
            {
                zonaC.add(sete, 0);
            }
            System.out.print(this);
            if(zonaC.canPut(seis, 0))
            {
                zonaC.add(seis, 0);
            }
            System.out.print(this);
            if(zonaC.canPut(cinco, 0))
            {
                zonaC.add(cinco, 0);
            }
            System.out.print(this);
            if(zonaC.canPut(quatro, 0))
            {
                zonaC.add(quatro, 0);
            }
            System.out.print(this);
            if(zonaC.canPut(tres3, 0))
            {
                zonaC.add(tres3, 0);
            }
            System.out.print(this);  
            if(zonaC.canPut(dois2, 0))
            {
                zonaC.add(dois2, 0);
            }
            System.out.print(this);
            if(zonaC.canPut(as2, 0))
            {
                zonaC.add(as2, 0);
            }
            System.out.print(this);  
           
            //teste, mover as cartas a partir do dez de paus na pilha 0 para a 
            //pilha 1
            if(zonaC.canPut( zonaC.getListSnapshot(3, 0), 1))
            {
                zonaC.add( zonaC.retira(3, 0), 1);
            }
            System.out.print(this);   
        } 
        catch (GameException ex) {
            System.out.println(ex.getMessage());
        }  
    }
}
