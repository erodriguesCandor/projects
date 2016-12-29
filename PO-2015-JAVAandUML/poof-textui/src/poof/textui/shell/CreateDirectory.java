/** @version $Id: CreateDirectory.java,v 1.11 2014/12/01 18:52:04 ist179027 Exp $ */
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
 * §2.2.6.
 */
public class CreateDirectory extends Command<Manager> {
	/**
	 * @param manager
	 */
	public CreateDirectory(Manager manager) {
		super(MenuEntry.MKDIR, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
	   String dname = IO.readString(Message.directoryRequest());
	   try {
         _receiver.newDirectory(dname);
	   }
	   catch (CoreEntryExistsException e) {
	      throw new EntryExistsException(e.getName());
	   }
	   catch (CoreAccessDeniedException e) {
         throw new AccessDeniedException(e.getName());
      }
	}

}
