package jm.task.core.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Сущность User, для хранения информации о пользователях.
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column
    private Byte age;

}





// инфа для меня
/**
 * Добавил @Data — Lombok сгенерирует геттеры, сеттеры, toString(), equals(), hashCode().

Добавил @NoArgsConstructor — создаст пустой конструктор (нужен для JPA).

Добавил @AllArgsConstructor — создаст конструктор со всеми полями (кроме поля id — оно генерируется базой, но конструктор будет с ним).

Удалил вручную написанные геттеры, сеттеры, конструкторы и toString().
 */
