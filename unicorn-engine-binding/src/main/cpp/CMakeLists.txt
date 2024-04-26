# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("Unicorn")

message("dir:" ${CMAKE_SOURCES_DIR})
set(JNI_LIBS_DIR ${CMAKE_SOURCES_DIR}/../jniLibs)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.
add_library(${CMAKE_PROJECT_NAME} SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        unicorn_Unicorn.c)
# 1、添加一个库，在这里可以理解为声明了一个变量，来承载需要引入的库
add_library(
        unicorn # 表示的是模块名称，可以自己任意取，例如：deviceutil
        SHARED # 这个是固定的，基本上表示共享库
        IMPORTED # 这个也基本上是固定的，表示当前是导入的，跟我们 java 的 import 差不多含义
)
# 2、将第一步声明的库名称(libraryname)，跟具体的库关联到一起
set_target_properties(
        unicorn # 库的名称
        PROPERTIES IMPORTED_LOCATION # 表示当前库是导入的方式
        ${JNI_LIBS_DIR}/${ANDROID_ABI}/libunicorn.so # so 动态库的具体路径
)
# 3、如果第三方库有头文件的，为了在编码的时候可以有提示，使用下面的指令进行指定
# 这里是指定头文件那些的目录，这样指定后，编译器会扫描这个目录，编码的时候就可以提示到这里的方法了
include_directories(include)
# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(${CMAKE_PROJECT_NAME}
        # List libraries link to the target library
        android
        log)