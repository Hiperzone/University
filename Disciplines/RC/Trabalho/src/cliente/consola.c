#include "../shared/common.h"
#include "consola.h"
#include "cliente.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "mensagens.h"
#include "sconnection.h"
#include "../shared/msg_streamer.h"

/************************************************************************/
/* Mostra o menu sem inicio de sessao                                   */
/************************************************************************/
void mostrarMenuAuth()
{
	int opcao = -1;
	int acesso = -1;
	char username[32];
	char password[32];
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);

	consoleEventStatus = CONSOLE_EVENT_NONE;

	fprintf(stdout, "-------MENU-------\n");
	fprintf(stdout, "0 - Registo\n");
	fprintf(stdout, "1 - Login\n");
	fprintf(stdout, "2 - Sair\n");
	fprintf(stdout, "\n");

	while (1)
	{
        memset(buffer, 0, sizeof(char)*255);
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
		sscanf(buffer, "%d", &opcao);


		if (opcao == 0)
		{
			fprintf(stdout, "NIVEL DE ACESSO\n");
			fprintf(stdout, "0 - Cliente\n");
			fprintf(stdout, "1 - Entidade\n");
			fprintf(stdout, "2 - Administrador\n");

			while (1)
			{
				fprintf(stdout, "Nivel de acesso:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &acesso);

				if (acesso >= 0 && acesso < 3) break;
				else fprintf(stdout, "Nivel de acesso invalido\n");
			}

			while (1)
			{
				fprintf(stdout, "Username:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%31[^\n]s", &username);
				if (StringValida(buffer, 32)) break;
				else fprintf(stdout, "Username invalido, verifique o seu username\n");
			}

			while (1)
			{
				fprintf(stdout, "Password:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%31[^\n]s", &password);
				if (StringValida(buffer, 32)) break;
				else fprintf(stdout, "Password invalida, verifique a sua password\n");
			}

			mensagem_s * s = preparePacket(CMSG_REGISTO);
			msg_put_string(username, s);
			msg_put_string(password, s);
			msg_put_int(acesso, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 1)
		{
			while (1)
			{
				fprintf(stdout, "Username:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%31[^\n]s", &username);
				if (StringValida(username, 32)) break;
				else fprintf(stdout, "Username invalido, verifique o seu username\n");
			}

			while (1)
			{
				fprintf(stdout, "Password:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%31[^\n]s", &password);
				if (StringValida(password, 32)) break;
				else fprintf(stdout, "Password invalida, verifique a sua password\n");
			}

			mensagem_s * s = preparePacket(CMSG_LOGIN);
			msg_put_string(username, s);
			msg_put_string(password, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 2)
		{
			exit(0);
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}
	}
	fprintf(stdout, "\n");
}

/************************************************************************/
/* Mostra o menu com inicio de sessao                                   */
/************************************************************************/
void mostrarMenuPrincipal()
{
	int opcao = -1;
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);
	consoleEventStatus = CONSOLE_EVENT_NONE;
	fprintf(stdout, "-------MENU PRINCIPAL-------\n");

	if (accessLevel == ACCESS_LEVEL_ADMIN)
	{

        fprintf(stdout, "0 - Pagamentos e Servicos\n");
		fprintf(stdout, "1 - Administracao\n");
		fprintf(stdout, "2 - Logout\n");
	}
	else
	{
        fprintf(stdout, "0 - Contas\n");
        fprintf(stdout, "1 - Pagamentos e Servicos\n");
		fprintf(stdout, "2 - Logout\n");
	}

	while (1)
	{
        memset(buffer, 0, sizeof(char)*255);
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
        sscanf(buffer, "%d", &opcao);

		if (opcao == 0 && accessLevel == ACCESS_LEVEL_ADMIN)
		{
            consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
            break;
		}
		else if(opcao == 0 && accessLevel != ACCESS_LEVEL_ADMIN)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
			break;
		}
		else if (opcao == 1 && accessLevel != ACCESS_LEVEL_ADMIN)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
			break;
		}
		else if (opcao == 1 && accessLevel == ACCESS_LEVEL_ADMIN)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_ADMIN_MENU;
			break;
		}
		else if (opcao == 2)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_AUTH_MENU;
			break;
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}
	}
	fprintf(stdout, "\n");
}

