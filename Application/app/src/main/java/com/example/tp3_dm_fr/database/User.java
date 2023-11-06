package com.example.tp3_dm_fr.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable( tableName = "T_Users" )
public class User {

    @DatabaseField( columnName = "idUser", generatedId = true )
    private int idUser;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String email;

    @DatabaseField
    private String password;

    @DatabaseField
    private String country;

    public User() {}

    public User(String firstName, String lastName, String email, String password, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "(" + firstName + " " + lastName + ")";
    }
}
