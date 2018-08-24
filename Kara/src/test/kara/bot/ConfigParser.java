package test.kara.bot;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigParser {
	private Map<String,String> configValues = new HashMap<String,String>();
	
	private ConfigParser() {
	}
	
	public ConfigParser(String configPath) throws ConfigParserException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File(configPath));
			
			NodeList authKeyNodes = doc.getElementsByTagName("authKey");
			String authKey = authKeyNodes.item(0).getTextContent();
			configValues.put("authKey", authKey);
			
		} catch (ParserConfigurationException e) {
			throw new ConfigParserException(e.getMessage());
		} catch (SAXException e) {
			throw new ConfigParserException(e.getMessage());
		} catch (IOException e) {
			throw new ConfigParserException(e.getMessage());
		}		
	}
	
	public String getConfigValue(String key) {
		return configValues.get(key);		
	}
}
