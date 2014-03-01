/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aguas;
import java.util.Calendar;
/**
 *
 * @author Daniel Ramos e Simão Ramos
 * Todo: compareTo
 */
public class Data implements Comparable<Data>{
    private static final String[] MES = {"", "Janeiro", "Fevereiro", "Março", "Abril", 
        "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", 
        "Dezembro" };
    private int mes = 1;
    private int ano = 1990;
    private int dia = 22; //dia de facturacao
    
   
    /**
     * constructor
     *
     *
     */
    public Data()
    {
        Calendar cal = Calendar.getInstance();
        mes = cal.get(Calendar.MONTH) + 1;
        ano = cal.get(Calendar.YEAR); 
    }
    
    /**
     * constructor
     *
     *
     */
    public Data( int Dia, int Mes, int Ano)
    {
        this.dia = Dia;
        this.mes = Mes;
        this.ano = Ano;
    }
    
    /**
     * constructor
     *
     *
     */
    public Data( int Mes, int Ano)
    {
        this.mes = Mes;
        this.ano = Ano;
    }
    
     /**
     * obtem a string referente a um mes valido
     *
     * @return String
     */
    public static String getStrMes(int mes) throws MesInvalidoException
    {
        if(mes < 1 && mes > 12) 
            throw new MesInvalidoException("Mes invalido");
        else
            return MES[mes];
    }
    
    /**
     * implementacao do metodo toString
     *
     * @return String
     */
    
    @Override
    public String toString()
    {
        try
        { 
            return  ( dia + " " + getStrMes(mes) + " " + ano);
        }
        catch(Exception e)
        {
            return ( dia + " " + mes + " " + ano);
        }
    }

    /**
     * obtem o ano
     *
     * @return int
     */
    public int getAno() {
        return ano;
    }

    /**
     * obtem o dia
     *
     * @return int
     */
    public int getDia() {
        return dia;
    }
    
    /**
     * obtem o mes
     *
     * @return int
     */
    public int getMes() {
        return mes;
    }

    /**
     * atribui o valor do ano
     *
     * @return none
     */
    public void setAno(int Ano) {
        this.ano = Ano;
    }

    /**
     * atribui o valor do dia
     *
     * @return none
     */
    public void setDia(int Dia) {
        this.dia = Dia;
    }

    /**
     * atribui o valor do mes
     *
     * @return none
     */
    public void setMes(int Mes) {
        this.mes = Mes;
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
        final Data other = (Data) obj;
        if (this.mes != other.mes) {
            return false;
        }
        if (this.ano != other.ano) {
            return false;
        }
        if (this.dia != other.dia) {
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
        int hash = 7;
        hash = 37 * hash + this.mes;
        hash = 37 * hash + this.ano;
        hash = 37 * hash + this.dia;
        return hash;
    }
    
    /**
     *implementacao do metodo clone
     *
     * @return Data
     */
    @Override
    public Data clone()
    {
        return new Data(dia, mes, ano);
    }   
    
    /**
     * implementacao do metodo compareTo
     *
     * @return int
     */
    @Override
    public int compareTo(Data data)
    {
        int distancia = ( (ano * 1000 + mes * 100 + dia) - (data.getAno() * 1000 + data.getMes() * 100 + data.getDia()) );
        
        if(distancia < 0) return -1;
        if(distancia > 0) return 1;
        else
        {
           return 0;
        }
    }
    
}
