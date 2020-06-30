package com.lening.entity;

import lombok.Data;

@Data
public class Usermohu {
    private String uname;
    private String brithday;
    private String ebrithday;

    public Usermohu() {
    }

    public Usermohu(String uname, String brithday, String ebrithday) {
        this.uname = uname;
        this.brithday = brithday;
        this.ebrithday = ebrithday;
    }
}
