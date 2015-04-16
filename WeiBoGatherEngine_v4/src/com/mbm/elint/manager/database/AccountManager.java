package com.mbm.elint.manager.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dw.party.mbmsupport.dto.Result;
import com.mbm.elint.entity.SinaAccount;
import com.mbm.elint.entity.dao.AccountDao;
import com.dw.party.mbmsupport.global.*;

@Component
@Transactional
public class AccountManager {
	private static Logger logger = LoggerFactory
			.getLogger(AccountManager.class);

	private AccountDao accountDao;

	public AccountDao getAccountDao() {
		return accountDao;
	}

	@Autowired
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

}
