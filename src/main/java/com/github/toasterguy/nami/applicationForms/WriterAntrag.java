package com.github.toasterguy.nami.applicationForms;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;


/**
 * abstract class to generate and output application form.
 * 
 * @author Tobias Miosczka
 *
 */
public abstract class WriterAntrag {

	static {
		// set loglevel off
		Level level = Level.OFF;
		Logger.getLogger("org.apache.zookeeper").setLevel(level);
		Logger.getLogger("org.apache.hadoop.hbase.zookeeper").setLevel(level);
		Logger.getLogger("org.apache.hadoop.hbase.client").setLevel(level);
	}

	/**
	 * manipulates and saves ONE page
	 * 
	 * @param output
	 * 				path to output file
	 * @param participants
	 * 				List of all participants
	 */
	private void runOneDoc(String output, List<NamiMitglied> participants) throws Exception{
		//input
		InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream("applicationForms/"+getResourceFileName());
		TextDocument odtDoc = TextDocument.loadDocument(s);
		//manipulate doc
		doTheMagic(participants, odtDoc);

		//output
		odtDoc.save(output);
	}
	
	/**
	 * Returns a path as string with the given page number.
	 * 
	 * @param filename
	 * 				filename
	 * @param number
	 * 				number of the current page
	 * @return
	 * 				Returns a path as string with the given page number.
	 * 				Example: "example.odt" -> "example-1.odt"
	 */
	private String convertPathString(String filename, int number){
		return filename.substring(0, filename.length() - 4) + "-" + String.valueOf(number) + filename.substring(filename.length() - 4, filename.length());
	}
	
	/**
	 * manipulates and saves ALL pages
	 * 
	 * @param output
	 * 				path to output file
	 * @param participants
	 * 				List of all participants
	 */
	public void run(String output, List<NamiMitglied> participants) throws Exception, NoParticipantsException {
		int pages = (int) Math.ceil((double)(participants.size())/(double)(getMaxParticipantsPerPage()));
		if(pages == 0){
			//no pages to export
			throw new NoParticipantsException();
		}
        //get info
		if(getMaxParticipantsPerPage() == 0){
			runOneDoc(output, participants);
		} else {
			for(int i = 0; i < pages; i++){
				int from = i * getMaxParticipantsPerPage();
				int to = ((i + 1) * getMaxParticipantsPerPage());
				runOneDoc(convertPathString(output, i + 1), participants.subList(from, to > participants.size() ? participants.size() : to));
			}
		}
	}
	
		
	/**
	 * Returns the number of participants on one page of the application form.
	 * If it returns 0, an infinite number of participants can be inserted in one page.
	 * 
	 * @return number of participants on one page of the application form, returns 0 if this number is infinite
	 */
	protected abstract int getMaxParticipantsPerPage();
	
	/**
	 * abstract function to manipulate the application form
	 * 
	 * @param participants
	 * 				List of all participants
	 * @param odtDoc
	 * 				document object
	 */
	protected abstract void doTheMagic(List<NamiMitglied> participants, TextDocument odtDoc);
		
	/**
	 * returns resource file name as a string. the resource files saved in "/resources"
	 * 
	 * @return
	 * 				returns file name as a string
	 */
	protected abstract String getResourceFileName();
}