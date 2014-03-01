/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;

import java.math.BigDecimal;

/**
 *
 * @author Daniel Ramos e Simão Ramos
 */
public interface ITarifario {
    public static final String[] CODIGO_TARIFARIO = { "Doméstico", "Comercial", "Instituição" };
    public static final String[] CODIGO_SERVICO = { "Consumo Água", "Resíduos", "Saneamento" };
    
    public static final int TARIFARIO_DOMESTICO = 0;
    public static final int TARIFARIO_COMERCIAL = 1;
    public static final int TARIFARIO_INSTITUICAO = 2;
    
    public static final int SERVICO_AGUA = 0;
    public static final int SERVICO_RESIDUOS = 1;
    public static final int SERVICO_SANEAMENTO = 2;
    
    public void addEscalao(int limiteInferior, int limiteSuperior, BigDecimal preco);
    
    public int getTipoTarifario();
    public void setTipoTarifario(int codigo);
    
    public int getTipoServico();
    public void setTipoServico(int codigo);
}
