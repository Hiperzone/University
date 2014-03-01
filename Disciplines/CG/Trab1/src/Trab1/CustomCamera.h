#include "Common.h"

#ifndef CUSTOMCAMERA_H
#define CUSTOMCAMERA_H
class CustomCamera : public Camera
{
public:
	CustomCamera(void);
	~CustomCamera(void);

	void prepareCamera();
};

#endif


