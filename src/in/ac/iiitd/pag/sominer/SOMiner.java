package in.ac.iiitd.pag.sominer;

import in.ac.iiitd.pag.util.SOUtil;
import in.ac.iiitd.pag.util.XMLUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * SOMiner is a tool to play around with Stackoverflow data dump. 
 * This data is used in a variety of research projects. 
 * The data dump is in XML format. It is available from here:
 *  https://archive.org/download/stackexchange/ for download.
 * We have tested it with stackoverflow.com-Posts.7z.
 * @author Venkatesh
 *
 */
public abstract class SOMiner {
	
	/**
	 * Indicates if the processing is done. Once set, 
	 * processRow method stops iterating.
	 */
	private static boolean IS_DONE = false;
	
	/**
	 * call this method to exit from mining.
	 */
	public static void stopMining() {
		IS_DONE = true;
	}
	
	/**
	 * Getter for IS_DONE. 
	 * @return
	 */
	private static boolean isDone() {
		return IS_DONE;
	}

	/**
	 * Mine Stackoverflow file.
	 * Calls the abstract method processRow for each post in the file.
	 * @param inputPath Gives the path to xml file with stackoverflow records.
	 */
	public void mine(String inputPath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputPath), 4 * 1024 * 1024);
			String line = null;			
			int lineCount = 0;
			System.out.println("Reading file...");
			while ((line = reader.readLine()) != null) {
				lineCount++;
				//if (lineCount % 2000 == 0) System.out.print(".");
				if (lineCount % 100000 == 0) {System.out.println(lineCount);}
				try {
					if (!line.trim().startsWith("<row")) continue;
					
					XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();        
			        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new StringReader(line));        
			        while(xmlEventReader.hasNext()){
			           XMLEvent xmlEvent = xmlEventReader.nextEvent();
			           if (xmlEvent.isStartElement()){
			               StartElement startElement = xmlEvent.asStartElement();
			               if(startElement.getName().getLocalPart().equalsIgnoreCase("row")){	
			            	   if (!isDone()) processRow(startElement, line);
			               }
			           }
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Get any string element like Title, Body etc from SO (stackoverflow) file.
	 * @param startElement
	 * @param item Any item to be retrieved such as Title or Body.
	 * @return
	 */
	public String getStringElement(StartElement startElement, String item) {
	   String value = XMLUtil.getStringElement(startElement, item);       
 	   if ((value == null)||(value.trim().length()==0)) {
 		   value = "";
 	   } 
 	   return value;
	}
	
	/**
	 * Get any int element like Title, Body etc from SO (stackoverflow) file.
	 * @param startElement
	 * @param item
	 * @return
	 */
	public int getIntElement(StartElement startElement, String item) {
		   int value = XMLUtil.getIntElement(startElement, item); 
	 	   return value;
	}
	
	/**
	 * Return Id of the post. This is the value of item with name "Id" in 
	 * the XML row of each post in the input SO XML.
	 * @param startElement
	 * @return
	 */
	public int getId(StartElement startElement) {
		return getIntElement(startElement, "Id");
	}
	
	/**
	 * Return ParentId of the post. This is the value of item with name "ParentId" in 
	 * the XML row of each post in the input SO XML.
	 * @param startElement
	 * @return
	 */
	public int getParentId(StartElement startElement) {
		return getIntElement(startElement, "ParentId");
	}
	
	/**
	 * Return Title of the post. This is the value of item with name "Title" in 
	 * the XML row of each post in the input SO XML.
	 * @param startElement
	 * @return
	 */
	public String getTitle(StartElement startElement) {
		String title = getStringElement(startElement, "Title");
		return title;
	}
	
	/**
	 * Return Body of the post. This is the value of item with name "Body" in 
	 * the XML row of each post in the input SO XML.
	 * @param startElement
	 * @return
	 */
	public String getBody(StartElement startElement) {
		String value = getStringElement(startElement, "Body");
		return value;
	}
	
	/**
	 * Return CreationDate of the post. This is the value of item with name "CreationDate" in 
	 * the XML row of each post in the input SO XML.
	 * @param startElement
	 * @return
	 */
	public String getCreationDate(StartElement startElement) {
		String value = getStringElement(startElement, "CreationDate");
		return value;
	}
	
	/**
	 * Return set of all code snippets in the post. 
	 * 
	 * @param startElement
	 * @return
	 */
	public Set<String> getCodeSet(StartElement startElement) {
		String body = getBody(startElement);
		Set<String> codeSet = SOUtil.getCodeSet(body);
		return codeSet;
	}
	
	/**
	 * For each row in the input Stack Overflow XML, this method is called.
	 * 
	 * @param startElement This is the handle to the XML. Pass this to other SOMiner methods.
	 * @param line Each XML line as string.
	 * @throws IOException
	 */
	public abstract void processRow(StartElement startElement, String line) throws IOException;
	
}
