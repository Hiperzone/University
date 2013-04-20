
package RFP.Util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * Class que contem diversos métodos de assistência a outras classes.
 */
public class Utilitaries 
{
    public final static String rfp = "rfp";

    /**
     * Obtem a extensão de um ficheiro.
     * @param f
     * @return 
     */
    public static String getExtension(File f) 
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) 
        {
            ext = s.substring(i+1).toLowerCase();
        }
        
        return ext;
    }
    
    /**
     * le uma string terminada em null de um ficheiro.
     * @param file
     * @return
     * @throws IOException 
     */
    public static String readString(RandomAccessFile file) throws IOException
    {
        String str = new String();
        byte buffer = file.readByte();
        while( buffer != 0)
        {
            str += (char)buffer;
            buffer = file.readByte();
        }
        return str;   
    }
}
