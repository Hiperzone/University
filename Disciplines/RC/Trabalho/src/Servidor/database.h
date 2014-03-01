
#include "LinkedList.h"
#include <time.h>

#ifndef _DATABASE_H_
#define _DATABASE_H_

extern LinkedList *dbUtilizadores;
extern LinkedList *dbContas;
extern LinkedList *dbSessions;
extern LinkedList *dbMovimentos;
extern LinkedList *dbServicos;

typedef struct Utilizador
{
	int id;
	int flags;
	char username[32];
	char password[32];
	bool congelada;
}Utilizador;

typedef struct Session
{
	int token;
	time_t sessionExpire;
	Utilizador *cliente;
}Session;

typedef struct Conta
{
	int contaID;
	int clientID;
	int saldo;
}Conta;

typedef struct Movimento
{
	int id;
	int tipo;
	int owner;
	int contaO;
	int ContaD;
	int Montante;
}Movimento;

typedef struct Servico
{
	int id;
	char nome[256];
	int nib;
}Servico;

int genUtilizadorID();
int genAccountID();
int genToken();
int genMovimentoID();
int genServicoID();

void inicializarDatabase();
void destroyDatabase();
Utilizador *GetCliente(char *username);
void UtilizadorAdd(Utilizador *c);
void SessionAdd(Session *s);
void ContasAdd(Conta *c);
void MovimentoAdd(Movimento *m);
void ServicoAdd(Servico *s);

Session *GetSession(int token);
Conta *GetConta(int id);
Movimento *GetMovimento(int id);
Servico *GetServico(int id);

Utilizador *list_get_client(char *username, LinkedList *list);
Session *list_get_session(int token, LinkedList *list);
Conta *list_get_conta(int id, LinkedList *list);
Movimento *list_get_movimento(int id, LinkedList *list);
Servico *list_get_servico(int id, LinkedList *list);

void createNewDatabaseServicos();
void createNewDatabaseMovimentos();
void createNewDatabaseContas();
void createNewDatabaseUtilizadores();


void DBInsertUtilizador(Utilizador *utilizador);
void DBInsertConta(Conta *conta);
void DBInsertMovimento(Movimento *movimento);
void DBInsertServico(Servico *servico);


void DBLoadUtilizadores();
void DBWriteUtilizadores();

void DBLoadContas();
void DBWriteContas();

void DBLoadMovimentos();
void DBWriteMovimentos();

void DBLoadServicos();
void DBWriteServicos();

#endif
