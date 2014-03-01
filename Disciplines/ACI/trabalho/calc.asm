
.data
PROGRAMA_CORRENDO: .word 0
ENDERECO_STACK: .word 0
TAMANHO_STACK: .word 0
POSICAO_STACK: .word 0
ACTIVAR_PROCESSAMENTO_VF: .word 1

Titulo: .asciiz  "Calculadora polonesa inversa, programado por Daniel Ramos(29423) e Simao Ramos(29035) versao final\nA calculadora apenas permite 10 operandos como limite\nPara sair do programa escreva exit\n"
STACK_STR: .asciiz "[STACK]\n"
STACK_VAZIA: .asciiz "<Vazia>\n"
ERROR_TAMANHO_INSUFICIENTE: .asciiz "Erro, Tamanho da pilha insuficiente para realizar operacoes\n"
ERROR_OUT_OF_MEMORY: .asciiz "Erro, memoria insuficiente\n"
ERROR_DADOS_INVALIDOS: .asciiz "Erro, Operando ou operador desconhecido\n"
ERROR_FP_N_SUPORTADO: .asciiz "Erro, virgula flutuante nao suportado\n"

NEG: .asciiz "neg"
CLR: .asciiz "clear"
DEL: .asciiz "del"
SWAP: .asciiz "swap"
DUP: .asciiz "dup"
SUM: .asciiz "+"
SUB: .asciiz "-"
DIV: .asciiz "/"
MUL: .asciiz "*"
EXIT: .asciiz "exit"

ARRAY_BRACKET_I: .asciiz "["
ARRAY_BRACKET_E: .asciiz "]\n"
.text


###############################################################
# main - funcao principal
#
# Argumentos:: 
# None
#
# Retorna:
# None
###############################################################	
main:
	addi $sp, $sp, -4
	sw $ra, 0($sp)
	#Header do programa
	la $a0, Titulo #le o endereço do Titulo
	li $v0, 4      #servico: print string
	syscall

	#criar stack para guardar calculos
	li $a0, 40 #argumento 1: tamanho da memoria a ser alocada(tamamho/4 = n operandos)
	li $v0, 9 #servico sbrk
	syscall
	
	la $t0, ENDERECO_STACK
	move $t1, $v0 #move o endereco de memoria da alocacao para t1
	sw $t1, 0($t0) #guarda o endereco de memoria da stack
	
	la $t0, TAMANHO_STACK
	li $t1, 40 # t1 = 40
	sw $t1, 0($t0) #guarda o tamanho da stack
	
	la $t0, POSICAO_STACK
	li $t1, 0 
	sw $t1, 0($t0) #guarda a posicao da stack
	
	la $t0, PROGRAMA_CORRENDO
	li $t1, 1 #program running
	sw $t1, 0($t0) #guarda o estado do programa
	
main_loop:
	la $t0, PROGRAMA_CORRENDO #carregar o endereco da label PROGRAMA_CORRENDO que esta em .data
	lw $t0, 0($t0) #carregar o valor que esta no endereco
	
	jal LerInput #funcao LerInput
	nop
	
	move $a0, $v0 #edereço da string retornada pelo LerInput
	li $a1, 255   #tamanho do buffer da string
	
	jal ProcessarString #processar input
	nop
	
	la $t0, PROGRAMA_CORRENDO #programa correndo?
	lw $t0, 0($t0)

	bne $t0, $zero, main_loop #parar programa? 0 = sim, 1 = nao
	nop
	
	lw $ra, 0($sp) #repor o registo $ra
	addi $sp, $sp, 4 #desalocar espaco da memoria stack
	jr $ra
	nop
	
###############################################################
# VFActivado - verifica se o processamento de virgula flutuante
#              esta activado
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - estado
###############################################################	
VFActivado:
	#carregar o valor que esta na label ACTIVAR_PROCESSAMENTO_VF
	la $t0, ACTIVAR_PROCESSAMENTO_VF
	lw $t0, 0($t0)
	
	beq $t0, $zero, VFActivado_Falso #Virgula flutuante activado?
	nop
	
	li $v0, 1 #sim
	jr $ra
	nop

VFActivado_Falso:
	li $v0, 0 #nao
	jr $ra
	nop

###############################################################
# MostrarStack - Mostra o estado da stack na consola
#
# Argumentos:: 
# None
#
# Retorna:
# None
###############################################################	
MostrarStack:
	addi $sp, $sp, -16 #alocar espaco na memoria stack
	sw $ra, 0($sp) #guardar o registo ra na stack
	
	#carrega o valor que esta no endereco da posicao da stack
	la $t0, POSICAO_STACK
	lw $t0, 0($t0) 
	
	li $t1, 0 #counter
	
	#carregar o endereco da stack
	la $t2, ENDERECO_STACK #endereço da stack
	lw $t2, 0($t2)
	
	#chamar o servico print string para mostrar a string da label
	la $a0, STACK_STR
	li $v0, 4      #servico: print string
	syscall
	
	beq $t0, $zero, MostrarStack_Vazia #string vazia?
	nop
	
MostrarStack_Loop:
	# counter < posicao da stack?
	bge $t1, $t0, MostrarStack_Return
	nop
	
	#Brackets [ ]
	la $a0, ARRAY_BRACKET_I # [
	li $v0, 4      #servico: print string
	syscall
	
	sw $t0, 4($sp) #guardar a posicao da stack
	sw $t1, 8($sp) #guardar o counter
	sw $t2, 12($sp) #guardar o endereco da stack
	
	jal VFActivado
	nop
	lw $t0, 4($sp) #repor a posicao da stack
	lw $t1, 8($sp) #repor o counter
	lw $t2, 12($sp) #repor o endereco da stack
	
	lw $t4, 0($t2) #carregar o valor que esta na stack na posicao actual
	
	bne $v0, $zero, MostrarStack_VF #mostrar em forma de virgula flutuante?
	nop
	
	move $a0, $t4
	li $v0, 1 #servico, print int
	syscall
	j MostrarStack_FecharBracket
	nop
	
MostrarStack_VF:
	mtc1 $t4, $f12 #mover para o coprocessor1
	li $v0, 2 #servico, print float
	syscall

MostrarStack_FecharBracket:	
	la $a0, ARRAY_BRACKET_E # ]
	li $v0, 4      #servico: print string
	syscall

	addi $t2, $t2, 4 #incrementar o endereço em 4
	addi $t1, $t1, 1 #incrementar o counter
	j MostrarStack_Loop
	
MostrarStack_Vazia:
	la $a0, STACK_VAZIA
	li $v0, 4      #servico: print string
	syscall
		
MostrarStack_Return:
	lw $ra, 0($sp) #repor o registo ra
	addi $sp, $sp, 16 #desalocar o espaco da memoria stack
	jr $ra
	nop	

