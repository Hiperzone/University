#include "NormalCamera.h"


NormalCamera::NormalCamera(void)
{
}


NormalCamera::~NormalCamera(void)
{
}

/**
 * Prepara a camera em modo nao isometrico.
 *
 */
void NormalCamera::prepareCamera()
{
	float cX, cY, cS;

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
	cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);
	cS = (cX > cY ? cX : cY) + 0.2f;
	glOrtho(-cS, cS, -cS, cS, -cS, cS);
	
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

}
