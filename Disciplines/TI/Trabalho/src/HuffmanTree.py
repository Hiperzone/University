import struct as struct
import numpy as np

# Class com os nos da arvore de huffman
class HuffmanNode:
    def __init__(self, *args):
        self.left = None
        self.right = None
        self.value = None
        self.leaf = False
        return super().__init__(*args)

    def create(self, value, left, right, leaf: bool):
        self.left = left
        self.right = right
        self.leaf = leaf
        self.value = value

#converte uma distribuicao de probabilidade em tuplos para ser gerado uma arvore valida
def gerarTuploDeProbabilidades(probabilidades):
        return [(v, k + 1, k ) for (k, v) in probabilidades.items()]


#Gera uma lista de tuplos triplos a partir de uma lista de probabilidades
#Status: Pending
#Todo: mudar o numero aleatorio que se nao, nao ha bits pa isto tudo
def generateTreeList(prob : list, groupSize):
    probcpy = prob.copy()
    return generateTreeList_(probcpy, groupSize)

def generateTreeList_(prob : list, groupSize):
    prob.sort()
    prob.reverse()
    if(len(prob) > 1):
        probabilidade1, simbolo1, simbolo11 = prob.pop()
        probabilidade2, simbolo2, simbolo22 = prob.pop()
            
        prob.append((probabilidade1 + probabilidade2, probabilidade1  + simbolo1 + 2*groupSize, (simbolo11, simbolo22 )))
        return generateTreeList_(prob, groupSize)
    else:
        return prob


#gera uma arvore a partir de uma lista gerada internamente
#o valor rng e um valor aleatorio unico entre todos os tuplos de
#probabilidade para diferenciar os simbolos com probabilidades iguais
#Status: Finalizada 
def generateTree(treeList : list):
    rootProb, rng, root = treeList[0]
    return __generateTree__(root)

#metodo interno para gerar uma arvore a partir de uma lista de tuplos
#triplos
#Test: Testar possiveis limites de profundidade recursiva com grupos
#      maximos
#Status: Finalizada
def __generateTree__(treeList : list):
    result = HuffmanNode()
    if(len(treeList) > 0):
        esq, dir = treeList
        if(type(dir) == int):
            result.right = HuffmanNode()
            result.right.create(dir, None, None, True)
        if(type(esq) == int):
            result.left = HuffmanNode()
            result.left.create(esq, None, None, True)

        if(type(dir) == tuple):
            result.right = __generateTree__(dir)

        if(type(esq) == tuple):
            result.left = __generateTree__(esq)
               
    return result

#gera os codigos para cada simbolo
def generateSymbolCodes(root: HuffmanNode):
        codeTable = dict()
        __generateSymbolCodes__(codeTable, root, 0)
        return codeTable

def __generateSymbolCodes__(codeTable : dict, node, position : int, currCode : list = []):
    if(node == None): return

    if(node.leaf == True):
        symbol = node.value
        codeTable.update( {symbol : currCode[:position]})

        #codificar a lista num numero inteiro de 32 bits para acesso rapido ao descomprimir

        position -= 1
    if(node.left != None):
        currCode.insert(position, 0)
        position +=1
        __generateSymbolCodes__(codeTable, node.left, position, currCode)
        position -= 1

    if(node.right != None):
        currCode.insert(position, 1)
        position += 1
        __generateSymbolCodes__(codeTable, node.right, position, currCode)

        position -= 1

#Codifica a arvore para ser adicionada no ficheiro
def codificarArvore(node : HuffmanNode, resultado : list, codeTable, bitGroupSize):
   if (node == None):
       return
   if(node.left != None):
       resultado.append(0)
       codificarArvore(node.left, resultado, codeTable, bitGroupSize)
           
   if(node.right != None):
       resultado.append(0)
       codificarArvore(node.right, resultado, codeTable, bitGroupSize)
    
   if(node.leaf == True):
       tuplo = (node.value, len(codeTable.get(node.value)))
       resultado.append(1)

       #converter o simbolo para binario
       remainingGroupBitsToBeWriten = bitGroupSize
       while(remainingGroupBitsToBeWriten > 0):
           bit = node.value >> (remainingGroupBitsToBeWriten-1) & 1
           remainingGroupBitsToBeWriten -= 1 #numero de bits ainda que precisam ser escritos
           resultado.append(bit)

