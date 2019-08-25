package com.example.libraryproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int pages;


    public Book() {
    }

    public Book(String isbn, String title, String author, String publisher, int pages) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pages = pages;
    }

    protected Book(Parcel in) {
        isbn = in.readString();
        title = in.readString();
        author = in.readString();
        publisher = in.readString();
        pages = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pages=" + pages +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(isbn);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(publisher);
        parcel.writeInt(pages);
    }
}
