201411/20
1.加入来自renren-fenqi-service的thirdparty包
	a.提供二维码功能
===================================================================
20140827
1.DBUtil新增一个count方法用来技术
===================================================================
20140813
1.修改StringUtil类，加强cutString方法，支持起始下标和左标点
2.新增countStrExts方法统计source字符串中str出现的次数
===================================================================	
20140420
1.修改FtpUtil类
	a.限制访问级别为友好
	b.去掉了一些冗余、不常用、容易误解的方法
	c.交由FtpUtilManager管理器类管理使用
2.新增SFtpUtil类，功能与FtpUtil类似，这是个安全的ftp工具，使用sftp协议
	a.访问级别同样是友好
	b.交由SFtpUtilManager管理使用
4.新增FtpUtilManager类，管理使用FtpUtil，其实是把一些分散的Ftp操作打包到了这个类的每个方法里
	a.访问级别友好
	b.交由FtpManager管理
5.新增SFtpUtilManager类，管理使用SFtpUtil，其实是把一些分散的SFtp操作打包到了这个类的每个方法里
	a.访问级别友好
	b.交由FtpManager管理
6.新增FtpManager类，用单例模式来创建FtpUtilManager和SFtpUtilManager两种管理器
	a.FtpUtilManager和SFtpUtilManager本来就是访问及为友好，所以外部使用它们，必须通过总的FtpManager来单例使用
7.新增FtpOrder类，意思是Ftp传输时候使用的快递单
	a.规避使用Map存放远程和本地路径时候，相同的key会被覆盖的问题
	b.可以扩充快递时候的信息，不仅仅是远程和本地的路径
8.新增FtpProgressInfo类，支持多线程同步
	a.用于监控传输过程的信息
9.新增Excel4DBUtil,提供excel与数据库表的交互
	a.从数据库表导出数据成excel
	b.将excel数据更新到数据库表
	c.基于excel的复杂增删改操作
10.新增DES2Util工具类，提供DES加密
===================================================================
20131031	caidongyu
1.修改FileUtil 类
	a.新增方法getFileType,获取方法后缀名
	b.新增方法getFileNameWithoutType,获取没有后缀的方法名
===================================================================
20131030	caidongyu
1.新增CopyUtil 类

20130819	caidongyu
1.修改DateUtil类
	a.新增方法daysBetween,可以比较两个日期的相差天数
===================================================================
20130608	caidongyu
1.修改NumberUtil类
	a.修改toBinStr方法为custNumberToBinaryStrArray方法








	