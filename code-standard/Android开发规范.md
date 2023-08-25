## Fotric Android开发规范

## 1. 前言

​       特定开发规范，有利于项目维护、增强代码可读性、提升CR(Code Review)效率和有利于团队协作开发。以下标题带有 '*' 的规范请严格遵守，其余规范仅作为开发的建议，不做强制要求。

## 2. *IDE规范

1. 统一使用最新的稳定版Android Studio作为开发工具；
2. 不可使用其他IDE或收费的开发工具；
3. 编码格式统一为UTF-8；
4. .java、.xml文件，需要统一进行格式化；
5. 删除多余的import（可利用AS的Optimize Imports， Settings->Keymap->Optimize Imports）快捷键；<u>Mac OS 快键操作：control+option+o</u>
6. 不允许随便使用AS的Code Clean Up工具，避免带来不必要的错误。

## 3. 命名规范

代码中命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。正确的英文拼写和语法可以让阅读者更容易理解，避免歧义。

> 注意：即使纯拼音命名方式也要避免采用。但alibaba、taobao、youku、hangzhou等国际通用的名称，可视同英文

### 3.1 *项目名

- 每个项目必须有一个简单易记的代号作为其项目名称，例如Falcon、Moth等。
- 项目的默认applicationId为：com.fotric.项目代号，例如Falcon项目的：com.fotric.falcon。
- 如果该项目有国际版本、或其他的多渠道版本，applicationId为：com.fotric.项目代号.渠道号，例如Falcon项目的国际版：com.fotric.falcon.global。

### 3.1 *包名

- 包名全部小写，连接的单词只是简单地连接起来，不使用下划线，采用反域全名规则，全部使用小写字母。
- 一级包名是顶级域名，必须为com，二级包名为公司名：fotric，三级包名根据应用代号进行命名，后面就是对包名的划分。例如：com.fotric.falcon.main

### 3.2 包组织结构

#### **3.2.1 PBL（Package By Layer）**

> 按层分包组织class，也就是传统做法，把职能相同的class放在一起。
>
> PBL的缺点如下:
>
> - package内低内聚、低模块化，通常同一个包下的各个class彼此不相关，也可能永远不会相关
> - package之间高耦合，通常在一个class里需要import来自不同的pakcage的class
> - 编码麻烦，实现一块功能需要编辑不同目录下的多个文件。

```java
com
 └── domain 公司名或者品牌名称
       └── XXX{app} 产品名称或者项目名称，可选“app”结尾
 	 			 ├──core 此包中包含最顶级的配置类
         │		├── localization 本地化配置，包括语言，时区以及单位等信息
         │		├── preference 首选项，用户配置
 	 			 │		├── oem 产品OEM信息配置
 	 			 │		├── XXXApp.java  定义Application类 
 	 			 │		└── Global.java 定义配置数据（常量）
 	 			 ├──framework 定义interface以及相关基类
         ├──adapter 页面用到的Adapter类
 	 			 ├──uitl 工具包，包下类以“Utils”、“Manager” 结尾
 	 			 ├──view(widget) 自定义的View类,view和widget不能同时存在
 	 			 ├──data 数据处理
     		 │   ├── DataManager.java 数据管理器，
     		 │   ├── local 来源于本地的数据，比如 SP，Database，File
     		 │   ├── model 定义model(数据结构以及 getter/setter、compareTo、equals 等等，不含复杂操作）
         │   └── remote 来源于远端的数据
         ├──business 业务逻辑
         │   ├── ir 热像业务逻辑
                  ├── camera 热像仪通信业务  
                  └── analyse 热像分析业务
         │   ├── business01 业务逻辑0   
         │   └── ... 其它业务逻辑
         ├──service Service服务
         ├──receiver Receiver接收定义
         ├──broadcast Broadcast服务
         ├──ui 用户界面 包括activitys、dialog、menu
         │	 ├── activities 页面用到的activity，包内可按模块分子包
         │	 │		├── model0 模块0
 	 			 │	 │		└── ...其他模块
         │	 ├── dialog 页面用到的Dialog  
         │	 └── menu 页面用到的菜单，包内可按模块分子包       
```

#### **3.2.2 PBF（Package By Feature）**

