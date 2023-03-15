package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity(name="Userpojo")
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(name="email_uk",columnNames = {"email"})}
)
public class UserPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String role;

}
