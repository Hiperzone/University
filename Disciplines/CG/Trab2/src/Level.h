#include "Common.h"

#ifndef LEVEL_H
#define LEVEL_H


/**
 *Contem m�todos que permitem carregar um n�vel e desenha-lo no ecr�.
 *
 */
class Level
{
public:
	Level(string filename);
	~Level(void);

	void Update(int time);
	int LoadLevel();
	void ReadColisionContent(char *buffer, int row);
	void ReadModelMapContent(char *buffer, int row);
	int getPlayerStartX();
	int getPlayerStartY();
	int getTileSizeX();
	int getTileSizeY();

	bool Collides(float x, float y);

	int **getColisionMap();
	Object ***getModelMap();

	void FreeResources();

	std::map<string, int>& getTextureList();
	std::map<string, Material*>& getMaterials();

	Model *getModel(int id);

	void signalEventToNearByNodes(float x, float y, EVENT_TRIGGER event);

	int getNumLights();

	Light * getLight(int l);

private:
	string filename;
	int levelMode;
	int **colisionMap;
	Object ***modelMap;
	Model *models[256];
	int size_x;
	int size_y;
	int start_x;
	int start_y;
	Light *lights[8];
	int numLights;

	std::map<string, int> textures;
	std::map<string, Material*> material;
};
	
#endif

