/** @version $Id: ChangeOwner.java,v 1.11 2014/12/01 18:52:04 ist179027 Exp $ */
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
 * §2.2.11.
 */
public class ChangeOwner extends Command<Manager>{
	/**
	 * @param manager
	 */
	public ChangeOwner(Manager manager) {
		super(MenuEntry.CHOWN, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
      String ename = IO.readString(Message.nameRequest());
      String uname = IO.readString(Message.usernameRequest());
      try{
         _receiver.changeOwner(ename,uname);
      }
	   catch (CoreEntryUnknownException e) {
	      throw new EntryUnknownException(e.getName());
	   }
	   catch (CoreUserUnknownException e) {
	      throw new UserUnknownException(e.getName());
	   }
      catch (CoreAccessDeniedException e) {
         throw new AccessDeniedException(e.getName());
      }
	}

}
