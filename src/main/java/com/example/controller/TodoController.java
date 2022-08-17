package com.example.controller;

import com.example.Repository.TodoRepository;
import com.example.exception.TodoNotFoundException;
import com.example.model.Todo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api")
@Validated
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/todo")
    public List<Todo> getAllTodos(){
        return this.todoRepository.findAll();
    }

    @PostMapping("/todo")
    public Todo addTodo(@RequestBody @Validated Todo todo){
        return this.todoRepository.save(todo);
    }


    @GetMapping("/todo/{id}")
    public Todo getTodo(@PathVariable("id") Long todoId) {
        return this.todoRepository.findById(todoId).orElseThrow(TodoNotFoundException::new);
    }

    @DeleteMapping("/todo/{id}")
    public void deleteTodo(@PathVariable("id") Long todoId) {
        this.todoRepository.deleteById(todoId);
    }

    @PutMapping("todo/{id}")
    public Todo updateRecord(@PathVariable("id") Long todoId, @RequestBody @Validated Todo newTodo) {
        return this.todoRepository.findById(todoId)
                .map(todo -> {
                    todo.setDescription(newTodo.getDescription());
                    todo.setUserName(newTodo.getUserName());
                    todo.setIsCompleted(newTodo.getIsCompleted());
                    todo.setDateOfCompletion(newTodo.getDateOfCompletion());

                    return this.todoRepository.save(todo);
                })
                .orElseGet(() -> {
                    newTodo.setId(todoId);
                    return this.todoRepository.save(newTodo);
                });
    }

    @PatchMapping(path = "/todo/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Todo> updateRecordFields(@PathVariable("id") Long todoId, @RequestBody JsonPatch patch) {
        try {
            Todo todo = this.todoRepository.findById(todoId).orElseThrow(TodoNotFoundException::new);
            Todo todoPatched = applyPatchToCustomer(patch, todo);
            this.todoRepository.save(todoPatched);

            return ResponseEntity.ok(todoPatched);
        } catch (TodoNotFoundException todoNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Todo applyPatchToCustomer(JsonPatch patch, Todo targetTodo) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetTodo, JsonNode.class));
        return objectMapper.treeToValue(patched, Todo.class);
    }
}
