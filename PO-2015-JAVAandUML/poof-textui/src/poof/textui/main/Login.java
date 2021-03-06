/** @version $Id: Login.java,v 1.14 2014/12/01 18:52:04 ist179027 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

import poof.system.Manager;
import poof.exception.*;

import poof.textui.UserUnknownException;

/**
 * §2.1.2.
 */
public class Login extends Command<Manager> {

	/**
	 * @param manager
	 */
	public Login(Manager manager) {
		super(MenuEntry.LOGIN, manager, new FileSystemPredicate(manager));
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
      String uname = IO.readString(Message.usernameRequest());
	   try {
	      _receiver.login(uname);
	   }
	   catch (CoreUserUnknownException e) {
	      throw new UserUnknownException(e.getName());
	   }
	}
}
