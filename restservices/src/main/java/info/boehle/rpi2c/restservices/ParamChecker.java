package info.boehle.rpi2c.restservices;

/**
 * This class models objects that are used to validate parameters passed in a
 * URL. The URL belongs to a REST API, to use I2C functions of the Raspberry Pi.
 * 
 * @author Hans Jürgen Böhle
 * @version 1.0.0
 */

public class ParamChecker {
	String paramI2cbus;
	String paramAddress;
	String paramRegister;
	String paramOneByte = "";
	String paramBuffer;
	String paramOffset;
	String paramSize;
	static final int LOWER_ADDRESS_LIMIT = 0x03; // limit given by i2c architecture on raspberry pi
	static final int UPPER_ADDRESS_LIMIT = 0x77; // limit given by i2c architecture on raspberry pi
	static final int LOWER_BYTE_LIMIT = 0x00;
	static final int UPPER_BYTE_LIMIT = 0xFF;
	static final String REGEX_BIN = "[0-1]+";
	static final String REGEX_DEC = "[0-9]+";
	static final String REGEX_HEX = "[0-9A-Fa-f]+";

	/**
	 * Constructor of ParamChecker for the read-services
	 * 
	 * @param paramI2cbus,
	 *            number of the I2C-Bus (0 or 1)
	 * @param paramAddress,
	 *            address of a I2C-Device (hexadecimal from 3 till 77)
	 * @param paramRegister,
	 *            register of a I2C-Device (hexadecimal)
	 * @param paramBuffer,
	 *            buffer for bytes in hexadecimal form (from 00 till FF)
	 * @param paramOffset,
	 *            position specification integer in decimal form
	 * @param paramSize,
	 *            size specification integer in decimal form
	 */
	public ParamChecker(String paramAddress, String paramI2cbus, String paramRegister, String paramBuffer,
			String paramOffset, String paramSize) {
		super();
		this.paramI2cbus = paramI2cbus;
		this.paramAddress = paramAddress;
		this.paramRegister = paramRegister;
		this.paramBuffer = paramBuffer;
		this.paramOffset = paramOffset;
		this.paramSize = paramSize;
	}

	/**
	 * Constructor of ParamChecker for the write-services
	 * 
	 * @param paramI2cbus,
	 *            number of the I2C-Bus (0 or 1)
	 * @param paramAddress,
	 *            address of a I2C-Device (hexadecimal from 3 till 77)
	 * @param paramOneByte,
	 *            one Byte to write on a I2C-Device (hexadecimal from 00 till FF)
	 * @param paramRegister,
	 *            register of a I2C-Device (hexadecimal)
	 * @param paramBuffer,
	 *            buffer for bytes in hexadecimal form (from 00 till FF)
	 * @param paramOffset,
	 *            position specification integer in decimal form
	 * @param paramSize,
	 *            size specification integer in decimal form
	 */
	public ParamChecker(String paramAddress, String paramI2cbus, String paramOneByte, String paramRegister, String paramBuffer,
			String paramOffset, String paramSize) {
		super();
		this.paramI2cbus = paramI2cbus;
		this.paramAddress = paramAddress;
		this.paramRegister = paramRegister;
		this.paramOneByte = paramOneByte;
		this.paramBuffer = paramBuffer;
		this.paramOffset = paramOffset;
		this.paramSize = paramSize;
	}

