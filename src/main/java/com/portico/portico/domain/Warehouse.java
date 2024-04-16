package com.portico.portico.domain;

import lombok.Data;

import java.util.Set;

@Data
public class Warehouse {
    private Integer id;
    private String name;
    private String address;
    private String contactName;
    private String contactPhone;
}