###############################################################
# ConverterParaVF - converte a parte inteira e decimal para 
#                   virgula flutuante
#
# Argumentos:: 
# a0 - parte inteira
# a1 - parte decimal
# a2 - tamanho
# Retorna:
# v0 - valor convertido
###############################################################	
ConverterParaVF:
	li $t0, 1 #divisor
	mtc1 $a0, $f4 #mover parte inteira para o coprocessor1
	cvt.s.w $f4, $f4 #converter para VF
	
	mtc1 $a1, $f6 #mover a parte decimal para o coprocessor1
	cvt.s.w $f6, $f6 #converter para VF
	
	#calcular divisor
ConverterParaVF_CalcDivisor:
	
	beq $a2, $zero, ConveterParaVF_CalcDivisor_End
	nop
	
	mulu $t0, $t0, 10 #multiplicar por 10
	sub $a2, $a2, 1 #subtrair 1 ao tamanho
	j ConverterParaVF_CalcDivisor
	nop
	
ConveterParaVF_CalcDivisor_End:
	
	mtc1 $t0, $f8 #mover o divisor para o coprocessor1
	cvt.s.w $f8, $f8 #converter para float
	
	div.s $f10, $f6, $f8 #dividir a parte decimal pelo divisor
	
	add.s $f4, $f4, $f10 #adicionar a parte decimal a parte inteira
	
	mov.s $f0, $f4 #mover o resultado para ser retornado
	jr $ra
	nop

###############################################################
# StackAdicionarVF - Adiciona um numero virgula flutuante a Stack
#
# Argumentos:: 
# a0 - float
#
# Retorna:
# v0 - erro
###############################################################	
StackAdicionarVF:
	addi $sp, $sp, -4    #alocar espaço na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na memoria stack
	
	#Verificação de espaço
	#carregar os valores da POSICAO_STACK e TAMANHO_STACK
	la $t0, POSICAO_STACK
	la $t1, TAMANHO_STACK
	lw $t0, 0($t0)
	lw $t1, 0($t1)
	
	mul $t0, $t0, 4 #multiplicar a posicao por 4 (alinhamento de endereço)
	bge $t0, $t1, StackAdicionarVF_Erro_OutOfMemory #se o espaco for >= que $t0, erro out of memory
	nop
	
	#fim de erros
	la $t2, ENDERECO_STACK #carregar o endereço de memoria da stack
	lw $t2, 0($t2)
	add $t2, $t2, $t0 #nova posicao da stack
	swc1 $f12, 0($t2) #guardar o valor no topo da stack

	#carregar o valor da POSICAO_STACK
	la $t0, POSICAO_STACK
	lb $t1, 0($t0)
	addi $t1, $t1, 1 #incrementar a posicao da stack
	sw $t1, 0($t0) #guardar a nova posicao
	
	j StackAdicionarVF_Return
	nop
	
StackAdicionarVF_Erro_OutOfMemory:

	la $a0, ERROR_OUT_OF_MEMORY #carregar o endereço do erro
	jal MostrarErro
	nop
	
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack
	li $v0, -1   #-1: erro 
	jr $ra
	nop
	
StackAdicionarVF_Return:
	li $v0, 0 #sem erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack
	addi $v0, $zero, 0   #0: sem erros 
	jr $ra
	nop	
	
###############################################################
# StackAdicionarInt - Adiciona um inteiro a Stack
#
# Argumentos:: 
# a0 - Int
#
# Retorna:
# v0 - erro
###############################################################	
StackAdicionarInt:
	addi $sp, $sp, -4    #alocar espaço na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na memoria stack
	
	#Verificação de espaço
	#carregar os valores da POSICAO_STACK e TAMANHO_STACK
	la $t0, POSICAO_STACK
	la $t1, TAMANHO_STACK
	lw $t0, 0($t0)
	lw $t1, 0($t1)
	
	mul $t0, $t0, 4 #multiplicar a posicao por 4 (alinhamento de endereço)
	bge $t0, $t1, StackAdicionarInt_Erro_OutOfMemory #se o espaco for >= que $t0, erro out of memory
	nop
	
	#fim de erros
	la $t2, ENDERECO_STACK #carregar o endereço de memoria da stack
	lw $t2, 0($t2) #carregar o valor que esta na label ENDERECO_STACK
	add $t2, $t2, $t0 #nova posicao da stack
	sw $a0, 0($t2) #guardar o valor no topo da stack

	la $t0, POSICAO_STACK
	lb $t1, 0($t0) #carregar o valor da posicao da stack
	addi $t1, $t1, 1 #incrementar a posicao da stack
	sw $t1, 0($t0) #salvar a nova posicao
	
	j StackAdicionarInt_Return
	nop
	
StackAdicionarInt_Erro_OutOfMemory:

	la $a0, ERROR_OUT_OF_MEMORY #carregar o endereço do erro
	jal MostrarErro
	nop
	
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack
	li $v0, -1   #-1: erro 
	jr $ra
	nop
	
StackAdicionarInt_Return:
	li $v0, 0 #sem erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack
	addi $v0, $zero, 0   #0: sem erros 
	jr $ra
	nop	
	
###############################################################
# MostrarErro - mostra o erro na consola
#
# Argumentos:: 
# a0 - String
#
# Retorna:
# None
###############################################################	
MostrarErro:
	li $v0, 4      #servico: print string
	syscall
	jr $ra         #return
	nop

###############################################################
# LerInput - le o input da consola 
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - String
###############################################################	
LerInput:
	#alocar memoria dinamica para guardar a string
	li $a0, 255          #argumento 1: tamanho da memoria a ser alocada
	li $v0, 9	     #servico
	syscall
	
	add $a0, $zero, $v0  #argumento 1: endereco da array alocada dinamicamente
	li $a1, 255          #argumento 2: buffer
	li $v0, 8	     #servico: read string
	syscall
	move $v0, $a0        #move o endereço da array para o registo v0
	
	jr $ra               #return
	nop

###############################################################
# ProcessarString - forma uma string parcial obtida lendo os 
#                   caracteres da string obtida no input
#
# Argumentos:: 
# a0 - String
#
# Retorna:
# v0 - erro
###############################################################	
ProcessarString:
	addi $sp, $sp, -16   #alocar espaco na memoria stack
	sw $ra, 0($sp)       #Variavel Local: guardar o endereco no registo $ra na memoria stack
	sw $a0, 4($sp)       #Variavel Local: guarda o endereço do argumento(String) na memoria stack
	
	#inicializacao
	li $t0, 0 #endereço de memoria da string parcial gerada
	li $t1, 0 #endereço de memoria do argumento $a0(String)
	li $t3, 0
	li $t4, 0
		
	#alocar memoria dinamica para guardar a string parcial
	li $a0, 255          #argumento 1: tamanho da memoria a ser alocada
	li $v0 9	     #servico
	syscall
	
	#por a string parcial a zero
	move $a0, $v0
	li $a1, 0
	li $a2, 255
	jal memset
	nop
	
	sw $v0, 8($sp)      #guarda o endereço alocado na memoria stack que corresponde a string parcial
	sw $zero, 12($sp)   #guarda o tamanho da string parcial, valor zero inicialmente
	move $t0, $v0       #move o endereço da string parcial em v0 para t0 para processamento
	
	lw $t1, 4($sp)  #endereço do argumento a0(String)
	lw $t0, 8($sp)  #endereco da string parcial
	lw $t3, 12($sp) #tamanho da string parcial processada
	

