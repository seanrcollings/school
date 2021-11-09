#include "./Pattern.hpp"

#include <array>
#include <cstdint>

#pragma once

class PatternAcorn : public Pattern
{
  public:
    std::uint8_t getSizeX() const;
    std::uint8_t getSizeY() const;
    bool getCell(std::uint8_t x, std::uint8_t y) const;

  private:
    static constexpr std::uint8_t m_sizeX = 7;
    static constexpr std::uint8_t m_sizeY = 3;
    static constexpr std::array<std::array<bool, m_sizeX>, m_sizeY> board{ { { 0, 1, 0, 0, 0, 0, 0 },
                                                                             { 0, 0, 0, 1, 0, 0, 0 },
                                                                             { 1, 1, 0, 0, 1, 1, 1 } } };
};