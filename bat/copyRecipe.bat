@echo off
echo.
echo.
echo -------------设备Recipe备份(by gmy)-------------
echo.
echo.
: adb root
adb devices
set /p device=请选择设备:
echo 设备设置为:%device%
echo.

adb -s %device% shell mkdir -p /sdcard/appcfg/recipe/oem/
adb -s %device% shell cp -r /iircfg/recipe/oem/* /sdcard/appcfg/recipe/oem/

echo 备份成功，使用"adb -s %device% shell & busybox vi sdcard/appcfg/recipe/oem/oem.json" 编辑
pause