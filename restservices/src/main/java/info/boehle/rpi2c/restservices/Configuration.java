package info.boehle.rpi2c.restservices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * The class models a configuration for Raspberry Pi I2C-RESTful services
 * 
 * @author Hans Jürgen Böhle
 * @version 1.0.0
 * 
 * @param port,
 *            port for the RESTful services on Web-Server (httpd)
 * @param preferredI2cBus,
 *            I2C Bus which is accepted, if this is not specified in the URL
 * @param delay,
 *            time in milliseconds, which should expire before a service is
 *            resumed
 */
public class Configuration {
	int port;
	int preferredI2cBus;
	int preferredDelay;
	static final int NUMBER_OF_CONFIGURATION_VALUES = 3;
	static final String XML_FILE_NAME = "/configuration.xml";
	static final String DEFAULT_PORT = "8181";
	static final String DEFAULT_PREFERRED_I2CBUS = "1";
	static final String DEFAULT_PREFERRED_DELAY = "300";

	/**
	 * Constructor of Class objects
	 */
	public Configuration() {
		super();
	}

	/**
	 * The checkConfigurationFile method verifies whether a configuration file
	 * exists that meets the requirements. If the requirements are not met, the file
	 * is corrected or re-created.
	 * 
	 * @return true if there is a correct file. false, if a correct file could not
	 *         be created.
	 */

	public boolean checkConfigurationFile() {
		boolean correctConfigurationFile = true;
		boolean writeSuccess;
		File configurationFile = new File(XML_FILE_NAME);
		if (configurationFile.exists()) {
			// file exists, validation of the file
			Configuration configuration = new Configuration();
			boolean validSuccess = configuration.validConfigurationFile(configurationFile);
			if (!validSuccess) {
				writeSuccess = configuration.writeConfigurationFile(configurationFile);
				if (!writeSuccess) {
					correctConfigurationFile = false;
				}
			}
			return correctConfigurationFile;
		} else {
			// file not exists, create a configuration file
			Configuration configuration = new Configuration();
			writeSuccess = configuration.writeConfigurationFile(configurationFile);
			if (!writeSuccess) {
				correctConfigurationFile = false;
			}
		}
		return correctConfigurationFile;
	}

	/**
	 * The validConfigurationFile method verifies the well-formedness and validity of the configuration file
	 * 
	 * @return true if the file was validate successfully, false if the file was not
	 *         validate successfully
	 */
	public boolean validConfigurationFile(File configurationFile) {
		boolean validSuccess = true;
		SAXBuilder saxBuilder = new SAXBuilder(XMLReaders.DTDVALIDATING);
		try {
			Document xmlDocument = saxBuilder.build(configurationFile);
		} catch (JDOMException e) {
			validSuccess = false;
		} catch (IOException e) {
			validSuccess = false;
			e.printStackTrace();
		}
		return validSuccess;

	}

	/**
	 * The writeConfigurationFile method creates a new configuration file with the
	 * default configuration values
	 * 
	 * @return true if the file was created successfully, false if the file was not
	 *         created successfully
	 */
	public boolean writeConfigurationFile(File configurationFile) {
		boolean writeSuccess = true;
		Element configuration = new Element("configuration");
		Document xmlDocument = new Document();
		xmlDocument.setRootElement(configuration);
		configuration.addContent(new Element("port").setText(DEFAULT_PORT));
		configuration.addContent(new Element("preferredI2cBus").setText(DEFAULT_PREFERRED_I2CBUS));
		configuration.addContent(new Element("preferredDelay").setText(DEFAULT_PREFERRED_DELAY));

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(configurationFile);
			XMLOutputter xmlOutputter = new XMLOutputter();
			xmlOutputter.setFormat(Format.getPrettyFormat());
			try {
				System.out.println("bin in xmlOutputter");
				xmlOutputter.output(xmlDocument, fileOutputStream);
			} catch (IOException e) {
				writeSuccess = false;
				return writeSuccess;
			}

		} catch (FileNotFoundException e) {
			writeSuccess = false;
			return writeSuccess;
		}

		return writeSuccess;
	}

	/**
	 * The readConfiguration method returns all values from the configuration
	 * xml-file.
	 * 
	 * @return the array of all values from the configuration xml-file
	 */
	public String[] readConfiguration() {

		String[] valuesConfiguration = new String[NUMBER_OF_CONFIGURATION_VALUES];
		Document xmlConfiguration = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			xmlConfiguration = builder.build(XML_FILE_NAME);
			Element root = xmlConfiguration.getRootElement();
			Element elementPort = root.getChild("port");
			Element elementPreferredI2cBus = root.getChild("preferredI2cBus");
			Element elementPreferredDelay = root.getChild("preferredDelay");
			valuesConfiguration[0] = elementPort.getText();
			valuesConfiguration[1] = elementPreferredI2cBus.getText();
			valuesConfiguration[2] = elementPreferredDelay.getText();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return valuesConfiguration;
	}

	/**
	 * The getPort method returns the port number that is stored in the
	 * configuration XML file.
	 * 
	 * @return the port from the configuration xml-file
	 */
	public int getPort() {
		Configuration configuration = new Configuration();
		String[] configurationValues = configuration.readConfiguration();
		this.port = Integer.parseInt(configurationValues[0]);
		return this.port;
	}

	/**
	 * The getPreferredI2cBus method returns number of the I2C Bus which is
	 * accepted, if this is not specified in the URL, that is stored in the
	 * configuration XML file.
	 * 
	 * @return the preferredI2cBus from the configuration xml-file
	 */
	public int getPreferredI2cBus() {
		Configuration configuration = new Configuration();
		String[] configurationValues = configuration.readConfiguration();
		this.preferredI2cBus = Integer.parseInt(configurationValues[1]);
		return this.preferredI2cBus;
	}

	/**
	 * The getPreferredDelay method returns the time in milliseconds, which should
	 * expire before a service is resumed, that is stored in the configuration XML
	 * file.
	 * 
	 * @return the delay from the configuration xml-file
	 */
	public int getPreferredDelay() {
		Configuration configuration = new Configuration();
		String[] configurationValues = configuration.readConfiguration();
		this.preferredDelay = Integer.parseInt(configurationValues[2]);
		return this.preferredDelay;
	}

}
