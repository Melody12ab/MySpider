package com.dw.party.mbmsupport.service.mbm;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.Result;
import com.mbm.elint.manager.database.GroupAndUserManager;
import com.mbm.elint.manager.database.GroupManager;
import com.mbm.elint.manager.database.PersonInfoManager;

/**
 * 组服务接口类,当对URL进行分组操作时用此类,可以创建新组名或是删除，也可以将url地址add、remove等组操作
 * 
 * @author zel
 */
@WebService(endpointInterface = "com.dw.party.mbmsupport.service.mbm.IGroupingService", serviceName = "GroupingService")
@Component(value = "groupingService")
public class GroupingServiceImpl implements IGroupingService {
	private GroupManager groupManager;
	private GroupAndUserManager groupAndUserManager;
	private PersonInfoManager personInfoManager;

	public PersonInfoManager getPersonInfoManager() {
		return personInfoManager;
	}

	public void setPersonInfoManager(PersonInfoManager personInfoManager) {
		this.personInfoManager = personInfoManager;
	}

	private Result result;

	public Result getResult() {
		return result;
	}

	@Autowired
	public void setResult(Result result) {
		this.result = result;
	}

	public GroupAndUserManager getGroupAndUserManager() {
		return groupAndUserManager;
	}

	@Autowired
	public void setGroupAndUserManager(GroupAndUserManager groupAndUserManager) {
		this.groupAndUserManager = groupAndUserManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	@Autowired
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	/**
	 * 将一个url集合列表加入到指定的组中，该组名必须是存在的
	 */
	public Result addAccountToGroup(String group, List<String> accountUrls) {
		addGroup(group);
		result = this.groupAndUserManager.savePojoList(group, accountUrls);
		return result;
	}

	/**
	 * 创建一个新组
	 */
	public Result addGroup(String groupName) {
		result = this.groupManager.addGroup(groupName);
		return result;
	}

	/**
	 * 将一个url集合列表移出指定的组中，该组名必须是存在的
	 */
	public Result removeAccountFromGroup(String group, List<String> accountUrls) {
		result = this.groupAndUserManager.removePojoList(group, accountUrls);
		return result;
	}

	/**
	 * 删除指定的组名
	 */
	public Result removeGroup(String group) {
		result = this.groupManager.removeGroup(group);
		return result;
	}

	public void test() {
		System.out.println("-----------------" + this.groupAndUserManager);
		System.out.println("GroupingServiceImpl");
	}

	public Result testRetResult(List<String> urls) {
		Result result = new Result();
		result.setResultCode("200");
		result.setInfo("成功");

		return result;
	}

	public static void main(String[] args) {
		// GroupingServiceImpl mm = new GroupingServiceImpl();
		// mm.addGroup("First Group");
	}

}