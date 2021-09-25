#include <cmath>
#include <iomanip>
#include <iostream>
#include <math.h>
#include <random>
#include <vector>

void test();

class DistributionPair
{
  public:
    DistributionPair(std::uint32_t minValue, std::uint32_t maxValue)
        : minValue(minValue), maxValue(maxValue), count(0)
    {
    }

    std::uint32_t minValue;
    std::uint32_t maxValue;
    std::uint32_t count;
};

std::vector<DistributionPair>
generateUniformDistribution(std::uint32_t howMany, std::uint32_t min,
                            std::uint32_t max, std::uint8_t numberBins)
{
    std::vector<DistributionPair> bins;
    std::uint32_t maxDouble = numberBins * 2;
    for (std::uint32_t i = min; i < maxDouble; i += 2)
    {
        bins.push_back(DistributionPair{i, i + 1});
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
        bins.push_back(DistributionPair{currMin, currMin});
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
        bins.push_back(DistributionPair{i, i});
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
                  << bin.maxValue
                  << "] : " << std::string(bin.count / scale, '*') << std::endl;
    }
}

int main()
{

    auto uniform = generateUniformDistribution(100000, 0, 79, 40);
    plotDistribution("--- Uniform ---", uniform, 80);

    auto normal = generateNormalDistribution(100000, 50, 5, 40);
    plotDistribution("--- Normal ---", normal, 80);

    auto poisson = generatePoissonDistribution(100000, 6, 40);
    plotDistribution("--- Poisson ---", poisson, 80);
    test();
}

// ------------------------------------------------------------------
//
// Testing Code
//
// ------------------------------------------------------------------
#include <functional>
#include <numeric>
#include <string>

namespace testing::detail
{
using namespace std::string_literals;

using Bins = std::vector<std::pair<std::uint32_t, std::uint32_t>>;
using DistFunc = std::function<std::vector<DistributionPair>()>;

#define CS3460_ASSERT_EQ(expected, actual, message)                            \
    if (expected != actual)                                                    \
    {                                                                          \
        fail(message, "[ Expected", expected, "but got", actual, "]");         \
        return;                                                                \
    }

#define CS3460_CASE(x)                                                         \
    [] { return x; };                                                          \
    std::cout << " Case " << #x << "\n";

template <typename Message> void failInternal(const Message& message)
{
    std::cout << message << " ";
}

template <typename Message, typename... Messages>
void failInternal(const Message& message, const Messages&... messages)
{
    failInternal(message);
    failInternal(messages...);
}

template <typename... Messages> void fail(const Messages&... messages)
{
    std::cout << "  Assertion failed: ";
    failInternal(messages...);
    std::cout << "\n";
}

Bins generateBins(const std::uint32_t min, const std::uint32_t max,
                  const std::uint8_t numberBins)
{
    const auto binRange = (max - min) / numberBins;
    auto minBin = min;
    auto maxBin = min + binRange;

    Bins results(numberBins);
    for (std::uint8_t bin = 0u; bin < numberBins; bin++)
    {
        results[bin] = {minBin, maxBin};
        minBin = maxBin + 1;
        maxBin = minBin + binRange;
    }

    return results;
}

void returnsTheExpectedBins(const DistFunc& func, const Bins& bins)
{
    const auto result = func();
    CS3460_ASSERT_EQ(bins.size(), result.size(), "Wrong number of bins");
    for (auto i = 0u; i < bins.size(); i++)
    {
        CS3460_ASSERT_EQ(bins[i].first, result[i].minValue,
                         "Wrong minimum value for bin "s + std::to_string(i));
        CS3460_ASSERT_EQ(bins[i].second, result[i].maxValue,
                         "Wrong maximum value for bin "s + std::to_string(i));
    }
}

void hasTheCorrectTotalAcrossAllBins(const DistFunc& func,
                                     const std::uint32_t howMany)
{
    const auto result = func();
    const auto add_counts = [](std::uint32_t total, const DistributionPair& bin)
    { return total + bin.count; };
    CS3460_ASSERT_EQ(
        howMany,
        std::accumulate(result.cbegin(), result.cend(), 0u, add_counts),
        "Wrong number of elements across all bins");
}

void testUniformDistribution()
{
    std::cout << "Testing generateUniformDistribution\n";
    auto func = CS3460_CASE(generateUniformDistribution(100000, 0, 79, 40));
    returnsTheExpectedBins(func, generateBins(0, 79, 40));
    hasTheCorrectTotalAcrossAllBins(func, 100000);
}

void testNormalDistribution()
{
    std::cout << "Testing generateNormalDistribution\n";
    auto func = CS3460_CASE(generateNormalDistribution(100000, 50, 5, 40));
    returnsTheExpectedBins(func, generateBins(30, 69, 40));
    hasTheCorrectTotalAcrossAllBins(func, 100000);
}

void testPoissonDistribution()
{
    std::cout << "Testing generatePoissonDistribution\n";
    auto func = CS3460_CASE(generatePoissonDistribution(100000, 6, 40));
    returnsTheExpectedBins(func, generateBins(0, 39, 40));
    hasTheCorrectTotalAcrossAllBins(func, 100000);
}
} // namespace testing::detail

void test()
{
    using namespace testing::detail;

    testUniformDistribution();
    testNormalDistribution();
    testPoissonDistribution();

    std::cout << "\n\n";
}