package com.left.gank.data.bean;

import java.io.Serializable;

public class PoseEvent implements Serializable {
    public int code;

    public boolean status;

    public PoseEvent(int code) {
        this.code = code;
    }

    public PoseEvent(int code, boolean status) {
        this.code = code;
        this.status = status;
    }
}
