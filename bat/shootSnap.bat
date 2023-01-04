@echo off
echo.
echo.
echo -------------Android(.db)数据库连接脚本(by gmy)-------------
echo -------------警告: 本脚本会挤占其他adb连接桥，--------------
echo -------------例如adb调试，wifi连接等------------------------
echo.
echo.
adb devices
set /p device=请选择设备:
echo 设备设置为:%device%
echo.
echo 开始持续拍照
echo.
set INTERVAL=6
:Again
adb -s %device% shell input keyevent 27
timeout %INTERVAL% > NUL
goto Again