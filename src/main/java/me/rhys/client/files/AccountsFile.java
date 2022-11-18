package me.rhys.client.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.rhys.client.Manager;
import me.rhys.client.ui.alt.element.AccountItem;
import lombok.val;
import me.rhys.base.file.IFile;
import me.rhys.base.util.LoginThread;

import java.io.File;

public class AccountsFile implements IFile {

    private File file;

    @Override
    public void save(Gson gson) {
        if (Manager.UI.ALT.altPanel != null) {
            val mainObj = new JsonObject();

            val array = new JsonArray();

            Manager.UI.ALT.altPanel.getContainer().stream().filter(element -> element instanceof AccountItem).forEach(element -> {
                val object = new JsonObject();
                val item = (AccountItem) element;

                object.addProperty("email", item.getEmail());
                object.addProperty("password", item.getPassword());

                array.add(object);
            });

            mainObj.add("accounts", array);

            writeFile(gson.toJson(mainObj), file);
        }
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        val mainObj = gson.fromJson(readFile(file), JsonObject.class);
        if (mainObj.has("lastAccount")) {
            val account = mainObj.get("lastAccount").getAsJsonObject();

            if (account.has("email")
                    && account.has("password")) {
                new LoginThread(
                        account.get("email").getAsString(),
                        account.get("password").getAsString()
                ).start();
            }
        }

        if (mainObj.has("accounts")) {
            val array = mainObj.get("accounts").getAsJsonArray();

            array.forEach(jsonElement -> {
                val object = jsonElement.getAsJsonObject();

                if (object.has("email")
                        && object.has("password")) {
                    Manager.UI.ALT.queue.put(
                            object.get("email").getAsString(),
                            object.get("password").getAsString()
                    );
                }
            });
        }
    }

    @Override
    public void setFile(File root) {
        file = new File(root, "/accounts.json");
    }

}