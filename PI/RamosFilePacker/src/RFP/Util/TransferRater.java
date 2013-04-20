
package RFP.Util;

/**
 * Class que calcula a velocidade de transferência de um ficheiro.
 * O valor calculado é o valor estimado em cada segundo e não e média desde do
 * início até ao fim.
 */
public class TransferRater 
{
    private long lastTime; //ultima vez que o tempo foi actualizado
    private long currTime; //tempo actual
    private long elapsedTime; //tempo que passou desde a ultima actualizacao
    private long transferRate; //taxa de transferencia actual
    private long totalTransfered; //total de bytes transferidos até ao momento
    private long bytesTransfered; //total de bytes transferidos em 1 segundo
    private long length; //tamanho do ficheiro
    
    public TransferRater(long fileSize)
    {
        //calcular a velocidade de transferencia
        // 1s = 10^9 nanoseconds
        lastTime =  System.nanoTime();
        currTime =  System.nanoTime();
        elapsedTime = 0;
        bytesTransfered = 0;
        transferRate = 0;
        length = fileSize;
    }
    
    /**
     * Calcula a velocidade de transferência a partir da quantidade de bytes
     * transferidos naquele momento.
     * @param bytesTransfered
     * @return 
     */
    public boolean calculate(long bytesTransfered)
    {
        this.bytesTransfered = bytesTransfered; 
        currTime = System.nanoTime();
        elapsedTime = currTime - lastTime;
        if(elapsedTime >= 1000000000)
        {
            totalTransfered += bytesTransfered;
            transferRate = this.bytesTransfered;
            show();
            this.bytesTransfered = 0;
            lastTime =  System.nanoTime();
            return true;
        }
        
        transferRate += this.bytesTransfered;
        return false;
    }
    
    /**
     * Calcula a velocidade de transferência no fim caso a transferência do
     * ficheiro tenha levado menos de um segundo.
     * @param bytesTransfered 
     */
    public void calculateAtEnd(long bytesTransfered)    
    {
        //dados foram transferidos em menos de 1 segundo
        if(this.bytesTransfered > 0)
        {
            //calcular a transfer rate baseado no tempo 
            totalTransfered += bytesTransfered;
            currTime =  System.nanoTime();
            elapsedTime = currTime - lastTime;
            transferRate = 1000000000 * this.bytesTransfered / elapsedTime;
            show();
        }
    }
    
    /**
     * Mostra os dados da transferência actual.
     */
    private void show()
    {
        System.out.printf("Total: %s of %s, Bytes Transfered/s %d, "
                        + "TransferRate: %s/s\n", 
                        ByteUnitConverter.convertTo(totalTransfered, false),
                        ByteUnitConverter.convertTo(length, false),
                        bytesTransfered, 
                        ByteUnitConverter.convertTo(transferRate, false) );
    }
}
