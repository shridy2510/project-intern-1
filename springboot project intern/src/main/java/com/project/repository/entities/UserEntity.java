package com.project.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Table(name="Users")
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "User-id")
    private String userId;
    @Column(name= "Username")
    private String username;
    @Column(name="Gmail")
    private String gmail;
    @Column(name="First-name")
    private String firstName;
    @Column(name="Last-name")
    private String lastName;





}
