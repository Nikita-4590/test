package com.hrs.mediarequesttool.dals;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.mappers.UserMapper;
import com.hrs.mediarequesttool.pojos.User;

@Service
public class UserDAL extends AbstractDAL<UserMapper> {

	// restricted constructor
	public UserDAL() {
	}

	public UserDAL(SqlSessionFactory sessionFactory) {
		super(sessionFactory, UserMapper.class);
	}

	public User getbyUserID(String userID) throws GenericException {
		try {
			openSession();
			return mapper.getbyUserID(userID);
		} catch (Exception e) {
			throw new GenericException(e, UserDAL.class);
		} finally {
			closeSession();
		}
	}
	
	public List<User> getListDirector() throws GenericException {
		try {
			openSession();
			return mapper.getListDirector();
		} catch (Exception e) {
			throw new GenericException(e, UserDAL.class);
		} finally {
			closeSession();
		}
	}

	public void countLoginFailure(int id) throws GenericException {
		try {
			openSession();
			mapper.countLoginFailure(id);
		} catch (Exception e) {
			throw new GenericException(e, UserDAL.class);
		} finally {
			closeSession();
		}
	}

	public void updateLastLoginTime(int id) throws GenericException {
		try {
			openSession();
			mapper.updateLastLoginTime(id);
		} catch (Exception e) {
			throw new GenericException(e, UserDAL.class);
		} finally {
			closeSession();
		}
	}
}
