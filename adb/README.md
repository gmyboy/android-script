## Android ADB

android adb相关功能记录



### 1.Android Studio ADB Plugin（ADB Idea）

 	包括adb常用的功能，adb install -r xxx、adb ubinstall(current package)、adb restart xxx等。



### 2.查看设备尺寸

#### 1.1 查看设备屏幕分辨率：

```
adb shell wm size
```

#### 1.2 设置屏幕分辨率：

```
adb shell wm size 240x320<reset>
adb shell wm density 120<reset>
```



### 3.Monkey自动化测试

#### 1.1 开启

```
adb shell
# -p 针对指定app，不指定为整个系统级别。
monkey -p com.fotric.hawk.debug -s 500 --ignore-crashes --ignore-timeouts --ignore-security-exceptions --throttle 500 -v -v -v 99999999 > data/local/tmp/monkeylog.txt
```

#### 1.2 停止

```
adb shell
top | grep monkey
kill (pid of monkey)
```



### 4.ADB Remote

#### 1.1 插上数据线

```
adb devices
adb tcpip 5555
```

#### 1.2 拔出数据线

```
adb connect ip:port
```



### 5.包管理（pm）

#### 1.1 查看所有应用包

```
adb shell pm list packages
```

#### 1.2 启动某个包名对应的Activity

```
# qualcomm自带的屏幕色彩调节工具
adb shell am start com.qualcomm.qti.snapdragon.qdcm_mobile/.QdcmMobileMain
```


### 6.读取CPU信息

#### 1.1 CPU工作频率

```
adb shell
cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq
```
#### 1.2 CPU工作温度

```
adb shell
cat /sys/class/thermal/thermal_zone9/temp
```

### 7.开启过度绘制

```
adb shell setprop debug.hwui.overdraw show
adb shell setprop debug.hwui.overdraw false
```

## 模拟器

### 1. 隐藏导航按钮

#### 1.1 查找并打开模拟器配置文件

[用户根目录]/.android/avd/ [模拟器名字].avd/config.ini

#### 1.2 修改这两个属性

```
hw.dPad=yes 
hw.mainKeys=yes
```

