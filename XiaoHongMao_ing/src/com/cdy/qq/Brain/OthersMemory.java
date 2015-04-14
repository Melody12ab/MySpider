package com.cdy.qq.Brain;


/**
 * @author dongyu.cai
 * 其他第三方问答平台的相关记忆内存空间
 */
public class OthersMemory {
	
	
	public AskAndAnswerMODEL getBaiDuZhiDaoSearchResult(String question) {
			return Brain.getInstance().askBaiDuZhiDao(question);
	}
	
}
