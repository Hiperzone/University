
package RFP.IO;
import RFP.Util.ByteUnitConverter;
import RFP.Util.CRC32;
import RFP.Util.TransferRater;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.io.RandomAccessFile;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.Iterator;
import java.util.zip.DataFormatException;


/**
 * Class que contem os métodos que permitem manipular o conteudo de um ficheiro
 * RFP.
 * 
 */
public class RFPBinaryFile implements RFPBinaryFileI
{
    public static final int FILE_BLOCK_SIZE = 8192; //NTFS block size
    public static final int RFP_DIRECTORY_ROOT = 0;
    public static final int BASE_OFFSET = 0x00000000;
    private LinkedList<RFPFileHeader> fileList;
    private LinkedList<RFPDirectoryHeader> directoryList;
    private RandomAccessFile rfpFile;
    private int genDirectoryId = RFP_DIRECTORY_ROOT + 1; //0 = root por defeito
    private int genFileId = 0;
    private RFPHeader header;
    private String filename;
    private String nativePath = "";
    private int currDirectory = RFP_DIRECTORY_ROOT;
        
    /**
     * Abre um ficheiro rfp dado o seu path e ficheiro.
     * @param filename
     * @param nativePath
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException 
     */
    public RFPBinaryFile(String filename, String nativePath) 
            throws FileNotFoundException, IOException, RFPException
    {
        this.filename = filename;
        this.nativePath = nativePath;
        
        //iniciar as listas
        fileList = new LinkedList<RFPFileHeader>();
        directoryList = new LinkedList<RFPDirectoryHeader>();
    
        header = new RFPHeader();
    }
    
    /**
     * Obtem a lista de ficheiros contidos neste ficheiro RFP.
     * @return 
     */
    @Override
    public LinkedList<RFPFileHeader> getFileList()
    {
        return fileList;
    }
    
    /**
     * Obtem a lista de directorias contidas neste ficheiro RFP.
     * @return 
     */
    @Override
    public LinkedList<RFPDirectoryHeader> getDirectoryList()
    {
        return directoryList;
    }
    /**
     * retorna uma directoria dado o ID.
     * @param Id
     * @return 
     */
    @Override
    public RFPDirectoryHeader getDirectoryId(int Id)
    {
        for(RFPDirectoryHeader dir: directoryList)
        {
            if (dir.getId() == Id)
            {
                return dir;
            } 
        }
        return null; 
    }
    
    /**
     * Verifica se existe uma directoria com o mesmo nome dentro de uma
     * directoria especifica.
     * @param dirId
     * @param name
     * @return 
     */
    @Override
    public boolean directoryNameExists(int dirId, String name)
    {
        for(RFPDirectoryHeader dir : directoryList)
        {
            if(dir.getParentId() == dirId && dir.getName().equals(name))
            {
                return true;
            }
        }
        return false;  
    }
    
