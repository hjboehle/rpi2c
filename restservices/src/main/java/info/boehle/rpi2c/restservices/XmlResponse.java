package info.boehle.rpi2c.restservices;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlResponse {
	String xmlMessage;
	String information;
	String returnmessage;
	String returncode;

	public XmlResponse(String i, String rm, String rc) {
		this.information = i;
		this.returnmessage = rm;
		this.returncode = rc;

	}

	public String getXmlMessage() {

		Element rpi2c = new Element("rpi2c");
		Document xmlDocument = new Document();
		xmlDocument.setRootElement(rpi2c);

		Element message = new Element("message");
		message.addContent(new Element("information").setText(this.information));
		message.addContent(new Element("returnmessage").setText(this.returnmessage));
		message.addContent(new Element("returncode").setText(this.returncode));
		
		xmlDocument.getRootElement().addContent(message);
		
		XMLOutputter xmlOutputter = new XMLOutputter();
		
		xmlOutputter.setFormat(Format.getPrettyFormat());
		xmlMessage = xmlOutputter.outputString(xmlDocument);
		
		return this.xmlMessage;
		
	}

}
