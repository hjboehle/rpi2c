package info.boehle.rpi2c.restservices;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

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
	static final String RETURNCODE_06 = "6";
	static final String RETURNCODE_99 = "99";
	static final String RETURNMESSAGE_00 = "0 - successful operation";
	static final String RETURNMESSAGE_01 = "1 - unsupported operation";
	static final String RETURNMESSAGE_02 = "2 - input/output error";
	static final String RETURNMESSAGE_03 = "3 - thread interrupted";
	static final String RETURNMESSAGE_04 = "4 - errors in the parameters of the URL, ";
	static final String RETURNMESSAGE_05 = "5 - combination of parameters not allowed";
	static final String RETURNMESSAGE_06 = "6 - unsupported I2C-Bus, I2C bus 1 is supported";
	static final String RETURNMESSAGE_99 = "99 - unknown error";
	//HttpServletRequest request;

	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld(@Context HttpServletRequest requestContext,@Context SecurityContext context) {
		String clientHostname = requestContext.getRemoteHost();
		String clientAddress = requestContext.getRemoteAddr();
		//this.information = "Hello World";
		this.information = "Host: " + clientHostname + ", ip-address: " + clientAddress;
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
		String [] responseRead = {"", ""};
		ParamChecker paramChecker = new ParamChecker(address, i2cbus, register, buffer, offset, size);
		String resultParamChecker = paramChecker.getResultsOnValidity();
		if (resultParamChecker.equals("valid")) {
			// all parameters of the URL are valid and now find out the read case
			String resultReadCase = paramChecker.getReadCase();
			if (resultReadCase.equals("1bvd")) {
				// read case: read one byte value from an i2c-device
				ServiceRead serviceRead = new ServiceRead(address, i2cbus);
				responseRead = serviceRead.readByteValueFromDevice();
				this.information = responseRead[0];
				this.returncode = responseRead[1];
				//this.information = "read one bit value from an i2c-device";
			} else if (resultReadCase.equals("nbvd")) {
				// read case: read n byte values from an i2c-device
				ServiceRead serviceRead = new ServiceRead(address, i2cbus, buffer, offset, size);
				responseRead = serviceRead.readByteValuesFromDevice();
				this.information = responseRead[0];
				this.returncode = responseRead[1];
				//this.information = "read n byte values from an i2c-device";
			} else if (resultReadCase.equals("1bvr")) {
				// read case: read one byte value from a register
				ServiceRead serviceRead = new ServiceRead(address, i2cbus, register);
				responseRead = serviceRead.readByteValueFromRegister();
				this.information = responseRead[0];
				this.returncode = responseRead[1];
				//this.information = "read one byte value from a register";
			} else if (resultReadCase.equals("nbvr")) {
				// read case: read n byte values from a register
				ServiceRead serviceRead = new ServiceRead(address, i2cbus, register, buffer, offset, size);
				responseRead = serviceRead.readByteValuesFromRegister();
				this.information = responseRead[0];
				this.returncode = responseRead[1];
				this.information = "read n byte values from a register";
			} else {
				// no read case identified
				this.returnmessage = RETURNMESSAGE_05;
				this.returncode = RETURNCODE_05;
			}
			if (responseRead[1].equals("0")) {
				this.returncode = RETURNCODE_00;
			} else if (responseRead[1].equals("6")) {
				this.returncode = RETURNCODE_06;
			} else if (responseRead[1].equals("2")) {
				this.returncode = RETURNCODE_02;
			} else {
				this.returncode = RETURNCODE_99;
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

	@Path("i2c/write")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String read(@DefaultValue("") @QueryParam("address") String address,
			@DefaultValue("1") @QueryParam("i2cbus") String i2cbus,
			@DefaultValue("") @QueryParam("byte") String oneByte,
			@DefaultValue("") @QueryParam("register") String register,
			@DefaultValue("") @QueryParam("buffer") String buffer,
			@DefaultValue("") @QueryParam("offset") String offset,
			@DefaultValue("") @QueryParam("size") String size) {
		// check whether the mandatory parameter "address" exists in the URL
		String [] responseWrite = {"", ""};
		ParamChecker paramChecker = new ParamChecker(address, i2cbus, oneByte, register, buffer, offset, size);
		String resultParamChecker = paramChecker.getResultsOnValidity();
		if (resultParamChecker.equals("valid")) {
			// all parameters of the URL are valid and now find out the write case
			String resultWriteCase = paramChecker.getWriteCase();
			if (resultWriteCase.equals("1bvd")) {
				// write case: write one byte value on an i2c-device
				ServiceWrite serviceWrite = new ServiceWrite(address, i2cbus, oneByte);
				responseWrite = serviceWrite.writeByteValueToDevice();
				this.information = responseWrite[0];
				this.returncode = responseWrite[1];
				//this.information = "write one byte value on an i2c-device";
			} else if (resultWriteCase.equals("nbvd")) {
				// write case: write n byte values to an i2c-device
				ServiceWrite serviceWrite = new ServiceWrite(address, i2cbus, oneByte, buffer, offset, size);
				responseWrite = serviceWrite.writeByteValuesToDevice();
				this.information = responseWrite[0];
				this.returncode = responseWrite[1];
				//this.information = "write n byte values to an i2c-device";
			} else if (resultWriteCase.equals("1bvr")) {
				// write case: write one byte value to a register
				ServiceWrite serviceWrite = new ServiceWrite(address, i2cbus, register, oneByte);
				responseWrite = serviceWrite.writeByteValueToRegister();
				this.information = responseWrite[0];
				this.returncode = responseWrite[1];
				//this.information = "write one byte value to a register";
			} else if (resultWriteCase.equals("nbvr")) {
				// write case: write n byte values to a register
				ServiceWrite serviceWrite = new ServiceWrite(address, i2cbus, register, buffer, offset, size, "");
				responseWrite = serviceWrite.writeByteValuesToRegister();
				this.information = responseWrite[0];
				this.returncode = responseWrite[1];
				this.information = "read n byte values from a register";
			} else {
				// no write case identified
				this.returnmessage = RETURNMESSAGE_05;
				this.returncode = RETURNCODE_05;
			}
			if (responseWrite[1].equals("0")) {
				this.returncode = RETURNCODE_00;
			} else if (responseWrite[1].equals("6")) {
				this.returncode = RETURNCODE_06;
			} else if (responseWrite[1].equals("2")) {
				this.returncode = RETURNCODE_02;
			} else {
				this.returncode = RETURNCODE_99;
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
