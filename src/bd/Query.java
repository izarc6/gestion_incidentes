/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import static bd.Parameters.*;
import java.sql.*;
import java.util.ArrayList;
import ticketsystem.*;

/**
 *
 * @author Izar
 */
public class Query {

    // TODO: Implementar servidor con autolanzador en la misma app?
    private Connection conn = null;
    private Statement stmt = null;

    /**
     * Permite establecer una conexión a la BD
     */
    public Query() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("ERROR - Query - jdbc.Driver class not found!");
            ex.printStackTrace();
        }

        try {

            System.out.println(ANSI_YELLOW + "Connecting to " + DB_URL + "...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(ANSI_GREEN + "Connected. Creating statement..." + ANSI_RESET);
            stmt = conn.createStatement();
            System.out.println("Statement created.");
        } catch (SQLException ex) {
            System.err.println("ERROR - Query - SQLException");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Cierra el statement y la conexión a la BD
     */
    public void close() {
        boolean closed = true;
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.err.println("ERROR - Query - SQL Exception while closing Statement and/or connection");
            System.err.println(ex.getMessage());
            closed = false;
        }

        // Si ha ido todo bien, visualiza mensaje por consola
        if (closed) {
            System.out.println(ANSI_GREEN + "Connection to " + DB_URL + " closed successfully." + ANSI_RESET);
        }
    }

    // Métodos para la lectura de datos de usuarios
    public User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM User WHERE userID = '" + id + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.first() == false) {
            System.out.println("\tDEBUG - Query - User w/ ID " + id + " does not exist!");
            return null;
        }

        User u;
        String[] userMetaData = new String[6];
        int cols = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= cols; i++) {
            //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
            userMetaData[i - 1] = rs.getString(i);
        }
        //System.out.println("\tDEBUG - Query - getUser - Creating user...");
        u = new User(userMetaData);
        u.setUserType(checkUserType(u.getId()));

        return u;
    }

    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM User WHERE username LIKE '" + username + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.first() == false) {
            System.out.println("\tDEBUG - Query - User w/ username " + username + " does not exist!");
            return null;
        }

        User u;
        String[] userMetaData = new String[6];
        int cols = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= cols; i++) {
            //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
            userMetaData[i - 1] = rs.getString(i);
        }

        u = new User(userMetaData);
        u.setUserType(checkUserType(u.getId()));

        return u;
    }

    public UserType checkUserType(int id) throws SQLException {

        // Mira si hay correspondencias de IDs en las tablas
        String sql = "SELECT COUNT(User.userID) FROM User, Admin WHERE userID = Admin.adminID AND userID = " + id;
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            if (rs.getInt(1) == 1) {
                return UserType.ADMIN;
            }
        }

        sql = "SELECT COUNT(User.userID) FROM User, Technician WHERE userID = Technician.techID AND userID = " + id;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            if (rs.getInt(1) == 1) {
                return UserType.TECHNICIAN;
            }
        }

        return UserType.USER;
    }

    public boolean userExists(int id) throws SQLException {
        String sql = "SELECT * FROM User WHERE userID = '" + id + "'";
        ResultSet rs = stmt.executeQuery(sql);
        // Si rs.first es false, el usuario no existe, si es true, entonces sí
        return rs.first();
    }

    public boolean userNameExists(String usern) throws SQLException {
        String sql = "SELECT * FROM User WHERE username LIKE '" + usern + "'";
        ResultSet rs = stmt.executeQuery(sql);
        // Si rs.first es false, el usuario no existe, si es true, entonces sí
        return rs.first();
    }

    public boolean login(String usern, String pass) throws SQLException {
        // Mira si hay correspondencias de IDs en las tablas
        String sql = "SELECT COUNT(User.userID) FROM User WHERE USER.username LIKE '" + usern + "' AND USER.password LIKE MD5('" + pass + "');";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            if (rs.getInt(1) == 1) {
                System.out.println("\tDEBUG - login - Password correcta!");
                return true;
            } else {
                System.out.println("\tDEBUG - login - Password incorrecta!");
                return false;
            }
        }
        return false;
    }

    public void writeUser(int id, UserType ut, String username, String password, String name, String surname, String birthDate) throws SQLException {

        String sql = "INSERT INTO User (userID, username, password, name, surname, birthDate) "
                + "VALUES('" + id + "', '" + username + "', MD5('" + password + "'), '" + name + "', '" + surname + "', '" + birthDate + "')";
        stmt.executeUpdate(sql);
        System.out.println("\tDEBUG - Query - writeUser - Saved user " + id);

        User u = this.getUser(username);

        switch (ut.toString()) {
            case "ADMIN":
                sql = "INSERT INTO Admin (adminID) VALUES ('" + u.getId() + "')";
                stmt.executeUpdate(sql);
                System.out.println("\tDEBUG - Query - writeUser - Set user " + u.getId() + " as " + ut + ".");
                break;
            case "TECHNICIAN":
                sql = "INSERT INTO Technician (techID) VALUES ('" + u.getId() + "')";
                stmt.executeUpdate(sql);
                System.out.println("\tDEBUG - Query - writeUser - Set user " + u.getId() + " as " + ut + ".");
                break;
        }

    }

    public ArrayList<User> readAllTechnicians() throws SQLException {
        String sql = "SELECT DISTINCT * FROM User, Technician WHERE User.userID = Technician.techID";

        ArrayList<User> technicians = new ArrayList<User>();
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] userMetaData = new String[6];
            for (int i = 1; i <= cols - 1; i++) {   // Cols - 1 porque ignoramos el TechID al final
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                userMetaData[i - 1] = rs.getString(i);
            }
            technicians.add(new User(userMetaData));
        }
        return technicians;
    }

    public ArrayList<User> readAllUsers() throws SQLException {
        String sql = "SELECT * FROM User";

        ArrayList<User> users = new ArrayList<User>();
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] userMetaData = new String[6];
            for (int i = 1; i <= cols; i++) {   // Cols - 1 porque ignoramos el TechID al final
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                userMetaData[i - 1] = rs.getString(i);
            }
            users.add(new User(userMetaData));
        }
        return users;
    }

    // Métodos para la lectura/escritura de datos de tickets
    public void writeTicket(Ticket t) throws SQLException {
        if (ticketExists(t.getId())) {
            // Update
            String sql = "UPDATE Ticket SET id = '" + t.getId() + "', dateTime = '" + t.getDatetime().toString() + "', creatorID = '"
                    + t.getCreator() + "', technicianID = '" + t.getTechnician() + "', priority = '" + t.getPriorityInt()
                    + "', status = '" + t.getStatusInt() + "', scalability = '" + t.getScalabilityInt()
                    + "', description = '" + t.getDescription() + "' WHERE Ticket.id = " + t.getId();
            stmt.executeUpdate(sql);
            System.out.println("\tDEBUG - Query - WriteTicket - Ticket " + t.getId() + " has been updated in DB");
        } else {
            // Insert
            String sql = "INSERT INTO Ticket (id, dateTime, creatorID, technicianID, priority, status, scalability, description) "
                    + "VALUES('" + t.getId() + "', '" + t.getDatetime().toString() + "', '" + t.getCreator() + "'), '" + t.getTechnician()
                    + "', '" + t.getPriorityInt() + "', '" + t.getStatusInt() + "', '" + t.getScalabilityInt() + "', '" + t.getDescription() + "')";
            stmt.executeUpdate(sql);
            System.out.println("\tDEBUG - Query - WriteTicket - Ticket " + t.getId() + " written to DB");
        }
    }

    // Autofill para ID y timestamp
    public void writeTicketAutoFill(Ticket t) throws SQLException {
        // Insert
        String sql = "INSERT INTO Ticket (id, dateTime, creatorID, technicianID, priority, status, scalability, description) "
                + "VALUES(NULL, CURRENT_TIMESTAMP, '" + t.getCreator() + "', '" + t.getTechnician()
                + "', '" + t.getPriorityInt() + "', '" + t.getStatusInt() + "', '" + t.getScalabilityInt() + "', '" + t.getDescription() + "')";
        stmt.executeUpdate(sql);
        System.out.println("\tDEBUG - Query - WriteTicket - Ticket with auto-increment ID written to DB");

    }

    public boolean ticketExists(int id) throws SQLException {
        String sql = "SELECT * FROM Ticket WHERE Ticket.id = '" + id + "'";
        ResultSet rs = stmt.executeQuery(sql);
        // Si rs.first es false, el ticket no existe, si es true, entonces sí
        return rs.first();
    }

    public Ticket readTicket(int id) throws SQLException {
        String sql = "SELECT * FROM Ticket WHERE Ticket.id = '" + id + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.first() == false) {
            System.out.println("\tDEBUG - Query - Ticket w/ ID " + id + " does not exist!");
            return null;
        }

        Ticket t;
        String[] ticketMetaData = new String[8];
        int cols = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= cols; i++) {
            //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
            ticketMetaData[i - 1] = rs.getString(i);
        }
        t = new Ticket(ticketMetaData);

        return t;
    }

    public ArrayList<Ticket> readAllTickets() throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readUserTickets(int id) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE creatorID = '" + id + "'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTicketsByUserName(String name) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT id, dateTime, creatorID, technicianID, priority, status, scalability, description FROM Ticket JOIN User ON Ticket.creatorID = User.userID AND (User.name LIKE '%" + name + "%' OR User.surname LIKE '%" + name + "%')";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    // Comprueba si el técnico tiene el ticket asignado
    public boolean TicketIsAssigned(int techID, Ticket t) {
        return t.getTechnician() == techID;
    }

    public ArrayList<Ticket> readTicketsByDateTime(String datetime) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE dateTime LIKE '%" + datetime + "%'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTechTickets(int techID) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE technicianID = '" + techID + "'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTicketsByTechName(String name) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT id, dateTime, creatorID, technicianID, priority, status, scalability, description FROM Ticket JOIN User ON Ticket.technicianID = User.userID AND (User.name LIKE '%" + name + "%' OR User.surname LIKE '%" + name + "%')";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTicketsByPriority(int priority) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE priority = '" + priority + "'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTicketsByStatus(int status) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE status = '" + status + "'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTicketsByScalability(int scalability) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE scalability = '" + scalability + "'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public ArrayList<Ticket> readTicketsByDescription(String description) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        String sql = "SELECT * FROM Ticket WHERE description LIKE '%" + description + "%'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] ticketMetaData = new String[8];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                ticketMetaData[i - 1] = rs.getString(i);
            }
            tickets.add(new Ticket(ticketMetaData));
        }
        return tickets;
    }

    public void addTask(int ticketID, Task t) throws SQLException {
        String sql = "INSERT INTO Task (taskID, dateTime, description, ticketID, userID) VALUES (NULL, CURRENT_TIMESTAMP, '" + t.getDescription()
                + "', '" + ticketID + "', '" + t.getUserID() + "')";
        stmt.executeUpdate(sql);
        System.out.println("\tDEBUG - Query - Added Task for ticket " + ticketID);
    }

    public Task readTask(int taskID) throws SQLException {
        String sql = "SELECT * FROM Task WHERE Task.taskID = '" + taskID + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.first() == false) {
            System.out.println("\tDEBUG - Query - Task w/ ID " + taskID + " does not exist!");
            return null;
        }

        Task t;
        String[] taskMetaData = new String[5];
        int cols = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= cols; i++) {
            //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
            taskMetaData[i - 1] = rs.getString(i);
        }
        t = new Task(taskMetaData);

        return t;
    }

    public ArrayList<Task> readTasksByID(int ticketID) throws SQLException {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM Task WHERE ticketID = '" + ticketID + "'";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] taskMetaData = new String[5];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                taskMetaData[i - 1] = rs.getString(i);
            }
            tasks.add(new Task(taskMetaData));
        }
        return tasks;
    }

    public ArrayList<Task> readAllTasks() throws SQLException {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM Task";
        ResultSet rs = stmt.executeQuery(sql);

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            String[] taskMetaData = new String[5];
            for (int i = 1; i <= cols; i++) {
                //System.out.println("\tDEBUG - " + rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
                taskMetaData[i - 1] = rs.getString(i);
            }
            tasks.add(new Task(taskMetaData));
        }
        return tasks;
    }

    public void UpdateStatus(int ticketID, int status) throws SQLException {
        if (ticketExists(ticketID)) {
            // Update
            String sql = "UPDATE Ticket SET status = '" + status + "' WHERE Ticket.id = " + ticketID;
            stmt.executeUpdate(sql);
        }
    }

    public void UpdatePriority(int ticketID, int priority) throws SQLException {
        if (ticketExists(ticketID)) {
            // Update
            String sql = "UPDATE Ticket SET priority = '" + priority + "' WHERE Ticket.id = " + ticketID;
            stmt.executeUpdate(sql);
        }
    }

    public void UpdateTechnician(int ticketID, int techID) throws SQLException {
        if (ticketExists(ticketID)) {
            // Update
            String sql = "UPDATE Ticket SET technicianID = '" + techID + "' WHERE Ticket.id = " + ticketID;
            stmt.executeUpdate(sql);
        }
    }

    public void removeTechnician(int ticketID) throws SQLException {
        if (ticketExists(ticketID)) {
            // Set technician ID to null
            String sql = "UPDATE Ticket SET technicianID = NULL WHERE Ticket.id = " + ticketID;
            stmt.executeUpdate(sql);
        }
    }

    public void UpdateScalability(int ticketID, int scalability) throws SQLException {
        if (ticketExists(ticketID)) {
            // Update
            String sql = "UPDATE Ticket SET scalability = '" + scalability + "' WHERE Ticket.id = " + ticketID;
            stmt.executeUpdate(sql);
        }
    }

    public void UpdateDescription(int ticketID, String description) throws SQLException {
        if (ticketExists(ticketID)) {
            // Update
            String sql = "UPDATE Ticket SET description = '" + description + "' WHERE Ticket.id = " + ticketID;
            stmt.executeUpdate(sql);
        }
    }

    public void removeTask(int taskID) throws SQLException {
        String sql = "DELETE FROM Task WHERE Task.taskID = " + taskID;
        stmt.executeUpdate(sql);
        System.out.println("\tDEBUG - Query - Task w/ ID " + taskID + " removed.");
    }

    public void removeTicket(int ticketID) throws SQLException {
        String sql = "DELETE FROM Ticket WHERE Ticket.id = " + ticketID;
        stmt.executeUpdate(sql);
        System.out.println("\tDEBUG - Query - Task w/ ID " + ticketID + " removed.");
    }

}
