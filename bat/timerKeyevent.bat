@echo off
echo.
echo.
echo.   ¨q©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤¨r
echo.   ©¦                                                                  ©¦
echo.   ©¦                Timer keyevent runner in Android.                 ©¦
echo.   ©¦                             by GMY.                              ©¦
echo.   ©¦                                                                  ©¦
echo.   ¨t©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤¨s
echo.
echo.
adb devices
set /p device=Set Android Device(Device No.):
echo Device set to: %device%
echo.
set /p keyCode=Set KeyEvent(Android KeyCode.):
echo KeyEvent set to: %keyCode%
echo.
set /p interval=Set timmer interval(in seconds. default is 6s):
echo Timmer interval set to: %interval%
echo.
echo Timer runner started in every %interval% seconds to execute: 
echo "adb -s %device% shell input keyevent %keyCode%"
echo ...
echo.
:Again
adb -s %device% shell input keyevent %keyCode%
timeout %interval% > NUL
goto Again