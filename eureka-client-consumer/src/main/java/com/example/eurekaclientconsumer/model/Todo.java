package com.example.eurekaclientconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Todo {
    private int userId;
    private int id;
    @NonNull
    private String title;
    private boolean completed;
}
