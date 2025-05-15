package com.lss.l101springsecuritydb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    //
    // serialVersionUID — це унікальний ідентифікатор версії класу,
    // який використовується в Java для серіалізації та десеріалізації об'єктів (інтерфейс Serializable).
    // Його основне призначення — гарантувати, що під час десеріалізації (відновлення об'єкта з байтового потоку) клас, який використовується,
    // сумісний із класом, який був використаний для серіалізації (запису об'єкта).
    //
    // Серіалізація — це процес перетворення об'єкта у послідовність байтів для збереження або передачі (наприклад, у файл чи по мережі).
    // Десеріалізація — це зворотний процес, коли з цієї послідовності байтів відновлюється початковий об'єкт.
    //
    // Пояснення:
    //
    // Коли об'єкт серіалізується, разом із його даними записується і значення serialVersionUID.
    // serialVersionUID записується у байтовий потік разом із іншими даними обʼєкта.
    // Це значення зберігається у заголовку серіалізованого обʼєкта, щоб під час десеріалізації JVM могла перевірити сумісність класу,
    // який зчитується, із класом, що є у ClassLoader.
    // Під час десеріалізації JVM порівнює serialVersionUID класу, який зчитується, із UID класу, який є у поточному ClassLoader.
    // Якщо значення не співпадають — виникає помилка InvalidClassException. Це захищає від некоректного відновлення об'єктів, якщо структура класу змінилася (наприклад, додали/видалили поля).
    // Якщо serialVersionUID не вказаний явно, JVM генерує його автоматично на основі структури класу, але це може призвести до несподіваних проблем при зміні коду.
    // Рекомендується явно оголошувати serialVersionUID, щоб контролювати сумісність версій класу.
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}
