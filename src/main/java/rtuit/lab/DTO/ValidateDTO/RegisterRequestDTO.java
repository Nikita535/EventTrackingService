package rtuit.lab.DTO.ValidateDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Component
public class RegisterRequestDTO {

    @NotNull(message = "Имя должно быть введено.")
    @Size(min = 4, message = "Длина имени должна быть минимум из четырёх символов.")
    private String username;

    @NotNull(message = "Пароль должен быть введён.")
    @Size(min = 5, message = "Пароль должен быть минимум из 5 символов.")
    private String password;
    private Object Roles;

    @NotNull(message = "Почта должна быть введена.")
    @Size(min = 1, message = "Почта должна быть введена.")
    @Email(message = "Почта не подходит под формат ввода.")
    private String email;
}
