package com.example.user02.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message="Tên không được để trống")
    private String username;
    @NotEmpty(message="Email không được để trống")
    @Email
    private String email;
    @NotEmpty(message = "Số điện thoại khng được để trống")
    @Pattern(regexp = "\\d{10}", message = "Số điện thoại ít nhất đủ 10 số")
    private Integer phone;
    @NotEmpty(message="Mật khẩu không được để trống")
    private String password;
}
