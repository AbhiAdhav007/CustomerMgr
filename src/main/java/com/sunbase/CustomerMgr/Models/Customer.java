package com.sunbase.CustomerMgr.Models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String first_name;

    private String last_name;

    private String street;

    private String address;

    private String city;

    private  String state;

    @NonNull
    @Column(unique = true)
    private String email;

    @Column(unique = true , nullable = false)
    private String phone;





}
