package me.rhys.client.ui.alt.element;

import lombok.Getter;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.LoginThread;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;

@Getter
public class AccountItem extends Panel {

    private final String email;
    private final String password;
    private long lastClick = System.currentTimeMillis();

    public AccountItem(String email, String password, int width, int height) {
        super(new Vec2f(), width, height);
        this.email = email;
        this.password = password;
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        StringBuilder info = new StringBuilder();

        if (email.contains("@")) {
            String[] parts = email.split("@");

            int clip = parts[0].length() / 2;

            info.append(parts[0], 0, clip);

            for (int i = 0; i < parts[0].length() - clip; i++) {
                info.append("*");
            }

            info.append("@");
            info.append(parts[1]);
        } else {
            int clip = email.length() / 2;

            info.append(email, 0, clip);

            for (int i = 0; i < email.length() - clip; i++) {
                info.append("*");
            }
        }

        FontUtil.drawStringWithShadow(info.toString(), pos.clone().add(5, 5), -1);

        super.draw(mouse, partialTicks);
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        //This is to prevent spam logging in, which can cause the account to get locked by Mojang
        //or your current IP to get temporarily blacklisted.
        if(System.currentTimeMillis() - lastClick > 1000L) {
            LoginThread loginThread = new LoginThread(
                    email,
                    password
            );
            loginThread.playSound = true;

            loginThread.start();

            getScreen().displayPopup(null);
            lastClick = System.currentTimeMillis();
        }
        super.clickMouse(pos, button);
    }
}
