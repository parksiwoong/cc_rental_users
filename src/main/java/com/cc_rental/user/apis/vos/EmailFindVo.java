package com.cc_rental.user.apis.vos;

import java.sql.Connection;

public class EmailFindVo {
    private final String name;
    private final String contact;

    public EmailFindVo(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
