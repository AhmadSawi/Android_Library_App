package com.example.libraryproject;

public class User {

    private String Name;
    private String Gender;
    private String Email;
    private String Password;
    private int Age;
    private int numberOfReservedBooks;
    private boolean blocked;

    public User() {
    }

    public User(String Name, String Gender, String Email, String Password, int Age) {
        this.Name = Name;
        this.Gender = Gender;
        this.Email = Email;
        this.Password = Password;
        this.Age = Age;
        this.numberOfReservedBooks = 0;
        this.blocked = false;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getNumberOfReservedBooks() { return numberOfReservedBooks; }

    public void setNumberOfReservedBooks(int numberOfReservedBooks) { this.numberOfReservedBooks = numberOfReservedBooks; }

    public boolean isBlocked() { return blocked; }

    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    @Override
    public String toString() {
        return "User{" +
                ", Name='" + Name + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", Age=" + Age +
                ", numberOfReservedBooks=" + numberOfReservedBooks +
                ", blocked=" + blocked +
                '}';
    }
}