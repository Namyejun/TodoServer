package org.example.repository;

import org.example.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> { // todoentity만 저장하면 됨, request랑 response는 요청 응답용도로 끝 굳이 저장할 이유 없음

}
