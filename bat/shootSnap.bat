@echo off
echo.
echo.
echo -------------Android(.db)���ݿ����ӽű�(by gmy)-------------
echo -------------����: ���ű��ἷռ����adb�����ţ�--------------
echo -------------����adb���ԣ�wifi���ӵ�------------------------
echo.
echo.
adb devices
set /p device=��ѡ���豸:
echo �豸����Ϊ:%device%
echo.
echo ��ʼ��������
echo.
set INTERVAL=6
:Again
adb -s %device% shell input keyevent 27
timeout %INTERVAL% > NUL
goto Again