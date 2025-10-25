package com.example.demo1.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "User request")
public class UserRequest {

  @Schema(description = "User name")
  @NotBlank(message = "Name is mandatory")
  private String name;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email should be valid")
  @Schema(description = "User email")
  private String email;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
}
