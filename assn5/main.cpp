#include "WordTree.hpp"
#include "rlutil.h"

#include <algorithm>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <iterator>
#include <memory>
#include <sstream>
#include <string>
#include <vector>

std::shared_ptr<WordTree> readDictionary(std::string filename)
{
    auto wordTree = std::make_shared<WordTree>();
    std::ifstream inFile = std::ifstream(filename, std::ios::in);

    while (!inFile.eof())
    {
        std::string word;
        std::getline(inFile, word);
        // Need to consume the carriage return character for some systems, if it
        // exists
        if (!word.empty() && word[word.size() - 1] == '\r')
        {
            word.erase(word.end() - 1);
        }
        // Keep only if everything is an alphabetic character -- Have to send
        // isalpha an unsigned char or it will throw exception on negative values;
        // e.g., characters with accent marks.
        if (std::all_of(word.begin(), word.end(),
                        [](unsigned char c)
                        { return std::isalpha(c); }))
        {
            std::transform(word.begin(), word.end(), word.begin(),
                           [](char c)
                           { return static_cast<char>(std::tolower(c)); });
            wordTree->add(word);
        }
    }

    return wordTree;
}

template <typename Out>
void split(const std::string& s, char delim, Out result)
{
    std::istringstream iss(s);
    std::string item;
    while (std::getline(iss, item, delim))
    {
        *result++ = item;
    }
}

std::vector<std::string> split(const std::string& s, char delim)
{
    std::vector<std::string> elems;
    split(s, delim, std::back_inserter(elems));
    return elems;
}

void renderSuggestions(std::vector<std::string>& suggestions)
{
    rlutil::locate(2, 4);
    std::cout << "--- Suggestions ---\n";
    for (std::string word : suggestions)
    {
        std::cout << word << std::endl;
    }
}

int main()
{
    auto tree = readDictionary("big_dictionary.txt");
    int const startRow = 2;
    int currColumn = 1;
    std::string buffer = "";

    while (true)
    {
        rlutil::cls();
        rlutil::locate(1, startRow);
        std::cout << buffer;

        auto words = split(buffer, ' ');
        auto lastWord = words.size() > 0 ? words[words.size() - 1] : "";
        auto predictions = tree->predict(lastWord, 20);

        renderSuggestions(predictions);
        rlutil::locate(currColumn, startRow);

        auto key = rlutil::getkey();
        if (key == rlutil::KEY_BACKSPACE)
        {
            buffer = buffer.substr(0, buffer.size() - 1);
            if (currColumn > 1)
            {
                currColumn--;
            }
        }
        // Key is a lower-case english letter or the space key
        else if ((key >= 97 && key <= 122) || key == rlutil::KEY_SPACE)
        {
            buffer += key;
            currColumn++;
        }
    }
}