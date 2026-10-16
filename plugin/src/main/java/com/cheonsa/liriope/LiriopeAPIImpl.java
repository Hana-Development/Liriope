package com.cheonsa.liriope;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import com.cheonsa.liriope.border.BorderManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.hanayori.aster.component.Injectable;

@Injectable
public final class LiriopeAPIImpl implements ILiriopeAPI {

    private final BorderManager borderManager;

    public LiriopeAPIImpl(BorderManager borderManager) {
        this.borderManager = borderManager;
    }

    @Override
    public IBorder getBorder(Player player) {
        return this.borderManager.getBorder(player);
    }

    @Override
    public IBorder getBorder(World world) {
        return this.borderManager.getBorder(world);
    }

    @Override
    public boolean hasBorder(Player player) {
        return this.borderManager.hasBorder(player);
    }

    @Override
    public boolean hasBorder(World world) {
        return this.borderManager.hasBorder(world);
    }

    @Override
    public void removeBorder(Player player) {
        this.borderManager.removeBorder(player);
    }

    @Override
    public void removeBorder(World world) {
        this.borderManager.removeBorder(world);
    }
}