    /**
     * Verifica se existe um ficheiro com o mesmo nome dentro de uma directoria
     * especifica.
     * @param dirId
     * @param name
     * @return 
     */
    @Override
    public boolean filenameExists(int dirId, String name)
    {
        for( RFPFileHeader file : fileList)
        {
            if(file.getDirectoryId() == dirId && file.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtem a directoria actual.
     * @return 
     */
    @Override
    public int getCurrDirectory() 
    {
        return currDirectory;
    }

    /**
     * Atribui a directoria actual.
     * @param currDirectory 
     */
    @Override
    public void setCurrDirectory(int currDirectory) 
    {
        this.currDirectory = currDirectory;
    }
    
    /**
     * Conta quantos ficheiros e directorias existem numa determinada 
     * directoria.
     * @param parent
     * @return 
     */
    @Override
    public int countDirFilesAtParent(int parent)
    {
        int i = 0;
        for(RFPDirectoryHeader dir : directoryList )
        {
            if(dir.getParentId() == parent)
            {
                i++;
            }
        }
        
        for(RFPFileHeader dir : fileList )
        {
            if(dir.getDirectoryId() == parent)
            {
                i++;
            }
        }
        return i;
    }
    
    /**
     * Abre o ficheiro designado no constructor.
     * Se houver algum ficheiro RFP aberto, este será fechado para evitar
     * conflito de recursos a serem usados.
     * @throws FileNotFoundException 
     */
    @Override
    public void openRFP() throws FileNotFoundException, IOException
    {
        closeRFP();
        rfpFile = new RandomAccessFile(nativePath + "\\" + filename, "rw"); 
    }
    
    /**
     * Fecha um ficheiro RFP caso exista um aberto.
     * @throws IOException 
     */
    @Override
    public void closeRFP() throws IOException
    {
        if(rfpFile != null)
        {
            rfpFile.close();
        } 
    }
    
    /**
     * Verifica se o ficheiro RFP actual está fechado.
     * @return 
     */
    @Override
    public boolean isClosed()
    {
        if(rfpFile != null)
        {
            return !rfpFile.getChannel().isOpen();
        } 
        return true;
    }

    /**
     * Le o conteudo de um ficheiro rfp.
     * Apenas é carregado os cabeçalhos dos ficheiros e directorias.
     * 
     * Lança uma excepção caso a versão não seja valida.
     * @throws IOException
     * @throws RFPException 
     */
    @Override
    public void readContents() throws IOException, RFPException
    {
        clearLists();
        //get header
        rfpFile.seek(0x00);
        header.readHeader(rfpFile);
        
        if(header.getVersion() != RFP_VERSION) 
        {
            throw new RFPException(RFPException.EXCEPTION_VERSION_MISMATCH);
        }

        //jump to file list offset
        rfpFile.seek(header.getFileListOffset());
        
        //read num files
        int numFiles = rfpFile.readInt();
        RFPFileHeader fHeader = null;
        for(int i = 0; i < numFiles; i++)
        {
            fHeader = new RFPFileHeader();
            fHeader.readHeader(rfpFile);
            fileList.add(fHeader);
            if(fHeader.getFilenameId() >= this.genFileId) 
            {
                genFileId = fHeader.getFilenameId() + 1;
            }
        }
        
        //read directory list
        RFPDirectoryHeader dHeader = null;
        int numDirectories = rfpFile.readInt();
        for(int i = 0; i < numDirectories; i++)
        {
            dHeader = new RFPDirectoryHeader();
            dHeader.readHeader(rfpFile);
            directoryList.add(dHeader);
            
            if(dHeader.getId() >= this.genDirectoryId) 
            {
                genDirectoryId = dHeader.getId() + 1;
            }
        }
    }
    
    /**
     * Gera um ficheiro rfp vazio.
     * @throws IOException 
     */
    @Override
    public void generateEmptyFile() throws IOException
    {
        deleteRFPFile();
        openRFP();
        RFPHeader header = new RFPHeader();
        header.setVersion(RFP_VERSION);
        header.setFlags(RFP_NO_FLAG);
        header.setFileListOffset( BASE_OFFSET + 24 ); 
        header.setDirectoryListOffset( BASE_OFFSET + 28 );
        header.writeHeader(rfpFile);
        
        //num files
        rfpFile.writeInt(0);
        //num directories
        rfpFile.writeInt(0);
    }
    
    /**
     * Adiciona um ficheiro que será comprimido.
     * 
     * O CRC32 neste caso não é possivel de calcular e será usado o Adler
     * calculado pelo deflater.
     * @param file
     * @param flags
     * @throws FileNotFoundException
     * @throws IOException
     * @todo renomeacao de ficheiro
     */
    @Override
    public void addCompressedFile(String file, String path, int flags) throws 
            FileNotFoundException, IOException, RFPException
    {
        //verificacao de duplicados
        if(this.filenameExists(currDirectory, file))
        {
            throw new RFPException(RFPException.EXCEPTION_FILE_ALREADY_EXISTS);
        }
        
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        RandomAccessFile iFile =  new RandomAccessFile(path + "\\" + file, "r");
        
        int blocksize = calculateBlockSize(iFile.length());
        byte[] buffer = new byte[blocksize];
        byte[] flaterBuffer = new byte[blocksize];
        long offset = 0;
        long bytesTransfered = 0;
        int bytesCompressed = 0;
        
        rfpFile.seek(header.getFileListOffset());
        
        TransferRater calculatedTransfer = new TransferRater(iFile.length());
        //transferir os dados a serem comprimidos
        while (offset != iFile.length())
        {   
            if(iFile.length() - offset > blocksize)
            {
                offset += iFile.read(buffer);
                deflater.setInput(buffer);
                bytesTransfered += blocksize;
            }
            else
            {
                //remaining bytes
                buffer = new byte[(int)iFile.length() - (int)offset];
                offset += iFile.read(buffer);
                deflater.setInput(buffer);
                bytesTransfered += buffer.length;
            }
            if(calculatedTransfer.calculate(bytesTransfered))
                bytesTransfered = 0;
            
            while (!deflater.needsInput()) 
            {
                 bytesCompressed = deflater.deflate(flaterBuffer);
                 if(bytesCompressed > 0)
                 {
                    rfpFile.write(flaterBuffer, 0, bytesCompressed);
                 }
            }   
        }
        iFile.close();
        calculatedTransfer.calculateAtEnd(bytesTransfered);
 
        deflater.finish();
        
        //adicionar os dados comprimidos ao rfp
        buffer = new byte[blocksize];
      
        while(!deflater.finished())
        {
            bytesCompressed = deflater.deflate(buffer);
            if(bytesCompressed > 0)
            {
               rfpFile.write(buffer, 0, bytesCompressed);
            } 
        }
        
        //cria o novo cabeçalho para o ficheiro que foi comprimido.
        RFPFileHeader fHeader = new RFPFileHeader();
        fHeader.setFilenameId(genFileId++);
        fHeader.setFlags(RFP_COMPRESSED | flags);
        fHeader.setSize( deflater.getBytesWritten() );
        fHeader.setDataOffset(header.getFileListOffset());
        fHeader.setCrc32((int)deflater.getAdler());
        fHeader.setDirectoryId(currDirectory);
        fHeader.setName(file);
        fileList.add(fHeader);

        header.setFileListOffset(rfpFile.getFilePointer());
        reconstructHeaders();   
    }
    
    /**
     * Adiciona um ficheiro ao ficheiro rfp
     * @param file
     * @param flags
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @Override
    public void addFile(String file, String path, int flags) 
            throws FileNotFoundException, IOException, RFPException
    {
        //verificacao de duplicados
        if(this.filenameExists(currDirectory, file))
        {
            throw new RFPException(RFPException.EXCEPTION_FILE_ALREADY_EXISTS);
        }
        
        RandomAccessFile iFile =  new RandomAccessFile(path + "\\" + file, "r");
        int blocksize = calculateBlockSize(iFile.length());
        byte[] buffer = new byte[blocksize];
        long offset = 0;
        long bytesTransfered = 0;
        //calculo de integridade de dados
        CRC32 fileIntegrity = new CRC32();
        fileIntegrity.initialize();
        
        //determinar onde acaba o ultimo ficheiro que foi adicionado
        //neste caso coicide com o offset da lista de ficheiros
        rfpFile.seek(header.getFileListOffset());
        
        TransferRater calculatedTransfer = new TransferRater(iFile.length());
        //transferir os dados
        while (offset != iFile.length())
        {
            if(iFile.length() - offset > blocksize)
            {
                offset += iFile.read(buffer);
                rfpFile.write(buffer);
                bytesTransfered += blocksize;
                fileIntegrity.update(buffer);
            }
            else
            {
                //remaining bytes
                buffer = new byte[(int)iFile.length() - (int)offset];
                offset += iFile.read(buffer);
                rfpFile.write(buffer);
                bytesTransfered += buffer.length;
                fileIntegrity.update(buffer);
            }
            
            if(calculatedTransfer.calculate(bytesTransfered))
            {
               bytesTransfered = 0;
            }
        }
       
        calculatedTransfer.calculateAtEnd(bytesTransfered);
        
        RFPFileHeader fHeader = new RFPFileHeader();
        fHeader.setFilenameId(genFileId++);
        fHeader.setFlags(flags);
        fHeader.setSize( iFile.length() );
        fHeader.setDataOffset(header.getFileListOffset());
        fHeader.setCrc32((int)fileIntegrity.getDigest());
        fHeader.setDirectoryId(currDirectory);
        fHeader.setName(file);
        fileList.add(fHeader);
        
        iFile.close();

        header.setFileListOffset(rfpFile.getFilePointer());
        reconstructHeaders();
    }
    
    /**
     * Calcula a quantidade de bytes "ideal" que deve ser transferido em cada
     * leitura feita para tentar maximizar a velocidade de transferencia.
     * @param size
     * @return 
     */
    public int calculateBlockSize(long size)
    {
        int blocksize = FILE_BLOCK_SIZE;
        while (blocksize != 1)
        {
            if(size / blocksize > 0) return blocksize;
            blocksize = blocksize / 2;
        }
        return 1;
    }
    
    /**
     * Apaga o ficheiro actual.
     * Se existir algum ficheiro RFP aberto, este será fechado.
     */
    @Override
    public void deleteRFPFile() throws IOException
    {
        closeRFP();
        java.io.File deleteFile = new java.io.File(nativePath + "\\" + 
                filename);
        deleteFile.delete();
    }
    
    /** Apaga o ficheiro temporario caso exista.
     * 
     */
    public void deleteTempFile(String path)
    {
         java.io.File deleteFile = new java.io.File(path + "\\" + 
                 "rfptemp.tmp");
         deleteFile.delete();
    }
    
    /**
     * Tenta mostrar o conteudo de um ficheiro escolhido.
     * @param fileId
     * @throws IOException
     * @throws FileNotFoundException
     * @throws RFPException
     * @throws DataFormatException 
     */
    @Override
    public void showFileContent(int fileId) throws IOException, 
            FileNotFoundException, RFPException, DataFormatException
    {
        
        //obtem o nome do ficheiro relativamente ao ficheiro pedido
        for( RFPFileHeader fn : fileList)
        {
            if(fn.getFilenameId() == fileId)
            {
                
                if( (int)(fn.getFlags() & RFPBinaryFile.RFP_COMPRESSED) == 
                        RFPBinaryFile.RFP_COMPRESSED)
                {
                    decompressFile(fn.getFilenameId(), nativePath);
                }
                else
                {
                    extractFile(fn.getFilenameId(), nativePath);
                }

                java.io.File runFile = new java.io.File(nativePath + "\\" +
                        fn.getName());
                java.lang.Runtime.getRuntime().exec("notepad.exe " + nativePath
                        + "\\" + fn.getName());
            }
        } 
    }

    /**
     * Extrai todo o conteudo de uma directoria para uma determinada path.
     * @param dirId
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException 
     */
    @Override
    public void extractAll(int dirId, String path) throws FileNotFoundException,
            IOException, RFPException
    {
        //criar a directoria primeiro
        boolean directoryCreated = new java.io.File(path).mkdir();
        
        System.out.printf("A extrair directoria para: %s\n", path);
        //extrair ficheiros desta directoria primeiro.
        for(RFPFileHeader file : fileList)
        {
            if(file.getDirectoryId() == dirId)
            {
                System.out.printf("A extrair para: %s\n", path + "\\" + 
                        file.getName());
                extractFile(file.getFilenameId(), path); //incluir path
            }
            
        }
        //extrair os ficheiros das sub directorias.
        for( RFPDirectoryHeader dir : directoryList)
        {
            if(dir.getParentId() == dirId)
            {
                extractAll(dir.getId(),  path + "\\" + dir.getName());
            }
        }
    }
    
    /**
     * Descomprime um ficheiro na path de destino.
     * @param fileId
     * @param path 
     */
    @Override
    public void decompressFile(int fileId, String path) 
            throws FileNotFoundException, IOException, DataFormatException,
            RFPException
    {
        java.io.RandomAccessFile oFile = null;
        try
        {
            
            deleteTempFile(path);
            //cria um novo ficheiro temporario
            oFile =  new java.io.RandomAccessFile(path + 
                    "\\rfptemp.tmp", "rw");

            Inflater inflater = new Inflater();

            long offset = 0;
            long bytesTransfered = 0;
            int bytesDecompressed = 0;
            //saltar para o offset que contem os dados.
            rfpFile.seek(header.getFileListOffset());

            for (RFPFileHeader f : fileList)
            {
                if(f.getFilenameId() == fileId)
                {
                    System.out.printf("A descomprimir %s\n", f.getName());
                    int blockSize = calculateBlockSize(f.getSize());
                    byte[] buffer = new byte[blockSize];
                    byte[] flaterBuffer = new byte[blockSize];
                    rfpFile.seek(f.getDataOffset());

                    while( bytesTransfered < f.getSize())
                    {
                        if(f.getSize() - bytesTransfered >= blockSize)
                        {
                            offset += rfpFile.read(buffer);
                            inflater.setInput(buffer);
                            bytesTransfered += blockSize;
                        }
                        else
                        {
                            //remaining bytes
                            buffer = new byte[(int)f.getSize() - 
                                    (int)bytesTransfered];
                            offset += rfpFile.read(buffer);
                            inflater.setInput(buffer);
                            bytesTransfered += buffer.length;
                        }

                        while (!inflater.needsInput()) 
                        {
                             bytesDecompressed = inflater.inflate(flaterBuffer);
                             if(bytesDecompressed > 0)
                             {
                                oFile.write(flaterBuffer, 0, bytesDecompressed);
                             }
                        }
                    }
                }
            }


            inflater.end();
            oFile.close();

            //obtem o nome do ficheiro relativamente ao ficheiro pedido
            for( RFPFileHeader fn : fileList)
            {
                if(fn.getFilenameId() == fileId)
                {
                    //apagar o ficheiro com o mesmo nome a ser renomeado
                    //caso exista
                    java.io.File deleteFile = new File(path + "\\"
                            + fn.getName());
                    deleteFile.delete();
                    //renomear o ficheiro temporario para o ficheiro de 
                    //destino
                    java.io.File renameFile = new File(path + 
                            "\\rfptemp.tmp");
                    renameFile.renameTo( new File( path + "\\" + 
                            fn.getName() ));
                }
            }
        }
        catch(Exception e)
        {
            oFile.close();
            deleteTempFile(path);
            throw new 
                    RFPException(RFPException.EXCEPTION_ERROR_WHILE_EXTRACTING);
        }
    }
    
    /**
     * Extrai um ficheiro escolhido do ficheiro rfp.
     * @param fileId
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @Override
    public void extractFile(int fileId, String path) 
            throws FileNotFoundException, IOException, RFPException
    {
        java.io.RandomAccessFile oFile = null;   
        try
        {
            

            //verificacao de integridade ao extrair
            //o ficheiro só é valido se a integridade for valida, mesmo que 
            //transfira o ficheiro todo
            CRC32 fileIntegrity = new CRC32();
            fileIntegrity.initialize();

            deleteTempFile(path);
            //cria um novo ficheiro temporario
            oFile =  new java.io.RandomAccessFile(path + 
                    "\\rfptemp.tmp", "rw");

            for (RFPFileHeader f : fileList)
            {
                if(f.getFilenameId() == fileId)
                {
                    System.out.printf("A extrair: %s\n", f.getName());
                    //quantidade de bytes que ja foram lidos/escritos
                    long actualSize = 0;
                    //salta para o endereço que contem os dados do ficheiro
                    rfpFile.seek(f.getDataOffset());

                    //calcula o tamanho do buffer baseado no tamanho dos dados
                    int blockSize = calculateBlockSize(f.getSize());
                    byte[] buffer = new byte[blockSize];

                    //escrever os dados no ficheiro temporario
                    while(actualSize < f.getSize())
                    {
                        //se o tamanho restante for equivalente ao bloco
                        //ler o bloco de uma so vez
                        if(f.getSize() - actualSize >= blockSize)
                        {
                            rfpFile.read(buffer);
                            oFile.write(buffer);
                            actualSize = actualSize + blockSize;
                            fileIntegrity.update(buffer);
                        }
                        else
                        {
                            //le os bytes restantes, ajustando o tamanho
                            //do buffer
                            buffer = new byte[(int)f.getSize() - 
                                    (int)actualSize];
                            rfpFile.read(buffer);
                            oFile.write(buffer);
                            actualSize = actualSize + buffer.length;
                            fileIntegrity.update(buffer);
                        }
                    }
                    //fecha o ficheiro
                    oFile.close();

                    //crc check
                    if((int)fileIntegrity.getDigest() != f.getCrc32())
                    {
                        //ficheiro corrupto
                        deleteTempFile(path);
                        throw new 
                           RFPException(RFPException.EXCEPTION_FILE_CORRUPTED);
                    }

                    //obtem o nome do ficheiro relativamente ao ficheiro pedido
                    for( RFPFileHeader fn : fileList)
                    {
                        if(fn.getFilenameId() == fileId)
                        {
                            //apagar o ficheiro com o mesmo nome a ser renomeado
                            //caso exista
                            java.io.File deleteFile = new File(path + "\\"
                                    + fn.getName());
                            deleteFile.delete();
                            //renomear o ficheiro temporario para o ficheiro de 
                            //destino
                            java.io.File renameFile = new File(path + 
                                    "\\rfptemp.tmp");
                            renameFile.renameTo( new File( path + "\\" + 
                                    fn.getName() ));
                        }
                    }
                    break;
                }
            }
        }
        catch(Exception e)
        {
             oFile.close();
             deleteTempFile(path);
              throw new 
                    RFPException(RFPException.EXCEPTION_ERROR_WHILE_EXTRACTING);
        }
    }
    
    /**
     * Remove os dados de um ficheiro da lista de ficheiros.
     * @param fileId 
     */
    public void removeFromFileList(int fileId)
    {
        for (RFPFileHeader f : fileList)
        {
            if(f.getFilenameId() == fileId)
            {
                fileList.remove(f);
                break;
            }
        }
    }
    
    /**
     * Limpa as listas que contem os ficheiros e directorias.
     */
    public void clearLists()
    {
        fileList.clear();
        directoryList.clear();
    }
    
    /**
     * Remove o ficheiro selecionado do ficheiro rfp.
     * @param fileId
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException 
     */
    @Override
    public void removeFile(int fileId) throws FileNotFoundException, 
            IOException, RFPException
    {
        //remover os dados das listas
        removeFromFileList(fileId);
        reconstructRFPFile();
        //reabrir o ficheiro rfp
        openRFP();
    }
    
    /**
     * Cria uma directoria na directoria de destino.
     * @param parent
     * @param name
     * @throws IOException 
     */
    @Override
    public void makeDirectory(int parent, String name) 
            throws IOException, RFPException
    {
        //verificacao de duplicados.
        System.out.printf("A criar directoria: %s\n", name);
        if(this.directoryNameExists(parent, name))
        {
            throw new RFPException(
                    RFPException.EXCEPTION_DIRECTORY_ALREADY_EXISTS);
        }
        
        
        RFPDirectoryHeader dirName = new RFPDirectoryHeader();
        dirName.setName(name);
        dirName.setId( genDirectoryId++ );
        dirName.setParentId(parent);

        directoryList.add(dirName);
        
        //guardar os dados
        reconstructHeaders();
    }
    
    /**
     * Obtem todas as sub directorias a partir de uma directoria especifica para
     * facilitar a remoção de uma directoria ala hardcore style.
     * A recursividade aqui iria complicar porque iria invalidar a linkedlist
     * quando removesse uma sub-directoria.
     * @param dirId
     * @return 
     */
    public java.util.LinkedList<Integer> calculateParentRelation(int dirId)
    {
        LinkedList<Integer> result = new LinkedList<Integer>();
        for( RFPDirectoryHeader dl : directoryList)
        {
            if(dl.getParentId() == dirId && !result.contains(dl.getParentId()))
            {
                result.add(dl.getId());
                result.addAll(calculateParentRelation(dl.getId()));
            }
        }
        return result;
    }
    
    /**
     * Remove a directoria e todos os ficheiros e directorias associados a esta.
     * @param dirId
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException 
     */
    @Override
    public void removeDirectory(int dirId) 
            throws FileNotFoundException, IOException, RFPException
    {
        //calcular todas as relacoes
        LinkedList<Integer> relation = this.calculateParentRelation(dirId);
        
        //remover todas as directorias baseado nas relacoes
        for( Iterator<RFPDirectoryHeader> it1 = directoryList.iterator(); 
                it1.hasNext(); )
        {
            RFPDirectoryHeader dl = it1.next();
            
            if(relation.contains(dl.getId()) || dl.getId() == dirId )
            {
                System.out.printf("A remover: %s\n", dl.getName());
                for( Iterator<RFPFileHeader> it2 = fileList.iterator(); 
                        it2.hasNext(); )
                {
                    RFPFileHeader fl = it2.next();
                    
                    if(fl.getDirectoryId() == dl.getId())
                    {
                        System.out.printf("A remover: %s\n", fl.getName());
                        it2.remove();
                    }
                }
                it1.remove(); 
            }
        }  
        //reconstruir o ficheiro sem os ficheiros removidos e as directorias
        //removidas.
        reconstructRFPFile();   
    }
    
    /**
     * Reconstroi os headers do ficheiro rfp aberto.
     * Usado quando é adicionado dados novos.
     * Os cabeçalhos sao reconstruidos a partir da ultima posicao
     * @throws IOException 
     */
    public void reconstructHeaders() throws IOException
    {   
        System.out.printf("A reconstruir cabeçalhos\n");
        rfpFile.seek( header.getFileListOffset());
        
        rfpFile.writeInt(fileList.size());
        for( RFPFileHeader h : fileList)
        {
            h.writeHeader(rfpFile);
        }
        
        header.setDirectoryListOffset(rfpFile.getFilePointer());
        rfpFile.writeInt(directoryList.size());
        for(RFPDirectoryHeader d : directoryList)
        {
            d.writeHeader(rfpFile);
        }

        //finalizacao, escrever para o header do rfp file
        rfpFile.seek(0);
        header.writeHeader(rfpFile);  
    }
    
    
    /**
     * Reconstroi o ficheiro RFP depois de ser modificado.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void reconstructRFPFile() 
            throws FileNotFoundException, IOException, RFPException
    {
        System.out.printf("A reconstruir ficheiro RFP\n");
        //apagar o ficheiro temporario
        deleteTempFile(nativePath);
        //criar rfp temporario para mover o conteudo do ficheiro
        RandomAccessFile oFile =  new RandomAccessFile(nativePath + "\\" + 
                "rfptemp.tmp", "rw");
        
        //escrever o cabeçalho
        header.writeHeader(oFile);

        for (RFPFileHeader f : fileList)
        {
            //quantidade de bytes que ja foram lidos/escritos
            long actualSize = 0;
            //adicionar o ficheiro
            //pula para a posicao que contem os dados.
            rfpFile.seek(f.getDataOffset());
            int blockSize = calculateBlockSize(f.getSize());
            byte[] buffer = new byte[blockSize];

            //actualiza o data offset com o novo offset onde serao
            //gravados os ficheiros
            f.setDataOffset(oFile.getFilePointer());

            //escrever os dados no ficheiro temporario
            while(actualSize < f.getSize())
            {
                //se o tamanho restante for equivalente ao bloco
                //ler o bloco de uma so vez
                if(f.getSize() - actualSize >= blockSize)
                {
                    rfpFile.read(buffer);
                    oFile.write(buffer);
                    actualSize = actualSize + blockSize;
                }
                else
                {
                    //le os bytes restantes, ajustando o tamanho
                    //do buffer
                    buffer = new byte[(int)f.getSize() - (int)actualSize];
                    rfpFile.read(buffer);
                    oFile.write(buffer);
                    actualSize = actualSize + buffer.length;
                }
            }
            
        }
        //fim de transferencia, gravar os cabeçalhos
        //actualizar a quantidade de ficheiros
        header.setFileListOffset( oFile.getFilePointer() );
        oFile.writeInt(fileList.size());
        //actualizar a lista de ficheiros no rfp file
        for( RFPFileHeader h : fileList)
        {
            h.writeHeader(oFile);
        }
        
        header.setDirectoryListOffset(oFile.getFilePointer());
        oFile.writeInt(directoryList.size());
        for(RFPDirectoryHeader d : directoryList)
        {
            d.writeHeader(oFile);
        }
 
        //finalizacao, escrever para o header do rfp file
        oFile.seek(0);
        header.writeHeader(oFile);
        
        //fechar o ficheiro actual
        rfpFile.close();
        //fecha o ficheiro temporario
        oFile.close();
        System.gc();
                
        //renomear o ficheiro temporario
        File deleteFile = new File(nativePath + "\\" + filename);
        deleteFile.delete();
        //renomear o ficheiro temporario para o ficheiro de destino
        File renameFile = new File(nativePath + "\\" + "rfptemp.tmp");
        renameFile.renameTo( new File( nativePath + "\\" + filename ));
        openRFP();
    }
    
    /**
     * Testa a integridade do conteudo do ficheiro RFP.
     * @return
     * @throws IOException
     * @throws RFPException 
     */
    @Override
    public boolean testIntegrity() throws IOException, RFPException
    {
        CRC32 fileIntegrity = new CRC32();
        
        System.out.printf("A testar integridade\n");
        for (RFPFileHeader f : fileList)
        {
            System.out.printf("A verificar: %s ", f.getName());
            fileIntegrity.initialize();
            //integridade dos ficheiros comprimidos n pode ser verificada
            //devido a maneira que o CRC32 é calculado
            //em principio o proprio deflater verifica a integridade usando
            // Adler.
            if( (f.getFlags() & RFPBinaryFile.RFP_COMPRESSED) == 4)
            {
                continue;
            }
            //quantidade de bytes que ja foram lidos/escritos
            long actualSize = 0;
            //salta para o endereço que contem os dados do ficheiro
            rfpFile.seek(f.getDataOffset());

            //calcula o tamanho do buffer baseado no tamanho dos dados
            int blockSize = calculateBlockSize(f.getSize());
            byte[] buffer = new byte[blockSize];
            
            while(actualSize < f.getSize())
            {
                //se o tamanho restante for equivalente ao bloco
                //ler o bloco de uma so vez
                if(f.getSize() - actualSize >= blockSize)
                {
                    rfpFile.read(buffer);
                    actualSize = actualSize + blockSize;
                    fileIntegrity.update(buffer);
                }
                else
                {
                    //le os bytes restantes, ajustando o tamanho
                    //do buffer
                    buffer = new byte[(int)f.getSize() - (int)actualSize];
                    rfpFile.read(buffer);
                    actualSize = actualSize + buffer.length;
                    fileIntegrity.update(buffer);
                }
            }
            //crc check
            if((int)fileIntegrity.getDigest() != f.getCrc32())
            {
                //ficheiro corrupto
                throw new RFPException(RFPException.EXCEPTION_FILE_CORRUPTED);
            }
            System.out.printf("OK\n");
        }
        
        return true;
    }    
}

    