/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;
import java.util.ArrayList;
import java.util.Objects;
/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class Consumidor {
    public static final String[] CODIGO_TIPO_CONSUMIDOR = { "Doméstico", "Comercial", "Instituição" };
    public static final int CONSUMIDOR_DOMESTICO = 0;
    public static final int CONSUMIDOR_COMERCIAL = 1;
    public static final int CONSUMIDOR_INSTITUICAO = 2;

    private int codigoConsumidor;
    private String morada;
    private String nome;
    private int numContribuinte;
    private int tipoConsumidor;
    private ArrayList<Consumo> consumo;
    private Facturas facturas;
    
    /**
     * constructor
     *
     *
     */
    public Consumidor(int tipoConsumidor, String morada, String nome, int contribuinte)
    {
        codigoConsumidor = Clientes.gerarNovoCodigoCliente();
        this.tipoConsumidor = tipoConsumidor;
        this.morada = morada;
        this.nome = nome;
        numContribuinte = contribuinte;
        consumo = new ArrayList<Consumo>();
        facturas = new Facturas();
    }
    
    /**
     * constructor
     *
     *
     */
    public Consumidor(int tipoConsumidor, String morada, String nome, int contribuinte, Consumo consumo)
    {
        codigoConsumidor = Clientes.gerarNovoCodigoCliente();
        this.tipoConsumidor = tipoConsumidor;
        this.morada = morada;
        this.nome = nome;
        numContribuinte = contribuinte;
        this.consumo = new ArrayList<Consumo>();
        this.consumo.add(consumo);
        facturas = new Facturas();
    }
    
    /**
     * obtem o codigo do consumidor
     *
     * @return int
     */
    public int getCodigoConsumidor()
    {
        return codigoConsumidor;
    }
    
    /**
     * atribui o codigo do consumidor
     *
     * @return none
     */
    public void setCodigoConsumidor (int id)
    {
        codigoConsumidor = id;
    }
    
    /**
     * obtem a morada do consumidor
     *
     * @return String
     */
    public String getMorada()
    {
        return morada;
    }
    
    /**
     * atribui a morada
     *
     * @return none
     */
    public void setMorada (String Morada)
    {
        this.morada = Morada;
    }
    
    /**
     * obtem o nome do consumidor
     *
     * @return String
     */
    public String getNome()
    {
        return nome;
    }
    
    /**
     * atribui o nome do consumidor
     *
     * @return none
     */
    public void setNome (String Nome)
    {
        this.nome = Nome;
    }
    
    /**
     * obtem o nr contribuinte do consumidor
     *
     * @return int
     */
    public int getNumContribuinte()
    {
        return numContribuinte;
    }
    
    /**
     * atribui o nr de contribuinte do consumidor
     *
     * @return none
     */
    public void setNumContribuinte(int NumC)
    {
        numContribuinte = NumC;
    }
    
    /**
     * obtem o tipo de consumidor
     *
     * @return int
     */
    public int getTipoConsumidor()
    {
        return tipoConsumidor;
    }
    
    /**
     * atribui o tipo de consumidor
     *
     * @return none
     */
    public void setTipoConsumidor (int TipoC)
    {
        tipoConsumidor = TipoC;
    }

    /**
     * retorna a lista de consumos mensais
     *
     * @return ArrrayList
     */
    public ArrayList<Consumo> getListaConsumos() {
        return consumo;
    }
    
    /**
     * obtem o consumo baseado num mes e ano
     *
     * @return Consumo
     */
    public Consumo getConsumo(int mes, int ano) throws ConsumoNaoEncontradoException
    {
        for( Consumo c : consumo)
        {
            if(c.getData().getMes() == mes && c.getData().getAno() == ano)
                return c;
        }
        throw new ConsumoNaoEncontradoException("Consumo nao encontrado");
    }
    
    /**
     * obtem o consumo actual
     *
     * @return Consumo
     */
    public Consumo getConsumoActual() {
        Data data = new Data();
        
        for( Consumo c : consumo)
        {
            if(c.getData().getMes() == data.getMes() && c.getData().getAno() == data.getAno())
                return c;
        }
        //sem consumo mensal atribuido, criar novo consumo
        Consumo c = criarNovoConsumoMensal();
        return c;
    }
    
    /**
     * criar um novo consumo mensal
     *
     * @return Consumo
     */
    public Consumo criarNovoConsumoMensal()
    {
        Data data = new Data();
        Consumo co = new Consumo(data);
        this.consumo.add(co);
        
        return co;
    }

    /**
     * obtem as facturas do consumidor
     *
     * @return Facturas
     */
    public Facturas getFacturas() {
        return facturas;
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
        final Consumidor other = (Consumidor) obj;
        if (this.codigoConsumidor != other.codigoConsumidor) {
            return false;
        }
        if (!Objects.equals(this.morada, other.morada)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (this.numContribuinte != other.numContribuinte) {
            return false;
        }
        if (this.tipoConsumidor != other.tipoConsumidor) {
            return false;
        }
        if (!Objects.equals(this.consumo, other.consumo)) {
            return false;
        }
        if (!Objects.equals(this.facturas, other.facturas)) {
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
        int hash = 5;
        hash = 59 * hash + this.codigoConsumidor;
        hash = 59 * hash + Objects.hashCode(this.morada);
        hash = 59 * hash + Objects.hashCode(this.nome);
        hash = 59 * hash + this.numContribuinte;
        hash = 59 * hash + this.tipoConsumidor;
        hash = 59 * hash + Objects.hashCode(this.consumo);
        hash = 59 * hash + Objects.hashCode(this.facturas);
        return hash;
    }
    
    /**
     * implementacao do metodo toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return  "Consumidor{" + "CodigoConsumidor=" + codigoConsumidor + ", Morada=" + morada + ", Nome=" + nome + ", NumContribuinte=" + numContribuinte + ", TipoConsumidor=" + tipoConsumidor + "\nConsumo=" + consumo + '}';
    
    }
    
    /**
     * gear uma factura mensal
     *
     * @return Factura
     */
    public Factura gerarFacturaMensal(Consumo consumo)
    {
        return facturas.gerarFacturaMensal(this, consumo);
    }
    
    /**
     * verifica se tem facturas em atraso
     *
     * @return boolean
     */
    public boolean temFacturasEmAtraso() 
    {
        for(Factura c: facturas)
        {
            if(c.isLiquidada() == false ) return true;
        }
        return false;
    }
    
    /**
     * estima o consumo mensal de um determinado mes
     *
     * @return int
     */
    public int estimarConsumoMensal(int mes)
    {
        int estimativa = 0;
        int divisor = 0;
        for( Consumo e: consumo)
        {
            if(e.getData().getMes() == mes)
            {
                estimativa = estimativa + e.getConsumoActual();
                divisor++;
            }
        }
        if(divisor == 0) return 0;
        return (estimativa /divisor);
    }
    
    
}
