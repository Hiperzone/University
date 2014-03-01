#include "../shared/common.h"
#include "sconnection.h"
#include <stdio.h>

#ifdef unix		// se o SO for linux/unix
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#else				// se o SO for windows
#include <winsock2.h>
WSADATA wsa_data;
#endif
#include "mensagens.h"
#include "cliente.h"
#include "consola.h"

int sock = 0;
struct sockaddr_in server;

/************************************************************************/
/* Evocacao da funcao com o handler da mensagem                         */
/************************************************************************/
void callback(func Callback, int arg1, mensagem_s *arg2)
{
	Callback(arg1, arg2);
}

/************************************************************************/
/* Inicia a ligacao ao servidor                                         */
/************************************************************************/
int InitConnection()
{
#ifndef unix	// se o SO for windows
	WSAStartup(MAKEWORD(2, 0), &wsa_data);
#endif

	//criar o socket	--------------------------------------------------------
	int sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	// Definição e população da estructura sockaddr_in -------------------------
	memset(&(server.sin_zero), 0, sizeof(server.sin_zero));
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = inet_addr("127.0.0.1");	//define o IP
	server.sin_port = htons(2000);						//define a porta [1012-65535]

	return sock;
}

/************************************************************************/
/* Envia a mensagem codificada para o servidor                          */
/************************************************************************/
void SendData(int sock, char *mensagem, int size)
{
	send(sock, mensagem, size, 0);
}

/************************************************************************/
/* Permite receber dados do servidor                                    */
/************************************************************************/
void RecvData(int socket)
{
	mensagem_s msg = { "", 0, 0 };
	msg.size = recv(socket, msg.buffer, sizeof(msg.buffer), 0);
	if (msg.size < 3)
	{
		fprintf(stdout, "%s\n", "Ocorreu um erro inesperado ao tentar comunicar com o servidor\n");
		consoleEventStatus = CONSOLE_EVENT_SHOW_AUTH_MENU;
		shutdown(socket, 2);
		return;
	}

	//extrair o opcode
	short msg_id = 0;
	short msg_size = 0;
	msg_id = msg_recv_byte(&msg);
	msg_size = msg_recv_short(&msg);
	if (msg_id < NUM_MENSAGENS)
	{
		callback(opcode_dispatch[msg_id].callback, socket, &msg);
	}
	else
	{
		fprintf(stdout, "Error: Opcode invalido\n");
	}
}

/************************************************************************/
/* Estabelece uma ligacao ao servidor                                   */
/************************************************************************/
int ConnectToServer(int sock)
{
	return connect(sock, (struct sockaddr *)&server, sizeof(server));
}

/************************************************************************/
/* Estabelece uma ligacao ao servidor e envia a msg                      */
/************************************************************************/
int ConnectAndSendRequest(mensagem_s * s)
{
	sock = InitConnection();
	int resultado = ConnectToServer(sock);
	if (resultado == -1)
	{
		fprintf(stdout, "Servico nao disponivel de momento, tente mais tarde!\n");
	}
	else
	{
		fprintf(stdout, "Aguarde por favor...\n");
		SendData(sock, s->buffer, s->size + 3);
		RecvData(sock);
		return 1;
	}
	free(s);
	return 0;
}