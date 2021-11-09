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

int main()
{
    LifeSimulator simul{
        static_cast<std::uint8_t>(rlutil::tcols()),
        static_cast<std::uint8_t>(rlutil::trows())
    };
    PatternBlock blockPattern;
    PatternBlinker blinkerPattern;
    PatternAcorn acornPattern;
    PatternGlider gliderPattern;
    PatternGosperGliderGun gliderGunPattern;
    // simul.insertPattern(blockPattern, 10, 5);
    simul.insertPattern(blinkerPattern, 40, 2);
    // simul.insertPattern(acornPattern, 60, 10);
    // simul.insertPattern(gliderPattern, 100, 30);
    simul.insertPattern(gliderGunPattern, 2, 2);
    RenderConsole renderer;

    while (true)
    {
        renderer.render(simul);
        std::this_thread::sleep_for(std::chrono::milliseconds(10));
        simul.update();
    }

    return 0;
}