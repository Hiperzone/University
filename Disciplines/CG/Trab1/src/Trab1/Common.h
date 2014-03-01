
#ifndef COMMON_H
#define COMMON_H

typedef struct
{
	float x;
	float y;
	float z;

}vector3;

typedef struct
{
	float r, g, b;
}color;

#define PI 3.1415926535897932384626433832795f

//constantes temporarias
#define GAME_STATE_NOT_PLAYING 2
#define GAME_STATE_PLAYING 3
#define GAME_STATE_PLAYER_WON 4

#define MAP_TILE_SIZE 0.2

//eventos

enum GameEvents
{
	EVENT_NONE = 0,
	EVENT_PLAYER_PLAYING = 1,
	EVENT_PLAYER_NOT_PLAYING,
	EVENT_PLAYER_REACHED_RED,
	EVENT_PLAYER_REACHED_GREEN,
	EVENT_PLAYER_REACHED_BLUE,
	EVENT_PLAYER_FINISHED_LEVEL,
	EVENT_PLAYER_WON
};

#define UI_EVENT_NONE 0
#define UI_EVENT_START_PLAYING 1
#define UI_EVENT_QUIT 2

//erros
#define ERROR_NONE                 0
#define ERROR_FAILED_TO_LOAD_LEVEL 1

#define LEVEL_MODE_A_MONEY_COULD_DO_IT 0
#define LEVEL_MODE_USE_BRAIN 1

enum LevelPath
{
	LEVEL_PATH_PASSABLE = 0,
	LEVEL_PATH_BLOCKED,
	LEVEL_PATH_OBJECTIVE_RED,
	LEVEL_PATH_OBJECTIVE_BLUE,
	LEVEL_PATH_OBJECTIVE_GREEN,
	LEVEL_PATH_PLAYER_START
};

#include <stdio.h>
#include <stdlib.h>
#include <istream>
#include <fstream>
#include <iostream>
#include <string>
#include <list>
#include <assert.h>
using namespace std;

#ifdef WIN32
#include "windows.h"
#include <GL/GL.h>
#include <GL/glut.h>

#endif

#include "Player.h"
#include "Camera.h"
#include "IsometricCamera.h"
#include "NormalCamera.h"
#include "Level.h"
#include "Game.h"

/**
 * Acesso global a metodos e variaveis partilhadas pelo
 * projecto.
 */
class Common
{
public:
	Common();
	~Common();
	static float getClippingFromTile(int tile, bool upperRound);
	static int roundToTile(float position);
	static float tileToPos(int tile);
	static Game * getGame();
private:
	static Game * game;
};

#endif
