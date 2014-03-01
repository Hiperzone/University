
package cpu_scheduler2;

import java.util.LinkedList;

/**
 * Gestor de memoria.
 * Contem metodos para obter, criar e libertar memoria de um process.
 * Class singleton, apenas uma instancia da classe esta disponivel, usar
 * getInstance()
 */
public final class MemFactory
{
    //constants
    public static final int MEM_ALGO_BEST_FIT  = 0; //procura o espaco mais a medida( o bloco mais pequeno de todos em que ele cabe).
    public static final int MEM_ALGO_WORST_FIT = 1; //procura o espaco maior disponivel.
    public static int MEM_POOL_SIZE = 300;
    public static int MEM_VARIABLE_SIZE = 10;
    public static int MEM[];
    
    //singleton instance access
    private static MemFactory instance = new MemFactory();
    
    //public static variables.
    
   
    
    private LinkedList<MemBlock> memBlock;
    private int memAlgo;
    
    /**
     * Constructor privado que impede a criacao de multiplas copias desta
     * classe.
     */
    private MemFactory()
    {
        MEM = new int[MEM_POOL_SIZE];
        memAlgo = MEM_ALGO_BEST_FIT;
        memBlock = new LinkedList<MemBlock>();
    }
    
    /**
     * Retorna a instancia da classe.
     * @return
     */
    public static MemFactory getInstance()
    {
        return instance;
    }
    
    /**
     * Permite alocar memoria.
     * Lanca uma excepcao se nao houver espaco disponivel.
     * @param size
     * @return
     * @throws Exception
     */
    public int memAlloc(int size) throws ProcessException{
    	MemBlock newBlock = null;
    	MemBlock block = null;
    	
    	switch (memAlgo) 
        {
            case MEM_ALGO_BEST_FIT:
                block = getBestFit(size);
                break;
            case MEM_ALGO_WORST_FIT:
                block = getWorstFit(size);
                break;
            default:
                break;
        }
    	
    	if(block == null)
            throw new ProcessException(
               ProcessException.PROCESS_EXCEPTION_RUNTIME_ERROR_OUT_OF_MEMORY);
    	
    	// Parte o bloco em dois e retorna o novo bloco
    	if(size < block.memPoolSize)
    		newBlock = block.split(size);
    	
    	newBlock.allocated = true;
    	memBlock.add(memBlock.indexOf(block), newBlock);
    	return newBlock.startAddress;
    }
    
    /**
     * Retorna um menor bloco livre que tenha
     * tamanho maior ou igual ao size
     *
     * @param size
     * @return
     * @throws ProcessException
     */
    private MemBlock getBestFit(int size)
    {
    	int diff = 0;
    	int lastBestFit = MEM.length;
    	
    	MemBlock newBlock = null;
    	
    	for( MemBlock block : memBlock)
        {
            if( fits(block, size) )
            {	
    		diff = block.memPoolSize - size;
    			
                if(diff < lastBestFit )
                {
                    lastBestFit = diff;
                    newBlock = block;
                }
            }
    	}
    	return newBlock;
    }
    
    /**
     * Retorna o maior bloco livre que tenha
     * tamanho maior ou igual ao size
     * @param size
     * @return
     * @throws ProcessException
     */
    private MemBlock getWorstFit(int size)
    {
    	int diff = 0;
    	int lastWorstFit = 0;
    	
    	MemBlock newBlock = null;
    	
    	for( MemBlock block : memBlock)
        {
            if( fits(block, size) )
            {
                diff = block.memPoolSize - size;
                
    		if( diff > lastWorstFit )
                {
                    lastWorstFit = diff;
                    newBlock = block;
                }
            }
    	}
    	return newBlock;
    }
    
    /**
     * Permite saber se um determinado tamanho de memoria cabe num bloco de
     * memoria livre.
     * @param block
     * @param size
     * @return 
     */
    private boolean fits(MemBlock block,int size)
    {
    	return block.memPoolSize >= size && !block.allocated;
    }
        
