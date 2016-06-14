package in.ac.iiitd.pag.sominerdemo;

import java.io.IOException;

import javax.xml.stream.events.StartElement;

import in.ac.iiitd.pag.sominer.SOMiner;

/**
 * A demo class to show how to use SOMiner.
 * 
 * @author Venkatesh
 *
 */
public class Demo extends SOMiner {

	public static void main(String[] args) {
		String filePath = "smallFile.xml";
		Demo demo = new Demo();
		demo.mine(filePath); //Mine and call processRow for each row.		
	}

	@Override
	public void processRow(StartElement startElement, String line)
			throws IOException {
		int id = getId(startElement);
		String title = getTitle(startElement); //Get Title from each line.
		if (title.length() > 0)
			System.out.println(id + ": " + title);
	}
}
