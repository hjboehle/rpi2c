package info.boehle.rpi2c.restservices;

import java.util.Arrays;

/**
 * Class to model objects to convert input parameters.
 * @author developer
 * @version 1.0.0
 *
 */
public class ParamConverter {
	String paramInputvalueString;
	byte [] paramInputvalueByte;
	
	/**
	 * Constructor for ParamConverter (convert a string)
	 * @param paramInputvalue, value to be converted as a string
	 */
	public ParamConverter(String paramInputvalue) {
		super();
		this.paramInputvalueString = paramInputvalue;
	}
	
	/**
	 * Constructor for ParamConverter (convert an array)
	 * @param paramInputvalue, value to be converted as a string
	 */
	public ParamConverter(byte [] paramInputvalue) {
		super();
		this.paramInputvalueByte = paramInputvalue;
	}
	
	/**
	 * method to convert a string in a binary value in byte array 
	 * @return paramOutputvalue
	 */
	public byte [] getStringHexToBytes() {
		int numberOfBytes;
		int bufferIntDec;
		int paramInputSize = this.paramInputvalueString.length();
		if (paramInputSize%2 != 0) {
			// size is an odd number, hexadecimal number, a 0 is added first
			paramInputvalueString = "0" + paramInputvalueString;
			paramInputSize = paramInputSize +1;
		}
		numberOfBytes = paramInputSize / 2;
		byte [] paramOutputValue = new byte[numberOfBytes];
		// create the byte array
		for (int i = paramInputSize-1; i > 0; i = i-2) {
			bufferIntDec = Integer.parseInt(paramInputvalueString.substring(i-1, i+1), 16);
			paramOutputValue[numberOfBytes-1] = (byte) bufferIntDec;
			numberOfBytes--;
		}
		return paramOutputValue;
	}
	
	/**
	 * method to convert a binary value in byte array to a string 
	 * @return paramOutputvalue
	 */
	public String getBytesToStringHex() {
		String paramOutputValue = "";
		int paramOutputSize = 0;
		for (int i = this.paramInputvalueByte.length - 1; i >= 0 ; i--) {
			paramOutputValue = Integer.toHexString((int) this.paramInputvalueByte[i] & 0xFF) + paramOutputValue;
			paramOutputSize = paramOutputValue.length();
			if (paramOutputSize%2 != 0) {
				// size is an odd number, hexadecimal number, a 0 is added first
				paramOutputValue = "0" + paramOutputValue;
			}
		}
		return paramOutputValue.toUpperCase();
	}

	public String toStringBytes() {
		return "ParamConverter [paramInputvalue=" + paramInputvalueString + ", getStringHexToBytes()="
				+ Arrays.toString(getStringHexToBytes()) + "]";
	}


}
