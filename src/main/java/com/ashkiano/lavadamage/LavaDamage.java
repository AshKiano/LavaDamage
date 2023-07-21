package com.ashkiano.lavadamage;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

//TODO přidat reload configu
//TODO udělat něco jako zčervenání obrazovky
//TODO přidat výpis do chatu a nebo do actionbaru že je horko a jsi blízko lávy
//TODO udelat aby ten efekt negovalo netherite brnko
//TODO udelat aby ten efekt negoval nějaký enchant
public class LavaDamage extends JavaPlugin {
    // Define variables to store the values loaded from the config
    private int CHECK_RADIUS;
    private int DAMAGE_AMOUNT;
    private String BYPASS_PERMISSION;

    @Override
    public void onEnable() {
        // Ensure config.yml is loaded or created
        this.saveDefaultConfig();

        // Load CHECK_RADIUS from config.yml
        CHECK_RADIUS = this.getConfig().getInt("CHECK_RADIUS", 5);
        DAMAGE_AMOUNT = this.getConfig().getInt("DAMAGE_AMOUNT", 2);
        BYPASS_PERMISSION = this.getConfig().getString("BYPASS_PERMISSION", "lavadamage.bypass");

        // Initialize Metrics for plugin analytics
        Metrics metrics = new Metrics(this, 18976);

        // Run a task every second (20 ticks = 1 second) for every player online
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Check if player is in survival or adventure mode
                    if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                        // Check if player has bypass permission
                        if (!player.hasPermission(BYPASS_PERMISSION)) {
                            // If the player is near lava, apply damage
                            if (isNearLava(player)) {
                                player.damage(DAMAGE_AMOUNT);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L);  // Run every second (20 ticks = 1 second)
    }

    // Method to check if a player is near lava
    private boolean isNearLava(Player player) {
        for (int x = -CHECK_RADIUS; x <= CHECK_RADIUS; x++) {
            for (int y = -CHECK_RADIUS; y <= CHECK_RADIUS; y++) {
                for (int z = -CHECK_RADIUS; z <= CHECK_RADIUS; z++) {
                    // If there is lava at this location, return true
                    if (player.getLocation().add(x, y, z).getBlock().getType() == Material.LAVA) {
                        return true;
                    }
                }
            }
        }
        // If there is no lava in the checked area, return false
        return false;
    }
}