@echo off
echo.
echo.
echo.   ¨q©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤¨r
echo.   ©¦                                                                  ©¦
echo.   ©¦           Copy recipe to "sdcard/appcfg" as user recipe.         ©¦
echo.   ©¦                             by GMY.                              ©¦
echo.   ©¦                                                                  ©¦
echo.   ¨t©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤¨s
echo.
echo.
: adb root
adb devices
set /p device=Set Android Device(Device No.):
echo.
adb -s %device% shell mkdir -p /sdcard/appcfg/recipe/oem/
adb -s %device% shell cp -r /iircfg/recipe/oem/* /sdcard/appcfg/recipe/oem/
echo Copy Successfully.
echo Use "adb -s %device% shell & busybox vi sdcard/appcfg/recipe/oem/oem.json" to edit.
echo.
pause