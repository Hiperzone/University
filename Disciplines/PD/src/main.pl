/*Goal principal que dado um ficheiro de texto, carrega o seu conte�do,
gerando de seguida um tabuleiro com as condi��es predefinidas no
ficheiro. � gerado uma lista com a sequ�ncia de n�meros v�lida que podem
ser inseridos no tabuleiro. As solu��es s�o de seguida geradas se
existirem.
*/
go(File):-
	carregarDados(File,DadosFicheiro),
	gerarTabela(DadosFicheiro, Resultado),
	DadosFicheiro = [Dimemsion|_],
	generateSeq(Sequencia,Dimemsion,0),!,
	gerarSolucao(Resultado, Sequencia,Dimemsion).

/* Este goal gera uma sequ�ncia num�rica de 1 a N inclusive, sendo o resultado 
uma lista de valores decrescente. Como caso base o termo Pos (posi��o) sera
 igual a dimens�o da matriz, e a lista estar� vazia.*/
generateSeq([],Size,Size):-!.

generateSeq([Valor|Tail],Size,Pos):-
	Pos < Size,
	NPos is Pos+1,
	Valor = NPos,
	generateSeq(Tail,Size,NPos).

/*Retorna o resultado do carregamento dos dados de um ficheiro ao permitir 
a abertura desse ficheiro sendo de seguida chamado o goal lerFicheiro que 
� respons�vel pelo processamento do mesmo.
*/
carregarDados(File,DadosFicheiro):-
	open(File,read,Stream),
	lerFicheiro(Stream, DadosFicheiro).

/*Goal que retorna o resultado de ler todas as letras de um ficheiro como
 uma lista de caracteres.*/
lerFicheiro(Stream, Dados):-
	lerLetra(Stream, Dados, _).

/*Goal que l� todos os caracteres v�lidos de um ficheiro. � lido um caracter 
a cada chamada recursiva e este � adicionado ao cabe�alho da lista, sendo 
que a leitura para quando o valor do �ltimo caracter � -1. � retornada uma 
lista com todos os caracteres lidos como resultado, sendo que a cada caracter
 v�lido, � subtra�do por � 48 para converter para inteiro.*/
lerLetra(Stream, [NewCharConverted|DadosTail], _):-
	get_code(Stream, NewChar),
	NewCharConverted is NewChar - 48,
	verifyChar(NewChar, DadosTail),
	lerLetra(Stream, DadosTail, NewChar).

lerLetra(Stream, Dados, LastChar):-
	LastChar \== -1,
	lerLetra(Stream, Dados, -1).
lerLetra(_,[],_).

/*Goal que permite verificar se um caracter � v�lido. Os predicados com um cut 
e um fail s�o os predicados que permitem indicar que aquele caracter � 
inv�lido, fazendo com que o predicado retorne como fail em vez de retornar
 como true. O cut aqui � necess�rio para evitar que se verifique os predicados
 alternativos. S� o ultimo predicado deve retornar true para validar os
 restantes caracteres.*/
verifyChar(10,_):-!,fail.
verifyChar(32,_):-!,fail.
verifyChar(-1,_):-!,fail.
verifyChar(end_of_file,[]):-!,fail.
verifyChar(_, _):-!.

/*Insere os valores fixos nas posi��es adequadas. A cada chamada recursiva �
 instanciada tr�s vari�veis a partir da lista que contem os dados (X,Y,V) e �
 usado o goal nth0 do prolog para verificar se existe uma estrutura v�lida 
 dentro do tabuleiro ao qual � poss�vel atribuir o valor V � vari�vel 
 correspondente ao �ltimo termo dessa estrutura. O X e Y determinam as 
 coordenadas do tabuleiro que tem que ser id�nticas aos valores do primeiro
 e segundo termo dessa estrutura. � retornado o resultado assim que n�o houver
 mais elementos na lista de dados do ficheiro.*/
inserirValoresFixosDoFicheiro([],_).
inserirValoresFixosDoFicheiro([X,Y,V|Dados], Tabuleiro):-
	nth0(_, Tabuleiro, c(X,Y,_,V)),
	inserirValoresFixosDoFicheiro(Dados, Tabuleiro).

/*Insere os valores das streams que foram lidas do ficheiro no tabuleiro. O goal
 ir� processar cada stream da lista de dados do ficheiro para um certo tamanho 
 m�ximo que neste caso � n*n sendo n o tamanho do tabuleiro. A cada chamada
 recursiva o cabe�alho da lista do tabuleiro � unificado com a estrutura acima
 referida, mas que neste caso o termo correspondente � stream � instanciado 
 baseado no valor da stream que est� no cabe�alho da lista de dados do 
 ficheiro. O processo � conclu�do quando o tamanho chega a zero.*/
inserirStreamsDoFicheiro(Dados,0, _, TabuleiroAux):-
	inserirValoresFixosDoFicheiro(Dados, TabuleiroAux).

