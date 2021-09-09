#include <iostream>
#include <string>
#include <array>
#include <cmath>


long nbonacci(unsigned int series, unsigned int n)
{
    if (n <= series)
    {
        return 1;
    }

    long sum = 0;
    for (unsigned int i = 1; i <= series; i++)
    {
        sum += nbonacci(series, n - i);
    }

    return sum;
}

void computeNbonacciRatio(std::string title, unsigned int series)
{

    unsigned int iterations = series - 1;
    unsigned int i = series + 1;
    double prevRatio = 0;
    double currRatio = 1;

    while (std::abs(currRatio - prevRatio) > 0.000001)
    {
        prevRatio = currRatio;
        currRatio = static_cast<double>(nbonacci(series, i)) / nbonacci(series, i - 1);
        iterations++;
        i++;
    }

    std::cout << title << " ratio approaches " << currRatio << " after " << iterations << " iterations\n";
}

int main()
{
    std::array<std::string, 4> sequence_names = {"Fibonacci", "Tribonacci", "Fourbonacci", "Fivebonacci"};


    for (unsigned int j = 0; j < sequence_names.size(); j++)
    {
        std::string sequence = sequence_names[j];
        std::cout << "\n---" << sequence << "---\n";
        for (int i = 0; i <= 20; i++)
        {
            std::cout << nbonacci(j + 2, i) << " ";
        }
    }
    std::cout << "\n\n";

    for (unsigned int j = 0; j < sequence_names.size(); j++)
    {
        std::string sequence = sequence_names[j];
        computeNbonacciRatio(sequence, j + 2);
    }

}