@echo off
echo.
echo.
echo -------------Android(.db)数据库连接脚本(by gmy)-------------
echo -------------警告: 本脚本会挤占其他adb连接桥，--------------
echo -------------例如adb调试，wifi连接等------------------------
echo.
echo.
: adb root
adb devices
set /p device=请选择设备:
echo 设备设置为:%device%
echo.
adb -s %device% shell find /storage -name *.db
echo.
set /p dbPath=请指定源数据库:
echo 源数据库:%dbPath%
for %%i in ("%dbPath%") do (
    set fileDrive=%%~di
    set fileDirectory=%%~pi
    set fileName=%%~ni
    set fileExtend=%%~xi
    set fileFullName=%%~nxi
)
echo.
echo 开始连接数据库：%dbPath%
echo.
set roamingPath=%appdata%
set tempPath=%roamingPath:Roaming=Local%\Temp\db

set CUR_YYYY=%date:~,4%
set CUR_MM=%date:~5,2%
set CUR_DD=%date:~8,2%

set ticks=%CUR_YYYY%%CUR_MM%%CUR_DD%

set tempFileName=%fileName%_%ticks%%fileExtend%
echo 数据库已连接：%tempPath%\%tempFileName%
set opened=false
set interval=4
:Again
IF NOT EXIST %tempPath% (
    mkdir %tempPath%
)
adb -s %device% pull %dbPath% %tempPath%\%tempFileName% > NUL
if %opened% == false (
    timeout 1 > NUL
    set opened=true
    start %tempPath%\%tempFileName%
)
timeout %interval% > NUL
goto Again