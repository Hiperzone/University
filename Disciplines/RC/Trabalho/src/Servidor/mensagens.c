#include "../shared/common.h"
#include "mensagens.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <malloc.h>
#include "servidor.h"
#include "database.h"
#include <time.h>
#include "LinkedList.h"

opcode_handler opcode_dispatch[35] = {
	{ MSG_NULL, &msg_no_action_handler },
	{ CMSG_LOGIN, &msg_login_handler },
	{ CMSG_REGISTO, &msg_registo_handler },
	{ CMSG_CRIAR_CONTA, &msg_criar_conta_handler },
	{ CMSG_LISTAR_CONTAS_PROPRIAS, &msg_listar_contas_proprias_handler },
	{ CMSG_CONSULTAR_SALDO, &msg_consultar_saldo_handler },
	{ CMSG_CONSULTAR_MOVIMENTOS, &msg_consultar_movimentos_handler },
	{ CMSG_CONSULTA_SALDO_INTEGRADA, &msg_consulta_saldo_integrada_handler },
	{ CMSG_MOVIMENTO_ENTRE_CONTAS, &msg_movimento_entre_contas_handler },
	{ CMSG_APAGAR_CONTA, &msg_apagar_conta_handler },
	{ CMSG_LISTAR_SERVICOS_PAGAMENTO, &msg_listar_servicos_handler },
	{ CMSG_EFECTUAR_PAGAMENTO_SERVICO, &msg_efectuar_pagamento_servico_handler },
	{ CMSG_CRIAR_NOVO_SERVICO, &msg_criar_novo_servico_handler },
	{ CMSG_LISTAR_CLIENTES, &msg_listar_clientes_handler },
	{ CMSG_LISTAR_ENTIDADES, &msg_listar_entidades_handler },
	{ CMSG_VALOR_DEPOSITADO_BANCO, &msg_valor_depositado_banco_handler },
	{ CMSG_CONGELAR_CONTA, &msg_congelar_conta_handler },
	{ CMSG_DESCONGELAR_CONTA, &msg_descongelar_conta_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler }
};

/************************************************************************/
/* Handler para mensagens sem um handler especifico                     */
/************************************************************************/
void msg_no_action_handler(int socket, mensagem_s *msg)
{
	fprintf(stdout, "MENSAGEM: MSG_NO_ACTION\n");
}

