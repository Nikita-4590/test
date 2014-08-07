package com.hrs.mediarequesttool.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.hrs.mediarequesttool.common.exception.GenericException;

public class DBConnection {
	private static String CONFIG_FILE = "/WEB-INF/conf/db/template.xml";
	public static String DATABASE_PADB_PUBLIC = "/WEB-INF/conf/db/database.padb_public.xml";
	public static String DATABASE_PUBLIC = "/WEB-INF/conf/db/database.public.xml";

	private static Map<String, SqlSessionFactory> sessionInstances = new HashMap<String, SqlSessionFactory>();

	public static SqlSessionFactory getSqlSessionFactory(
			ServletContext servletContext, String configFile,
			boolean forceCreate) throws GenericException {
		try {
			if (!sessionInstances.containsKey(configFile) || forceCreate) {
				InputStream config = servletContext
						.getResourceAsStream(configFile);

				SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
						.build(config);

				sessionInstances.put(configFile, sqlSessionFactory);
			}

			return sessionInstances.get(configFile);
		} catch (Exception e) {
			throw new GenericException(e, DBConnection.class);
		}
	}

	public static SqlSessionFactory getSqlSessionFactoryFromTemplate(
			ServletContext servletContext, String databaseAddress,
			int databasePort, String databaseName, String username,
			String password, boolean forceCreate) throws GenericException {
		try {
			String key = String.format("%s:%s/%s_%s_%s", databaseAddress,
					databasePort, databaseName, username, password);

			if (!sessionInstances.containsKey(key) || forceCreate) {
				Properties properties = new Properties();

				properties.setProperty("username", username);
				properties.setProperty("password", password);
				properties.setProperty("database_address", databaseAddress);
				properties.setProperty("database_port",
						String.valueOf(databasePort));
				properties.setProperty("database_name", databaseName);

				InputStream config = servletContext
						.getResourceAsStream(CONFIG_FILE);

				SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
						.build(config, properties);

				sessionInstances.put(key, sqlSessionFactory);
			}

			return sessionInstances.get(key);
		} catch (Exception e) {
			throw new GenericException(e, DBConnection.class);
		}
	}
}
