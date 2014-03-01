#include "Common.h"

#ifndef NORMALCAMERA_H
#define NORMALCAMERA_H

/**
 * Classe que prepara uma camera com projec��o ortonormada(paralela).
 *
 */
class MiniMapCamera : public Camera
{
public:
	MiniMapCamera(void);
	~MiniMapCamera(void);

	void prepareCamera();
};

#endif
