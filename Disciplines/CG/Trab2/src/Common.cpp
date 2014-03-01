#include "Common.h"

Game * Common::game;

Common::Common()
{
	Common::game = new Game(800,800);
}

Common::~Common()
{
	if(Common::game)
		delete Common::game;
}

/**
 * Converte uma coordenada do mundo opengl para uma
 * coordwar margens de erro.
 */
int Common::roundToTile(float position)
{
	int tile = floor(position + 0.05);
	return tile;
}

/**
 * Obtem a instancia do jogo actual.
 */
Game *Common::getGame()
{
	return game;
}

/**
 * Converte uma posicao na array em coordenadas do
 * mundo opengl.
 * E necessario fazer ajustamentos porque as coordenadas 
 * convertidas tem como origem a posicao (0,0).
 */
float Common::tileToPos(int tile)
{
	return tile * MAP_TILE_SIZE;
}

/**
 * Obtem as coordenadas "ideais" para o clipping que
 * sera usado no glortho baseado no tamanho do nivel.
 * Permite arredondar para o numero inteiro acima.
 */
float Common::getClippingFromTile(int tile, bool upperRound)
{
	if(upperRound)
		return ceil(tile * 1.0 + 0.5);
	else
		return tile * 1.0;
}

/************************************************************************/
/* Verifica se uma coordenada convertida em tile esta dentro do nivel   */
/************************************************************************/
bool Common::checkMapBounds(int x, int y)
{
	if(getGame() && getGame()->getLevel())
	{
		return (x >= 0 && x < getGame()->getLevel()->getTileSizeX() && y >= 0 && y < getGame()->getLevel()->getTileSizeY());
	}
	return false;
}

