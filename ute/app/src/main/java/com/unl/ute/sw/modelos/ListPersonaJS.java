package com.unl.ute.sw.modelos;

import java.util.ArrayList;
import java.util.List;

public class ListPersonaJS {
    private String msg;
    private Integer code;
    private List<PersonaJS> data = new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<PersonaJS> getData() {
        return data;
    }

    public void setData(List<PersonaJS> data) {
        this.data = data;
    }
}


