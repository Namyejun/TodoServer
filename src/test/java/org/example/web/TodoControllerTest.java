package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    TodoService todoService;

    private TodoEntity expected;

    @BeforeEach
    void setup(){
        this.expected = new TodoEntity();
        this.expected.setId(123L);
        this.expected.setTitle("title");
        this.expected.setOrder(0L);
        this.expected.setCompleted(false);
    }

    @Test
    void create() throws Exception{
        when(this.todoService.add(any(TodoRequest.class))).then((i) -> {
            TodoRequest request = i.getArgument(0, TodoRequest.class);
            return new TodoEntity(this.expected.getId(), request.getTitle(), this.expected.getOrder(), this.expected.getCompleted()); // title만 리퀘스트로 받은값을 반환하고 나머지는 expected
        });

        TodoRequest request = new TodoRequest();
        request.setTitle("Any Title");

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);
        this.mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())                 // 상태값이 Ok가 내려오면
                .andExpect(jsonPath("$.title").value("Any Title"));
    }

    @Test
    void readOne() {
    }
}