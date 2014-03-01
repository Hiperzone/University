
package cpu_scheduler2;

/**
 * Contem a estructura de um bloco de memoria.
 *
 */
public class MemBlock
{
    public int startAddress;
    public int endAddress;
    public int memPoolSize;
    public boolean allocated;
    
    public MemBlock()
    {
        startAddress = 0;
        endAddress = 0;
        memPoolSize = 0;
        allocated = false;
    }
    
    /**
     * Parte o bloco em dois.
     * @param size
     * @return novo bloco
     * @throws ProcessException
     */
    public MemBlock split(int size) throws ProcessException{
    	
    	boolean sameSize = memPoolSize == size;
    	
    	if(!allocated && !sameSize)
        {
            MemBlock newBlock = new MemBlock();
            newBlock.memPoolSize = size;
            newBlock.startAddress = this.startAddress;
            newBlock.endAddress = newBlock.startAddress + size - 1;
            this.startAddress = newBlock.endAddress + 1;
            this.memPoolSize = memPoolSize - size;

            return newBlock;
    	}
        else
            throw new ProcessException(
                    ProcessException.PROCESS_EXCEPTION_RUNTIME_ERROR_OUT_OF_MEMORY);
    }
    
    @Override
    public String toString()
    {
        String output = "";
        output += "[Alocado: " + allocated + " Enderecamento: " + startAddress
        + " to " + endAddress + " Tamanho: " + memPoolSize + "]";
        return output;
    }
}
