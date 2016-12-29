/* Project - POOF */
package poof.system;

import poof.exception.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.*;

/**
 * class Directory
 *    Stores a collection of the entries it owns. Accesses their different attributes to respond to the file system's requests.
 */
public class Directory extends Entry implements Serializable {
	/* the directory's entries */
	private TreeMap<String,Entry> _entries = new TreeMap<String,Entry>();
	
	/**
	 * Creates a new directory
	 */
	public Directory(String name, Directory parent, User owner) {
		super(name, parent, owner);
		_entries.put("..", parent);
		_entries.put(".", this);
	}
	
	/**
	 * Creates a new directory
	 */
	public Directory(String name, Directory parent, User owner, boolean privacy) {
		super(name, parent, owner, privacy);
		_entries.put("..", parent);
		_entries.put(".", this);
	}
	
	/**
	 * Creates a new directory; used specifically for the root directory
	 */
	public Directory(String name, User owner) {
		super(name, owner);
		setParent(this);
		_entries.put("..", this);
		_entries.put(".", this);
	}
	
	/**
	 * @param name
	 *             the entry to check
	 *
	 * @return true, if the entry exists; false, otherwise      
	 */
	public boolean checkEntry(String name) {
		return _entries.containsKey(name);
	}
	
	/**
	 * @param ename
	 *             an entry's name
	 *
	 * @param user
	 *             the user requesting an operation
	 *
	 * @return true, if the the user has permission to access the entry; false, otherwise      
	 */
	public boolean checkOwner(String ename, User user) throws CoreEntryUnknownException {
		Entry entryToCheck = getEntry(ename);
		return (entryToCheck.getPrivacy() || user.isRoot() || user.equals(entryToCheck.getOwner()));
	}
	
   /**
	 * @param ename
	 *             an entry's name
	 *
	 * @return true, if the the entry is a file; false, otherwise      
	 */
	public boolean checkFile(String ename) throws CoreEntryUnknownException {
		return (getEntry(ename).isFile());
	}
	
	/**
	 * @param ename
	 *             an entry's name
	 *
	 * @return true, if the the entry is a directory; false, otherwise      
	 */
	public boolean checkDirectory(String ename) throws CoreEntryUnknownException {
		return (getEntry(ename).isDirectory());
	}
	
   /**
	 * @return an ArrayList with all the entries' names
	 */
   public ArrayList<String> getKeys() {
       return new ArrayList<String>(_entries.keySet());
   }
   
	/**
	 * @param name
	 *             the name of an entry
	 *
	 * @return the corresponding entry      
	 */
	public Entry getEntry(String name) throws CoreEntryUnknownException {
	   if(!checkEntry(name))
	      throw new CoreEntryUnknownException(name);
	   else
			return _entries.get(name);
	}
	
	/** 
	 * @return the directory's size
	 */
	@Override
	public int getSize() {
		return _entries.size()*8;
	}
	
	/** 
	 * Adds an entry to the directory
	 *
	 * @param entry
	 * 			   the entry to add
	 */ 
	public void addEntry(Entry entry) throws CoreEntryExistsException {
	   String ename = entry.getName();
	   if (checkEntry(ename))
	      throw new CoreEntryExistsException(ename);
		else
		   _entries.put(ename, entry);
	}

	/** 
	 * Removes an entry from the directory
	 *
	 * @param name
	 *             the name of an entry
	 *
	 */
	public void removeEntry(String name) throws CoreEntryUnknownException {
	   if (!checkEntry(name))
	      throw new CoreEntryUnknownException(name);
		else
		   _entries.remove(name);
	}
   
   /**
    * @return true, since this is a directory
	 */
	@Override
	public boolean isDirectory() {
		return true;
	}
	
   /**
	 * @param ename
	 *             the entry to get info from
	 *
	 * @return the entry's representation     
	 */
   public String printEntry(String ename) throws CoreEntryUnknownException {
     return getEntry(ename).getInfo() + " " + ename;
   }
   
	/**
    * Get the complete path of a directory
    *
	 * @return the corresponding path            
	 */
	public String getPath() {
	   if (getParent().getName().equals("/"))
	      return "/" + (getName().equals("/") ? "" : getName());
	   else
	      return getParent().getPath() +"/"+ getName();
	}
	
   /** 
	 * Adds content to a file
	 *
	 * @param fname
	 * 			   the file's
	 *
	 * @param content
	 * 			   the content to add
	 */
   public void addFileContent(String fname, String content) throws CoreEntryUnknownException, CoreIsNotFileException {
      if (!checkFile(fname))
         throw new CoreIsNotFileException(fname);
      else
         ((File) getEntry(fname)).addContent(content);
   }
   
	/**
	 * @param fname
	 *             the file to read from
	 *
	 * @return the file's contents     
	 */
   public String getFileContent(String fname) throws CoreEntryUnknownException, CoreIsNotFileException {
     if (!checkFile(fname))
         throw new CoreIsNotFileException(fname);
     else
         return ((File) getEntry(fname)).getContent();
   }
   	
   /** 
	 * Changes an entry's privacy
	 *
	 * @param ename
	 * 			   the entry's name 
	 *
	 * @param privacy
	 * 			   the new privacy
	 */
   public void setEntryPrivacy(String ename, boolean privacy) throws CoreEntryUnknownException {
     getEntry(ename).setPrivacy(privacy);
   }
   
   /** 
	 * Changes an entry's owner
	 *
	 * @param ename
	 * 			   the entry's name 
	 *
	 * @param owner
	 * 			   the new user
	 */
   public void setEntryOwner(String ename, User owner) throws CoreEntryUnknownException {
      getEntry(ename).setOwner(owner);
   }
		
	/**
	 * @return the representation of the directory
	 */
	@Override
	public String getInfo() {
	   return ( "d " + super.getInfo());
   }
   
}
