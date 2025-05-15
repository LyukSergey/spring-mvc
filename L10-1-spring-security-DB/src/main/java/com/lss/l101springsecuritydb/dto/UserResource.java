package com.lss.l101springsecuritydb.dto;

import java.util.Set;

public record UserResource(Long id, String username, String password, Set<String> roles) {

}
