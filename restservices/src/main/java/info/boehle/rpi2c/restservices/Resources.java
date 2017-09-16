package info.boehle.rpi2c.restservices;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.pi4j.system.SystemInfo;

@Path("rpi2c")
public class Resources {
	String xmlMessage = "";
	String information = "";
	String success = "yes";
	String returncode = "0";
	boolean isHexAddress;
	boolean isHexRegister;
	boolean isIntSize;
	boolean isIntOffset;
	static final String REGEX_BYTE_IN_HEX = "[0-9A-Fa-f]{1,2}";
	static final String REGEX_BYTES_IN_HEX = "[[0-9A-F]+";
	static final String REGEX_INT = "[0-9A-F]+";
	static final String RETURNCODE_01 = "01 - unsupported operation";
	static final String RETURNCODE_02 = "02 - input/output error";
	static final String RETURNCODE_03 = "03 - thread interrupted";
	static final String RETURNCODE_04 = "04 - the address-parameter is missing in the URL";
	static final String RETURNCODE_05 = "05 - the address-parameter does not contains a maximal double-digit hexadecimal number";
	static final String RETURNCODE_06 = "06 - the register-parameter does not contains a maximal double-digit hexadecimal number";
	static final String RETURNCODE_07 = "07 - the buffer-parameter does not contains a hexadecimal number";
	static final String RETURNCODE_08 = "08 - the size-parameter does not contains an integer value";
	static final String RETURNCODE_09 = "09 - the offset-parameter does not contains an integer value";
	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld() {
		information = "Hello World!";
		XmlResponse xmlResponse = new XmlResponse(information, success, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
	    return xmlMessage;
	}
	@GET
	@Path("system/os")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemInfoOs() {
		String information = System.getProperty("os.name");
		XmlResponse xmlResponse = new XmlResponse(information, success, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
	    return xmlMessage;
	}
	@GET
	@Path("system/processor")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemInfoModel() {
		try {
			this.information = SystemInfo.getModelName();
			this.success = "yes";
			this.returncode = "0";
		} catch (UnsupportedOperationException e) {
			this.success = "no";
			this.returncode = RETURNCODE_01;
			e.printStackTrace();
		} catch (IOException e) {
			this.success = "no";
			this.returncode = RETURNCODE_02;
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.success = "no";
			this.returncode = RETURNCODE_03;
			e.printStackTrace();
		}
		XmlResponse xmlResponse = new XmlResponse(information, success, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
	    return xmlMessage;
	}
	@GET
	@Path("system/hardware")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemHardware() {
		try {
			this.information = SystemInfo.getHardware();
			this.success = "yes";
			this.returncode = "0";
		} catch (UnsupportedOperationException e) {
			this.success = "no";
			this.returncode = RETURNCODE_01;
			e.printStackTrace();
		} catch (IOException e) {
			this.success = "no";
			this.returncode = RETURNCODE_02;
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.success = "no";
			this.returncode = RETURNCODE_03;
			e.printStackTrace();
		}
		XmlResponse xmlResponse = new XmlResponse(information, success, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
	    return xmlMessage;
	}
	@Path("i2c/read")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String read(
			@DefaultValue("no") @QueryParam("address") String address,
			@DefaultValue("") @QueryParam("register") String register,
			@DefaultValue("1") @QueryParam("size") String size) {
		boolean flagRegister = true;
		this.isHexAddress = address.matches(REGEX_BYTE_IN_HEX);
		this.isHexRegister = register.matches(REGEX_BYTE_IN_HEX);
		this.isIntSize = size.matches(REGEX_INT);
		// check whether the optional parameter "register" exists in the URL
		if (register.equals("")) {
			flagRegister = false;
			register = null;
		}
		if (address.equals("no")) {
			this.success = "no";
			this.returncode = RETURNCODE_04;
		}
		else if (!this.isHexAddress) {
			this.success = "no";
			this.returncode = RETURNCODE_05;
		}
		else if (!this.isHexRegister || flagRegister) {
			this.success = "no";
			this.returncode = RETURNCODE_06;
		}
		else if (!this.isIntSize) {
			this.success = "no";
			this.returncode = RETURNCODE_08;
		}
		else {
			this.information = "address: " + address + ", register: " + register + ", size: " + size;
			this.success = "yes";
			this.returncode = "0";
		}
		XmlResponse xmlResponse = new XmlResponse(information, success, returncode);
		this.xmlMessage = xmlResponse.getXmlMessage();
	    return this.xmlMessage;
	}
}
