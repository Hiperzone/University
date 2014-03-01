#include "../shared/common.h"
#include "database.h"
#include <stdio.h>
#include <string.h>
#include <malloc.h>
#include "LinkedList.h"
#include <assert.h>

LinkedList *dbUtilizadores;
LinkedList *dbContas;
LinkedList *dbSessions;
LinkedList *dbMovimentos;
LinkedList *dbServicos;

int UtilizadorIDGen;
int AccountIDGen;
int TokenIDGen;
int MovimentoIDGen;
int ServicoIDGen;

/************************************************************************/
/* Geradores de ids para a database                                     */
/************************************************************************/
int genMovimentoID()
{
	MovimentoIDGen += 1;
	return MovimentoIDGen;
}

int genServicoID()
{
	ServicoIDGen += 1;
	return ServicoIDGen;
}

int genToken()
{
	TokenIDGen += 1;
	return TokenIDGen;
}

int genUtilizadorID()
{
	UtilizadorIDGen += 1;
	return UtilizadorIDGen;
}

int genAccountID()
{
	AccountIDGen += 1;
	return AccountIDGen;
}

/************************************************************************/
/* Inicializa a database                                                */
/************************************************************************/
void inicializarDatabase()
{
	dbUtilizadores = list_new();
	dbContas = list_new();
	dbSessions = list_new();
	dbMovimentos = list_new();
	dbServicos = list_new();

	UtilizadorIDGen = 0;
	AccountIDGen = 0;
	TokenIDGen = 0;
	MovimentoIDGen = 0;
	ServicoIDGen = 0;

	DBLoadUtilizadores();
	DBLoadContas();
	DBLoadMovimentos();
	DBLoadServicos();
}

/************************************************************************/
/* Liberta os recursos ocupados pela database                           */
/************************************************************************/
void destroyDatabase()
{
	list_destroy(dbUtilizadores, true);
	list_destroy(dbContas, true);
	list_destroy(dbMovimentos, true);
	list_destroy(dbServicos, true);
	list_destroy(dbSessions, true);
}

/************************************************************************/
/* Adiciona uma conta a database                                        */
/************************************************************************/
void ContasAdd(Conta *c)
{
	list_insert(c, dbContas);
	DBInsertConta(c);
}

/************************************************************************/
/* Adiciona um utilizador a database                                    */
/************************************************************************/
void UtilizadorAdd(Utilizador *c)
{
	list_insert(c, dbUtilizadores);
	DBInsertUtilizador(c);
}

/************************************************************************/
/* Adiciona uma sessao a database                                       */
/************************************************************************/
void SessionAdd(Session *s)
{
	list_insert(s, dbSessions);
}

/************************************************************************/
/* Adiciona um movimento a database                                     */
/************************************************************************/
void MovimentoAdd(Movimento *m)
{
	list_insert(m, dbMovimentos);
	DBInsertMovimento(m);
}

/************************************************************************/
/* Adiciona um servico a database                                       */
/************************************************************************/
void ServicoAdd(Servico *s)
{
	list_insert(s, dbServicos);
	DBInsertServico(s);
}

/************************************************************************/
/* Obtencao dos dados da database em memoria                            */
/************************************************************************/
Utilizador *GetCliente(char *username)
{
	return list_get_client(username, dbUtilizadores);
}

Session *GetSession(int token)
{
	return list_get_session(token, dbSessions);
}

Conta *GetConta(int id)
{
	return list_get_conta(id, dbContas);
}

Movimento *GetMovimento(int id)
{
	return list_get_movimento(id, dbMovimentos);
}

Servico *GetServico(int id)
{
	return list_get_servico(id, dbServicos);
}


Conta *list_get_conta(int id, LinkedList *list)
{
	assert(list->head != NULL);
	LinkedListNode *current = list->head->next;

	while (current != NULL)
	{
		Conta * c = current->element;
		if (c->contaID == id)
		{
			return c;
		}
		current = current->next;
	}
	return NULL;
}

Utilizador *list_get_client(char *username, LinkedList *list)
{
	assert(list->head != NULL);
	LinkedListNode *current = list->head->next;

	while (current != NULL)
	{
		Utilizador * c = current->element;
		if (strcmp(username, c->username) == 0)
		{
			return c;
		}
		current = current->next;
	}
	return NULL;
}

Session *list_get_session(int token, LinkedList *list)
{
	assert(list->head != NULL);
	LinkedListNode *current = list->head->next;

	while (current != NULL)
	{
		Session * c = current->element;
		if (c->token == token)
		{
			return c;
		}
		current = current->next;
	}
	return NULL;
}