/************************************************************************/
/* Handling do registo de novo utilizador                               */
/************************************************************************/
void msg_registo_handler(int socket, mensagem_s *msg)
{
	//header + str + str + int
	if (msg->size < HEADER_SIZE + 1 + 1 + 4)
	{
		disconnect(socket);
		return;
	}

	fprintf(stdout, "MENSAGEM: CMSG_REGISTO\n");
	char *username = msg_recv_string(msg);
	char *password = msg_recv_string(msg);
	Utilizador * c = GetCliente(username);

	mensagem_s * s = preparePacket(SMSG_REGISTO_RESPONSE);
	if (c != NULL)
	{
		msg_put_short(ERROR_CODE_NOME_UTILIZADOR_OU_PASS_INVALIDA, s);
	}
	else
	{
		//actualizar a base de dados
		Utilizador * newCliente = malloc(sizeof(Utilizador));
		int acesso = msg_recv_int(msg);

		if ((!StringValida(password, 31)) || (!StringValida(username, 31)) || 
			acesso < ACCESS_LEVEL_CLIENTE || acesso > ACCESS_LEVEL_ADMIN )
		{
			msg_put_short(ERROR_CODE_NOME_UTILIZADOR_OU_PASS_INVALIDA, s);
		}
		else
		{
			newCliente->flags = acesso;
			newCliente->id = genUtilizadorID();
			newCliente->congelada = false;

			strcpy(newCliente->password, password);
			strcpy(newCliente->username, username);
			UtilizadorAdd(newCliente);

			msg_put_short(ERROR_CODE_SUCCESS, s);
		}
	}
	free(username);
	free(password);

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do login                                                    */
/************************************************************************/
void msg_login_handler(int socket, mensagem_s *msg)
{
	//header + str + str
	if (msg->size < HEADER_SIZE + 1 + 1)
	{
		disconnect(socket);
		return;
	}

	fprintf(stdout, "MENSAGEM: CMSG_LOGIN\n");
	char *username = msg_recv_string(msg);
	char *password = msg_recv_string(msg);

	mensagem_s * s = preparePacket(SMSG_LOGIN_RESPONSE);
	Utilizador * c = GetCliente(username);
	if (c)
	{
		if (c->congelada)
		{
			msg_put_short(ERROR_CODE_CONTA_CONGELADA, s);
		}
		else if (strcmp(password, c->password) == 0)
		{
			Session *session = malloc(sizeof(Session));
			session->cliente = c;
			session->token = genToken();
			session->sessionExpire = time(NULL) + 1 *60 * 10;
			SessionAdd(session);

			msg_put_short(ERROR_CODE_SUCCESS, s);
			msg_put_int(session->token, s);
			msg_put_byte(c->flags, s);
		}
		else
		{
			msg_put_short(ERROR_CODE_NOME_UTILIZADOR_OU_PASS_INVALIDA, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_NOME_UTILIZADOR_OU_PASS_INVALIDA, s);
	}

	free(username);
	free(password);
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do criar conta                                              */
/************************************************************************/
void msg_criar_conta_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}

	fprintf(stdout, "MENSAGEM: CMSG_CRIAR_CONTA\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_CRIAR_CONTA_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			Conta *conta = malloc(sizeof(Conta));
			conta->clientID = session->cliente->id;
			conta->contaID = genAccountID();
			conta->saldo = 1000;
			ContasAdd(conta);

			Movimento *movimentoO = malloc(sizeof(Movimento));
			movimentoO->owner = conta->contaID;
			movimentoO->contaO = conta->contaID;
			movimentoO->ContaD = conta->contaID;
			movimentoO->id = genMovimentoID();
			movimentoO->Montante = 1000;
			movimentoO->tipo = TIPO_MOVIMENTO_CREDITO;
			MovimentoAdd(movimentoO);

			msg_put_short(ERROR_CODE_SUCCESS, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do listar contas proprias                                   */
/************************************************************************/
void msg_listar_contas_proprias_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_LISTAR_CONTAS_PROPRIAS\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_LISTAR_CONTAS_PROPRIAS_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			msg_put_short(ERROR_CODE_SUCCESS, s);
			msg_put_int(0, s); //total de contas

			int total = 0;
			for (LinkedListNode * p = Begin(dbContas); p != NULL; p = Next(p))
			{
				Conta * conta = p->element;
				if (conta->clientID == session->cliente->id)
				{
					msg_put_int(conta->contaID, s);
					total += 1;
				}
			}
			msg_put_int_at(5, total, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do listar saldo                                             */
/************************************************************************/
void msg_consultar_saldo_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4 + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_CONSULTAR_SALDO\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_CONSULTAR_SALDO_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int contaId = msg_recv_int(msg);
			Conta *conta = GetConta(contaId);
			if (conta && conta->clientID == session->cliente->id)
			{
				msg_put_short(ERROR_CODE_SUCCESS, s);
				msg_put_int(conta->saldo, s);
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do consultar movimentos de uma conta                        */
/************************************************************************/
void msg_consultar_movimentos_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4 + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_CONSULTAR_MOVIMENTOS\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_CONSULTAR_MOVIMENTOS_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int contaId = msg_recv_int(msg);
			Conta *conta = GetConta(contaId);
			if (conta && conta->clientID == session->cliente->id)
			{
				msg_put_short(ERROR_CODE_SUCCESS, s);
				msg_put_int(0, s);
				unsigned char total = 0;
				for (LinkedListNode * p = Begin(dbMovimentos); p != NULL; p = Next(p))
				{
					Movimento * movimento = p->element;
					if (movimento->owner == contaId && total < 256)
					{
						msg_put_byte(movimento->tipo, s);
						msg_put_int(movimento->Montante, s);
						msg_put_int(movimento->contaO, s);
						msg_put_int(movimento->ContaD, s);
						total += 1;
					}
				}
				msg_put_int_at(5, total, s);
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da consulta de saldo integrada                              */
/************************************************************************/
void msg_consulta_saldo_integrada_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_CONSULTA_SALDO_INTEGRADA");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_CONSULTA_SALDO_INTEGRADA_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			msg_put_short(ERROR_CODE_SUCCESS, s);

			int total = 0;
			for (LinkedListNode * p = Begin(dbContas); p != NULL; p = Next(p))
			{
				Conta * conta = p->element;

				if (conta->clientID == session->cliente->id)
				{
					total += conta->saldo;
				}
			}
			msg_put_int(total, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do movimento entre contas                                   */
/************************************************************************/
void msg_movimento_entre_contas_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4 + 4 + 4 + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_MOVIMENTO_ENTRE_CONTAS\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_MOVIMENTO_ENTRE_CONTAS_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int montante = msg_recv_int(msg);
			int contaOrigem = msg_recv_int(msg);
			int contaDestino = msg_recv_int(msg);

			Conta *contaO = GetConta(contaOrigem);
			Conta *contaD = GetConta(contaDestino);

			if (contaO && contaO->clientID == session->cliente->id && contaD && contaD->clientID == session->cliente->id)
			{
				if (contaO->saldo < montante)
				{
					msg_put_short(ERROR_CODE_SALDO_INSUFICIENTE, s);
				}
				else
				{
					contaO->saldo -= montante;
					contaD->saldo += montante;
					//criar movimentos de conta
					//origem
					Movimento *movimentoO = malloc(sizeof(Movimento));
					movimentoO->owner = contaO->contaID;
					movimentoO->contaO = contaO->contaID;
					movimentoO->ContaD = contaD->contaID;
					movimentoO->id = genMovimentoID();
					movimentoO->Montante = montante;
					movimentoO->tipo = TIPO_MOVIMENTO_DEBITO;
					MovimentoAdd(movimentoO);
					//destino
					Movimento *movimentoD = malloc(sizeof(Movimento));
					movimentoD->owner = contaD->contaID;
					movimentoD->contaO = contaO->contaID;
					movimentoD->ContaD = contaD->contaID;
					movimentoD->id = genMovimentoID();
					movimentoD->Montante = montante;
					movimentoD->tipo = TIPO_MOVIMENTO_CREDITO;
					MovimentoAdd(movimentoD);

					//actualizar a db
					DBWriteContas();
					msg_put_short(ERROR_CODE_SUCCESS, s);
				}
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* handler do apagar conta                                              */
/************************************************************************/
void msg_apagar_conta_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4 + 4 + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_APAGAR_CONTA\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_APAGAR_CONTA_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int contaOrigem = msg_recv_int(msg);
			int contaDestino = msg_recv_int(msg);

			Conta *contaO = GetConta(contaOrigem);
			Conta *contaD = GetConta(contaDestino);

			if (contaO && contaO->clientID == session->cliente->id && contaD && contaD->clientID == session->cliente->id)
			{
				contaD->saldo += contaO->saldo;
				
				//criar o movimento
				Movimento *movimentoO = malloc(sizeof(Movimento));
				movimentoO->owner = contaD->contaID;
				movimentoO->contaO = contaO->contaID;
				movimentoO->ContaD = contaD->contaID;
				movimentoO->id = genMovimentoID();
				movimentoO->Montante = contaO->saldo;
				movimentoO->tipo = TIPO_MOVIMENTO_CREDITO;
				MovimentoAdd(movimentoO);

				contaO->saldo = 0;

				//remover todos os movimentos associados a esta conta
				for (LinkedListNode * p = Begin(dbMovimentos); p != NULL;)
				{
					Movimento * movimento = p->element;
					if (movimento->owner == contaO->contaID)
					{ 
						p = Next(p);
						list_remove(movimento, dbMovimentos);
					}
					else
					{
						p = Next(p);
					}
				}

				DBWriteMovimentos();

				//remover a conta
				list_remove(contaO, dbContas);
				DBWriteContas();
				msg_put_short(ERROR_CODE_SUCCESS, s);
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da listagem dos servicos                                    */
/************************************************************************/
void msg_listar_servicos_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_LISTAR_SERVICOS\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_LISTAR_SERVICOS_PAGAMENTO_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		int total = 0;
		msg_put_short(ERROR_CODE_SUCCESS, s);
		msg_put_int(0, s);
		for (LinkedListNode * p = Begin(dbServicos); p != NULL; p = Next(p))
		{
			Servico * servico = p->element;
			msg_put_int(servico->id, s);
			msg_put_string(servico->nome, s);
			total += 1;
		}
		msg_put_int_at(5, total, s);
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do pagamento de um servico                                  */
/************************************************************************/
void msg_efectuar_pagamento_servico_handler(int socket, mensagem_s * msg)
{
	if (msg->size < HEADER_SIZE + 4 + 4 + 4 + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_EFECTUAR_PAGAMENTO_SERVICO\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_EFECTUAR_PAGAMENTO_SERVICO_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN || session->cliente->flags == ACCESS_LEVEL_ENTIDADE)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int servicoId = msg_recv_int(msg);
			int contaOrigem = msg_recv_int(msg);
			int montante = msg_recv_int(msg);

			Conta *conta = GetConta(contaOrigem);
			if (conta)
			{
				if (conta->saldo < montante)
				{
					msg_put_short(ERROR_CODE_SALDO_INSUFICIENTE, s);
				}
				else
				{
					Servico *servico = GetServico(servicoId);
					if (servico)
					{
						//obter a conta de destino para onde vai ser depositado o dinheiro
						Conta *contaD = GetConta(servico->nib);
						if (contaD)
						{
							contaD->saldo += montante;
							conta->saldo -= montante;

							Movimento *movimentoO = malloc(sizeof(Movimento));
							movimentoO->owner = conta->contaID;
							movimentoO->contaO = conta->contaID;
							movimentoO->ContaD = contaD->contaID;
							movimentoO->id = genMovimentoID();
							movimentoO->Montante = montante;
							movimentoO->tipo = TIPO_MOVIMENTO_DEBITO;
							MovimentoAdd(movimentoO);

							Movimento *movimentoD = malloc(sizeof(Movimento));
							movimentoD->owner = contaD->contaID;
							movimentoD->contaO = conta->contaID;
							movimentoD->ContaD = contaD->contaID;
							movimentoD->id = genMovimentoID();
							movimentoD->Montante = montante;
							movimentoD->tipo = TIPO_MOVIMENTO_CREDITO;
							MovimentoAdd(movimentoD);

							DBWriteContas();
							msg_put_short(ERROR_CODE_SUCCESS, s);
						}
						else
						{
							msg_put_short(ERROR_CODE_GERAL, s);
						}
					}
					else
					{
						msg_put_short(ERROR_CODE_SERVICO_NAO_EXISTE, s);
					}
				}
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da criacao de um novo servico                               */
/************************************************************************/
void msg_criar_novo_servico_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4 + 4 + 1)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_CRIAR_NOVO_SERVICO\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_CRIAR_NOVO_SERVICO_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ADMIN || session->cliente->flags == ACCESS_LEVEL_CLIENTE)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			char * nomeServico = msg_recv_string(msg);
			int nib = msg_recv_int(msg);
			//verificacao minima do nome de servico
			if (strlen(nomeServico) == 0 || strlen(nomeServico) > 255)
			{
				msg_put_short(ERROR_CODE_NOME_SERVICO_INVALIDO, s);
			}
			else
			{
				Servico *servico = malloc(sizeof(Servico));

				Conta *contaD = GetConta(nib);
				if (contaD)
				{
					servico->id = genServicoID();
					servico->nib = nib;
					strcpy(servico->nome, nomeServico);
					ServicoAdd(servico);

					msg_put_short(ERROR_CODE_SUCCESS, s);
				}
				else
				{
					msg_put_short(ERROR_CODE_NIB_INVALIDO, s);
				}
			}
			free(nomeServico);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da listagem de clientes                                     */
/************************************************************************/
void msg_listar_clientes_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_LISTAR_CLIENTES");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_LISTAR_CLIENTES_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ENTIDADE || session->cliente->flags == ACCESS_LEVEL_CLIENTE)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			unsigned char total = 0;
			msg_put_short(ERROR_CODE_SUCCESS, s);
			msg_put_int(0, s);
			for (LinkedListNode * p = Begin(dbUtilizadores); p != NULL; p = Next(p))
			{
				Utilizador * cliente = p->element;
				if (cliente->flags == ACCESS_LEVEL_CLIENTE)
				{
					msg_put_string(cliente->username, s);
					int saldo = 0;
					for (LinkedListNode * p2 = Begin(dbContas); p2 != NULL; p2 = Next(p2))
					{
						Conta *conta = p2->element;
						if (conta->clientID == cliente->id)
						{
							saldo += conta->saldo;
						}
					}
					msg_put_int(saldo, s);
					total += 1;
				}
			}
			msg_put_int_at(5, total, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da listagem das entidades                                   */
/************************************************************************/
void msg_listar_entidades_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_LISTAR_ENTIDADES\n");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_LISTAR_ENTIDADES_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ENTIDADE || session->cliente->flags == ACCESS_LEVEL_CLIENTE)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int total = 0;
			msg_put_short(ERROR_CODE_SUCCESS, s);
			msg_put_int(0, s);
			for (LinkedListNode * p = Begin(dbUtilizadores); p != NULL; p = Next(p))
			{
				Utilizador * cliente = p->element;
				if (cliente->flags == ACCESS_LEVEL_ENTIDADE)
				{
					msg_put_string(cliente->username, s);
					int saldo = 0;
					for (LinkedListNode * p2 = Begin(dbContas); p2 != NULL; p2 = Next(p2))
					{
						Conta *conta = p2->element;
						if (conta->clientID == cliente->id)
						{
							saldo += conta->saldo;
						}
					}
					msg_put_int(saldo, s);
					total += 1;
				}
			}
			msg_put_int_at(5, total, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling do valor depositado no banco                                */
/************************************************************************/
void msg_valor_depositado_banco_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_VALOR_DEPOSITADO_BANCO");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_VALOR_DEPOSITADO_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ENTIDADE || session->cliente->flags == ACCESS_LEVEL_CLIENTE)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			int total = 0;
			for (LinkedListNode * p = Begin(dbContas); p != NULL; p = Next(p))
			{
				Conta * conta = p->element;
				total += conta->saldo;
			}
			msg_put_short(ERROR_CODE_SUCCESS, s);
			msg_put_int(total, s);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}
	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da congelacao de uma conta                                  */
/************************************************************************/
void msg_congelar_conta_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_CONGELAR_CONTA");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_CONGELAR_CONTA_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ENTIDADE || session->cliente->flags == ACCESS_LEVEL_CLIENTE)
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			char *utilizador = msg_recv_string(msg);
			Utilizador *cliente = GetCliente(utilizador);
			if (cliente)
			{
				cliente->congelada = true;
				DBWriteUtilizadores();
				msg_put_short(ERROR_CODE_SUCCESS, s);
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
			free(utilizador);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}

/************************************************************************/
/* Handling da descongelacao de uma conta                                  */
/************************************************************************/
void msg_descongelar_conta_handler(int socket, mensagem_s *msg)
{
	if (msg->size < HEADER_SIZE + 4 + 1)
	{
		disconnect(socket);
		return;
	}
	fprintf(stdout, "MENSAGEM: CMSG_DESCONGELAR_CONTA");
	int token = msg_recv_int(msg);

	mensagem_s * s = preparePacket(SMSG_DESCONGELAR_CONTA_RESPONSE);
	//obter a session activa
	Session *session = GetSession(token);
	if (session && session->sessionExpire >= currTime)
	{
		if (session->cliente->flags == ACCESS_LEVEL_ENTIDADE || session->cliente->flags == ACCESS_LEVEL_CLIENTE) 
		{
			msg_put_short(ERROR_CODE_TIPO_CONTA_INVALIDA, s);
		}
		else
		{
			char *utilizador = msg_recv_string(msg);
			Utilizador *cliente = GetCliente(utilizador);
			if (cliente)
			{
				cliente->congelada = false;
				DBWriteUtilizadores();
				msg_put_short(ERROR_CODE_SUCCESS, s);
			}
			else
			{
				msg_put_short(ERROR_CODE_CONTA_INVALIDA, s);
			}
			free(utilizador);
		}
	}
	else
	{
		msg_put_short(ERROR_CODE_SESSAO_EXPIROU, s);
	}

	finalizePacket(s);
	sendData(socket, s);
	free(s);
}
