#include "tries.h"
#include "LinkedList.h"
#include "stdlib.h"
#include "stdio.h"
#include "memory.h"

/************************************************************************/
/* converte um caracter em numero                                       */
/************************************************************************/
int POS(char *c)
{
    assert(c != NULL);
    if(c[0] == ' ')
    {
        return 26;
    }
    else
    {
        return (int)(c[0] - 0x61);
    }
}

/************************************************************************/
/* converte um inteiro em um caracter                                   */
/************************************************************************/
char CHAR(int pos)
{
    if(pos == 26)
    {
        return ' ';
    }
    else
    {
        return (char)(pos + 0x61);
    }
}

/************************************************************************/
/* Cria um novo no na trie.                                             */
/************************************************************************/
Trie * trie_newnode()
{
    Trie * node = (Trie*)malloc(sizeof(Trie));

    int i = 0;
    for(; i < TRIE_TABLE_SIZE; i++)
    {
        node->table[i] = NULL;
    }
    node->element = NULL;
    node->word = false;
    return node;
}

/************************************************************************/
/* Inicializa a trie                                                    */
/************************************************************************/
Trie * trie_initialize()
{
    return trie_newnode();
}

/************************************************************************/
/* Insere um tema na trie                                               */
/************************************************************************/
void trie_insert(Trie * node, char *tema)
{
    //a palavra nao contem letras ou o no e invalido
    assert(tema != NULL && *tema != '\0' && node != NULL && POS(tema) >= 0);

    if(node->table[POS(tema)] == NULL )
    {
        //criar um novo node para a letra actual
        node->table[POS(tema)] = trie_newnode();

        //posicao seguinte e o fim da palavra, parar o avanço e marcar o
        //fim da palavra.
        if(*(tema+1) == '\0')
        {
            node->table[POS(tema)]->word = 1;
            return;
        }
    }
    else
    {
        if(*(tema+1) == '\0')
        {
            node->table[POS(tema)]->word = 1;
            return;
        }
    }
    //letra seguinte
    trie_insert(node->table[POS(tema)], tema+1);
}

/************************************************************************/
/* Remove um tema da trie                                               */
/************************************************************************/
Trie * trie_remove(Trie * node, char *tema)
{
    int filhos = 0;

    assert(tema != NULL && *tema != '\0' && node != NULL && POS(tema) >= 0);

    if(*(tema) != '\0' && node->table[POS(tema)] != NULL)
    {
        node->table[POS(tema)] = trie_remove(node->table[POS(tema)], tema+1);
    }

    //verificar se tem filhos
    for(int i = 0; i < TRIE_TABLE_SIZE; i++)
    {
        if(node->table[i] != NULL)
            filhos++;
    }

    //nao remover se ouver filhos
    if(filhos > 0)
    {
        return node;
    }
    //remover apenas se coicidir com o fim da palavra e so remover letras
    //dessa palavra se nao pertencerem a outra palavra e nao tiverem filhos
    else if( (filhos == 0 && node->word == 0) || (*tema == '\0' && node->word == 1))
    {
        free(node);
        return NULL;
    }
    //retornar o no intocavel.
    return node;
}

/************************************************************************/
/* Procura um tema na trie                                              */
/************************************************************************/
bool trie_find(Trie  * node, char *tema)
{
    assert(tema != NULL && *tema != '\0' && node != NULL && POS(tema) >= 0);

    //apenas procurar se o proximo char nao for o fim da palavra
    if(*(tema+1) != '\0' && node->table[POS(tema)] != NULL)
    {
        return trie_find(node->table[POS(tema)], tema+1);
    }
    //e a ultima letra da palavra, verificar se esta marcado como uma palavra.
    else if( node->table[POS(tema)] != NULL && node->table[POS(tema)]->word == 1 )
    {
        return true;
    }

    //nao encontrou
    return false;
}

/************************************************************************/
/* Imprime todas as palavras comecadas por um prefixo                   */
/* Nao usar directamente!                                               */
/************************************************************************/
void trie_print_completations(Trie * node, char *buffer, int pos)
{

    assert(buffer != NULL && node != NULL && pos >= 0);

    if(node->word == 1)
    {
        fprintf(stdin, "%.*s\n", pos, buffer);
    }

    //percorrer a tabela.
    for(int i = 0; i < TRIE_TABLE_SIZE; i++)
    {
        if(node->table[i] != NULL)
        {
            //adicionar a letra ao buffer
            buffer[pos] = CHAR(i);
            trie_print_completations(node->table[i], buffer, pos+1);
        }
    }
}

