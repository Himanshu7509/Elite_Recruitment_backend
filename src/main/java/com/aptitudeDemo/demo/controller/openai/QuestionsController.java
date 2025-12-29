package com.aptitudeDemo.demo.controller.openai;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.QuestionsRequest;
import com.aptitudeDemo.demo.service.QuestionsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@CrossOrigin
public class QuestionsController {

    private final QuestionsService service;

    // CREATE
    @PostMapping
    public ResponseEntity<QuestionsRequest> create(@RequestBody QuestionsRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<QuestionsRequest>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionsRequest> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // READ BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<List<QuestionsRequest>> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<QuestionsRequest> update(
            @PathVariable String id,
            @RequestBody QuestionsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
