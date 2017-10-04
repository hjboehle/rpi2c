package info.boehle.rpi2c.restservices;

/**
 * This class models a value that is either binary, octal or hexidecimal.
 * Further, the value has an upper an lower limit. When des model is
 * constructed, it is not ensured whether the value is valid for its definition.
 * 
 * @author Hans Jürgen Böhle
 * @version 1.0.0
 *
 */

public class UncheckedValue {
	String value;
	String lowerLimit;
	String upperLimit;
	int numberBase;

	/**
	 * Constructor of a UncheckedValue
	 * 
	 * @param v
	 *            value of the unchecked value
	 * @param nB
	 *            number base of the unchecked value
	 * @param lL
	 *            lower validity limit, so that the value is valid
	 * @param uL
	 *            upper validity limit, so that the value is valid
	 */

	public UncheckedValue(String v, int nB, String lL, String uL) {
		super();
		this.value = v;
		this.numberBase = nB;
		this.lowerLimit = lL;
		this.upperLimit = uL;
	}

	/**
	 * The getValidity method determines whether the value is regular and valid
	 * within the limits
	 * 
	 * @return true when the value is valid, false when the value is not valid
	 */

	public boolean getValidity() {
		boolean validity = true;
		String regexValue = null;
		long valueDec;
		long lowerLimitDec;
		long upperLimitDec;
		// select the correct regular expression
		switch (this.numberBase) {
		case 2: // value is a dual number
			regexValue = "[0-1]+";
			break;
		case 8: // value is an octal number
			regexValue = "[0-7]+";
			break;
		case 16: // value is a hexadecimal number
			regexValue = "[0-9A-Fa-f]+";
			break;
		default: // all other cases
			validity = false;
			return validity;
		}
		// check for regularity of the value
		validity = this.value.matches(regexValue);
		if (!validity) {
			return validity;
		}
		// check for regularity of the limits
		validity = this.lowerLimit.matches(regexValue);
		if (!validity) {
			return validity;
		}
		validity = this.upperLimit.matches(regexValue);
		if (!validity) {
			return validity;
		}
		// converts the value and limits to decimal numbers
		valueDec = Long.parseLong(this.value, this.numberBase);
		lowerLimitDec = Long.parseLong(this.lowerLimit, this.numberBase);
		upperLimitDec = Long.parseLong(this.upperLimit, this.numberBase);
		// verify that the value is within the range
		if (!(valueDec >= lowerLimitDec) || !(valueDec <= upperLimitDec)) {
			validity = false;
		}
		return validity;
	}

}
