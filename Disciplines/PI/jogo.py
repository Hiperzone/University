"""MODULO JOGO
   Descricao: Implementa e controla o jogo em si
   Autores: Daniel Ramos(29423), Eduardo Martins(29035)
"""

import modo_jogo

#inputs principais
INPUT_SAIR_DO_JOGO          = 0 #0
INPUT_COMECAR_JOGO_MODO_1   = 1 #modo texto pvp
INPUT_COMECAR_JOGO_MODO_2   = 2 #modo grafico pve
INPUT_COMECAR_JOGO_MODO_3   = 3 #modo texto pve
INPUT_COMECAR_JOGO_MODO_4   = 4 #modo grafico pve


#estado do jogo
GS_NENHUM = 0
GS_EM_ESPERA = 1
GS_JOGANDO = 2

class DotsAndBoxes:
    """implementacao do jogo dots and boxes, classe principal"""
    
    def __init__(self):
        self.int_estado_jogo = GS_NENHUM                    #estado do jogo
        self.int_modo = modo_jogo.MODO_JOGO_NENHUM   #modo de jogo
        self.int_linhas = 0                                 #nr linhas
        self.int_colunas = 0                                #nr colunas
        self.cls_modo = 0     #instancia da classe modo_xxx escolhida

    def gerar_sub_menu_esc_jogador(self):
        print "Escolha quando pretende jogar"
        print " 0 - Jogador A"
        print " 1 - Jogador B"

    def Gerar_Menu_Principal(self):
        print "--------Dots and Boxes-----------"
        print "Escolha uma das seguintes opcoes:"
        print " 0 - Sair do jogo"
        print " 1 - Humano vs Humano: Modo Texto"
        print " 2 - Humano Vs Humano: Modo Grafico"
        print " 3 - Humano Vs Computador: Modo Texto"

    def preparar_jogo(self):
        int_estado_perguntas = 0
        #booleanos para verificacoes por partes a sintaxe das dimensoes
        #bool_check1: verdadeiro quando a sintaxe e valida e o valor das 
        #linhas e valido
        bool_check1 = False
        #bool_check2: verdadeiro quando a sintaxe e valida e o valor das
        #colunas e valido
        bool_check2 = False

        bool_check3 = False
        while(True):
            if(int_estado_perguntas == 0):
                #reset aos checks
                bool_check1 = False
                bool_check2 = False
                str_tamanho = raw_input("Qual a dimensao do tabuleiro?" \
                                      +  "(linhas,colunas):")
                #verificacao de sintaxe (int,int)
                if(str_tamanho[0] == "("): #check OK
                    #verificar se as linhas sao numero inteiro
                    #split da string: ex: (3,3) que e adicionada a uma lista
                    inteiro_split = str_tamanho.split(",")
                    #passar a primeira parte da string para uma lista a parte
                    inteiro_list = list(inteiro_split[0])
                    #obter a parte numerica ignorando o "(" no index 0
                    inteiro = inteiro_list[1:]
                    #passa a lista para uma string
                    inteiro = str().join(inteiro)
                    #tenta converter para inteiro
           
                    try:
                        inteiro = int(inteiro)
                        #passou na conversao, dados sao validos
                        self.int_linhas = inteiro
                        bool_check1 = True
                    except:
                        print "A dimensao das linhas contem um valor invalido"

                    #dados validos, continuar
                    #verificar se as colunas sao numero inteiro e termina 
                    #com )
                    #apenas prossegue se a primeira verificacao foi valida e o 
                    #ultimo index tem um ")"
                    if(bool_check1 and str_tamanho[-1] == ")"):
                        #verificar se as int_colunas sao numero inteiro
                        #split da string: ex: (3,3) que e adicionada a uma
                        #lista
                        inteiro_split = str_tamanho.split(",")
                        #passar a segunda parte da string para uma lista a 
                        #parte
                        inteiro_list = list(inteiro_split[1])
                        #obter a parte numerica ignorando o ")" que esta 
                        #no ultimo index
                        inteiro = inteiro_list[0:-1]
                        #passa a lista para uma string
                        inteiro = str().join(inteiro)
                        #tenta converter para inteiro
                        try:
                            inteiro = int(inteiro)
                            #passou na conversao, dados sao validos
                            self.int_colunas = inteiro
                            bool_check2 = True
                        except:
                            print "A dimensao das colunas contem um valor" + \
                                " invalido"
                    else:
                         print "Sintaxe invalida para as dimensoes" + \
                             " introduzidas"

                    if(bool_check1 and bool_check2 and
                       self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_HUMANO_TEXTO or 
                       bool_check1 and bool_check2 and self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_HUMANO_GRAFICO):
                        #ambas as verificacoes sao validas, passar a proxima
                        #pergunta
                        int_estado_perguntas = 1
                        
                        if(self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_HUMANO_TEXTO):
                             #prepara o modo escolhido
                             self.cls_modo = modo_jogo.Modo_Texto(self.int_modo,self.int_linhas,self.int_colunas, None)

                        if(self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_HUMANO_GRAFICO):
                             #prepara o modo escolhido
                             self.cls_modo = modo_jogo.Modo_Grafico(self.int_modo,self.int_linhas,self.int_colunas, None)
 
                        break
                    elif (bool_check1 and bool_check2 and self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_COMPUTADOR_TEXTO):
                        int_estado_perguntas = 1
                else:
                    print "Sintaxe invalida para as dimensoes introduzidas"
            #Fim de verificacao da pergunta 1
            #perguntas para modo Humano vs computador modo texto
            if(int_estado_perguntas == 1 and self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_COMPUTADOR_TEXTO):
                if(not bool_check3):
                    self.gerar_sub_menu_esc_jogador()
                    bool_check3 = True
                str_jogador = raw_input("Opcao:")
                
                try:
                    opcao = int(str_jogador)
                except:
                    print "Opcao invalida"
                else:
                    if(len(str_jogador) > 1 or opcao > 2 or opcao < 0):
                        print "Opcao invalida"
                    else:
                        int_estado_perguntas = 2
                                
                        if(self.int_modo == modo_jogo.MODO_JOGO_HUMANO_VS_COMPUTADOR_TEXTO):
                            self.cls_modo = modo_jogo.Modo_Texto(self.int_modo,self.int_linhas,self.int_colunas, opcao)
                        break

        #preparar os jogadores e o jogo em si      
        self.int_estado_jogo = GS_JOGANDO
    #loop principal 
    def Run(self):
        if(self.int_estado_jogo == GS_NENHUM):
            self.Gerar_Menu_Principal()
            self.int_estado_jogo = GS_EM_ESPERA
            #ler opcao escolhida
            opcao = raw_input("opcao:")
            try:
                opcao = int(opcao)
            except:
                print "Opcao invalida"
                opcao = -1
            #selecionar opcao pretendida
            if(int(opcao) == INPUT_SAIR_DO_JOGO):
                return False
            elif(int(opcao) == INPUT_COMECAR_JOGO_MODO_1):
                #preparacao do jogo
                self.int_modo = \
                    modo_jogo.MODO_JOGO_HUMANO_VS_HUMANO_TEXTO
                self.preparar_jogo()
            elif(int(opcao) == INPUT_COMECAR_JOGO_MODO_2):
                self.int_modo = \
                    modo_jogo.MODO_JOGO_HUMANO_VS_HUMANO_GRAFICO
                self.preparar_jogo()
            elif(int(opcao) == INPUT_COMECAR_JOGO_MODO_3):
                self.int_modo = \
                    modo_jogo.MODO_JOGO_HUMANO_VS_COMPUTADOR_TEXTO
                self.preparar_jogo()
            else:
                 self.int_estado_jogo = GS_NENHUM


        if(self.int_estado_jogo == GS_JOGANDO):
            #actualiza o jogo actual ate este retornar falso que equivale a 
            #jogo terminado
            if( not  self.cls_modo.actualizar()):
                self.cls_modo = 0
                self.int_colunas = 0
                self.int_linhas = 0
                self.int_modo = modo_jogo.MODO_JOGO_NENHUM
                self.int_estado_jogo = GS_NENHUM
        return True


