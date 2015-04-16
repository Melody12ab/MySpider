package com.mbm.elint.manager.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.mbm.elint.entity.Group;
import com.mbm.elint.entity.dao.GroupDao;

@Component
@Transactional
public class GroupManager {
	private static Logger logger = LoggerFactory.getLogger(GroupManager.class);
	private GroupDao groupDao;
    private Result result;
    private GroupAndUserManager groupAndUserManager;
	public GroupAndUserManager getGroupAndUserManager() {
		return groupAndUserManager;
	}
	@Autowired
	public void setGroupAndUserManager(GroupAndUserManager groupAndUserManager) {
		this.groupAndUserManager = groupAndUserManager;
	}
	public Result getResult() {
		return result;
	}
    @Autowired
	public void setResult(Result result) {
		this.result = result;
	}

	public GroupDao getGroupDao() {
		return groupDao;
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void saveGroup(Group pojo) {
		this.groupDao.save(pojo);
	}
	
	public Result addGroup(String groupName) {
		List<Group> list=this.groupDao.find("select g from Group g where g.groupName='"+groupName+"'");
		if(list.size()>0){
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("分组已存在，无需创建!");
		}else {
			Group group=new Group();
			group.setGroupName(groupName);
			this.groupDao.save(group);
			
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("添加分组成功!");		
		}
		return result;
	}
	
	public Result removeGroup(String groupName) {
		List<Group> list=this.groupDao.find("select g from Group g where g.groupName='"+groupName+"'");
		if(list.size()>0){
			this.groupAndUserManager.removeGroupUsers(groupName);
			Group group=list.get(0);
			if(group!=null){
				this.groupDao.delete(group);			
			}
			//删除组成功
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("删除分组成功");
		}else {
			//组不存在，无须删除
//			result.setResultCode("2");
//			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
//			result.setInfo("分组不存在，删除成功!");
			/**
			 * 响应需求
			 */
			result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			result.setInfo("删除分组成功");
		}
		return result;
	}
	

}
