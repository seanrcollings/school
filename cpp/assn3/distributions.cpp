#include "distributions.hpp"

#include <cmath>
#include <iomanip>
#include <iostream>
#include <math.h>
#include <random>
#include <vector>

std::vector<DistributionPair>
generateUniformDistribution(std::uint32_t howMany, std::uint32_t min,
                            std::uint32_t max, std::uint8_t numberBins)
{
    std::vector<DistributionPair> bins;
    std::uint32_t maxDouble = numberBins * 2;
    for (std::uint32_t i = min; i < maxDouble; i += 2)
    {
        bins.push_back(DistributionPair{ i, i + 1 });
    }

    std::random_device device;
    std::mt19937 engine(device());
    std::uniform_int_distribution<int> dist(min, max);

    for (std::uint32_t i = 0; i < howMany; i++)
    {
        int value = dist(engine);
        bins[(value - min) / 2].count++;
    }

    return bins;
}

std::vector<DistributionPair>
generateNormalDistribution(std::uint32_t howMany, float mean, float stdev,
                           std::uint8_t numberBins)
{
    float min = mean - numberBins / 10 * stdev;
    float max = mean + numberBins / 10 * stdev - 1.f;
    std::vector<DistributionPair> bins;

    for (auto currMin = static_cast<std::uint32_t>(min); currMin <= max;
         currMin++)
    {
        bins.push_back(DistributionPair{ currMin, currMin });
    };

    std::random_device device;
    std::mt19937 engine(device());
    std::normal_distribution<float> dist(mean, stdev);

    for (std::uint32_t i = 0; i < howMany; i++)
    {
        float num = dist(engine);

        if (num > max)
        {
            num = max;
        }
        else if (num < min)
        {
            num = min;
        }

        bins[static_cast<std::uint32_t>(num - min)].count++;
    }

    return bins;
}

std::vector<DistributionPair>
generatePoissonDistribution(std::uint32_t howMany, std::uint8_t howOften,
                            std::uint8_t numberBins)
{
    std::vector<DistributionPair> bins;
    for (std::uint32_t i = 0; i < numberBins; i++)
    {
        bins.push_back(DistributionPair{ i, i });
    }

    std::random_device device;
    std::mt19937 engine(device());
    std::poisson_distribution<> dist(howOften);

    for (std::uint32_t i = 0; i < howMany; i++)
    {
        auto value = static_cast<std::uint32_t>(floor(dist(engine)));
        bins[value].count++;
    }

    return bins;
}

void plotDistribution(std::string title,
                      const std::vector<DistributionPair>& distribution,
                      const std::uint8_t maxPlotLineSize)
{
    std::uint32_t biggestCount = 0;
    for (auto bin : distribution)
    {
        if (bin.count > biggestCount)
        {
            biggestCount = bin.count;
        }
    }

    int scale = biggestCount / maxPlotLineSize;

    std::cout << title << std::endl;
    for (auto bin : distribution)
    {
        std::cout << "[ " << std::setw(2) << bin.minValue << "," << std::setw(4)
                  << bin.maxValue << "] : " << std::string(bin.count / scale, '*')
                  << std::endl;
    }
}