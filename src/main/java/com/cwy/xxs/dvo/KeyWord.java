package com.cwy.xxs.dvo;

import lombok.Data;

@Data
public class KeyWord {

    private String value,color;

    public KeyWord(String value, String color){
        this.color = color;
        this.value = value;
    }
}
