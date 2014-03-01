/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;
import java.math.BigDecimal;
/**
 *
 * @author Hiperzone
 */
public class Aguas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        //teste
        //1- Dado um numero de cliente e um consumo produzir a fatura detalhada do consumidor,
        //e armazenar os dados corresponentes a essa fatura
        
        //--Criar clientes
        Clientes clientes = new Clientes(); //client storage
        Consumidor cliente1 = new Consumidor(Consumidor.CONSUMIDOR_DOMESTICO, "Rua da china nr1", "Daniel Ramos", 123456);
        Consumidor cliente2 = new Consumidor(Consumidor.CONSUMIDOR_DOMESTICO, "Rua da china nr2", "Simão Ramos", 123457);
        clientes.addCliente(cliente1);
        clientes.addCliente(cliente2);

        //obter o consumo de junho 2012
        Consumo consumo1 = cliente1.criarNovoConsumoMensal();
        consumo1.adicionarConsumo(16);
 
        //gerar factura respectivamente ao consumo em questao
        Factura fac1 = cliente1.gerarFacturaMensal(consumo1);
        
        //print dos dados referentes ao cliente
        System.out.println(cliente1);
        System.out.println(fac1);
        
        //2 - Liquidar uma factura
        //obter a primeira factura
        try
        {
            Factura fac = cliente1.getFacturas().getFactura(1);
            if(fac.isLiquidada() == false)
            {
                System.out.println(fac);
                System.out.println("Factura não liquidada\n");
                fac.setLiquidada(true);
                System.out.println(fac);
                System.out.println("Factura liquidada\n");
            }
        }
        catch(FacturaNaoExisteException e)
        {
            System.out.println(e.getMessage());
        }
        
        
        //3 -  saber se ha clientes em atraso
        for ( Consumidor c : clientes)
        {
            if(c.temFacturasEmAtraso() == true)
            {
                System.out.printf("Consumidor %d tem facturas em atraso\n", c.getCodigoConsumidor());
            }
        }
        
        //4 - estimar o consumo para determinado mes
        //estimar o consumo de junho para o cliente1
        
        for ( Consumidor c : clientes)
        {
            int estimativa = c.estimarConsumoMensal(6);
            System.out.printf("Consumo estimado do consumidor %d para o mes de %s é de %d m3\n", c.getCodigoConsumidor(), Data.getStrMes(6), estimativa);  
        }
        
        
        //5 - saber quantos clientes domesticos ou de outro tipo existem
        int totalDomesticos = clientes.getTotalClientesDoTipo(Consumidor.CONSUMIDOR_DOMESTICO);
        int totalComerciais = clientes.getTotalClientesDoTipo(Consumidor.CONSUMIDOR_COMERCIAL);
        int totalInstituicao = clientes.getTotalClientesDoTipo(Consumidor.CONSUMIDOR_INSTITUICAO);
        
        System.out.printf( "Total Consumidores domesticos: %d\n", totalDomesticos);
        System.out.printf( "Total Consumidores comerciais: %d\n", totalComerciais);
        System.out.printf( "Total Consumidores instituicionais: %d\n", totalInstituicao);
        
        //6- identificar o maior consumidor
        Consumidor consummer = clientes.obterMaiorConsumidor();
        System.out.printf( "Maior consumidor: codigo: %d Nome: %s\n", consummer.getCodigoConsumidor(), consummer.getNome());
        
        
        //teste de excepcoes
        try
        {
            Consumo consumo = cliente1.getConsumo(7, 2012);
            cliente1.gerarFacturaMensal(consumo);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
