package com.oodwj_assignment.helpers;

public class Response<Type> {
    private final boolean success;
    private final String message;
    private final Type data; // You can use this to return additional data if needed.

    private Response(boolean success, String message, Type data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Type getData() {
        return data;
    }

    public static <Type> Response<Type> success(String message) {
        return new Response<>(true, message, null);
    }

    public static <Type> Response<Type> success(String message, Type data) {
        return new Response<>(true, message, data);
    }

    public static <Type> Response<Type> failure(String message) {
        return new Response<>(false, message, null);
    }

    public static <Type> Response<Type> failure(String message, Type data) {
        return new Response<>(false, message, data);
    }
}
