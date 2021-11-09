#include "./PatternGosperGliderGun.hpp"

#include <cstdint>

std::uint8_t PatternGosperGliderGun::getSizeX() const
{
    return m_sizeX;
}

std::uint8_t PatternGosperGliderGun::getSizeY() const
{
    return m_sizeY;
}

bool PatternGosperGliderGun::getCell(std::uint8_t x, std::uint8_t y) const
{
    return board[y][x];
}