package org.example.service;


import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void todoAdd() {
        when(this.todoRepository.save(any(TodoModel.class))).then(AdditionalAnswers.returnsFirstArg());

        TodoRequest request = new TodoRequest();
        request.setTitle("테스트타이틀");

        TodoModel entity = this.todoService.todoAdd(request);
        assertEquals(request.getTitle(), entity.getTitle());
    }

    @Test
    void searchByID() {
        TodoModel entity = new TodoModel();
        entity.setId(123L);
        entity.setTitle("따이틀");
        entity.setOrder(0L);
        entity.setCompleted(false);
        Optional<TodoModel> optional = Optional.of(entity);

        given(this.todoRepository.findById(anyLong())).willReturn(optional);
        TodoModel actual = this.todoService.searchByID(123L);
        TodoModel expected = optional.get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());
        assertEquals(expected.getTitle(), actual.getTitle());

    }


    @Test
    public void searchByIdFailed(){
        given(this.todoRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, ()->{this.todoService.searchByID(17L);
        });
    }

}