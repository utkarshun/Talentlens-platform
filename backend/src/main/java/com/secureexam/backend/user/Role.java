package com.secureexam.backend.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String name;
    public Role(String name){
        this.name=name;
    }
//    public Role(){}
//    public Role(String name){
//        this.name=name;
//    }
//    public Long getId(){return id;}
//    public String getName() {return name;}
//    public void setName(String name){
//        this.name=name;
//    }
}
