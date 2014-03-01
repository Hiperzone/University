/*Goal principal que dado um ficheiro de texto, carrega o seu conteúdo,
gerando de seguida um tabuleiro com as condições predefinidas no
ficheiro. É gerado uma lista com a sequência de números válida que podem
ser inseridos no tabuleiro. As soluções são de seguida geradas se
existirem.
*/
go(File):-
	carregarDados(File,DadosFicheiro),
	gerarTabela(DadosFicheiro, Resultado),
	DadosFicheiro = [Dimemsion|_],
	generateSeq(Sequencia,Dimemsion,0),!,
	gerarSolucao(Resultado, Sequencia,Dimemsion).

/* Este goal gera uma sequência numérica de 1 a N inclusive, sendo o resultado 
uma lista de valores decrescente. Como caso base o termo Pos (posição) sera
 igual a dimensão da matriz, e a lista estará vazia.*/
generateSeq([],Size,Size):-!.

generateSeq([Valor|Tail],Size,Pos):-
	Pos < Size,
	NPos is Pos+1,
	Valor = NPos,
	generateSeq(Tail,Size,NPos).

/*Retorna o resultado do carregamento dos dados de um ficheiro ao permitir 
a abertura desse ficheiro sendo de seguida chamado o goal lerFicheiro que 
é responsável pelo processamento do mesmo.
*/
carregarDados(File,DadosFicheiro):-
	open(File,read,Stream),
	lerFicheiro(Stream, DadosFicheiro).

/*Goal que retorna o resultado de ler todas as letras de um ficheiro como
 uma lista de caracteres.*/
lerFicheiro(Stream, Dados):-
	lerLetra(Stream, Dados, _).

/*Goal que lê todos os caracteres válidos de um ficheiro. É lido um caracter 
a cada chamada recursiva e este é adicionado ao cabeçalho da lista, sendo 
que a leitura para quando o valor do último caracter é -1. É retornada uma 
lista com todos os caracteres lidos como resultado, sendo que a cada caracter
 válido, é subtraído por – 48 para converter para inteiro.*/
lerLetra(Stream, [NewCharConverted|DadosTail], _):-
	get_code(Stream, NewChar),
	NewCharConverted is NewChar - 48,
	verifyChar(NewChar, DadosTail),
	lerLetra(Stream, DadosTail, NewChar).

lerLetra(Stream, Dados, LastChar):-
	LastChar \== -1,
	lerLetra(Stream, Dados, -1).
lerLetra(_,[],_).

/*Goal que permite verificar se um caracter é válido. Os predicados com um cut 
e um fail são os predicados que permitem indicar que aquele caracter é 
inválido, fazendo com que o predicado retorne como fail em vez de retornar
 como true. O cut aqui é necessário para evitar que se verifique os predicados
 alternativos. Só o ultimo predicado deve retornar true para validar os
 restantes caracteres.*/
verifyChar(10,_):-!,fail.
verifyChar(32,_):-!,fail.
verifyChar(-1,_):-!,fail.
verifyChar(end_of_file,[]):-!,fail.
verifyChar(_, _):-!.

/*Insere os valores fixos nas posições adequadas. A cada chamada recursiva é
 instanciada três variáveis a partir da lista que contem os dados (X,Y,V) e é
 usado o goal nth0 do prolog para verificar se existe uma estrutura válida 
 dentro do tabuleiro ao qual é possível atribuir o valor V à variável 
 correspondente ao último termo dessa estrutura. O X e Y determinam as 
 coordenadas do tabuleiro que tem que ser idênticas aos valores do primeiro
 e segundo termo dessa estrutura. É retornado o resultado assim que não houver
 mais elementos na lista de dados do ficheiro.*/
inserirValoresFixosDoFicheiro([],_).
inserirValoresFixosDoFicheiro([X,Y,V|Dados], Tabuleiro):-
	nth0(_, Tabuleiro, c(X,Y,_,V)),
	inserirValoresFixosDoFicheiro(Dados, Tabuleiro).

/*Insere os valores das streams que foram lidas do ficheiro no tabuleiro. O goal
 irá processar cada stream da lista de dados do ficheiro para um certo tamanho 
 máximo que neste caso é n*n sendo n o tamanho do tabuleiro. A cada chamada
 recursiva o cabeçalho da lista do tabuleiro é unificado com a estrutura acima
 referida, mas que neste caso o termo correspondente à stream é instanciado 
 baseado no valor da stream que está no cabeçalho da lista de dados do 
 ficheiro. O processo é concluído quando o tamanho chega a zero.*/
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
a essa linha. A cada chamada recursiva é gerado a próxima linha, parando quando
 o número de linhas chegar a zero, retornando o resultado.*/
