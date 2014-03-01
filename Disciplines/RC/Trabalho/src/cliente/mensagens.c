#include "../shared/common.h"
#include "mensagens.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <malloc.h>
#include "cliente.h"
#include "consola.h"

/************************************************************************/
/* Tabela de opcodes                                                    */
/************************************************************************/
opcode_handler opcode_dispatch[35] = {
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
	{ MSG_NULL, &msg_no_action_handler },
	{ MSG_NULL, &msg_no_action_handler },
	{ SMSG_LOGIN_RESPONSE, &msg_login_response_handler },
	{ SMSG_REGISTO_RESPONSE, &msg_registo_response_handler },
	{ SMSG_LISTAR_CONTAS_PROPRIAS_RESPONSE, &msg_listar_contas_proprias_response_handler },
	{ SMSG_CRIAR_CONTA_RESPONSE, &msg_criar_conta_response_handler },
	{ SMSG_CONSULTAR_SALDO_RESPONSE, &msg_consultar_saldo_response_handler },
	{ SMSG_CONSULTAR_MOVIMENTOS_RESPONSE, &msg_consultar_movimentos_response_handler },
	{ SMSG_CONSULTA_SALDO_INTEGRADA_RESPONSE, &msg_consulta_saldo_integrada_handler },
	{ SMSG_MOVIMENTO_ENTRE_CONTAS_RESPONSE, &msg_movimento_entre_contas_handler },
	{ SMSG_APAGAR_CONTA_RESPONSE, &msg_apagar_conta_response_handler },
	{ SMSG_LISTAR_SERVICOS_PAGAMENTO_RESPONSE, &msg_listar_servicos_pagamento_handler },
	{ SMSG_CRIAR_NOVO_SERVICO_RESPONSE, &msg_criar_novo_servico_handler },
	{ SMSG_EFECTUAR_PAGAMENTO_SERVICO_RESPONSE, &msg_efectuar_pagamento_servico_handler },
	{ SMSG_LISTAR_CLIENTES_RESPONSE, &msg_listar_clientes_handler },
	{ SMSG_LISTAR_ENTIDADES_RESPONSE, &msg_listar_entidades_handler },
	{ SMSG_VALOR_DEPOSITADO_RESPONSE, &msg_valor_depositado_handler },
	{ SMSG_VALOR_CONGELAR_CONTA_RESPONSE, &msg_congelar_conta_handler },
	{ SMSG_VALOR_DESCONGELAR_CONTA_RESPONSE, &msg_descongelar_conta_handler }
};

/************************************************************************/
/* Tabela de erros                                                      */
/************************************************************************/
void initializeErrorTable()
{
	int i;
	for (i = 0; i < NUM_ERROR_CODES; i++)
	{
		result_error_table[i].errorStr = "Unknown error\n";
	}

	result_error_table[ERROR_CODE_SUCCESS].errorStr = "Operacao feita com sucesso\n";
	result_error_table[ERROR_CODE_SESSAO_EXPIROU].errorStr = "Sessao expirou\n";
	result_error_table[ERROR_CODE_CONTA_INVALIDA].errorStr = "A conta nao existe\n";
	result_error_table[ERROR_CODE_SALDO_INSUFICIENTE].errorStr = "Saldo insuficiente\n";
	result_error_table[ERROR_CODE_NOME_SERVICO_INVALIDO].errorStr = "Nome de servico invalido\n";
	result_error_table[ERROR_CODE_NIB_INVALIDO].errorStr = "NIB invalido\n";
	result_error_table[ERROR_CODE_SERVICO_NAO_EXISTE].errorStr = "O servico nao existe\n";
	result_error_table[ERROR_CODE_IMPOSSIVEL_CONGELAR_ADMIN].errorStr = "Impossivel congelar/descongelar contas administrativas\n";
	result_error_table[ERROR_CODE_NOME_UTILIZADOR_OU_PASS_INVALIDA].errorStr = "Nome de utilizador ou palavra-passe invalidos\n";
	result_error_table[ERROR_CODE_TIPO_CONTA_INVALIDA].errorStr = "Tipo de conta invalida\n";
	result_error_table[ERROR_CODE_NOME_DE_CONTA_CLIENTE_ENTIDADE_INVALIDA].errorStr = "Nome de conta cliente/entidade invalida\n";
	result_error_table[ERROR_CODE_GERAL].errorStr = "Operacao invalida\n";
	result_error_table[ERROR_CODE_CONTA_CONGELADA].errorStr = "A sua conta foi congelada\n";
}


/************************************************************************/
/* handler generico                                                     */
/************************************************************************/
void msg_no_action_handler(int socket, mensagem_s *msg)
{
	//fprintf(stdout, "MENSAGEM: MSG_NO_ACTION\n");
}

/************************************************************************/
/* handler do login                                                     */
/************************************************************************/
void msg_login_response_handler(int socket, mensagem_s *msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_LOGIN_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_AUTH_MENU;
	}
	else
	{
		//login feito com sucesso
		token = msg_recv_int(msg);
		accessLevel = msg_recv_byte(msg);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_MAIN_MENU;
	}
}

/************************************************************************/
/* handler do registo                                                   */
/************************************************************************/
void msg_registo_response_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_REGISTO_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_AUTH_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_AUTH_MENU;
	}
}

