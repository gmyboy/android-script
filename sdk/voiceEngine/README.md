## 一、语音识别

### 1.  [科大讯飞](https://www.xfyun.cn/services/voicedictation)

#### 1.1 语音听写

将短音频（≤60秒）精准识别成文字，除中文普通话和英文外，支持35个语种、24种方言和1个民族语言，实时返回结果，达到边说边返回的效果

* [在线](https://www.xfyun.cn/services/voicedictation)：支持多语种，不超过60s。

![微信截图_20210222113004](res\微信截图_20210222113004.png)

* [离线](https://www.xfyun.cn/services/offline_iat)：只支持中文。价格在https://console.xfyun.cn/services/esriat

* [离线命令词](https://www.xfyun.cn/services/commandWord)：只支持中文。

  ![微信截图_20210222115448](res\微信截图_20210222115448.png)

#### 1.2 语音转写

* [非实时转写(5小时以内)](https://www.xfyun.cn/services/lfasr): 语音转写（Long Form ASR）基于深度全序列卷积神经网络，将长段音频（5小时以内）数据转换成文本数据，为信息处理和数据挖掘提供基础

![微信截图_20210222114405](res\微信截图_20210222114405.png)

* [实时语音转写](https://www.xfyun.cn/services/rtasr): 实时语音转写（Real-time ASR）基于深度全序列卷积神经网络框架，通过 WebSocket 协议，建立应用与语言转写核心引擎的长连接，将音频流数据实时转换成文字流数据结果

  ![微信截图_20210222115654](res\微信截图_20210222115654.png)

#### 1.3 语音合成

* 在线: 支持中文、英文等多种语种的合成；将文字转化为自然流畅的人声，提供100+发音人供您选择，支持多语种、多方言和中英混合，可灵活配置音频参数。广泛应用于新闻阅读、出行导航、智能硬件和通知播报等场景。

  ![微信截图_20210222120255](res\微信截图_20210222120255.png)

  * 离线: 支持中文、英文等多种语种的合成；支持根据业务需求选择合适的音量、语速等属性；更有多种发音人音色供选择。

    ![image-20210222120427893](C:\Users\gumingyong\AppData\Roaming\Typora\typora-user-images\image-20210222120427893.png)
    
### 2.  [阿里](https://common-buy.aliyun.com/?commodityCode=nlsService#/open)

只支持中文，不支持离线识别。免费版本最大并发为10，商务专用版 最大为200/100两个并发。一句话为60s内语音。

![微信截图_20210222132842](res\微信截图_20210222132842.png)

### 3. 百度

#### 3.1 [语音识别](https://cloud.baidu.com/doc/SPEECH/s/Ek39uxgre)

价格参考 https://cloud.baidu.com/doc/SPEECH/s/Jk38lxn2j

* [短语音识别标准版](https://cloud.baidu.com/doc/SPEECH/s/dk38lxg4d): 短语音识别标准版可以将语音精准识别为文字，适用于手机语音输入、语音搜索、智能语音对话等场景。包含中文普通话输入法、英语、粤语、四川话、远场5个识别模型。其中Android，iOS，Linux SDK支持超过60秒的实时场语音识别。
* 短语音识别极速版(RESTFul api): 短语音识别极速版包含极速版输入法模型（普通话）。识别速度提升5倍，准确率相对提升15%。适用于对识别速度要求更高的人机对话等场景。
* 实时语音识别: 可以将音频流实时识别为文字，并返回每句话的开始和结束时间，适用于长句语音输入、音视频字幕、会议等场景。在线识别只支持中文。

#### 3.2 语音合成

* [在线](https://cloud.baidu.com/doc/SPEECH/s/Ck4nlz4cx)：目前只有中英文混合这一种语言，优先中文发音。价格参考https://cloud.baidu.com/doc/SPEECH/s/Nk38y8pjq
* [离线](https://cloud.baidu.com/doc/SPEECH/s/Qk4nwu25z)：离线语音合成SDK可直接在设备终端进行语音合成，无论何种网络状态(有网、无网、弱网)，价格参考https://cloud.baidu.com/doc/SPEECH/s/yk38y8pc1

#### 3.3 语音唤醒 

附带在语音识别中，不再另外收费

### 4. 腾讯

价格，免费额度：

- 录音文件识别免费额度为**每月**10小时。

- 一句话识别免费额度为**每月**5000次。

- 实时语音识别免费额度为**每月**5小时。

- 语音流异步识别免费额度为**每月**5小时。

- 录音文件识别极速版免费额度为**每月**5小时。

免费额度用完后开始计费https://cloud.tencent.com/document/product/1093/35686

#### 4.1 [一句话识别](https://cloud.tencent.com/document/product/1093/35646)

只支持中文。识别60s内的短语音，当音频放在请求body中传输时整个请求大小不能超过1M，当音频以url方式传输时，音频时长不可超过60s。接口请求频率限制：20次/每秒

#### 4.2 实时语音识别

本接口服务对实时音频流进行识别，同步返回识别结果，达到“边说边出文字”的效果。接口是 HTTP RESTful 形式，在使用该接口前，需要在语音识别控制台开通服务

### 5. [云知音](http://dev.hivoice.cn/)

语音识别支持普通话和英语，包括语音转写和语音合成

## 二. 文字识别

### 1. 科大讯飞

印刷文字识别，支持多语种，免费试用(离线版500装机量)，定制化需要访问https://console.xfyun.cn/services/minority_language

### 2. 百度[OCR](https://cloud.baidu.com/doc/OCR/s/Jk3h7xtsd)

通用版免费额度50,000次/天，价格参考https://ai.baidu.com/ai-doc/OCR/fk3h7xu7h



