package me.rhys.base.friend;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 13/09/2020 Package me.rhys.lite.friend
 */
@Getter
public class FriendManager {

    //don't have any :(
    private List<Friend> friendList = Collections.synchronizedList(new CopyOnWriteArrayList<>());

    public void removeFriend(String name) {
        this.friendList.remove(getFriend(name));
    }

    public void addFriend(String name) {
        this.friendList.add(new Friend(name, null));
    }

    public void addFriend(String name, String alias) {
        this.friendList.add(new Friend(name, alias));
    }

    public void changeName(String oldName, String newName) {
        getFriend(oldName).setName(newName);
    }

    public void changeAlias(String name, String alias) {
        getFriend(name).setAlias(alias);
    }

    public Friend getFriend(String name) {
        return this.friendList.parallelStream()
                .filter(friend ->
                        friend.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
