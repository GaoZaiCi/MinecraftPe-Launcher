# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("launcher")

set(BUILD_FLAGS_INFO)

if (${CMAKE_BUILD_TYPE} MATCHES Release)
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -Os -Wall")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -fvisibility=hidden -fno-unwind-tables -fno-asynchronous-unwind-tables")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -fla")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -split")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -split_num=2")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -sub")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -sub_loop=1")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -bcf")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -bcf_loop=1")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -bcf_prob=10")
    set(BUILD_FLAGS_INFO "${BUILD_FLAGS_INFO} -mllvm -sobf")
    add_definitions(-DRELEASE_MODE)
else ()
    add_definitions(-DDEV_MODE)
endif ()

add_definitions(-DIMGUI_IMPL_OPENGL_ES3)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${BUILD_FLAGS_INFO}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${BUILD_FLAGS_INFO}")

include_directories(
        include
        libs/And64InlineHook
        libs/jsoncpp
        libs/imgui
        )

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

aux_source_directory(${PROJECT_SOURCE_DIR} SRCS)
aux_source_directory(util SRCS)
aux_source_directory(core CORE_SRCS)
aux_source_directory(libs/And64InlineHook SRCS)
aux_source_directory(libs/imgui IMGUI)
aux_source_directory(libs/imgui/backends IMGUI)

add_subdirectory(libs/zeroframework)

add_library( # Sets the name of the library.
        launcher

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${SRCS}
        )


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        find-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        launcher
        zerof
        android
        EGL
        GLESv3
        # Links the target library to the log library
        # included in the NDK.
        ${find-lib})

add_library(core SHARED ${IMGUI} ${CORE_SRCS})
target_link_libraries(core android
        EGL
        GLESv3
        log)