/************************************************************************/
/* Procura todas as palavras comecadas por um prefixo                   */
/************************************************************************/
void trie_completation(Trie * root, char *tema)
{
    char buffer[256]; //tamanho maximo da palavra
    int pos = 0;

    assert(tema != NULL && *tema != '\0' && root != NULL && POS(tema) >= 0);

    //procurar o prefixo.
    Trie *node = root;
    while(*tema != '\0')
    {
        if(node != NULL && node->table[POS(tema)] != NULL)
        {
            node = node->table[POS(tema)];
            buffer[pos] = *tema;
            pos++;
            tema++;
        }
        else
        {
            fputs("nao foram encontrados resultados\n", stderr);
            return;
        }
    }

    if(node)
    {
        trie_print_completations(node, buffer, pos);
    }
}

/************************************************************************/
/* Destroi a trie, libertando a memoria ocupada                         */
/************************************************************************/
Trie * trie_destroy(Trie * node)
{
    assert(node != NULL);
    for(int i = 0; i < TRIE_TABLE_SIZE; i++)
    {
        if(node->table[i] != NULL)
        {
            node->table[i] = trie_destroy(node->table[i]);
        }
    }
    //libertar o espaco ocupado pela linkedlist
    if(node->element != NULL)
    {
        node->element = list_destroy(node->element, false);
    }

    free(node);
    return NULL;
}

/************************************************************************/
/* Insere um elemento num tema                                          */
/************************************************************************/
void trie_insert_element(Trie * node, char *tema, Foto * element)
{
    assert(tema != NULL && *tema != '\0' && node != NULL && POS(tema) >= 0 && element != NULL);

    //apenas procurar se o proximo char nao for o fim da palavra
    if(*(tema+1) != '\0' && node->table[POS(tema)] != NULL)
    {
        trie_insert_element(node->table[POS(tema)], tema+1, element);
    }
    //e a ultima letra da palavra, verificar se esta marcado como uma palavra.
    else if( node->table[POS(tema)] != NULL && node->table[POS(tema)]->word == 1 )
    {
        //A lista ligada ainda nao foi criada, alocar memoria
        if(node->table[POS(tema)]->element == NULL)
        {
            node->table[POS(tema)]->element = list_new();
        }
        list_insert(element, node->table[POS(tema)]->element);
    }
}


/************************************************************************/
/* Imprime o conteudo de um tema                                        */
/************************************************************************/
FilterTrie *trie_print_element_content(Trie * root, char *tema, List *result, FilterTrie *filter, bool save)
{
    Trie *node = root;
    assert(tema != NULL && *tema != '\0' && root != NULL && POS(tema) >= 0);

    //procurar a palavra
    while(*tema != '\0')
    {
        if(node != NULL && node->table[POS(tema)] != NULL)
        {
            node = node->table[POS(tema)];
            tema++;
        }
        else
        {
            return NULL;
        }
    }

    //verificar se encontrou a palavra.
    if(node != NULL && node->word == 1)
    {
       return list_add_to_result(filter, node->element, result, save);
    }

    return NULL;
}

/************************************************************************/
/* Procura uma foto dado uma sequencia de temas                         */
/************************************************************************/
void trie_search_theme_sequence(Trie * root, char *temas, List * result)
{
    int indice = 0;
    char tema[21];
    memset(tema, 0, sizeof(tema));
    bool singleTheme = true;

    FilterTrie *filterResult = NULL;
    while(temas && *temas != '\0')
    {
        if(temas[0] == '|')
        {
            tema[indice] = '\0';
            singleTheme = false;

            //procurar
            filterResult = trie_print_element_content(root, tema, result, filterResult, false);

            memset(tema, 0, sizeof(tema));
            indice = 0;

            if(!filterResult)
            {
                return;
            }
        }
        else
        {
            tema[indice] = temas[0];
            indice++;
        }
        temas++;
    }
    //imprimir a ultima palavra pq nao tem uma barra
    filterResult = trie_print_element_content(root, tema, result, singleTheme ? NULL : filterResult, true);
    filterResult ? filtertrie_destroy(filterResult) : NULL;
    memset(tema, 0, sizeof(tema));
}