# Escreve a arvore de huffman no ficheiro na sua forma comprimida
# Os bits de cada grupo sao inseridos sequencialmente bit a bit no ficheiro.
def writeTreeToFile(handler, compressedBitSize : int, paddingSize, codeTable : dict, bitGroupSize, arvore : HuffmanNode):
    print("Comprimindo a arvore de huffman...")
    handler.write(struct.pack('i', compressedBitSize)) #tamanho do ficheiro comprimido
    handler.write(struct.pack('B', bitGroupSize)) #tamanho do grupo
    handler.write(struct.pack('B', paddingSize)) #tamanho do grupo
        
    print("A gerar a sequencia de codificacao da arvore de huffman")
    arvoreCodificada = list()
    codificarArvore(arvore, arvoreCodificada, codeTable, bitGroupSize)
    print(arvoreCodificada)
    handler.write(struct.pack('i', len(arvoreCodificada))) #tamanho da arvore codificada
        
    print("A escrever a arvore para o ficheiro de output")
    np.packbits(arvoreCodificada, -1).tofile(handler)
    # handler.write(np.packbits(arvoreCodificada, -1).tostring()) segunda versao
    print("Escrita da arvore de huffman completa")

#calcula quantos bits sao lixo na codificacao da arvore de huffman
def calcularBitsARemoverNoFim(size):
    i = 0
    t = 0
    while( (size + i) % 8 != 0):
        i +=1

    return i

#gera a arvore a partir da lista codificada
def generateTreeFromEncodedList(encodedList : list, bitGroupSize):
    pilha = list()
    #criar a raiz
    node = HuffmanNode()
    node.create(1, None, None, False)
    root = node
    pilha.append(node)
        
    i = 0
    currentNode = None
    while(i < len(encodedList)):
        currentNode = pilha[-1]
        bit = encodedList[i]

        #esvaziar a pilha com os nos preenchidos
        while(currentNode.left != None and currentNode.right != None):
            pilha.pop()
            currentNode = pilha[-1]

        if(bit == 0):
            if(currentNode.left == None):
                currentNode.left = HuffmanNode()
                currentNode.left.create(0, None, None, False)
                pilha.append(currentNode.left)
            elif(currentNode.right == None):
                currentNode.right = HuffmanNode()
                currentNode.right.create(0, None, None, False)
                pilha.append(currentNode.right)

        if(bit == 1):
            bitGroup = 0B0
            bitsLeft = bitGroupSize
            while( i < len(encodedList) and bitsLeft > 0):
                    i += 1
                    bitGroup = bitGroup << 1 | (encodedList[i])
                    bitsLeft -= 1
 
            currentNode.create(bitGroup,None, None, True)
            pilha.pop()
        i += 1
    return root         


#gera os codigos por cada simbolo            
def generateCodesToSymbol( bitGroup, root):
    codeTable = dict()
    __generateCodesToSymbol__(bitGroup, codeTable, root, 0)
   
    return codeTable


def __generateCodesToSymbol__(bitGroup, codeTable, node, position : int, code : list = []):
    if(node == None): return

    if(node.leaf == True):
        symbol = node.value
            
        codigo = str()
        for i in range(position):
            codigo += str(code[i])

        c = list()
        for i in range(bitGroup-1, -1, -1):
            c.extend([symbol >> i & 1])

        codeTable.update( {codigo : c})

        position -= 1
    if(node.left != None):
        code.insert(position, 0)
        position +=1
        __generateCodesToSymbol__(bitGroup, codeTable, node.left, position, code)
        position -= 1

    if(node.right != None):
        code.insert(position, 1)
        position += 1
        __generateCodesToSymbol__(bitGroup, codeTable, node.right, position, code)

        position -= 1
