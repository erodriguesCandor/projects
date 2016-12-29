/** @version $Id: MenuBuilder.java,v 1.5 2014/11/13 10:51:31 ist179027 Exp $ */
package poof.textui.user;

import ist.po.ui.Command;
import ist.po.ui.Menu;

import poof.system.Manager;

/**
 * Menu builder for search operations.
 */
public class MenuBuilder {

	/**
	 * @param manager
	 */
	public static void menuFor(Manager manager) {
		Menu menu = new Menu(MenuEntry.TITLE, new Command<?>[] {
				new CreateUser(manager),
				new ListAllUsers(manager),
				});
		menu.open();
	}

}
