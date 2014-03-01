#include "Foto.h"
#include "stdbool.h"
#include "LinkedList.h"

#include <assert.h>
#ifndef TRIES_H
#define TRIES_H

//converter um char para a posicao
//#define CHAR(pos) ()





#define TRIE_TABLE_SIZE 27
typedef struct Trie Trie;

struct Trie
{
    bool word;
    List *element;
    Trie *table[TRIE_TABLE_SIZE];
};


Trie * trie_initialize();
void trie_insert(Trie * node, char *tema);
void trie_insert_element(Trie * node, char *tema, Foto * element);
FilterTrie *trie_print_element_content(Trie * root, char *tema, List *result, FilterTrie *filter, bool save);
Trie * trie_remove(Trie * node, char *tema);
bool trie_find(Trie  * node, char *tema);
void trie_completation(Trie * root, char *tema);
Trie * trie_destroy(Trie * node);
void trie_search_theme_sequence(Trie * root, char *temas, List * result);
#endif
