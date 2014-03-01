#include "MiniMapCamera.h"

MiniMapCamera::MiniMapCamera(void)
{
}

MiniMapCamera::~MiniMapCamera(void)
{
}

/**
 * Prepara a camera em modo topo
 */
void MiniMapCamera::prepareCamera()
{
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
	float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);
	float cS = (cX > cY ? cX : cY);
	glOrtho(0, cS, 0, cS, -cS, cS);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}
