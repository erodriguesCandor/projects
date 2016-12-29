/** @version $Id: CreateUser.java,v 1.11 2014/12/01 18:52:04 ist179027 Exp $ */
package poof.textui.user;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;
import java.io.IOException;

import poof.system.Manager;
import poof.exception.*; 

import poof.textui.*;

/**
 * §2.3.1.
 */
public class CreateUser extends Command<Manager> {
	/**
	 * @param manager
	 */
	public CreateUser(Manager manager) {
		super(MenuEntry.CREATE_USER, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
      String uname = IO.readString(Message.usernameRequest());
      String name = IO.readString(Message.nameRequest());
      try {
         _receiver.addUser(uname, name);
	   }
	   catch (CoreUserExistsException e) {
	      throw new UserExistsException(e.getName());
	   }
      catch (CoreAccessDeniedException e) {
         throw new AccessDeniedException(e.getName());
      }
	}
}