ProcessarString_ObterCharacter: #loop, obter caracteres
	lb $t2, 0($t1)  #carrega 1 byte da string completa que esta em $t1
	
	beq $t2, $zero, ProcessarString_ProcessarFimDeString #se o byte for 0x00(null), termina a funcao
	nop
	beq $t2, 0x0A, ProcessarString_ProcessarFimDeString #detectar \n
	nop
	
	#ler string ate encontrar espacos 0x20, ou 0x0A \n
	li $t4, 32  #registo com o  codigo ascii do espaco 0x20
	beq $t2, $t4, ProcessarString_Processar #comparacao, $t2 igual a 0x20?
	nop
	
	sb $t2, 0($t0) #guardar o caracter na string parcial
	
	addi $t1, $t1, 1 #incrementar endereço do argumento a0
	addi $t0, $t0, 1 #incrementar endereço da string parcial
	addi $t3, $t3, 1 #incrementar o tamamnho da string parcial
	
	j ProcessarString_ObterCharacter #loop jump
	nop

ProcessarString_ProcessarFimDeString:
	
	sb $zero, 0($t0) #adicionar um zero ao fim da string gerada just in case, TODO: Verificar se ha espaco no buffer
	add $a0, $zero, $t0 #adiciona o endereço da string parcial ao argumento a0
	sub $a0, $a0, $t3 #subtrai o endereço pelo tamanho da string para que este volte ao endereço original
	add $a1, $zero, $t3 #tamanho da string parcial
	jal ProcessarSubString
	nop
	bne $v0, $zero, ProcessarString_Return
	nop
	
	#mostrar stack
	jal MostrarStack
	nop
	
	j ProcessarString_Return
	nop
ProcessarString_Processar:
	#processar comando
	addi $t1, $t1, 1 #incrementar endereço do argumento a0
	sb $zero, 0($t0) #adicionar um zero ao fim da string gerada just in case, TODO: Verificar se ha espaco no buffer
	sub $t0, $t0, $t3 #subtrair o endereço da string parcial para o valor original
	add $a0, $zero, $t0 #adiciona o endereço da string parcial ao argumento a0
	add $a1, $zero, $t3 #tamanho da string parcial
	
	li $t3, 0 #reset ao tamanho da string parcial processada
	sw $t0, 8($sp)  #guarda o endereço incrementado da string parcial na stack
	sw $t1, 4($sp) #guarda o endereço incrementado do argumento a0 na stack
	sw $t3, 12($sp) #tamanho da string parcial
	
	jal ProcessarSubString
	nop
	bne $v0, $zero, ProcessarString_Return
	nop
	
	lw $t1, 4($sp)  #endereço do argumento a0(String)
	lw $t0, 8($sp)  #endereco da string parcial
	lw $t3, 12($sp) #tamanho da string parcial processada
	
	#por a string parcial a zero
	move $a0, $t0
	li $a1, 0
	li $a2, 255
	jal memset
	nop
	
	lw $t1, 4($sp)  #endereço do argumento a0(String)
	lw $t0, 8($sp)  #endereco da string parcial
	lw $t3, 12($sp) #tamanho da string parcial processada
	
	j ProcessarString_ObterCharacter
	nop

ProcessarString_Return:
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 16     #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# ProcessarSubString - Processa uma string gerada a partir de 
#		       outra    
#
# Argumentos:: 
# a0 - endereco do buffer
# a1 - tamanho
#
# Retorna:
# v0 - erro
###############################################################	
ProcessarSubString:
	addi $sp, $sp, -4   #alocar espaco na memoria stack
	sw $ra, 0($sp)       #Variavel Local: guardar o endereco no registo $ra na memoria stack
	
	li $t0, 0
	li $t1, 0
	
	lb $t0, 0($a0)  #carrega 1 byte da string
	
	beq $a1, $zero, ProcessarSubString_Error #nao contem dados validos, string vazia
	nop
	
	li $t1, 32  #registo com o  codigo ascii do espaco 0x20
	beq $t0, $t1, ProcessarSubString_Error #nao contem dados validos, duplo espaco detectado
	nop

	#processar comando correspondente a string
	jal ProcessarComando
	nop
	bne $v0, $zero, ProcessarSubString_Error
	nop
	li $v0, 0 #sem erros
	j ProcessarSubString_Return
	nop
	
ProcessarSubString_Error:
	#limpar a stack
	jal OperacaoClear
	nop
	#mostrar a stack
	jal MostrarStack
	nop
	
	#mostrar o erro
	la $a0, ERROR_DADOS_INVALIDOS
	jal MostrarErro
	nop

	li $v0, -1 #erro
	
ProcessarSubString_Return:
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# memset - preenche a memoria com um dado valor    
#
# Argumentos:: 
# a0 - endereco do buffer
# a1 - dados a por
# a2 - tamanho
#
# Retorna:
# None
###############################################################	
memset:
	li $t0, 0 #counter
memset_loop:

	beq $t0, $a2, memset_Return #t0 == a2?
	nop
	sb $a1, 0($a0) #guardar o valor de a1 no buffer
	addi $a0, $a0, 1 #incrementar o endereco do buffer
	addi $t0, $t0, 1 #incrementar o counter
	j memset_loop
	nop
	
memset_Return:
	jr $ra
	nop

###############################################################
# ProcessarOperando - processa o operando	    
#
# Argumentos:: 
# a0 - String
# a1 - tamanho
#
# Retorna:
# v0 - erro
###############################################################	
ProcessarOperando:
	addi $sp, $sp, -4 #alocar espaco na memoria stack
	sw $ra, 0($sp) #guardar o registo ra na memoria stack

	#Converter para inteiro
	jal ConverterParaInteiro
	nop
	
	#move o resultado da conversao e adiciona a stack
	move $a0, $v0
	jal StackAdicionarInt
	nop
	
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# ProcessarOperador - processa o operador se possivel	    
#
# Argumentos:: 
# a0 - String
#
# Retorna:
# v0 - erro
###############################################################	
ProcessarOperador:
	addi $sp, $sp, -8 #alocar espaco na memoria stack
	sw $ra, 0($sp) #guardar o registo ra na memoria stack
	sw $a0, 4($sp) #guardar o argumento a0 na memoria stack
	li $t0, 0
	
