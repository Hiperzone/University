
package RFP.Util;

/**
 * Interface para a implementação de integridade de dados
 */
public interface IntegrityI 
{
    public long getDigest();
    public void update( byte[] data, int size);
    public void update(byte[] data);
    public void initialize();
    
}
