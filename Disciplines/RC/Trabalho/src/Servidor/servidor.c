#include "../shared/common.h"
#include <stdio.h>
#include <string.h>


#ifdef unix			// se o SO for linux/unix
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#endif
#ifdef _WIN32				// se o SO for windows
#include <winsock2.h>
#include "win/unistd.h"
WSADATA wsa_data;
#endif
#include "servidor.h"
#include "database.h"

time_t currTime;

/************************************************************************/
/* Evocacao da funcao com o handler da mensagem                         */
/************************************************************************/
void callback(func Callback, int arg1, mensagem_s *arg2)
{
	Callback(arg1, arg2);
}

/************************************************************************/
/* Inicializa o servidorr                                               */
/************************************************************************/
int inicializarServidor()
{
#ifndef unix 	// se o SO for windows
	WSAStartup(MAKEWORD(2, 0), &wsa_data);
#endif

	// Definição e população da estructura sockaddr_in -------------------------
	struct sockaddr_in servidor;
	memset(&(servidor.sin_zero), 0, sizeof(servidor.sin_zero));
	servidor.sin_family = AF_INET;
	servidor.sin_addr.s_addr = htonl(INADDR_ANY);
	servidor.sin_port = htons(2000); //servidor na porta 2000

	//criar o socket	--------------------------------------------------------
	int sock = socket(AF_INET, SOCK_STREAM, 0);

	//fazer o bind	------------------------------------------------------------
	int bindResult = bind(sock, (struct sockaddr *) &servidor, sizeof(servidor));
	if (bindResult == -1) // verifica se houve erros no bind
		printf("Bind: Falhou!\n");
	else
		printf("Bind: ok!\n");

	// fazer o listen com uma fila de 1 cliente no máximo ----------------------
	int tamanho_fila = 1;
	if (listen(sock, tamanho_fila) == -1)  // verifica se o listen falhou
		printf("Listen: Falhou!\n");
	else
		printf("Listen: ok!\n");
	return sock;
}

/************************************************************************/
/* Disconecta o cliente                                                 */
/************************************************************************/
void disconnect(int socket)
{
	shutdown(socket, 2);
}

/************************************************************************/
/* Recebe dados do socket                                               */
/************************************************************************/
void recvData(int socket)
{
	mensagem_s msg = { "", 0, 0 };
	msg.size = recv(socket, msg.buffer, sizeof(msg.buffer), 0);
	if (msg.size < 3)
	{
		fprintf(stdout, "Error: Header invalido\n");
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
/* Envia dados para o cliente                                           */
/************************************************************************/
void sendData(int socket, mensagem_s *s)
{
	send(socket, s->buffer, s->size + HEADER_SIZE, 0);
}

/************************************************************************/
/* Espera por uma ligacao do cliente                                    */
/************************************************************************/
void waitForClient(int socket)
{
	int tamanho_cliente;	// guarda o tamanho da estructura do cliente
	int sock_aceite;		// guarda socket aceite
	struct sockaddr_in cliente; // preciso de outro para o cliente

	// fica à espera que um cliente se ligue
	tamanho_cliente = sizeof(struct sockaddr_in);
	sock_aceite = accept(socket, (struct sockaddr *)&cliente, &tamanho_cliente);
	if (sock_aceite > -1)
	{	//se recebeu um cliente
		fprintf(stdout, "Cliente aceite\n");
		currTime = time(NULL);
		recvData(sock_aceite);
	}
}

/************************************************************************/
/* funcao principal                                                 */
/************************************************************************/
main()
{
	inicializarDatabase();
	int socket = inicializarServidor();
	while (1)
	{
		waitForClient(socket);
	}

	destroyDatabase();
}
