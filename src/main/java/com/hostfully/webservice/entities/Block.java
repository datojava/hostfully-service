package com.hostfully.webservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "block")
public class Block {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}
