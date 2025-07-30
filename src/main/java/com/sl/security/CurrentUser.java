package com.sl.security;

import com.sl.entity.User;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.Optional;

import static java.util.Objects.requireNonNull;


public class CurrentUser {

    private static final Logger log = LoggerFactory.getLogger(CurrentUser.class);

    private final SecurityContextHolderStrategy securityContextHolderStrategy;

    /**
     * Creates a new {@code CurrentUser} service for the given {@link SecurityContextHolderStrategy}.
     * <p>
     * This constructor uses the new Spring Security recommendation of accessing the
     * {@link SecurityContextHolderStrategy} as a bean rather than using the static methods of
     * {@link org.springframework.security.core.context.SecurityContextHolder}.
     * </p>
     *
     * @param securityContextHolderStrategy
     *            the strategy used to fetch the security context (never {@code null}).
     */
    public CurrentUser(SecurityContextHolderStrategy securityContextHolderStrategy) {
        this.securityContextHolderStrategy = requireNonNull(securityContextHolderStrategy);
    }





}
