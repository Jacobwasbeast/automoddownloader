package com.jacobwasbeast.amd.mixins.client;

import com.jacobwasbeast.amd.Main;
import net.devtech.grossfabrichacks.entrypoints.PrePreLaunch;

public class MainInit implements PrePreLaunch {

    @Override
    public void onPrePreLaunch() {
        if (true==true) {
            if (Main.sync(Main.ip, Main.port, Main.user, Main.pass)) {
                if (!Main.todownload.isEmpty()) {
                    Main.showmessage("Downloaded " + Main.todownload.size() + " mods, please restart your game");
                }
            } else {
                Main.showmessage("Failed to download mods error code: 0");

            }
        }
        else {
            Main.showmessage("Failed to download mods error code: 1");
        }
    }
}
