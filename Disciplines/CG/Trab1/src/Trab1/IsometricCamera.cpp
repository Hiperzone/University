#include "IsometricCamera.h"


IsometricCamera::IsometricCamera(void)
{
}


IsometricCamera::~IsometricCamera(void)
{
}

/**
 * Prepara a camera em modo isometrico
 *
 */
void IsometricCamera::prepareCamera()
{
	float cX, cY, cS;

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), true);
	cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), true);
	cS = (cX > cY ? cX : cY);
	glOrtho(-cS, cS, -cS, cS, -cS, cS);
	gluLookAt(0.25, 0.25, 0.25, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}
