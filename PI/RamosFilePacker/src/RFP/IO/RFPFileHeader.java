/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RFP.IO;

import RFP.Util.Utilitaries;
import java.io.IOException;

/**
 * Class que contem a estructura que permite identificar um ficheiro dentro do
 * ficheiro RFP.
 */
public class RFPFileHeader 
{
    private int filenameId = 0;
    private long dataOffset = 0;
    private int crc32 = 0;
    private int flags = 0;
    private int directoryId = 0;
    private long size = 0;
    private String name = "";

    public RFPFileHeader()
    {

    }

    /**
     * Lê o cabeçalho que permite identificar um ficheiro dentro do ficheiro
     * RFP.
     * @param file
     * @throws IOException 
     */
    public void readHeader(java.io.RandomAccessFile file) throws IOException
    {
        filenameId = file.readInt();
        dataOffset = file.readLong();
        crc32 = file.readInt();
        flags = file.readInt();
        directoryId = file.readInt();
        size = file.readLong();
        name = file.readUTF();
    }

    /**
     * Escreve o cabeçalho que permite indentificar um ficheiro no ficheiro 
     * de destino.
     * @param file
     * @throws IOException 
     */
    public void writeHeader(java.io.RandomAccessFile file) throws IOException
    {
        file.writeInt(filenameId);
        file.writeLong(dataOffset);
        file.writeInt(crc32);
        file.writeInt(flags);
        file.writeInt(directoryId);
        file.writeLong(size);
        file.writeUTF(name);
      //  file.write(0);
    }
    
    /**
     * Obtem o nome do ficheiro.
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Atribui o nome do ficheiro.
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Obtem o Id do ficheiro.
     * @return 
     */
    public int getFilenameId() 
    {
        return filenameId;
    }

    /**
     * Atribui o Id do ficheiro.
     * @param filenameId 
     */
    public void setFilenameId(int filenameId) 
    {
        this.filenameId = filenameId;
    }

    /**
     * Obtem o offset que contem o inicio dos dados deste ficheiro.
     * @return 
     */
    public long getDataOffset() 
    {
        return dataOffset;
    }

    /**
     * Atribui o offset que contem o inicio dos dados deste ficheiro.
     * @param dataOffset 
     */
    public void setDataOffset(long dataOffset) 
    {
        this.dataOffset = dataOffset;
    }

    /**
     * Obtem o crc deste ficheiro.
     * @return 
     */
    public int getCrc32() 
    {
        return crc32;
    }

    /**
     * Atribui o crc deste ficheiro.
     * @param crc32 
     */
    public void setCrc32(int crc32) 
    {
        this.crc32 = crc32;
    }

    /**
     * Obtem as flags deste ficheiro.
     * @return 
     */
    public int getFlags() 
    {
        return flags;
    }

    /**
     * Atribui as flags deste ficheiro.
     * @param flags 
     */
    public void setFlags(int flags) 
    {
        this.flags = flags;
    }

    /**
     * Obtem o Id da directoria que contem este ficheiro.
     * @return 
     */
    public int getDirectoryId() 
    {
        return directoryId;
    }

    /**
     * Atribui o Id da directoria que contem este ficheiro.
     * @param directoryId 
     */
    public void setDirectoryId(int directoryId) 
    {
        this.directoryId = directoryId;
    }

    /**
     * Obtem o tamanho do ficheiro.
     * @return 
     */
    public long getSize() 
    {
        return size;
    }

    /**
     * Atribui o tamanho do ficheiro.
     * @param size 
     */
    public void setSize(long size) 
    {
        this.size = size;
    }
    
    @Override
    public String toString()
    {
        return this.getName();
    }
}
