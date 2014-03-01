from math import log2
import numpy as np
from HuffmanTree import *
import struct as struct
from sys import argv
from sys import setrecursionlimit
setrecursionlimit(4000)

tamanhoFicheiro = 0
tamanhoBitsPorLer = 0
tamanhoFicheiroPorLer = 0
currentBuffer = None
paddingSize = 0
nomeFicheiro = ''
##################################################################FILE MANIPULATOR##############################################################
def abrirFicheiro(nomeFicheiro : str):
    try:
        handler = open(nomeFicheiro, 'rb')
        return handler
    except:
        print("Erro ao abrir ficheiro pbm")


#Le o conteudo do ficheiro na sua totalidade
def lerConteudo(handler):
    global tamanhoFicheiro
    handler.seek(0,2)
    tamanhoFicheiro = handler.tell()
    handler.seek(0)
    return handler.read(tamanhoFicheiro)


#permite ler uma sequencia de bits a partir do buffer do ficheiro que foi lido
def read(dataBuffer, size, padding = False):
    global tamanhoFicheiro, tamanhoBitsPorLer, tamanhoFicheiroPorLer, currentBuffer, paddingSize
    bit = 0B0

    bitsDoGrupoEmFalta = size
    remainingByteSize = tamanhoBitsPorLer
    fileSize = tamanhoFicheiro
    i =  tamanhoFicheiroPorLer
    buffer = currentBuffer

    while(bitsDoGrupoEmFalta > 0 and i < fileSize):
        if(remainingByteSize == 0):
            buffer = dataBuffer[i]
            remainingByteSize = 8
            i += 1
        else:
            bit = bit << 1 | (buffer >> (remainingByteSize - 1) & 1)
            remainingByteSize -= 1
            bitsDoGrupoEmFalta -= 1

    #ainda existe dados no byte por ler
    if(bitsDoGrupoEmFalta > 0 and remainingByteSize > 0):
        while(bitsDoGrupoEmFalta > 0 and remainingByteSize > 0):
            bit = bit << 1 | (buffer >> (remainingByteSize - 1) & 1)
            remainingByteSize -= 1
            bitsDoGrupoEmFalta -= 1

    tamanhoFicheiroPorLer = i
    tamanhoBitsPorLer = remainingByteSize
    currentBuffer = buffer

    #padding
    if(i >= fileSize and bitsDoGrupoEmFalta > 0):
        paddingSize = bitsDoGrupoEmFalta
        while(bitsDoGrupoEmFalta > 0):
              bit = bit << 1 | 0
              bitsDoGrupoEmFalta -= 1

    return bit

#fecha o ficheiro
def fecharFicheiro(handler):
        if(handler):
            handler.close()

#descomprimi o ficheiro tendo em conta o padding no fim do ficheiro
def descomprimir(handler, dataBuffer):
    global tamanhoFicheiroPorLer, paddingSize
    print("Tamanho do ficheiro", tamanhoFicheiro*8, "bits")
    handler.seek(0)

    tamanhoRestantePorLer = tamanhoFicheiro
    tamanhoFicheiroComprimido, = struct.unpack('i',  handler.read(4))    
    print("Tamanho do ficheiro comprimido:", tamanhoFicheiroComprimido, "bits")           
    bitGroupSize, = struct.unpack('B',  handler.read(1))
    print("Tamanho do grupo:", bitGroupSize, "bit(s)")  
    paddingSize, = struct.unpack('B',  handler.read(1))
    huffmanEncodedSize, = struct.unpack('i',  handler.read(4))

    tamanhoActualDescomprimido = 0
    #avancar o indice dos dados ja lidos para que seja possivel ler os bits na posicao correcta devido
    #a ter sido usado o unpack
    tamanhoFicheiroPorLer += 4 + 1 + 4 + 1

    #ler os dados da arvore
    huffmanTree = HuffmanNode()

    #lista com os bits codificados da arvore
    huffmanEncodedList = list()

    i = 0
    while(i < huffmanEncodedSize):
        huffmanEncodedList.append( read(dataBuffer, 1) )
        i += 1

    print("Arvore de huffman Codificada:", huffmanEncodedList)

    #calcular quantos bits sao lixo devido a codificacao da arvore
    huffmanLixo = calcularBitsARemoverNoFim(huffmanEncodedSize)
    print("Ignorando", huffmanLixo, "bits que sao lixo da arvore de huffman")
    read(dataBuffer, huffmanLixo)

    #gerar a arvore de huffman
    huffmanTree = generateTreeFromEncodedList(huffmanEncodedList, bitGroupSize)
    codeTable = generateCodesToSymbol(bitGroupSize, huffmanTree)
    print("Geracao de simbolos por codigo completa", codeTable)
    print("Descomprimindo ficheiro com a arvore de huffman gerada")

    #descomprime o ficheiro, lendo todos os bits, incluindo com padding, 
    #removendo-os no fim do ultimo simbolo descodificado para ficar alinhado
    outputHandler = open(nomeFicheiro.replace('.cpbm', '.pbm'), 'wb')
    codigo = str()
    simbolo = None
    outBuffer = list()
    while(tamanhoActualDescomprimido < tamanhoFicheiroComprimido):
        codigo += str(read(dataBuffer, 1))
        tamanhoActualDescomprimido +=1
        simbolo = codeTable.get(codigo)
        if(simbolo != None):
            #print("Simbolo descodificado", simbolo, "codigo:", codigo, "bin:", simbolo)
            outBuffer.extend(simbolo)
            simbolo = None
            codigo = ''

    #remover o padding
    if(paddingSize > 0):
        np.packbits(outBuffer[:-paddingSize], -1).tofile(outputHandler)
    else:
        np.packbits(outBuffer, -1).tofile(outputHandler)

    outputHandler.close()
    print("Descompressao completa")
    print("Tamanho Descomprimido", tamanhoActualDescomprimido)
    print("Bits restantes na stream:", len(dataBuffer) - tamanhoFicheiroPorLer)   
    print("Padding size:", paddingSize) 


def main():
    global nomeFicheiro
    if(len(argv) > 1):
        nomeFicheiro = argv[1]
    else:
        print("E necessario adicionar o nome do ficheiro de imagem a descomprimir")
        return

    handler = abrirFicheiro(argv[1])
    dataBuffer = lerConteudo(handler)
    descomprimir(handler, dataBuffer)
    fecharFicheiro(handler)

main()
