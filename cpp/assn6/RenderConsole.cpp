#include "./RenderConsole.hpp"

#include "rlutil.h"

#include <iostream>
#include <signal.h>

void teardown(int signum)
{
    rlutil::resetColor();
    rlutil::showcursor();
    exit(signum);
}

void RenderConsole::setup(const LifeSimulator& simulation)
{
    lastState.resize(simulation.getSizeY());
    for (auto i = lastState.begin(); i != lastState.end(); i++)
    {
        i->resize(simulation.getSizeX());
    }

    rlutil::cls();
    rlutil::hidecursor();
    rlutil::setColor(rlutil::BLUE);
    signal(SIGINT, teardown);
}

void RenderConsole::render(const LifeSimulator& simulation)
{
    if (firstRender)
    {
        setup(simulation);
        firstRender = false;
    }

    auto simHeight = simulation.getSizeY();
    auto simWidth = simulation.getSizeX();

    for (std::uint8_t i = 0; i < simHeight; ++i)
    {

        for (std::uint8_t j = 0; j < simWidth; j++)
        {
            bool cell = simulation.getCell(j, i);
            bool previousCell = lastState[i][j];
            if (cell != previousCell)
            {
                rlutil::locate(j + 1, i + 1);
                rlutil::setChar(cell ? 'X' : ' ');
                lastState[i][j] = cell;
            }
        }
    }
}