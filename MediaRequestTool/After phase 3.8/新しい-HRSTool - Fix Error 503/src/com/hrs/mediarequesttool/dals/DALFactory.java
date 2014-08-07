package com.hrs.mediarequesttool.dals;

import java.lang.reflect.Constructor;

import javax.servlet.ServletContext;

import org.apache.ibatis.session.SqlSessionFactory;

import com.hrs.mediarequesttool.common.DBConnection;
import com.hrs.mediarequesttool.common.Dictionary;
import com.hrs.mediarequesttool.common.exception.GenericException;

public class DALFactory {
	private static Dictionary<SqlSessionFactory, Class<?>, Object> instances;

	public static <T> T getDAL(Class<T> clazz, SqlSessionFactory sessionFactory)
			throws GenericException {
		if (instances == null) {
			instances = new Dictionary<SqlSessionFactory, Class<?>, Object>();
		}

		if (!instances.containsKeys(sessionFactory, clazz)) {
			try {
				Constructor<?> ctor = clazz
						.getConstructor(SqlSessionFactory.class);

				ctor.setAccessible(true);
				T instanceDAL = clazz.cast(ctor.newInstance(sessionFactory));

				if (instanceDAL != null) {
					instances.put(sessionFactory, clazz, instanceDAL);
				}
			} catch (Exception e) {
				throw new GenericException(e, DALFactory.class);
			}
		}

		return clazz.cast(instances.get(sessionFactory, clazz));
	}
	public static<T> T getDAL(Class<T> clazz, ServletContext servletContext) throws GenericException{
		return getDAL(clazz, DBConnection.getSqlSessionFactory(servletContext, DBConnection.DATABASE_PADB_PUBLIC, false));
	}
	
	public static<T> T getDAL(Class<T> clazz, ServletContext servletContext, String filePath) throws GenericException{
		return getDAL(clazz, DBConnection.getSqlSessionFactory(servletContext, filePath, false));
	}
}