/************************************************************************/
/* Mostra o menu das contas                                             */
/************************************************************************/
void mostrarMenuContas()
{
	int opcao = -1;
	consoleEventStatus = CONSOLE_EVENT_NONE;
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);
	fprintf(stdout, "-------MENU CONTAS-------\n");
	fprintf(stdout, "0 - Listar Contas\n");
	fprintf(stdout, "1 - Criar Conta\n");
	fprintf(stdout, "2 - Consulta de saldo integrado\n");
	fprintf(stdout, "3 - Voltar ao menu principal\n");

	while (1)
	{
        memset(buffer, 0, sizeof(char)*255);
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
        sscanf(buffer, "%d", &opcao);

		if (opcao == 0)
		{
			mensagem_s * s = preparePacket(CMSG_LISTAR_CONTAS_PROPRIAS);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 1)
		{
			mensagem_s * s = preparePacket(CMSG_CRIAR_CONTA);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}

		else if (opcao == 2)
		{
			mensagem_s * s = preparePacket(CMSG_CONSULTA_SALDO_INTEGRADA);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 3)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_MAIN_MENU;
			break;
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}

	}
	fprintf(stdout, "\n");
}

/************************************************************************/
/* Mostra o menu do servico                                             */
/************************************************************************/
void mostrarMenuServico()
{
	int opcao = -1;
	consoleEventStatus = CONSOLE_EVENT_NONE;
	int servicoID = 0;
	int contaOrigem;
	int montante = 0;
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);
	fprintf(stdout, "-------MENU PAGAMENTO DE SERVICO-------\n");
	if(accessLevel == ACCESS_LEVEL_CLIENTE)
	{
        fprintf(stdout, "0 - Efectuar pagamento de servico\n");
        fprintf(stdout, "1 - Voltar ao menu servicos de pagamento\n");
	}
	else
	{
        fprintf(stdout, "0 - Voltar ao menu servicos de pagamento\n");
	}



	while (1)
	{
        memset(buffer, 0, sizeof(char)*255);
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
        sscanf(buffer, "%d", &opcao);

		if (opcao == 0 && accessLevel == ACCESS_LEVEL_CLIENTE)
		{
			mensagem_s * s = preparePacket(CMSG_EFECTUAR_PAGAMENTO_SERVICO);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Numero de servico:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &servicoID);
				if (servicoID > 0) break;
				else fprintf(stdout, "Numero de servico invalido:\n");
			}

			while (1)
			{
				fprintf(stdout, "Conta de origem:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &contaOrigem);
				if (contaOrigem > 0) break;
				else fprintf(stdout, "Conta de origem invalida:\n");
			}

			while (1)
			{
				fprintf(stdout, "Montante a mover:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &montante);
				if (montante > 0) break;
				else fprintf(stdout, "Montante inserdido e invalido:\n");
			}

			msg_put_int(servicoID, s);
			msg_put_int(contaOrigem, s);
			msg_put_int(montante, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 1 && accessLevel == ACCESS_LEVEL_CLIENTE)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
			break;
		}
		else if (opcao == 0 && accessLevel != ACCESS_LEVEL_CLIENTE)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_SERVICOS_MENU;
			break;
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}
	}
	fprintf(stdout, "\n");
}

