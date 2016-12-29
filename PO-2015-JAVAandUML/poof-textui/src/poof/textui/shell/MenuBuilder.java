/** @version $Id: MenuBuilder.java,v 1.4 2014/11/13 10:51:31 ist179027 Exp $ */
package poof.textui.shell;

import ist.po.ui.Command;
import ist.po.ui.Menu;

import poof.system.Manager;

/**
 * Menu builder for shell operations.
 */
public class MenuBuilder {

	/**
	 * @param manager
	 */
	public static void menuFor(Manager manager) {
		Menu menu = new Menu(MenuEntry.TITLE, new Command<?>[] {
				new ListAllEntries(manager),
				new ListEntry(manager),
				new RemoveEntry(manager),
				new ChangeWorkingDirectory(manager),
				new CreateFile(manager),
				new CreateDirectory(manager),
				new ShowWorkingDirectory(manager),
				new AppendDataToFile(manager),
				new ShowFileData(manager),
				new ChangeEntryPermissions(manager),
				new ChangeOwner(manager),
				});
		menu.open();
	}

}
