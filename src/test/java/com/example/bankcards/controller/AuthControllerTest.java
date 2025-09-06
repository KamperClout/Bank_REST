package com.example.bankcards.controller;

import com.example.bankcards.dto.authDto.AuthRequestDto;
import com.example.bankcards.dto.authDto.AuthResponseDto;
import com.example.bankcards.dto.authDto.RegisterRequestDto;
import com.example.bankcards.dto.userDto.UserDto;
import com.example.bankcards.exception.ExceptionHandlerController;
import com.example.bankcards.exception.userException.InvalidPasswordException;
import com.example.bankcards.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(exceptionHandlerController).build();
    }

    @Test
    void shouldReturnTokensAndUserDtoWhenRegister() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("Pavel",
                "kamper.inc@yandex.ru", "user123");
        AuthResponseDto response = new AuthResponseDto("access", "refresh", UserDto.builder()
                .name("Pavel")
                .build());
        when(authService.register(any(RegisterRequestDto.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"))
                .andExpect(jsonPath("$.user.name").value("Pavel"));
        verify(authService).register(any(RegisterRequestDto.class));
    }

    @Test
    void shouldReturnTokensAndUserDtoWhenLogin() throws Exception {
        AuthRequestDto auth = new AuthRequestDto("kamper.inc@yandex.ru", "user123");
        AuthResponseDto response = new AuthResponseDto("access", "refresh", UserDto.builder()
                .name("Pavel")
                .email("kamper.inc@yandex.ru")
                .build());
        when(authService.login(any(AuthRequestDto.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"))
                .andExpect(jsonPath("$.user.email").value("kamper.inc@yandex.ru"));
        verify(authService).login(any(AuthRequestDto.class));
    }

    @Test
    void shouldReturnNewAccessTokenWhenRefreshTokenValid() throws Exception {
        String refreshToken = "valid-refresh-token";
        UserDto userDto = UserDto.builder().name("Pavel").build();
        AuthResponseDto response = new AuthResponseDto("new-access-token", refreshToken, userDto);

        when(authService.refreshToken(anyString())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("valid-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.user.name").value("Pavel"));

        verify(authService).refreshToken(refreshToken);
    }
    @Test
    void shouldReturnUnauthorizedWhenLoginWithWrongPassword() throws Exception {
        AuthRequestDto auth = new AuthRequestDto("kamper.inc@yandex.ru", "wrongPass");

        when(authService.login(any(AuthRequestDto.class)))
                .thenThrow(new InvalidPasswordException("Bad credentials"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auth)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }
    @Test
    void shouldReturnBadRequestWhenRegisterWithInvalidEmail() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "Pavel", "not-an-email", "123"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isBadRequest());
    }

}