gerarTabela_(0, _, L, L).
gerarTabela_(Linhas, Colunas, RTemp, Resultado):-
	Linhas > 0,
	LinhaSeguinte is Linhas - 1,
	gerarColunas(Linhas, Colunas, [], ResultadoA),
	append(ResultadoA, RTemp, ResultadoB),
	gerarTabela_(LinhaSeguinte, Colunas, ResultadoB, Resultado).

/*Goal que gera N colunas por cada linha do jogo. É aqui que é utilizada pela 
primeira vez a estrutura acima referida onde é usado o goal functor do prolog 
para gerar um functor c com quatro argumentos. Depois do functor gerado, é 
adicionado a posição referente à linha e à coluna no mesmo, sendo que a stream
 e o valor são variáveis para serem unificadas posteriormente. A chamada 
 recursiva acaba quando não existirem mais colunas para gerar, isto é, quando
 a variável Coluna chega a zero. */
gerarColunas(_, 0, L, L).
gerarColunas(Linha, Coluna, RTemp, Resultado):-
	Coluna > 0,
	functor(LC, c, 4),
	ColunaSeguinte is Coluna - 1,
	LC = c(Linha, Coluna, _, _),
	append([LC], RTemp, ResultadoB),
	gerarColunas(Linha, ColunaSeguinte, ResultadoB, Resultado).

/*Goal principal que gera as soluções ao gerar permutações da primeira linha do
 jogo e chama a clausula gerarSolucao_/5 para gerar as restantes permutações e
 linhas.*/
gerarSolucao(Tabela,ListaSeq,Size):-
	permutation(ListaSeq,Permutado),
	gerarSolucao_(Tabela,ListaSeq,Permutado,Size,Size).

/*Gera as permutações das linhas restantes, e contem o caso base. O caso base
 não gera permutações mas é responsável por colocar os valores na estrutura 
 respeitando os valores dados no ficheiro, falhando se não for possível. Esta
 cláusula também chama as cláusulas de verificação. Se esta cláusula for bem 
 sucedida, implica que a solução seja válida. */
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

/*Esta função faz uma verificação rápida, para as permutações falharem o mais
 cedo possível.
Funciona verificando se existem pares de valores iguais entre a permutação 
atual e a permutação anterior. Caso existam, o goal falha e invalida a 
permutação atual.
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


/*Este goal é responsável por inserir os valores das permutações (lista) numa
 estrutura de dados que contem a posição e a stream de um valor. Se este valor
 já estiver definido, verifica se é igual à permutação. É saltado caso 
 contrário o goal falhe. estrutura resultante vai ser igual à original em 
 termos de posições e streams mas os valores estarão todos definidos. A 
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

/*Goal que verifica se o tabuleiro está em conformidade com as regras que são 
válidas para que uma solução seja válida. Para cada N, onde N é o tamanho do 
tabuleiro, é verificado se a linha, coluna e stream N são válidas. Retorna true
 se o tabuleiro é válido.*/
regraDiferentes(Lista, N):-
	N > 0,
	regraLinhaDiferente(N, Lista),
	regraColunaDiferente(N, Lista),
	regraStreamDiferente(N, Lista),
	N1 is N - 1,
	regraDiferentes(Lista, N1).

regraDiferentes(_, 0).

/*Goal que verifica se a linha contem números diferentes. A cada chamada 
recursiva o valor que está à cabeça da lista é unificado com a estrutura 
acima referida, contendo a linha e o valor como variáveis (c(Linha, _, _, V)).
 Caso não exista no resto da lista a mesma estrutura com a mesma linha e valor,
 quer dizer que não existe números repetidos nessa linha.*/
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

/*Goal que verifica se a coluna contem números diferentes. A cada chamada 
recursiva o valor que está à cabeça da lista é unificado com a estrutura 
acima referida, contendo a coluna e o valor como variáveis 
(c(_, Coluna, _, V)). Caso não exista no resto da lista a mesma estrutura com a
mesma coluna e valor, quer dizer que não existe números repetidos nessa coluna.
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

/*Goal que verifica se a stream contem números diferentes. A cada chamada
recursiva o valor que está à cabeça da lista é unificado com a estrutura 
acima referida, contendo a stream e o valor como variáveis
(c(_, _, Stream, V)). Caso não exista no resto da lista a mesma estrutura
com a mesma stream e valor, quer dizer que não existe números repetidos 
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


/*Este goal é responsável por fazer o display do output da forma descrita no 
enunciado do trabalho. A cláusula showResult/3 é o ponto de entrada do goal,
recebendo como termos a lista de objetos a serem mostrados, e a dimensão do
tabuleiro (N e Dimensão). A cláusula de entrada chama a cláusula showResult/4
para fazer print de uma linha da matriz de valores. Como o goal recebe como 
termo uma lista, faz print dos N primeiros elementos da lista e devolve o resto
 da lista por processar. O goal é atingido quando o Número de colunas vistas
 for igual à dimensão da matriz. */
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