ProcessarOperador_CASE1:	
	#Comparar se e a soma
 	la $a1, SUM
  	jal strcmp
  	nop
	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE2
  	nop
  	#somar
  	jal OperacaoSoma
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE2: 
 	#Comparar se e a subtracao
 	la $a1, SUB
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE3
  	nop
  	#subtracao
  	jal OperacaoSubtracao
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE3:
 	#Comparar se e a multiplicacao
 	la $a1, MUL
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE4
  	nop
  	#multiplicar
  	jal OperacaoMultiplicacao
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
ProcessarOperador_CASE4:
	#Comparar se e a divisao
 	la $a1, DIV
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE5
  	nop
  	#divisao
  	jal OperacaoDivisao
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE5:
	#Comparar se e o clear
 	la $a1, CLR
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE6
  	nop
  	#clear
  	jal OperacaoClear
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE6:
	#Comparar se e a duplicar
 	la $a1, DUP
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE7
  	nop
  	#duplicar
  	jal OperadorDup
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE7:
	#Comparar se e a negacao
 	la $a1, NEG
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE8
  	nop
  	#negar
  	jal OperadorNeg
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE8:
	#Comparar se e o del
 	la $a1, DEL
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE9
  	nop
  	#apagar
  	jal OperadorDel
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	  	
ProcessarOperador_CASE9:
	#Comparar se e o swap
 	la $a1, SWAP
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_CASE10
  	nop
  	#swap
  	jal OperadorSwap
  	nop
  	##!retorna v0
  	j ProcessarOperador_Return
  	nop
  	
ProcessarOperador_CASE10:
	#Comparar se e o exit
 	la $a1, EXIT
  	jal strcmp
  	nop
  	lw $a0, 4($sp) #restaurar os argumentos
  	bne $v0, $zero, ProcessarOperador_DEFAULT
  	nop
  	#sair do programa
  	la $t0, PROGRAMA_CORRENDO
  	sw $zero, 0($t0)
  	##!retorna v0
  	li $v0, 0 #sem erros
  	j ProcessarOperador_Return
  	nop
  	  	  	  	  	  	
ProcessarOperador_DEFAULT:
 	#erro
 	la $a0, ERROR_DADOS_INVALIDOS
 	jal MostrarErro  
 	nop
 	li $v0, -1 #erro
 	
ProcessarOperador_Return:
	##!retorna v0
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8    #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# EDigitoInteiro - Detecta se é um numero	    
#
# Argumentos:: 
# a0 - String
#
# Retorna:
# v0 - erro
###############################################################	
EDigitoInteiro:

EDigitoInteiro_loop:
	lb $t1, 0($a0) #load do primeiro byte da string parcial
	beq $t1, $zero, EDigitoInteiro_Verdade #verificaçao do final da string parcial
	nop
	
	slti  $t2, $t1, 0x30 #segundo o código ASCII os 10 digitos encontram-se entre o byte [0x30, 0x39]
	bne $t2, $zero, EDigitoInteiro_Falso #comparar se o caractere se encontra abaixo de 0x30 
	nop
	
	slti $t2, $t1, 0x40 #definir o range para menos de 0x40 exclusive, logo [0x30, 0x39]
	beq $t2, $zero, EDigitoInteiro_Falso #comparar se o caractere se encontra acima de 0x40
	nop
	
	addi $a0, $a0, 1 #incrementar o endereco do argumento em 1
	j EDigitoInteiro_loop
	nop
	
EDigitoInteiro_Verdade:
	li $v0, 1 #e inteiro
	jr $ra
	nop
	
EDigitoInteiro_Falso:
	li $v0, 0 #nao e inteiro
	jr $ra
	nop


###############################################################
# EDigitoVF - Detecta se é um numero virgula flutuante	    
#
# Argumentos:: 
# a0 - String
#
# Retorna:
# v0 - erro
###############################################################	
EDigitoVF:

EDigitoVF_loop:
	lb $t1, 0($a0) #load do primeiro byte da string parcial
	beq $t1, $zero, EDigitoVF_Return #verificaçao do final da string parcial
	nop
	
	beq $t1, 0x2E, EDigitoVF_VirgulaDetectada #t1 contem uma virgula?
	nop
	
	addi $a0, $a0, 1 #incrementar endereco do a0
	
	j EDigitoVF_loop
	nop
	
EDigitoVF_VirgulaDetectada:
	li $v0, 1
	jr $ra
	nop
EDigitoVF_Return:
	li $v0, 0
	jr $ra
	nop
	
###############################################################
# ProcessarVF - Processa a string, convertendo para VF	    
#
# Argumentos:: 
# a0 - String
#
# Retorna:
# v0 - erro
###############################################################	

ProcessarVF:
	addi $sp, $sp, -20 #alocar espaco na memoria stack
	sw $ra, 0($sp) #guardar o registo ra na memoria stack
	sw $a0, 4($sp) #guardar o argumeto a0 na memoria stack
	
	#inicializacao de registos
	li $t0, 0
	li $t1, 0
	li $t2, 0
	li $t3, 0
	
	#NOTA: em principio isto ja n faz falta porque ja foi detectado 
	#anteriormente mas pelo sim e pelo nao fica aqui
	jal EDigitoVF 
	nop
	
	#é virgula flutuante?
	bne $v0, $zero, ProcessarVF_VirgulaDetectada
	nop
	
	li $v0, -1 #erro
	j ProcessarVF_Return
	nop
	
ProcessarVF_VirgulaDetectada:

	lw $t0, 4($sp) #obter o valor original do argumento a0
	sb $zero, 0($a0) #por um zero em vez da virgula no argumento modificado
	addi $a0, $a0, 1 #incrementar o endereco a0 em 1	
	move $t1, $a0 #mover o argumento para o registo t1
	
	lw $t0, 4($sp)  #obter o valor original do argumento a0
	sw $t1, 8($sp) #guardar t1 na stack
	
	#t0, endereco para a parte inteira
	#t1, endereco para a parte decimal
	
	#parte inteira e um digito?
	move $a0, $t0
	jal EDigitoInteiro
	nop
	
	#repor valores e salvar resultado
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	sw $v0, 12($sp)
	
	#parte decimal e digito?
	move $a0, $t1
	jal EDigitoInteiro
	nop
	#repor valores
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	#comparar se ambas as partes decimal e inteira sao numeros inteiros
	and $t3, $t2, $v0 #ambos verdadeiros?
	
	#converter se forem digitos
	bne $t3, $zero, ProcessarVF_ConverterParaVF
	nop
	
	li $v0, -1 #erro
	
	j ProcessarVF_Return
	nop


