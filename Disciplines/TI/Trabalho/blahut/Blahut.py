from numpy import *
from math import *

#Calcula a capacidade de um canal
#Q - Matriz de transicoes
#p - distribuicao de probabilidade de gerar um simbolo a entrada
def calcularCapacidadeCanal(Q):
    print(Q)
    e = 0.000001 #erro
    tamanhoLinha, tamanhoColuna = Q.shape
    #distribuicao de probabilidade de ser gerado um simbolo a entrada p
    p = arange(tamanhoColuna, dtype=float)
    c = arange(tamanhoColuna, dtype=float)
    for i in p: 
        p[i]= float(1)/tamanhoColuna
        c[i]= 0.0

    k = 0
    C = 0
    j = 0
    soma1 = 0.0
    soma2 = 0.0
    soma3 = 0.0
    while True:
        j = 0
        #calcular o cj
        soma1 = 0.0
        soma2 = 0.0
        while(j < tamanhoColuna):
            soma1 = 0.0
            k = 0
            while(k < tamanhoLinha):
                #somatorio de j no log
                soma2 = 0.0
                j1 = 0
                while(j1 < tamanhoColuna):
                    soma2 += p[j1]*Q[k][j1]
                    j1 += 1
                #problema com os log2(0)
                if(Q[k][j] == 0):
                    soma1 += 0
                else:
                    soma1 += Q[k][j]*log( Q[k][j] / soma2)
                k+=1 
            c[j] = exp(soma1)
            j += 1

        #calcular Il lowerbound
        Il = [log2(sum([p[j]*c[j] for j in range(tamanhoColuna)]))]
        print("Il:",Il)

        #calcular Iu uppperbound
        Iu = [log2(max(c[j] for j in range(tamanhoColuna)))]
        print("Iu:",Iu)

        soma3 = 0.0
        if((Iu[0] - Il[0]) < e ):
            C = Il
            print("Capacidade:", C)
            break
        else:
             while(j < tamanhoColuna):
                soma3 += p[j]*c[j]
             p[j] = p[j] * (c[j] / soma3)
def main():
    print("+---- Calculo da Capacidade de um Canal ----+")
    print("+                                           +")
    print("+      1- Canal Binario Simetrico           +")
    print("+      2- Canal Binario com Perdas          +")
    print("+      3- Maquina de Escrever Ruidosa       +")
    print("+                                           +")
    print("+-------------------------------------------+")
    var = input("\nEscolher opcao: ")

    while True:
        if var=='1':
            calcularCapacidadeCanal(array([[0.9, 0.1],[0.1, 0.9]]))
            break
        elif var == '2':
         
            calcularCapacidadeCanal(array([[.8,.0],[.2,.2],[.0,.8]]))
            break
        elif var == '3':
            c = array([[0.5, 0 , 0 ,0  ,0],
	                  [ 0.5,0.5, 0 ,0  ,0],
	                  [ 0 ,0.5,0.5,0  ,0 ] ,
	                  [ 0 , 0 ,0.5,0.5,0] ,
	                  [ 0 , 0 , 0 ,0.5,0.5]])
            calcularCapacidadeCanal(c)
            break
        else:
            print("Opcao escolhida possivelmente invalida, tente de novo")
            var = input("\nEscolher opcao: ")


main()