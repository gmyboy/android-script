@echo off
echo.
echo.
echo -------------Android����ANR��־(by gmy)-------------
echo.
echo.
: adb root
adb devices
set /p device=��ѡ���豸:
echo �豸����Ϊ:%device%
echo.

set outputFileName=%cd%\traces_%date%-%time::=-%.txt
adb -s %device% pull /data/anr/traces.txt %outputFileName%
echo ��־�ѵ�������%outputFileName%
code %outputFileName%