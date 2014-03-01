
#include "Light.h"

Light::Light(int type)
{
    setType(type);
}

Light::Light()
{
	setType(LIGHT_OMNISCENT);
}

Light::~Light()
{
    
}

/************************************************************************/
/* Prepara as propriedades da luz.                                      */
/************************************************************************/
void Light::prepareLight()
{
	//activa a luz apropriada
	glEnable(GL_LIGHT0 + id);
    // atribuição da luz ambiente
    glLightfv(GL_LIGHT0 + id,GL_AMBIENT, cAmb);
    // atribuição da luz difusa
    glLightfv(GL_LIGHT0 + id,GL_DIFFUSE, cDiff);
    // atribuição da luz especular
    glLightfv(GL_LIGHT0 + id,GL_SPECULAR, cSpec);
	glLightfv(GL_LIGHT0 + id, GL_POSITION, lightPos);
    
    if(type == LIGHT_SPOT)
    {
        // Definig spotlight attributes
        glLightf(GL_LIGHT0 + id,GL_SPOT_CUTOFF, cutOff);
        glLightf(GL_LIGHT0 + id,GL_SPOT_EXPONENT, exp);
        glLightfv(GL_LIGHT0 + id,GL_SPOT_DIRECTION, spotDir); 
    }
}

/************************************************************************/
/* Define a posicao da luz                                              */
/************************************************************************/
void Light::setLightPosition(float x, float y, float z)
{
    lightPos[0] = x;
    lightPos[1] = y;
    lightPos[2] = z;
    lightPos[3] = 1.0f;
	glLightfv(GL_LIGHT0 + id, GL_POSITION, lightPos);
}

void Light::setType(int type)
{
    this->type = type;
}

int Light::getType()
{
    return type;
}

void Light::readAmbLight( std::ifstream &stream )
{
	stream >> cAmb[0] >> cAmb[1] >> cAmb[2];
	cAmb[3] = 1.0;
}

void Light::readDiffLight( std::ifstream &stream )
{
	stream >> cDiff[0] >> cDiff[1] >> cDiff[2];
	cDiff[3] = 1.0;
}

void Light::readSpecLight( std::ifstream &stream )
{
	stream >> cSpec[0] >> cSpec[1] >> cSpec[2];
	cSpec[3] = 1.0;
}

void Light::readLightPos( std::ifstream &stream )
{
	stream >> lightPos[0] >> lightPos[1] >> lightPos[2];
	lightPos[3] = 1.0;
}

void Light::readSpotDir( std::ifstream &stream )
{
	stream >> spotDir[0] >> spotDir[1] >> spotDir[2];
}

void Light::readCutOff( std::ifstream &stream )
{
	stream >> cutOff;
}

void Light::readExponent( std::ifstream &stream )
{
	stream >> exp;
}

void Light::readName( std::ifstream &stream )
{
	stream >> name;
}

void Light::readType( std::ifstream &stream )
{
	stream >> type;
}

void Light::readId( std::ifstream &stream )
{
	stream >> id;
}

int Light::getId()
{
	return id;
}

