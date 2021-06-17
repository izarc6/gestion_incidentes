/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketsystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import ticketsystem.Exceptions.*;

/**
 *
 * @author izar
 */
public class Ticket implements Serializable {

    private int id;
    private LocalDateTime datetime;
    private int creator;
    private int technician = 0;    // 0 => No asignado
    private Priority priority;
    private Status status;
    private Scalability scalability;
    private ArrayList<Task> tasks;
    private final String DEFAULT_DESCR = "Ningúna descripción.";
    private String description = DEFAULT_DESCR;
    
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    
//    
//    /**
//     * Constructor de un ticket sin su descripción
//     *
//     * @param datetime Fecha y hora de creación
//     * @param creator ID del usuario creador del ticket
//     * @param priority Prioridad del ticket
//     * @param status Estado actual (debería ser START)
//     */
//    public Ticket(LocalDateTime datetime, int creator, Priority priority, Status status) {
//        this.id = generateTicketID();
//        this.datetime = datetime;
//        this.creator = creator;
//        this.priority = priority;
//        this.status = status;
//        this.tasks = new ArrayList<Task>();
//    }
//
//    /**
//     * Constructor de un ticket con su descripción
//     *
//     * @param datetime Fecha y hora de creación
//     * @param creator ID del usuario creador del ticket
//     * @param priority Prioridad del ticket
//     * @param status Estado actual (debería ser START)
//     * @param description Breve descripción del problema
//     */
//    public Ticket(LocalDateTime datetime, int creator, Priority priority, Status status, String description) {
//        this.id = generateTicketID();
//        this.datetime = datetime;
//        this.creator = creator;
//        this.priority = priority;
//        this.status = status;
//        this.tasks = new ArrayList<Task>();
//        this.description = description;
//    }
    
    // CONSTRUCTORES SQL
    public Ticket(String id, String dateTime, String creator, String technician, String priority, String status, String scalability, String description) {
        this.id = Integer.parseInt(id);
        this.datetime = LocalDateTime.parse(dateTime, formatter);
        this.creator = Integer.parseInt(creator);
        this.technician = technician==null ? 0 : Integer.parseInt(technician);
        this.priority = Integer.parseInt(priority) < 5 ? Priority.values()[Integer.parseInt(priority)] : Priority.LOW;
        this.status = Integer.parseInt(status) < 4 ? Status.values()[Integer.parseInt(status)] : Status.START;
        this.scalability = Integer.parseInt(scalability) < 3 ? Scalability.values()[Integer.parseInt(scalability)] : Scalability.BASIC;
        this.description = description;
    }
    
    public Ticket(String[] metaData) {
        this.id = Integer.parseInt(metaData[0]);
        this.datetime = LocalDateTime.parse(metaData[1], formatter);
        this.creator = Integer.parseInt(metaData[2]);
        this.technician = metaData[3]==null ? 0 : Integer.parseInt(metaData[3]);
        this.priority = Integer.parseInt(metaData[4]) < 5 ? Priority.values()[Integer.parseInt(metaData[4])] : Priority.LOW;
        this.status = Integer.parseInt(metaData[5]) < 4 ? Status.values()[Integer.parseInt(metaData[5])] : Status.START;
        this.scalability = Integer.parseInt(metaData[6]) < 3 ? Scalability.values()[Integer.parseInt(metaData[6])] : Scalability.BASIC;
        this.description = metaData[7];
    }
    
    public Ticket(int creator, int priority, int status, int scalability, String description) {
        this.creator = creator;
        this.priority = priority < 5 ? Priority.values()[priority] : Priority.LOW;
        this.status = status < 4 ? Status.values()[status] : Status.START;
        this.scalability = scalability < 3 ? Scalability.values()[scalability] : Scalability.BASIC;
        this.description = description;
    }
    
    public Ticket(int creator, int technician, int priority, int status, int scalability, String description) {
        this.creator = creator;
        this.technician = technician;
        this.priority = priority < 5 ? Priority.values()[priority] : Priority.LOW;
        this.status = status < 4 ? Status.values()[status] : Status.START;
        this.scalability = scalability < 3 ? Scalability.values()[scalability] : Scalability.BASIC;
        this.description = description;
    }

    /**
     * Permite asignar un técnico al ticket especificado
     *
     * @param tech Usuario a asignar
     * @throws ticketsystem.Exceptions.WrongUserTypeException
     */
    public void assignTechnician(User tech) throws WrongUserTypeException {
        // Se comprueba si el usuario es efectivamente un técnico
        if (tech.getUserType() == UserType.TECHNICIAN) {
            this.technician = tech.getId();
        } else {
            throw new WrongUserTypeException();
        }
    }

    /*
    public static int generateTicketID() {
        int[] allIDs = new TicketIO().readAllTicketIDs();
        int max = 0;
        for (int i : allIDs) {
            if (i > max) {
                max = i;
            }
        }
        return max + 1;
    }
    */

    public void addTask(Task t) {
        this.tasks.add(t);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getTechnician() {
        return technician;
    }

    public void setTechnician(int technician) {
        this.technician = technician;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Scalability getScalability() {
        return scalability;
    }

    public void setScalability(Scalability scalability) {
        this.scalability = scalability;
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public String getDescription() {
        return description;
    }
    
    public int getPriorityInt() {
        switch (this.priority.toString()) {
            case "LOW": return 0;
            case "MEDIUM": return 1;
            case "HIGH": return 2;
            case "SERIOUS": return 3;
            case "CRITICAL": return 4;            
        }
        return 0;
    }
    
    public int getStatusInt() {
        switch (this.status.toString()) {
            case "START": return 0;
            case "ASSIGNED": return 1;
            case "PROCESSING": return 2;
            case "FINALIZED": return 3;
        }
        return 0;
    }
    
    public int getScalabilityInt() {
        switch (this.scalability.toString()) {
            case "BASIC": return 0;
            case "TECHNICIAN": return 1;
            case "EXTERNAL": return 2;
        }
        return 0;
    }
    

    @Override
    public String toString() {
        String s = "***Información ticket***\n";

        s += "\tID:\t\t\t\t" + this.id + "\n";
        s += "\tFecha/hora de creación:\t\t" + formatter.format(datetime) + "\n";
        s += "\tCreado por usuario con ID:\t" + this.creator + "\n";
        s += technician == 0 ? ("\tTécnico no asignado\n") : ("\tID técnico asignado:\t" + this.technician + "\n");
        s += "\tPrioridad actual:\t\t" + this.priority.toString() + "\n";
        s += "\tEstado actual:\t\t\t" + this.status.toString() + "\n";
        s += "\tN. de tareas efectuadas:\t" + this.tasks.size() + "\n";
        s += "\tDescripción:\t\t\t" + this.description + "\n";
        
        /**
         * private int id; private LocalDateTime datetime; private int creator;
         * private int technician = 0; // -0=> No asignado private Priority
         * priority; private Status status; private ArrayList<Task> tasks;
         * private String description;
         */

        return s;
    }

}
