package pl.edu.uj.mq;

import javax.xml.parsers.*;
import org.w3c.dom.*;


/**
 * @TODO
 *
 */
public class XMLLoader {

	public String get()
	{
	        try
	        {
	        	
	            DocumentBuilder builder =
	                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            Document doc = builder.parse("http://www.money.pl/gielda/gpw/akcje/?sektor=0&indeks=WIG&typ=0&refresh_time=60");

	            NodeList tables = doc.getElementsByTagName("table");
	            if (tables != null && tables.getLength() > 0)
	            {
	                for (int i = 0; i < tables.getLength(); i++)
	                {
	                    Element table = (Element)tables.item(i);

	                    if(table.getAttribute("name") == "main")
	                    {
	                    	NodeList index = table.getElementsByTagName("tr");
	                    	
	                    	return new Integer(index.getLength()).toString();

	                    	/*
		                    String  = getTextValue(uzytkownik, "imie");
		                    String nazwisko = getTextValue(uzytkownik, "nazwisko");
		                    String miejscowosc = getTextValue(uzytkownik, "miejscowosc");
		                    String typMiejscowosci = getAttrValue(uzytkownik, "miejscowosc", "typ");
		                    */
	                    }
	                    
	                }
	            }
	            return "123";


	        } catch (Exception e)
	        {
	            e.printStackTrace();
	        }	        
	        
			return "-1";
	}
	
	
	private static String getAttrValue(Element ele, String tagName, String attrName) {
		String attrVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			attrVal = el.getAttribute(attrName);
		}

		return attrVal;
	}

    private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}


    public static String getCharacterDataFromElement(Element e)
    {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData)
        {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
	
	
}
