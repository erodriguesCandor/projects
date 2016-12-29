/** @version $Id: MenuOpenShell.java,v 1.7 2014/11/13 22:09:38 ist179027 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;
import poof.system.Manager;

/**
 * Open shell menu.
 */
public class MenuOpenShell extends Command<Manager> {

	/**
	 * @param manager
	 */
	public MenuOpenShell(Manager manager) {
		super(MenuEntry.MENU_SHELL, manager, new FileSystemPredicate(manager));
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() {
		poof.textui.shell.MenuBuilder.menuFor(_receiver);
	}

}
