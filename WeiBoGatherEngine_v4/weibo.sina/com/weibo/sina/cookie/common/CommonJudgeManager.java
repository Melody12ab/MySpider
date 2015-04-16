package com.weibo.sina.cookie.common;

import org.apache.log4j.Logger;

import com.mbm.util.StringOperatorUtil;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.manager.SwtichUserThreadManager;
import com.weibo.common.utils.AnsjPaser;
import com.weibo.common.utils.FormatOutput;
import com.weibo.common.utils.HtmlParserUtil;

public class CommonJudgeManager {
	public static Logger logger = Logger.getLogger(CommonJudgeManager.class);

	public static boolean isNotExistUser(String content) {
		// 测试用户是否存在
		AnsjPaser userJudge = new AnsjPaser(
				"<span class=\"icon_delM\"></span><p>", "<", content,
				AnsjPaser.TEXTEGEXANDNRT);
		String judgeText = userJudge.getText();
		// System.out.println("judgeText------------" + judgeText);
		if (judgeText != null) {
			if (judgeText.trim().equals("抱歉，你访问的页面地址有误，或者该页面不存在")) {
				logger.info("抱歉，你访问的页面地址有误，或者该页面不存在");
				return true;
			}
			if (judgeText.trim().equals("抱歉，你所访问的用户不存在")) {
				logger.info("抱歉，你所访问的用户不存在");
				return true;
			}
			if (judgeText.trim().contains("抱歉，你当前访问的帐号异常，暂时无法访问")) {
				logger.info("抱歉，你当前访问的帐号异常，暂时无法访问");
				return true;
			}
			if (judgeText.trim().contains("抱歉，你的帐号存在异常，暂时无法访问")) {
				logger.info("抱歉，你的帐号存在异常，暂时无法访问");
				return true;
			}
		}
		return false;
	}

	/**
	 * 对是否是出现网络请求繁忙页做测试，如果出现则直接进行帐户切换
	 */
	private static HtmlParserUtil htmlParserUtil = new HtmlParserUtil();

	public static boolean isForbiddenToGrab(String content) {
		String title = htmlParserUtil.getTitleByLine(content);
		if ((!StringOperatorUtil.isBlank(title)) && title.contains("错误提示")) {
			// logger.info("发现请求繁忙，将进行一次线程中断!");
			return true;
		}
		return false;
	}

	/**
	 * 判断是否又抓回到种子帐户本身，说明出错误了!
	 */
	public static boolean isGrabCircle(String person_uid, String sendurl) {
		if (person_uid.equals(SpiderControler.isRunningAccountUid4Sina)
				&& !sendurl.equals(FormatOutput.urlPrefix
						+ SpiderControler.isRunningAccountUid4Sina)) {
			logger.info(sendurl + " --抓回到种子帐号了，估计是被反爬了!");
			return true;
		}
		return false;
	}
}
