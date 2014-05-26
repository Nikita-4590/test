package com.hrs.mediarequesttool.dals;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.hrs.mediarequesttool.common.Page;
import com.hrs.mediarequesttool.common.PagingResult;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.AccountMapper;
import com.hrs.mediarequesttool.pojos.Account;

@Service
public class AccountDAL extends AbstractDAL<AccountMapper> {

	// restricted constructor
	public AccountDAL() {
	}

	public AccountDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, AccountMapper.class);
	}

	public List<Account> getAll(int companyId, String sort, String direction, String mediaName, String loginId, String shopName) throws GenericException {
		try {
			openSession();
			mediaName = "%" + (mediaName == null ? "" : mediaName) + "%";
			mediaName = mediaName.replace(' ', '%');
			loginId = "%" + (loginId == null ? "" : loginId) + "%";
			loginId = loginId.replace(' ', '%');
			shopName = "%" + (shopName == null ? "" : shopName) + "%";
			shopName = shopName.replace(' ', '%');

			return mapper.getAll(companyId, sort, direction, mediaName, loginId, shopName);
		} catch (Exception e) {
			throw new GenericException(e, this.getClass());
		} finally {
			closeSession();
		}
	}

	public PagingResult<Account> paging(int companyId, int page, String sort, String direction, String mediaName, String loginId, String shopName, Boolean status) throws GenericException {
		try {
			openSession();
			mediaName = (mediaName == null ? "" : mediaName);
			// mediaName = mediaName.replace(' ', '%');
			loginId = "%" + (loginId == null ? "" : loginId) + "%";
			loginId = loginId.replace(' ', '%');
			shopName = "%" + (shopName == null ? "" : shopName) + "%";
			shopName = shopName.replace(' ', '%');

			PagingResult<Account> result = new PagingResult<Account>();

			Page pagingSetting = new Page(page);

			int totalRecord = mapper.count(companyId, mediaName, loginId, shopName, status);

			result.setPage(page, totalRecord, pagingSetting.getLimit());

			if (!result.isExceed()) {
				result.setList(mapper.paging(companyId, new Page(page), sort, direction, mediaName, loginId, shopName, status));
			}

			return result;
		} catch (Exception e) {
			throw new GenericException(e, this.getClass());
		} finally {
			closeSession();
		}
	}
}
