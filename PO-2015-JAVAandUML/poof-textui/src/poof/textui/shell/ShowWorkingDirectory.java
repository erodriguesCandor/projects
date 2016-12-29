/** @version $Id: ShowWorkingDirectory.java,v 1.7 2014/11/13 10:51:31 ist179027 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

import poof.system.Manager;

/**
 * §2.2.7.
 */
public class ShowWorkingDirectory extends Command<Manager>{
	/**
	 * @param manager
	 */
	public ShowWorkingDirectory(Manager manager) {
		super(MenuEntry.PWD, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() {
      IO.println(_receiver.getPath());
	}

}
