/* Project - POOF */
package poof.system;

import java.io.*;
import poof.exception.*;

/**
 * class Manager
 *    Serves as the interface between the file system and the text ui. Asks its file system to process the requests given by the text ui.
 */
public class Manager {
   /** the manager's current file system */
	private FileSystem _session = null;
	
   /** the manager's active user */
	private User _activeUser;  
	 
   /** the manager's current directory */
	private Directory _currentDirectory; 
	
	/** the manager's save status */
	private boolean _saveStatus = false;
	
	/**
	 * Creates a new manager
	 */
   public Manager() {
      // does not need to do anything
   }
	
	/**
	 * Creates a new manager from the file provided
	 *
	 * @param filename
	 *             the file with the initial data
	 */
   public Manager(String filename) throws IOException, ClassNotFoundException {
      try {
         Builder fsbuilder = new Builder();
         fsbuilder.readImportFile(filename);
         setFileSystem(fsbuilder.getFileSystem());
         login("root");
      }
      catch (CoreException e) {
         // never actually happens
         e.printStackTrace();
      }
	}
	
	/**
	 * @return true, if the manager has an initialized file system; false, otherwise            
	 */
   public boolean checkFileSystem() {
      return getFileSystem()!=null;
   }
   
	/**
	 * @return true if the current file system has a save file; false, otherwise
	 */
	public boolean checkSave() {
		return getSave()!=null;
	}
	
	/**
	 * @return true if any change was made; false, otherwise
	 */
	public boolean checkStatus() {
		return _saveStatus;
	}
	
	/**
	 * @return the file system
	 */
	public FileSystem getFileSystem() {
		return _session;
	}
	
   /**
	 * @return the current directory
	 */
	public Directory getCurrentDirectory() {
		return _currentDirectory;
	}
	
   /**
	 * @return the active user
	 */
	public User getActiveUser() {
		return _activeUser;
	}
	
	/**
	 * @return the manager's save status
	 */
	public boolean getSaveStatus() {
		return _saveStatus;
	}
	
	/**
	 * @return the current file system's save file
	 */
	public String getSave() {
		return getFileSystem().getSave();
	}
	 
   
   /**
    * Change the current file system
    *
	 * @param session
	 *             the new file system
	 */
	public void setFileSystem(FileSystem session) {
		_session=session;
	}
	
   /**
    * Change the current directory
    *
	 * @param dir
	 *             the new directory
	 */
	public void setCurrentDirectory(Directory dir) {
		_currentDirectory=dir;
	}
	
   /**
    * Change the active user
    *
	 * @param user
	 *             the new user
	 */
	public void setActiveUser(User user) {
		_activeUser = user;
	}
	
	/**
    * Change the manager's save status
    *
	 * @param status
	 *             the new save status
	 */
	public void setSaveStatus(boolean status) {
	   _saveStatus = status;
	}
	
	/**
    * Change the manager's save status
	 */
	public void changed() {
		_saveStatus = true;
	}
	
	/**
    * Create a new file system and set it as the current one
	 */
	public void newFileSystem() {
      try {
         changed();
	      setFileSystem(new FileSystem());
	      login("root");
	   }
	   catch (CoreUserUnknownException e) {
	      // never actually happens
	      e.printStackTrace();
	   }
	}
	
