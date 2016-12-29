/** @version $Id: ListAllEntries.java,v 1.7 2014/11/14 01:04:01 ist179027 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

import poof.system.Manager;
import poof.exception.*;

/**
 * §2.2.1.
 */
public class ListAllEntries extends Command<Manager>{
	/**
	 * @param manager
	 */
	public ListAllEntries(Manager manager) {
		super(MenuEntry.LS, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
      IO.println(_receiver.listEntries());
	}

}
