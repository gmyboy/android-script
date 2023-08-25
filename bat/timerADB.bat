@echo off
echo.
echo.
echo -------------adb 指令定时执行脚本(by gmy)-------------
echo.
echo.
: adb root
adb devices
set /p device=请选择设备:
echo 设备设置为:%device%
echo.
set /p command=请输入待循环执行的adb指令:

echo.
echo 开始定时(每秒)执行指令: adb %command%
echo.
set opened=false
set interval=1
:Again
adb -s %device% %command% > NUL
timeout %interval% > NUL
goto Again