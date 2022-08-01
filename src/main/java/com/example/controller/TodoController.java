package com.example.controller;

import com.example.Repository.TodoRepository;
import com.example.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;


    @GetMapping("/todo")
    public List<Todo> getAllTodos(){
        return this.todoRepository.findAll();
    }

    @PostMapping("/todo")
    public Todo addTodo(@RequestBody Todo todo){
        return this.todoRepository.save(todo);
    }


}