Movimento *list_get_movimento(int id, LinkedList *list)
{
	assert(list->head != NULL);
	LinkedListNode *current = list->head->next;

	while (current != NULL)
	{
		Movimento * c = current->element;
		if (c->id == id)
		{
			return c;
		}
		current = current->next;
	}
	return NULL;
}

Servico *list_get_servico(int id, LinkedList *list)
{
	assert(list->head != NULL);
	LinkedListNode *current = list->head->next;

	while (current != NULL)
	{
		Servico * c = current->element;
		if (c->id == id)
		{
			return c;
		}
		current = current->next;
	}
	return NULL;
}

//////////////////////////////////////ESCRITA/LEITURA NO DISCO///////////////////

/************************************************************************/
/* Insere um utilizador na database                                     */
/************************************************************************/
void DBInsertUtilizador(Utilizador *utilizador)
{
	FILE *fp = fopen("utilizadores.dat", "ab");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de utilizadores\n");
		return;
	}
	fwrite(utilizador, sizeof(Utilizador), 1, fp);
	fclose(fp);
}

/************************************************************************/
/* Carrega os utilizadores da database                                  */
/************************************************************************/
void DBLoadUtilizadores()
{
	FILE *fp = fopen("utilizadores.dat", "rb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de utilizadores\n");
		return;
	}
	fseek(fp, 0, SEEK_END);
	int fsize = ftell(fp);

	fseek(fp, 0, SEEK_SET);
	int fcurrsize = 0;

	while (fcurrsize != fsize)
	{
		Utilizador * utilizador = malloc(sizeof(Utilizador));
		fread(utilizador, sizeof(Utilizador), 1, fp);
		list_insert(utilizador, dbUtilizadores);

		if (utilizador->id > UtilizadorIDGen)
		{
			UtilizadorIDGen = utilizador->id;
		}

		fcurrsize = ftell(fp);
	}
}

/************************************************************************/
/* Escreve todos os utilizadores em memoria na database                 */
/************************************************************************/
void DBWriteUtilizadores()
{
	remove("utilizadores.bak");
	rename("utilizadores.dat", "utilizadores.bak");
	createNewDatabaseUtilizadores();

	FILE *fp = fopen("utilizadores.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de utilizadores\n");
		return;
	}
	fseek(fp, 0, SEEK_SET);
	for (LinkedListNode * p = Begin(dbUtilizadores); p != NULL; p = Next(p))
	{
		Utilizador * utilizador = p->element;
		fwrite(utilizador, sizeof(Utilizador), 1, fp);
	}
	fclose(fp);
}

/************************************************************************/
/* Insere uma conta na database                                         */
/************************************************************************/
void DBInsertConta(Conta *conta)
{
	FILE *fp = fopen("contas.dat", "ab");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de contas\n");
		return;
	}
	fwrite(conta, sizeof(Conta), 1, fp);
	fclose(fp);
}

/************************************************************************/
/* Carrega as contas da database                                        */
/************************************************************************/
void DBLoadContas()
{
	FILE *fp = fopen("contas.dat", "rb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de contas\n");
		return;
	}
	fseek(fp, 0, SEEK_END);
	int fsize = ftell(fp);

	fseek(fp, 0, SEEK_SET);
	int fcurrsize = 0;

	while (fcurrsize != fsize)
	{
		Conta * conta = malloc(sizeof(Conta));
		fread(conta, sizeof(Conta), 1, fp);
		list_insert(conta, dbContas);

		if (conta->contaID > AccountIDGen)
		{
			AccountIDGen = conta->contaID;
		}

		fcurrsize = ftell(fp);
	}
}

/************************************************************************/
/* Escreve todas as contas em memoria na database                       */
/************************************************************************/
void DBWriteContas()
{
	remove("contas.bak");
	rename("contas.dat", "contas.bak");
	createNewDatabaseContas();

	FILE *fp = fopen("contas.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de contas\n");
		return;
	}
	fseek(fp, 0, SEEK_SET);
	for (LinkedListNode * p = Begin(dbContas); p != NULL; p = Next(p))
	{
		Conta * conta = p->element;
		fwrite(conta, sizeof(Conta), 1, fp);
	}
	fclose(fp);
}