> PBF按功能模块分包组织class，每块功能对应一个package，包名就是功能名，除了通用的类定义在util的pacakge下，实现该功能的所有类都在该package下
>
> 优点：
>
> - package内高内聚，package间低耦合，哪一个功能需要进行添加或者修改，只改某一个package下的东西。按class职能分层（PBL）降低了代码耦合，但带来了package耦合，要添新功能，需要改其它关联package下的代码，改动涉及class越多越容易产生新问题。按功能分包（PBF），featureA相关的class都在featureA包，feature内高内聚高度模块化，不同feature之间低耦合。
> - package有私有作用域（package-private scope）
> - 很容易删除功能，统计发现新功能没人用，这个版本那块功能得去掉，如果是PBL，得从功能入口到整个业务流程把受到牵连的所有能删的代码和class都揪出来删掉；如果是PBF，好说，先删掉对应包，再删掉功能入口（删掉包后入口肯定报错了。
> - 高度抽象。解决问题的一般方法是从抽象到具体，PBF包名是对功能模块的抽象，包内的class是实现细节，符合从抽象到具体。PBF从确定AppName开始，根据功能模块划分package，再考虑每块的具体实现细节。
> - 只通过class来分离逻辑代码，PBL既分离class又分离package，而PBF只通过class来分离逻辑代码。
> - package的大小有意义了，PBL中包的大小无限增长是合理的，因为功能越添越多，而PBF中包太大（包里class太多）表示这块需要重构（划分子包）

```java
com
 └── domain 公司名或者品牌名称
       └── XXX{app} 产品名称或者项目名称，可选“app”结尾
 	 			 ├──core 此包中包含最顶级的配置类
         │		├── localization 本地化配置，包括语言，时区以及单位等信息
         │		├── preference 首选项，用户配置
 	 			 │		├── oem 产品OEM信息配置
 	 			 │		├── XXXApp.java  定义Application类 
 	 			 │		└── Global.java 定义配置数据（常量）
 	 			 ├──framework 定义interface以及相关基类
 	 			 ├──uitl 工具包，包下类以“Utils”、“Manager” 结尾
 	 			 ├──view(widget) 自定义的View类，view和widget不能同时存在
 	 			 ├──data 数据处理
     		 │   ├── DataManager.java 数据管理器，
     		 │   ├── local 来源于本地的数据，比如 SP，Database，File
     		 │   ├── model 定义model(数据结构以及 getter/setter、compareTo、equals 等等，不含复杂操作）
         │   └── remote 来源于远端的数据
         ├──receiver Receiver接收定义
         ├──service Service服务
         ├──feature 功能
         │   ├── feature0 功能 0
         │   │   ├── feature0Activity.java
         │   │   ├── feature0Fragment.java
         │   │   ├── xxAdapter.java
         │   │   └── ... 其他 class
         │   └── ...其他功能
         └──ir 热像相关,非UI相关   
            ├── camera 热像仪通信业务  
            └── analyse 热像分析业务
```



```java
com
└── domain
    └── app
        ├── App.java 定义 Application 类
        ├── Config.java 定义配置数据（常量）
        ├── base 基础组件
        ├── custom_view 自定义视图
        ├── data 数据处理
        │   ├── DataManager.java 数据管理器，
        │   ├── local 来源于本地的数据，比如 SP，Database，File
        │   ├── model 定义 model（数据结构以及 getter/setter、compareTo、equals 等等，不含复杂操作）
        │   └── remote 来源于远端的数据
        ├── feature 功能
        │   ├── feature0 功能 0
        │   │   ├── feature0Activity.java
        │   │   ├── feature0Fragment.java
        │   │   ├── xxAdapter.java
        │   │   └── ... 其他 class
        │   └── ...其他功能
        ├── injection 依赖注入
        ├── util 工具类
        └── widget 小部件
```

### 3.3 *类名

- 类名都以 UpperCamelCase 编写；
- 类名通常是名词或名词短语；
- 接口名称有时可能是形容词或形容词短语；
- 名词采用大驼峰命名法，尽量避免缩写，除非该缩写是众所周知的，比如HTML、URL，如果类名称中包含单词缩写，则单词缩写的每个字母均应大写。

