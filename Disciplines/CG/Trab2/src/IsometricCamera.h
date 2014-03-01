#include "Common.h"

#ifndef ISOMETRICCAM_H
#define ISOMETRICCAM_H

/**
 * Classe que prepara uma camera com projec��o isom�trica.
 *
 */
class IsometricCamera : public Camera
{
public:
	IsometricCamera(void);
	~IsometricCamera(void);

	void prepareCamera();
};
#endif

