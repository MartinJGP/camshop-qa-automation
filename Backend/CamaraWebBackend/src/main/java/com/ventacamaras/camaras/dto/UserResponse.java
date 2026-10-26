package com.ventacamaras.camaras.dto;

import com.ventacamaras.camaras.model.Role;
import com.ventacamaras.camaras.model.User;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private boolean enabled;
    private List<String> roles;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream().map(Role::getName).sorted().toList())
                .build();
    }
}
