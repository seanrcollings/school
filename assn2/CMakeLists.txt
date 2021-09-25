# src/CMakeLists.txt
cmake_minimum_required(VERSION 3.10)

# set the project name
project("Assignment2")

# specify the executable and corresponding source file
add_executable(RandDistributions "Assignment2.cpp")

if (CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
    target_compile_options(RandDistributions PRIVATE /W4 /permissive-)
elseif (CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    target_compile_options(RandDistributions PRIVATE -Wall -Wextra -pedantic)
elseif (CMAKE_CXX_COMPILER_ID MATCHES "CLANG")
    target_compile_options(RandDistributions PRIVATE -Wall -Wextra -pedantic)
endif()