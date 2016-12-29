/* Project - POOF */
package poof.system;

import java.io.Serializable;

/**
 * class Entry
 *    A generic entry stored in the file system. Provides the base structure of its subclasses.
 */
abstract class Entry implements Serializable {
	/* the entry's name */
	private String _name;
	
	/* the entry's parent directory */
	private Directory _parent;
	
	/* the entry's owner */
	private User _owner;
	
	/* the entry's privacy */
	private boolean _privacy=false;
	
	/**
    * Creates a new Entry
    *
	 * @param name
	 *             the entry's name
	 *
	 * @param parent
	 *             the entry's parent directory
	 *
	 * @param owner
	 *             the entry's owner
	 *
	 * @param privacy
	 *             the entry's privacy
	 */
	protected Entry(String name, Directory parent, User owner, boolean privacy) {
		_name=name;
		_parent=parent;
		_owner=owner;
		_privacy=privacy;
	}
	
	/**
    * Creates a new Entry
    *
	 * @param name
	 *             the entry's name
	 *
	 * @param parent
	 *             the entry's parent directory
	 *
	 * @param owner
	 *             the entry's owner
	 */
	protected Entry(String name, Directory parent, User owner) {
		_name=name;
		_parent=parent;
		_owner=owner;
	}
	
	/**
    * Creates a new Entry
    *
	 * @param name
	 *             the entry's name
	 *
	 * @param owner
	 *             the entry's owner
	 */
	protected Entry(String name, User owner) {
      _name=name;
      _owner=owner;  
   }
   
	/**
	 * @return the entry's name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * @return the entry's parent directory
	 */
	public Directory getParent() {
		return _parent;
	}
	
	/**
	 * @return the entry's owner
	 */
	public User getOwner() {
		return _owner;
	}
	
	/**
	 * @return the entry's privacy
	 */
	public boolean getPrivacy() {
		return _privacy;
	}
	
	/**
	 * Abstract method which must be implemented by each subclass
	 *
	 * @return the entry's size
	 */
	abstract int getSize();
	
	/**
    * Change the entry's name
    *
	 * @param name
	 *             the entry's new name
	 */
	public void setName(String name) {
		_name=name;
	}
	
	/**
    * Change the entry's parent directory
    *
	 * @param parent
	 *             the entry's new parent directory
	 */
	public void setParent(Directory parent) {
		_parent=parent;
	}
	
	/**
    * Change the entry's owner
    *
	 * @param owner
	 *             the entry's new owner
	 */
	public void setOwner(User owner) {
		_owner=owner;
	}
	
	/**
    * Change the entry's privacy
    *
	 * @param privacy
	 *            the entry's new privacy
	 */
	public void setPrivacy(boolean privacy) {
		_privacy=privacy;
	}	
	
   /**
    * @return true if this entry is a file; false otherwise
	 */
	public boolean isFile() {
		return false;
	}	

   /**
    * @return true if this entry is a directory; false otherwise 
	 */
	public boolean isDirectory() {
		return false;
	}	
	
	
   /**
	 * @return the representation of the entry
	 */
	public String getInfo() {
	   return ( ( getPrivacy()? "w " : "- " ) + getOwner().getUname() + " " + getSize() );
   }

}
