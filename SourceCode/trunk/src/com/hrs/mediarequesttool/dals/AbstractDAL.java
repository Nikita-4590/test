package com.hrs.mediarequesttool.dals;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public abstract class AbstractDAL<MAPPER> {

	protected MAPPER mapper;

	protected Class<MAPPER> mapperType;

	protected SqlSession session;

	protected SqlSessionFactory sessionFactory;
	
	protected boolean autoCloseSession;

	protected AbstractDAL() {

	}

	protected AbstractDAL(SqlSessionFactory sessionFactory,
			Class<MAPPER> mapperType) {
		this.mapperType = mapperType;

		this.sessionFactory = sessionFactory;
		
		this.autoCloseSession = true;
	}

	protected void openSession() {
		if (session == null) {
			session = sessionFactory.openSession(true);
		}
		
		mapper = session.getMapper(mapperType);
	}

	public void forceCloseSession() {
		autoCloseSession = true;
		closeSession();
	}
	
	protected void closeSession() {
		if (autoCloseSession && session != null) {
			session.close();
			session = null;
		}

		mapper = null;
	}

	public SqlSession getSession() {
		return session;
	}

	public void setSession(SqlSession session) {
		this.session = session;
		autoCloseSession = false;
	}
	protected String parse(String input) {
		if(input == null) {
			return input;
		} else {
			input = "%" + input + "%";
			return input.replace(" ", "%");
		}
	}
	protected String parseId(String input) {
		if(input == null || input.length() == 0) {
			return null;
		} else {
			return input;
		}
	}
}
