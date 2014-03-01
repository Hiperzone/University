#ifndef _CONSOLA_H_
#define _CONSOLA_H_

/************************************************************************/
/* Estados da consola                                                   */
/************************************************************************/
enum CONSOLE_STATE
{
	CONSOLE_EVENT_NONE = 0,
	CONSOLE_EVENT_SHOW_AUTH_MENU,
	CONSOLE_EVENT_SHOW_MAIN_MENU,
	CONSOLE_EVENT_SHOW_CONTAS_MENU,
	CONSOLE_EVENT_SHOW_CONTA_MENU,
	CONSOLE_EVENT_SHOW_SERVICOS_MENU,
	CONSOLE_EVENT_SHOW_SERVICO_MENU,
	CONSOLE_EVENT_SHOW_ADMIN_MENU
};

void mostrarMenuAuth();
void updateMenu();
void mostrarMenuPrincipal();
void mostrarMenuContas();
void mostrarMenuConta();
void mostrarMenuServicos();
void mostrarMenuAdmin();
void mostrarMenuServico();

#endif
