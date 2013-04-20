
package RFP.Util;

/**
 * Class que permite calcular a potencia que corresponde a um tamanho
 * de dados.
 */
public class ByteUnitConverter {
    
    /**
     * Obtem a sigla correcta baseado na quantidade de bytes.
     * @param bytes
     * @param si
     * @return 
     */
    public static String convertTo(long bytes, boolean si) 
    {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    } 
}
