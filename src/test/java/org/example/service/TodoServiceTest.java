package org.example.service;

import org.example.model.TodoEntity;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


// @ExtendWith(MockitoExtension.class): 테스트 클래스가 Mockito를 사용함을 의미합니다.
// @Mock: 실제 구현된 객체 대신에 Mock 객체를 사용하게 될 클래스를 의미합니다. 목의 객체(Mock)는 실제 구현체가 없고, 껍데기(인터페이스, 메서드, 필드)를 참조할 수 있는 객체입니다.
//        테스트 런타임 시 해당 객체 대신 Mock 객체가 주입되어 Unit Test가 처리됩니다.
// @InjectMocks: Mock 객체가 주입된 클래스를 사용하게 될 클래스를 의미합니다.
//        테스트 런타임 시 클래스 내부에 선언된 멤버 변수들 중에서 @Mock으로 등록된 클래스의 변수에 실제 객체 대신 Mock 객체가 주입되어 Unit Test가 처리됩니다.

// Mock 사용 이유 : 외부 시스템에 의존하지 않고 자체 테스트를 시행할 수 있어야하기 때문
// 네트워크나 데이터베이스가 안된다고 해서 단위 테스트도 함께 실행 못하면 안되기 때문에,
// 테스트를 할 때마다 DB에 값이 추가되거나 삭제된다. DB에는 민감한 정보가 포함되기도 하기 때문에 실제 DB와 연결해서 작업하지 않는다.
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void add() {
        // when 함수를 사용하면, 간단히 Mock 객체의 행동을 설정할 수 있다.
        // when 함수를 사용하면 단순히 "어떤 동작을 할 때~"라는 명시만 주어지는 것이다.
        // 뒤에 이어서 "어떤 것을 한다"라는 명세를 주는 함수가 then이다.
        when(this.todoRepository.save(any(TodoEntity.class))).then(AdditionalAnswers.returnsFirstArg()); // 어떤 TodoEntity 클래스가 들어오면, mockito input중 첫번째를 그대로 반환한다.

        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title");

        TodoEntity actual = this.todoService.add(expected);

        assertEquals(expected.getTitle(), actual.getTitle()); // actual과 expected의 title이 동일한지 여부 확인
    }

    @Test
    void searchById() {
        TodoEntity entity = new TodoEntity();
        entity.setId(123L);
        entity.setTitle("Test Title");
        entity.setOrder(0L);
        entity.setCompleted(false);
        Optional<TodoEntity> optional = Optional.of(entity);

        given(this.todoRepository.findById(anyLong())).willReturn(optional); // 어떤 값이든 Id 값이 주어졌을 때 optional을 반환한다.

        TodoEntity actual = this.todoService.searchById(123L);

        TodoEntity expected = optional.get();

        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getTitle(),actual.getTitle());
        assertEquals(expected.getOrder(),actual.getOrder());
        assertEquals(expected.getCompleted(),actual.getCompleted());
    }

    @Test
    public void searchByIdFailed(){
        given(this.todoRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            this.todoService.searchById(123L); // 이 값은 없는 값이기 때문에 에러를 뱉어줘야함
        });
    }
}