    /**
     * Permite libertar memoria que foi alocada por um processo.
     * Funde blocos de memoria caso estes estejam ligados e libertados, criando
     * um bloco de memoria livre maior.
     * @param size
     * @param pcb
     */
    public void memfree(int size, PCB pcb)
    {
        MemBlock block = getMemBlockByStartAddress(pcb.getBaseVarAddress());
        //assert(block != null);
        
        for(int i = 0; i < size; i++)
        {
            MEM[ pcb.getBaseVarAddress() + i] = 0;
        }
        //libertar a memoria
        block.allocated = false;
        //fundir blocos livres
        //obter o bloco anterior
        MemBlock startblock = getMemBlockByEndAddress(pcb.getBaseVarAddress() - 1);
        //obter o seguinte.
        MemBlock endblock = getMemBlockByStartAddress(pcb.getBaseVarAddress() + pcb.getAllocatedMemSize());
        
        if(startblock  != null && !startblock.allocated)
        {
            block.startAddress = startblock.startAddress;
            block.memPoolSize = block.memPoolSize + startblock.memPoolSize;
            memBlock.remove(startblock);
        }
        if(endblock != null && !endblock.allocated)
        {
            block.endAddress = endblock.endAddress;
            block.memPoolSize = block.memPoolSize + endblock.memPoolSize;
            memBlock.remove(endblock);
        }
    }
    
    /**
     * Permite copiar o conteudo de uma array para um endereco especifico na
     * memoria.
     * @param address
     * @param data
     */
    public void memCopy(int address, int data[])
    {
        System.arraycopy(data, 0, MEM, address, data.length);
    }
    
    /**
     * Copia o conteudo a partir de uma posicao de memoria para outra posicao
     * de memoria.
     * @param originAdress
     * @param destinyAddress
     * @param amountToCopy
     */
    public void copyToAddress(int originAdress, int destinyAddress, int amountToCopy)
    {
    	for(int i = 0, j = destinyAddress; i < amountToCopy; i++, j++){
    		MEM[j] = MEM[originAdress + i];
    	}
    }
    
    /**
     * Obtem o bloco de memoria que coincide com o endereco inicial.
     * @param address
     * @return
     */
    private MemBlock getMemBlockByStartAddress(int address)
    {
        for( MemBlock block : memBlock)
        {
            if(block.startAddress == address)
            {
                return block;
            }
        }
        return null;
    }
    
    /**
     * Obtem o bloco de memoria que coincide com o endereco final.
     * @param address
     * @return
     */
    private MemBlock getMemBlockByEndAddress(int address)
    {
        for( MemBlock block : memBlock)
        {
            if(block.endAddress == address)
            {
                return block;
            }
        }
        return null;
    }
    
    /**
     * Retorna a lista de blocos de memoria livres e alocados.
     * @return
     */
    public LinkedList<MemBlock> getMemBlock()
    {
        return memBlock;
    }
    
    /**
     * Atribui uma nova lista de blocos de memoria.
     * @param memBlock
     */
    public void setMemBlock(LinkedList<MemBlock> memBlock)
    {
        this.memBlock = memBlock;
    }
    
    /**
     * Retorna o algoritmo de gestao de memoria a ser aplicado na alocacao de
     * memoria.
     * @return
     */
    public int getMemAlgo()
    {
        return memAlgo;
    }
    
    /**
     * Define qual o algoritmo de gestao de memoria que ira ser usado para a
     * alocacao de memoria.
     * @param memAlgo
     */
    public void setMemAlgo(int memAlgo)
    {
        this.memAlgo = memAlgo;
    }
    
    
    @Override
    public String toString()
    {
        String output = "";
        //array de memoria
        
        if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_SPECIAL_DEBUG)
        {
        	output += "MEMORIA [";
            for(int i = 0; i < MEM.length; i++)
            {
                output += MEM[i] + " ";
            }
            output += "]\n";
            
            //array dos blocos
            output += "BLOCOS: {";
            for( MemBlock block : memBlock)
            {
                output += block.toString();
            }
            output += "}\n";
        }
        else if(Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_NORMAL ||
                 Cpu_scheduler2.printMode == Cpu_scheduler2.PRINT_MODE_DEBUG)
        {
            output += "MEMORIA [";
            for(int i = 0; i < MEM.length; i++)
            {
                output += MEM[i] + " ";
            }
            output += "]\n";
        }
        
        return output;
    }
}
