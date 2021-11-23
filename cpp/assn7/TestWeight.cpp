#include "weight.hpp"

#include "gtest/gtest.h"
#include <cstdint>
#include <ratio>

constexpr auto kEpsilon = 1E-6;

using TestWeight = usu::weight<std::ratio<2, 1>, std::uint32_t>;
using BigWeight = usu::weight<std::ratio<4, 1>, std::uint32_t>;
using SmallWeight = usu::weight<std::ratio<1, 3>, std::uint32_t>;

int main(int argc, char* argv[])
{
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

TEST(Weight_Constructor, DefaultInitializesCountToZero)
{
    TestWeight weight;

    EXPECT_EQ(0u, weight.count());
}

TEST(Weight_Constructor, OverloadedConstructorInitializesCount)
{
    TestWeight weight1(12);
    EXPECT_EQ(12u, weight1.count());

    TestWeight weight2(44);
    EXPECT_EQ(44u, weight2.count());
}

TEST(Weight_Operator, CanAddOtherWeight)
{
    TestWeight weight1(11);
    TestWeight weight2(4);

    TestWeight result = weight1 + weight2;
    EXPECT_EQ(15u, result.count());
    EXPECT_EQ(11u, weight1.count());
    EXPECT_EQ(4u, weight2.count());

    result = weight1 + TestWeight(0);
    EXPECT_EQ(11u, result.count());
    EXPECT_EQ(11u, weight1.count());
}

TEST(Weight_Operator, CanSubtractOtherWeight)
{
    TestWeight weight1(11);
    TestWeight weight2(4);

    TestWeight result = weight1 - weight2;
    EXPECT_EQ(7u, result.count());
    EXPECT_EQ(11u, weight1.count());
    EXPECT_EQ(4u, weight2.count());

    result = weight1 - TestWeight(0);
    EXPECT_EQ(11u, result.count());
    EXPECT_EQ(11u, weight1.count());
}

TEST(Weight_Operator, CanMultiplyScalarWeight)
{
    TestWeight myWeight(14);

    TestWeight result = 3 * myWeight;
    EXPECT_EQ(3u * 14u, result.count());
    EXPECT_EQ(14u, myWeight.count());
}

TEST(Weight_Operator, CanMultiplyWeightScalar)
{
    TestWeight myWeight(4);

    TestWeight result = myWeight * 5;
    EXPECT_EQ(4u * 5u, result.count());
    EXPECT_EQ(4u, myWeight.count());
}

TEST(Weight_Cast, CanDoIdentityCast)
{
    usu::weight<std::ratio<5, 1>> weight1(10); // 50 units
    decltype(weight1) result1 = usu::weight_cast<decltype(weight1)>(weight1);

    EXPECT_EQ(10u, weight1.count());
    EXPECT_EQ(10u, result1.count());

    usu::weight<std::ratio<1, 2>> weight2(5); // 2.5 units
    decltype(weight2) result2 = usu::weight_cast<decltype(weight2)>(weight2);

    EXPECT_EQ(5u, weight2.count());
    EXPECT_EQ(5u, result2.count());
}

TEST(Weight_Cast, CanCastToSmallerUnit)
{
    BigWeight bigWeight(15);

    auto result = usu::weight_cast<SmallWeight>(bigWeight);

    EXPECT_EQ(15u, bigWeight.count());
    EXPECT_EQ(180u, result.count());
}

TEST(Weight_Cast, CanCastToLargerUnit)
{
    SmallWeight smallWeight(24);

    auto result = usu::weight_cast<BigWeight>(smallWeight);

    EXPECT_EQ(24u, smallWeight.count());
    EXPECT_EQ(2u, result.count());
}

TEST(Weight_Cast, CanCastToLargerUnitWithPrecisionLoss)
{
    SmallWeight smallWeight(39);

    auto result = usu::weight_cast<BigWeight>(smallWeight);

    EXPECT_EQ(39u, smallWeight.count());
    EXPECT_EQ(3u, result.count());
}

TEST(Weight_Cast, CanCastToLargerUnitWithoutPrecisionLoss)
{
    usu::weight<std::ratio<1, 3>, double> smallWeight(39);

    auto result = usu::weight_cast<usu::weight<std::ratio<4, 1>, double>>(smallWeight);

    EXPECT_EQ(39u, smallWeight.count());
    EXPECT_NEAR(3.25, result.count(), kEpsilon);
}

TEST(Weight_Cast, CanCastToLargerUnitWithoutPrecisionLossDifferentStorageTypes)
{
    usu::weight<std::ratio<1, 3>, std::uint32_t> smallWeight(39);

    auto result = usu::weight_cast<usu::weight<std::ratio<4, 1>, double>>(smallWeight);

    EXPECT_EQ(39u, smallWeight.count());
    EXPECT_NEAR(3.25, result.count(), kEpsilon);
}

TEST(Logical_Operators, EqualityInequality)
{
    SmallWeight a(1);
    SmallWeight b(1);
    SmallWeight c(2);
    BigWeight d(1);
    BigWeight e(2);

    EXPECT_EQ(a == b, true);
    EXPECT_EQ(a == d, false);
    EXPECT_EQ(a == c, false);
    EXPECT_EQ(a == e, false);

    EXPECT_EQ(a != b, false);
    EXPECT_EQ(a != d, true);
    EXPECT_EQ(a != c, true);
    EXPECT_EQ(a != e, true);
}

TEST(Logical_Operators, LessThanLessThanEqual)
{
    SmallWeight a(1);
    SmallWeight b(1);
    SmallWeight c(2);
    BigWeight d(1);

    EXPECT_EQ(a < b, false);
    EXPECT_EQ(a < d, true);
    EXPECT_EQ(a < c, true);
    EXPECT_EQ(c < a, false);

    EXPECT_EQ(a <= b, true);
    EXPECT_EQ(a <= c, true);
    EXPECT_EQ(c <= a, false);
    EXPECT_EQ(a <= d, true);
}

TEST(Logical_Operators, GreaterThanGreaterThanEqual)
{
    SmallWeight a(1);
    SmallWeight b(1);
    SmallWeight c(2);
    BigWeight d(1);

    EXPECT_EQ(a > b, false);
    EXPECT_EQ(a > d, false);
    EXPECT_EQ(a > c, false);
    EXPECT_EQ(c > a, true);

    EXPECT_EQ(a >= b, true);
    EXPECT_EQ(a >= c, false);
    EXPECT_EQ(c >= a, true);
    EXPECT_EQ(a >= d, false);
}

TEST(Weight_Unit, CorrectUnitsDefined)
{
    EXPECT_EQ(std::micro::num, usu::microgram::conversion::num);
    EXPECT_EQ(std::micro::den, usu::microgram::conversion::den);
    EXPECT_EQ(usu::gram::conversion::num, usu::gram::conversion::den);
    EXPECT_EQ(std::kilo::num, usu::kilogram::conversion::num);
    EXPECT_EQ(std::kilo::den, usu::kilogram::conversion::den);
    EXPECT_EQ((std::ratio<28349523125, 1000000000>::num), usu::ounce::conversion::num);
    EXPECT_EQ((std::ratio<28349523125, 1000000000>::den), usu::ounce::conversion::den);
    EXPECT_EQ((std::ratio<45359237, 100000>::num), usu::pound::conversion::num);
    EXPECT_EQ((std::ratio<45359237, 100000>::den), usu::pound::conversion::den);
    EXPECT_EQ((std::ratio<90718474, 100>::num), usu::ton::conversion::num);
    EXPECT_EQ((std::ratio<90718474, 100>::den), usu::ton::conversion::den);
}