inserirStreamsDoFicheiro([HDados|Dados], Tamanho, [HTabuleiro|Tabuleiro],
 TabuleiroCompleto):-
	Tamanho > 0,
	HTabuleiro = c(_,_,Z,_),
	Z = HDados,
	ProxTamanho is Tamanho - 1,
	inserirStreamsDoFicheiro(Dados, ProxTamanho, Tabuleiro,TabuleiroCompleto).

/*Goal que permite gerar o tabuleiro, retornando o resultado.*/
gerarTabela([Size|Dados], Resultado):-
	Dim is Size*Size,
	gerarTabela_(Size, Size, [], Resultado),
	inserirStreamsDoFicheiro(Dados, Dim, Resultado, Resultado).

/*Goal que gera para cada linha do tabuleiro, todas as colunas correspondentes 
a essa linha. A cada chamada recursiva � gerado a pr�xima linha, parando quando
 o n�mero de linhas chegar a zero, retornando o resultado.*/
gerarTabela_(0, _, L, L).
gerarTabela_(Linhas, Colunas, RTemp, Resultado):-
	Linhas > 0,
	LinhaSeguinte is Linhas - 1,
	gerarColunas(Linhas, Colunas, [], ResultadoA),
	append(ResultadoA, RTemp, ResultadoB),
	gerarTabela_(LinhaSeguinte, Colunas, ResultadoB, Resultado).

/*Goal que gera N colunas por cada linha do jogo. � aqui que � utilizada pela 
primeira vez a estrutura acima referida onde � usado o goal functor do prolog 
para gerar um functor c com quatro argumentos. Depois do functor gerado, � 
adicionado a posi��o referente � linha e � coluna no mesmo, sendo que a stream
 e o valor s�o vari�veis para serem unificadas posteriormente. A chamada 
 recursiva acaba quando n�o existirem mais colunas para gerar, isto �, quando
 a vari�vel Coluna chega a zero. */
gerarColunas(_, 0, L, L).
gerarColunas(Linha, Coluna, RTemp, Resultado):-
	Coluna > 0,
	functor(LC, c, 4),
	ColunaSeguinte is Coluna - 1,
	LC = c(Linha, Coluna, _, _),
	append([LC], RTemp, ResultadoB),
	gerarColunas(Linha, ColunaSeguinte, ResultadoB, Resultado).

/*Goal principal que gera as solu��es ao gerar permuta��es da primeira linha do
 jogo e chama a clausula gerarSolucao_/5 para gerar as restantes permuta��es e
 linhas.*/
gerarSolucao(Tabela,ListaSeq,Size):-
	permutation(ListaSeq,Permutado),
	gerarSolucao_(Tabela,ListaSeq,Permutado,Size,Size).

/*Gera as permuta��es das linhas restantes, e contem o caso base. O caso base
 n�o gera permuta��es mas � respons�vel por colocar os valores na estrutura 
 respeitando os valores dados no ficheiro, falhando se n�o for poss�vel. Esta
 cl�usula tamb�m chama as cl�usulas de verifica��o. Se esta cl�usula for bem 
 sucedida, implica que a solu��o seja v�lida. */
gerarSolucao_(Tabela,ListaSeq,Permutado,N,Size):-
	N>1,!,
	permutation(ListaSeq,PermutadoNovo),
	ProxN is N-1,
	append(Permutado,PermutadoNovo,NovaLista),
	verificarPermutacoes(NovaLista,Size,0),
	gerarSolucao_(Tabela,ListaSeq,NovaLista,ProxN,Size).

gerarSolucao_(Tabela,_,Permutado,1,Size):-
	atribuicao(Tabela,Permutado,NovaTabela),
	regraDiferentes(NovaTabela,Size),
	showResult(NovaTabela,Size,Size).

/*Esta fun��o faz uma verifica��o r�pida, para as permuta��es falharem o mais
 cedo poss�vel.
Funciona verificando se existem pares de valores iguais entre a permuta��o 
atual e a permuta��o anterior. Caso existam, o goal falha e invalida a 
permuta��o atual.
*/
verificarPermutacoes(_,Size,Size):-!.

verificarPermutacoes(Lista,Size,N):-
	N<Size,
	length(Lista,SizeList),
	Valor is SizeList-N,
	ValorAnterior is (SizeList-N)-Size,
	nth1(Valor,Lista,A),
	nth1(ValorAnterior,Lista,B),
	A=\=B,
	Next is N+1,
	verificarPermutacoes(Lista,Size,Next).


/*Este goal � respons�vel por inserir os valores das permuta��es (lista) numa
 estrutura de dados que contem a posi��o e a stream de um valor. Se este valor
 j� estiver definido, verifica se � igual � permuta��o. � saltado caso 
 contr�rio o goal falhe. estrutura resultante vai ser igual � original em 
 termos de posi��es e streams mas os valores estar�o todos definidos. A 
 estrutura original mantem-se, sendo esta gerada originalmente no goal 
 gerarTabela.
*/
atribuicao([],[],[]):-!.

