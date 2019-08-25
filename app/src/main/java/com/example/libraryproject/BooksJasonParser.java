package com.example.libraryproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BooksJasonParser {

    public static List<Book> getObjectFromJason(String jason) {

        List<Book> books;

        try {
            books = new ArrayList<>();
            JSONObject jsonArrayObject = new JSONObject(jason);
            JSONArray jsonArray = (JSONArray) jsonArrayObject.getJSONArray("books");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);
                Book book = new Book();
                book.setIsbn(jsonObject.getString("isbn"));
                book.setTitle(jsonObject.getString("title"));
                book.setAuthor(jsonObject.getString("author"));
                book.setPublisher(jsonObject.getString("publisher"));
                book.setPages(jsonObject.getInt("pages"));
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return books;
    }

}
