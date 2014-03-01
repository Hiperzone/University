
#include "mensagens.h"
#include <time.h>
#ifndef _SERVIDOR_H_
#define _SERVIDOR_H_

extern time_t currTime;
void sendData(int socket, mensagem_s * s);
void disconnect(int socket);
#endif
