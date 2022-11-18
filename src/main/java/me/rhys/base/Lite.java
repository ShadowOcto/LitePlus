package me.rhys.base;

import com.github.creeper123123321.viafabric.ViaFabric;
import me.rhys.base.font.Fonts;
import me.rhys.client.Manager;
import me.rhys.base.client.ClientManifest;
import me.rhys.base.command.CommandFactory;
import me.rhys.base.event.EventBus;
import me.rhys.base.event.impl.InitializeEvent;
import me.rhys.base.event.impl.init.CommandInitializeEvent;
import me.rhys.base.event.impl.init.FileInitializeEvent;
import me.rhys.base.event.impl.init.ModuleInitializeEvent;
import me.rhys.base.file.FileFactory;
import me.rhys.client.files.FriendsFile;
import me.rhys.base.file.impl.ModulesFile;
import me.rhys.client.files.SettingsFile;
import me.rhys.base.friend.FriendManager;
import me.rhys.base.module.ModuleFactory;
import me.rhys.base.module.setting.SettingFactory;
import org.lwjgl.opengl.Display;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Lite {

    public static final EventBus EVENT_BUS = new EventBus();

    public static final ModuleFactory MODULE_FACTORY = new ModuleFactory();

    public static final SettingFactory SETTING_FACTORY = new SettingFactory();

    public static final FileFactory FILE_FACTORY = new FileFactory();

    public static final CommandFactory COMMAND_FACTORY = new CommandFactory('.');

    public static final ClientManifest MANIFEST = new ClientManifest("Lite", "2.1");

    public static final FriendManager FRIEND_MANAGER = new FriendManager();

    public static final String CLIENT_VERSION = MANIFEST.getVersion();

    public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    public static void initialize() {

        InitializeEvent initializeEvent;

        // register viamcp
        try {
            new ViaFabric().onInitialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // register the client so it can receive events
        EVENT_BUS.register(new Manager());

        // setup the client information
        EVENT_BUS.call(initializeEvent = new InitializeEvent());

        // update the manifest information
        MANIFEST.setName(initializeEvent.getName());
        MANIFEST.setVersion(initializeEvent.getVersion());

        // update the game title
        Display.setTitle(MANIFEST.getName() + " v" + MANIFEST.getVersion());

        // register the module factory so it can receive events
        EVENT_BUS.register(MODULE_FACTORY);

        // call the module initialize event
        EVENT_BUS.call(new ModuleInitializeEvent(MODULE_FACTORY));

        // setup the file system
        FILE_FACTORY.setupRoot();

        // collect all the settings in the settings factory
        SETTING_FACTORY.fetchSettings();

        // adding the default files into the file system
        FILE_FACTORY.add(new ModulesFile());
        FILE_FACTORY.add(new SettingsFile());
        FILE_FACTORY.add(new FriendsFile());

        // calling the file initialize event
        EVENT_BUS.call(new FileInitializeEvent(FILE_FACTORY));

        // call the command initialize event
        EVENT_BUS.call(new CommandInitializeEvent(COMMAND_FACTORY));

        // register the command manager
        EVENT_BUS.register(COMMAND_FACTORY);

        // load in all the data from files
        FILE_FACTORY.load();

        // setup fonts
        Fonts.INSTANCE.setup();
    }

    public static void shutdown() {
        // save all the data to the files
        FILE_FACTORY.save();
    }
}
