package me.rhys.client.files;

import com.google.gson.Gson;
import me.rhys.base.Lite;
import me.rhys.base.file.IFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FriendsFile implements IFile {

    private File file;

    @Override
    public void save(Gson gson) {
        Map<String, String> data = new HashMap<>();

        Lite.FRIEND_MANAGER.getFriendList()
                .forEach(friend -> data.put(friend.getName(), friend.getAlias()));

        writeFile(gson.toJson(data), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        String data = readFile(file);
        JSONObject jsonObject = new JSONObject(data);

        if (jsonObject.names() != null) {
            JSONArray jsonArray = jsonObject.names();

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getString(i).replace(" ", "");

                    if (name.length() > 0) {
                        boolean hasAlias = false;

                        try {
                            hasAlias = jsonObject.getString(name) != null
                                    && !jsonObject.getString(name).equalsIgnoreCase("null");
                        } catch (Exception ignored) {
                        }

                        Lite.FRIEND_MANAGER.addFriend(name, (hasAlias ? jsonObject.getString(name) : null));
                    }
                }
            }
        }
    }

    @Override
    public void setFile(File root) {
        file = new File(root, "/friends.json");
    }
}