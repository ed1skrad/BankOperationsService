package com.bank.api.techtask.domain.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Entity class for a role.
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;

    /**
     * Default constructor.
     */
    public Role() {
    }

    /**
     * Constructor with role name.
     *
     *
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }

    public Role(RoleEnum name) {
        this.name = name;
    }
}

