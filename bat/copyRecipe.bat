@echo off
echo.
echo.
echo -------------�豸Recipe����(by gmy)-------------
echo.
echo.
: adb root
adb devices
set /p device=��ѡ���豸:
echo �豸����Ϊ:%device%
echo.

adb -s %device% shell mkdir -p /sdcard/appcfg/recipe/oem/
adb -s %device% shell cp -r /iircfg/recipe/oem/* /sdcard/appcfg/recipe/oem/

echo ���ݳɹ���ʹ��"adb -s %device% shell & busybox vi sdcard/appcfg/recipe/oem/oem.json" �༭
pause