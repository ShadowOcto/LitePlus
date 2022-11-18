package me.rhys.base.util;

import lombok.Getter;

/**
 * Created on 28/09/2020 Package me.rhys.lite.util
 */
@Getter
public class BlockValueEntry {
    private float min;
    private float max;

    public BlockValueEntry(float min, float max) {
        this.min = min;
        this.max = max;
    }
}
