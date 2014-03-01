#include "Common.h"

#ifndef PLAYER_H
#define PLAYER_H

/************************************************************************/
/* Classe do jogador                                                    */
/************************************************************************/
class Player
{
public:
	Player(void);
	~Player(void);

	int getObjectiveState();
	void setObjectiveState(int state);
	void setPosition(float x, float y, float z);
	void setPosition(vector3 pos);
	void setColor(float r, float g, float b);
    void setFront(int front);
    int getFront();
	vector3& getPosition();
	color& getColor();
	void Update();
	void UpdateZPosition(int dir);
	bool Collides(float x, float y);
    
private:
	int objectiveState;
    int front;
	vector3 position;
	BOUNDING_BOX bBox;
	color color;
};

#endif
