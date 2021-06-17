/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketsystem;

import bd.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JFrame;
import ui.*;

/**
 *
 * @author izar
 */
public class TicketSystem {
    
    static UserType usertype;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exceptions.UserDoesNotExistException, Exceptions.UserAlreadyExistsException, Exceptions.TicketDoesNotExistException, SQLException {
        
        /*
        JFrame test_frame = new Login();
        test_frame.setVisible(true);
        test_frame.setResizable(false);

        // DEBUGGING
        
        for (int i = 0; i < 10; i++) {
            Ticket t = new Ticket(LocalDateTime.now(), 5, Priority.MEDIUM, Status.START);
            new TicketIO().writeTicket(t);
        }
        
        int[] tickets = new TicketIO().readAllTicketIDs();
        
        for (int i = 0; i < tickets.length; i++) {
            System.out.println("DEBUG - " + new TicketIO().readTicket(tickets[i]).toString());
        }
        
        
        // SQL TESTS
        
        Query test = new Query();
        User u = test.getUser(1);
        System.out.println("DEBUG - UserType for user " + u.getId() + ": " + u.getUserType());
        System.out.println("*******");
        u = test.getUser(2);
        System.out.println("DEBUG - UserType for user " + u.getId() + ": " + u.getUserType());
        u = test.getUser(3);
        System.out.println("DEBUG - UserType for user " + u.getId() + ": " + u.getUserType());
        System.out.println("DEBUG - User 2 exists: " + test.userExists(2));
        System.out.println("DEBUG - User Test3 exists: " + test.userNameExists("Test3"));
        
        test.login("izar", "password");
        test.login("izar", "test");
        
        
        System.out.println("\n\n");
        Ticket t = test.readTicket(1);
        System.out.println("DEBUG - ticket " + t.getId() + " technician: " + t.getTechnician());
        System.out.println("DEBUG - ticket " + t.getId() + " dateTime: " + t.getDatetime().toString());
        System.out.println("\n");
        
        System.out.println("**************************************");
        System.out.println("DEBUG - Reading All Tickets");
        ArrayList<Ticket> all = test.readAllTickets();
        for (Ticket tick: all) {
            System.out.println("DEBUG - Ticket " + tick.getId() + ": " + tick.getDescription());
        }
        
        System.out.println("**************************************");
        System.out.println("DEBUG - Reading all Tasks");
        ArrayList<Task> allTasks = test.readAllTasks();
        for (Task tsk : allTasks) {
            System.out.println("DEBUG - Task " + tsk.getTaskID() + ": " + tsk.getDescription());
        }
        
        test.close();
        
        */
        
        
        JFrame test_frame = new Login();
        test_frame.setVisible(true);
        test_frame.setResizable(false);
        
    }

}
