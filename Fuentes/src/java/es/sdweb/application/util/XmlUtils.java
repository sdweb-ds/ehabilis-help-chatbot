package es.sdweb.application.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author Antonio Carro Mariño
 *
 */
public class XmlUtils {

	public static org.jdom.Element getElement(org.jdom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			StringTokenizer st = new StringTokenizer(entryName, "/");
			org.jdom.Element element = document.getRootElement();
	
			if (entryName.startsWith(element.getName()) || entryName.startsWith("/" + element.getName())) st.nextToken(); 
			while (st.hasMoreTokens() && element != null) {
				element = element.getChild(st.nextToken());
			}
			return element;
		}
		return null;
	}
	public static org.jdom.Element getElement(org.jdom.Element element, String entryName) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			org.jdom.Element aux = element;
			String elementEntryName = aux.getName();
			if (aux.getParent() != null) {
				while (!aux.getParent().equals(element.getDocument().getRootElement())) {
					aux = aux.getParentElement();
					elementEntryName = aux.getName() + "/" + elementEntryName;
				}
			}
			else {
				elementEntryName = null;
				StringTokenizer stEntry = new StringTokenizer(entryName, "/");
				String name = null;
				while (!aux.getName().equalsIgnoreCase(name) && stEntry.hasMoreTokens()) {
					name = stEntry.nextToken();
					elementEntryName = (elementEntryName==null?"":elementEntryName + "/") + name;
				}
			}

			StringTokenizer st = new StringTokenizer(entryName, "/");
			if (element.getDocument() != null && (entryName.startsWith(element.getDocument().getRootElement().getName()) || entryName.startsWith("/" + element.getDocument().getRootElement().getName()))) st.nextToken();
			StringTokenizer stElement = new StringTokenizer(elementEntryName, "/");
			if (st.countTokens() > stElement.countTokens()) {
				while (stElement.hasMoreTokens()) {
					String nameElement = stElement.nextToken();
					String name = st.nextToken();
					if (!name.equalsIgnoreCase(nameElement)) return null;
				}
				while (st.hasMoreTokens() && element != null) {
					element = element.getChild(st.nextToken());
				}
				return element;
			}
			else if (st.countTokens() == stElement.countTokens()) {
				return element;
			}
		}
		return null;
	}
	public static List getElementList(org.jdom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement == null) {
				return null;
			}
			else if (anteriorElement.getChildren(ultimoName) != null) {
				return anteriorElement.getChildren(ultimoName);
			}
		}
		return null;
	}
	public static void setElementValue(org.jdom.Document document, String entryName, String value) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)
				&& value != null) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.getAttribute(ultimoName) != null) {
					anteriorElement.getAttribute(ultimoName).setValue(value);
				}
				if (anteriorElement.getChild(ultimoName) != null) {
					anteriorElement.getChild(ultimoName).setText(value);
				}
			}
		}
	}
	public static void setElementValue(org.jdom.Element element, String entryName, String value) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)
				&& value != null) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(element, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.getAttribute(ultimoName) != null) {
					anteriorElement.getAttribute(ultimoName).setValue(value);
				}
				if (anteriorElement.getChild(ultimoName) != null) {
					anteriorElement.getChild(ultimoName).setText(value);
				}
			}
		}
	}
	public static String getElementValue(org.jdom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement == null) {
				return null;
			}
			else if (anteriorElement.getAttribute(ultimoName) != null) {
				return anteriorElement.getAttribute(ultimoName).getValue();
			}
			else if (anteriorElement.getChild(ultimoName) != null) {
				return anteriorElement.getChild(ultimoName).getText();
			}
		}
		return null;
	}
	public static String getElementValue(org.jdom.Element element, String entryName) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(element, anteriorName);
			if (anteriorElement == null) {
				return null;
			}
			else if (anteriorElement.getAttribute(ultimoName) != null) {
				return anteriorElement.getAttribute(ultimoName).getValue();
			}
			else if (anteriorElement.getChild(ultimoName) != null) {
				return anteriorElement.getChild(ultimoName).getText();
			}
		}
		return null;
	}
	

	public static void removeElement(org.jdom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.getAttribute(ultimoName) != null) {
					anteriorElement.removeAttribute(ultimoName);
				}
				else if (anteriorElement.getChild(ultimoName) != null) {
					anteriorElement.removeChild(ultimoName);
				}
			}
		}
	}
	public static void removeElement(org.jdom.Element element, String entryName) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.jdom.Element anteriorElement = getElement(element, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.getAttribute(ultimoName) != null) {
					anteriorElement.removeAttribute(ultimoName);
				}
				else if (anteriorElement.getChild(ultimoName) != null) {
					anteriorElement.removeChild(ultimoName);
				}
			}
		}
	}



	public static org.w3c.dom.Document parseDocument(InputStream xml) throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {
		ByteArrayOutputStream xmls = new ByteArrayOutputStream();
		OutputStreamWriter xmlw = new OutputStreamWriter(xmls, "UTF-8");

		byte[] buffer = new byte[1024];
		int l = 0;
		while ((l = xml.read(buffer)) > 0) xmls.write(buffer, 0, l); 

		DocumentBuilder docBuildXml = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document doc = docBuildXml.parse(new InputSource(new StringReader(new String(xmls.toByteArray()))));
		return doc;
	}
	public static org.w3c.dom.Document parseDocument(String xml) throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {
		ByteArrayOutputStream xmls = new ByteArrayOutputStream();
		OutputStreamWriter xmlw = new OutputStreamWriter(xmls, "UTF-8");
		xmls.write(xml.getBytes()); 

		DocumentBuilder docBuildXml = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document doc = docBuildXml.parse(new InputSource(new StringReader(new String(xmls.toByteArray()))));
		return doc;
	}
	public static org.w3c.dom.Element getElement(org.w3c.dom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			StringTokenizer st = new StringTokenizer(entryName, "/");
			org.w3c.dom.Element element = document.getDocumentElement();
	
			if (entryName.startsWith(element.getNodeName()) || entryName.startsWith("/" + element.getNodeName())) st.nextToken(); 
			while (st.hasMoreTokens() && element != null) {
				String name = st.nextToken();
				if (element.getElementsByTagName(name).item(0) instanceof org.w3c.dom.Element) {
					element = (org.w3c.dom.Element)element.getElementsByTagName(name).item(0);
				}
			}
			return element;
		}
		return null;
	}
	public static org.w3c.dom.Element getElement(org.w3c.dom.Element element, String entryName) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			org.w3c.dom.Element aux = element;
			String elementEntryName = aux.getNodeName();
			if (aux.getParentNode() != null) {
				while (!aux.getParentNode().equals(element.getOwnerDocument().getDocumentElement())) {
					aux = (org.w3c.dom.Element)aux.getParentNode();
					elementEntryName = aux.getNodeName() + "/" + elementEntryName;
				}
			}
			else {
				elementEntryName = null;
				StringTokenizer stEntry = new StringTokenizer(entryName, "/");
				String name = null;
				while (!aux.getNodeName().equalsIgnoreCase(name) && stEntry.hasMoreTokens()) {
					name = stEntry.nextToken();
					elementEntryName = (elementEntryName==null?"":elementEntryName + "/") + name;
				}
			}

			StringTokenizer st = new StringTokenizer(entryName, "/");
			if (element.getOwnerDocument() != null && (entryName.startsWith(element.getOwnerDocument().getDocumentElement().getNodeName()) || entryName.startsWith("/" + element.getOwnerDocument().getDocumentElement().getNodeName()))) st.nextToken();
			StringTokenizer stElement = new StringTokenizer(elementEntryName, "/");
			if (st.countTokens() > stElement.countTokens()) {
				while (stElement.hasMoreTokens()) {
					String nameElement = stElement.nextToken();
					String name = st.nextToken();
					if (!name.equalsIgnoreCase(nameElement)) return null;
				}
				while (st.hasMoreTokens() && element != null) {
					String name = st.nextToken();
					if (element.getElementsByTagName(name).item(0) instanceof org.w3c.dom.Element) {
						element = (org.w3c.dom.Element)element.getElementsByTagName(name).item(0);
					}
				}
				return element;
			}
			else if (st.countTokens() == stElement.countTokens()) {
				return element;
			}
		}
		return null;
	}
	public static org.w3c.dom.NodeList getElementList(org.w3c.dom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement == null) {
				return null;
			}
			else if (anteriorElement.getElementsByTagName(ultimoName) != null) {
				return anteriorElement.getElementsByTagName(ultimoName);
			}
		}
		return null;
	}
	
	public static void setElementValue(org.w3c.dom.Document document, String entryName, String value) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)
				&& value != null) 
		{
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.hasAttribute(ultimoName)) {
					anteriorElement.setAttribute(ultimoName, value);
				}
				else if (anteriorElement.getElementsByTagName(ultimoName) != null && anteriorElement.getElementsByTagName(ultimoName).getLength() > 0 && anteriorElement.getElementsByTagName(ultimoName).item(0) instanceof org.w3c.dom.Element) {
					((org.w3c.dom.Element)anteriorElement.getElementsByTagName(ultimoName).item(0)).setNodeValue(value);
				}
				else {
					anteriorElement.appendChild(document.createTextNode(value));
				}
			}
		}
	}
	public static void setElementValue(org.w3c.dom.Element element, String entryName, String value) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)
				&& value != null) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(element, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.hasAttribute(ultimoName)) {
					anteriorElement.setAttribute(ultimoName, value);
				}
				else if (anteriorElement.getElementsByTagName(ultimoName) != null && anteriorElement.getElementsByTagName(ultimoName).getLength() > 0 && anteriorElement.getElementsByTagName(ultimoName).item(0) instanceof org.w3c.dom.Element) {
					((org.w3c.dom.Element)anteriorElement.getElementsByTagName(ultimoName).item(0)).setNodeValue(value);
				}
				else {
					anteriorElement.appendChild(anteriorElement.getOwnerDocument().createTextNode(value));
				}
			}
		}
	}
	public static String getElementValue(org.w3c.dom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement == null) {
				return null;
			}
			else if (anteriorElement.hasAttribute(ultimoName)) {
				return anteriorElement.getAttribute(ultimoName);
			}
			else if (anteriorElement.getElementsByTagName(ultimoName) != null 
					&& anteriorElement.getElementsByTagName(ultimoName).getLength() > 0 
					&& anteriorElement.getElementsByTagName(ultimoName).item(0) instanceof org.w3c.dom.Element
					&& ((org.w3c.dom.Element)anteriorElement.getElementsByTagName(ultimoName).item(0)).getFirstChild() != null) {
				return ((org.w3c.dom.Element)anteriorElement.getElementsByTagName(ultimoName).item(0)).getFirstChild().getNodeValue();
			}
		}
		return null;
	}
	public static String getElementValue(org.w3c.dom.Element element, String entryName) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(element, anteriorName);
			if (anteriorElement == null) {
				return null;
			}
			else if (anteriorElement.hasAttribute(ultimoName)) {
				return anteriorElement.getAttribute(ultimoName);
			}
			else if (anteriorElement.getElementsByTagName(ultimoName) != null 
					&& anteriorElement.getElementsByTagName(ultimoName).getLength() > 0 
					&& anteriorElement.getElementsByTagName(ultimoName).item(0) instanceof org.w3c.dom.Element
					&& ((org.w3c.dom.Element)anteriorElement.getElementsByTagName(ultimoName).item(0)).getFirstChild() != null) {
				return ((org.w3c.dom.Element)anteriorElement.getElementsByTagName(ultimoName).item(0)).getFirstChild().getNodeValue();
			}
		}
		return null;
	}
	

	public static void removeElement(org.w3c.dom.Document document, String entryName) {
		if (document != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(document, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.hasAttribute(ultimoName)) {
					anteriorElement.removeAttribute(ultimoName);
				}
				else if (anteriorElement.getElementsByTagName(ultimoName) != null) {
					org.w3c.dom.NodeList list = anteriorElement.getElementsByTagName(ultimoName);
					for (int i=0; i<list.getLength(); i++) {
						anteriorElement.removeChild(list.item(i));	
					}
				}
			}
		}
	}
	public static void removeElement(org.w3c.dom.Element element, String entryName) {
		if (element != null 
				&& (entryName != null && entryName.trim().length()>0)) {
			String anteriorName = entryName.substring(0, entryName.lastIndexOf("/"));
			String ultimoName = entryName.substring(entryName.lastIndexOf("/") + 1);
			org.w3c.dom.Element anteriorElement = getElement(element, anteriorName);
			if (anteriorElement != null) {
				if (anteriorElement.hasAttribute(ultimoName)) {
					anteriorElement.removeAttribute(ultimoName);
				}
				else if (anteriorElement.getElementsByTagName(ultimoName) != null) {
					org.w3c.dom.NodeList list = anteriorElement.getElementsByTagName(ultimoName);
					for (int i=0; i<list.getLength(); i++) {
						anteriorElement.removeChild(list.item(i));	
					}
				}
			}
		}
	}

	public static org.w3c.dom.Element cloneNode(org.w3c.dom.Document doc, org.w3c.dom.Element ele) {
		org.w3c.dom.Element res = doc.createElement(ele.getNodeName());
		org.w3c.dom.NamedNodeMap attrs = ele.getAttributes();
		for (int i=0; i<attrs.getLength(); i++) {
			org.w3c.dom.Attr attr = (org.w3c.dom.Attr)attrs.item(i);
			res.setAttribute(attr.getName(), attr.getValue());
		}
		org.w3c.dom.NodeList children = ele.getChildNodes();
		for (int i=0; i<children.getLength(); i++) {
			if (children.item(i) instanceof org.w3c.dom.Element) {
				res.appendChild(cloneNode(doc, (org.w3c.dom.Element)children.item(i)));
			}
			if (children.item(i) instanceof org.w3c.dom.Text) {
				res.appendChild(doc.createTextNode(((org.w3c.dom.Text)children.item(i)).getNodeValue()));
			}
		}
		return res;
	}
}
