/** @version $Id: ChangeWorkingDirectory.java,v 1.10 2014/12/01 18:52:04 ist179027 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

import poof.system.Manager;
import poof.exception.*;

import poof.textui.*;

/**
 * §2.2.4.
 */
public class ChangeWorkingDirectory extends Command<Manager>{
	/**
	 * @param manager
	 */
	public ChangeWorkingDirectory(Manager manager) {
		super(MenuEntry.CD, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
      String dname = IO.readString(Message.directoryRequest());
      try {
         _receiver.changeDirectory(dname);
      }
      catch (CoreEntryUnknownException e) {
         throw new EntryUnknownException(e.getName());
      }
      catch (CoreIsNotDirectoryException e) {
         throw new IsNotDirectoryException(e.getName());
      }
	}

}
