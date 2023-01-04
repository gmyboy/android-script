@echo off
echo.
echo.
echo -------------Android导出ANR日志(by gmy)-------------
echo.
echo.
: adb root
adb devices
set /p device=请选择设备:
echo 设备设置为:%device%
echo.

set outputFileName=%cd%\traces_%date%-%time::=-%.txt
adb -s %device% pull /data/anr/traces.txt %outputFileName%
echo 日志已导出至：%outputFileName%
code %outputFileName%