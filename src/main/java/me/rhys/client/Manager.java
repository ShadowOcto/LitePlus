package me.rhys.client;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.InitializeEvent;
import me.rhys.base.event.impl.init.CommandInitializeEvent;
import me.rhys.base.event.impl.init.FileInitializeEvent;
import me.rhys.base.event.impl.init.ModuleInitializeEvent;
import me.rhys.base.module.data.Category;
import me.rhys.client.command.*;
import me.rhys.client.files.AccountsFile;
import me.rhys.client.module.combat.PVPBot;
import me.rhys.client.module.combat.aura.Aura;
import me.rhys.client.module.combat.criticals.Criticals;
import me.rhys.client.module.combat.velocity.Velocity;
import me.rhys.client.module.ghost.Reach;
import me.rhys.client.module.ghost.autoclicker.AutoClicker;
import me.rhys.client.module.longjump.Longjump;
import me.rhys.client.module.movement.CustomSpeed;
import me.rhys.client.module.movement.Sprint;
import me.rhys.client.module.movement.fly.Fly;
import me.rhys.client.module.movement.noslow.NoSlow;
import me.rhys.client.module.movement.sneak.Sneak;
import me.rhys.client.module.movement.speed.Speed;
import me.rhys.client.module.movement.step.Step;
import me.rhys.client.module.player.*;
import me.rhys.client.module.player.autoarmor.AutoArmor;
import me.rhys.client.module.player.pingspoof.PingSpoof;
import me.rhys.client.module.player.disabler.Disabler;
import me.rhys.client.module.player.nofall.NoFall;
import me.rhys.client.module.player.scaffold.Scaffold;
import me.rhys.client.module.render.*;
import me.rhys.client.module.render.animations.Animations;
import me.rhys.client.ui.alt.AltUI;
import me.rhys.client.ui.click.ClickUI;
import org.lwjgl.input.Keyboard;

public class Manager {

    @EventTarget
    public void initialize(InitializeEvent event) {
        event.setName("Aspirion");
        event.setVersion("1.1");
    }

    @EventTarget
    public void commandInitialize(CommandInitializeEvent event) {
        event.register(
                new HelpCommand("help", "shows you information about other commands"),
                new ToggleCommand("toggle", "toggle <module>", "toggles a module", "t"),
                new BindCommand("bind", "bind <add/remove> <module> <key>",
                        "binds a key to a module"),
                new PluginsCommand("plugins", "shows you the servers plugins"),
                new FriendCommand("friend", "friend add / remove <name>, list",
                        "if you have friends..", "f"),
                new VClip("vclip", "teleport through blocks", "vc")
        );
    }

    @EventTarget
    public void fileInitialize(FileInitializeEvent event){
        event.register(new AccountsFile());
    }

    @EventTarget
    public void moduleInitialize(ModuleInitializeEvent event) {
        event.register(
                new Aura("Aura", "Attacks players for you", Category.COMBAT, Keyboard.KEY_NONE),
                new Velocity("Velocity", "Control your velocity", Category.COMBAT, Keyboard.KEY_NONE),
                new Criticals("Criticals", "Force Critical attacks", Category.COMBAT, Keyboard.KEY_NONE),

                new Sprint("Sprint", "Sprints for you", Category.MOVEMENT, Keyboard.KEY_NONE),
                new Fly("Fly", "Fly like a faggot", Category.MOVEMENT, Keyboard.KEY_NONE),
                new Longjump("Longjump", "Jump long lol", Category.MOVEMENT, Keyboard.KEY_NONE),
                new Speed("Speed", "move at un-legit speeds", Category.MOVEMENT, Keyboard.KEY_NONE),
                new NoSlow("NoSlow", "Removes slowdown from items / blocks", Category.MOVEMENT, Keyboard.KEY_NONE),
                new Step("Step", "Steps up blocks", Category.MOVEMENT, Keyboard.KEY_NONE),
                new Sneak("Sneak", "Make your player sneak", Category.MOVEMENT, Keyboard.KEY_NONE),

                new Timer("Timer", "Control game time", Category.PLAYER, Keyboard.KEY_NONE),
                new PingSpoof("PingSpoof", "Spoof your connection latency", Category.PLAYER, Keyboard.KEY_NONE),
                new InventoryMove("InventoryMove", "Allows you to move in your inventory", Category.PLAYER, Keyboard.KEY_NONE),
                new NoRotate("NoRotate", "Prevents Server from changing head position", Category.PLAYER, Keyboard.KEY_NONE),

                new HUD("HUD", "HUD for the client", Category.RENDER, Keyboard.KEY_NONE),
                new ClickGui("ClickGui", "Opens the ClickUI", Category.RENDER, Keyboard.KEY_RSHIFT),
                new Cape("Cape", "Change capes", Category.RENDER, Keyboard.KEY_NONE),
                new Animations("Animations", "Change swing / block animations", Category.RENDER, Keyboard.KEY_NONE),
                new Infomation("Infomation", "Shows client infomation", Category.RENDER, Keyboard.KEY_NONE),
                new TargetHUD("TargetHUD", "Change swing / block animations", Category.RENDER, Keyboard.KEY_NONE),
                new Chams("Chams", "Chams?", Category.RENDER, Keyboard.KEY_NONE),
                new Nametags("Nametags", "Display a player's nametag", Category.RENDER, Keyboard.KEY_NONE),
                new NoHurtCam("NoHurtCam", "Hides hurtcam", Category.RENDER, Keyboard.KEY_NONE),
                new NoFall("NoFall", "Prevents fall damage", Category.PLAYER, Keyboard.KEY_NONE),
                new Disabler("Disabler", "Disables Anti-Cheats", Category.PLAYER, Keyboard.KEY_NONE),
                new Stealer("Stealer", "Steals items from chests", Category.PLAYER, Keyboard.KEY_NONE),
                new InvManager("Manager", "Manages your inventory", Category.PLAYER, Keyboard.KEY_NONE),
                new AutoArmor("AutoArmor", "Automatically wears armor", Category.PLAYER, Keyboard.KEY_NONE),

                new Reach("Reach", "Extend Reach", Category.GHOST, Keyboard.KEY_NONE),
                new AutoClicker("AutoClicker", "Clicks for you", Category.GHOST, Keyboard.KEY_NONE),
                new Scaffold("Scaffold", "Places blocks for you", Category.PLAYER, Keyboard.KEY_NONE),
                new CustomSpeed("CustomSpeed", "Custom speed bypasses?", Category.MOVEMENT, Keyboard.KEY_NONE),
                new PVPBot("PvPBot", "testing", Category.COMBAT, Keyboard.KEY_NONE)
        );
    }

    public static final class UI {
        public static final ClickUI CLICK = new ClickUI();
        public static final AltUI ALT = new AltUI();
    }

    public static final class Data {
        public static String lastAlt;
    }
}
