package com.streamlined.library.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@PreAuthorize("hasAnyRole('CUSTOMER','LIBRARIAN')")
public @interface IsCustomerOrLibrarian {

}