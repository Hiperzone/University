
from math import log2
import numpy as np
from HuffmanTree import *
from sys import setrecursionlimit
from sys import argv
setrecursionlimit(4000)

tamanhoComprimidoNominal = 0
tamanhoComprimido = 0
tamanhoFicheiro = 0
tamanhoBitsPorLer = 0
tamanhoFicheiroPorLer = 0
currentBuffer = None
paddingSize = 0
nomeFicheiro = ''

##################################################################FILE MANIPULATOR##############################################################
#Tenta abrir o ficheiro
def abrirFicheiro(nomeFicheiro : str):
    try:
        handler = open(nomeFicheiro, 'rb')
        return handler
    except:
        print("Erro ao abrir ficheiro pbm")

#fecha o ficheiro
def fecharFicheiro(handler):
        if(handler):
            handler.close()

#Le o conteudo do ficheiro na sua totalidade
def lerConteudo(handler):
    global tamanhoFicheiro
    handler.seek(0,2)
    tamanhoFicheiro = handler.tell()
    handler.seek(0)
    print("Tamanho original do ficheiro", tamanhoFicheiro*8, "bits")
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

def reiniciarBufferDoFicheiro():
    global tamanhoBitsPorLer, tamanhoFicheiroPorLer
    tamanhoBitsPorLer = 0
    paddingSize = 0
    tamanhoFicheiroPorLer = 0

#################################################################ENTROPIA#########################################################

#calcula a entropia H(x)
def calcularEntropia(probabilidades):
    return -sum([v*log2(v) for (k, v) in probabilidades.items()])


#calcula as probabilidades baseado no dicionario de frequencias
def calcularProbabilidades(frequencias):
    n = sum(list(frequencias.values()))
    if(n == 0):
        n = 1
    return { k:v/n for (k, v) in frequencias.items()}

#calcula as probabilidades para uma cadeia de markov
def calcularProbabilidadesMarkov(frequencias):
    n = sum([ sum(i) for i in frequencias])
    if(n == 0):
        n = 1.0

    probabilidadesMarkov = np.array([ [0 for j in range(2)] for i in range(2)], dtype=float)
    for xt in range(2):
        for xt_1 in range(2):
            probabilidadesMarkov[xt][xt_1] = frequencias[xt][xt_1]/ n

    return probabilidadesMarkov

#Normaliza as probabilidades de uma cadeia de markov
def normalizarProbabilidadesMarkov(M):
    l, c = M.shape
    MResult = np.zeros((l,c))
    for i in range(l):
        for j in range(c):
            if(sum(M[:, j]) > 0):
                MResult[i][j] = M[i][j] / sum(M[:, j])
    return MResult

#calcula a distribuicao estacionaria
def MarkovDistEstacionaria(M):
    (val, vec) = np.linalg.eig(M)
    value = 0
    for i in val:
        if i > 0.99 and i <= 1.0:
            break
        value = value + 1
    print("Matriz Valores proprios:", val)
    print("Matriz Vectores proprios:", vec)
        
    return vec[:, value] / sum(vec[:, value])

#calcula a entropia condicional de uma matriz de transicao M e distribuicao estacionaria E
def calcularEntropiaCondicional(M, E):
    l, c = M.shape
    return -sum([ E[xt_1] * sum([ M[xt][xt_1]*log2(M[xt][xt_1]) for xt in range(c) ]) for xt_1 in range(c) ])  

#calcula a entropia conjunta a partir de uma matriz de transicao
def calcularEntropiaConjunta(M):
    l, c = M.shape
    return -sum([M[xt][xt_1]*log2(M[xt][xt_1]) for xt in range(c) for xt_1 in range(c)])

#################################################################GERACAO DE FREQUENCIAS#########################################################
#gera as frequencias para um determinado grupo
def gerarFrequencias(bitGroup, bitBuffer):
     global tamanhoFicheiro, tamanhoFicheiroPorLer, paddingSize, tamanhoBitsPorLer
     
     try:
         reiniciarBufferDoFicheiro()
         #dicionario para inserir as frequencias
         frequencias = dict()
         while(tamanhoFicheiroPorLer < tamanhoFicheiro or tamanhoBitsPorLer > 0):
            bits = read(bitBuffer, bitGroup)
            #aumentar a frequencia para o grupo lido
            freq = frequencias.get(bits, 0)
            frequencias.update( {bits : freq + 1})
         print("Padding Size:", paddingSize)
         return frequencias
     except:
        print("Processamento de ficheiro pbm concluida sem exito")
        return None

