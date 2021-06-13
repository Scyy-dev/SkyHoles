package me.scyphers.plugins.skyhole.config;

import java.io.File;
import java.util.UUID;

public class UniqueDataFile extends ConfigFile {

    private final UUID uuid;

    public UniqueDataFile(ConfigManager manager, UUID uuid) {
        super(manager, "player_data" + File.separator + uuid.toString() + ".yml", false);
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
}
