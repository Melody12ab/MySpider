package com.mbm.elint.manager.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.weibo.common.controler.SpiderControler;
import com.weibo.common.utils.StaticValue;

@Component
public class SpiderControlerManager {
	@Autowired
	private SpiderControler spiderControler;
	@Autowired
	private Result result;

	public Result start() {
		if (StaticValue.isRunning) {
			this.result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			this.result.setInfo("抓取系统正在运行，请停止后再重新启动");
			return result;
		}
		boolean flag = false;
		try {
			flag = this.spiderControler.startSpider();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag == true) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("抓取程序启动成功!");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("抓取程序启动失败!");
		}
		return result;
	}

	public Result stop() {
		if(!StaticValue.isRunning){
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("抓取没有启动无需终止!");
		}
		if (this.spiderControler.stopSpider()) {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("关闭抓取程序成功!");
		} else {
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
			result.setInfo("关闭抓取程序启动失败!");
		}
		return result;
	}

}
