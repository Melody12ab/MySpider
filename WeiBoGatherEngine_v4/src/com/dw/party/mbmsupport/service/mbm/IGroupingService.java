package com.dw.party.mbmsupport.service.mbm;

import java.util.List;

import javax.jws.WebService;

import com.dw.party.mbmsupport.dto.Result;
/**
 * 组操作服务类
 * @author zel
 *
 */
@WebService
public interface IGroupingService {
	/**
	 * 向分组增加微博用户
	 * 该方法向一个已有的分组中添加微博用户，当分组不存在时，请先创建分组。
	 * @param group
	 * @param accountUrls
	 * @return
	 */
	Result addAccountToGroup(java.lang.String group, java.util.List<java.lang.String> accountUrls);
	/**
	 *  增加分组
	 *  该方法为数据支撑系统增加一个分组信息，在分组已存在的情况下，请忽略。 
	 * @param group
	 * @return
	 */
	Result addGroup(java.lang.String group);
	/**
	 * 从分组中删除微博用户
	 * 该方法从一个已有的分组中删除微博用户，当分组不存在时，请忽略。 
	 * @param group
	 * @param accountUrls
	 * @return
	 */
	Result removeAccountFromGroup(java.lang.String group, java.util.List<java.lang.String> accountUrls);
	/**
	 *   删除分组
	 *   该方法从数据支撑系统中删除一个分组信息，在分组不存在的情况下，请忽略。 
	 * @param group
	 * @return
	 */
	Result removeGroup(java.lang.String group); 
	
	void test();
	
	Result testRetResult(List<String> urls);
}