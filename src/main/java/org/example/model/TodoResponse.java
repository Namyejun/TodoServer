package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {

    private Long id;

    private String title;

    private Long order;

    private Boolean completed;

    private String url;

    public TodoResponse(TodoEntity todoEntity){
        this.id = todoEntity.getId();
        this.title = todoEntity.getTitle();
        this.order = todoEntity.getOrder();
        this.completed = todoEntity.getCompleted();
        this.url = "http://localhost:8080/" + this.id;
        // 저렇게 url을 하드코딩하는 것은 선호되지 않음, url이 바뀌거나 포트가 바뀔 때 계속 컨트롤 해야하기 때문
        // 그래서 config나 property로 관리한다.
    }
}
