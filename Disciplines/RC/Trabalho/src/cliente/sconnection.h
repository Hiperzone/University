#include "../shared/msg_streamer.h"

#ifndef _SCONNECTION_H_
#define _SCONNECTION_H_


int ConnectAndSendRequest(mensagem_s * s);
extern int sock;
void RecvData(int socket);
#endif