#gera as frequencias para uma cadeia de markov
def gerarMatrizFrequenciasMarkov(bitBuffer):
    try:
        reiniciarBufferDoFicheiro()
        matrizFrequencias = np.array([ [0 for j in range(2)] for i in range(2)], dtype=float)
        xt = 0 #estado actual
        xt_1 = 0 #estado anterior

        xt_1 = read(bitBuffer, 1)
        while(tamanhoFicheiroPorLer < tamanhoFicheiro or tamanhoBitsPorLer > 0):
            xt = read(bitBuffer, 1)
            matrizFrequencias[xt_1][xt] +=1
            xt_1 = xt
        return matrizFrequencias

    except:
            print("geracao da matriz da markov concluida sem sucesso")

#funcao que tenta tirar algumas conclusoes acerca dos resultados obtidos
def analisador(grupoUtilizado, alfabeto, entropia, condicional, conjunta, mutua, tamanhoComprimido = 0, 
               tamanhoEsperadoComprimido = 0, tamanhoOriginal = 1):

    taxaCompressao = (1-(tamanhoEsperadoComprimido/tamanhoOriginal))*100
    taxaCompressaoNominal = (1-(tamanhoComprimido/tamanhoOriginal))*100
    print("Agrupamento de bits:", grupoUtilizado)
    print("Entropia:", entropia)
    print("Entropia Condicional:", condicional)
    print("Entropia Conjunta:", conjunta)
    print("Informacao Mutua:", mutua)
    print("Taxa de compressao total atingida:", taxaCompressao, "%")
    print("Taxa de compressao nominal:", taxaCompressaoNominal, "%")
    print("Taxa de compressao maxima atingivel:", entropia/grupoUtilizado*100, "%")




############################################################COMPRESSOR###################################################################################
#Comprimi um ficheiro pbm
#Status: Pending, falta o padding para certos grupos
def comprimir(arvore: HuffmanNode, bitGroupSize: int, compressedBitSize, codeTable : dict, dataBuffer):
    global paddingSize, nomeFicheiro
    try:
        outputHandler = open(nomeFicheiro.replace('.pbm', '.cpbm'), 'wb')
        #codificar a arvore de huffman e escreve-la no ficheiro
        writeTreeToFile(outputHandler, compressedBitSize, paddingSize, codeTable, bitGroupSize, arvore)
        
        print("A comprimir os dados com grupos de:", bitGroupSize)
        reiniciarBufferDoFicheiro()
        outBuffer = list()
        get = codeTable.get
        bitpos = 0
        tamanhoFicheiroBits = tamanhoFicheiro * 8
        while(bitpos < tamanhoFicheiroBits):
            #ler um grupo de bits
            bitGroup = read(dataBuffer, bitGroupSize)
            codigo = get(bitGroup, None)
            if(codigo != None):
                outBuffer.extend(codigo)
            bitpos +=bitGroupSize

        print("Padding adicional no fim:", paddingSize)
        np.packbits(outBuffer, -1).tofile(outputHandler)
        outputHandler.close()
    except:
        print("Compressao de ficheiro pbm concluida sem exito")

