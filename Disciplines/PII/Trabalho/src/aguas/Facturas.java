/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public class Facturas implements Iterable<Factura>{
    private static int CODIGO_FACTURA = 0; //gerador de codigo de facturas
    private ArrayList<Factura> facturas;
    
    /**
     * construtor
     *
     *
     */
    public Facturas()
    {
        facturas = new ArrayList<Factura>();
    }
    
    /**
     * obtem o iterator das facturas
     *
     * @return Iterator
     */
    @Override
    public Iterator iterator()
    {
        return facturas.iterator();
    }

    /**
     * implementacao do metodo toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Facturas{" + "facturas=" + facturas + '}';
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
        final Facturas other = (Facturas) obj;
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
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.facturas);
        return hash;
    }

    /**
     * gera um novo codigo de factura
     *
     * @return valor de CODIGO_FACTURA
     */
    public static int geraNovoCodigoFactura()
    {
        CODIGO_FACTURA++;
        return CODIGO_FACTURA;
    }
    
    /**
     * gera a factura mensal actual
     *
     * @return Factura
     */
    public Factura gerarFacturaMensal(Consumidor consumidor, Consumo consumo)
    {
        Factura factura = new Factura(consumidor, consumo);

        if(consumidor.getTipoConsumidor() == Consumidor.CONSUMIDOR_DOMESTICO)
        {
            Tarifario tarifarioAgua = new Tarifario(Tarifario.TARIFARIO_DOMESTICO, Tarifario.SERVICO_AGUA);
            tarifarioAgua.addEscalao(0, 5, new BigDecimal(0.34).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(6, 8, new BigDecimal(0.41).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(9, 11, new BigDecimal(0.64).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(12, 14, new BigDecimal(0.83).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(15, 17, new BigDecimal(1.16).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(18, 20, new BigDecimal(1.32).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(21, 23, new BigDecimal(1.40).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(23, Escalao.MAX_CONSUMO, new BigDecimal(1.50).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioAgua, consumo);
            
            Tarifario tarifarioSaneamento = new Tarifario(Tarifario.TARIFARIO_DOMESTICO, Tarifario.SERVICO_SANEAMENTO);
            tarifarioSaneamento.addEscalao(0, 5, new BigDecimal(0.25).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioSaneamento.addEscalao(6, 8, new BigDecimal(0.25).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioSaneamento.addEscalao(9, 11, new BigDecimal(0.50).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioSaneamento.addEscalao(12, 14, new BigDecimal(0.50).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioSaneamento.addEscalao(15, Escalao.MAX_CONSUMO, new BigDecimal(0.75).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioSaneamento, consumo);
            
            Tarifario tarifarioResiduos = new Tarifario(Tarifario.TARIFARIO_DOMESTICO, Tarifario.SERVICO_RESIDUOS);
            tarifarioResiduos.addEscalao(0, 5, new BigDecimal(0.0).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioResiduos.addEscalao(6, 8, new BigDecimal(0.20).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioResiduos.addEscalao(9, Escalao.MAX_CONSUMO, new BigDecimal(0.30).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioResiduos, consumo);
          
            facturas.add(factura);
        } 
        if(consumidor.getTipoConsumidor() == Consumidor.CONSUMIDOR_COMERCIAL)
        {
            Tarifario tarifarioAgua = new Tarifario(Tarifario.TARIFARIO_COMERCIAL, Tarifario.SERVICO_AGUA);
            tarifarioAgua.addEscalao(0, 100, new BigDecimal(0.93).setScale(2, BigDecimal.ROUND_HALF_UP));
            tarifarioAgua.addEscalao(101, Escalao.MAX_CONSUMO, new BigDecimal(1.02).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioAgua, consumo);
            
            Tarifario tarifarioSaneamento = new Tarifario(Tarifario.TARIFARIO_COMERCIAL, Tarifario.SERVICO_SANEAMENTO);
            tarifarioSaneamento.addEscalao(0, Escalao.MAX_CONSUMO, new BigDecimal(0.75).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioSaneamento, consumo);
            
            Tarifario tarifarioResiduos = new Tarifario(Tarifario.TARIFARIO_COMERCIAL, Tarifario.SERVICO_RESIDUOS);
            tarifarioResiduos.addEscalao(0, Escalao.MAX_CONSUMO, new BigDecimal(0.40).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioResiduos, consumo);
            facturas.add(factura);   
        }
        
        if(consumidor.getTipoConsumidor() == Consumidor.CONSUMIDOR_INSTITUICAO)
        {
            Tarifario tarifarioAgua = new Tarifario(Tarifario.TARIFARIO_INSTITUICAO, Tarifario.SERVICO_AGUA);
            tarifarioAgua.addEscalao(0, Escalao.MAX_CONSUMO, new BigDecimal(0.61).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioAgua, consumo);
            
            Tarifario tarifarioSaneamento = new Tarifario(Tarifario.TARIFARIO_INSTITUICAO, Tarifario.SERVICO_SANEAMENTO);
            tarifarioSaneamento.addEscalao(0, Escalao.MAX_CONSUMO, new BigDecimal(0.50).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioSaneamento,consumo);
            
            Tarifario tarifarioResiduos = new Tarifario(Tarifario.TARIFARIO_INSTITUICAO, Tarifario.SERVICO_RESIDUOS);
            tarifarioResiduos.addEscalao(0, Escalao.MAX_CONSUMO, new BigDecimal(0.40).setScale(2, BigDecimal.ROUND_HALF_UP));
            gerarDetalhesConsumo(factura, consumidor, tarifarioResiduos, consumo);
            facturas.add(factura);  
        }
        return factura;
    }
    
    /**
     * adiciona uma nova factura
     *
     * @return none
     */
    public void add(Factura fac)
    {
        facturas.add(fac);
    }
    
    /**
     * obtem uma factura caso exista baseado no codigo
     *
     * @return Factura
     */
    public Factura getFactura(int codigo) throws FacturaNaoExisteException
    {
        for(Factura fac: facturas)
        {
            if(fac.getCodigoFactura() == codigo)
            {
                return fac;
            }
        }
        //return null;
        throw new FacturaNaoExisteException("A Factura não existe");
    }
    
    /**
     * remove uma factura caso exista baseado no codigo
     *
     * @return none
     */
    public void remove(int codigo) throws FacturaNaoExisteException
    {
        Factura fac = getFactura(codigo);
        if(fac != null)
        {
            facturas.remove(fac);
        }
    }
    
    /**
     * remove uma factura obtida anteriormente e caso exista
     *
     * @return none
     */
    public void remove(Factura fac) throws FacturaNaoExisteException
    {
        if(facturas.contains(fac) == true)
        {
            facturas.remove(fac);
        }
        throw new FacturaNaoExisteException();
    }
    
    /**
     * liquida uma factura baseado no codigo
     *
     * @return none
     */
    public void liquidarFactura(int codigo) throws FacturaNaoExisteException
    {
        Factura fac = getFactura(codigo);
        if(fac != null)
        {
            if(fac.isLiquidada() == false)
            {
                fac.setLiquidada(true);
            }
        }
    }
    
    /**
     * gera o consumo de cada servico em detalhe
     * adicionando o resultado a factura
     * 
     * @return none
     */
    public void gerarDetalhesConsumo(Factura f, Consumidor c, Tarifario t, Consumo co)
    {
        //gerar detalhe da factura e os respectivos preços
        //loop dos escaloes dos respectivos tarifarios
        int consumototal = co.getConsumoActual();
        for( Escalao e: t)
        {
            int facturado = 0;
            for(int i = e.getLimiteSuperior(); i >= e.getLimiteInferior(); i--)
            {
                int diferenca = i - consumototal;
                //diferenca é positiva, nao existe consumo para este escalao
                if(diferenca > 0 || i == 0) continue; 
                //diferenca negativa ou igual a 0, existe consumo para o escalao
                if(diferenca <= 0) facturado++;
            }
            //facturado calculado
            if(facturado > 0)
            {
                FacturaConsumoDetalhado detalhe = new FacturaConsumoDetalhado();
                detalhe.setDescricao(Tarifario.CODIGO_SERVICO[t.getTipoServico()]);
                detalhe.setFacturado(facturado);
               
                //calcular preço
                BigDecimal transacao = new BigDecimal(facturado);
                detalhe.setValor(transacao.multiply(e.getPreco()));
                
                detalhe.setValorUnitario(e.getPreco());
                detalhe.setEscalao(e);
                f.addDetalhesConsumo(detalhe);
                //actualizar preco calculado
                if(t.getTipoServico() == Tarifario.SERVICO_AGUA)
                {
                    
                    f.setTotalAPagar(f.getTotalAPagar().add(detalhe.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP) );
                    f.setTotalAPagarAgua(f.getTotalAPagarAgua().add(detalhe.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP) );
                }
                
                if(t.getTipoServico() == Tarifario.SERVICO_RESIDUOS)
                {
                    f.setTotalAPagar(f.getTotalAPagar().add(detalhe.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    f.setTotalAPagarResiduos(f.getTotalAPagarResiduos().add(detalhe.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                
                if(t.getTipoServico() == Tarifario.SERVICO_SANEAMENTO)
                {
                    f.setTotalAPagar(f.getTotalAPagar().add(detalhe.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    f.setTotalAPagarSaneamento(f.getTotalAPagarSaneamento().add(detalhe.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }
}
