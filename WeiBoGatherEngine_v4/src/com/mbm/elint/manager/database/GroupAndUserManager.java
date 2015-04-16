package com.mbm.elint.manager.database;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.mbm.LJSearch.manager.LJSearchManager;
import com.mbm.elint.entity.GroupAndPerson;
import com.mbm.elint.entity.PersonInfo;
import com.mbm.elint.entity.dao.GroupAndUserDao;
import com.mbm.elint.entity.dao.PersonInfoDao;
import com.mbm.elint.manager.business.UrlToUidManager;
import com.weibo.common.utils.DateUtil;
import com.weibo.common.utils.LJSearch_StaticValue;
import com.weibo.utils.sina.MyStringUtils;

@Component
@Transactional
public class GroupAndUserManager {
	private static Logger logger = LoggerFactory
			.getLogger(GroupAndUserManager.class);
	@Autowired
	private UrlToUidManager uuMapManager;
	private GroupAndUserDao groupAndUserDao;
	private Result result;
	private PersonInfoDao personInfoDao;
	@Autowired
	private LJSearchManager ljSearchManager;

	public PersonInfoDao getPersonInfoDao() {
		return personInfoDao;
	}

	@Autowired
	public void setPersonInfoDao(PersonInfoDao personInfoDao) {
		this.personInfoDao = personInfoDao;
	}

	public Result getResult() {
		return result;
	}

	@Autowired
	public void setResult(Result result) {
		this.result = result;
	}

	public GroupAndUserDao getGroupAndUserDao() {
		return groupAndUserDao;
	}

	@Autowired
	public void setGroupAndUserDao(GroupAndUserDao groupAndUserDao) {
		this.groupAndUserDao = groupAndUserDao;
	}

	public void savePojo(GroupAndPerson pojo) {
		this.groupAndUserDao.save(pojo);
	}

	public Result savePojoList(String groupName, List<String> accountUrls) {
		List<GroupAndPerson> list = null;
		for (String url : accountUrls) {
			url = MyStringUtils.getUrlRemoveSlash(url);
			list = this.groupAndUserDao
					.find("select g from GroupAndPerson g where g.groupName='"
							+ groupName + "' and g.userUrl='" + url + "'");
			if (list.size() > 0) {
				/**
				 * 如果这个用户已经在这个组里了，不做操作
				 */
			} else {
				GroupAndPerson groupanduser = new GroupAndPerson();
				groupanduser.setGroupName(groupName);
				groupanduser.setUserUrl(url);
				try {
					groupanduser.setUrl_id_long(Long.parseLong(uuMapManager
							.getValueByKey(url)));
				} catch (Exception e) {
					logger.info(e.getLocalizedMessage());
					logger.info("groupanduser设置url_id_long时出现错误，请修正,url---"
							+ url + "--对的uid取得的是"
							+ uuMapManager.getValueByKey(url));
				}
				this.groupAndUserDao.save(groupanduser);
			}
		}
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		result.setInfo("组用户添加成功!");
		return result;
	}

	public Result removePojoList(String groupName, List<String> accountUrls) {
		List<GroupAndPerson> list = null;
		for (String url : accountUrls) {
			url = MyStringUtils.getUrlRemoveSlash(url);
			list = this.groupAndUserDao
					.find("select g from GroupAndPerson g where g.groupName='"
							+ groupName + "' and g.userUrl='" + url + "'");
			if (list.size() > 0) {
				Iterator<GroupAndPerson> iter = list.iterator();
				while (iter.hasNext()) {
					GroupAndPerson groupanduser = iter.next();
					this.groupAndUserDao.delete(groupanduser);
					/**
					 * 在此处向搜索发命令，删除数据库中已经删除的记录
					 */
					String temp_del_query = "[cmd] del-where [field] groupName [and] "
							+ groupanduser.getGroupName()
							+ " [field] userUrl [and] "
							+ groupanduser.getUserUrl();
					temp_del_query = LJSearch_StaticValue.groupAndUserDeleteQueryFormate
							.replace("$1", temp_del_query);
					logger.info("发出的del-index语句是----"+temp_del_query);
					ljSearchManager.deleteGroupAndUserItems(temp_del_query);
				}
			}
		}
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		result.setInfo("该帐户列表已在组中删除!");

		return result;
	}

	public Result removeGroupUsers(String groupName) {
		int items = this.groupAndUserDao
				.batchExecute("delete from GroupAndPerson g where  g.groupName='"
						+ groupName + "'");
		System.out.println("共删除---" + items + "条");
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		result.setInfo("该帐户列表已在组中删除!");

		return result;
	}

}
