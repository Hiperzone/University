#ifndef FOTO_H
#define FOTO_H

struct Foto
{
    int id;
    char *temas;
    char *nome;
};
typedef struct Foto Foto;


Foto *newFoto();


#endif
