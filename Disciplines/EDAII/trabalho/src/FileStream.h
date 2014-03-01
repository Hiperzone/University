#include "tries.h"
#include "LinkedList.h"

#ifndef FILESTREAM_H
#define FILESTREAM_H

#define DATAFILE "data.dat"


void guardarFoto(Foto *foto);
void carregarFotos(char *filename, Trie *trie, List *list);
#endif