	/**
    * Sets the file system to the one read from a file
    *
	 * @param fsname
	 *             the name of the file to read from   
	 */
	public void open(String fsname) throws IOException, ClassNotFoundException {
	   try {
	      ObjectInputStream inSession = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fsname)));
	      setFileSystem((FileSystem) inSession.readObject());
	      setActiveUser((User) inSession.readObject());
	      login(getActiveUser().getUname());
	      inSession.close();
	      setSaveStatus(false);
	   }
	   catch (CoreUserUnknownException e) {
	      // never actually happens
	      e.printStackTrace();
	   }
	}
	
	/**
    * Saves the current state of the file system
    *
	 * @param fsname
	 *             the name of the file to write to  
	 */
	public void save(String fsname) throws IOException, ClassNotFoundException {
	   getFileSystem().setSave(fsname);
	   ObjectOutputStream outSession = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fsname)));
	   outSession.writeObject(getFileSystem());
	   outSession.writeObject(getActiveUser());
	   outSession.close();
	   setSaveStatus(false);
	}
   
   /**
    * Logs in with a different user
    *
	 * @param uname
	 *             the name of the new logged user   
	 */
	public void login(String uname) throws CoreUserUnknownException {
	   changed();
	   setActiveUser(getFileSystem().getUser(uname));
  	   setCurrentDirectory(getFileSystem().getUserHome(uname));
	}
	
	/**
	 * @return the current directory's entries, ordered by name            
	 */
	public String listEntries() {
	   return _session.listEntries(getCurrentDirectory());
	}
		
   /**
	 * @param ename
	 *             the name of the requested entry
	 *
	 * @return the entry's info          
	 */
	public String showEntryInfo(String ename) throws CoreEntryUnknownException {
		return _session.showEntryInfo(ename, getCurrentDirectory());
	}
	
   /**
    * Remove an entry from the file system
    *
	 * @param ename
	 *             the name of the requested entry         
	 */
	public void removeEntry(String ename) throws CoreEntryUnknownException, CoreAccessDeniedException, CoreIllegalRemovalException {
	   changed();
		_session.removeEntry(ename, getCurrentDirectory(), getActiveUser());
	}
	
	/**
    * Change the current directory 
    *
	 * @param dname
	 *             the name of the new directory         
	 */
	public void changeDirectory(String dname) throws CoreEntryUnknownException, CoreIsNotDirectoryException {
	   if (dname != ".")
	      setCurrentDirectory(getFileSystem().getDirectory(dname, getCurrentDirectory()));
	}
	
   /**
    * Add a file to the file system
    *
	 * @param fname
	 *             the name of the new file        
	 */
	public void newFile(String fname) throws CoreEntryExistsException, CoreAccessDeniedException {
	   changed();
		_session.newFile(fname, getCurrentDirectory(), getActiveUser(), false);
	}
	
   /**
    * Add a directory to the file system
    *
	 * @param dname
	 *             the name of the new directory       
	 */
	public void newDirectory(String dname) throws CoreEntryExistsException, CoreAccessDeniedException {
	   changed();
		_session.newDirectory(dname, getCurrentDirectory(), getActiveUser(), false);
	}
	
	/**
	 * @return the current directory's path            
	 */
	public String getPath(){
		return _session.getPath(getCurrentDirectory());
	}
	
   /**
	 * Add content to a file
	 *
	 * @param content
	 *             the string to write
	 *
	 * @param fname
	 *             the name of the file to write to           
	 */
	public void writeToFile(String content, String fname) throws CoreEntryUnknownException, CoreIsNotFileException, CoreAccessDeniedException {
	   changed();
		_session.writeToFile(content, fname, getCurrentDirectory(), getActiveUser());
	}
	
   /**
	 * @param fname
	 *             the name of the file to read from
	 *
	 * @return the file's content
	 */
	public String readFromFile(String fname) throws CoreEntryUnknownException, CoreIsNotFileException {
		return _session.readFromFile(fname, getCurrentDirectory());
	}
	
   /**
	 * Change an entry's privacy
	 *
	 * @param ename
	 *             the entry's name
	 *
	 * @param privacy
	 *             the privacy to set to the entry
	 */
	public void changePrivacy(String ename, boolean privacy) throws CoreEntryUnknownException, CoreAccessDeniedException { 
	   changed();
		_session.changePrivacy(ename, privacy, getCurrentDirectory(), getActiveUser());
	}
	
   /**
	 * Change an entry's owner
	 *
	 * @param ename
	 *             the entry's name
	 *
	 * @param uname
	 *             the new owner's username
	 */
	public void changeOwner(String ename, String uname) throws CoreEntryUnknownException, CoreAccessDeniedException, CoreUserUnknownException { 
	   changed();
		_session.changeOwner(ename, uname, getCurrentDirectory(), getActiveUser());
	}

   /**
    * Add a new user to the file system
    *
	 * @param uname
	 *             the new user's username
	 * @param name
	 *             the new user's name
	 */
   public void addUser(String uname, String name) throws CoreAccessDeniedException, CoreUserExistsException {
      changed();
      _session.addUser(uname, name, getActiveUser());
   }
   
   /**
	 * @return the file system's users, ordered by username            
	 */
	public String listUsers() { 
	   return _session.listUsers();
	}
   
}
