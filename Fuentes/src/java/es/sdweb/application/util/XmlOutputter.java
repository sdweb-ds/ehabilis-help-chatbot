package es.sdweb.application.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 
 * @author Antonio Carro Mariño
 */
public class XmlOutputter {

	private static final String STRING_NEWLINE = "\r\n";
	private static final String STRING_TABCHAR = "\t";

	private boolean expandEmptyElements = false;
	public void setExpandEmptyElements(boolean expandEmptyElements) {this.expandEmptyElements = expandEmptyElements;}
	public boolean getExpandEmptyElements() {return expandEmptyElements;}

	private String encoding = null;
	public void setEncoding(String encoding) {this.encoding = encoding;}
	public String getEncoding() {return encoding;}

	private boolean suppressDeclaration = false;
	public void setSuppressDeclaration(boolean suppressDeclaration) {this.suppressDeclaration = suppressDeclaration;}
	public boolean getSuppressDeclaration() {return suppressDeclaration;}

	private boolean indent = false;
	public void setIndent(boolean indent) {this.indent = indent;}
	public boolean getIndent() {return indent;}

	private boolean newlines = false;
	public void setNewlines(boolean newlines) {this.newlines = newlines;}
	public boolean getNewlines() {return newlines;}

	private boolean trimText = false;
	public void setTrimText(boolean trimText) {this.trimText = trimText;}
	public boolean getTrimText() {return trimText;}

	private static String encode(String str, String encoding) throws IOException {
		if (encoding == null) return str;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(str.getBytes());
		return os.toString(encoding);
	}

	private void outputLevel(org.jdom.Element el, OutputStream out, int level) throws IOException {
		if (indent) {
			for (int i=0; i<level; i++) out.write(STRING_TABCHAR.getBytes());
		}
		out.write(("<" + el.getName()).getBytes());
		List atts = el.getAttributes();
		for (int i=0; i<atts.size(); i++) {
			org.jdom.Attribute att = (org.jdom.Attribute)atts.get(i);
			out.write((" " + att.getName() + "=\"" + encode(att.getValue(), encoding) + "\"").getBytes());
		}
		el.getAttributes();

		String text = encode(el.getText()==null?"":el.getText(), encoding);
		List chln = el.getChildren();
		if (chln.size() == 0 && !expandEmptyElements && text.trim().length() == 0) {
			out.write(("/>").getBytes());
			return;
		}
		else {
			out.write((">").getBytes());
			if (text.trim().length() > 0) {
				if (trimText) {
					text = text.trim();
				}
				out.write(text.getBytes());
			}
			else if (chln.size() > 0){
				if (newlines) {
					out.write(STRING_NEWLINE.getBytes());
				}
				for (int i=0; i<chln.size(); i++) {
					org.jdom.Element chl = (org.jdom.Element)chln.get(i);
					outputLevel(chl, out, level+1);
					if (newlines) out.write(STRING_NEWLINE.getBytes());
				}
				if (newlines && indent) {
					for (int i=0; i<level; i++) out.write(STRING_TABCHAR.getBytes());
				}
			}
			out.write(("</" + el.getName() + ">").getBytes());
		}
	}

	private void outputLevel(org.w3c.dom.Element el, OutputStream out, int level) throws IOException {
		if (indent) {
			for (int i=0; i<level; i++) out.write(STRING_TABCHAR.getBytes());
		}
		out.write(("<" + el.getNodeName()).getBytes());
		org.w3c.dom.NamedNodeMap atts = el.getAttributes();
		for (int i=0; i<atts.getLength(); i++) {
			org.w3c.dom.Node att = (org.w3c.dom.Node)atts.item(i);
			out.write((" " + att.getNodeName() + "=\"" + encode(att.getNodeValue(), encoding) + "\"").getBytes());
		}
		el.getAttributes();

		String text = encode(el.getFirstChild() == null || el.getFirstChild().getNodeValue() == null?"":el.getFirstChild().getNodeValue(), encoding);
		org.w3c.dom.NodeList chln = el.getChildNodes();
		if (chln.getLength() == 0 && !expandEmptyElements && text.trim().length() == 0) {
			out.write(("/>").getBytes());
			return;
		}
		else {
			out.write((">").getBytes());
			if (text.trim().length() > 0) {
				if (trimText) {
					text = text.trim();
				}
				out.write(text.getBytes());
			}
			else if (chln.getLength() > 0){
				if (newlines) {
					out.write(STRING_NEWLINE.getBytes());
				}
				for (int i=0; i<chln.getLength(); i++) {
					if (chln.item(i) instanceof org.w3c.dom.Element) {
						org.w3c.dom.Element chl = (org.w3c.dom.Element)chln.item(i);
						outputLevel(chl, out, level+1);
						if (newlines) out.write(STRING_NEWLINE.getBytes());
					}
				}
				if (newlines && indent) {
					for (int i=0; i<level; i++) out.write(STRING_TABCHAR.getBytes());
				}
			}
			out.write(("</" + el.getNodeName() + ">").getBytes());
		}
	}

	public void output(org.jdom.Document doc, OutputStream out) throws IOException {
		if (!suppressDeclaration) {
			out.write(("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + STRING_NEWLINE).getBytes());
		}
		outputLevel(doc.getRootElement(), out, 0);
		out.write(STRING_NEWLINE.getBytes());
	}
	public void output(org.jdom.Element el, OutputStream out) throws IOException {
		outputLevel(el, out, 0);
	}

	public String outputString(org.jdom.Document doc) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		output(doc, out);
		return new String(out.toByteArray());
	}
	public String outputString(org.jdom.Element el) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		output(el, out);
		return new String(out.toByteArray());
	}

	public void output(org.w3c.dom.Document doc, OutputStream out) throws IOException {
		if (!suppressDeclaration) {
			out.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + STRING_NEWLINE).getBytes());
		}
		outputLevel(doc.getDocumentElement(), out, 0);
		out.write(STRING_NEWLINE.getBytes());
	}
	public void output(org.w3c.dom.Element el, OutputStream out) throws IOException {
		outputLevel(el, out, 0);
	}

	public String outputString(org.w3c.dom.Document doc) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		output(doc, out);
		return new String(out.toByteArray());
	}
	public String outputString(org.w3c.dom.Element el) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		output(el, out);
		return new String(out.toByteArray());
	}

}
