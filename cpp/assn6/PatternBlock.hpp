#include "./Pattern.hpp"

#include <array>
#include <cstdint>

#pragma once

class PatternBlock : public Pattern
{
  public:
    virtual std::uint8_t getSizeX() const;
    virtual std::uint8_t getSizeY() const;
    virtual bool getCell(std::uint8_t x, std::uint8_t y) const;

  private:
    static constexpr std::uint8_t m_sizeX = 2;
    static constexpr std::uint8_t m_sizeY = 2;
    static constexpr std::array<std::array<bool, m_sizeX>, m_sizeY> board{ { { 1, 1 },
                                                                             { 1, 1 } } };
};