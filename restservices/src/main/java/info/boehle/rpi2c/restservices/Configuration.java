package info.boehle.rpi2c.restservices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * The class models a configuration for Raspberry Pi I2C-RESTful services
 * 
 * @author Hans Jürgen Böhle
 * @version 1.0.0
 */
public class Configuration {
	static final int NUMBER_OF_CONFIGURATION_VALUES = 3;
	static final String XML_FILE_NAME = "configuration.xml";
	static final String DEFAULT_PORT = "8181";
	static final String DEFAULT_PREFERRED_I2CBUS = "1";
	static final String DEFAULT_PREFERRED_DELAY = "300";
	static final String PORT_MIN = "0";
	static final String PORT_MAX = "65535";
	static final String PREFERRED_I2CBUS_MIN = "0";
	static final String PREFERRED_I2CBUS_MAX = "1";
	static final String PREFERRED_DELAY_MIN = "0";
	static final String PREFERRED_DELAY_MAX = "10000"; // 10.000 milliseconds
	static final String PROPERTIES_FILENAME = "conf.properties";
	String port;
	String preferredI2cBus;
	String preferredDelay;
	Properties properties = null;
	Writer propertiesFileWriter = null;
	Reader propertiesFileReader = null;

	/**
	 * Constructor of Class objects
	 */
	public Configuration() {
		super();
	}

	/**
	 * The setDefaultProperties method sets the properties to the default values.
	 * 
	 * @param p,
	 *            properties object
	 * @return default properties
	 */
	public Properties setDefaultProperties(Properties p) {
		this.properties = new Properties();
		this.properties.setProperty("port", DEFAULT_PORT);
		this.properties.setProperty("preferredI2cBus", DEFAULT_PREFERRED_I2CBUS);
		this.properties.setProperty("preferredDelay", DEFAULT_PREFERRED_DELAY);
		return this.properties;
	}

	/**
	 * The createNewPropertiesFile method creates a new conf.properties file.
	 * 
	 * @return true by success, false by no success
	 */
	public boolean createNewPropertiesFile(String propertiesComment, boolean defaultValues) {
		boolean successWrite = true;
		Configuration configuration = new Configuration();
		if (defaultValues) {
			this.properties = configuration.setDefaultProperties(properties);
		}
		try {
			FileOutputStream propertiesFileOutputStream = new FileOutputStream(PROPERTIES_FILENAME);
			try {
				properties.store(propertiesFileOutputStream, propertiesComment);
			} catch (IOException e) {
				// properties can not write to the conf.properties file
				successWrite = false;
			}
		} catch (FileNotFoundException e) {
			// conf.properties can not create
			successWrite = false;
		}
		return successWrite;
	}

	/**
	 * The readProperties method reads the properties from the conf.properties file.
	 * 
	 * @return properties by success, default properties by no success
	 */
	public Properties readProperties() {
		properties = new Properties();
		String propertiesComment = "file with default properties create automatically";                   
		FileInputStream propertiesFileInputStream;
		try {
			propertiesFileInputStream = new FileInputStream(PROPERTIES_FILENAME);
			try {
				properties.load(propertiesFileInputStream);
			} catch (IOException e) {
				System.out.println("!!!ERROR!!! - IOException");
				Configuration configuration = new Configuration();
				configuration.createNewPropertiesFile(propertiesComment, true);
			}
		} catch (FileNotFoundException e) {
			// conf.properties file not found, returns the default properties an create a
			// new file
			System.out.println("!!!ERROR!!! - FileNotFoundException");
			Configuration configuration = new Configuration();
			configuration.createNewPropertiesFile(propertiesComment, true);
		}
		return properties;
	}

	/**
	 * The getPort method returns the port property and checks the value for
	 * plausibility. If the check is negative, the default value is returned and
	 * written to the property file.
	 * 
	 * @return property port
	 */
	public String getPort() {
		Configuration configuration = new Configuration();
		this.properties = configuration.readProperties();
		this.port = this.properties.getProperty("port");
		if (Integer.parseInt(this.port) < Integer.parseInt(PORT_MIN)
				|| Integer.parseInt(this.port) > Integer.parseInt(PORT_MAX)) {
			// property port out of range, port set to the default value
			this.port = DEFAULT_PORT;
			this.properties.setProperty("port", this.port);
			configuration.createNewPropertiesFile("file with default property port create automatically", false              );
		}
		return this.port;
	}

	/**
	 * The getPreferredI2cBus method returns the preferredI2cBus property and checks the value for
	 * plausibility. If the check is negative, the default value is returned and
	 * written to the property file.
	 * 
	 * @return property preferredI2cBus
	 */
	public String getPreferredI2cBus() {
		Configuration configuration = new Configuration();
		this.properties = configuration.readProperties();
		this.preferredI2cBus = this.properties.getProperty("preferredI2cBus");
		if (Integer.parseInt(this.preferredI2cBus) < Integer.parseInt(PREFERRED_I2CBUS_MIN)
				|| Integer.parseInt(this.preferredI2cBus) > Integer.parseInt(PREFERRED_I2CBUS_MAX)) {
			// property preferredI2cBus out of range, preferredI2cBus set to the default value
			this.preferredI2cBus = DEFAULT_PREFERRED_I2CBUS;
			this.properties.setProperty("preferredI2cBus", this.preferredI2cBus);
			configuration.createNewPropertiesFile("file with default property preferredI2cBus create automatically", false);
		}
		return this.preferredI2cBus;
	}

	/**
	 * The getPreferredDelay method returns the preferredDelay property and checks the value for
	 * plausibility. If the check is negative, the default value is returned and
	 * written to the property file.
	 * 
	 * @return property preferredDelay
	 */
	public String getPreferredDelay() {
		Configuration configuration = new Configuration();
		this.properties = configuration.readProperties();
		this.preferredDelay = this.properties.getProperty("preferredDelay");
		if (Integer.parseInt(this.preferredDelay) < Integer.parseInt(PREFERRED_DELAY_MIN)
				|| Integer.parseInt(this.preferredDelay) > Integer.parseInt(PREFERRED_DELAY_MAX)) {
			// property preferredDelay out of range, preferredDelay set to the default value
			this.preferredDelay = DEFAULT_PREFERRED_DELAY;
			this.properties.setProperty("preferredDelay", this.preferredDelay);
			configuration.createNewPropertiesFile("file with default property preferredDelay create automatically", false);
		}
		return this.preferredDelay;
	}

}
