@echo off
echo.
echo.
echo -------------adb ָ�ʱִ�нű�(by gmy)-------------
echo.
echo.
: adb root
adb devices
set /p device=��ѡ���豸:
echo �豸����Ϊ:%device%
echo.
set /p command=�������ѭ��ִ�е�adbָ��:

echo.
echo ��ʼ��ʱ(ÿ��)ִ��ָ��: adb %command%
echo.
set opened=false
set interval=1
:Again
adb -s %device% %command% > NUL
timeout %interval% > NUL
goto Again