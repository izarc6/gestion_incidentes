/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketsystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ticketsystem.Exceptions.*;

/**
 *
 * @author izar
 */
public class Task {
    
    private int taskID;
    private LocalDateTime dateTime;
    private String description;
    private int ticketID;
    private int userID;
    
    // Constructores SQL
    
    // Para crear objetos locales
    public Task(String description, int ticketID, int userID) {
        this.taskID = 0;
        this.dateTime = null;
        this.description = description;
        this.ticketID = ticketID;
        this.userID = userID;
    }
    
    // Para los objetos leidos desde la BD
    public Task(String taskID, String dateTime, String description, String ticketID, String userID) {
        this.taskID = Integer.parseInt(taskID);
        this.dateTime = LocalDateTime.parse(dateTime, Ticket.formatter);
        this.description = description;
        this.ticketID = Integer.parseInt(ticketID);
        this.userID = Integer.parseInt(userID);
    }
    
    public Task(String[] taskMetaData) {
        this.taskID = Integer.parseInt(taskMetaData[0]);
        this.dateTime = LocalDateTime.parse(taskMetaData[1], Ticket.formatter);
        this.description = taskMetaData[2];
        this.ticketID = Integer.parseInt(taskMetaData[3]);
        this.userID = Integer.parseInt(taskMetaData[4]);
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public String getDateTimeString() {
        return dateTime.format(Ticket.formatter);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    
    
    
}