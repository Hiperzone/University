#include "filtertrie.h"
#include "LinkedList.h"
#include "stdlib.h"
#include "stdio.h"
#include "memory.h"


/************************************************************************/
/* converte um caracter em numero                                       */
/************************************************************************/
int filterTriePos(char *c)
{
    assert(c != NULL);
    return (int)(c[0] - 0x30);
}

/************************************************************************/
/* converte um inteiro(0 a 9) em um caracter                            */
/************************************************************************/
char filterTrieChar(int pos)
{
    return (char)(pos + 0x30);
}

/************************************************************************/
/* converte um digito em uma string                                     */
/************************************************************************/
char * convertDigitToChar(int digit)
{
    char *buffer;
    int i = 10;
    int counter = 0;

    //contar os digitos
    int digitos = digit;
    while(digitos != 0)
    {
        digitos = digitos / 10;
        counter ++;
    }

    buffer = malloc(counter + 1);
    memset(buffer, 0, counter + 1);

    //conversao
    while(digit != 0)
    {
        counter = counter - 1;
        buffer[counter] = filterTrieChar(digit % i);
        digit = digit / 10;
    }
    return buffer;
}


/************************************************************************/
/* Cria um novo no na FilterTrie.                                       */
/************************************************************************/
FilterTrie * filtertrie_newnode()
{
    FilterTrie * node = (FilterTrie*)malloc(sizeof(FilterTrie));

    for(int i = 0; i < FILTER_TRIE_TABLE_SIZE; i++)
    {
        node->table[i] = NULL;
    }

    node->word = false;
    return node;
}

/************************************************************************/
/* Inicializa a FilterTrie                                              */
/************************************************************************/
FilterTrie * filtertrie_initialize()
{
    return filtertrie_newnode();
}

/************************************************************************/
/* Insere um id na FilterTrie                                           */
/************************************************************************/
void filtertrie_insert(FilterTrie * node, char *id)
{
    int pos = filterTriePos(id);
    assert(id != NULL && *id != '\0' && node != NULL && pos >= 0);

    if(node->table[pos] == NULL )
    {
        //criar um novo node para a letra actual
        node->table[pos] = filtertrie_newnode();

        //posicao seguinte e o fim da palavra, parar o avanço e marcar o
        //fim da palavra.
        if(*(id+1) == '\0')
        {
            node->table[pos]->word = 1;
            return;
        }
    }
    else
    {
        if(*(id+1) == '\0')
        {
            node->table[pos]->word = 1;
            return;
        }
    }
    //letra seguinte
    filtertrie_insert(node->table[pos], id+1);
}

/************************************************************************/
/* Procura um id na FilterTrie                                          */
/************************************************************************/
bool filtertrie_find(FilterTrie  * node, char *id)
{
    int pos = filterTriePos(id);
    assert(id != NULL && *id != '\0' && node != NULL && pos >= 0);

    //apenas procurar se o proximo char nao for o fim da palavra
    if(*(id+1) != '\0' && node->table[pos] != NULL)
    {
        return filtertrie_find(node->table[pos], id+1);
    }
    //e a ultima letra da palavra, verificar se esta marcado como uma palavra.
    else if( node->table[pos] != NULL && node->table[pos]->word == 1 )
    {
        return true;
    }

    //nao encontrou
    return false;
}


/************************************************************************/
/* Destroi a FilterTrie, libertando a memoria ocupada                   */
/************************************************************************/
FilterTrie * filtertrie_destroy(FilterTrie * node)
{
    assert(node != NULL);
    for(int i = 0; i < FILTER_TRIE_TABLE_SIZE; i++)
    {
        if(node->table[i] != NULL)
        {
            node->table[i] = filtertrie_destroy(node->table[i]);
        }
    }

    free(node);
    return NULL;
}




