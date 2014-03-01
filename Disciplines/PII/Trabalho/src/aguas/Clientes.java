/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class Clientes implements Iterable<Consumidor>, Comparable<Clientes>{
    private static int CODIGO_CONSUMIDOR = 0; //gerador de codigos
    private ArrayList<Consumidor> clientes;
    
    /**
     * constructor
     *
     *
     */
    public Clientes()
    {
        clientes = new ArrayList<Consumidor>();
    }
    
    /**
     * gera um novo codigo de cliente
     *
     *
     */
    public static int gerarNovoCodigoCliente()
    {
        CODIGO_CONSUMIDOR++;
        return CODIGO_CONSUMIDOR;
    }
    
    /**
     * obtem o nr de clientes
     *
     *
     */
    public int size()
    {
        return clientes.size();
    }
    
    /**
     * implementacao do metodo compareTo
     *
     *
     */
    @Override
    public int compareTo(Clientes c)
    {
        return (size() - c.size() );
    }
    
    /**
     * retorna o iterator dos clientes
     *
     * @return Iterator
     */
    @Override
    public Iterator iterator()
    {
        return clientes.iterator();
    }
    
    /**
     * adiciona um consumidor
     *
     * @return none
     */
    public void addCliente(Consumidor consumidor) throws ConsumidorJaExisteNaDBException
    {
        if(clientes.contains(consumidor))
        {
            throw new ConsumidorJaExisteNaDBException("Consumidor já existe na database");
        }
        else
        {
            clientes.add(consumidor);
        }
    }
    
    /**
     * remove um consumidor
     *
     * @return none
     */
    public void removeCliente(Consumidor consumidor) throws ConsumidorNaoExisteException
    {
        if(clientes.contains(consumidor))
        {
            clientes.remove(consumidor);
        }
        else
        {
            throw new ConsumidorNaoExisteException("Consumidor não existe");
        }
    }
    
    /**
     * remove um consumidor baseado no codigo
     *
     * @return none
     */
    public void removeCliente(int codigo) throws ConsumidorNaoExisteException
    {
        Consumidor c = getCliente(codigo);
        if(c != null)
        {
            removeCliente(c);
        }
        throw new ConsumidorNaoExisteException("Consumidor não existe");
    }
    
    /**
     * obtem um cliente baseado no codigo
     *
     * @return Consumidor
     */
    public Consumidor getCliente(int codigo) throws ConsumidorNaoExisteException
    {
        for(Consumidor c: clientes)
        {
            if(c.getCodigoConsumidor() == codigo) return c;
        }
        throw new ConsumidorNaoExisteException("Consumidor não existe");
    }
    
    /**
     * verifica se existem clientes com facturas em atraso
     *
     * @return boolean
     */
    public boolean existeClientesComFacturasEmAtraso()
    {
        for( Consumidor c : clientes)
        {
            if(c.temFacturasEmAtraso() == true)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * obtem o total de clientes de um determinado tipo
     *
     * @return int
     */
    public int getTotalClientesDoTipo(int tipoConsumidor)
    {
        int total = 0;
        for( Consumidor c: clientes)
        {
            if(c.getTipoConsumidor() == tipoConsumidor)
            {
                total++;
            }
        } 
        return total;
    }
    
    /**
     * obtem o maior consumidor, retorna o primeiro consumidor encontrado
     * sempre que haja mais de um consumidor com o mesmo consumo
     *
     * @return Consumidor
     */
    public Consumidor obterMaiorConsumidor()
    {
        int consumo = 0;
        Consumidor c2 = null;;
        for( Consumidor c: clientes)
        {
            if(consumo < c.getConsumoActual().getConsumoActual())
            {
                consumo = c.getConsumoActual().getConsumoActual();
                c2 = c;
            }
        }
        return c2;  
    }
    
    /**
     * implementacao do metodo equals
     *
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Clientes other = (Clientes) obj;
        if (!Objects.equals(this.clientes, other.clientes)) {
            return false;
        }
        return true;
    }
    
    /**
     * implementacao do metodo hashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.clientes);
        return hash;
    }
}
