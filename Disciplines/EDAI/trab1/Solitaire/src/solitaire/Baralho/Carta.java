package solitaire.Baralho;

  public class Carta 
  {
    public enum Valor { ÁS, DOIS, TRÊS, QUATRO, CINCO, SEIS,
        SETE, OITO, NOVE, DEZ, VALETE, DAMA, REI }

    public enum Naipe { ESPADAS, OUROS, PAUS, COPAS  }

    private final Valor o_valor;
    private final Naipe o_naipe;
    
    private boolean voltadaParaCima = false;
    
    public Carta()
    {
      o_valor=Valor.ÁS;
      o_naipe=Naipe.COPAS;
    }
    
    public Carta(Valor v, Naipe n) 
    {
        this.o_valor = v;
        this.o_naipe = n;
    }
    
    public boolean isVoltadaParaCima() 
    {
        return voltadaParaCima;
    }

    public void setVoltadaParaCima(boolean voltadaParaCima) 
    {
        this.voltadaParaCima = voltadaParaCima;
    }

    public Valor valor() { return o_valor; }
    public Naipe naipe() { return o_naipe; }
    
      @Override
    public boolean equals(Object x)
    {
      if(this==x)
        return true;
      if(x==null ||x.getClass()!=this.getClass())
        return false;
      Carta c=(Carta) x;
      return c.valor().equals(this.valor()) && c.naipe().equals(this.naipe());
    }
    
      @Override
    public String toString() { return o_valor+ " de " + o_naipe; }
}
