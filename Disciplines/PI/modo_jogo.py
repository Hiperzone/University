"""MODULO MODO
   Descricao: Implementa e controla todos os modos de jogo disponiveis
   Autores: Daniel Ramos(29423), Eduardo Martins(29035)
"""
import sys
sys.path.append("c:\\python27\\lib\\swampy")
from World import *

#VARIAVEIS CONSTANTES
LETRA_JOGADOR = ['A','B','C','D','E']
COR_JOGADOR = ['red','blue','black','green','yellow']
LIMITE_MIN_TABULEIRO = (1,1)

MODO_JOGO_NENHUM                       = 0
MODO_JOGO_HUMANO_VS_HUMANO_TEXTO       = 1
MODO_JOGO_HUMANO_VS_COMPUTADOR_TEXTO   = 2
MODO_JOGO_HUMANO_VS_HUMANO_GRAFICO     = 3
MODO_JOGO_HUMANO_VS_COMPUTADOR_GRAFICO = 4

MODO_JOGADOR_HUMANO     = 0
MODO_JOGADOR_PC         = 1

ESTADO_TABULEIRO_EM_PROGRESSO = 0
ESTADO_TABULEIRO_GAMEOVER     = 1

FLAG_NENHUM   = 0
FLAG_EMPATE   = 1
FLAG_VENCEDOR = 2

JOGADA_EM_ESPERA = 0
JOGADA_JOGADOR_SEGUINTE = 1


class Rectangulo:
    """Implementacao da class rectangulo"""
    def __init__(self):
        self.int_x = 0
        self.int_y = 0
        self.int_width = 0
        self.int_height = 0
        self.str_color = "red"
        self.str_jogador = 0
        self.tupple_coordenadas = 0

    def colide(self, x, y):
        x1 = self.int_x
        x2 = self.int_x + self.int_width
        y1 = self.int_y
        y2 = self.int_y + self.int_height
        if (x >= x1 and x <= x2 and y >= y1 and y <= y2):
            return True
        return False

class Jogador:
    """Implementacao da class jogador"""        

    def __init__(self, id, tipo):
        self.int_tipo = tipo                      # tipo de jogador
        self.str_jogador = LETRA_JOGADOR[id]      # letra do jogador
        self.str_cor_jogador = COR_JOGADOR[id]    # cor do jogador 