atribuicao([Tabela|TTail],[Permutado|Tail],NovaTabela):-
	Tabela = c(A,B,C,D),
	var(D),!,
	atribuicao(TTail,Tail,NovaTabelaIntremedia),
	append([c(A,B,C,Permutado)],NovaTabelaIntremedia,NovaTabela).

atribuicao([Tabela|TTail],[Permutado|Tail],NovaTabela):-
	Tabela = c(A,B,C,D),
	atomic(D),
	D =:= Permutado,
	atribuicao(TTail,Tail,NovaTabelaIntremedia),
	append([c(A,B,C,D)],NovaTabelaIntremedia,NovaTabela).

/*Goal que verifica se o tabuleiro est� em conformidade com as regras que s�o 
v�lidas para que uma solu��o seja v�lida. Para cada N, onde N � o tamanho do 
tabuleiro, � verificado se a linha, coluna e stream N s�o v�lidas. Retorna true
 se o tabuleiro � v�lido.*/
regraDiferentes(Lista, N):-
	N > 0,
	regraLinhaDiferente(N, Lista),
	regraColunaDiferente(N, Lista),
	regraStreamDiferente(N, Lista),
	N1 is N - 1,
	regraDiferentes(Lista, N1).

regraDiferentes(_, 0).

/*Goal que verifica se a linha contem n�meros diferentes. A cada chamada 
recursiva o valor que est� � cabe�a da lista � unificado com a estrutura 
acima referida, contendo a linha e o valor como vari�veis (c(Linha, _, _, V)).
 Caso n�o exista no resto da lista a mesma estrutura com a mesma linha e valor,
 quer dizer que n�o existe n�meros repetidos nessa linha.*/
regraLinhaDiferente(Linha, [X | Lista]):-
	X = c(Linha, _, _, V),
	atomic(V),
	\+ member(c(Linha, _, _, V), Lista),
	regraLinhaDiferente(Linha, Lista).

regraLinhaDiferente(_, []).

regraLinhaDiferente(Linha, [X | Lista]):-
	X = c(X1, _, _, _),
	X1 \== Linha,
	regraLinhaDiferente(Linha, Lista).

/*Goal que verifica se a coluna contem n�meros diferentes. A cada chamada 
recursiva o valor que est� � cabe�a da lista � unificado com a estrutura 
acima referida, contendo a coluna e o valor como vari�veis 
(c(_, Coluna, _, V)). Caso n�o exista no resto da lista a mesma estrutura com a
mesma coluna e valor, quer dizer que n�o existe n�meros repetidos nessa coluna.
 */
regraColunaDiferente(Coluna, [X | Lista]):-
	X = c(_, Coluna, _, V),
	atomic(V),
	\+ member(c(_, Coluna, _, V), Lista),
	regraColunaDiferente(Coluna, Lista).

regraColunaDiferente(_, []).

regraColunaDiferente(Coluna, [X | Lista]):-
	X = c(_, X1, _, _),
	X1 \== Coluna,
	regraColunaDiferente(Coluna, Lista).

/*Goal que verifica se a stream contem n�meros diferentes. A cada chamada
recursiva o valor que est� � cabe�a da lista � unificado com a estrutura 
acima referida, contendo a stream e o valor como vari�veis
(c(_, _, Stream, V)). Caso n�o exista no resto da lista a mesma estrutura
com a mesma stream e valor, quer dizer que n�o existe n�meros repetidos 
nessa stream. */
regraStreamDiferente(Stream, [X | Lista]):-
	X = c(_, _, Stream, V),
	atomic(V),
	\+ member(c(_, _, Stream, V), Lista),
	regraStreamDiferente(Stream, Lista).

regraStreamDiferente(_, []).

regraStreamDiferente(Stream, [X | Lista]):-
	X = c(_, _, X1, _),
	X1 \== Stream,
	regraStreamDiferente(Stream, Lista).


/*Este goal � respons�vel por fazer o display do output da forma descrita no 
enunciado do trabalho. A cl�usula showResult/3 � o ponto de entrada do goal,
recebendo como termos a lista de objetos a serem mostrados, e a dimens�o do
tabuleiro (N e Dimens�o). A cl�usula de entrada chama a cl�usula showResult/4
para fazer print de uma linha da matriz de valores. Como o goal recebe como 
termo uma lista, faz print dos N primeiros elementos da lista e devolve o resto
 da lista por processar. O goal � atingido quando o N�mero de colunas vistas
 for igual � dimens�o da matriz. */
showResult(Rest,0,_,Rest):-!.

showResult([L|Tail],N,Dimensao,Rest):-
	N>0,
	NewN is N-1,
	L = c(_, _, _, Valor),
	write(Valor),
	write(' '),
	showResult(Tail,NewN,Dimensao,Rest).

showResult(L,N,Dimensao):-
	N>0,
	NewN is N-1,
	showResult(L,Dimensao,Dimensao,Rest),
	write('\n'),
	showResult(Rest,NewN,Dimensao).