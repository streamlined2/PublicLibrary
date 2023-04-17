package com.streamlined.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "librarian")
@SuperBuilder
@NoArgsConstructor
public class Librarian extends Person {

}
