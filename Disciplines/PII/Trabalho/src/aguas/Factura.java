/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
/**
 *
 * @author Daniel Ramos
 * 
 * todo: mudar periodo de facturacao para uma string do mes
 */
public class Factura implements IFactura {
    //dados do consumidor
    private int codigoCliente;
    private String morada;
    private String nome;
    private int codigoFactura;
    private BigDecimal totalAPagar;
    private int tipoCliente;
    private Data periodoFacturacao; //obter a string do mes aqui em vez da data
    private int consumo;
    private BigDecimal totalAPagarAgua;
    private BigDecimal totalAPagarSaneamento;
    private BigDecimal totalAPagarResiduos;
    private boolean liquidada = false;

    //detalhe do consumo
    private ArrayList<FacturaConsumoDetalhado> detalhesDeConsumo;

    
    /**
     * construtor
     *
     * 
     */
    public Factura(Consumidor consumidor, Consumo consumo)
    {
        this.detalhesDeConsumo = new ArrayList<FacturaConsumoDetalhado>();
        this.codigoCliente = consumidor.getCodigoConsumidor();
        this.morada = consumidor.getMorada();
        this.nome = consumidor.getNome();
        this.codigoFactura = Facturas.geraNovoCodigoFactura(); //pre incremental
        this.totalAPagar = new BigDecimal(0);
        this.totalAPagarAgua = new BigDecimal(0);
        this.totalAPagarSaneamento = new BigDecimal(0);
        this.totalAPagarResiduos = new BigDecimal(0);
        this.tipoCliente = consumidor.getTipoConsumidor();
        this.periodoFacturacao = consumidor.getConsumoActual().getData();
        this.consumo = consumo.getConsumoActual();
    }

    /**
     * obtem o codigo do cliente
     *
     * @return int
     */
    @Override
    public int getCodigoCliente() {
        return codigoCliente;
    }

    /**
     * atribui o codigo de cliente
     *
     * @return none
     */
    @Override
    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    /**
     * obtem o codigo da factura
     *
     * @return int
     */
    @Override
    public int getCodigoFactura() {
        return codigoFactura;
    }

    /**
     * atribui o codigo da factura
     *
     * @return none
     */
    @Override
    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    /**
     * obtem o consumo mensal
     *
     * @return int
     */
    @Override
    public int getConsumo() {
        return consumo;
    }

    /**
     * atribui o consumo mensal
     *
     * @return none
     */
    @Override
    public void setConsumo(int consumo) throws ConsumoInvalidoException{
        if(consumo < 0) throw new ConsumoInvalidoException();
        this.consumo = consumo;
    }

    /**
     * obtem a morada
     *
     * @return String
     */
    @Override
    public String getMorada() {
        return morada;
    }

    /**
     * atribui a morada actual
     *
     * @return none
     */
    @Override
    public void setMorada(String morada) {
        this.morada = morada;
    }

    /**
     * obtem o nome do consumidor
     *
     * @return String
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * atribui o nome do consumidor
     *
     * @return none
     */
    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * obtem o periodo de facturacao
     *
     * @return Data
     */
    @Override
    public Data getPeriodoFacturacao() {
        return periodoFacturacao;
    }

    /**
     * atribui o periodo de facturacao
     *
     * @return none
     */
    @Override
    public void setPeriodoFacturacao(Data periodoFacturacao) {
        this.periodoFacturacao = periodoFacturacao;
    }

    /**
     * obtem o tipo de cliente
     *
     * @return int
     */
    @Override
    public int getTipoCliente() {
        return tipoCliente;
    }

