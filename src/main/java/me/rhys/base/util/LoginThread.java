package me.rhys.base.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.rhys.client.Manager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;

import java.net.Proxy;


@Getter
@Setter
public class LoginThread extends Thread {

    private String password;
    public String status;
    private String username;

    private Minecraft mc;
    public boolean loggedIn;
    public boolean playSound = false;

    public LoginThread() {
        this.mc = Minecraft.getMinecraft();
        this.status = (EnumChatFormatting.GRAY + "Waiting...");
        loggedIn = false;
    }

    public LoginThread(String username, String password) {
        super("Alt Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.username = username;
        this.password = password;
        this.status = (EnumChatFormatting.GRAY + "Waiting...");
        loggedIn = false;
    }

    public LoginThread(String alt) {
        super("Alt Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.username = alt.split(":")[0];
        this.password = alt.split(":")[1];
        this.status = (EnumChatFormatting.GRAY + "Waiting...");
        loggedIn = false;
    }

    public Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            loggedIn = true;
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (com.mojang.authlib.exceptions.AuthenticationException e) {
            this.status = (EnumChatFormatting.RED + "Login failed!");
        }
        return null;
    }

    @Override
    public void run() {
        if (this.password == null || this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = (EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline account [cracked])");
            loggedIn = true;
            return;
        }
        this.status = (EnumChatFormatting.YELLOW + "Logging in...");
        Session auth = createSession(this.username, this.password);
        if (auth == null) {
            this.status = (EnumChatFormatting.RED + "Login failed!");
            loggedIn = false;
            if (playSound) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), -0.25F));
            }
        } else {
            this.status = (EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
            this.mc.session = auth;
            loggedIn = true;

            Manager.Data.lastAlt = this.username + ":" + this.password;
            if (playSound) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.orb"), 1.0F));
            }
        }
    }
}