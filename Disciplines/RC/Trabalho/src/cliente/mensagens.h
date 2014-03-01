#include "../shared/msg_streamer.h"
#ifndef _MENSAGENS_H_
#define _MENSAGENS_H_

/************************************************************************/
/* Codigos de erro                                                      */
/************************************************************************/
enum CODIGOS_ERRO
{
	ERROR_CODE_SUCCESS = 0,
	ERROR_CODE_SESSAO_EXPIROU = 501,
	ERROR_CODE_CONTA_INVALIDA = 201,
	ERROR_CODE_SALDO_INSUFICIENTE = 202,
	ERROR_CODE_NOME_SERVICO_INVALIDO = 301,
	ERROR_CODE_NIB_INVALIDO = 302,
	ERROR_CODE_SERVICO_NAO_EXISTE = 303,
	ERROR_CODE_IMPOSSIVEL_CONGELAR_ADMIN = 401,
	ERROR_CODE_NOME_UTILIZADOR_OU_PASS_INVALIDA = 101,
	ERROR_CODE_TIPO_CONTA_INVALIDA = 102,
	ERROR_CODE_NOME_DE_CONTA_CLIENTE_ENTIDADE_INVALIDA = 103,
	ERROR_CODE_CONTA_CONGELADA = 104,
	ERROR_CODE_GERAL = 502,
	NUM_ERROR_CODES
};

/************************************************************************/
/* lista de opcodes                                                     */
/************************************************************************/
enum OPCODES
{
	MSG_NULL = 0,
	CMSG_LOGIN,
	CMSG_REGISTO,
	CMSG_CRIAR_CONTA,
	CMSG_LISTAR_CONTAS_PROPRIAS,
	CMSG_CONSULTAR_SALDO,
	CMSG_CONSULTAR_MOVIMENTOS,
	CMSG_CONSULTA_SALDO_INTEGRADA,
	CMSG_MOVIMENTO_ENTRE_CONTAS,
	CMSG_APAGAR_CONTA,
	CMSG_LISTAR_SERVICOS_PAGAMENTO,
	CMSG_EFECTUAR_PAGAMENTO_SERVICO,
	CMSG_CRIAR_NOVO_SERVICO,
	CMSG_LISTAR_CLIENTES,
	CMSG_LISTAR_ENTIDADES,
	CMSG_VALOR_DEPOSITADO_BANCO,
	CMSG_CONGELAR_CONTA,
	CMSG_DESCONGELAR_CONTA,
	SMSG_LOGIN_RESPONSE,
	SMSG_REGISTO_RESPONSE,
	SMSG_LISTAR_CONTAS_PROPRIAS_RESPONSE,
	SMSG_CRIAR_CONTA_RESPONSE,
	SMSG_CONSULTAR_SALDO_RESPONSE,
	SMSG_CONSULTAR_MOVIMENTOS_RESPONSE,
	SMSG_CONSULTA_SALDO_INTEGRADA_RESPONSE,
	SMSG_MOVIMENTO_ENTRE_CONTAS_RESPONSE,
	SMSG_APAGAR_CONTA_RESPONSE,
	SMSG_LISTAR_SERVICOS_PAGAMENTO_RESPONSE,
	SMSG_CRIAR_NOVO_SERVICO_RESPONSE,
	SMSG_EFECTUAR_PAGAMENTO_SERVICO_RESPONSE,
	SMSG_LISTAR_CLIENTES_RESPONSE,
	SMSG_LISTAR_ENTIDADES_RESPONSE,
	SMSG_VALOR_DEPOSITADO_RESPONSE,
	SMSG_VALOR_CONGELAR_CONTA_RESPONSE,
	SMSG_VALOR_DESCONGELAR_CONTA_RESPONSE,
	NUM_MENSAGENS
};

typedef struct opcode_handler
{
	int msgId;
	func *callback;
}opcode_handler;

typedef struct message_result_error
{
	char *errorStr;

}message_result_error;

/************************************************************************/
/* Handlers                                                             */
/************************************************************************/
void msg_no_action_handler(int socket, mensagem_s *msg);
void msg_login_response_handler(int socket, mensagem_s *msg);
void msg_registo_response_handler(int socket, mensagem_s * msg);
void msg_listar_contas_proprias_response_handler(int socket, mensagem_s * msg);
void msg_criar_conta_response_handler(int socket, mensagem_s * msg);
void msg_consultar_saldo_response_handler(int socket, mensagem_s * msg);
void msg_consultar_movimentos_response_handler(int socket, mensagem_s * msg);
void msg_consulta_saldo_integrada_handler(int socket, mensagem_s * msg);
void msg_movimento_entre_contas_handler(int socket, mensagem_s * msg);
void msg_apagar_conta_response_handler(int socket, mensagem_s * msg);
void msg_listar_servicos_pagamento_handler(int socket, mensagem_s * msg);
void msg_criar_novo_servico_handler(int socket, mensagem_s * msg);
void msg_efectuar_pagamento_servico_handler(int socket, mensagem_s * msg);
void msg_listar_clientes_handler(int socket, mensagem_s * msg);
void msg_listar_entidades_handler(int socket, mensagem_s * msg);
void msg_valor_depositado_handler(int socket, mensagem_s * msg);
void msg_congelar_conta_handler(int socket, mensagem_s * msg);
void msg_descongelar_conta_handler(int socket, mensagem_s * msg);



opcode_handler opcode_dispatch[35];
message_result_error result_error_table[NUM_ERROR_CODES];

void initializeErrorTable();

#endif
