#include "LifeSimulator.hpp"
#include "PatternAcorn.hpp"
#include "PatternBlinker.hpp"
#include "PatternBlock.hpp"
#include "PatternGlider.hpp"
#include "PatternGosperGliderGun.hpp"
#include "RenderConsole.hpp"
#include "rlutil.h"

#include <array>
#include <iostream>
#include <thread>

const std::uint8_t MIN_WIDTH = 80;
const std::uint8_t MIN_HEIGHT = 30;

int main()
{
    auto width = static_cast<std::uint8_t>(rlutil::tcols());
    auto height = static_cast<std::uint8_t>(rlutil::trows());
    if (width < MIN_WIDTH || height < MIN_HEIGHT)
    {
        std::cout << "Please increase size of terminal window" << std::endl;
        return 1;
    }

    LifeSimulator simul{ width, height };
    PatternBlock blockPattern;
    PatternBlinker blinkerPattern;
    PatternAcorn acornPattern;
    PatternGlider gliderPattern;
    PatternGosperGliderGun gliderGunPattern;
    simul.insertPattern(gliderGunPattern, 0, 2);
    simul.insertPattern(blinkerPattern, 40, 2);
    simul.insertPattern(gliderPattern, 50, 2);
    simul.insertPattern(blockPattern, 60, 2);
    simul.insertPattern(acornPattern, 60, 10);
    RenderConsole renderer;

    for (int i = 0; i < 800; i++)
    {
        renderer.render(simul);
        std::this_thread::sleep_for(std::chrono::milliseconds(10));
        simul.update();
    }

    std::cout << std::endl
              << "Goodbye!" << std::endl;
    return 0;
}