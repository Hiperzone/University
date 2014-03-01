#include "Common.h"
#ifndef __CG___Trab_2__Light__
#define __CG___Trab_2__Light__

#define LIGHT_OMNISCENT 0
#define LIGHT_SPOT 1


class Light 
{
public:
    Light(int type);
	Light();
    ~Light();
    void prepareLight();
    void setLightPosition(float x, float y, float z);
    void setType(int type);
    int getType();
	int getId();

	void readAmbLight(std::ifstream &stream);
	void readDiffLight(std::ifstream &stream);
	void readSpecLight(std::ifstream &stream);
	void readLightPos(std::ifstream &stream);
	void readSpotDir(std::ifstream &stream);
	void readCutOff(std::ifstream &stream);
	void readExponent(std::ifstream &stream);
	void readId(std::ifstream &stream);
	void readType(std::ifstream &stream);
	void readName(std::ifstream &stream);

private:
	// Cor da luz ambiente (RGBA)
	GLfloat cAmb[4];
	// Cor da luz difusa (RGBA)
	GLfloat cDiff[4];
	// Cor da luz especular (RGBA)
	GLfloat cSpec[4];
	// Posicao da luz
	GLfloat lightPos[4];
	// Spotlight direction
	GLfloat spotDir[3];
	// Type of the light
	int type;
	GLfloat exp;
	GLfloat cutOff;
	int id;
	string name;


};
#endif /* defined(__CG___Trab_2__Light__) */