	public String getResultsOnValidity() {
		String returnMessage = "valid";
		boolean regularity;

		// check paramAddress
		if (this.paramAddress.equals("")) {
			returnMessage = "mandatory parameter address not included in URL";
			return returnMessage;
		}
		regularity = this.paramAddress.matches(REGEX_HEX);
		if (!regularity) {
			returnMessage = "parameter address is not a hexadecimal value";
			return returnMessage;
		}
		if (!(Integer.parseInt(this.paramAddress, 16) >= LOWER_ADDRESS_LIMIT)
				|| !(Integer.parseInt(this.paramAddress, 16) <= UPPER_ADDRESS_LIMIT)) {
			returnMessage = "parameter address is not in the permitted address range";
			return returnMessage;
		}

		// check paramI2cbus
		regularity = this.paramI2cbus.matches(REGEX_DEC);
		if (!regularity) {
			returnMessage = "parameter i2cbus is not an integer in decimal form";
			return returnMessage;
		}
		if (!(this.paramI2cbus.equals("0") || this.paramI2cbus.equals("1"))) {
			returnMessage = "parameter i2cbus have not the value 0 or 1";
			return returnMessage;
		}

		// check paramRegister
		if (!this.paramRegister.equals("")) {
			regularity = this.paramRegister.matches(REGEX_HEX);
			if (!regularity) {
				returnMessage = "parameter register is not a hexadecimal value";
				return returnMessage;
			}
		}

		// check paramOneByte
		if (!this.paramOneByte.equals("")) {
			regularity = this.paramOneByte.matches(REGEX_HEX);
			if (!regularity) {
				returnMessage = "parameter byte is not a hexadecimal value";
				return returnMessage;
			}
			if (!(Integer.parseInt(this.paramOneByte, 16) >= LOWER_BYTE_LIMIT)
					|| !(Integer.parseInt(this.paramOneByte, 16) <= UPPER_BYTE_LIMIT)) {
				returnMessage = "parameter byte is not in the permitted range";
				return returnMessage;
			}
		}

		// check paramBuffer
		if (!this.paramBuffer.equals("")) {
			regularity = this.paramBuffer.matches(REGEX_HEX);
			if (!regularity) {
				returnMessage = "parameter buffer is not a hexadecimal value";
				return returnMessage;
			}
		}

		// check paramOffset
		if (!this.paramOffset.equals("")) {
			regularity = this.paramOffset.matches(REGEX_DEC);
			if (!regularity) {
				returnMessage = "parameter offset is not an integer in decimal format";
				return returnMessage;
			}
		}

		// check paramSize
		if (!this.paramSize.equals("")) {
			regularity = this.paramSize.matches(REGEX_DEC);
			if (!regularity) {
				returnMessage = "parameter size is not an integer in decimal format";
				return returnMessage;
			}
		}

		return returnMessage;
	}

	public String getReadCase() {
		String readCase = "ncfu"; // read case: no case found in URL

		if (this.paramRegister.equals("")) {
			// parameter register is not given
			if (this.paramBuffer.equals("") && this.paramOffset.equals("") && this.paramSize.equals("")) {
				// parameter register, parameter byte, buffer, offset and size are not given
				readCase = "1bvd"; // read case: read one byte value from i2c device
				return readCase;
			}
			if (!this.paramBuffer.equals("") && !this.paramOffset.equals("") && !this.paramSize.equals("")) {
				// parameter register is not given, parameter buffer, offset, size are given
				readCase = "nbvd"; // read case: read n byte values from i2c device
				return readCase;
			}
		} else {
			// parameter register is given
			if (this.paramBuffer.equals("") && this.paramOffset.equals("") && this.paramSize.equals("")) {
				// parameter register is given, parameter buffer, offset and size are not given
				readCase = "1bvr"; // read case: read one byte value from register
				return readCase;
			}
			if (!this.paramBuffer.equals("") && !this.paramOffset.equals("") && !this.paramSize.equals("")) {
				// parameter register buffer, offset, size are given
				readCase = "nbvr"; // read case: read n byte values from register
				return readCase;
			}
		}
		return readCase;
	}

	public String getWriteCase() {
		String writeCase = "ncfu"; // write case: no case found in URL

		if (this.paramRegister.equals("")) {
			// parameter register is not given
			if (!this.paramOneByte.equals("") && this.paramBuffer.equals("") && this.paramOffset.equals("") && paramSize.equals("")) {
				// parameter register, parameter byte, buffer, offset and size are not given, byte is given
				writeCase = "1bvd"; // write case: write one byte value to i2c device
				return writeCase;
			}
			if (this.paramOneByte.equals("") && !this.paramBuffer.equals("") && !this.paramOffset.equals("") && !this.paramSize.equals("")) {
				// parameter register and byte are not given, parameter buffer, offset, size are given
				writeCase = "nbvd"; // read case: read n byte values from i2c device
				return writeCase;
			}
		} else {
			// parameter register is given
			if (!this.paramOneByte.equals("") && this.paramBuffer.equals("") && this.paramOffset.equals("") && this.paramSize.equals("")) {
				// parameter register and byte are given, parameter buffer, offset and size are not given
				writeCase = "1bvr"; // read case: read one byte value from register
				return writeCase;
			}
			if (this.paramOneByte.equals("") && !this.paramBuffer.equals("") && !this.paramOffset.equals("") && !this.paramSize.equals("")) {
				// parameter register buffer, offset, size are given, parameter byte is not given
				writeCase = "nbvr"; // read case: read n byte values from register
				return writeCase;
			}
		}
		return writeCase;
	}

}
