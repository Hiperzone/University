
package RFP.Util;

import java.util.zip.Checksum;

/**
 * Classe que permite calcular o CRC de um ficheiro
 * Deve ser utilizado o método update para actualizar o checksum e o
 * método getDigest para obter o CRC32.
 * 
 * É necessário fazer o casting para int quando se usa o método getDigest
 */
public class CRC32 implements IntegrityI {
    private Checksum checksum ;
    
    public CRC32()
    {
        
    }
    
    /**
     * Inicializa o algoritmo CRC32.
     */
    @Override
    public void initialize()
    {
        checksum = new java.util.zip.CRC32();
    }
    
    /**
     * Actualiza o crc ao adicionar novos dados no calculo.
     * @param data 
     */
    @Override
    public void update(byte[] data)
    {
        checksum.update(data, 0, data.length);
    }
    
    /**
     * Actualiza o crc ao adicionar novos dados no calculo.
     * Tem em conta o tamanho mesmo que o buffer seja maior.
     * @param data
     * @param size 
     */
    @Override
    public void update( byte[] data, int size)
    {
        checksum.update(data, 0, size); 
    }
    
    /**
     * Obtem a digestão final do calculo crc.
     * @return crc32
     */
    @Override
    public long getDigest()
    {
         return checksum.getValue();
    }
    
    /**
     * Retorna um valor long em hexadecimal.
     * @param result
     * @return 
     */
    public String toString(long result)
    {
         return Long.toHexString(result);
    }
    
    @Override
    public String toString()
    {
        return toString(getDigest());
    }
}