class Modo_Base:
    """Implementacao da class base Modo que partilha metodos
       e variaveis entre os diferentes modos de jogo
    """ 
    def __init__(self,modo, x,y, j_humano):
        self.int_id_gen         = 0     #gerador de ids do jogador
        self.list_jogadores     = []    #lista de jogadores
        self.int_jogador_actual = 0     #id do jogador actual
        self.int_estado_jogada  = 0     #estado da jogada actual
        self.int_modo_id        = modo
        #compensar aqui para que este desenha linhas/colunas adicionais para
        #formar o numero de linhas e colunas exacto na grelha
        self.int_linhas = x+1
        self.int_colunas = y+1

        #guarda os dados de cada jogada do tabuleiro
        self.dict_tabuleiro = dict()

        #inicia a lista list_quadrados com arrays bidimensionais
        #nao e necessario compensar
        #guarda os dados de cada quadrado
        self.list_quadrados = [ [0 for i in range(x)] for i in range(y)]
        if(self.int_modo_id == MODO_JOGO_HUMANO_VS_HUMANO_TEXTO or self.int_modo_id == MODO_JOGO_HUMANO_VS_HUMANO_GRAFICO):
            self.list_jogadores.append(Jogador( self.__gerar_id_jogador__(), MODO_JOGADOR_HUMANO))
            self.list_jogadores.append(Jogador( self.__gerar_id_jogador__(), MODO_JOGADOR_HUMANO))

        #seleciona o primeiro jogador a jogar de acordo com a opcao
        if(self.int_modo_id == MODO_JOGO_HUMANO_VS_COMPUTADOR_TEXTO):
            if(j_humano == 0):
                self.list_jogadores.append(Jogador( self.__gerar_id_jogador__(), MODO_JOGADOR_HUMANO))
                self.list_jogadores.append(Jogador( self.__gerar_id_jogador__(), MODO_JOGADOR_PC))
            else:
                self.list_jogadores.append(Jogador( self.__gerar_id_jogador__(), MODO_JOGADOR_PC))
                self.list_jogadores.append(Jogador( self.__gerar_id_jogador__(), MODO_JOGADOR_HUMANO))

    #gera o id correspondente ao index para cada jogador
    def __gerar_id_jogador__(self):
        int_id =  self.int_id_gen
        self.int_id_gen += 1
        return int_id
    #obtem o jogador seguinte
    def __obter_jogador_seguinte__(self):
        if(self.int_jogador_actual >= (len(self.list_jogadores)-1)):
            self.int_jogador_actual = 0
            return self.int_jogador_actual
        else:
            self.int_jogador_actual += 1
            return (self.int_jogador_actual)

    #ai, calcula a jogada baseado em prioridades
    def __ai_calcular_jogada__(self):
        #AI, calculo de jogadas.
        #caso 1: completar quadrados duplos
        #caso 2: completar quadrados com 3 lados
        #caso 2: verificar quadrados com 0 ou 1 lado, evitando quadrados com 2 lados
        #caso 3: jogar para 1 ou 2 lados de um quadrado
        #caso 4: jogar para os restantes lados de qualquer quadrado

        a = 0
        b = 0
        c = 0
        d = 0
        lados =0
        #verificar double boxes
        for i in range(self.int_linhas-1):
            for j in range(self.int_colunas-1):
                #verificar se este quadrado nao esta completo
                
                if(not self.list_quadrados[j][i]):
                    lados = 0
                    if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                        lados += 1
                        a = 0
                    else:
                        a = ((j,i),(j+1,i))

                    if(self.dict_tabuleiro.has_key(((j,i),(j,i+1)))):
                        lados += 1
                        b = 0
                    else:
                        b = ((j,i),(j,i+1))

                    if(self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1)))):
                        lados +=1
                        c = 0
                    else:
                        c = ((j,i+1),(j+1,i+1))
                       
                    if(self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                        lados +=1
                        d = 0
                    else:
                        d = ((j+1,i),(j+1,i+1))
                    if(lados == 3):
                        if( a ):
                            #verificar quadrado a sul
                            if( not self.__verificar_quadrado_out_of_bounds__(j ,i-1)):
                                #contar os lados do quadrado adjacente sul
                                count = self.__contar_lados_quadrado__(j,i-1)
                                ##jogada dupla detectada
                                if(count == 3):
                                    bool_validade, str_erro = self.__set_coordenadas__(j,i, j+1, i)
                                    assert(str_erro == None)
                                    return True
                        if(b):
                            #verificar o quadrado oeste
                            if( not self.__verificar_quadrado_out_of_bounds__(j-1 ,i)):
                                #contar os lados do quadrado adjacente oeste
                                count = self.__contar_lados_quadrado__(j-1,i)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 3):
                                    bool_validade, str_erro = self.__set_coordenadas__(j,i, j, i+1) # ((j,i),(j,i+1))
                                    assert(str_erro == None)
                                    return True
                        if(c):
                            #verificar o quadrado norte
                            if( not self.__verificar_quadrado_out_of_bounds__(j ,i+1)):
                                #contar os lados do quadrado adjacente norte
                                count = self.__contar_lados_quadrado__(j,i+1)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 3):
                                    bool_validade, str_erro = self.__set_coordenadas__(j,i+1, j+1, i+1)
                                    assert(str_erro == None)
                                    return True
                        if(d):
                            #verificar quadrados a este
                            if( not self.__verificar_quadrado_out_of_bounds__(j+1 ,i)):
                                #contar os lados do quadrado adjacente norte
                                count = self.__contar_lados_quadrado__(j+1,i)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 3):
                                    bool_validade, str_erro = self.__set_coordenadas__(j+1,i, j+1, i+1) #((j+1,i),(j+1,i+1))
                                    assert(str_erro == None)
                                    return True
        #verificar 3 single side boxes
        for i in range(self.int_linhas-1):
            for j in range(self.int_colunas-1):
                #verificar se este quadrado nao esta completo
                
                if(not self.list_quadrados[j][i]):
                    lados = 0
                    if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                        lados += 1
                        a = 0
                    else:
                        a = ((j,i),(j+1,i))

                    if(self.dict_tabuleiro.has_key(((j,i),(j,i+1)))):
                        lados += 1
                        b = 0
                    else:
                        b = ((j,i),(j,i+1))

                    if(self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1)))):
                        lados +=1
                        c = 0
                    else:
                        c = ((j,i+1),(j+1,i+1))
                       
                    if(self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                        lados +=1
                        d = 0
                    else:
                        d = ((j+1,i),(j+1,i+1))
                    if(lados == 3):
                        if(a):
                            bool_validade, str_erro = self.__set_coordenadas__(j,i, j+1, i)
                            assert(str_erro == None)
                            return True
                        if(b):
                            bool_validade, str_erro = self.__set_coordenadas__(j,i, j, i+1) # ((j,i),(j,i+1))
                            assert(str_erro == None)
                            return True
                        if(c):
                            bool_validade, str_erro = self.__set_coordenadas__(j,i+1, j+1, i+1)
                            assert(str_erro == None)
                            return True
                        if(d):
                            bool_validade, str_erro = self.__set_coordenadas__(j+1,i, j+1, i+1) #((j+1,i),(j+1,i+1))
                            assert(str_erro == None)
                            return True



                     

        #verificar quadrados com 0 e 1
        for i in range(self.int_linhas-1):
            for j in range(self.int_colunas-1):
                #verificar se este quadrado nao esta completo
                
                if(not self.list_quadrados[j][i]):
                    lados = 0
                    if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                        lados += 1
                        a = 0
                    else:
                        a = ((j,i),(j+1,i))

                    if(self.dict_tabuleiro.has_key(((j,i),(j,i+1)))):
                        lados += 1
                        b = 0
                    else:
                        b = ((j,i),(j,i+1))

                    if(self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1)))):
                        lados +=1
                        c = 0
                    else:
                        c = ((j,i+1),(j+1,i+1))
                       
                    if(self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                        lados +=1
                        d = 0
                    else:
                        d = ((j+1,i),(j+1,i+1))

                    #preencher quadrados que tenham 0 ou 1 lados
                    #evitando os quadrados adjacentes com 2 lados
                    #evita que se forme um quadrado com 3 lados disponivel na
                    #seguinte jogada
                    if(lados == 0 or lados == 1): #requer adjacencia pelos vistos
                        if(a):
                            #verificar quadrados a sul
                            if( not self.__verificar_quadrado_out_of_bounds__(j ,i-1)):
                                #contar os lados do quadrado adjacente sul
                                count = self.__contar_lados_quadrado__(j,i-1)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 2):
                                     pass
                                else:
                                     bool_validade, str_erro = self.__set_coordenadas__(j,i, j+1, i)
                                     assert(str_erro == None)
                                     return True
                            else:
                                #nao existe quadrados a sul
                                bool_validade, str_erro = self.__set_coordenadas__(j,i, j+1, i)
                                assert(str_erro == None)
                                return True

                        if(b):
                            #verificar quadrados a oeste
                            if( not self.__verificar_quadrado_out_of_bounds__(j-1 ,i)):
                                #contar os lados do quadrado adjacente oeste
                                count = self.__contar_lados_quadrado__(j-1,i)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 2):
                                     pass
                                else:
                                     bool_validade, str_erro = self.__set_coordenadas__(j,i, j, i+1) # ((j,i),(j,i+1))
                                     assert(str_erro == None)
                                     return True
                            else:

                                bool_validade, str_erro = self.__set_coordenadas__(j,i, j, i+1) # ((j,i),(j,i+1))
                                assert(str_erro == None)
                                return True

                        if(c):
                            #verificar quadrados a norte
                            if( not self.__verificar_quadrado_out_of_bounds__(j ,i+1)):
                                #contar os lados do quadrado adjacente norte
                                count = self.__contar_lados_quadrado__(j,i+1)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 2):
                                     pass
                                else:
                                     bool_validade, str_erro = self.__set_coordenadas__(j,i+1, j+1, i+1)
                                     assert(str_erro == None)
                                     return True
                            else:
                                bool_validade, str_erro = self.__set_coordenadas__(j,i+1, j+1, i+1)
                                assert(str_erro == None)
                                return True
                        if(d):
                            #verificar quadrados a este
                            if( not self.__verificar_quadrado_out_of_bounds__(j+1 ,i)):
                                #contar os lados do quadrado adjacente norte
                                count = self.__contar_lados_quadrado__(j+1,i)
                                #ignorar se o quadrado tem 2 lados preenchidos
                                if(count == 2):
                                     pass
                                else:
                                     bool_validade, str_erro = self.__set_coordenadas__(j+1,i, j+1, i+1) #((j+1,i),(j+1,i+1))
                                     assert(str_erro == None)
                                     return True
                            else:
                                bool_validade, str_erro = self.__set_coordenadas__(j+1,i, j+1, i+1)
                                assert(str_erro == None)
                                return True
        #verificar os restantes casos
        for i in range(self.int_linhas-1):
            for j in range(self.int_colunas-1):
                #verificar se este quadrado nao esta completo
                
                if(not self.list_quadrados[j][i]):
                    lados = 0
                    if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                        lados += 1
                        a = 0
                    else:
                        a = ((j,i),(j+1,i))

                    if(self.dict_tabuleiro.has_key(((j,i),(j,i+1)))):
                        lados += 1
                        b = 0
                    else:
                        b = ((j,i),(j,i+1))

                    if(self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1)))):
                        lados +=1
                        c = 0
                    else:
                        c = ((j,i+1),(j+1,i+1))
                       
                    if(self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                        lados +=1
                        d = 0
                    else:
                        d = ((j+1,i),(j+1,i+1))

                    if(a):
                        bool_validade, str_erro = self.__set_coordenadas__(j,i, j+1, i)
                        assert(str_erro == None)
                        return True
                    if(b):
                        bool_validade, str_erro = self.__set_coordenadas__(j,i, j, i+1)
                        assert(str_erro == None)
                        return True
                    if(c):
                        bool_validade, str_erro = self.__set_coordenadas__(j,i+1, j+1, i+1)
                        assert(str_erro == None)
                        return True
                    if(d):
                        bool_validade, str_erro = self.__set_coordenadas__(j+1,i, j+1, i+1)
                        assert(str_erro == None)
                        return True


        ###CRITICO### BOOM HEADSHOT, nunca deve acontecer
        return False

    #verifica e adiciona as coordenadas do jogador
    #se forem validas
    def __set_coordenadas__(self, x1, y1, x2, y2):
        #converte as coordenadas para a base original
        #y1 = self.int_linhas - y1
        #y2 = self.int_linhas - y2
        #verificacao da validade de coordenadas
        #caso 1: coordenadas de origem e destino sao iguais
        if( (x1,y1) == (x2,y2)):
            return (False, "Coordenadas sao iguais")
        #caso 2: as coordenadas forma uma diagonal
        if( abs(x1 - x2) == 1 and abs(y1 - y2) == 1):
            return (False, "Coordenadas invalidas, jogada diagonal nao" +
                    " permitida")
        #caso 3: as coordenadas de destino sao maiores que o ponto adjacente
        if( abs(x1 - x2) > 1 or abs(y1 - y2) > 1):
            return (False, "Coordenadas invalidas, ponto adjacente muito" +
                    " longe")
        #caso 4: out of bounds
        if(x1 >= self.int_colunas or x2 >= self.int_colunas or 
           y1 >= self.int_linhas or y2 >= self.int_linhas):
            return (False, "Coordenadas fora de alcance")
        #caso 4: jogada ja feita
        if( (x1,y1) > (x2,y2)):
            if( self.dict_tabuleiro.has_key( ((x2,y2),(x1,y1)) )):
                return (False, "Jogada nao permitida, a jogada ja foi feita" +
                        " anteriormente")
        else:
            if( self.dict_tabuleiro.has_key( ((x1,y1),(x2,y2)) )):
                return (False, "Jogada nao permitida, a jogada ja foi feita" +
                        " anteriormente")

        #verificacao de coordenadas. As coordenadas devem ser re-ordenadas
        #caso seja necessario
        if( (x1,y1) > (x2,y2)):
            #inverte as coordenadas
            self.dict_tabuleiro[(x2,y2),(x1,y1)] = 1
        else:
            self.dict_tabuleiro[(x1,y1),(x2,y2)] = 1
        return (True, None)

    #verifica se existe um quadrado fora de alcance
    #metodo helper para a AI
    def __verificar_quadrado_out_of_bounds__(self, x, y):
        if(x >= self.int_colunas-1 or y >= self.int_linhas-1):
            return True
        return False

    #conta os lados de um quadrado
    def __contar_lados_quadrado__(self, j, i):
        lados = 0
        if(not self.list_quadrados[j][i]):

            if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                lados += 1

            if(self.dict_tabuleiro.has_key(((j,i),(j,i+1)))):
                lados += 1

            if(self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1)))):
                    lados +=1
                       
            if(self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                    lados +=1
            return lados
        else:
            return 4

class Modo_Texto(Modo_Base):
    """Implementacao do modo texto do jogo"""

    def __init__(self,modo, x,y, j_humano):
        Modo_Base.__init__(self, modo,x,y,j_humano)

    def ai_concluir_jogada(self):
        self.__ai_calcular_jogada__()

    #verifica o tabuleiro e retorna o nr de quadrados
    #compltos
    def actualizar_tabuleiro(self, jogador):
        #verificar se o jogador completou quadrados
        #3x3 -> 0 1 2 x 0 1 2
        bool_completou_quadrados = 0
        for i in range(self.int_linhas-1):
            for j in range(self.int_colunas-1):
                if (self.dict_tabuleiro.has_key(((j,i),(j+1,i))) and
                    self.dict_tabuleiro.has_key(((j,i),(j,i+1))) and
                    self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1))) and
                    self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                    if( not self.list_quadrados[j][i]):
                        self.list_quadrados[j][i] = jogador
                        bool_completou_quadrados += 1
        return bool_completou_quadrados

    def ler_coordenadas(self,input, jogador):
        if len(input)<= 2*len(str(self.int_linhas))+2* \
            len(str(self.int_colunas))+6 and len(input)>=10:
             # verifica o input segundo comprimento, parenteses + virgulas, 
             # 6 indexes
             # minimo len possivel 10 (6 indexes + 4 unidades (coordenadas)
             # maximo len possivel (6 indexes + total de caracteres presentes 
             # no total de coordenadas)
             if (input[0]=='(') and (input[-1]==')') and (')(' in input):
                 #divisao do input em pontos de saida e partida
                 ponto1,ponto2=input[1:-1].split(')(')
                 # divisao do ponto de saida em coordenadas x e y
                 x1,y1=ponto1.split(',')
                 # divisao do ponto de chegada em coordenadas x e y
                 x2,y2=ponto2.split(',')
                 try:
                     x1=int(x1)
                     x2=int(x2)
                     y1=int(y1)
                     y2=int(y2)
                     return (x1,y1,x2,y2)
                 except:
                     print "Coordenadas nao sao validas"
                     i=raw_input(str.format("Jogador %s: segmento entre que pontos?(x1,y1)(x2,y2)" % jogador))
                     return self.ler_coordenadas(i,jogador)
             else:
                 print "Coordenadas nao sao validas"
                 i=raw_input(str.format("Jogador %s: segmento entre que pontos?(x1,y1)(x2,y2)" % jogador))
                 return self.ler_coordenadas(i,jogador)
        else:
            print "Coordenadas nao sao validas"
            i=raw_input(str.format("Jogador %s: segmento entre que pontos?(x1,y1)(x2,y2)" % jogador))
            return self.ler_coordenadas(i,jogador)

    #funcao principal
    def actualizar(self):
        #loop do modo texto
        if(self.int_estado_jogada == JOGADA_EM_ESPERA):
            self.desenhar_tabuleiro()
            str_jog = self.list_jogadores[self.int_jogador_actual].str_jogador
              
            #selecionar se e o computador ou o jogador a jogar
            if(self.list_jogadores[self.int_jogador_actual].int_tipo == MODO_JOGADOR_PC):
                print "Jogador %s(Computador):" % str_jog 
                self.ai_concluir_jogada()
                int_quadrados = self.actualizar_tabuleiro(str_jog)

                int_estado, int_flag, str_vencedor = \
                         self.obter_estado_tabuleiro()

                if(int_estado == ESTADO_TABULEIRO_GAMEOVER):
                    self.desenhar_tabuleiro()
                    if(int_flag == FLAG_EMPATE):
                        print "Fim do jogo, os jogadores empataram"
                    elif(int_flag == FLAG_VENCEDOR):
                        print "Fim do jogo. O vencedor foi o jogador ", \
                            str_vencedor
                    return False

                #se o jogador completou quadrados, ganha uma nova jogada
                if(int_quadrados):
                    self.int_estado_jogada = JOGADA_EM_ESPERA
                else:
                    self.int_estado_jogada = JOGADA_JOGADOR_SEGUINTE
            else:
                str_coordenadas = raw_input( \
                    str.format("Jogador %s: segmento entre que pontos?(x1,y1)" \
                    % str_jog + "(x2,y2)"))

                int_x1, int_y1, int_x2, int_y2 = \
                    self.ler_coordenadas(str_coordenadas,str_jog)

                bool_validade, str_erro = \
                    self.__set_coordenadas__(int_x1,int_y1,int_x2,int_y2)

                if(bool_validade):
                     #verifica o estado do tabuleiro e atribui novos quadrados 
                     #se o jogador os completou com a nova jogada.
                     #verifica tambem se o tabuleiro foi completado e retorna o 
                     #vencedor.
                     int_quadrados = self.actualizar_tabuleiro(str_jog)
                 
                     int_estado, int_flag, str_vencedor = \
                         self.obter_estado_tabuleiro()

                     if(int_estado == ESTADO_TABULEIRO_GAMEOVER):
                         self.desenhar_tabuleiro()
                         if(int_flag == FLAG_EMPATE):
                             print "Fim do jogo, os jogadores empataram"
                         elif(int_flag == FLAG_VENCEDOR):
                             print "Fim do jogo. O vencedor foi o jogador ", \
                                 str_vencedor
                         return False

                     #se o jogador completou quadrados, ganha uma nova jogada
                     if(int_quadrados):
                         self.int_estado_jogada = JOGADA_EM_ESPERA
                     else:
                         self.int_estado_jogada = JOGADA_JOGADOR_SEGUINTE

                else:
                    print str_erro
        elif(self.int_estado_jogada == JOGADA_JOGADOR_SEGUINTE):
            self.__obter_jogador_seguinte__()
            self.int_estado_jogada = JOGADA_EM_ESPERA
        return True

    def desenhar_tabuleiro(self):
        #desenha a tabela na forma invertida, comecando do fim
        #outra solucao seria desenhar na forma nao invertida e converter as 
        #coordenadas para a base actual (x1, y1 - nlinhas)(x1, y2 - nlinhas)
        #desenha a primeira linha para manter consistencia e evitar que se use
        #codigo adicional para
        #que este nao desenhe uma linha de colunas adicional no fim.

        #desenha a primeira linha
        for i in range(self.int_linhas-1, self.int_linhas-2, -1):
            #desenha n partes da linha por n colunas
            for j in range(self.int_colunas):
                #verifica se existe uma jogada: ex (0,0)(1,0)
                if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                    print 'o - - - - ',
                #fecha a linha quando esta chega a ultima coluna que equivale
                #a colunas - 1
                elif(j == (self.int_colunas-1)):
                    print 'o'
                else:
                    print 'o         ',

        #desenha as restantes linhas, comecando por desenhar a linha 
        #"vertical" que forma as colunas da primeira linha anteriormente 
        #desenhada
        # |    |     |    |
        #desenha ate a ultima linha que equivale ao zero
        # o(0,3)-----(1,3)o <- primeira linha
        # |               |
        # |     (0,2)     |
        # o(0,2)-----(1,2)o  <--- comeca aqui
        for i in range(self.int_linhas-2, -1, -1):
            #verticais
            ##consistencia, desenha 4 vezes a coluna verticalmente
            for k in range(4): 
                for j in range(self.int_colunas):
                    #verifica se existe uma jogada feita: ex: (0,0)(0,1)
                    if(self.dict_tabuleiro.has_key(((j,i),(j,i+1)))):
                        #fecha a linha "vertical" quando esta chega a ultima
                        #coluna
                        #verifica se existe um quadrado preenchido e escreve o
                        #numero do jogador
                        #a coordenada do quadrado equivale ao i e j actual
                        if(j != (self.int_colunas-1) and 
                           self.list_quadrados[j][i] and k == 2):
                            print '|   ',self.list_quadrados[j][i],'   ',
                        else:
                            print '|         ',
                    else:
                        print '          ',
                print ''
            #horizontais
            #desenha a linha seguinte
            for j in range(self.int_colunas):
                #verifica se existe uma jogada feita: ex: (0,0)(1,0)
                if(self.dict_tabuleiro.has_key(((j,i),(j+1,i)))):
                    print 'o - - - - ',
                #fecha a linha quando esta chega a ultima coluna que equivale
                #a colunas - 1
                elif(j == (self.int_colunas-1)):
                    print 'o'
                else:
                    print 'o         ',

                 
    def obter_estado_tabuleiro(self):
        #verifica se o jogo terminou
        int_counter = 0
        dict_pontos = dict()
        #contagem de pontos por jogador
        for i in range( self.int_linhas-1):
            for j in range(self.int_colunas-1):
                if self.list_quadrados[j][i]:
                    int_counter += 1
                    dict_pontos[self.list_quadrados[j][i]] =\
                    dict_pontos.get(self.list_quadrados[j][i], 0) + 1

        #verificar se ouve empate ou vencedor, reordenar pelo jogador que
        #obteve mais pontos
        #se o counter for igual ao numero de quadrados no tabuleiro
        #entao o jogo terminou
        if (int_counter == ((self.int_linhas-1) * (self.int_colunas-1))):
            
            #reordena por pontos (pontos,jogador)
            list_dados = []
            for i in dict_pontos:
                list_dados.append((dict_pontos[i], i))

            list_dados.sort(reverse = True)
            
            #se existir quadrados com mais de 1 jogador atribuido
            #verificar se ouve empate(so funciona com dois jogadores ofc)
            if(len(list_dados) > 1 and list_dados[0][0] == list_dados[1][0]):
                return (ESTADO_TABULEIRO_GAMEOVER, FLAG_EMPATE, None)
            else:
                #retorna os dados do vencedor que esta no index 0 da lista
                #que por sua vez e uma tupple
                pontos, jogador = list_dados[0]
                return (ESTADO_TABULEIRO_GAMEOVER, FLAG_VENCEDOR, jogador)
        return (ESTADO_TABULEIRO_EM_PROGRESSO, None, None)
 
