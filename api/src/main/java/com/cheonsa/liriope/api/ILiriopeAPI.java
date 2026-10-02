package com.cheonsa.liriope.api;

import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface ILiriopeAPI {

    /**
     * Gets the player's own border, creating it if missing.
     * Creating one stops the world border from affecting this player, use {@link #hasBorder(Player)} to only check.
     */
    IBorder getBorder(Player player);

    /**
     * Gets the world's border, creating it if missing.
     */
    IBorder getBorder(World world);

    /**
     * @return true if the player has their own border
     */
    boolean hasBorder(Player player);

    /**
     * @return true if the world has a border
     */
    boolean hasBorder(World world);

    /**
     * Removes the player's own border, the player goes back to the world border.
     */
    void removeBorder(Player player);

    /**
     * Removes the world's border. Players with their own border are not affected.
     */
    void removeBorder(World world);
}
