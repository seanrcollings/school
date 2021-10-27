#include "WordTree.hpp"

#include <algorithm>
#include <fstream>
#include <iostream>
#include <memory>
#include <string>

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

// abandon    abandon
// abandoned  abandoneis
// abandoning abandoneisd
// abandons   abandoneisdng

int main()
{
    auto tree = readDictionary("dictionary.txt");
    // std::cout << tree->size() << std::endl;
    // std::cout << tree->find("aardvark") << std::endl;
    auto predictions = tree->predict("abando", 10);
    for (std::string s : predictions)
    {
        std::cout << s << ' ';
    }
}