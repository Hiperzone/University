#include "../shared/common.h"
#ifdef _WIN32
#include "win/unistd.h"
#endif

#include "cliente.h"
#include "consola.h"

int loginState = LOGIN_STATE_NOT_LOGGEDIN;
int consoleEventStatus = CONSOLE_EVENT_SHOW_AUTH_MENU;
int accessLevel = ACCESS_LEVEL_CLIENTE;
int token = 0xFFFFFFFF;

/************************************************************************/
/* Funcao principal                                                     */
/************************************************************************/
main() 
{
	initializeErrorTable();
	while (1)
	{
		updateMenu();
	}
}
