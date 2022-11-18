package me.rhys.base.friend;

import lombok.Getter;
import lombok.Setter;

/**
 * Created on 13/09/2020 Package me.rhys.lite.friend
 */
@Getter
@Setter
public class Friend {

    private String name;
    private String alias;

    public Friend(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Friend(String name) {
        this.name = name;
    }
}