def main():
    global tamanhoComprimido, nomeFicheiro
    #obter o nome do ficheiro da linha de comandos
    if(len(argv) > 1):
        nomeFicheiro = argv[1]
    else:
        print("E necessario adicionar o nome do ficheiro de imagem a comprimir")
        return

    print("Compressor de ficheiros pbm")

    #menu aqui com opcoes

    #abrir o ficheiro e fazer cache do conteudo
    handler = abrirFicheiro(nomeFicheiro)
    dataBuffer = lerConteudo(handler)

    #ENTROPIA
    print("A calcular a entropia")
    frequencias = gerarFrequencias(1, dataBuffer)
   
    print("Frequencias:", frequencias)
    probabilidades = calcularProbabilidades(frequencias)
    print("Probabilidades:", probabilidades)
    entropia = calcularEntropia(probabilidades)
    print("Entropia:",  entropia)

    #ENTROPIA CONDICIONAL
    matrizFrequenciasMarkov = gerarMatrizFrequenciasMarkov(dataBuffer)
    print("Matriz de frequencias de markov:", matrizFrequenciasMarkov)

    probabilidadesMarkov = calcularProbabilidadesMarkov(matrizFrequenciasMarkov)
    print("Matriz de probabilidades de markov:", probabilidadesMarkov)

    #ENTROPIA CONJUNTA
    entropiaConjunta = calcularEntropiaConjunta(probabilidadesMarkov)
    print("Entropia Conjunta:", entropiaConjunta)

    print("A normalizar a matriz de probabilidades de Markov...")
    matrizNormalizadaMarkov = normalizarProbabilidadesMarkov(probabilidadesMarkov)
    print("Matriz de transicao da cadeia de markov:", matrizNormalizadaMarkov)

    distEstacionaria = MarkovDistEstacionaria(matrizNormalizadaMarkov)
    print("Distribuicao Estacionaria:", distEstacionaria)

    entropiaCondicional = calcularEntropiaCondicional(matrizNormalizadaMarkov, distEstacionaria)
    print("Entropia condicional:", entropiaCondicional)

    print("Informacao Mutua:", entropia - entropiaCondicional)



    #DETERMINACAO DO MELHOR GRUPO PARA COMPRIMIR
    #GRUPOS MAXIMOS DE 32 bits, comeca em 2 como e obvio o porque
    print("A analisar o ficheiro para determinar a melhor taxa de compressao")
    tActual = tamanhoFicheiro*8
    tMaxActual = 0
    group = 2
    grupoAUsar = 2
    for group in range( 2, 33, 1 ):
        treeRoot = None
        reiniciarBufferDoFicheiro()
        frequencias = gerarFrequencias(group, dataBuffer)              
        probabilidades = calcularProbabilidades(frequencias)
        
        tuploProbabilidades = gerarTuploDeProbabilidades(probabilidades)
        treeList = generateTreeList(tuploProbabilidades, group)
        treeRoot = generateTree(treeList)

        codeTable = generateSymbolCodes(treeRoot)

        tMax = sum([frequencias.get(i)*len(codeTable.get(i)) for i in frequencias.keys() if frequencias.get(i) != None and frequencias.get(i)!= 0])

        arvoreCodificada = list()
        codificarArvore(treeRoot, arvoreCodificada, codeTable, group)
        #tcalculado = frequencias*comprimentoCodigo + header + tamanho da codificacao da arvore
        tCalculado = tMax + 10*8 + len(arvoreCodificada) + calcularBitsARemoverNoFim(len(arvoreCodificada)) + calcularBitsARemoverNoFim(tMax) + paddingSize
        print("Grupo:", group, " Tamanho Estimado:", tCalculado)
        

        if(tCalculado < tActual):
            tActual = tCalculado
            tMaxActual = tMax
        else:
            grupoAUsar = group - 1
            tamanhoComprimido = tActual
            tamanhoComprimidoNominal = tMaxActual
            break
 
    #COMPRIMIR FICHEIRO
    reiniciarBufferDoFicheiro()
    frequencias = gerarFrequencias(grupoAUsar, dataBuffer)              
    probabilidades = calcularProbabilidades(frequencias)
    #gerar a arvore de huffman
    treeRoot = None

    tuploProbabilidades = gerarTuploDeProbabilidades(probabilidades)
    treeList = generateTreeList(tuploProbabilidades, grupoAUsar)

    print(treeList)
    treeRoot = generateTree(treeList)

    codeTable = generateSymbolCodes(treeRoot)

    t = sum([frequencias.get(i)*len(codeTable.get(i)) for i in frequencias.keys() if frequencias.get(i) != None and frequencias.get(i)!= 0])

    #comprimir os dados
    #codificar a arvore de huffman e comprimir o ficheiro
    comprimir(treeRoot, grupoAUsar, t, codeTable, dataBuffer)
    print("Compressao concluida com sucesso")
    fecharFicheiro(handler)
    analisador(grupoAUsar, 2, entropia, entropiaCondicional, entropiaConjunta, entropia - entropiaCondicional, tamanhoComprimidoNominal, tamanhoComprimido, tamanhoFicheiro*8)

main()

