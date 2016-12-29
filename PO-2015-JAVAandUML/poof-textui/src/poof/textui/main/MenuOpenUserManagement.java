/** @version $Id: MenuOpenUserManagement.java,v 1.6 2014/11/13 10:51:30 ist179027 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

import poof.system.Manager;

/**
 * Open user management menu.
 */
public class MenuOpenUserManagement extends Command<Manager> {

	/**
	 * @param manager
	 */
	public MenuOpenUserManagement(Manager manager) {
		super(MenuEntry.MENU_USER_MGT, manager, new FileSystemPredicate(manager));
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() {
		poof.textui.user.MenuBuilder.menuFor(_receiver);
	}

}
