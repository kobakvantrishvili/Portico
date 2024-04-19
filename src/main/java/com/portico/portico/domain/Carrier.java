package com.portico.portico.domain;

import lombok.Data;

@Data
public class Carrier {
    private Integer id;
    private String organizationName;
    private String officeAddress;
    private String contactName;
    private String contactPhone;
}