/************************************************************************/
/* Mostra o menu dos servicos                                           */
/************************************************************************/
void mostrarMenuServicos()
{
	int opcao = -1;
	char nomeServico[256];
	int NIB;
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);
	consoleEventStatus = CONSOLE_EVENT_NONE;
	fprintf(stdout, "-------MENU PAGAMENTOS E SERVICOS-------\n");
	fprintf(stdout, "0 - Listagem de servicos de pagamento\n");
	if(accessLevel == ACCESS_LEVEL_ENTIDADE)
	{
        fprintf(stdout, "1 - Criar novo servico\n");
        fprintf(stdout, "2 - Voltar ao menu principal\n");
	}
	else
	{
        fprintf(stdout, "1 - Voltar ao menu principal\n");
	}

	while (1)
	{
        memset(buffer, 0, sizeof(char)*255);
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
        sscanf(buffer, "%d", &opcao);

		if (opcao == 0)
		{
			mensagem_s * s = preparePacket(CMSG_LISTAR_SERVICOS_PAGAMENTO);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		if (opcao == 1 && accessLevel == ACCESS_LEVEL_ENTIDADE)
		{
			// (Código, Tamanho da mensagem, Tamanho do token, Token, Nome do serviço, NIB)
			mensagem_s * s = preparePacket(CMSG_CRIAR_NOVO_SERVICO);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Nome do servico:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%255[a-z-A-Z ][^\n]", &nomeServico);
				if (StringValida(nomeServico, 255)) break;
				else fprintf(stdout, "Nome de servico invalido:\n");
			}

			while (1)
			{
				fprintf(stdout, "NIB:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &NIB);
				if (NIB > 0) break;
				else fprintf(stdout, "NIB invalido:\n");
			}

			msg_put_string(nomeServico, s);
			msg_put_int(NIB, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if(opcao == 1 && accessLevel != ACCESS_LEVEL_ENTIDADE)
		{
            consoleEventStatus = CONSOLE_EVENT_SHOW_MAIN_MENU;
            break;
		}
		else if (opcao == 2 && accessLevel == ACCESS_LEVEL_ENTIDADE)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_MAIN_MENU;
			break;
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}
	}
	fprintf(stdout, "\n");
}

/************************************************************************/
/* Mostra o menu das operacoes sobre uma conta                          */
/************************************************************************/
void mostrarMenuConta()
{
	int opcao = -1;
	int accountID = 0;
	int montante = 0;
	int accountIDOrigem = 0;
	int accountIDDestino = 0;
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);

	fprintf(stdout, "0 - Consulta de saldo de uma conta\n");
	fprintf(stdout, "1 - Consulta de movimentos de uma conta\n");
	fprintf(stdout, "2 - Movimento entre contas proprias\n");
	fprintf(stdout, "3 - Apagar conta\n");
	fprintf(stdout, "4 - Voltar ao menu contas\n");

	while (1)
	{
        memset(buffer, 0, sizeof(char)*255);
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
        sscanf(buffer, "%d", &opcao);
		if (opcao == 0)
		{
			mensagem_s * s = preparePacket(CMSG_CONSULTAR_SALDO);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Numero de conta:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &accountID);
				if (accountID > 0) break;
				else fprintf(stdout, "Numero de conta invalida:\n");
			}

			msg_put_int(accountID, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 1)
		{
			mensagem_s * s = preparePacket(CMSG_CONSULTAR_MOVIMENTOS);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Numero de conta:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &accountID);
				if (accountID > 0) break;
				else fprintf(stdout, "Numero de conta invalida:\n");
			}

			msg_put_int(accountID, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 2)
		{
			mensagem_s * s = preparePacket(CMSG_MOVIMENTO_ENTRE_CONTAS);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Montante a mover:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &montante);
				if (montante > 0) break;
				else fprintf(stdout, "Montante inserdido e invalido:\n");
			}

			while (1)
			{
				fprintf(stdout, "Numero de conta de origem:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &accountIDOrigem);
				if (accountIDOrigem > 0) break;
				else fprintf(stdout, "Numero de conta invalida:\n");
			}

			while (1)
			{
				fprintf(stdout, "Numero de conta de destino:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &accountIDDestino);
				if (accountIDDestino > 0 && accountIDOrigem != accountIDDestino) break;
				else fprintf(stdout, "Numero de conta invalida:\n");
			}
			msg_put_int(montante, s);
			msg_put_int(accountIDOrigem, s);
			msg_put_int(accountIDDestino, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 3)
		{
			mensagem_s * s = preparePacket(CMSG_APAGAR_CONTA);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Numero de conta de origem:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &accountIDOrigem);
				if (accountIDOrigem > 0) break;
				else fprintf(stdout, "Numero de conta invalida:\n");
			}

			while (1)
			{
				fprintf(stdout, "Numero de conta de destino para transferir o saldo da sua conta:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%d", &accountIDDestino);
				if (accountIDDestino > 0 && accountIDOrigem != accountIDDestino) break;
				else fprintf(stdout, "Numero de conta invalida:\n");
			}

			msg_put_int(accountIDOrigem, s);
			msg_put_int(accountIDDestino, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 4)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_CONTAS_MENU;
			break;
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}
	}
	fprintf(stdout, "\n");
}
/************************************************************************/
/* Mostra o menu administrativo                                         */
/************************************************************************/
void mostrarMenuAdmin()
{
	int opcao = -1;
	char utilizador[32];
	char buffer[255];

	memset(buffer, 0, sizeof(char)*255);
	consoleEventStatus = CONSOLE_EVENT_NONE;
	fprintf(stdout, "-------MENU ADMINISTRACAO-------\n");
	fprintf(stdout, "0 - Listar todos os clientes e respectivos saldos\n");
	fprintf(stdout, "1 - Listar todas as entidades e respectivos saldos\n");
	fprintf(stdout, "2 - Valor total depositado no banco\n");
	fprintf(stdout, "3 - Congelar conta\n");
	fprintf(stdout, "4 - Descongelar conta\n");
	fprintf(stdout, "5 - Voltar ao menu principal\n");

	while (1)
	{
		fprintf(stdout, "Opcao:");
		fgets (buffer, 255, stdin);
        sscanf(buffer, "%d", &opcao);

		if (opcao == 0)
		{
			mensagem_s * s = preparePacket(CMSG_LISTAR_CLIENTES);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 1)
		{
			mensagem_s * s = preparePacket(CMSG_LISTAR_ENTIDADES);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 2)
		{
			mensagem_s * s = preparePacket(CMSG_VALOR_DEPOSITADO_BANCO);
			msg_put_int(token, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 3)
		{
			mensagem_s * s = preparePacket(CMSG_CONGELAR_CONTA);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Nome de utilizador:");
                fgets (buffer, 255, stdin);
                sscanf(buffer, "%31[^\n]s", &utilizador);
				if (StringValida(utilizador, 32)) break;
				else fprintf(stdout, "Nome de utilizador:\n");
			}

			msg_put_string(utilizador, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 4)
		{

			//(Código, Tamanho da mensagem, Tamanho do token, Token, Tamanho do nome de utilizador, Utilizador a ser congelado)
			mensagem_s * s = preparePacket(CMSG_DESCONGELAR_CONTA);
			msg_put_int(token, s);

			while (1)
			{
				fprintf(stdout, "Nome de utilizador:");
				fgets (buffer, 255, stdin);
                sscanf(buffer, "%31[^\n]s", &utilizador);
				if (StringValida(utilizador, 32)) break;
				else fprintf(stdout, "Nome de utilizador:\n");
			}

			msg_put_string(utilizador, s);
			finalizePacket(s);

			if (ConnectAndSendRequest(s))
			{
				break;
			}
		}
		else if (opcao == 5)
		{
			consoleEventStatus = CONSOLE_EVENT_SHOW_MAIN_MENU;
			break;
		}
		else
		{
			fprintf(stdout, "Opcao invalida\n");
		}
	}
}

/************************************************************************/
/* Handling dos eventos do menu                                         */
/************************************************************************/
void updateMenu()
{
	switch (consoleEventStatus)
	{
	case CONSOLE_EVENT_SHOW_AUTH_MENU: mostrarMenuAuth(); break;
	case CONSOLE_EVENT_SHOW_MAIN_MENU: mostrarMenuPrincipal(); break;
	case CONSOLE_EVENT_SHOW_CONTAS_MENU: mostrarMenuContas(); break;
	case CONSOLE_EVENT_SHOW_ADMIN_MENU: mostrarMenuAdmin(); break;
	case CONSOLE_EVENT_SHOW_CONTA_MENU: mostrarMenuConta(); break;
	case CONSOLE_EVENT_SHOW_SERVICOS_MENU: mostrarMenuServicos(); break;
	case CONSOLE_EVENT_SHOW_SERVICO_MENU: mostrarMenuServico(); break;
	default: break;
	}
}
