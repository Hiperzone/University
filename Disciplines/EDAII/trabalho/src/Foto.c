#include "Foto.h"
#include "stdlib.h"

/************************************************************************/
/* Cria uma nova foto                                                   */
/************************************************************************/
Foto *newFoto()
{
    static int id = 1;
    Foto *nova_foto = malloc(sizeof(Foto));
    nova_foto->nome = NULL;
    nova_foto->temas = NULL;
    nova_foto->id = id;
    id++;

    return nova_foto;
}
