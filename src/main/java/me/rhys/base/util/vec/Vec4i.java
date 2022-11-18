package me.rhys.base.util.vec;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Vec4i {

    public int x;
    public int y;
    public int z;
    public int a;

    public Vec4i() {
        this(0, 0, 0, 0);
    }

}