package com.aptitudeDemo.demo.controller.openai;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.QuestionsRequest;
import com.aptitudeDemo.demo.model.OpenAI.Questions;
import com.aptitudeDemo.demo.service.QuestionsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/questions")
public class QuestionsController {


    private QuestionsService service;

    public QuestionsController(QuestionsService service) {
        this.service = service;
    }
    
    @PostMapping("/{studentFormId}")
    public ResponseEntity<?> createQuestions(
            @PathVariable String studentFormId,
            @RequestBody QuestionsRequest request
    ) {
        service.create(studentFormId, request);
        return ResponseEntity.ok("Questions saved successfully");
    }
    
    @GetMapping("/{studentFormId}")
    public ResponseEntity<?> getQuestionsByStudentFormId(@PathVariable String studentFormId) {
        return ResponseEntity.ok(service.getByStudentFormId(studentFormId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Questions>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    /*
    @PostMapping("/submit")
    public ResponseEntity<Questions> create(
            @RequestBody QuestionsRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Questions>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Questions> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Questions>> getByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Questions> update(
            @PathVariable String id,
            @RequestBody QuestionsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }
    */
}