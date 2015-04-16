package com.dw.party.mbmsupport.service.mbm;

import java.util.List;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dw.party.mbmsupport.dto.Result;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.SpiderControlerManager;
import com.mbm.elint.manager.business.TaskControlerManager;

/**
 * 本身是由管理员来调用，非管理员无需调用
 * 
 * @author zel
 */
@WebService(endpointInterface = "com.dw.party.mbmsupport.service.mbm.ISystemOperatorService", serviceName = "SystemOperatorService")
@Component(value = "systemOperatorService")
public class SystemOperatorServiceImpl implements ISystemOperatorService {
	private SpiderControlerManager spiderControlerManager;

	public SpiderControlerManager getSpiderControlerManager() {
		return spiderControlerManager;
	}

	@Autowired
	public void setSpiderControlerManager(
			SpiderControlerManager spiderControlerManager) {
		this.spiderControlerManager = spiderControlerManager;
	}

	private Result result;

	public Result getResult() {
		return result;
	}

	/**
	 * 在此处增加主要是为了补救一些特殊情况下程序造成的bug
	 */
	private TaskControlerManager taskControlerManager;

	public TaskControlerManager getTaskControlerManager() {
		return taskControlerManager;
	}

	@Autowired
	public void setTaskControlerManager(
			TaskControlerManager taskControlerManager) {
		this.taskControlerManager = taskControlerManager;
	}

	@Autowired
	public void setResult(Result result) {
		this.result = result;
	}

	public void getStaus() {

	}

	/**
	 * 开启抓取系统方法,若已启动，则要先stop才能再调用该方法
	 */
	public Result start() {
		this.result = this.spiderControlerManager.start();
		return result;
	}

	/**
	 * 停止抓取系统方法,若已关闭，则要先start才能再调用该方法
	 */
	public Result stop() {
		// TODO Auto-generated method stub
		this.result = this.spiderControlerManager.stop();
		return result;
	}

	/**
	 * 程序的bug导致的缓存映射与数据库不一致
	 */
	// 增加微博用户采集任务(一次性任务) 根据微博用户的地址列表采集这些微博用户，如果这些微博用户已存在，请忽略。
	public Result addAccountGrabTask(List<UrlPojo> accountUrls) {
		result = taskControlerManager.addAccountGrabTask4System(accountUrls);
		return result;
	}
}