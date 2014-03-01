
package RFP.IO;

import RFP.Util.Utilitaries;
import java.io.IOException;

/**
 * Class que contem a estructura que permite identificar uma directoria dentro
 * do ficheiro RFP.
 */
public class RFPDirectoryHeader 
{
    private int id = 0;
    private int parentId = 0;
    private String name = "";

    public RFPDirectoryHeader()
    {

    }

    /**
     * Lê o cabeçalho que permite identificar uma directoria dentro do ficheiro
     * RFP.
     * @param file
     * @throws IOException 
     */
    public void readHeader(java.io.RandomAccessFile file) throws IOException
    {
        id = file.readInt();
        parentId = file.readInt();
        name = file.readUTF();
    }

    /**
     * Escreve o cabeçalho que permite indentificar uma directoria no ficheiro 
     * de destino.
     * @param file
     * @throws IOException 
     */
    public void writeHeader(java.io.RandomAccessFile file)
            throws IOException
    {
        file.writeInt(id);
        file.writeInt(parentId);
        file.writeUTF(name);
    }
    
    /**
     * Obtem o nome da directoria.
     * @return 
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Atribui o nome da directoria.
     * @param name 
     */
    public void setName(String name) 
    {
        this.name = name;
    }  
    
    /**
     * Obtem o Id da directoria parente.
     * @return 
     */
    public int getParentId() 
    {
        return parentId;
    }

    /**
     * Atribui o Id da directoria parente.
     * @param parentId 
     */
    public void setParentId(int parentId) 
    {
        this.parentId = parentId;
    }
    
    /**
     * Obtem o Id da directoria.
     * @return 
     */
    public int getId() 
    {
        return id;
    }

    /**
     * Atribui o Id da directoria.
     * @param id 
     */
    public void setId(int id) 
    {
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return this.getName();
    }
}
