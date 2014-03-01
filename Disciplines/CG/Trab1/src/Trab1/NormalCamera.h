#include "Common.h"

#ifndef NORMALCAMERA_H
#define NORMALCAMERA_H

/**
 * Classe que prepara uma camera com projecção ortonormada(paralela).
 *
 */
class NormalCamera : public Camera
{
public:
	NormalCamera(void);
	~NormalCamera(void);

	void prepareCamera();
};

#endif
