package com.may.accountservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserListDto {
    private String username;
    private String fullName;
    private String email;

}
