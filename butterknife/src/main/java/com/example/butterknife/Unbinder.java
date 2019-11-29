package com.example.butterknife;

public interface Unbinder {
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {

        }
    };
}
