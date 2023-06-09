package com.streamlined.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@SuperBuilder
@NoArgsConstructor
public class Customer extends Person {

}
