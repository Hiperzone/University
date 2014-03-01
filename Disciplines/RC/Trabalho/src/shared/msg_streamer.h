
#include "common.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <malloc.h>

#ifndef _MSG_STREAMER_H_
#define _MSG_STREAMER_H_

#define HEADER_SIZE 3
typedef struct mensagem_s
{
	char buffer[65536];
	int size;
	int rpos;
}mensagem_s;

typedef void* (func)(int socket, mensagem_s *msg);

mensagem_s * preparePacket(char msgId);
void finalizePacket(mensagem_s *s);

char msg_recv_byte(mensagem_s *msg);
short msg_recv_short(mensagem_s *msg);
int msg_recv_int(mensagem_s *msg);
char* msg_recv_string(mensagem_s *msg);
__int64 msg_recv_int64(mensagem_s *msg);

void msg_put_byte(char value, mensagem_s *msg);
void msg_put_short(short value, mensagem_s *msg);
void msg_put_int(int value, mensagem_s *msg);
void msg_put_string(char *value, mensagem_s *msg);

void msg_put_byte_at(int pos, char value, mensagem_s * msg);
void msg_put_int_at(int pos, int value, mensagem_s * msg);

int StringValida(char *buffer, int maxlen);

#endif
