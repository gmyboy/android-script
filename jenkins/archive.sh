#!/usr/bin/env bash

#
# sdk编译脚本
# 版本号和构建日期需在build.gradle中修改
# jenkins只调用该脚本，并传入自动构建的参数
# ./archive.sh <release/debug> <version> <outputDir>(must) <sampleModule>(default is app) <libraryModule>(default is library)
# <buildNumber> <build_armeabi_v7a> <build_arm64_v8a> <build_x86> <build_x86_64>
# doc目录无需指定,默认拷贝项目根目录下的doc目录
# demo: ./archive.sh release 1.0.0 D:/fotric/FJPEG-SDK app library 27 true true true true
#

RED='\033[0;31m'
Green='\033[0;33m'
NC='\033[0m' # No Color

# -e 若指令传回值不等于0，则立即退出shell。
set -e
# 执行指令后，会先显示该指令及所下的参数。
# set +x

function getAarName(){
    for element in `ls $1`
      do
        dir_or_file=$1/$element
    if [ -d $dir_or_file ]
      then
        getdir $dir_or_file
      else
        temp=$(basename $dir_or_file)
        echo ${temp%-*}
    fi
    done
}

#--------------------
echo "--------------------"
echo -e "${RED}[*]start to archive $1 ${NC}"
echo "--------------------"

# 构建类型
buildType=$1
version=$2
# 输出的SDk目录名称
outputPath=$3
# 源目录
sampleModule=$4
libraryModule=$5
# build info
buildNumber=$6
build_armeabi_v7a=$7
build_arm64_v8a=$8
build_x86=$9
build_x86_64=${10}

echo "buildType=$buildType"
echo "version=$version"

if [ -z "$sampleModule" ]; then
    sampleModule="app"
fi
if [ -z "$libraryModule" ]; then
    libraryModule="library"
fi

BUILD_ROOT=`pwd`
outputPath=${outputPath}/${version}

echo "sampleModule=$sampleModule"
echo "libraryModule=$libraryModule"
echo "outputPath=$outputPath"

if [ -z "$buildType" ]; then
    echo "You must specific an build type 'release, debug'."
    exit 1
fi
if [ -z "$version" ]; then
    echo "You must specific an build version."
    exit 1
fi
if [ -z "$outputPath" ]; then
    echo "You must specific an output path."
    exit 1
fi
if [ ! -d "$sampleModule" ]; then
    echo "sampleModule=$sampleModule is not a directory."
    exit 1
fi
if [ ! -d "$libraryModule" ]; then
    echo "libraryModule=$libraryModule is not a directory."
    exit 1
fi

# 源目录
pathDoc=${BUILD_ROOT}/doc
pathAar=${BUILD_ROOT}/${libraryModule}/build/outputs/aar
# pathJar=${BUILD_ROOT}/${libraryModule}/build/intermediates/full_jar
# pathJni=${BUILD_ROOT}/${libraryModule}/build/intermediates/merged_native_libs
# 输出目录
outputPathDoc=${outputPath}/doc
outputPathLib=${outputPath}/libs
outputPathSample=${outputPath}/samplecode

#echo "--------------------"
#echo -e "${RED}[*] building $1 ${NC}"
#echo "--------------------"

#if [ "release" = "$buildType" ]; then
#    ./gradlew ${libraryModule}:clean ${libraryModule}:assembleRelease ${libraryModule}:createFullJarRelease
#else
#    ./gradlew ${libraryModule}:clean ${libraryModule}:assembleDebug ${libraryModule}:createFullJarDebug
#fi

echo "--------------------"
echo -e "${RED}[*] clear output dir ${NC}"
echo "--------------------"
echo "outputPathDoc=$outputPathDoc"
echo "outputPathLib=$outputPathLib"
echo "outputPathSample=$outputPathSample"

rm -rf ${outputPath}
if [ ! -d ${outputPathDoc} ]; then
    mkdir -p ${outputPathDoc}
fi
if [ ! -d ${outputPathLib} ]; then
    mkdir -p ${outputPathLib}
fi
if [ ! -d ${outputPathSample} ]; then
    mkdir -p ${outputPathSample}
fi

echo "--------------------"
echo -e "${RED}[*] link lib ${NC}"
echo "--------------------"

# jar包由于使用系统自带的createFullJarRelease,无法指定输出文件
# 故在此处重命名jar文件
# pathJar=${pathJar}/${buildType};
aarName=$(getAarName $pathAar)
# cp -rf ${pathJar}/*.jar ${outputPathLib}/${aarName}.jar

cp -rf ${pathAar}/*.aar ${outputPathLib}/${aarName}.aar

# pathJni=${pathJni}/${buildType};
# cp -rf ${pathJni}/out/lib/* ${outputPathLib}
echo "lib linked to ${outputPathLib}"

echo "--------------------"
echo -e "${RED}[*] link sample ${NC}"
echo "--------------------"

cp -rf ${BUILD_ROOT}/* ${outputPathSample}
# delete unused dir and file
rm -rf ${outputPathSample}/*.iml
rm -rf ${outputPathSample}/*.md
rm -rf ${outputPathSample}/doc
rm -rf ${outputPathSample}/${libraryModule}
rm -rf ${outputPathSample}/archive.sh
rm -rf ${outputPathSample}/local.properties
chmod u+x ${outputPathSample}/${sampleModule}/build.gradle
chmod u+x ${outputPathSample}/settings.gradle
chmod u+x ${outputPathSample}/gradle.properties
sed -i "s/implementation project(path: ':${libraryModule}')//g" ${outputPathSample}/${sampleModule}/build.gradle
echo "include ':${sampleModule}'" > ${outputPathSample}/settings.gradle
echo -e "org.gradle.jvmargs=-Xmx1536m\nandroid.useAndroidX=true\nandroid.enableJetifier=true" > ${outputPathSample}/gradle.properties
echo "sample linked to ${outputPathSample}"

echo "--------------------"
echo -e "${RED}[*] link doc ${NC}"
echo "--------------------"
cp -rf ${pathDoc}/* ${outputPathDoc}
echo "doc linked to ${outputPathDoc}"

readMe=${outputPath}/readme.txt
readMeContent="shell script: ./archive.sh $1 $2 $3 $4 $5 $6 $7 $8 $9 ${10} \n\n
|---doc\n
|   |---FJPEGSDK开发者手册.md \t 开发说明文档\n
|---libs\n
|   |---fjpeg_v1.0.0_202006290915.aar \t 热像图格式库\n
|---samplecode \t\t\t 示例工程\n
|---readme.txt \t\t\t 说明文件\n\n
build_type: ${buildType}\n
build_version: ${version}\n
armeabi_v7a: ${build_armeabi_v7a}\n
arm64_v8a: ${build_arm64_v8a}\n
x86: ${build_x86}\n
x86_64: ${build_x86_64}\n
jenkins_build_number: ${buildNumber}\n
build_time: $(date "+%Y-%m-%d %H:%M:%S")"
echo -e ${readMeContent} >> ${readMe}

echo "--------------------"
echo -e "${RED}[*] archive done ${NC}"
echo "--------------------"

