package com.mbm.LJSearch.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.dw.party.mbmsupport.dto.SearchAccountResultPojo;
import com.dw.party.mbmsupport.dto.SearchContentResultPojo;
import com.dw.party.mbmsupport.dto.SupportSort;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.dw.party.mbmsupport.global.Emotion;
import com.mbm.LJSearch.core.LJSearchProcessor;
import com.mbm.elint.entity.dao.mongoDB.TopicDocDao;
import com.mbm.elint.entity.mongoDB.TopicDoc;
import com.mbm.elint.entity.util.MyJson;
import com.mbm.elint.manager.business.UrlToUidManager;
import com.mbm.elint.mongoInter.impl.MyPageable;
import com.weibo.common.utils.DateUtil;
import com.weibo.common.utils.LJSearch_StaticValue;
import com.weibo.common.utils.ResultPage;
import com.weibo.common.utils.StaticValue;
import com.weibo.utils.sina.MyStringUtils;

@Component
public class LJSearchManager {
	@Autowired
	private LJSearchProcessor searchProcessor;
	@Autowired
	private UrlToUidManager uuMapManager;
	@Autowired
	private MyJson myJSON;
	@Autowired
	private TopicDocDao topicDocDao;

	/**
	 * 专为解决回传uids
	 * 
	 * @param accountUrls
	 * @param sendPage
	 * @return
	 */
	public ResultPage<SearchAccountResultPojo> findAccountInfoPageByUids(
			String uids, ResultPage<SearchAccountResultPojo> sendPage) {
		String query = "[field] uid [or] " + uids;
		sendPage = searchProcessor.searchAccountInfo(sendPage, query);
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findAccountInfoPage(
			String accountUrls, ResultPage<SearchAccountResultPojo> sendPage) {
		String query = "[field] uid [or] " + accountUrls;
		sendPage = searchProcessor.searchAccountInfo(sendPage, query);
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchContentResultPojo> findContentInfoPage(
			List<String> accountUrls, List<String> keys, String group,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage) {
		String query = "";
		String temp = "";
		String groupQuery = "";

		String listAllQuery = " [cmd] listall ";

		if (keys != null && keys.size() != 0) {
			for (String temp_url : keys) {
				temp += (temp_url + " ");
			}
			temp = temp.trim();
			query += "[field] * [or] " + temp;
			temp = "";
		}
		if (accountUrls != null && accountUrls.size() != 0) {
			/*
			 * 这里是对UUMap加之后的改良，最后都通过uid来查询数据
			 */
			for (String temp_url : accountUrls) {
				temp += (uuMapManager.getValueByKey(MyStringUtils
						.getUrlRemoveSlash(temp_url)) + " ");
			}
			temp = temp.trim();
			query += " [field] person_id [or] " + temp;
		}

		if (group != null && group.trim().length() > 0) {
			group = group.trim();
			// query += " [field] groupName [or] " + group;
			// 对组搜索进行修改
			groupQuery = " [field] groupName [or] " + group;
		}
		String beginTime = "";
		String endTime = "";
		/**
		 * 以下是针对publishTime做的匹配
		 */
		if (startPublishTime != null && endPublishTime == null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startPublishTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime == null && endPublishTime != null) {
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endPublishTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime != null && endPublishTime != null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		}
		/**
		 * 以下是针对grabTime做的匹配
		 */
		if (startGrabTime != null && endGrabTime == null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			// endTime = DateUtil.getRangDateFormat(new Date());
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startGrabTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime == null && endGrabTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endGrabTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime != null && endGrabTime != null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		}

		/**
		 * 以下是针对sort做的匹配
		 */
		if (sort != null) {
			for (SupportSort ss : sort) {
				if (query.trim().length() != 0) {
					temp = " [sort] " + ss.getSortArgName() + " "
							+ ss.getSortType();
					query += temp;
				}
				listAllQuery += " [sort] " + ss.getSortArgName() + " "
						+ ss.getSortType();
			}
		}
		if (groupQuery.trim().length() == 0) {
			sendPage = searchProcessor.searchContentInfo(sendPage,
					LJSearch_StaticValue.contentQueryFormate_no_join_begin
							.replace("$1", query));
		} else if (query.trim().length() == 0 && groupQuery.trim().length() > 0) {
			sendPage = searchProcessor.searchContentInfo(sendPage,
					LJSearch_StaticValue.contentQueryFormate_join_begin
							.replace("$1", listAllQuery)
							+ LJSearch_StaticValue.contentQueryFormate_join_end
									.replace("$1", groupQuery));
		} else {
			sendPage = searchProcessor.searchContentInfo(sendPage,
					LJSearch_StaticValue.contentQueryFormate_join_begin
							.replace("$1", query)
							+ LJSearch_StaticValue.contentQueryFormate_join_end
									.replace("$1", groupQuery));
		}
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	/**
	 * 重点在flag,标志是哪些特殊的请求过来的，0代表正常请求，非0代表各种定制请求,如1代表华为的请求
	 * 
	 * @param accountUrls
	 * @param keys
	 * @param startPublishTime
	 * @param endPublishTime
	 * @param startGrabTime
	 * @param endGrabTime
	 * @param sort
	 * @param sendPage
	 * @param flag
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findNewestContentInfoPage(
			List<String> accountUrls, List<String> keys,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage, int flag) {
		String query = "";

		if (flag != 0) {// 代表是否正常请求,暂只为华为服务
			query = "<index><no>1</no><main>1</main><cmd> [cmd] listall [sort] reverse_docid</cmd></index>";
			sendPage = searchProcessor.searchNewestContentInfo(sendPage, query);
			sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
			sendPage.setInfo("搜索成功");
			return sendPage;
		}

		String temp = "";
		String groupQuery = "";

		String listAllQuery = " [cmd] listall ";

		int keywords_number_count = 0;

		if (keys != null && keys.size() != 0) {
			for (String temp_url : keys) {
				temp += (temp_url + " ");
				keywords_number_count++;
				if (keywords_number_count >= StaticValue.max_or_command_keyword_number) {
					break;
				}
			}
			temp = temp.trim();
			query += "[field] * [or] " + temp;
			temp = "";
		}
		if (accountUrls != null && accountUrls.size() != 0) {
			/*
			 * 这里是对UUMap加之后的改良，最后都通过uid来查询数据
			 */
			for (String temp_url : accountUrls) {
				temp += (uuMapManager.getValueByKey(MyStringUtils
						.getUrlRemoveSlash(temp_url)) + " ");
			}
			temp = temp.trim();
			query += " [field] person_id [or] " + temp;
		}
		// if (group != null && group.trim().length() > 0) {
		// group = group.trim();
		// // query += " [field] groupName [or] " + group;
		// // 对组搜索进行修改
		// groupQuery = " [field] groupName [or] " + group;
		// }
		String beginTime = "";
		String endTime = "";
		/**
		 * 以下是针对publishTime做的匹配
		 */
		if (startPublishTime != null && endPublishTime == null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startPublishTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime == null && endPublishTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endPublishTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime != null && endPublishTime != null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		}
		/**
		 * 以下是针对grabTime做的匹配
		 */
		if (startGrabTime != null && endGrabTime == null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			// endTime = DateUtil.getRangDateFormat(new Date());
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startGrabTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime == null && endGrabTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endGrabTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime != null && endGrabTime != null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		}

		/**
		 * 以下是针对sort做的匹配
		 */
		if (sort != null) {
			for (SupportSort ss : sort) {
				if (query.trim().length() != 0) {
					temp = " [sort] " + ss.getSortArgName() + " "
							+ ss.getSortType();
					query += temp;
				}
				listAllQuery += " [sort] " + ss.getSortArgName() + " "
						+ ss.getSortType();
			}
		}
		if (groupQuery.trim().length() == 0) {
			sendPage = searchProcessor.searchNewestContentInfo(sendPage,
					LJSearch_StaticValue.contentQueryFormate_no_join_begin
							.replace("$1", query));
		}
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	/**
	 * 为情感正负面搜索而加
	 * 
	 * @param accountUrls
	 * @param keys
	 * @param startPublishTime
	 * @param endPublishTime
	 * @param startGrabTime
	 * @param endGrabTime
	 * @param sort
	 * @param sendPage
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findNewestContentInfoPageWithEmotion(
			List<String> accountUrls, List<String> keys,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage, Emotion emotion) {
		String query = "";
		String temp = "";
		String groupQuery = "";

		String listAllQuery = " [cmd] listall ";

		if (keys != null && keys.size() != 0) {
			for (String temp_url : keys) {
				temp += (temp_url + " ");
			}
			temp = temp.trim();
			query += "[field] * [or] " + temp;
			temp = "";
		}
		if (accountUrls != null && accountUrls.size() != 0) {
			/*
			 * 这里是对UUMap加之后的改良，最后都通过uid来查询数据
			 */
			for (String temp_url : accountUrls) {
				temp += (uuMapManager.getValueByKey(MyStringUtils
						.getUrlRemoveSlash(temp_url)) + " ");
			}
			temp = temp.trim();
			query += " [field] person_id [or] " + temp;
		}
		// if (group != null && group.trim().length() > 0) {
		// group = group.trim();
		// // query += " [field] groupName [or] " + group;
		// // 对组搜索进行修改
		// groupQuery = " [field] groupName [or] " + group;
		// }
		String beginTime = "";
		String endTime = "";
		/**
		 * 以下是针对publishTime做的匹配
		 */
		if (startPublishTime != null && endPublishTime == null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			// endTime = DateUtil.getRangDateFormat(new Date());
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startPublishTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime == null && endPublishTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endPublishTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime != null && endPublishTime != null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		}
		/**
		 * 以下是针对grabTime做的匹配
		 */
		if (startGrabTime != null && endGrabTime == null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			// endTime = DateUtil.getRangDateFormat(new Date());
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startGrabTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime == null && endGrabTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endGrabTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime != null && endGrabTime != null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		}

		if (emotion != null && query.trim().length() != 0) {
			if (emotion == Emotion.Good_Effect) {
				listAllQuery += "  " + StaticValue.Good_Effect + " ";
			} else {
				listAllQuery += "  " + StaticValue.Bad_Effect + " ";
			}
		}

		/**
		 * 以下是针对sort做的匹配
		 */
		if (sort != null) {
			for (SupportSort ss : sort) {
				if (query.trim().length() != 0) {
					temp = " [sort] " + ss.getSortArgName() + " "
							+ ss.getSortType();
					query += temp;
				}
				listAllQuery += " [sort] " + ss.getSortArgName() + " "
						+ ss.getSortType();
			}
		}
		if (groupQuery.trim().length() == 0) {
			sendPage = searchProcessor.searchNewestContentInfo(sendPage,
					LJSearch_StaticValue.contentQueryFormate_no_join_begin
							.replace("$1", query));
		}
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findAttentionInfoPage(
			String attentionUrl, ResultPage<SearchAccountResultPojo> sendPage) {
		String query = "[field] attentionUrl [or] " + attentionUrl;
		sendPage = searchProcessor.searchAttentionInfo(sendPage,
				LJSearch_StaticValue.attentionQueryFomate.replace("$1",
						"[cmd] listall").replace("$2", query));
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findAccountAttentionList(
			String accountUrl, ResultPage<SearchAccountResultPojo> sendPage) {
		sendPage = this.findAttentionInfoPage(accountUrl, sendPage);
		return sendPage;
	}

	/**
	 * 为转发获取原博文而添加
	 * 
	 * @param accountUrl
	 * @param sendPage
	 * @return
	 */
	public ResultPage<SearchContentResultPojo> findDocInfoListByDocUrls(
			List<String> docUrls, ResultPage<SearchContentResultPojo> sendPage) {
		String temp = "";
		String query = null;
		if (docUrls != null && docUrls.size() > 0) {
			for (String docUrl : docUrls) {
				docUrl = docUrl.trim();
				temp += docUrl + " ";
			}

			query = "[field] docurl [or] " + temp;
		}
		sendPage = this.searchProcessor.searchContentInfo(sendPage, query);
		return sendPage;
	}

	/**
	 * 搜索，统计特征词
	 * 
	 * @param args
	 * @throws Exception
	 */
	public ResultPage<SearchContentResultPojo> findPersonFeatureWordsBySendUrl(
			String sendUrl, ResultPage<SearchContentResultPojo> sendPage) {
		String temp = null;
		String query = null;
		if (sendUrl != null && sendUrl.trim().length() != 0) {
			/*
			 * 这里是对UUMap加之后的改良，最后都通过uid来查询数据
			 */
			temp = (uuMapManager.getValueByKey(MyStringUtils
					.getUrlRemoveSlash(sendUrl)) + " ");
			temp = temp.trim();
			query = "[field] person_id [and] " + temp
					+ " [sort] publishtime desc";
		} else {
			query = "[field] person_id [and] " + "null"
					+ " [sort] publishtime desc";
		}

		sendPage = searchProcessor.searchPersonFeatureWords(sendPage,
				LJSearch_StaticValue.contentQueryFormate_no_join_begin.replace(
						"$1", query));
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	/**
	 * 向搜索服务发送删除指定条件的记录
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void deleteGroupAndUserItems(String query) {
		String pack_query = query;
		try {

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 以下是为缔元信添加的
	 */
	public ResultPage<SearchContentResultPojo> findContentInfoPage(
			MyJson myJSON, ResultPage<SearchContentResultPojo> sendPage) {

		String temp = "";

		String query = " [field] grab_method [or] media enterprise profession";

		if (myJSON != null) {
			// 首先截取关键字
			String keyWord = myJSON.getStringByKey("keyWord");
			if (keyWord != null && keyWord.trim().length() > 0) {
				String[] keywords = keyWord.split(" ");
				keyWord = "";
				for (String key : keywords) {
					keyWord += "\"" + key + "\" ";
				}
				query += " [field] * [or] " + keyWord;
			}

			String startPublishTime = myJSON.getStringByKey("beginTime");
			String endPublishTime = myJSON.getStringByKey("endTime");

			String beginTime = "";
			String endTime = "";
			/**
			 * 以下是针对publishTime做的匹配
			 */
			if (startPublishTime != null && endPublishTime == null) {
				beginTime = DateUtil.getRangDateFormat(new Date(Long
						.parseLong(startPublishTime)));
				endTime = DateUtil.getRangDateFormat(new Date());
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			} else if (startPublishTime == null && endPublishTime != null) {
				beginTime = "2000/01/01_00:00:00";
				endTime = DateUtil.getRangDateFormat(new Date(Long
						.parseLong(endPublishTime)));
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			} else if (startPublishTime != null && endPublishTime != null) {
				beginTime = DateUtil.getRangDateFormat(new Date(Long
						.parseLong(startPublishTime)));
				endTime = DateUtil.getRangDateFormat(new Date(Long
						.parseLong(endPublishTime)));
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		}

		/**
		 * 以下是针对sort做的匹配
		 */
		// for (SupportSort ss : sort) {
		if (query.trim().length() != 0) {
			temp = " [sort] " + "transmit" + " " + "desc";
			query += temp;
		}
		if (query.trim().length() > 0) {
			sendPage = searchProcessor.searchDocInfo(sendPage, query);
		}
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	/**
	 * for LingJoin
	 */
	public ResultPage<SearchContentResultPojo> findContentInfoPage(
			List<String> accountUrls, List<String> uids, List<String> keys,
			java.util.Date startPublishTime, java.util.Date endPublishTime,
			java.util.Date startGrabTime, java.util.Date endGrabTime,
			java.util.List<SupportSort> sort,
			ResultPage<SearchContentResultPojo> sendPage) {
		String query = "";
		String temp = "";
		if (keys != null && keys.size() != 0) {
			for (String temp_url : keys) {
				temp += (temp_url + " ");
			}
			temp = temp.trim();
			query += "[field] * [or] " + temp;
			temp = "";
		}
		if (accountUrls != null && accountUrls.size() != 0) {
			/*
			 * 这里是对UUMap加之后的改良，最后都通过uid来查询数据
			 */
			for (String temp_url : accountUrls) {
				temp += (uuMapManager.getValueByKey(MyStringUtils
						.getUrlRemoveSlash(temp_url)) + " ");
			}
			/**
			 * 在这里再加上uids
			 */
			temp = temp.trim();
			if (uids != null && uids.size() > 0) {
				for (String uid : uids) {
					temp += " " + uid;
				}
				temp = temp.trim();
			}
			query += " [field] person_id [or] " + temp;
		} else {
			temp = temp.trim();
			if (uids != null && uids.size() > 0) {
				for (String uid : uids) {
					temp += " " + uid;
				}
				temp = temp.trim();
				query += " [field] person_id [or] " + temp;
			}
		}

		// 对腾讯微博过滤
		query += StaticValue.NotQQQuery;

		String beginTime = "";
		String endTime = "";
		/**
		 * 以下是针对publishTime做的匹配
		 */
		if (startPublishTime != null && endPublishTime == null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			// endTime = DateUtil.getRangDateFormat(new Date());
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startPublishTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime == null && endPublishTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endPublishTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime != null && endPublishTime != null) {
			beginTime = DateUtil.getRangDateFormat(startPublishTime);
			endTime = DateUtil.getRangDateFormat(endPublishTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startPublishTime == null && endPublishTime == null) {
			endTime = DateUtil.getRangDateFormat(new Date());
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(new Date(),
							StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] publishtime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] publishtime [range] " + beginTime + " "
						+ endTime;
			}
		}
		/**
		 * 以下是针对grabTime做的匹配
		 */
		if (startGrabTime != null && endGrabTime == null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			// endTime = DateUtil.getRangDateFormat(new Date());
			endTime = DateUtil.getRangDateFormat(DateUtil.getAfterIntervalDate(
					startGrabTime, StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime == null && endGrabTime != null) {
			// beginTime = "2000/01/01_00:00:00";
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(endGrabTime,
							StaticValue.before_after_interval_time));
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime != null && endGrabTime != null) {
			beginTime = DateUtil.getRangDateFormat(startGrabTime);
			endTime = DateUtil.getRangDateFormat(endGrabTime);
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		} else if (startGrabTime == null && endGrabTime == null) {
			endTime = DateUtil.getRangDateFormat(new Date());
			beginTime = DateUtil.getRangDateFormat(DateUtil
					.getBeforeIntervalDate(new Date(),
							StaticValue.before_after_interval_time));
			if (query.trim().length() == 0) {
				query += " [cmd] listall [field] insertTime [range] "
						+ beginTime + " " + endTime;
			} else {
				query += " [field] insertTime [range] " + beginTime + " "
						+ endTime;
			}
		}

		/**
		 * 以下是针对sort做的匹配
		 */
		if (sort != null) {
			for (SupportSort ss : sort) {
				if (query.trim().length() != 0) {
					temp = " [sort] " + ss.getSortArgName() + " "
							+ ss.getSortType();
					query += temp;
				}
			}
		}
		sendPage = searchProcessor.searchNewestContentInfo(sendPage,
				LJSearch_StaticValue.contentQueryFormate_no_join_begin.replace(
						"$1", query));
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findContentInfoPage(String uids,
			ResultPage<SearchAccountResultPojo> sendPage) {
		String query = "[field] uid [or] " + uids;
		sendPage = searchProcessor.searchAccountInfo(sendPage, query);
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchAccountResultPojo> findAccountInfoPageForLingJoin(
			String keywords_name, ResultPage<SearchAccountResultPojo> sendPage,
			boolean isAll) {
		String query = null;
		if (isAll) {
			query = keywords_name;
		} else {
			query = " [field] * [or] " + keywords_name + StaticValue.NotQQQuery;
		}
		sendPage = searchProcessor.searchAccountInfo(sendPage, query);
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<SearchContentResultPojo> findAllContentInfoPage(
			SupportSort ss, ResultPage<SearchContentResultPojo> sendPage) {

		Date date = new Date();
		String endTime = DateUtil.getRangDateFormat(date);
		String beginTime = DateUtil.getRangDateFormat(DateUtil
				.getBeforeIntervalDate(date,
						StaticValue.before_after_interval_time_4_lingjoin));

		String query = " [cmd] listall [field] publishtime [range] "
				+ beginTime + " " + endTime;

		if (ss == null) {
			query += StaticValue.NotQQQuery + " [sort] " + " publishtime desc ";
		} else {
			query += StaticValue.NotQQQuery + " [sort] " + ss.getSortArgName()
					+ " " + ss.getSortType();
		}
		if (query.trim().length() > 0) {
			// sendPage = searchProcessor.searchContentInfo(sendPage,
			// query);
			sendPage = searchProcessor.searchNewestContentInfo(sendPage, query);
		}
		sendPage.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.SUCCESS);
		sendPage.setInfo("搜索成功");
		return sendPage;
	}

	public ResultPage<TopicDoc> findHotTopicInfoPage(
			java.util.List<SupportSort> sort, ResultPage<TopicDoc> sendPage) {
		/**
		 * 以下是针对sort做的匹配
		 */
		MyPageable myPage = null;
		if (sort != null) {
			if (sort.size() > 0) {
				SupportSort selfSort = sort.get(0);
				Sort mongo_sort = null;
				if (selfSort.getSortType().startsWith("d")) {
					mongo_sort = new Sort(
							org.springframework.data.domain.Sort.Direction.DESC,
							selfSort.getSortArgName());
				} else {
					mongo_sort = new Sort(
							org.springframework.data.domain.Sort.Direction.ASC,
							selfSort.getSortArgName());
				}
				myPage = new MyPageable(0, 1, StaticValue.hot_topic_number,
						mongo_sort);
				sendPage.setResult(topicDocDao.findTopN(myPage));
			}
		} else {
			myPage = new MyPageable(0, 1, StaticValue.hot_topic_number, null);
		}
		return sendPage;
	}
}
