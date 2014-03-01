#include "common.h"
#include "msg_streamer.h"
/************************************************************************/
/* Prepara a mensagem, adicionando o header                             */
/************************************************************************/
mensagem_s * preparePacket(char msgId)
{
	mensagem_s *s = malloc(sizeof(mensagem_s));
	memset(s, 0, sizeof(mensagem_s));
	msg_put_byte(msgId, s);
	msg_put_short(0, s);
	return s;
}

/************************************************************************/
/* Finaliza a construcao da mensagem, corrigindo o tamanho dos dados    */
/************************************************************************/
void finalizePacket(mensagem_s *s)
{
	//size hack! removes header size
	s->size -= 3;
	s->buffer[1] = s->size & 0xFF;
	s->buffer[2] = s->size >> 8 & 0xFF;
}

/************************************************************************/
/* insere um byte na mensagem                                           */
/************************************************************************/
void msg_put_byte(char value, mensagem_s *msg)
{
	assert((msg->size + 1) <= sizeof(msg->buffer));
	msg->buffer[msg->size] = value & 0xFF;
	msg->size += 1;

}

/************************************************************************/
/* Insere um byte na posicao especifica da mensagem                     */
/************************************************************************/
void msg_put_byte_at(int pos, char value, mensagem_s * msg)
{
	assert((pos) <= sizeof(msg->buffer));
	msg->buffer[pos] = value & 0xFF;
}

/************************************************************************/
/* Insere um valor de 16 bits na mensagem                               */
/************************************************************************/
void msg_put_short(short value, mensagem_s *msg)
{
	assert((msg->size + 2) <= sizeof(msg->buffer));
	msg->buffer[msg->size] = value & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 8 & 0xFF;
	msg->size += 1;
}

/************************************************************************/
/* Insere um valor de 32 bits na mensagem                               */
/************************************************************************/
void msg_put_int(int value, mensagem_s *msg)
{
	assert((msg->size + 4) <= sizeof(msg->buffer));
	msg->buffer[msg->size] = value & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 8 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 16 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 24 & 0xFF;
	msg->size += 1;
}

/************************************************************************/
/* Insere um valor de 32 bits na posicao designada                      */
/************************************************************************/
void msg_put_int_at(int pos, int value, mensagem_s * msg)
{
	assert((pos) <= sizeof(msg->buffer));
	msg->buffer[pos] = value & 0xFF;
	msg->buffer[pos + 1] = value >> 8 & 0xFF;
	msg->buffer[pos + 2] = value >> 16 & 0xFF;
	msg->buffer[pos + 3] = value >> 24 & 0xFF;
}

/************************************************************************/
/* Insere um valor de 64 bits                                           */
/************************************************************************/
void msg_put_int64(__int64 value, mensagem_s *msg)
{
	assert((msg->size + 8) <= sizeof(msg->buffer));
	msg->buffer[msg->size] = value & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 8 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 16 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 24 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 32 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 40 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 48 & 0xFF;
	msg->size += 1;
	msg->buffer[msg->size] = value >> 56 & 0xFF;
	msg->size += 1;
}

/************************************************************************/
/* Insere uma string na mensagem                                        */
/************************************************************************/
void msg_put_string(char *value, mensagem_s *msg)
{
	while (*value != '\0')
	{
		msg_put_byte(*value, msg);
		value += 1;
	}
	/*terminador da string*/
	msg_put_byte('\0', msg);
}

/************************************************************************/
/* Obtem um byte a partir da mensagem                                   */
/************************************************************************/
char msg_recv_byte(mensagem_s *msg)
{
	assert((msg->rpos + 1) <= (msg->size + HEADER_SIZE));
	char value = msg->buffer[msg->rpos];
	msg->rpos += 1;
	return value;
}

/************************************************************************/
/* Obtem um valor de 16 bits a partir da mensagem                       */
/************************************************************************/
short msg_recv_short(mensagem_s *msg)
{
	assert((msg->rpos + 2) <= (msg->size + HEADER_SIZE));
	short value = *(short*)&msg->buffer[msg->rpos];
	msg->rpos += 2;
	return value;
}

/************************************************************************/
/* Obtem um valor de 32 bits a partir da mensagem                       */
/************************************************************************/
int msg_recv_int(mensagem_s *msg)
{
	assert((msg->rpos + 4) <= (msg->size + HEADER_SIZE));
	int value = *(int*)&msg->buffer[msg->rpos];
	msg->rpos += 4;
	return value;
}

/************************************************************************/
/* Obtem um valor de 64 bits a partir da mensagem                       */
/************************************************************************/
__int64 msg_recv_int64(mensagem_s *msg)
{
	assert((msg->rpos + 8) <= (msg->size + HEADER_SIZE));
	__int64 value = *(__int64*)&msg->buffer[msg->rpos];
	msg->rpos += 8;
	return value;
}

/************************************************************************/
/* Obtem uma string a partir da mensagem                                */
/************************************************************************/
char *msg_recv_string(mensagem_s *msg)
{
	char *value = msg->buffer;
	value += msg->rpos;
	char *buffer = malloc(sizeof(char)* 256);
	memset(buffer, 0, sizeof(char)* 256);

	int i = 0;
	while (*value != '\0')
	{
		buffer[i] = msg_recv_byte(msg);
		value += 1;
		i += 1;
	}
	/*terminador da string*/
	buffer[i] = msg_recv_byte(msg);
	return buffer;
}

/************************************************************************/
/* Valida uma string dado um tamanho maximo nao inclusive               */
/************************************************************************/
int StringValida(char *buffer, int maxlen)
{
	//string vazia
	if (strlen(buffer) == 0)
	{
		return 0;
	}
	else if (strlen(buffer) > (unsigned int)maxlen)
	{
		return 0;
	}
	return 1;
}
