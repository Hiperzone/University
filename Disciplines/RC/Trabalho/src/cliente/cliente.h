#include "mensagens.h"
#ifndef _CLIENTE_H_
#define _CLIENTE__H_

enum LOGIN_STATE
{
	LOGIN_STATE_NOT_LOGGEDIN,
	LOGIN_STATE_LOGGEDIN
};

/************************************************************************/
/* Nivel de acesso do utilizador                                        */
/************************************************************************/
enum ACCESS_LEVEL
{
	ACCESS_LEVEL_CLIENTE = 0,
	ACCESS_LEVEL_ENTIDADE,
	ACCESS_LEVEL_ADMIN
};

/************************************************************************/
/* Variaveis globais                                                    */
/************************************************************************/
extern int loginState;
extern int consoleEventStatus;
extern int accessLevel;
extern int token;

#endif
