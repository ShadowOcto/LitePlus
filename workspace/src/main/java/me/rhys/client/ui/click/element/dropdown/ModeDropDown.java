package me.rhys.client.ui.click.element.dropdown;

import me.rhys.client.Manager;
import me.rhys.client.ui.click.element.button.ModuleButton;
import me.rhys.base.module.Module;
import me.rhys.base.ui.element.button.DropDownButton;
import me.rhys.base.util.vec.Vec2f;

import java.util.Comparator;

public class ModeDropDown extends DropDownButton {

    private final Module module;

    public ModeDropDown(Module module, Vec2f offset, int width, int height) {
        super(offset, width, height, module.getCurrentMode().getName());
        this.module = module;

        module.getItems().stream().filter(moduleMode -> !moduleMode.getName().equalsIgnoreCase(module.getCurrentMode().getName())).sorted(Comparator.comparingInt(value -> (int) value.getName().charAt(0))).forEachOrdered(moduleMode -> items.add(moduleMode.getName()));
    }

    @Override
    public void setCurrent(String current) {
        if (items.contains(current)) {
            this.label = current;
            this.current = items.indexOf(current);

            module.setCurrentMode(label);

            Manager.UI.CLICK.addSettings(((ModuleButton) Manager.UI.CLICK.modulesPanel.getContainer().get(Manager.UI.CLICK.moduleCurrent)).getModule());
        }
    }

}