| 类                  | 描述                                          | 例如                                                         |
| ------------------- | --------------------------------------------- | ------------------------------------------------------------ |
| Activity类          | Activity为后缀标识                            | 如：WelcomeActivity                                          |
| Adapter类           | Adapter为后缀标识                             | 如：NewsDetailAdapter                                        |
| 解析类              | Parse为后缀标识                               | 如：HomePosterParser                                         |
| 工具方法类          | Utils或Manager为后续标识                      | 线程池管理类：ThreadPoolManager<br />日志工具类：LogUtils<br />打印工具类：PrinterUtils |
| 数据库类            | DBHelper后缀标识                              | 如：NewsDBHelper                                             |
| Service类           | Service为后缀标识                             | 如：TimeService                                              |
| BroadcastReceiver类 | Receiver为后缀标识                            | 如：JPushReceiver                                            |
| ContentProvider类   | Provider为后缀标识                            | ShareProvider                                                |
| 自定义的共享基础类  | 以Base开头                                    | BaseActivity,BaseFragment                                    |
| Native相关调用类    | 以NativeApi后缀标识、特殊情况下可以只用Native | 如：FalconIRCameraNativeApi                                  |

- 测试类的命名以它要测试的类的名称开始，以Test结束，如：HashTest\HashIntegrationTest

- 接口（interface）：全名规则与类一样采用 UpperCamelCase ，多以able或ible结尾，如：interface Runable、interface Accessible

  > 注意：如果项目采用MVP，所有Model、View、Presenter的接口都以I为前缀，不加后缀，其他的接口采用上述命名规则。

### 3.4 *方法名

- 方法名都以 lowerCamelCase 风格编写；
- 方法名通常是动词或动词短语；

| 方法                   | 说明                                                       |
| ---------------------- | ---------------------------------------------------------- |
| initXX()               | 初始化相关方法，使用init为前缀标识，如初始化布局initView() |
| isXX(),check()         | 方法返回值为boolean型的请使用is/check为前缀标识            |
| getXX()                | 返回某个值的方法，使用get为前缀标识                        |
| setXX()                | 设置某个属性值                                             |
| handleXX(),processXX() | 对数据进行处理的方法                                       |
| diaplayXX(),showXX()   | 弹出提示框和提示信息，使用display/show为前缀标识           |
| updateXX()             | 更新数据                                                   |
| saveXX(),insertXX()    | 保存或插入数据                                             |
| resetXX()              | 重置数据                                                   |
| clearXX()              | 清除数据                                                   |
| removeXX(),deleteXX()  | 移除数据或者视图等，如removeView()                         |
| drawXX()               | 绘制数据或效果相关的，使用draw前缀标识                     |
| _xXXX()                | 以'_'开头，表示native方法                                  |

### 3.5 常量名

常量名命名模式为 CONSTANT_CAST ，全部字母大写，用下划线分隔单词。

每个常量都是一个 static final 字段，但不是所有 static final 字段都是常量。

### 3.6 非常量字段名

非常量字段以 lowerCamelCase 风格的基础上改造为如下风格：

- scope{Type0}VariableName{Type1}
- type0VariableName{Type1}
- variableName{Type1}

> 说明：{}中的内容为可选。所有的VO（值对象）统一采用标准的 lowerCamelCase 风格编写，所有的DTO（数据传输对象）就按照接口文档中字义的字段名编写

#### 3.6.1 scope（范围）

- 非公有，非静态字段命名以 m 开头
- 静态字段全名以 s 开头
- 其它字段以小写字母开头

> 使用1个字符前缀来表示作用范围，1个字符的前缀必须小写，前缀后面是由表意性强的一个单词或多个单词组成的名字，而且每个单词的首字母大写，其它字母小写。

#### 3.6.2 *Type0 （控件类型）

主要用于Android的UI控件命名，为避免控件和普通成员变量混淆以及更好地表达意思，所有用来表示控件的成员变量统一加上控件缩写作为前缀，例如：mIvAvatar、rvBooks、flContainer.

**UI控件缩写：**

