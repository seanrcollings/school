#include "./RenderConsole.hpp"

#include "rlutil.h"

#include <iostream>
#include <thread>

void RenderConsole::render(LifeSimulator& simulation)
{
    while (true)
    {
        rlutil::cls();
        rlutil::hidecursor();
        rlutil::setColor(rlutil::BLUE);
        auto simHeight = simulation.getSizeY();
        auto simWidth = simulation.getSizeX();

        for (std::uint8_t i = 0; i < simHeight; ++i)
        {

            for (std::uint8_t j = 0; j < simWidth; j++)
            {
                bool cell = simulation.getCell(j, i);
                rlutil::locate(j, i);
                rlutil::setChar(cell ? 'X' : ' ');
            }
            std::cout << '\n';
        }
        rlutil::resetColor();
        rlutil::showcursor();
        std::this_thread::sleep_for(std::chrono::milliseconds(1000));
        simulation.update();
    }
}