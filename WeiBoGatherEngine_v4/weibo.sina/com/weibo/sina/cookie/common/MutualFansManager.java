package com.weibo.sina.cookie.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.elint.entity.Mutualfans;
import com.mbm.elint.manager.database.PersonAttentionManager;

/**
 * 作互粉的计算
 * 
 * @author zel
 */
@Component
@Scope(value = "prototype")
public class MutualFansManager {
	@Autowired
	private PersonAttentionManager personAttentionManager;// 做互粉等的db save

	// operator

	public void processMutualFans(String oid, String attentionUids,
			String fansUids) {
		// 如果有一个集合未得到，则直接不处理
		if (attentionUids == null || attentionUids.equals("")
				|| fansUids == null || fansUids.equals("")) {
			return;
		}
		String[] attentionUidsArray = attentionUids.split("#");
		String[] fansUidsArray = fansUids.split("#");

		HashSet<String> uidSet = new HashSet<String>();

		int tempListSize = attentionUidsArray.length;
		for (int i = 0; i < tempListSize; i++) {// 将关注列表加入去重集合
			uidSet.add(attentionUidsArray[i]);
		}

		tempListSize = fansUidsArray.length;
		List<Mutualfans> mutualFansList = new ArrayList<Mutualfans>();
		for (int i = 0; i < tempListSize; i++) {// 将关注列表加入去重集合
			if (uidSet.contains(fansUidsArray[i])) {// 说明此时互粉的,将它与oid一起存入数据库
				mutualFansList.add(new Mutualfans(oid, fansUidsArray[i],
						new Date()));
			}
		}
		this.personAttentionManager.saveMutualfansList(mutualFansList);
	}

}
