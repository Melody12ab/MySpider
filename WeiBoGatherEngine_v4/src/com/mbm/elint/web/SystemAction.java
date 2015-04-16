package com.mbm.elint.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.mbm.elint.dao.HibernateGeneralDao;
import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.TaskPojo;
import com.mbm.elint.entity.util.UrlPojo;
import com.mbm.elint.manager.business.SpiderControlerManager;
import com.mbm.elint.manager.business.TaskControlerManager;

@Namespace("/")
@Results( { @Result(name = "success", location = "index.jsp"),
		@Result(name = "addTask", location = "addTask.jsp") })
public class SystemAction extends SimpleActionSupport {
	private static Logger logger = Logger.getLogger(SystemAction.class);
	@Autowired
	private SpiderControlerManager spiderControlerManager;
	@Autowired
	private TaskControlerManager taskControlerManager;
	private String flag;
	@Autowired
	private TaskPojo taskPojo;

	public TaskPojo getTaskPojo() {
		return taskPojo;
	}

	public void setTaskPojo(TaskPojo taskPojo) {
		this.taskPojo = taskPojo;
	}

	private String message;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String startSystem() {
		com.dw.party.mbmsupport.dto.Result result = spiderControlerManager
				.start();
		return "success";
	}

	public String addMyTask() {
		return "addTask";
	}

	private File txtFile;

	public File getTxtFile() {
		return txtFile;
	}

	public void setTxtFile(File txtFile) {
		this.txtFile = txtFile;
	}

	public String addTask() throws Exception {
		if ("1".equals(flag)) {
			StringBuilder urlString = new StringBuilder(taskPojo.getUrl());
			BufferedReader br = null;
			try {
				System.out.println("txtFile---" + txtFile);
				if (txtFile != null) {
					br = new BufferedReader(new InputStreamReader(
							new FileInputStream(txtFile)));
					String temp = null;
					while ((temp = br.readLine()) != null) {
						if (temp.trim().length() > 0)
							urlString.append("\n" + temp);
					}
				}
			} catch (Exception e) {
				logger.info("读取文件路径的URL时出现异常!");
			} finally {
				if (br != null)
					br.close();
			}
			// System.out.println(urlString);
			if (urlString == null || urlString.toString().trim().length() == 0) {
				message = "<script>alert('Are you kidding me!?');</script>";
			} else {
				List<UrlPojo> urls = new ArrayList<UrlPojo>();
				String temp_array[] = urlString.toString().split("\n");
				UrlPojo url = null;
				for (String temp : temp_array) {
					temp = temp.trim();
					if (temp != null && temp.length() > 0) {
						url = new UrlPojo(temp, 1);
						urls.add(url);
					}
				}
				if ("1".equals(taskPojo.getType())) {
					result = this.taskControlerManager.addAccountGrabTask(urls);
				} else if ("2".equals(taskPojo.getType())) {
					result = this.taskControlerManager
							.addAccountInfoGrabTask(urls);
				} else if ("3".equals(taskPojo.getType())) {
					result = this.taskControlerManager
							.addAccountListAttentionGrabTask(urls);
				} else if ("5".equals(taskPojo.getType())) {
					result = this.taskControlerManager
							.addCommentInfoGrabTask(urls);
				} else {
					List<KeywordPojo> keywords = new ArrayList<KeywordPojo>();
					KeywordPojo keyword = null;
					for (String temp : temp_array) {
						temp = temp.trim();
						// keyword = new KeywordPojo(temp, 1);
						// 改为周期性抓取
						keyword = new KeywordPojo(temp, 0);
						keywords.add(keyword);
					}
					if ("6".equals(taskPojo.getType())) {
						result = this.taskControlerManager
								.addTopicTitleInfoGrabTask(keywords);
					} else {
						result = this.taskControlerManager
								.addKeyWordsGrabTask(keywords);
					}
					keywords.clear();
					keywords = null;
				}
				if ("fault".equalsIgnoreCase(result.getResultCode())) {
					message = "<script>alert('请先开启抓取系统!');</script>";
				} else {
					message = "<script>alert('添加任务成功!');</script>";
				}
				urls.clear();
				urls = null;
			}
		}
		return "addTask";
	}

	@Autowired
	private HibernateGeneralDao generalDao;

	private PersonInfo person = new PersonInfo();

	public HibernateGeneralDao getGeneralDao() {
		return generalDao;
	}

	@Autowired
	public void setGeneralDao(HibernateGeneralDao generalDao) {
		this.generalDao = generalDao;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private com.dw.party.mbmsupport.dto.Result result;

	public com.dw.party.mbmsupport.dto.Result getResult() {
		return result;
	}

	public void setResult(com.dw.party.mbmsupport.dto.Result result) {
		this.result = result;
	}

	/**
	 * 开启抓取系统方法,若已启动，则要先stop才能再调用该方法
	 */
	public String start() {
		this.result = this.spiderControlerManager.start();
		if ("success".equals(result.getResultCode())) {
			message = "<script>alert('" + result.getInfo() + "');</script>";
		} else {
			message = "<script>alert('" + result.getInfo() + "');</script>";
		}
		// System.out.println("result code---"+result.getResultCode());
		// System.out.println("result info---"+result.getInfo());
		return "success";
	}
	
	public String pause() {
		this.result = this.spiderControlerManager.stop();
		if ("success".equals(result.getResultCode())) {
			message = "<script>alert('" + result.getInfo() + "');</script>";
		} else {
			message = "<script>alert('" + result.getInfo() + "');</script>";
		}
		// System.out.println("result code---"+result.getResultCode());
		// System.out.println("result info---"+result.getInfo());
		return "success";
	}

}
