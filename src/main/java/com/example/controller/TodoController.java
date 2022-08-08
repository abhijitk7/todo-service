package com.example.controller;

import com.example.Repository.TodoRepository;
import com.example.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @DeleteMapping("/todo/{id}")
    public List<Todo> deleteTodo(@PathVariable Long id){
        Optional<Todo> todo=this.todoRepository.findById(id);
        todo.ifPresent(value -> this.todoRepository.delete(value));
        // @Bala TODO: 08/08/22  1. If invalid id is sent then API should respond with correct status 2. Exception is not handled
        return this.todoRepository.findAll();
    }


}