/************************************************************************/
/* Insere um movimento na database                                      */
/************************************************************************/
void DBInsertMovimento(Movimento *movimento)
{
	FILE *fp = fopen("movimentos.dat", "ab");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de movimentos\n");
		return;
	}
	fwrite(movimento, sizeof(Movimento), 1, fp);
	fclose(fp);
}

/************************************************************************/
/* Carrega os movimentos da database                                    */
/************************************************************************/
void DBLoadMovimentos()
{
	FILE *fp = fopen("movimentos.dat", "rb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de movimentos\n");
		return;
	}
	fseek(fp, 0, SEEK_END);
	int fsize = ftell(fp);

	fseek(fp, 0, SEEK_SET);
	int fcurrsize = 0;

	while (fcurrsize != fsize)
	{
		Movimento * movimento = malloc(sizeof(Movimento));
		fread(movimento, sizeof(Movimento), 1, fp);
		list_insert(movimento, dbMovimentos);

		if (movimento->id > MovimentoIDGen)
		{
			MovimentoIDGen = movimento->id;
		}

		fcurrsize = ftell(fp);
	}
}

/************************************************************************/
/* Escreve todos os movimentos em memoria na database                      */
/************************************************************************/
void DBWriteMovimentos()
{
	remove("movimentos.bak");
	rename("movimentos.dat", "movimentos.bak");
	createNewDatabaseMovimentos();

	FILE *fp = fopen("movimentos.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de movimentos\n");
		return;
	}
	fseek(fp, 0, SEEK_SET);
	for (LinkedListNode * p = Begin(dbMovimentos); p != NULL; p = Next(p))
	{
		Movimento * movimento = p->element;
		fwrite(movimento, sizeof(Movimento), 1, fp);
	}
	fclose(fp);
}


/************************************************************************/
/* Insere um servico na database                                        */
/************************************************************************/
void DBInsertServico(Servico *servico)
{
	FILE *fp = fopen("servicos.dat", "ab");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de servicos\n");
		return;
	}
	fwrite(servico, sizeof(Servico), 1, fp);
	fclose(fp);
}

/************************************************************************/
/* Carrega os servicos da database                                      */
/************************************************************************/
void DBLoadServicos()
{
	FILE *fp = fopen("servicos.dat", "rb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de servicos\n");
		return;
	}
	fseek(fp, 0, SEEK_END);
	int fsize = ftell(fp);

	fseek(fp, 0, SEEK_SET);
	int fcurrsize = 0;

	while (fcurrsize != fsize)
	{
		Servico * servico = malloc(sizeof(Servico));
		fread(servico, sizeof(Servico), 1, fp);
		list_insert(servico, dbServicos);

		if (servico->id > ServicoIDGen)
		{
			ServicoIDGen = servico->id;
		}

		fcurrsize = ftell(fp);
	}
}

/************************************************************************/
/* Escreve todos os servicos em memoria na database                      */
/************************************************************************/
void DBWriteServicos()
{
	remove("servicos.bak");
	rename("servicos.dat", "servicos.bak");
	createNewDatabaseMovimentos();

	FILE *fp = fopen("servicos.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de servicos\n");
		return;
	}
	fseek(fp, 0, SEEK_SET);
	for (LinkedListNode * p = Begin(dbServicos); p != NULL; p = Next(p))
	{
		Servico * servico = p->element;
		fwrite(servico, sizeof(Servico), 1, fp);
	}
	fclose(fp);
}

/************************************************************************/
/* Cria uma database vazia para os utilizadores                         */
/************************************************************************/
void createNewDatabaseUtilizadores()
{
	FILE *fp = fopen("utilizadores.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de utilizadores\n");
		return;
	}
	fclose(fp);
}
/************************************************************************/
/* Cria uma database vazia para as contas                               */
/************************************************************************/
void createNewDatabaseContas()
{
	FILE *fp = fopen("contas.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de contas\n");
		return;
	}
	fclose(fp);
}

/************************************************************************/
/* Cria uma database vazia para os movimentos                           */
/************************************************************************/
void createNewDatabaseMovimentos()
{
	FILE *fp = fopen("movimentos.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de movimentos\n");
		return;
	}
	fclose(fp);
}

/************************************************************************/
/* Cria uma database vazia para os servicos                               */
/************************************************************************/
void createNewDatabaseServicos()
{
	FILE *fp = fopen("servicos.dat", "wb");
	if (fp == NULL)
	{
		fprintf(stderr, "%s", "erro ao carregar a database de servicos\n");
		return;
	}
	fclose(fp);
}
