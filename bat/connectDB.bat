@echo off
echo.
echo.
echo -------------Android(.db)���ݿ����ӽű�(by gmy)-------------
echo -------------����: ���ű��ἷռ����adb�����ţ�--------------
echo -------------����adb���ԣ�wifi���ӵ�------------------------
echo.
echo.
: adb root
adb devices
set /p device=��ѡ���豸:
echo �豸����Ϊ:%device%
echo.
adb -s %device% shell find /storage -name *.db
echo.
set /p dbPath=��ָ��Դ���ݿ�:
echo Դ���ݿ�:%dbPath%
for %%i in ("%dbPath%") do (
    set fileDrive=%%~di
    set fileDirectory=%%~pi
    set fileName=%%~ni
    set fileExtend=%%~xi
    set fileFullName=%%~nxi
)
echo.
echo ��ʼ�������ݿ⣺%dbPath%
echo.
set roamingPath=%appdata%
set tempPath=%roamingPath:Roaming=Local%\Temp\db

set CUR_YYYY=%date:~,4%
set CUR_MM=%date:~5,2%
set CUR_DD=%date:~8,2%

set ticks=%CUR_YYYY%%CUR_MM%%CUR_DD%

set tempFileName=%fileName%_%ticks%%fileExtend%
echo ���ݿ������ӣ�%tempPath%\%tempFileName%
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