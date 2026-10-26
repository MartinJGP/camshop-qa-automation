package com.ventacamaras.camaras.dto;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
@NotBlank @Email @Size(max = 255)
    String username;
@NotBlank @Size(min = 8, max = 72)
    String password;
}
