@echo off

:: Buildscript for building on windows with Ninja and clang-cl
:: Copy the script to llvm-project root dir

set version=%1

set "curdir=%cd%"

call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvarsall" amd64
cd /d %curdir%

set CFLAGS=-m64
set CXXFLAGS=-m64
set config=RelWithDebInfo

cmake . -B"BUILD-WIN32" -DJVM_MODULE_VERSION="%version%" -DCMAKE_BUILD_TYPE=%config% -GNinja -DCMAKE_C_COMPILER="C:/Program Files/LLVM/bin/clang-cl.exe" -DCMAKE_CXX_COMPILER="C:/Program Files/LLVM/bin/clang-cl.exe" -DCMAKE_LINKER="C:/Program Files/LLVM/bin/lld-link.exe"
cmake --build "BUILD-WIN32" --config %config%
