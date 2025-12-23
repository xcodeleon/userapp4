package com.xcodeleon.userapp4.controller;


import com.xcodeleon.userapp4.dto.UserDTO;
import com.xcodeleon.userapp4.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @Operation(
            summary = "Создать пользователя",
            description = "Создает нового пользователя. Возвращает созданного пользователя с присвоенным идентификатором."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"id\":\"11111111-1111-1111-1111-111111111111\",\"name\":\"Иван\",\"password\":\"P@ssw0rd\"}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким id уже существует")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUserDTO(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового пользователя (name, password)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserDTO.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"name\":\"Иван\",\"password\":\"P@ssw0rd\"}"
                            ))
            )
            @RequestBody(required = true) UserDTO userDTO
    ) {
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"id\":\"11111111-1111-1111-1111-111111111111\",\"name\":\"Иван\"}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким ID не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }



    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех активных пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    @Operation(summary = "Обновить пользователя", description = "Обновляет данные существующего пользователя по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"id\":\"11111111-1111-1111-1111-111111111111\",\"name\":\"Петр\"}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким ID не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновленные данные пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserDTO.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"name\":\"Петр\"}"
                            ))
            )
            @RequestBody UserDTO userDetails) {
        UserDTO updatedUser = userService.updateUser(id, userDetails);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }



    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален, тело ответа отсутствует"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким ID не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "11111111-1111-1111-1111-111111111111")
            @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(
            summary = "Получить пользователя по логину и паролю",
            description = "Аутентификация пользователя с использованием логина и пароля",
            parameters = {
                    @Parameter(name = "login", description = "Логин пользователя", required = true, example = "john_doe"),
                    @Parameter(name = "password", description = "Пароль пользователя", required = true, example = "P@ssw0rd")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @GetMapping("/auth")
    public ResponseEntity<UserDTO> getUserByCredentials(
            @RequestParam String login,
            @RequestParam String password) {
        var user = userService.getUserByLoginAndPassword(login, password);
        return user == null ? ResponseEntity.status(401).build() : ResponseEntity.ok(user);
    }
}
