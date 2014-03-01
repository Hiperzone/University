/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RFP.IO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.DataFormatException;

/**
 * Interface para a implementação do ficheiro binario RFP.
 */
public interface RFPBinaryFileI 
{
    public static final int RFP_NO_FLAG = 0x00;
    public static final int RFP_PROTECTED = 0x01;
    public static final int RFP_CRYPT_XORC = 0x02;
    public static final int RFP_COMPRESSED = 0x04;
    public static final int RFP_CRYPT_SHA256 = 0x08;
    
    public static final int RFP_VERSION = 100;
    
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
    void addCompressedFile(String file, String path, int flags) 
            throws FileNotFoundException, IOException, RFPException;

    /**
     * Adiciona um ficheiro ao ficheiro rfp
     * @param file
     * @param flags
     * @throws FileNotFoundException
     * @throws IOException
     */
    void addFile(String file, String path, int flags) 
            throws FileNotFoundException, IOException, RFPException;

    /**
     * Fecha um ficheiro RFP caso exista um aberto.
     * @throws IOException
     */
    void closeRFP() throws IOException;

    /**
     * Conta quantos ficheiros e directorias existem numa determinada
     * directoria.
     * @param parent
     * @return
     */
    int countDirFilesAtParent(int parent);

    /**
     * Descomprime um ficheiro na path de destino.
     * @param fileId
     * @param path
     */
    void decompressFile(int fileId, String path) 
            throws FileNotFoundException, IOException, DataFormatException,
            RFPException;

    /**
     * Apaga o ficheiro actual.
     * Se existir algum ficheiro RFP aberto, este será fechado.
     */
    void deleteRFPFile() throws IOException;

    /**
     * Verifica se existe uma directoria com o mesmo nome dentro de uma
     * directoria especifica.
     * @param dirId
     * @param name
     * @return
     */
    boolean directoryNameExists(int dirId, String name);

    /**
     * Extrai todo o conteudo de uma directoria para uma determinada path.
     * @param dirId
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException
     */
    void extractAll(int dirId, String path)
            throws FileNotFoundException, IOException, RFPException;

    /**
     * Extrai um ficheiro escolhido do ficheiro rfp.
     * @param fileId
     * @throws FileNotFoundException
     * @throws IOException
     */
    void extractFile(int fileId, String path) 
            throws FileNotFoundException, IOException, RFPException;

    /**
     * Verifica se existe um ficheiro com o mesmo nome dentro de uma directoria
     * especifica.
     * @param dirId
     * @param name
     * @return
     */
    boolean filenameExists(int dirId, String name);

    /**
     * Gera um ficheiro rfp vazio.
     * @throws IOException
     */
    void generateEmptyFile() throws IOException;

    /**
     * Obtem a directoria actual.
     * @return
     */
    int getCurrDirectory();

    /**
     * retorna uma directoria dado o ID.
     * @param Id
     * @return
     */
    RFPDirectoryHeader getDirectoryId(int Id);

    /**
     * Obtem a lista de directorias contidas neste ficheiro RFP.
     * @return
     */
    LinkedList<RFPDirectoryHeader> getDirectoryList();

    /**
     * Obtem a lista de ficheiros contidos neste ficheiro RFP.
     * @return
     */
    LinkedList<RFPFileHeader> getFileList();

    /**
     * Verifica se o ficheiro RFP actual está fechado.
     * @return
     */
    boolean isClosed();

    /**
     * Cria uma directoria na directoria de destino.
     * @param parent
     * @param name
     * @throws IOException
     */
    void makeDirectory(int parent, String name) 
            throws IOException, RFPException;

    /**
     * Abre o ficheiro designado no constructor.
     * Se houver algum ficheiro RFP aberto, este será fechado para evitar
     * conflito de recursos a serem usados.
     * @throws FileNotFoundException
     */
    void openRFP() throws FileNotFoundException, IOException;

    /**
     * Le o conteudo de um ficheiro rfp.
     * Apenas é carregado os cabeçalhos dos ficheiros e directorias.
     *
     * Lança uma excepção caso a versão não seja valida.
     * @throws IOException
     * @throws RFPException
     */
    void readContents() throws IOException, RFPException;

    /**
     * Remove a directoria e todos os ficheiros e directorias associados a esta.
     * @param dirId
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException
     */
    void removeDirectory(int dirId) 
            throws FileNotFoundException, IOException, RFPException;

    /**
     * Remove o ficheiro selecionado do ficheiro rfp.
     * @param fileId
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RFPException
     */
    void removeFile(int fileId) 
            throws FileNotFoundException, IOException, RFPException;

    /**
     * Atribui a directoria actual.
     * @param currDirectory
     */
    void setCurrDirectory(int currDirectory);

    /**
     * Tenta mostrar o conteudo de um ficheiro escolhido.
     * @param fileId
     * @throws IOException
     */
    void showFileContent(int fileId) 
            throws IOException, FileNotFoundException, RFPException, 
            DataFormatException;

    /**
     * Testa a integridade do conteudo do ficheiro RFP.
     * @return
     * @throws IOException
     * @throws RFPException
     */
    boolean testIntegrity() throws IOException, RFPException;
    
}
