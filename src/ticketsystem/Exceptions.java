/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketsystem;

/**
 *
 * @author izar
 */
public class Exceptions {
    public static class PrivilegeException extends Exception {
        public PrivilegeException(User u) {
            super("ERROR - El usuario con ID " + u.getId() + " no posee los privilegios necesarios para realizar esta operación.");
        }
    }
    
    public static class WrongUserTypeException extends Exception {
        public WrongUserTypeException() {
            super("ERROR - El tipo de usuario no es correcto!");
        }
    }
    
    public static class UserDoesNotExistException extends Exception {
        public UserDoesNotExistException(int id) {
            super("ERROR - El usuario con id " + id + " no existe!");
        }
    }
    
    public static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(int id) {
            super("ERROR - El usuario con id " + id + " ya existe!");
        }
    }
    
    public static class TicketDoesNotExistException extends Exception {
        public TicketDoesNotExistException(int id) {
            super("ERROR - El ticket con id " + id + " no existe!");
        }
    }
    
}
