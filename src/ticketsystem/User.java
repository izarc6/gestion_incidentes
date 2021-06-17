/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketsystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 *
 * @author izar
 */
public class User implements Serializable {
    
    private int id;
    private UserType userType;
    
    private String username;
    private String password;
    
    private String name;
    private String surname;
    private LocalDate birthDate;
    
    public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /*
    // Constructores
    // ID, tipo, user, pass
    public User(int id, UserType userType, String username, String password) {
        this.id = id;
        this.userType = userType;
        this.username = username;
        this.password = encodePassword(password);
    }
    
    // Tipo, user, pass
    public User(UserType userType, String username, String password) {
        this.id = generateID();
        this.userType = userType;
        this.username = username;
        this.password = encodePassword(password);
    }
    
    // ID, tipo, user, pass, nombre, apellidos
    public User(int id, UserType userType, String username, String password, String name, String surname) {
        this.id = id;
        this.userType = userType;
        this.username = username;
        this.password = encodePassword(password);
        this.name = name;
        this.surname = surname;
    }
    
    // Tipo, user, pass, nombre, apellidos
    public User(UserType userType, String username, String password, String name, String surname) {
        this.id = generateID();
        this.userType = userType;
        this.username = username;
        this.password = encodePassword(password);
        this.name = name;
        this.surname = surname;
    }
    */
    
    // CONSTRUCTORES SQL
    public User(int id, String username, String encPassword, String name, String surname, String birthDate) {
        
        this.id = id;
        this.username = username;
        this.password = encPassword;
        this.name = name;
        this.surname = surname;
        this.birthDate = LocalDate.parse(birthDate, DF);
        
    }
    
    public User(String id, String username, String encPassword, String name, String surname, String birthDate) {
        this.id = Integer.parseInt(id);
        this.username = username;
        this.password = encPassword;
        this.name = name;
        this.surname = surname;
        this.birthDate = LocalDate.parse(birthDate, DF);
    }
    
    public User(String[] metaData) {
        this.id = Integer.parseInt(metaData[0]);
        this.username = metaData[1];
        this.password = metaData[2];
        this.name = metaData[3];
        this.surname = metaData[4];
        this.birthDate = LocalDate.parse(metaData[5], DF);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthdate) {
        this.birthDate = birthdate;
    }
    
    public void setBirthDate(int year, int month, int day) {
        this.birthDate = LocalDate.of(year, month, day);
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "Detalles del usuario con ID " + this.id + "\n";
        s += "Nombre:\t\t\t" + this.name + "\n";
        s += "Apellido(s):\t\t" + this.surname + "\n";
        s += "Fecha de nacimiento:\t" + this.birthDate.toString() + "\n";
        s += "Username:\t\t" + this.username + "\n";
        s += "Tipo:\t\t\t" + this.userType.toString();
        
        return s;
    }

    /*
    
    // Genera un nuevo ID de manera secuencial
    public static int generateID() {
        int[] allIDs = new UserIO().readAllUsers();
        int max = 0;
        for (int i : allIDs) {
            if (i > max)
                max = i;
        }
        return max + 1;
    }
    
    public String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    
    public String decodePassword(String password) {
        return new String(Base64.getDecoder().decode(password));
    }
    
    
    
    PARA CREAR UN NUEVO USUARIO:
    INSERT INTO `User` (`userID`, `username`, `password`, `name`, `surname`, `birthDate`)
        VALUES (NULL, 'izar', MD5('password'), 'Izar', 'Castorina', '1997-12-01');
    
    */
    
    
    // MÉTODOS ANTIGUOS
    
    /*
    public String encodePassword(String pass) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            
            cipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
            byte[] encrypted = cipher.doFinal(pass.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char) b);
            }
            
            return sb.toString();
            
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            System.err.println("ERROR en el encriptado de la contraseña del usuario " + this.id);
            ex.printStackTrace();
        }
        return null;
    }
    
    public String decodePassword(String pass) {
        try {
            
            byte[] bb = new byte[pass.length()];
            for (int i = 0; i < pass.length(); i++) {
                bb[i] = (byte) pass.charAt(i);
            }
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, this.aesKey);
            String decrypted = new String(cipher.doFinal(bb));
            return decrypted;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException ex) {
            System.err.println("ERROR en decriptar la contraseña del usuario " + this.id);
            ex.printStackTrace();
        }
        return null;
    }
    */
    
}