class Modo_Grafico(Modo_Base):
    """Implementacao do modo grafico do jogo, usando swampy"""

    def __init__(self, modo, x, y, j_humano):
        Modo_Base.__init__(self,modo,x,y,j_humano)
        self.bool_fim_de_jogo = False

        self.cls_world = World()
        #self.canvas = self.cls_world.ca(width=((2*self.int_colunas)*100 -(self.int_colunas - 2)*100)/2, height=((2*self.int_linhas)*100 -(self.int_linhas- 2)*100)/2, background='white')
        self.canvas = self.cls_world.ca(width=(60*self.int_colunas-1)+50, height=(60*self.int_linhas-1)+50, background='white')
        self.canvas.transforms[0].shift = [0+50,self.canvas.height-50]
        self.canvas.bind('<Button-1>',self.mouse_event_clicked)
        self.canvas.bind('<Motion>',self.mouse_move)

       # self.canvas_size = ((2*self.int_colunas)*100 -(self.int_colunas - 2)*100)/2
        self.dot_distance = 60
        self.rectangulos = []
        self.preparar_tabuleiro()
    def mouse_move(self,event):
        print event.x - 50, " ", (event.y - self.canvas.height) * -1 - 50
    def actualizar(self):

        wait_for_user()
        return False
    #verifica o tabuleiro e retorna o nr de quadrados completos
    #pelo jogador
    def actualizar_tabuleiro(self, jogador):
        #verificar se o jogador completou quadrados
        #3x3 -> 0 1 2 x 0 1 2
        bool_completou_quadrados = 0
        for i in range(self.int_linhas-1):
            for j in range(self.int_colunas-1):
                if (self.dict_tabuleiro.has_key(((j,i),(j+1,i))) and
                    self.dict_tabuleiro.has_key(((j,i),(j,i+1))) and
                    self.dict_tabuleiro.has_key(((j,i+1),(j+1,i+1))) and
                    self.dict_tabuleiro.has_key(((j+1,i),(j+1,i+1)))):
                    if( not self.list_quadrados[j][i].str_jogador):
                        self.list_quadrados[j][i].str_jogador = jogador.str_jogador
                        self.list_quadrados[j][i].str_color = jogador.str_cor_jogador
                        bool_completou_quadrados += 1
        return bool_completou_quadrados

    #event do rato ao clicar
    def mouse_event_clicked(self, event):
        
        if(not self.bool_fim_de_jogo):
            #desenha os pontos
            self.canvas.clear()
            for i in range(self.int_linhas):
                for k in range(self.int_colunas):
                    self.desenhar_rectangulo(k*self.dot_distance ,i*self.dot_distance,10,10, fill = "black")


            #desenha os quadrados
            for i in range( self.int_linhas-1):
                for j in range(self.int_colunas-1):
                    
                    self.desenhar_rectangulo( self.list_quadrados[j][i].int_x , self.list_quadrados[j][i].int_y, self.list_quadrados[j][i].int_width, self.list_quadrados[j][i].int_height, fill = self.list_quadrados[j][i].str_color)
           
            #desenha os rectangulos
            for i in range(len(self.rectangulos)):
                #detectar colisao
                if(self.rectangulos[i].colide( event.x - 50, (event.y - self.canvas.height) * -1 - 50)):
                    #colisao detectada, adicionar jogada se possivel e verificar o tabuleiro etc como no modo texto
                    x1, y1, x2, y2 = self.rectangulos[i].tupple_coordenadas
                    bool_validade, str_erro = self.__set_coordenadas__(x1,y1,x2,y2)
                    str_jog = self.list_jogadores[self.int_jogador_actual].str_jogador
                    if(bool_validade):
                            #mudar a cor do rectangulo
                            self.rectangulos[i].str_color = self.list_jogadores[self.int_jogador_actual].str_cor_jogador
                            self.desenhar_rectangulo(self.rectangulos[i].int_x ,self.rectangulos[i].int_y,self.rectangulos[i].int_width,self.rectangulos[i].int_height, fill = self.rectangulos[i].str_color)
                            #verifica o estado do tabuleiro e atribui novos quadrados 
                            #se o jogador os completou com a nova jogada.
                            #verifica tambem se o tabuleiro foi completado e retorna o 
                            #vencedor.

                            int_quadrados = self.actualizar_tabuleiro(self.list_jogadores[self.int_jogador_actual])
                 
                            int_estado, int_flag, str_vencedor = \
                                self.obter_estado_tabuleiro()

                            #desenha os quadrados -.- com as alteracoes
                            for i in range( self.int_linhas-1):
                                for j in range(self.int_colunas-1):
                    
                                    self.desenhar_rectangulo( self.list_quadrados[j][i].int_x , self.list_quadrados[j][i].int_y, self.list_quadrados[j][i].int_width, self.list_quadrados[j][i].int_height, fill = self.list_quadrados[j][i].str_color)
           
                            if(int_estado == ESTADO_TABULEIRO_GAMEOVER):
                                if(int_flag == FLAG_EMPATE):
                                    print "Fim do jogo, os jogadores empataram. Feche o tabuleiro para voltar ao menu principal"
                                elif(int_flag == FLAG_VENCEDOR):
                                    print "Fim do jogo. O vencedor foi o jogador ", \
                                        str_vencedor + " Feche o tabuleiro para voltar ao menu principal"
                                self.bool_fim_de_jogo = True
                            else:
                                #se o jogador completou quadrados, ganha uma nova jogada
                                if(int_quadrados):
                                    print "Jogada extra, garantida ao jogador %s" % str_jog
                                else:
                                    self.__obter_jogador_seguinte__()

                    else:
                        print str_erro
                        self.desenhar_rectangulo(self.rectangulos[i].int_x ,self.rectangulos[i].int_y,self.rectangulos[i].int_width,self.rectangulos[i].int_height, fill = self.rectangulos[i].str_color)
                else:

                    self.desenhar_rectangulo(self.rectangulos[i].int_x ,self.rectangulos[i].int_y,self.rectangulos[i].int_width,self.rectangulos[i].int_height, fill = self.rectangulos[i].str_color)

    def desenhar_rectangulo(self,x,y,width, height, fill = ''):
        points = [[x,y],[x+width,y+height]]
        self.canvas.rectangle(points, outline='black', width=2, fill=fill)
        #self.canvas.create_rectangle(x,y,x+width,y+height, fill = fill)

    def preparar_tabuleiro(self):
        "preparar o tabuleiro grafico aqui pela primeira vez"
        #desenho e preparacao do tabuleiro
        for i in range(self.int_linhas):

            #verticais

            for k in range(self.int_colunas):
                self.desenhar_rectangulo(k*self.dot_distance ,i*self.dot_distance,10,10, fill = "black")

                #desenhar quadrados
                if(k  < self.int_colunas-1 and i < self.int_linhas-1):
                    rectc = Rectangulo()
                    rectc.int_x = k*self.dot_distance + 10
                    rectc.int_y = i*self.dot_distance + 10
                    rectc.int_width = self.dot_distance - 10
                    rectc.int_height = self.dot_distance - 10
                    rectc.str_color = "white"
                    self.list_quadrados[k][i] = rectc
                    self.desenhar_rectangulo( k*self.dot_distance + 10,i*self.dot_distance + 10, self.dot_distance-10, self.dot_distance-10, fill = "white")
  
                #desenhar rectangulos evitando a ultima linha
                if(i != self.int_linhas-1):
                    rectb = Rectangulo()
                    rectb.int_x = k*self.dot_distance
                    rectb.int_y = i*self.dot_distance + 10
                    rectb.int_width = 10
                    rectb.int_height = self.dot_distance - 10
                    rectb.str_color = "white"
                    rectb.tupple_coordenadas = ((k,i,k,i+1))
                    self.rectangulos.append(rectb)
                    self.desenhar_rectangulo( k*self.dot_distance,i*self.dot_distance + 10, 10, self.dot_distance - 10, fill = "white") #verticais


            #horizontais
            for k in range(self.int_colunas):
                #desenhar rectangulos evitando a ultima coluna
                if(k != self.int_colunas-1):
                    rect = Rectangulo()
                    rect.int_x = k*self.dot_distance + 10
                    rect.int_y = i*self.dot_distance
                    rect.int_width = self.dot_distance - 10
                    rect.int_height = 10
                    rect.str_color = "white"
                    rect.tupple_coordenadas = ((k,i,k+1,i))
                    self.rectangulos.append(rect)
                
                    self.desenhar_rectangulo(k*self.dot_distance + 10,i*self.dot_distance, self.dot_distance - 10, 10, fill = "white") #horizontais

    def obter_estado_tabuleiro(self):
        #verifica se o jogo terminou
        int_counter = 0
        dict_pontos = dict()
        #contagem de pontos por jogador
        for i in range( self.int_linhas-1):
            for j in range(self.int_colunas-1):
                if self.list_quadrados[j][i].str_jogador:
                    int_counter += 1
                    dict_pontos[self.list_quadrados[j][i].str_jogador] =\
                    dict_pontos.get(self.list_quadrados[j][i].str_jogador, 0) + 1

        #verificar se ouve empate ou vencedor, reordenar pelo jogador que
        #obteve mais pontos
        #se o counter for igual ao numero de quadrados no tabuleiro
        #entao o jogo terminou
        if (int_counter == ((self.int_linhas-1) * (self.int_colunas-1))):
            
            #reordena por pontos (pontos,jogador)
            list_dados = []
            for i in dict_pontos:
                list_dados.append((dict_pontos[i], i))

            list_dados.sort(reverse = True)
            
            #se existir quadrados com mais de 1 jogador atribuido
            #verificar se ouve empate(so funciona com dois jogadores ofc)
            if(len(list_dados) > 1 and list_dados[0][0] == list_dados[1][0]):
                return (ESTADO_TABULEIRO_GAMEOVER, FLAG_EMPATE, None)
            else:
                #retorna os dados do vencedor que esta no index 0 da lista
                #que por sua vez e uma tupple
                pontos, jogador = list_dados[0]
                return (ESTADO_TABULEIRO_GAMEOVER, FLAG_VENCEDOR, jogador)
        return (ESTADO_TABULEIRO_EM_PROGRESSO, None, None)



