package info.boehle.rpi2c.restservices;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * The class models a read operation on an I2C-bus
 * @author Hans Jürgen Böhle
 * @version 1.0.0
 */
public class ServiceRead {
	String address;
	String busnumber;
	String register;
	String buffer;
	String offset;
	String size;
	String information;
	String returncode;
	String [] response = {"", ""};
    public static I2CBus i2cBus;
    public static I2CDevice i2cDevice;
	
	/**
	 * Constructor for ServiceRead to read one byte value from an i2c-device
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 */
	public ServiceRead(String address, String i2cbus) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
	}
	
	/**
	 * Constructor for ServiceRead to read n byte values from an i2c-device
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param buffer, array into the read bytes 
	 * @param offset, position from which bytes are written to the array buffer
	 * @param size, number of bytes to be read
	 */
	public ServiceRead(String address, String i2cbus, String buffer, String offset, String size) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.buffer = buffer;
		this.offset = offset;
		this.size = size;
	}
	
	/**
	 * Constructor for ServiceRead to read one byte value from a register
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param register, address from the register
	 */
	public ServiceRead(String address, String i2cbus, String register) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.register = register;
	}
	
	/**
	 * Constructor for ServiceRead to read n byte values from a register
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param register, address from the register
	 * @param buffer, array into the read bytes 
	 * @param offset, position from which bytes are written to the array buffer
	 * @param size, number of bytes to be read
	 */
	public ServiceRead(String address, String i2cbus, String register, String buffer, String offset, String size) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.register = register;
		this.buffer = buffer;
		this.offset = offset;
		this.size = size;
	}
	
	/**
	 * The method reads a byte value from an I2C device
	 * @return response, the answer from the read-operation
	 */
	public String [] readByteValueFromDevice() {
		int byteValue;
		this.information = "";
		
		try {
			if (this.busnumber.equals("1")) {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			} else {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_0);
			}
			i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
			// reads a byte value of an i2C-device
			byteValue = i2cDevice.read();
			this.information = Integer.toHexString(byteValue);
			this.returncode = "0";
		} catch (UnsupportedBusNumberException e) {
			this.returncode = "6";
		} catch (IOException e) {
			this.returncode = "2";
		}
		
		this.response [0] = this.information;
		this.response [1] = this.returncode;
		return this.response;
	}
	
	/**
	 * The method reads n byte values from an I2C device
	 * @return response, the answer from the read-operation
	 */
	public String [] readByteValuesFromDevice() {
		this.information = "";
		// transformation of the URL-parameters
		ParamConverter paramConverterBuffer = new ParamConverter(this.buffer);
		byte [] convertedBuffer = paramConverterBuffer.getStringHexToBytes();
		
		try {
			if (this.busnumber.equals("1")) {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			} else {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_0);
			}
			i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
			// reads n byte values to an i2C-device
			i2cDevice.read(convertedBuffer, Integer.parseInt(this.offset), Integer.parseInt(this.size));
			ParamConverter paramConverterInformation = new ParamConverter(convertedBuffer);
			this.information = paramConverterInformation.getBytesToStringHex();
			this.returncode = "0";
		} catch (UnsupportedBusNumberException e) {
			this.returncode = "6";
		} catch (IOException e) {
			this.returncode = "2";
		}
		
		this.response [0] = this.information;
		this.response [1] = this.returncode;
		return this.response;
	}

	/**
	 * The method reads a byte value from a register
	 * @return response, the answer from the read-operation
	 */
	public String [] readByteValueFromRegister() {
		int byteValue;
		this.information = "";
		
		try {
			if (this.busnumber.equals("1")) {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			} else {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_0);
			}
			i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
			// reads a byte value of a register
			byteValue = i2cDevice.read(Integer.parseInt(register, 16));
			this.information = Integer.toHexString(byteValue);
			this.returncode = "0";
		} catch (UnsupportedBusNumberException e) {
			this.returncode = "6";
		} catch (IOException e) {
			this.returncode = "2";
		}
		
		this.response [0] = this.information;
		this.response [1] = this.returncode;
		return this.response;
	}
	
	/**
	 * The method reads n byte values from a register
	 * @return response, the answer from the read-operation
	 */
	public String [] readByteValuesFromRegister() {
		this.information = "";
		// transformation of the URL-parameters
		ParamConverter paramConverterBuffer = new ParamConverter(this.buffer);
		byte [] convertedBuffer = paramConverterBuffer.getStringHexToBytes();
		
		try {
			if (this.busnumber.equals("1")) {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			} else {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_0);
			}
			i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
			// reads n byte values of a register
			i2cDevice.read(Integer.parseInt(register, 16), convertedBuffer, Integer.parseInt(this.offset), Integer.parseInt(this.size));
			ParamConverter paramConverterInformation = new ParamConverter(convertedBuffer);
			this.information = paramConverterInformation.getBytesToStringHex();
			this.returncode = "0";
		} catch (UnsupportedBusNumberException e) {
			this.returncode = "6";
		} catch (IOException e) {
			this.returncode = "2";
		}
		
		this.response [0] = this.information;
		this.response [1] = this.returncode;
		return this.response;
	}

}
