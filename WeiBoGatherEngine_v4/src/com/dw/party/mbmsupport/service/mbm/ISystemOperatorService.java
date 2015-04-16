package com.dw.party.mbmsupport.service.mbm;

import java.util.List;
import javax.jws.WebService;
import com.dw.party.mbmsupport.dto.Result;
import com.mbm.elint.entity.util.UrlPojo;

/**
 * 系统操作类，暂不对外开放
 * 
 * @author zel
 * 
 */
@WebService
public interface ISystemOperatorService {
	public Result start();

	public Result stop();

	public void getStaus();

	public Result addAccountGrabTask(List<UrlPojo> accountUrls);
}