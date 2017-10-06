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
	String success = "yes";
	String returncode = "0";
	String addressLowerLimit = "03";
	String addressUpperLimit = "77";
	static final String RETURNCODE_01 = "01 - unsupported operation";
	static final String RETURNCODE_02 = "02 - input/output error";
	static final String RETURNCODE_03 = "03 - thread interrupted";
	static final String RETURNCODE_04 = "04 - the address-parameter is missing in the URL";
	static final String RETURNCODE_05 = "05 - the address-parameter does not contains a regular and valid value";
	static final String RETURNCODE_06 = "06 - the register-parameter does not contains a regular and valid value";
	static final String RETURNCODE_07 = "07 - the size-parameter does not contains a regular and valid value";
	static final String RETURNCODE_08 = "08 - unsupported I2C-Bus, I2C bus 1 is supported";
    public static I2CBus i2cBus;
    public static I2CDevice i2cDevice;

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
	public String read(@DefaultValue("") @QueryParam("address") String address,
			@DefaultValue("") @QueryParam("register") String register,
			@DefaultValue("1") @QueryParam("size") String size) {
		// check whether the mandatory parameter "address" exists in the URL
		this.success = "no";
		if (!address.equals("no")) {
			// address exists, check whether the mandatory parameter "address"
			UncheckedValue uncheckedValueAddress = new UncheckedValue(address, 16, this.addressLowerLimit,
					this.addressUpperLimit);
			if (uncheckedValueAddress.getValidity()) {
				// address is valid, check whether the optional parameter "register" 
				UncheckedValue uncheckedValueRegister = new UncheckedValue(register, 16, this.addressLowerLimit,
						this.addressUpperLimit);
				if (uncheckedValueRegister.getValidity() || register.equals("")) {
					// address and register are valid, check whether the optional parameter "size"
					UncheckedValue uncheckedValueSize = new UncheckedValue(size, 10, "1", "10");
					if (uncheckedValueSize.getValidity() || size.equals("")) {
						try {
							i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
							i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
							int returnValue = i2cDevice.read();
							this.information = "addressHex: " +  Integer.parseInt(address, 16) + ", byte value: " + returnValue;
							this.success = "yes";
							this.returncode = "0";
						} catch (UnsupportedBusNumberException e) {
							this.returncode = RETURNCODE_08;
						} catch (IOException e) {
							this.returncode = RETURNCODE_02;
						}
						// reads a byte value of a I2C device
						
						// reads a byte value from the register of a I2C device 
						
						//this.information = "address: " + address + ", register: " + register + ", size: " + size;
						this.success = "yes";
						this.returncode = "0";
					} else {
						this.returncode = RETURNCODE_07;
					}
				} else {
					this.returncode = RETURNCODE_06;
				}
			} else {
				this.returncode = RETURNCODE_05;
			}
		} else {
			this.returncode = RETURNCODE_04;
		}
		XmlResponse xmlResponse = new XmlResponse(information, success, returncode);
		this.xmlMessage = xmlResponse.getXmlMessage();
		return this.xmlMessage;
	}
}
