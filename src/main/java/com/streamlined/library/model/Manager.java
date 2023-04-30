package com.streamlined.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "manager")
@SuperBuilder
@NoArgsConstructor
public class Manager extends Person {

}
