package com.ventacamaras.camaras.dto;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
@NotBlank @Size(max = 255)
    String username;
@NotBlank @Size(max = 72)
    String password;
}
