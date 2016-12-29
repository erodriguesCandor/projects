/** @version $Id: MenuBuilder.java,v 1.5 2014/11/13 10:51:30 ist179027 Exp $ */
package poof.textui.main;

import ist.po.ui.Command;
import ist.po.ui.Menu;

import poof.system.Manager;

/**
 * Menu builder.
 */
public abstract class MenuBuilder {

	/**
	 * @param manager
	 */
	public static void menuFor(Manager manager) {
		Menu menu = new Menu(MenuEntry.TITLE, new Command<?>[] {
				new New(manager),
				new Open(manager),
				new Save(manager),
				new Login(manager),
				new MenuOpenShell(manager),
				new MenuOpenUserManagement(manager)
		});
		menu.open();
	}

}
