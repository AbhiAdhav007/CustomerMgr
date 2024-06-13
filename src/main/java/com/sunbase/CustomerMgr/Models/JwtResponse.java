package com.sunbase.CustomerMgr.Models;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JwtResponse {

    private String jwtToken;

    private String username;
}
