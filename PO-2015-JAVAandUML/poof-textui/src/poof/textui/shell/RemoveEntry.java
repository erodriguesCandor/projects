/** @version $Id: RemoveEntry.java,v 1.10 2014/12/01 18:52:04 ist179027 Exp $ */
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
 * §2.2.3.
 */
public class RemoveEntry extends Command<Manager>{
	/**
	 * @param manager
	 */
	public RemoveEntry(Manager manager) {
		super(MenuEntry.RM, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
	   String ename = IO.readString(Message.nameRequest());
	   try {
         _receiver.removeEntry(ename);
	   }
	   catch (CoreEntryUnknownException e) {
	      throw new EntryUnknownException(e.getName());
	   }
	   catch (CoreIllegalRemovalException e) {
	      throw new IllegalRemovalException();
	   }
      catch (CoreAccessDeniedException e) {
         throw new AccessDeniedException(e.getName());
      }
	}
}
