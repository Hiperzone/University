//
//  FollowCamera.h
//  CG - Trab 2
//
//  Created by Marcus Santos on 13/06/13.
//  Copyright (c) 2013 Marcus Santos. All rights reserved.
//

#include "Common.h"

#ifndef __CG___Trab_2__FollowCamera__
#define __CG___Trab_2__FollowCamera__

#include <iostream>

class FirsPersonCamera : public Camera
{
private:
    int angle;
public:
    FirsPersonCamera();
    ~FirsPersonCamera();
    
    void prepareCamera();
    
};

#endif /* defined(__CG___Trab_2__FollowCamera__) */
