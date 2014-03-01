#include "Common.h"

#ifndef LEVEL_H
#define LEVEL_H
/**
 *Contem métodos que permitem carregar um nível e desenha-lo no ecrã.
 *
 */
class Level
{
public:
	Level(string filename);
	~Level(void);

	void Update();
	int LoadLevel();
	void ReadContent(char *buffer, int row);
	int getPlayerStartX();
	int getPlayerStartY();
	int getTileSizeX();
	int getTileSizeY();

	bool Collides(float x, float y);

	int **getLevelData();

	void setLevelMode(int m);
	int getLevelMode();
	void UpdateLevelModeAI(float px, float py);


private:
	string filename;
	int levelMode;
	int **data;
	int size_x;
	int size_y;
	int start_x;
	int start_y;
};

#endif

