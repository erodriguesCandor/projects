/* Project - POOF */
package poof.system;

import java.io.Serializable;

/**
 * class File
 *    Stores text. It is possible to add text to the file or read its contents.
 */
public class File extends Entry implements Serializable {	
	/** the file's content */
	private String _content = new String();
	
	/**
	 * Creates a new file
	 */
	public File(String name, Directory parent, User owner) {
		super(name,parent,owner);
	}
	
	/**
	 * Creates a new file
	 */
	public File(String name, Directory parent, User owner, boolean privacy) {
		super(name,parent,owner,privacy);
	}
	
	/**
	 * Creates a new file
	 */
	public File(String name, Directory parent, User owner, boolean privacy, String content) {
		super(name,parent,owner,privacy);
		setContent(content);
	}
	
	/** 
	 * @return the file's content
	 */
	public String getContent() {
		return _content;
	}
	
	/** 
	 * @return the file's size
	 */
	@Override
	public int getSize() {
		return _content.length();
	}
	
	/** 
	 * Changes the file's content
	 *
	 * @param content
    *				the file's new content
	 */
	public void setContent(String content) {
		_content = content+"\n";
	}
	
	/** 
	 * Adds content to the file
	 *
	 * @param content
    *				the file's additional content
	 */
	public void addContent(String content) {
		_content += content+"\n";
	}
	
   /**
    * @return true, since this is a file
	 */
	@Override
	public boolean isFile() {
		return true;
	}	
	
	/**
	 * @return the representation of the file
	 */
	@Override
	public String getInfo() {
	   return ( "- " + super.getInfo());
   }
}
