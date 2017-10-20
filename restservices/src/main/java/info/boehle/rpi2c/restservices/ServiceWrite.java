package info.boehle.rpi2c.restservices;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * The class models a write operation on an I2C-bus
 * @author Hans Jürgen Böhle
 * @version 1.0.0
 */
public class ServiceWrite {
	String address;
	String busnumber;
	String register;
	String oneByte;
	String buffer;
	String offset;
	String size;
	String information;
	String returncode;
	String [] response = {"", ""};
    public static I2CBus i2cBus;
    public static I2CDevice i2cDevice;

	
	/**
	 * Constructor for ServiceWrite to write one byte value to an i2c-device
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param oneByte, byte written to the device
	 */
	public ServiceWrite(String address, String i2cbus, String oneByte) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.oneByte = oneByte;
	}
	
	/**
	 * Constructor for ServiceWrite to write n byte values to an i2c-device
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param buffer, array that contains the write bytes 
	 * @param offset, position from which bytes are written to the array buffer
	 * @param size, number of bytes to be write
	 */
	public ServiceWrite(String address, String i2cbus, String oneByte, String buffer, String offset, String size) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.buffer = buffer;
		this.offset = offset;
		this.size = size;
	}
	
	/**
	 * Constructor for ServiceWrite to write one byte value to a register
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param register, address from the register
	 * @param oneByte, byte written to the device
	 */
	public ServiceWrite(String address, String i2cbus, String register, String oneByte) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.register = register;
		this.oneByte = oneByte;
	}
	
	/**
	 * Constructor for ServiceWrite to write n byte values to a register
	 * @param address, address from the i2c-device
	 * @param busnumber, number of the i2c-bus (0 or 1)
	 * @param register, address from the register
	 * @param buffer, array into the write bytes 
	 * @param offset, position from which bytes are written to the array buffer
	 * @param size, number of bytes to be read
	 * @param dummy, help string to have a different constructor
	 */
	public ServiceWrite(String address, String i2cbus, String register, String buffer, String offset, String size, String dummy) {
		super();
		this.address = address;
		this.busnumber = i2cbus;
		this.register = register;
		this.buffer = buffer;
		this.offset = offset;
		this.size = size;
	}
	
	/**
	 * The method writes a byte value to an I2C device
	 * @return response, the answer from the write-operation
	 */
	public String [] writeByteValueToDevice() {
		this.information = "";
		
		try {
			if (this.busnumber.equals("1")) {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			} else {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_0);
			}
			i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
			// writes a byte value on an i2C-device
			i2cDevice.write((byte) Integer.parseInt(oneByte, 16));
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
	 * The method writes n byte values to an I2C device
	 * @return response, the answer from the write-operation
	 */
	public String [] writeByteValuesToDevice() {
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
			// writes n byte values to an i2C-device
			i2cDevice.write(convertedBuffer, Integer.parseInt(this.offset), Integer.parseInt(this.size));
			this.information = "";
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
	 * The method writes a byte value to a register
	 * @return response, the answer from the write-operation
	 */
	public String [] writeByteValueToRegister() {
		this.information = "";
		
		try {
			if (this.busnumber.equals("1")) {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			} else {
				i2cBus = I2CFactory.getInstance(I2CBus.BUS_0);
			}
			i2cDevice = i2cBus.getDevice(Integer.parseInt(address, 16));
			// writes a byte value to a register
			i2cDevice.write(Integer.parseInt(register, 16), (byte) Integer.parseInt(oneByte, 16));
			this.information = "";
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
	 * The method writes n byte values to a register
	 * @return response, the answer from the write-operation
	 */
	public String [] writeByteValuesToRegister() {
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
			// writes n byte values to a register
			i2cDevice.write(Integer.parseInt(register, 16), convertedBuffer, Integer.parseInt(this.offset), Integer.parseInt(this.size));
			this.information = "";
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