    /**
     * atribui o tipo de cliente
     *
     * @return none
     */
    @Override
    public void setTipoCliente(int tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    /**
     * retorna a arraylist contendo os detalhes do consumo
     *
     * @return ArrayList
     */
    @Override
    public ArrayList<FacturaConsumoDetalhado> getDetalhesDeConsumo() {
        return detalhesDeConsumo;
    }
    
    /**
     * adiciona o consumo detalhado de um determinado escalao e servico
     *
     * @return none
     */
    @Override
    public void addDetalhesConsumo(FacturaConsumoDetalhado f)
    {
        detalhesDeConsumo.add(f);
    }

    /**
     * retorna se a factura foi ou nao liquidada
     *
     * @return boolean
     */
    @Override
    public boolean isLiquidada() {
        return liquidada;
    }

    /**
     * atribui se a factura deve ser ou nao liquidada
     *
     * @return none
     */
    @Override
    public void setLiquidada(boolean liquidada) {
        this.liquidada = liquidada;
    }
    
    /**
     * obtem o valor total a pagar
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    /**
     * atribui o valor total a pagar
     *
     * @return none
     */
    @Override
    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    /**
     * obtem o valor total a pagar pelo consumo de agua
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalAPagarAgua() {
        return totalAPagarAgua;
    }

    /**
     * atribui o valor total a pagar pelo consumo de agua
     *
     * @return none
     */
    @Override
    public void setTotalAPagarAgua(BigDecimal totalAPagarAgua) {
        this.totalAPagarAgua = totalAPagarAgua;
    }

    /**
     * obtem o valor total a pagar pelo consumo de residuos
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalAPagarResiduos() {
        return totalAPagarResiduos;
    }

    /**
     * atribui o valor total a pagar pelo consumo de residuos
     *
     * @return none
     */
    @Override
    public void setTotalAPagarResiduos(BigDecimal totalAPagarResiduos) {
        this.totalAPagarResiduos = totalAPagarResiduos;
    }

    /**
     * obtem o valor total a pagar pelo consumo de saneamento
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalAPagarSaneamento() {
        return totalAPagarSaneamento;
    }

    /**
     * atribui o valor total a pagar pelo consumo de saneamento
     *
     * @return none
     */
    @Override
    public void setTotalAPagarSaneamento(BigDecimal totalAPagarSaneamento) {
        this.totalAPagarSaneamento = totalAPagarSaneamento;
    }

    /**
     * implementacao do metodo toString
     *
     * @return String
     */
    @Override
    public String toString() { 
        //detalhe da factura
        System.out.printf("Instalação Nr %-14d %-42s %-14s%n", codigoCliente, morada, nome);
        System.out.printf("Fatura        %-57d Total(Euros) %s\n", codigoFactura, totalAPagar);
        try
        {
            System.out.printf("Tipo Cliente  %-14s Período Faturação %s\n", Consumidor.CODIGO_TIPO_CONSUMIDOR[tipoCliente], Data.getStrMes(periodoFacturacao.getMes()) );  
        }
        catch(Exception e)
        {
            System.out.printf("Tipo Cliente  %-14s Período Faturação %s\n", Consumidor.CODIGO_TIPO_CONSUMIDOR[tipoCliente], periodoFacturacao );
        }
        System.out.printf("                                                           Consumo: %d\n", consumo);
        //loop dos detalhes da factura
        System.out.printf("              %-14s %-14s %-14s %-14s %-14s\n", "Descrição", "Esc.Tarifário", "Faturado", "Valor Unit.", "Valor");
        String Servico = "Consumo Água";
        for( FacturaConsumoDetalhado d : detalhesDeConsumo)
        {
             if(Servico.equals(d.getDescricao()))
              {
                  
              }
              else
              {
                  if(Servico.equals("Consumo Água"))
                  {
                      System.out.printf("                                                           Total:%13s\n", this.getTotalAPagarAgua());
                      Servico = "Saneamento";
                      System.out.printf("\n");
                  }
                  else if(Servico.equals("Saneamento"))
                  {
                      System.out.printf("                                                           Total:%13s\n", this.getTotalAPagarSaneamento());
                      Servico = "Resíduos";
                      System.out.printf("\n");
                  }
              }
              System.out.printf("              %-14s %-14s %-14d %-14s %-14s\n", d.getDescricao(), "[" + d.getEscalao().getLimiteInferior() + "-" + d.getEscalao().getLimiteSuperior() + "]", d.getFacturado(), d.getValorUnitario(), d.getValor());
             
        }
        System.out.printf("                                                           Total:%13s\n", this.getTotalAPagarResiduos()); 
        System.out.printf("\n");
        return "";
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
        final Factura other = (Factura) obj;
        if (this.codigoCliente != other.codigoCliente) {
            return false;
        }
        if (!Objects.equals(this.morada, other.morada)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (this.codigoFactura != other.codigoFactura) {
            return false;
        }
        if (!Objects.equals(this.totalAPagar, other.totalAPagar)) {
            return false;
        }
        if (this.tipoCliente != other.tipoCliente) {
            return false;
        }
        if (!Objects.equals(this.periodoFacturacao, other.periodoFacturacao)) {
            return false;
        }
        if (this.consumo != other.consumo) {
            return false;
        }
        if (!Objects.equals(this.totalAPagarAgua, other.totalAPagarAgua)) {
            return false;
        }
        if (!Objects.equals(this.totalAPagarSaneamento, other.totalAPagarSaneamento)) {
            return false;
        }
        if (!Objects.equals(this.totalAPagarResiduos, other.totalAPagarResiduos)) {
            return false;
        }
        if (this.liquidada != other.liquidada) {
            return false;
        }
        if (!Objects.equals(this.detalhesDeConsumo, other.detalhesDeConsumo)) {
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
        hash = 97 * hash + this.codigoCliente;
        hash = 97 * hash + Objects.hashCode(this.morada);
        hash = 97 * hash + Objects.hashCode(this.nome);
        hash = 97 * hash + this.codigoFactura;
        hash = 97 * hash + Objects.hashCode(this.totalAPagar);
        hash = 97 * hash + this.tipoCliente;
        hash = 97 * hash + Objects.hashCode(this.periodoFacturacao);
        hash = 97 * hash + this.consumo;
        hash = 97 * hash + Objects.hashCode(this.totalAPagarAgua);
        hash = 97 * hash + Objects.hashCode(this.totalAPagarSaneamento);
        hash = 97 * hash + Objects.hashCode(this.totalAPagarResiduos);
        hash = 97 * hash + (this.liquidada ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.detalhesDeConsumo);
        return hash;
    }  
}
