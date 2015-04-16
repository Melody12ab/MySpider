package com.dw.party.mbmsupport.global;

/**
 * 主要定义一些全局静态变量，分布在各自的内部静态类中
 * 
 * @author zel
 * 
 */
public class BlogRemoteDefine implements java.io.Serializable {
	// 序列化标志
	private static long serialVersionUID = 23920343032L;

	/**
	 * 基础取值定义
	 * 
	 * @author zel
	 */
	public static class BASE {
		/**
		 * 默认的一页返回的数据的条目
		 */
		public static java.lang.Long DEFAULT_NUM = 15L;
	}

	/**
	 * 基本结果定义
	 * 
	 * @author zel
	 */
	public static class BASE_RESULT_CODE {
		// 失败
		public static java.lang.String FAULT = "fault";
		// 成功
		public static java.lang.String SUCCESS = "success";
	}

	/**
	 * 微博类型定义
	 * 
	 * @author zel
	 * 
	 */
	public static class BLOG_TYPE {
		/**
		 * 新浪微博
		 */
		public static int SINA = 1;
		/**
		 * 腾讯微博
		 */
		public static int TT = 2;
	}

	/**
	 * 采集状态的结果定义
	 * 
	 * @author zel
	 */
	public static class GRAB_RESULT {
		/**
		 * 正在排队进入采集队列
		 */
		public static java.lang.String WAITING = "正在排队";
		/**
		 * 正在采集
		 */
		public static java.lang.String DOING = "正在采集";
		/**
		 * 采集错误
		 */
		public static java.lang.String ERROR = "账号地址错误 ";
		/**
		 * 不在采集任务列表中
		 */
		public static java.lang.String NONE = "该url不在采集任务列表中";
		/**
		 * 采集完成
		 */
		public static java.lang.String FINISHED = "采集完成";
		/**
		 * 系统未启动，无法对任何请求应答
		 */
		public static java.lang.String SystemNotRunning = "抓取系统未开启";
	}

	/**
	 * 性别定义
	 * 
	 * @author zel
	 */
	public static class SEX {
		/**
		 * 女
		 */
		public static int FEMALE = 2;
		/**
		 * 男
		 */
		public static int MALE = 1;
		/**
		 * 未知
		 */
		public static int UNKNOW = 0;
	}

	/**
	 * 帐户信息排序名称定义-微博用户
	 * 
	 * @author zel
	 */
	public static class SORT_NAME_ACCOUNT {
		/**
		 * 排序字段--关注数
		 */
		public static java.lang.String SORT_ARG_NAME_ATTENTIONNUM = "attentionNum";
		/**
		 * 排序字段--发布的信息数
		 */
		public static java.lang.String SORT_ARG_NAME_BLOGINFONUM = "blogInfoNum";
		/**
		 * 排序字段--粉丝数
		 */
		public static java.lang.String SORT_ARG_NAME_FANSNUM = "fansNum";
		/**
		 * 排序字段--最新发布信息的时间
		 */
		public static java.lang.String SORT_ARG_NAME_PUBLISHINFOTIME = "publishInfoTime";
	}

	/**
	 * 微博内容排序名称定义-微博内容
	 * 
	 * @author zel
	 */
	public static class SORT_NAME_INFO {
		/**
		 * 排序字段--采集时间
		 */
		public static java.lang.String SORT_ARG_NAME_GRABTIME = "insertTime";
		/**
		 * 排序字段--发布时间
		 */
		public static java.lang.String SORT_ARG_NAME_PUBLISHTIME = "publishtime";
		/**
		 * 排序字段--转发数
		 */
		public static java.lang.String SORT_ARG_NAME_ConvertNum = "transmit";
		/**
		 * 排序字段--评论数
		 */
		public static java.lang.String SORT_ARG_NAME_CommentNum = "discuss";
	}

	/**
	 * 排序方式定义
	 * 
	 * @author zel
	 */
	public static class SORT_TYPE {
		/**
		 * 升序
		 */
		public static java.lang.String ASC = "asc";
		/**
		 * 降序
		 */
		public static java.lang.String DESC = "desc";

	}

}
