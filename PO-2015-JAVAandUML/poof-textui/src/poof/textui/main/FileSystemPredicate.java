/* Project - POOF */
package poof.textui.main;

import ist.po.ui.ValidityPredicate;
import poof.system.Manager;

/**
 * class FileSystemPredicate
 *    Checks if a there is an active file system in the manager.
 */
public class FileSystemPredicate extends ValidityPredicate<Manager>{
	public FileSystemPredicate(Manager manager) {
		super(manager);
	}
	
	public boolean isValid(){
		return _receiver.checkFileSystem();
	}
}