| 名称           | 缩写 |
| -------------- | ---- |
| Button         | btn  |
| CheckBox       | cb   |
| EditText       | et   |
| FrameLayout    | fl   |
| GridView       | gv   |
| ImageButton    | ib   |
| LinearLayout   | ll   |
| ListView       | lv   |
| ProgressBar    | pb   |
| RadioButton    | rb   |
| RecyclerView   | rv   |
| RelativeLayout | rl   |
| ScrollView     | sv   |
| SeekBar        | sb   |
| Spinner        | spn  |
| TextView       | tv   |
| ToggleButton   | tb   |
| VideoView      | vv   |
| WebView        | wv   |
| ImageView      | iv   |

#### 3.6.3 VariableName (变量名)

变量名可能会出现量词 例如：mFirstBook、mPreBook、curBook

| 量词列表 | 量词后缀说明         |
| -------- | -------------------- |
| First    | 一组变量中的第一个   |
| Last     | 一组变量中的最后一个 |
| Next     | 一组变量中的下一个   |
| Pre      | 一组变量中的上一个   |
| Cur      | 一组变量中的当前变量 |

#### 3.6.4 *Type1 （数据类型）

对于表示集合或者数组的非常量字段名，需要添加后缀来增强字段的可读性

- 集合添加：List、Map、Set
- 数组：Arr
- 如果数据类型不确定的话，比如表示的是很多书，那么使用其复数形式来表示也可，如：mBooks

### 3.6 *参数名

参数名以 lowerCamelCase 风格编写，参数应该避免用单个字符命名

### 3.7 *局部变更名

局部变更名以 lowerCamelCase 风格编写

### 3.8 临时变量

临时变量通常被取名为 `i`、`j`、`k`、`m` 和 `n`，它们一般用于整型；`c`、`d`、`e`，它们一般用于字符型。 如：`for (int i = 0; i < len; i++)`。

### 3.9 类型变量名

类型变量可用以下两种风格之一进行命名：

