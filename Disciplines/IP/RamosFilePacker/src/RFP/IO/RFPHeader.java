
package RFP.IO;

import java.io.IOException;

/**
 * Class que contem a estructura que permite identificar um ficheiro RFP.
 */
public class RFPHeader 
{
    private int version = 0;
    private long fileListOffset = 0;
    private long directoryListOffset = 0;
    private int flags = 0;

    public RFPHeader()
    {

    }

    /**
     * Lê o cabeçalho que permite identificar um ficheiro RFP.
     * @param file
     * @throws IOException 
     */
    public void readHeader(java.io.RandomAccessFile file) throws IOException
    {
        version = file.readInt();
        fileListOffset = file.readLong();
        directoryListOffset = file.readLong();
        flags = file.readInt();

    }

    /**
     * Escreve o cabeçalho que permite indentificar um ficheiro RFP.
     * @param file
     * @throws IOException 
     */
    public void writeHeader(java.io.RandomAccessFile file)
            throws IOException
    {
        file.writeInt(version);
        file.writeLong(fileListOffset);
        file.writeLong(directoryListOffset);
        file.writeInt(flags);
    }

    /**
     * Obtem a versão do ficheiro RFP.
     * @return 
     */
    public int getVersion() 
    {
        return version;
    }
    
    /**
     * Atribui a versão do ficheiro RFP.
     * @param version 
     */
    public void setVersion(int version) 
    {
        this.version = version;
    }

    /**
     * Obtem o offset que corresponde ao inicio da listagem de ficheiros.
     * @return 
     */
    public long getFileListOffset() 
    {
        return fileListOffset;
    }

    /*
     * Atribui o offset que corresponde ao inicio da listagem de ficheiros.
     */
    public void setFileListOffset(long fileListOffset) 
    {
        this.fileListOffset = fileListOffset;
    }

    /**
     * Obtem o offset que corresponde ao inicio da listagem de directorias.
     * @return 
     */
    public long getDirectoryListOffset() 
    {
        return directoryListOffset;
    }
    
    /**
     * Atribui o offset que corresponde ao inicio da listagem de directorias.
     * @param directoryListOffset 
     */
    public void setDirectoryListOffset(long directoryListOffset) 
    {
        this.directoryListOffset = directoryListOffset;
    }

    /**
     * Obtem as flags do ficheiro RFP.
     * @return 
     */
    public int getFlags() 
    {
        return flags;
    }
    
    /**
     * Atribui as flags do ficheiro RFP.
     * @param flags 
     */
    public void setFlags(int flags) 
    {
        this.flags = flags;
    }    
}
