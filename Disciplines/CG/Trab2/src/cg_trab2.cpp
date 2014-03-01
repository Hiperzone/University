#include "Common.h"

int main(int argc, char **argv)
{
	Common *common = new Common();
	Common::getGame()->SetupGL(argc, argv);
	Common::getGame()->SetupCameras();

	glutMainLoop();
	delete common;
	return 0;
}