1. 单个的大写字母，后面可以跟一个数字（如：`E`, `T`, `X`, `T2`）。
2. 以类命名方式（参考[3.2 类名](https://github.com/Blankj/AndroidStandardDevelop#32-类名)），后面加个大写的 T（如：`RequestT`, `FooBarT`）。

### 4.0 *动态链接库(.so)

- 文件名都以lib开头，lib之后的内容按照 UpperCamelCase 编写；
- lib之后的内容通常是名词或名词短语；
- lib之后的名词采用大驼峰命名法，尽量避免缩写，除非该缩写是(内部)众所周知的，比如HTML、URL、F、IR等，如果类名称中包含单词缩写，则单词缩写的每个字母均应大写。

## 4 代码样式规范

### 4.1 *使用标准括号样式

左大括号不单独占一行，与其前面的代码位于同一行：

```java
class MyClass {
    int func() {
        if (something) {
            // ...
        } else if (somethingElse) {
            // ...
        } else {
            // ...
        }
    }
}
```

### 4.2 *编写简短方法

在可行的情况下，尽量编写短小精练的方法。有些情况下较长的方法是恰当的，因此对方法的代码长度没有做出硬性限制。如果某个方法的代码超过40行，请考虑是否可以在不破坏程序结构的前提下对其拆解重构。

### 4.3 类成员的顺序

推荐使用如下排序：

1. 常量
2. 字段
3. 构造函数
4. 重写函数和回调
5. 公有函数
6. 私有函数
7. 内部类或接口

如果类继承于Android组件（例如Activity或Fragment）,那么把重写函数按照他们的生命周期进行排序，如Activity，onCreate()、onDestroy()、onPause()、onResume().

### 4.4 函数参数的排序

在Andorid开发过程中，把Contex作为其第一个参数，把回调接口放在其最后一个参数。

### 4.5 字符串常量的全名和值

Android SDK 中的很多类都用到了键值对函数，比如`SharedPreferences`、`Bundle`、`Intent`，当时用到这些类的时候，我们 **必须** 将它们的键定义为 `static final` 字段，并遵循以下指示作为前缀。

| 类                 | 字段名前缀 |
| ------------------ | ---------- |
| SharedPreferences  | PREF_      |
| Bundle             | BUNDLE_    |
| Fragment Arguments | ARGUMENT_  |
| Intent Extra       | EXTRA_     |
| Intent Action      | ACTION_    |

### 4.6 *Activities 和 Fragments的传参

当Activity或Fragment传递数据通过Intent或Bundle时，不同值的键须遵循上一条所提及到的。

当Activity或Fragment启动需要传递参数时，那么它需要提供一个public static 的函数来启动或创建它。

> 注意：这些函数需要放在 `onCreate()` 之前的类的顶部；如果我们使用了这种方式，那么 `extras` 和 `arguments` 的键应该是 `private` 的，因为它们不再需要暴露给其他类来使用。

### 4.7 行长限制

代码中每一行文本的长度都应该不超过 100 个字符。如果行长超过了 100（AS 窗口右侧的竖线就是设置的行宽末尾 ），通常有两种方法来缩减行长。

- 提取一个局部变量或方法（最好）。
- 使用换行符将一行换成多行。

不过存在以下例外情况：

- 如果备注行包含长度超过 100 个字符的示例命令或文字网址，那么为了便于剪切和粘贴，该行可以超过 100 个字符。
- 导入语句行可以超出此限制，因为用户很少会看到它们（这也简化了工具编写流程）。

#### 4.7.1 换行策略

- 除赋值操作符之处，把换行符放在操作符之前；
- 函数链的换行，同一行中调用多个函数时（比如使用构建器时），对每个函数的调用应该在新的一行中，将换行符插入在 . 之前。
- 多参数的换行，当一个方法有很多参数或者参数很长的时候，应该在每个 ，后面进行换行。
- RxJava链式的换行，每个操作符都需要换新行，并且把换行符插入在 . 之前。

## 5 *资源文件规范

资源文件命名为全部小写，采用下划线命名法

#### 5.1 *动画资源文件（anim/和animator/）

属性动画文件需要放在 res/animator/ 目录下，视图动画文件需要放在 res/anim/ 目录下。

全名规则： {模块名_}逻辑名称

> 说明：{} 中的内容为可选，逻辑名称 可由多个单词加下划线组成

如：refresh_progress.xml、market_cart_add.xml、market_car_remove.xml

如果是普通的补间动画或者属性动画，可采用：动画类型_方向 的命名方式。

如：

| 名称              | 说明           |
| ----------------- | -------------- |
| fade_in           | 淡入           |
| fade_out          | 淡出           |
| push_down_in      | 从下方推入     |
| push_down_out     | 从下方推出     |
| push_left         | 推向左方       |
| slide_in_from_top | 从头部滑动进入 |
| zoom_enter        | 变形进入       |
| slide_in          | 滑动进入       |
| shrink_to_middle  | 中间缩小       |

#### 5.2 *颜色资源文件（color/）

命名规则：类型_逻辑名称

例如：`sel_btn_font.xml`

颜色资源文件放置于 `res/color/` 目录下

#### 5.3 *图片资源文件（drawable/和mipmap）

`res/drawable/` 目录下放的位图文件（.png、.9.png、.jpg、.gif）和编译为可绘制对象资源子类型的XML文件(ic_xxx.xml)

`res/mipmap/` 目录下放的是不同密度的启动图标，所以 `res/mipmap/` 只用于存放启动图标，其余图片资源文件都应该放到 `res/drawable/` 目录下

命名规则：`类型{_模块名}_逻辑名称`、`类型{_模块名}_颜色`。

说明：`{}` 中的内容为可选；`类型` 可以是[可绘制对象资源类型](https://link.jianshu.com/?t=可绘制对象资源类型)，也可以是控件类型（具体见附录[UI 控件缩写表](#ui-控件缩写表)）；最后可加后缀 `_small` 表示小图，`_big` 表示大图。

例如：

| 名称                      | 说明                                        |
| ------------------------- | ------------------------------------------- |
| `btn_main_about.png`      | 主页关于按键 `类型_模块名_逻辑名称`         |
| `btn_back.png`            | 返回按键 `类型_逻辑名称`                    |
| `divider_maket_white.png` | 商城白色分隔线 `类型_模块名_颜色`           |
| `ic_edit.xml`             | 编辑图标 `类型_逻辑名称`                    |
| `bg_main.png`             | 主页背景 `类型_逻辑名称`                    |
| `btn_red.png`             | 红色按键 `类型_颜色`                        |
| `btn_read_big.png`        | 红色大按键 `类型_颜色`                      |
| `icon_head_small.png`     | 小头像图标 `类型_逻辑名称`                  |
| `bg_input.png`            | 输入框背景 `类型_逻辑名称`                  |
| `divider_white.png`       | 白色分割线 `类型_颜色`                      |
| `bg_main_head.png`        | 主页头部背景 `类型_模块名_逻辑名称`         |
| `def_search_cell.png`     | 搜索页面默认单元图片 `类型_模块名_逻辑名称` |
| `icon_more_help.png`      | 更多帮助图标 `类型_逻辑名称`                |
| `divider_list_line.png`   | 列表分割线 `类型_逻辑名称`                  |
| `sel_search_ok.xml`       | 搜索界面确认选择器 `类型_模块名_逻辑名称`   |
| `shape_music_ring.xml`    | 音乐界面环形形状 `类型_模块名_逻辑名称`     |

如果有多种形态，如按钮选择器：`sel_btn_xx.xml`,采用如下命名：

| 名称                    | 说明                                |
| ----------------------- | ----------------------------------- |
| `sel_btn_xx`            | 作用在 `btn_xx` 上的 `selector`     |
| `btn_xx_normal`         | 默认状态效果                        |
| `btn_xx_pressed`        | `state_pressed` 点击效果            |
| `btn_xx_focused`        | `state_focused` 聚焦效果            |
| `btn_xx_disabled`       | `state_enabled` 不可用效果          |
| `btn_xx_checked`        | `state_checked` 选中效果            |
| `btn_xx_selected`       | `state_selected` 选中效果           |
| `btn_xx_hovered`        | `state_hovered` 悬停效果            |
| `btn_xx_checkable`      | `state_checkable` 可选效果          |
| `btn_xx_activated`      | `state_activated` 激活效果          |
| `btn_xx_window_focused` | `state_window_focused` 窗口聚焦效果 |

#### 5.4 *布局资源文件 （layout/）

命名规则：`类型_模块名`、`类型{_模块名}_逻辑名称`。

说明：`{}` 中的内容为可选。

如：

| 名称                        | 说明                                    |
| --------------------------- | --------------------------------------- |
| `activity_main.xml`         | 主窗体 `类型_模块名`                    |
| `activity_main_head.xml`    | 主窗体头部 `类型_模块名_逻辑名称`       |
| `fragment_music.xml`        | 音乐片段 `类型_模块名`                  |
| `fragment_music_player.xml` | 音乐片段的播放器 `类型_模块名_逻辑名称` |
| `dialog_loading.xml`        | 加载对话框 `类型_逻辑名称`              |
| `view_info.xml`             | 信息布局 `类型_逻辑名称`                |
| `item_main_song.xml`        | 主页歌曲列表项 `类型_模块名_逻辑名称`   |

#### 5.5 *菜单资源文件（menu/）

菜单相关的资源文件应放在该目录下。

命名规则：`{模块名_}逻辑名称`

说明：`{}` 中的内容为可选。

例如：`main_drawer.xml`、`navigation.xml`。

#### 5.6 values资源文件（values/）

`values/` 资源文件下的文件都以 `s` 结尾，如 `attrs.xml`、`colors.xml`、`dimens.xml`，起作用的不是文件名称，而是 `` 标签下的各种标签，比如 `` 决定样式，`` 决定颜色，所以，可以把一个大的 `xml` 文件分割成多个小的文件，比如可以有多个 `style` 文件，如 `styles.xml`、`styles_home.xml`、`styles_item_details.xml`、`styles_forms.xml` 。

##### 5.6.1 *colors.xml

`color` 的 `name` 命名使用下划线命名法，在你的 `colors.xml` 文件中应该只是映射颜色的名称一个 ARGB 值，而没有其它的。不要使用它为不同的按钮来定义 ARGB 值。

例如，不要像下面这样做

```xml
<resources>
      <color name="button_foreground">#FFFFFF</color>
      <color name="button_background">#2A91BD</color>
      <color name="comment_background_inactive">#5F5F5F</color>
      <color name="comment_background_active">#939393</color>
      <color name="comment_foreground">#FFFFFF</color>
      <color name="comment_foreground_important">#FF9D2F</color>
      ...
      <color name="comment_shadow">#323232</color>
```

使用这种格式，会非常容易重复定义 ARGB 值，而且如果应用要改变基色的话会非常困难。同时，这些定义是跟一些环境关联起来的，如 `button` 或者 `comment`，应该放到一个按钮风格中，而不是在 `colors.xml` 文件中。

相反，应该这样做：

```xml
<resources>

      <!-- grayscale -->
      <color name="white"     >#FFFFFF</color>
      <color name="gray_light">#DBDBDB</color>
      <color name="gray"      >#939393</color>
      <color name="gray_dark" >#5F5F5F</color>
      <color name="black"     >#323232</color>

      <!-- basic colors -->
      <color name="green">#27D34D</color>
      <color name="blue">#2A91BD</color>
      <color name="orange">#FF9D2F</color>
      <color name="red">#FF432F</color>

  </resources>
```

向应用设计者那里要这个调色板，名称不需要跟 `"green"`、`"blue"` 等等相同。`"brand_primary"`、`"brand_secondary"`、`"brand_negative"` 这样的名字也是完全可以接受的。像这样规范的颜色很容易修改或重构，会使应用一共使用了多少种不同的颜色变得非常清晰。通常一个具有审美价值的 UI 来说，减少使用颜色的种类是非常重要的。

> 注意：如果某些颜色和主题有关，那就单独写一个 `colors_theme.xml`。

##### 5.6.2 *dimens.xml

像对待 `colors.xml` 一样对待 `dimens.xml` 文件，与定义颜色调色板一样，你同时也应该定义一个空隙间隔和字体大小的“调色板”。 一个好的例子，如下所示：

```xml
<resources>
    <!-- font sizes -->
    <dimen name="font_22">22sp</dimen>
    <dimen name="font_18">18sp</dimen>
    <dimen name="font_15">15sp</dimen>
    <dimen name="font_12">12sp</dimen>
    <!-- typical spacing between two views -->
    <dimen name="spacing_40">40dp</dimen>
    <dimen name="spacing_24">24dp</dimen>
    <dimen name="spacing_14">14dp</dimen>
    <dimen name="spacing_10">10dp</dimen>
    <dimen name="spacing_4">4dp</dimen>
    <!-- typical sizes of views -->
    <dimen name="button_height_60">60dp</dimen>
    <dimen name="button_height_40">40dp</dimen>
    <dimen name="button_height_32">32dp</dimen>
</resources>
```

布局时在写 `margins` 和 `paddings` 时，你应该使用 `spacing_xx` 尺寸格式来布局，而不是像对待 `string` 字符串一样直接写值，像这样规范的尺寸很容易修改或重构，会使应用所有用到的尺寸一目了然。 这样写会非常有感觉，会使组织和改变风格或布局非常容易。

##### 5.6.3 *strings.xml

`<string>` 的 `name` 命名使用下划线命名法，采用以下规则：`{模块名_}逻辑名称`，这样方便同一个界面的所有 `string` 都放到一起，方便查找。

| 名称              | 说明           |
| ----------------- | -------------- |
| `main_menu_about` | 主菜单按键文字 |
| `friend_title`| 好友模块标题栏 |
|`friend_dialog_del` |好友删除提示  |
|`login_check_email`| 登录验证 |
|`dialog_title`| 弹出框标题|
|`button_ok` |确认键  |
|`loading` |加载文字|

##### 5.6.4 *style.xml

`<style>`的 `name` 命名使用大驼峰命名法，几乎每个项目都需要适当的使用 `styles.xml` 文件，因为对于一个视图来说，有一个重复的外观是很常见的，将所有的外观细节属性（`colors`、`padding`、`font`）放在 `styles.xml` 文件中。 在应用中对于大多数文本内容，最起码你应该有一个通用的 `styles.xml` 文件，例如：

```xml
<style name="ContentText">
    <item name="android:textSize">@dimen/font_normal</item>
    <item name="android:textColor">@color/basic_black</item>
</style>
```

应用到`TextView`中：

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/price"
    style="@style/ContentText"
    />
```

或许你需要为按钮控件做同样的事情，不要停止在那里，将一组相关的和重复`android:xxx`的属性放到一个能用的`<style>`中。

## 6 其他的一些建议规范

1. 合理布局，有效运用 `<merge>`、`<ViewStub>`、`<include>` 标签、clipRect等方法，尽量控制过度绘制在2次以内；

2. `Activity` 和 `Fragment` 里面有许多重复的操作以及操作步骤，所以我们都需要提供一个 `BaseActivity` 和 `BaseFragment`，让所有的 `Activity` 和 `Fragment` 都继承这个基类。

3. 方法基本上都按照调用的先后顺序在各自区块中排列；

4. 相关功能作为小区块放在一起（或者封装掉）；

5. 当一个类有多个构造函数，或是多个同名函数，这些函数应该按顺序出现在一起，中间不要放进其它函数；

6. 数据提供统一的入口。无论是在 MVP、MVC 还是 MVVM 中，提供一个统一的数据入口，都可以让代码变得更加易于维护。比如可使用一个 `DataManager`，把 `http`、`preference`、`eventpost`、`database` 都放在 `DataManager` 里面进行操作，只需要与 `DataManager`打交道；

7. 多用组合，少用继承；

8. 提取方法，去除重复代码。对于必要的工具类抽取也很重要，这在以后的项目中是可以重用的。

9. 可引入 `Dagger2` 减少模块之间的耦合性。`Dagger2` 是一个依赖注入框架，使用代码自动生成创建依赖关系需要的代码。减少很多模板化的代码，更易于测试，降低耦合，创建可复用可互换的模块；

10. 项目引入 `RxAndroid` 响应式编程，可以极大的减少逻辑代码；

11. 通过引入事件总线，如：`EventBus`、`AndroidEventBus`、`RxBus`，它允许我们在 `DataLayer` 中发送事件，以便 `ViewLayer` 中的多个组件都能够订阅到这些事件，减少回调；

12. 尽可能使用局部变量；

13. 及时关闭流；

14. 尽量减少对变量的重复计算；

    如下面的操作：

    ```java
for (int i = 0; i < list.size(); i++) {
    	...
    }
    ```
    
    建议替换为：

    ```java
for (int i = 0, len = list.size(); i < len; i++) {
    	...
}
    ```
    
15. 尽量采用懒加载的策略，即在需要的时候才创建；

    例如：

    ```java
String str = "aaa";
    if (i == 1) {
    	list.add(str);
    }
    ```
    
    建议替换为：

    ```java
if (i == 1) {
    	String str = "aaa";
    	list.add(str);
    }
    ```
    
16. 不要在循环中使用 `try…catch…`，应该把其放在最外层；

17. 使用带缓冲的输入输出流进行 IO 操作；

18. 尽量使用 `HashMap`、`ArrayList`、`StringBuilder`，除非线程安全需要，否则不推荐使用 `HashTable`、`Vector`、`StringBuffer`，后三者由于使用同步机制而导致了性能开销；

19. 尽量在合适的场合使用单例；

    使用单例可以减轻加载的负担、缩短加载的时间、提高加载的效率，但并不是所有地方都适用于单例，简单来说，单例主要适用于以下三个方面：

    1. 控制资源的使用，通过线程同步来控制资源的并发访问。
    2. 控制实例的产生，以达到节约资源的目的。
    3. 控制数据的共享，在不建立直接关联的条件下，让多个不相关的进程或线程之间实现通信。

20. 把一个基本数据类型转为字符串，`基本数据类型.toString()` 是最快的方式，`String.valueOf(数据)` 次之，`数据 + ""` 最慢；

21. 使用 AS 自带的 Lint 来优化代码结构（右键 module、目录或者文件，选择 Analyze -> Inspect Code）；

22. 使用内存泄漏的检测工具：LeakCanary；

23. jni加载.so文件尽量使用jni_onLoad()的方式，避免暴漏程序包名结构；

24. 使用cmake代替ndk-build，可以有效优化编译速度、减小.so文件大小；