/************************************************************************/
/* handler da listagem das contas proprias                              */
/************************************************************************/
void msg_listar_contas_proprias_response_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_LISTAR_CONTAS_PROPIAS_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		fprintf(stdout, "--------Listagem de contas---------\n");
		int totalContas = msg_recv_int(msg);

		for (int i = 0; i < totalContas; i++)
		{
			int contaId = msg_recv_int(msg);
			fprintf(stdout, "Conta: %d\n", contaId);
		}
		fprintf(stdout, "-----------------------------------\n");
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTA_MENU;
	}
}

/************************************************************************/
/* handler da criacao de uma conta                                      */
/************************************************************************/
void msg_criar_conta_response_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_CRIAR_CONTA_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
}

/************************************************************************/
/* handler da consulta do saldo de uma conta                            */
/************************************************************************/
void msg_consultar_saldo_response_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_CONSULTAR_SALDO_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTA_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		int saldo = msg_recv_int(msg);
		fprintf(stdout, "Saldo actual: %d euros\n", saldo);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTA_MENU;
	}
}

/************************************************************************/
/* handler da consulta de movimentos                                    */
/************************************************************************/
void msg_consultar_movimentos_response_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_CONSULTAR_MOVIMENTOS_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	else
	{
		int totalMovimentos = msg_recv_int(msg);
		fprintf(stdout, "ORIGEM  - DESTINO - MONTANTE - OPERACAO\n");
		for (int i = 0; i < totalMovimentos; i++)
		{
			char tipoOperacao = msg_recv_byte(msg);
			int quantia = msg_recv_int(msg);
			int contaO = msg_recv_int(msg);
			int contaD = msg_recv_int(msg);

			fprintf(stdout, "%d - %d - %d - %s\n", contaO, contaD, quantia, tipoOperacao == 1 ? "Credito" : "Debito");
		}
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	fprintf(stdout, "\n");
}

/************************************************************************/
/* handler da consulta de saldo integrada                               */
/************************************************************************/
void msg_consulta_saldo_integrada_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_CONSULTA_SALDO_INTEGRADA_RESPONSE\n");

	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		int saldo = msg_recv_int(msg);
		fprintf(stdout, "Saldo total na conta: %d euros\n", saldo);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
}

/************************************************************************/
/* handler do movimento entre contas                                    */
/************************************************************************/
void msg_movimento_entre_contas_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_MOVIMENTO_ENTRE_CONTAS_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
}

/************************************************************************/
/* handler do apagar conta                                              */
/************************************************************************/
void msg_apagar_conta_response_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_APAGAR_CONTA_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
	}
}

/************************************************************************/
/* handler da listagem de servicos                                      */
/************************************************************************/
void msg_listar_servicos_pagamento_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_LISTAR_SERVICOS_PAGAMENTO_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICO_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		int totalServicos = msg_recv_int(msg);
		fprintf(stdout, "ID ---- NOME DE SERVICO\n");
		for (int i = 0; i < totalServicos; i++)
		{
			int servicoID = msg_recv_int(msg);
			char *nome = msg_recv_string(msg);
			fprintf(stdout, "%d - %s\n", servicoID, nome);
			free(nome);
		}
		consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICO_MENU;
	}
}
/************************************************************************/
/* handler da criação de um novo servico                                */
/************************************************************************/
void msg_criar_novo_servico_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_CRIAR_NOVO_SERVICO_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
	}
}

/************************************************************************/
/* handler do efectuar pagamento de servico                             */
/************************************************************************/
void msg_efectuar_pagamento_servico_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_EFECTUAR_PAGAMENTO_SERVICO_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
	}
}

/************************************************************************/
/* handler da listagem de cliente                                       */
/************************************************************************/
void msg_listar_clientes_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_LISTAR_CLIENTES_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		int total = msg_recv_int(msg);
		fprintf(stdout, "CLIENTE ---- SALDO\n");
		for (int i = 0; i < total; i++)
		{
			char *username = msg_recv_string(msg);
			int saldo = msg_recv_int(msg);
			fprintf(stdout, "%s - %d euros\n", username, saldo);
			free(username);
		}
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
}

/************************************************************************/
/* handler da listagem de entidades                                     */
/************************************************************************/
void msg_listar_entidades_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_LISTAR_ENTIDADES_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		int total = msg_recv_int(msg);
		fprintf(stdout, "ENTIDADE ---- SALDO\n");
		for (int i = 0; i < total; i++)
		{
			char *username = msg_recv_string(msg);
			int saldo = msg_recv_int(msg);
			fprintf(stdout, "%s - %d euros\n", username, saldo);
			free(username);
		}
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
}

/************************************************************************/
/* handler do valor depositado no banco                                 */
/************************************************************************/
void msg_valor_depositado_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_VALOR_DEPOSITADO_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
	else
	{
		int total = msg_recv_int(msg);
		fprintf(stdout, "Total depositado: %d euros\n", total);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
}

/************************************************************************/
/* handler da congelacao de uma conta                                   */
/************************************************************************/
void msg_congelar_conta_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_CONGELAR_CONTA_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
}

/************************************************************************/
/* handler da descongelacao de uma conta                                */
/************************************************************************/
void msg_descongelar_conta_handler(int socket, mensagem_s * msg)
{
	//fprintf(stdout, "MENSAGEM: SMSG_DESCONGELAR_CONTA_RESPONSE\n");
	short resultado = msg_recv_short(msg);
	if (resultado)
	{
		assert(resultado < NUM_ERROR_CODES);
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
	else
	{
		fprintf(stdout, result_error_table[resultado].errorStr);
		consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
	}
}
