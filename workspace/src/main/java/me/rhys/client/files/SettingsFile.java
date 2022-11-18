package me.rhys.client.files;

import com.google.gson.Gson;
import me.rhys.client.Manager;
import me.rhys.base.file.IFile;
import me.rhys.base.util.LoginThread;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SettingsFile implements IFile {

    private File file;

    @Override
    public void save(Gson gson) {
        Map<String, String> data = new HashMap<>();

        if (Manager.Data.lastAlt != null) {
            data.put("lastAccount", Manager.Data.lastAlt);
        }

        writeFile(gson.toJson(data), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        String data = readFile(file);
        JSONObject jsonObject = new JSONObject(data);

        if (jsonObject.has("lastAccount")) {
            Manager.Data.lastAlt = jsonObject.getString("lastAccount");
            new LoginThread(Manager.Data.lastAlt.split(":")[0], Manager.Data.lastAlt.split(":")[1]).start();
        }
    }

    @Override
    public void setFile(File root) {
        file = new File(root, "/settings.json");
    }
}