package me.rhys.client.module.player.disabler;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.player.disabler.modes.*;

public class Disabler extends Module {
    public Disabler(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(
                new C0C("C0C", this),
                new KeyChange("KeyChange", this),
                new NullPlace("NullPlace", this),
                new C03Replace("C03Replace", this),
                new KeepAliveCancel("KeepAliveCancel", this),
                new TransactionCancel("TransactionCancel", this),
                new TransactionKeepAliveCancel("C0F&C00-Cancel", this)
        );
    }
}
