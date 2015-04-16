package com.mbm.util;

import java.util.List;

import com.dw.party.mbmsupport.dto.Result;
import com.dw.party.mbmsupport.dto.ResultGrab;
import com.dw.party.mbmsupport.global.BlogRemoteDefine;
import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.elint.entity.util.UrlPojo;

public class VerifyValidateUtils {
	public static boolean verifyNullCollections(List<UrlPojo> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean verifyNullKeySet(List<KeywordPojo> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static Result getNotRunningResult(Result result) {
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
		result.setInfo("抓取系统没有启动，请启动抓取系统后，再做此项请求!");
		return result;
	}

	public static List<ResultGrab> getNotRunningResultGrabList(List<ResultGrab> resultGrabList,
			ResultGrab resultGrab) {
		resultGrab.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
		resultGrab.setInfo("抓取系统没有启动，请启动抓取系统后，再做此项请求!");
		resultGrabList.add(resultGrab);
		return resultGrabList;
	}
	
	public static List<Result> getNotRunningResultList(List<Result> resultList,
			Result result) {
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
		result.setInfo("抓取系统没有启动，请启动抓取系统后，再做此项请求!");
		resultList.add(result);
		return resultList;
	}

	public static Result getNullCollectionResult(Result result) {
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
		result.setInfo("列表不可为空!");
		return result;
	}

	public static List<Result> getNullConnectionResultList(
			List<Result> resultList, Result result) {
		result.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
		result.setInfo("添加的集合不可为空!");
		resultList.add(result);
		return resultList;
	}
	public static List<ResultGrab> getNullConnectionResultGrabList(
			List<ResultGrab> resultGrabList,
			ResultGrab resultGrab) {
		resultGrab.setResultCode(BlogRemoteDefine.BASE_RESULT_CODE.FAULT);
		resultGrab.setInfo("添加的集合不可为空!");
		resultGrabList.add(resultGrab);
		return resultGrabList;
	}
}
