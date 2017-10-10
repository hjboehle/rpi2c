package info.boehle.rpi2c.restservices;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.system.SystemInfo;

@Path("rpi2c")
public class Resources {
	String xmlMessage = "";
	String information = "";
	String returnmessage = RETURNMESSAGE_00;
	String returncode = "0";
	String addressLowerLimit = "03";
	String addressUpperLimit = "77";
	static final String RETURNCODE_00 = "0";
	static final String RETURNCODE_01 = "1";
	static final String RETURNCODE_02 = "2";
	static final String RETURNCODE_03 = "3";
	static final String RETURNCODE_04 = "4";
	static final String RETURNCODE_05 = "5";
	static final String RETURNMESSAGE_00 = "0 - successful operation";
	static final String RETURNMESSAGE_01 = "1 - unsupported operation";
	static final String RETURNMESSAGE_02 = "2 - input/output error";
	static final String RETURNMESSAGE_03 = "3 - thread interrupted";
	static final String RETURNMESSAGE_04 = "4 - errors in the parameters of the URL, ";
	static final String RETURNMESSAGE_05 = "5 - combination of parameters not allowed";
    public static I2CBus i2cBus;
    public static I2CDevice i2cDevice;

	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld() {
		this.information = "Hello World!";
		XmlResponse xmlResponse = new XmlResponse(information, returnmessage, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
		return xmlMessage;
	}

	@GET
	@Path("system/os")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemInfoOs() {
		this.information = System.getProperty("os.name");
		XmlResponse xmlResponse = new XmlResponse(information, returnmessage, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
		return xmlMessage;
	}

	@GET
	@Path("system/processor")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemInfoModel() {
		try {
			this.information = SystemInfo.getModelName();
			this.returnmessage = RETURNMESSAGE_00;
			this.returncode = RETURNCODE_00;
		} catch (UnsupportedOperationException e) {
			this.returnmessage = RETURNMESSAGE_01;
			this.returncode = RETURNCODE_01;
			e.printStackTrace();
		} catch (IOException e) {
			this.returnmessage = RETURNMESSAGE_02;
			this.returncode = RETURNCODE_02;
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.returnmessage = RETURNMESSAGE_03;
			this.returncode = RETURNCODE_03;
			e.printStackTrace();
		}
		XmlResponse xmlResponse = new XmlResponse(information, returnmessage, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
		return xmlMessage;
	}

	@GET
	@Path("system/hardware")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSystemHardware() {
		try {
			this.information = SystemInfo.getHardware();
			this.returnmessage = RETURNMESSAGE_00;
			this.returncode = RETURNCODE_00;
		} catch (UnsupportedOperationException e) {
			this.returnmessage = RETURNMESSAGE_01;
			this.returncode = RETURNCODE_01;
			e.printStackTrace();
		} catch (IOException e) {
			this.returnmessage = RETURNMESSAGE_02;
			this.returncode = RETURNCODE_02;
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.returnmessage = RETURNMESSAGE_03;
			this.returncode = RETURNCODE_03;
			e.printStackTrace();
		}
		XmlResponse xmlResponse = new XmlResponse(information, returnmessage, returncode);
		xmlMessage = xmlResponse.getXmlMessage();
		return xmlMessage;
	}

	@Path("i2c/read")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String read(@DefaultValue("") @QueryParam("address") String address,
			@DefaultValue("1") @QueryParam("i2cbus") String i2cbus,
			@DefaultValue("") @QueryParam("register") String register,
			@DefaultValue("") @QueryParam("buffer") String buffer,
			@DefaultValue("") @QueryParam("offset") String offset,
			@DefaultValue("") @QueryParam("size") String size) {
		// check whether the mandatory parameter "address" exists in the URL
		ParamChecker paramChecker = new ParamChecker(address, i2cbus, register, buffer, offset, size);
		String resultParamChecker = paramChecker.getResultsOnValidity();
		if (resultParamChecker.equals("valid")) {
			// all parameters of the URL are valid and now find out the read case
			String resultReadCase = paramChecker.getReadCase();
			if (resultReadCase.equals("1bvd")) {
				// read case: read one bit value from an i2c-device
				this.information = "read one bit value from an i2c-device";
			} else if (resultReadCase.equals("nbvd")) {
				// read case: read n bit values from an i2c-device
				this.information = "read n bit values from an i2c-device";
			} else if (resultReadCase.equals("1bvr")) {
				// read case: read one bit value from a register
				this.information = "read one bit value from a register";
			} else if (resultReadCase.equals("nbvr")) {
				// read case: read n bit values from a register
				this.information = "read n bit values from a register";
			} else {
				// no read case identified
				this.returnmessage = RETURNMESSAGE_05;
				this.returncode = RETURNCODE_05;
			}
		} else {
			// errors in the parameters of the URL
			this.returnmessage = RETURNMESSAGE_04 + resultParamChecker;
			this.returncode = RETURNCODE_04;
		}
		XmlResponse xmlResponse = new XmlResponse(information, returnmessage, returncode);
		this.xmlMessage = xmlResponse.getXmlMessage();
		return this.xmlMessage;
	}
}