ProcessarVF_ConverterParaVF:

	#obter o tamanho do argumento a0 modificado
	move $a0, $t0
	jal strlen
	nop
	
	#repor os valores
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	
	#coverter a parte inteira para inteiro
	move $a0, $t0
	move $a1, $v0
	jal ConverterParaInteiro
	nop
	
	#guardar o resultado da conversao e repor os valores anteriores
	sw $v0, 16($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	
	#obter o tamanho do argumento original
	move $a0, $t1
	jal strlen
	nop
	#salvar o resultado
	sw $v0, 12($sp)
	#repor os valores
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t3, 16($sp)
	
	#converter a parte decimal para inteiro
	move $a0, $t1
	move $a1, $v0
	jal ConverterParaInteiro
	nop
	
	#repor os valores
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	lw $t3, 16($sp)
	
	#converter para virgula flutuante
	move $a0, $t3
	move $a1, $v0
	move $a2, $t2
	jal ConverterParaVF
	nop
	
	#repor valores
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	lw $t3, 16($sp)
	
	#adicionar a stack
	movf.s $f12, $f0
	jal StackAdicionarVF
	nop
	#!!!retorna v0

ProcessarVF_Return:
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 20    #libertar espaco na stack 
	jr $ra               #return
	nop
	
###############################################################
# ConverterParaInteiro - converte uma string para numero inteiro		    
#
# Argumentos:: 
# a0 - String
# a1 - tamanho
#
# Retorna:
# v0 - valor convertido
###############################################################	
ConverterParaInteiro:
	#inicializacao
	li $t0, 0
	li $t1, 0
	li $t3, 0 #counter
	li $t4, 0
	li $t5, 0
	li $t6, 0
	
	add $a0, $a0, $a1 #incrementar o endereco do argumento a0 pelo tamanho
	subi $a0, $a0, 1 #subtrair 1 para corrigir o indice
	li $t5, 1 #multiplicador1
	li $t6, 10 #multiplicador2
	add $t3, $a1, $zero 

ConverterParaInteiro_loop:
	beq $t3, $zero, ConverterParaInteiro_Return #verificaçao do final da string parcial
	nop
	lb $t1, 0($a0) #load do primeiro byte da string parcial
	beq $t1, $zero, ConverterParaInteiro_Return #verificaçao do final da string parcial
	nop
	
	#converter para numero
	subi $t4, $t1, 0x30
	
	#multiplicar, lets get some magic here
	mul $t4, $t4, $t5 #multiplicar o numero pelo multiplicador1
	add $t0, $t0, $t4 #adicionar o resultado a t0
	mul $t5, $t5, $t6 #multiplicar o multiplicador1 pelo multiplicador2
	
	subi $a0, $a0, 1 #subtrair 1 ao endereco do argumento a0
	subi $t3, $t3, 1 #subtrair 1 a t3(counter)
	j ConverterParaInteiro_loop
	nop

ConverterParaInteiro_Return:
	move $v0, $t0 #mover t0 para v0 e retornar o resultado
	jr $ra
	nop

###############################################################
# ProcessarComando - processa o comando obtido seja ele 
#		     operando ou operador
#
# Argumentos:: 
# a0 - String
# a1 - tamanho
#
# Retorna:
# v0 - erro
###############################################################	
ProcessarComando:
	addi $sp, $sp, -12 #alocar espaco na memoria stack
	sw $ra, 0($sp) #guardar o registo ra na memoria stack
	sw $a0, 4($sp) #guardar o argumento a0 na memoria stack
	sw $a1, 8($sp) #guardar o argumento a1 na memoria stack
	
	#verificar se e digito inteiro
	jal EDigitoInteiro
	nop
	lw $a0, 4($sp) #repor a0
	lw $a1, 8($sp) #repor a1
	
	bne $v0, $zero, ProcessarComando_Inteiro #é digito inteiro?
	nop
	
	#verificar se e virgula flutuante
	jal EDigitoVF 
	nop
	
	lw $a0, 4($sp)  #repor a0
	lw $a1, 8($sp)  #repor a1
	
	bne $v0, $zero, ProcessarComando_VF #é virgula flutuante?
	nop
		
	#é operador
	jal ProcessarOperador
	nop
	j ProcessarComando_Return
	nop
ProcessarComando_VF:
	
	#veriricar se VF esta activado
	jal VFActivado
	nop
	
	lw $a0, 4($sp) #repor a0
	lw $a1, 8($sp) #repor a1
	
	beq $v0, $zero, ProcessarComando_SkipVF #virgula flutuante activado?
	nop
	#processar virgula flutuante
	jal ProcessarVF
	nop
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 12     #libertar espaco na stack 
	jr $ra               #return
	nop
	
ProcessarComando_Inteiro:

	#verificar se eve converter pa VF ou nao
	jal VFActivado
	nop
	
	lw $a0, 4($sp) #repor a9
	lw $a1, 8($sp) #repor a1
	
	bne $v0, $zero, ProcessarComando_IntToVF #VF activado? converter de int para float
	nop
	
	jal ProcessarOperando
	nop
	
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 12     #libertar espaco na stack 
	jr $ra               #return
	nop
ProcessarComando_IntToVF:

	lw $a0, 4($sp) #repor a0
	lw $a1, 8($sp) #repor a1
	
	jal ConverterParaInteiro
	nop
	
	move $a0, $v0 #mover o resultado da funcao para o argumento a0
	li $a1, 0 #parte decimal
	li $a2, 0 #tamanho da parte decimal
	jal ConverterParaVF
	nop
	
	
	movf.s $f12, $f0 #mover o resultado da conversao para f12
	jal StackAdicionarVF #adicionar o resultado a stack
	nop
	
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 12     #libertar espaco na stack 
	jr $ra               #return
	nop
	
ProcessarComando_SkipVF:
	la $a0, ERROR_FP_N_SUPORTADO
	jal MostrarErro
	nop
	li $v0, -1
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 12     #libertar espaco na stack 
	jr $ra               #return
	nop
		
ProcessarComando_Return:
	##!retorna v0
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 12    #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# strcmp - compara duas strings
#
# Argumentos:: 
# a0 - String
# a1 - String
#
# Retorna:
# v0 - resultado da comparacao
###############################################################	
strcmp:
	addi $sp, $sp, -16    #alocar memoria na stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	#inicializacao
	li $t0, 0 #argumento a0
	li $t1, 0 #argumento a1
	li $t2, 0 #temp
	li $t3, 0 #tamanho da stringB
	li $t4, 0 #tamanho da stringA

	sw $a0, 4($sp) #guarda o argumento $a0 na memoria stack
	sw $a1, 8($sp) #guarda o argumento $a1 na memoria stack
	
	move $t0, $a0 #move o argumento $a0 para $t0
	move $t1, $a1 #move o argumento $a1 para $t1
	
	#obter o tamanho da string B
	move $a0, $t1
	jal strlen
	nop
	
	sw $v0, 12($sp) #guarda o resultado do tamanho da stringB na memoria stack
	lw $a0, 4($sp) #restaura o registo $a0 que foi guardado na memoria stack
	lw $a1, 8($sp) #restaura o registo $a1 que foi guardado na memoria stack
	
	#obter tamamho da string A, usa $a0 que foi restaurado
	jal strlen
	nop
	
	lw $a0, 4($sp) #restaura o registo $a0 que foi guardado na memoria stack
	lw $a1, 8($sp) #restaura o registo $a1 que foi guardado na memoria stack
	lw $t3, 12($sp) #carrega o resultado do tamanho da stringB
	move $t4, $v0 #move o resultado do tamanho da stringA para $t4

strcmp_checksizes:
	beq $t3, $t4, strcmp_saoiguais #comparar se sao iguais
	nop
	addi $v0, $zero, -1 #nao sao iguais, retornar -1
	
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 16     #libertar espaco na memoria stack 
	jr $ra
	nop
	
strcmp_saoiguais:
	lb $t1, 0($a1) #carregar 1 byte do argumento a1 que contem a stringB
	lb $t2, 0($a0) #carregar 1 byte do argumento a1 que contem a stringA
	
	beq $t1, $zero, strcmp_return #fim da stringB, nao e necessario verificar a stringA, whatever
	nop
	
	beq $t1, $t2, strcmp_next #se forem ambos iguais, verificar seguinte byte
	nop
	bne $t1, $t2, strcmp_diff #sao diferentes
	nop

strcmp_diff:
	addi $v0, $zero, -1 #retorna -1
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 16     #libertar espaco na memoria stack 
	jr $ra
	nop
	
strcmp_next:
	addi $a1, $a1, 1 #incrementar o endereço da stringB
	addi $a0, $a0, 1 #incrementar o endereço da stringA
	j strcmp_saoiguais

strcmp_return:
	addi $v0, $zero, 0   #v0 = 0
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 16     #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperacaoDivisao - Faz a divisao no topo da stack,
#		    requer 2 valores
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	
OperacaoDivisao:
	addi $sp, $sp, -8   #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 2  # $t2 < 2? operacao requer pelo menos 2 operandos na stack
	la $t1, POSICAO_STACK #carregar o endereco da label POSICAO_STACK em t1
	lw $t1, 0($t1) #carregar o valor que esta no endereco em t1
	blt $t1, $t0, OperacaoDivisao_WrongSize # se $t1 < $t0, erro
	nop
	
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor de 32 bits que esta na posicao 0 da stack
	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t1, 0($t1) #carregar o valor de 32 bits que contem a posicao actual da stack
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[size + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$size - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	
	sw $t0, 4($sp) #guardar o endereco da stack na memoria stack para posterior uso
	jal VFActivado #verifica se virgula flutuante esta activado
	nop
	lw $t0, 4($sp) #carrega o endereco da stack que esta na memoria stack
	
	bne $v0, $zero, OperacaoDivisao_DividirVF #salta para o label se virgula flutuante estiver activado
	nop
	
	#divisao inteira caso virgula flutuante estiver desactivado
	lw $t1, 0($t0) #carregar valor do topo da stack
	
	sw $zero, 0($t0) #limpar o topo da stack
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lw $t2, 0($t0) #carregar o penultimo valor
	
	div $t3, $t2, $t1 #operacao dividir ambos os valores
	
	sw $t3, 0($t0) #guardar o valor no topo da stack
	j OperacaoDivisao_Return
	nop
	
OperacaoDivisao_DividirVF:
	lwc1 $f4, 0($t0) #carregar o valor do topo da stack
	sw $zero, 0($t0) #limpar o topo da stack(por um zero)
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lwc1 $f6, 0($t0) #carregar o penultimo valor

 	div.s $f8, $f6, $f4 #operacao divisao virgula flutuante
 	
 	swc1 $f8, 0($t0) #guardar o valor no topo da stack
	
	
	j OperacaoDivisao_Return
	nop

OperacaoDivisao_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1           # atribuir -1 a v0 que equivale a erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na memoria stack
	addi $sp, $sp, 8     #libertar espaco na memoria stack 
	jr $ra               #return
	nop

OperacaoDivisao_Return:

	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t4, 0($t1) #carregar o valor de 32 bits que esta na posicao 0 da stack
	
	subi $t4, $t4, 1 #decrementar a posicao da stack em 1
	sw $t4, 0($t1) #guardar o valor da nova posicao da stack
	
	li $v0, 0            #atribuir 0 a v9 que equivale a sem erros
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8    #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperacaoSoma - Faz a soma no topo da stack,
#		 requer 2 valores
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	
OperacaoSoma:
	addi $sp, $sp, -8   #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 2  # $t2 < 2? operacao requer pelo menos 2 operandos na stack
	la $t1, POSICAO_STACK #carregar o endereco da label POSICAO_STACK em t1
	lw $t1, 0($t1) #carregar o valor que esta no endereco em t1
	blt $t1, $t0, OperacaoSoma_WrongSize # se $t1 < $t0, erro
	nop
	
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor de 32 bits que esta na posicao 0 da stack
	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t1, 0($t1) #carregar o valor de 32 bits que contem a posicao actual da stack
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[size + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$size - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	
	sw $t0, 4($sp) #guardar o endereco da stack na memoria stack para posterior uso
	jal VFActivado #verifica se virgula flutuante esta activado
	nop
	lw $t0, 4($sp) #carrega o endereco da stack que esta na memoria stack
	
	bne $v0, $zero, OperacaoSoma_DividirVF #salta para o label se virgula flutuante estiver activado
	nop
	
	#divisao inteira caso virgula flutuante estiver desactivado
	lw $t1, 0($t0) #carregar valor do topo da stack
	
	sw $zero, 0($t0) #limpar o topo da stack
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lw $t2, 0($t0) #carregar o penultimo valor
	
	add $t3, $t2, $t1 #operacao somar ambos os valores
	
	sw $t3, 0($t0) #guardar o valor no topo da stack
	j OperacaoSoma_Return
	nop
	
OperacaoSoma_DividirVF:
	lwc1 $f4, 0($t0) #carregar o valor do topo da stack
	sw $zero, 0($t0) #limpar o topo da stack(por um zero)
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lwc1 $f6, 0($t0) #carregar o penultimo valor

 	add.s $f8, $f6, $f4 #operacao somar virgula flutuante
 	
 	swc1 $f8, 0($t0) #guardar o valor no topo da stack
	
	
	j OperacaoSoma_Return
	nop

OperacaoSoma_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1           # atribuir -1 a v0 que equivale a erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na memoria stack
	addi $sp, $sp, 8     #libertar espaco na memoria stack 
	jr $ra               #return
	nop

OperacaoSoma_Return:

	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t4, 0($t1) #carregar o valor de 32 bits que esta na posicao 0 da stack
	
	subi $t4, $t4, 1 #decrementar a posicao da stack em 1
	sw $t4, 0($t1) #guardar o valor da nova posicao da stack
	
	li $v0, 0            #atribuir 0 a v9 que equivale a sem erros
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8    #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperacaoSubtracao - Faz a subtracao no topo da stack,
#		      requer 2 valores
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	

OperacaoSubtracao:
	addi $sp, $sp, -8   #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 2  # $t2 < 2? operacao requer pelo menos 2 operandos na stack
	la $t1, POSICAO_STACK #carregar o endereco da label POSICAO_STACK em t1
	lw $t1, 0($t1) #carregar o valor que esta no endereco em t1
	blt $t1, $t0, OperacaoSubtracao_WrongSize # se $t1 < $t0, erro
	nop
	
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor de 32 bits que esta na posicao 0 da stack
	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t1, 0($t1) #carregar o valor de 32 bits que contem a posicao actual da stack
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[size + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$size - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	
	sw $t0, 4($sp) #guardar o endereco da stack na memoria stack para posterior uso
	jal VFActivado #verifica se virgula flutuante esta activado
	nop
	lw $t0, 4($sp) #carrega o endereco da stack que esta na memoria stack
	
	bne $v0, $zero, OperacaoSubtracao_DividirVF #salta para o label se virgula flutuante estiver activado
	nop
	
	#divisao inteira caso virgula flutuante estiver desactivado
	lw $t1, 0($t0) #carregar valor do topo da stack
	
	sw $zero, 0($t0) #limpar o topo da stack
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lw $t2, 0($t0) #carregar o penultimo valor
	
	sub $t3, $t2, $t1 #operacao subtrair ambos os valores
	
	sw $t3, 0($t0) #guardar o valor no topo da stack
	j OperacaoSubtracao_Return
	nop
	
OperacaoSubtracao_DividirVF:
	lwc1 $f4, 0($t0) #carregar o valor do topo da stack
	sw $zero, 0($t0) #limpar o topo da stack(por um zero)
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lwc1 $f6, 0($t0) #carregar o penultimo valor

 	sub.s $f8, $f6, $f4 #operacao subtrair virgula flutuante
 	
 	swc1 $f8, 0($t0) #guardar o valor no topo da stack
	
	
	j OperacaoSubtracao_Return
	nop

OperacaoSubtracao_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1           # atribuir -1 a v0 que equivale a erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na memoria stack
	addi $sp, $sp, 8     #libertar espaco na memoria stack 
	jr $ra               #return
	nop

OperacaoSubtracao_Return:

	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t4, 0($t1) #carregar o valor de 32 bits que esta na posicao 0 da stack
	
	subi $t4, $t4, 1 #decrementar a posicao da stack em 1
	sw $t4, 0($t1) #guardar o valor da nova posicao da stack
	
	li $v0, 0            #atribuir 0 a v9 que equivale a sem erros
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8    #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperacaoMultiplicacao - Faz a multiplicacao no topo da stack, 
#			  requer 2 valores
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	

OperacaoMultiplicacao:
	addi $sp, $sp, -8   #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 2  # $t2 < 2? operacao requer pelo menos 2 operandos na stack
	la $t1, POSICAO_STACK #carregar o endereco da label POSICAO_STACK em t1
	lw $t1, 0($t1) #carregar o valor que esta no endereco em t1
	blt $t1, $t0, OperacaoMultiplicacao_WrongSize # se $t1 < $t0, erro
	nop
	
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor de 32 bits que esta na posicao 0 da stack
	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t1, 0($t1) #carregar o valor de 32 bits que contem a posicao actual da stack
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[size + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$size - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	
	sw $t0, 4($sp) #guardar o endereco da stack na memoria stack para posterior uso
	jal VFActivado #verifica se virgula flutuante esta activado
	nop
	lw $t0, 4($sp) #carrega o endereco da stack que esta na memoria stack
	
	bne $v0, $zero, OperacaoMultiplicacao_DividirVF #salta para o label se virgula flutuante estiver activado
	nop
	
	#divisao inteira caso virgula flutuante estiver desactivado
	lw $t1, 0($t0) #carregar valor do topo da stack
	
	sw $zero, 0($t0) #limpar o topo da stack
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lw $t2, 0($t0) #carregar o penultimo valor
	
	mul $t3, $t2, $t1 #operacao multiplicar ambos os valores
	
	sw $t3, 0($t0) #guardar o valor no topo da stack
	j OperacaoMultiplicacao_Return
	nop
	
OperacaoMultiplicacao_DividirVF:
	lwc1 $f4, 0($t0) #carregar o valor do topo da stack
	sw $zero, 0($t0) #limpar o topo da stack(por um zero)
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lwc1 $f6, 0($t0) #carregar o penultimo valor

 	mul.s $f8, $f6, $f4 #operacao multiplicar virgula flutuante
 	
 	swc1 $f8, 0($t0) #guardar o valor no topo da stack
	
	
	j OperacaoMultiplicacao_Return
	nop

OperacaoMultiplicacao_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1           # atribuir -1 a v0 que equivale a erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na memoria stack
	addi $sp, $sp, 8     #libertar espaco na memoria stack 
	jr $ra               #return
	nop

OperacaoMultiplicacao_Return:

	la $t1, POSICAO_STACK #carregar o endereco que contem a posicao actual da stack
	lw $t4, 0($t1) #carregar o valor de 32 bits que esta na posicao 0 da stack
	
	subi $t4, $t4, 1 #decrementar a posicao da stack em 1
	sw $t4, 0($t1) #guardar o valor da nova posicao da stack
	
	li $v0, 0            #atribuir 0 a v9 que equivale a sem erros
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8    #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperacaoClear - Faz o clear da stack
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################		
OperacaoClear:
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor que esta na label ENDERECO_STACK
	la $t1, POSICAO_STACK #carregar o endereco que esta na label POSICAO_STACK

	la $t2, TAMANHO_STACK #carregar o endereco que esta na label TAMANHO_STACK
	lw $t3, 0($t2) #carregar o valor que esta em TAMANHO_STACK
	
	sub $t3, $t3, 1 #n - 1 #ajustar o indice da stack
OperacaoClear_Loop:
	beq $t3, $zero, OperacaoClear_Return
	nop
	sw $zero, 0($t0) #por a zero
	
	addi $t0, $t0, 4 #incrementar o endereco da stack
	sub $t3, $t3, 1 #subtrair 1 valor do tamanho da stack
	j OperacaoClear_Loop
	nop

OperacaoClear_Return:
	li $v0, 0 #sem erro
	sw $zero, 0($t1) #reset da posicao da stack
	jr $ra               #return
	nop

###############################################################
# OperadorDup - Duplicar ultimo valor na stack 
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	
OperadorDup:
	addi $sp, $sp, -4    #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 1  # $t2 < 1? operacao requer pelo menos 1 operando na stack
	la $t1, POSICAO_STACK #carregar o endereco que esta na label POSICAO_STACK
	lw $t1, 0($t1) #carregar o valor que esta na label POSICAO_STACK
	blt $t1, $t0, OperadorDup_WrongSize
	nop
	
	#carregamento do endereço da stack s0 para a variavel t0
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor que esta na label ENDERECO_STACK
	la $t1, POSICAO_STACK #carregar o endereco que esta na label POSICAO_STACK
	lw $t1, 0($t1) #carregar o valor que esta na label POSICAO_STACK
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[$s1 + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$s2 - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	lw $t1, 0($t0) #carregar valor do topo da stack
	sw $t1, 4($t0) #duplicar o valor
	
	la $t1, POSICAO_STACK #carregar o endereco que esta na label POSICAO_STACK
	lw $t4, 0($t1)  #carregar o valor que esta na label POSICAO_STACK
	
	addi $t4, $t4, 1 #incrementar a posicao da stack em 1
	sw $t4, 0($t1) #guardar a nova posicao
	
	j OperadorDup_Return
	nop

OperadorDup_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1 #erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop

OperadorDup_Return:
	li $v0, 0 #sem erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop
	
###############################################################
# OperadorNeg - Transformar valor no seu simétrico
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	
OperadorNeg:
	addi $sp, $sp, -8    #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 1  # $t2 < 1? operacao requer pelo menos 1 operando na stack
	la $t1, POSICAO_STACK
	lw $t1, 0($t1)
	blt $t1, $t0, OperadorNeg_WrongSize
	nop
	
	#carregamento do endereço da stack s0 para a variavel t0
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor que se encontra na label ENDERECO_STACK
	la $t1, POSICAO_STACK #carregar o endereço da label POSICAO_STACK
	lw $t1, 0($t1) #carregar o valor que se encontra na label POSICAO_STACK
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[$s1 + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$s2 - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	sw $t0, 4($sp) #guardar t0 na stack para posterior uso
	jal VFActivado #verifica se virgula flutuante esta activado
	nop
	lw $t0, 4($sp) #repor o registo t0
	
	bne $v0, $zero, OperadorNeg_NegVF #salta para o label se virgula flutuante estiver activado
	nop
	
	lw $t1, 0($t0) #carregar valor do topo da stack
	mul $t1, $t1, -1 #operacao NEG (transformar valor no seu simétrico)

	sw $t1, 0($t0) #duplicar o valor
	
	j OperadorNeg_Return
	nop
OperadorNeg_NegVF:
	lwc1 $f4, 0($t0) #carregar o valor do topo da stack no coprocessor1
	
	li $t1, -1 #iniciar t1 a -1
	mtc1 $t1, $f8 #mover t1 para o coprocessor1
	cvt.s.w $f8, $f8 #converter para float
	mul.s $f6, $f4,$f8 #multiplicar por -1 em float
	swc1 $f6, 0($t0) #guardar o resultado
	j OperadorNeg_Return
	nop

OperadorNeg_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1 #erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8     #libertar espaco na stack 
	jr $ra               #return
	nop

OperadorNeg_Return:
	li $v0, 0 #sem erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 8     #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperadorDel - Apaga o último valor da stack
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	
OperadorDel:
	addi $sp, $sp, -4    #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 1  # $t2 < 1? operacao requer pelo menos 1 operando na stack
	la $t1, POSICAO_STACK  #carregar o endereço da label POSICAO_STACK
	lw $t1, 0($t1) #carregar o valor que se encontra na label POSICAO_STACK
	blt $t1, $t0, OperadorDel_WrongSize  # t1 < t0 ?
	nop
	
	#carregamento do endereço da stack s0 para a variavel t0
	la $t0, ENDERECO_STACK  #carregar o endereço de memoria da stack
	lw $t0, 0($t0) #carregar o valor que se encontra na label ENDERECO_STACK
	la $t1, POSICAO_STACK  #carregar o endereço da label POSICAO_STACK
	lw $t1, 0($t1)  #carregar o valor que se encontra na label POSICAO_STACK
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[$s1 + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[$s2 - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	lw $t1, 0($t0) #carregar valor do topo da stack
	
	sw $zero, 0($t0) #limpar o topo da stack (operacao del)
	
	la $t1, POSICAO_STACK #carregar o endereço da label POSICAO_STACK
	lw $t4, 0($t1) #carregar o valor que se encontra na label POSICAO_STACK
	
	subi $t4, $t4, 1 #decrementar a posicao da stack em 1
	sw $t4, 0($t1) #guardar a nova posicao da stack
	
	j OperadorDel_Return
	nop

OperadorDel_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	li $v0, -1 #erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop

OperadorDel_Return:
	li $v0, 0 #sem erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop

###############################################################
# OperadorSwap - Faz a troca dos valores no topo da stack
#
# Argumentos:: 
# None
#
# Retorna:
# v0 - erro
###############################################################	
OperadorSwap:
	addi $sp, $sp, -4    #alocar espaco na memoria stack
	sw $ra, 0($sp)       #guardar o endereco do ra na stack
	
	#verificacao de erros
	addi $t0, $zero, 2  # $t2 < 2? operacao requer pelo menos 2 operandos na stack
	la $t1, POSICAO_STACK #carregar o endereço da label POSICAO_STACK
	lw $t1, 0($t1) #carregar o valor que se encontra na label POSICAO_STACK
	blt $t1, $t0, OperadorSwap_WrongSize # t1 < t0 ?
	nop
	
	#carregamento do endereço da stack s0 para a variavel t0
	la $t0, ENDERECO_STACK  #carregar o endereço da label ENDERECO_STACK
	lw $t0, 0($t0) #carregar o valor que se encontra na label ENDERECO_STACK
	
	la $t1, POSICAO_STACK #carregar o endereço da label POSICAO_STACK
	lw $t1, 0($t1) #carregar o valor que se encontra na label POSICAO_STACK
	
	mul $t1, $t1, 4  #multiplicar a posicao por 4 (alinhamento de endereço) para obter o indice do topo da stack MEM[indice + 1]
	subi $t1, $t1, 4 #subtrair 4 ao indice para correcao da posicao na stack MEM[indice - 1]
	add $t0, $t0, $t1 #adicionar a t0 o valor de t1 para obter o topo da stack
	
	lw $t1, 0($t0) #carregar valor do topo da stack
	sw $zero, 0($t0) #limpar o topo da stack
	
	#obter o valor no topo da stack - 1
	subi $t0, $t0, 4 #subtrair a $t0 4 bytes para obter o penultimo valor da stack
	lw $t2, 0($t0) #carregar o penultimo valor
	
	#operacao swap dos valores no topo da stack
	sw $t1, 0($t0) 
	sw $t2, 4($t0)

	j OperadorSwap_Return
	nop
				
OperadorSwap_WrongSize:
	la $a0, ERROR_TAMANHO_INSUFICIENTE #carregar o endereço do erro
	jal MostrarErro
	nop
	
	li $v0, -1 #erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop	

OperadorSwap_Return:
	li $v0, 0 #sem erro
	lw $ra, 0($sp)       #carregar o endereco de retorno no registo $ra que esta na stack
	addi $sp, $sp, 4     #libertar espaco na stack 
	jr $ra               #return
	nop				

###############################################################
# strlen - calcula o tamamnho de uma string
#
# Argumentos:: 
# a0 - string
#
# Retorna:
# v0 - tamanho
###############################################################						
strlen:
	li $v0, 0                      #iniciar v0 a zero

	addi $sp, $sp, -4              # alocar espaco na memoria stack
	sw $ra, 0($sp) 		       #guardar o registo $ra na memoria stack

	lb $t0, 0($a0)                 #carregar um byte do argumento $a0
	beq $t0, $zero, strlen_return  #retornar se t0 = 0
	nop

	addi $a0, $a0, 1               #incrementar o endereco do argumento a0

	jal strlen                     #chamada de função
	nop
	addi $v0, $v0, 1               #incrementar v0 em 1

strlen_return:
	lw $ra, 0($sp)                 #repor o registo $ra
	addi $sp, $sp, 4               #desalocar o espaco da memoria stack
	
	jr $ra                         #jump and link
	nop
								
