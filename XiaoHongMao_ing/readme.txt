两个地址:
例子:
http://pan.baidu.com/s/1sj4hKvV#path=%252FQQRobot
开源项目imqq：
https://github.com/im-qq

有两种回复模式：
1.普通，回复小黄鸡的对话
2.#开头，执行命令。
想执行命令，需通过#password:获取限权，并5分钟清除一次所有限权
想回复群必须将群名称填入GROUP_FILTER中，用 ; 号分割


在config.properties中填入相关配置（中文请通过http://tool.chinaz.com/Tools/native_ascii.aspx转码）：
QQ= QQ号
PASSWORD= QQ密码
SHELL_PASSWORD= 管理密码
SINGLE=true （是否回复个人信息）
GROUP=true （是否回复群消息）
BLACKLIST=null （黑名单，填入用户昵称，用 ; 号分割）
GROUP_FILTER=null （群过滤器，用 ; 号分割）
DIRECTORY=f:/ （shell执行目录）