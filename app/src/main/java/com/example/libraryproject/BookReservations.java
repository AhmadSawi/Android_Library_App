package com.example.libraryproject;

import java.sql.Date;

public class BookReservations {

    private String isbn;
    private String username;
    private String reservationDate;

    public BookReservations() {
    }

    public BookReservations(String isbn, String username, String reservationDate) {
        this.isbn = isbn;
        this.username = username;
        this.reservationDate = reservationDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }
}
