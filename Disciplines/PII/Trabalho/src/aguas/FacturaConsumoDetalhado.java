/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;

import java.math.BigDecimal;
import java.util.Objects;
/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class FacturaConsumoDetalhado {
    private String descricao = "Desconhecido";
    private Escalao escalao = null;
    private int facturado = 0;
    private BigDecimal valorUnitario;
    private BigDecimal valor;
    
    /**
     * constructor
     *
     * 
     */
    public FacturaConsumoDetalhado()
    {
        valorUnitario = new BigDecimal(0);
        valor = new BigDecimal(0);
    }
    
    /**
     * retorna a descricao do servico
     *
     * @return String
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * atribui a descricao do servico
     *
     * @return none
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * obtem o escalao do servico
     *
     * @return Escalao
     */
    public Escalao getEscalao() {
        return escalao;
    }
    
    /**
     * atribui o escalao do servico
     *
     * @return none
     */
    public void setEscalao(Escalao escalao) {
        this.escalao = escalao;
    }

    /**
     * obtem o consumo facturado para o escalao e servico actual
     *
     * @return int
     */
    public int getFacturado() {
        return facturado;
    }
    
    /**
     * atribui o valor facturado para o escalao e servico actual
     *
     * @return none
     */
    public void setFacturado(int facturado) {
        this.facturado = facturado;
    }

    /**
     * obtem o preco calculado
     *
     * @return BigDecimal
     */
    public BigDecimal getValor() {
        return valor;
    }
    
    /**
     * atribui o preco calculado
     *
     * @return none
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * obtem o valor unitario do escalao
     *
     * @return BigDecimal
     */
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    
    /**
     * atribui o valor unitario do escalao
     *
     * @return none
     */
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    /**
     * implementacao do metodo toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "FacturaConsumoDetalhado{" + "descricao=" + descricao + ", escalao=" + escalao + ", facturado=" + facturado + ", valorUnitario=" + valorUnitario + ", valor=" + valor + '}';
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
        final FacturaConsumoDetalhado other = (FacturaConsumoDetalhado) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.escalao, other.escalao)) {
            return false;
        }
        if (this.facturado != other.facturado) {
            return false;
        }
        if (!Objects.equals(this.valorUnitario, other.valorUnitario)) {
            return false;
        }
        if (!Objects.equals(this.valor, other.valor)) {
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
        hash = 19 * hash + Objects.hashCode(this.descricao);
        hash = 19 * hash + Objects.hashCode(this.escalao);
        hash = 19 * hash + this.facturado;
        hash = 19 * hash + Objects.hashCode(this.valorUnitario);
        hash = 19 * hash + Objects.hashCode(this.valor);
        return hash;
    } 
}
