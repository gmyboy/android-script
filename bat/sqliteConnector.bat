@echo off
echo.
echo.
echo.   ¨q©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤¨r
echo.   ©¦                                                                  ©¦
echo.   ©¦                Android Sqlite Database Connect Script.           ©¦
echo.   ©¦                             by GMY.                              ©¦
echo.   ©¦                                                                  ©¦
echo.   ¨t©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤¨s
echo.
echo.
: adb root
adb devices
set /p device=Set Android Device(Device No.):
echo Device set to: %device%
echo.
adb -s %device% shell find /storage -type d -name emulated -prune -o -name "*.db" -print
echo.
set /p dbPath=Connect to:
echo.
echo.
echo Start to connect: %dbPath%
for %%i in ("%dbPath%") do (
    set fileDrive=%%~di
    set fileDirectory=%%~pi
    set fileName=%%~ni
    set fileExtend=%%~xi
    set fileFullName=%%~nxi
)
set roamingPath=%appdata%
set tempPath=%roamingPath:Roaming=Local%\Temp\db

set CUR_YYYY=%date:~,4%
set CUR_MM=%date:~5,2%
set CUR_DD=%date:~8,2%

set ticks=%CUR_YYYY%%CUR_MM%%CUR_DD%

set tempFileName=%fileName%_%ticks%%fileExtend%
echo Connect : %tempPath%\%tempFileName%
set opened=false
set interval=6
:Again
IF NOT EXIST %tempPath% (
    mkdir %tempPath%
)
adb -s %device% pull "%dbPath%" %tempPath%\%tempFileName% > NUL
if %opened% == false (
    timeout 1 > NUL
    set opened=true
    start %tempPath%\%tempFileName%
)
timeout %interval% > NUL
goto Again