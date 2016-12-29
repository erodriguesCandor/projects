/** @version $Id: ListAllUsers.java,v 1.6 2014/11/13 10:51:31 ist179027 Exp $ */
package poof.textui.user;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;
import java.io.IOException;

import poof.system.Manager;

/**
 * §2.3.2.
 */
public class ListAllUsers extends Command<Manager> {
	/**
	 * @param manager
	 */
	public ListAllUsers(Manager manager) {
		super(MenuEntry.LIST_USERS, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException  {
      IO.println(_receiver.listUsers());
	}
}
