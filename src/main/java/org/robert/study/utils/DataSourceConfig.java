package org.robert.study.utils;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

public class DataSourceConfig {
    private volatile static DataSourceConfig uniqueInstance;
    private static final String DIALOG_SETTING_FILE = System.getProperty("user.home") + "/" + "rober_toolkits.xml";
    private static final String SERVICE_NAME = "com-org-iisigroup-rl-robert-toolkits";
    public static final String JDBC_USERNAME = "jdbc-username";// ex:<jdbc-username>risappl</jdbc-username>
    public static final String JDBC_PASSWORD = "jdbc-password";// ex:<jdbc-password>risappl</jdbc-password>
    public static final String JDBC_URL = "jdbc-url";// ex:<jdbc-url>jdbc:informix-sqli://192.168.10.11:4526/teun0020:informixserver=ibm;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1</jdbc-url>

    private final String defaulDriverClassName = "com.informix.jdbc.IfxDriver";
    private final String defaulUsername = "srismapp";
    private final String defaulPassword = "ris31123";
    private final String defaulUrl = "jdbc:informix-sqli://192.168.10.18:4526/teun0020:informixserver=aix2;DB_LOCALE=zh_tw.utf8;CLIENT_LOCALE=zh_tw.utf8;GL_USEGLU=1";

    public static synchronized DataSourceConfig getInstance() {
        if (uniqueInstance == null) {
            synchronized (DataSourceConfig.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DataSourceConfig();
                }
            }
        }
        return uniqueInstance;
    }
    
    public void saveGeneralConfig(final Map<String, String> data) {
        DialogSettings settings = new DialogSettings(DataSourceConfig.SERVICE_NAME);
        String username = data.get(JDBC_USERNAME);
        String password = data.get(JDBC_PASSWORD);
        String url = data.get(DataSourceConfig.JDBC_URL);

        settings.put(DataSourceConfig.JDBC_USERNAME, username);
        settings.put(DataSourceConfig.JDBC_PASSWORD, password);
        settings.put(DataSourceConfig.JDBC_URL, url);

        settings.save(DataSourceConfig.DIALOG_SETTING_FILE);
    }

    public Map<String, String> getGeneralConfig() {
        Map<String, String> result = new HashMap<String, String>();
        IDialogSettings settings = DialogSettings.getOrCreateSection( new DialogSettings(SERVICE_NAME), SERVICE_NAME);
        try {
            settings.load(DIALOG_SETTING_FILE);
            result.put(JDBC_USERNAME, settings.get(JDBC_USERNAME));
            result.put(JDBC_PASSWORD, settings.get(JDBC_PASSWORD));
            result.put(JDBC_URL, settings.get(JDBC_URL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveSerailConfig(Map<String, String> data, String prefix) {
        DialogSettings settings = new DialogSettings(DataSourceConfig.SERVICE_NAME);
        String username = data.get(JDBC_USERNAME + prefix);
        String password = data.get(JDBC_PASSWORD + prefix);
        String url = data.get(DataSourceConfig.JDBC_URL + prefix);

        settings.put(DataSourceConfig.JDBC_USERNAME + prefix, username);
        settings.put(DataSourceConfig.JDBC_PASSWORD + prefix, password);
        settings.put(DataSourceConfig.JDBC_URL + prefix, url);

        settings.save(DataSourceConfig.DIALOG_SETTING_FILE);
    }

    public Map<String, String> getSerailConfig(String prefix) {
        Map<String, String> result = new HashMap<String, String>();
        DialogSettings settings = new DialogSettings(SERVICE_NAME);
        try {
            settings.load(DIALOG_SETTING_FILE);
            result.put(JDBC_USERNAME + prefix, settings.get(JDBC_USERNAME + prefix));
            result.put(JDBC_PASSWORD + prefix, settings.get(JDBC_PASSWORD + prefix));
            result.put(JDBC_URL + prefix, settings.get(JDBC_URL + prefix));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public DataSource getDefaultDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(defaulDriverClassName);
        ds.setUsername(defaulUsername);
        ds.setPassword(defaulPassword);
        ds.setUrl(defaulUrl);
        return ds;
    }

    public DataSource getCustomeDataSource() {
        BasicDataSource ds = new BasicDataSource();
        Map<String, String> data = getGeneralConfig();
        String username = data.get(JDBC_USERNAME);
        String password = data.get(JDBC_PASSWORD);
        String url = data.get(DataSourceConfig.JDBC_URL);
        ds.setDriverClassName(defaulDriverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        return ds;
    }

    public String getDefaulDriverClassName() {
        return defaulDriverClassName;
    }

    public String getDefaulUsername() {
        return defaulUsername;
    }

    public String getDefaulPassword() {
        return defaulPassword;
    }

    public String getDefaulUrl() {
        return defaulUrl;
    }

    private DataSourceConfig() {
    }
}
