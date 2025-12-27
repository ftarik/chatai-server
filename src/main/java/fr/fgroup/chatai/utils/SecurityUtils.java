package fr.fgroup.chatai.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tarik.famil (contact: tarikfamil@gmail.com)
 *
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 25 avr. 2019
 */
public class SecurityUtils {

    /**
     * Private constructor
     */
    private SecurityUtils() {
    }

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String TOKEN_TYPE = "Bearer";
    public static final String TOKEN_NAME = "Token";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String ROLE_KEY = "role";
    public static final String ID_KEY = "id";
    public static final String USERNAME_KEY = "username";
    public static final String SYSTEM_ACCOUNT = "SYSTEM";


}
