package me.isRadiant.endoftime.common.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import me.isRadiant.endoftime.core.interfaces.IPlayerBoolean;
import me.isRadiant.endoftime.core.registration.ComponentReg;
import net.minecraft.nbt.NbtCompound;

public class PlayerBooleans implements IPlayerBoolean, AutoSyncedComponent
{
    Object provider;
    boolean hasReceivedSpawnItems = false;
    String hasReceivedSpawnItems_KEY = "hasReceivedSpawnItems";

    public PlayerBooleans(Object provider)
    {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(NbtCompound tag)
    {
        this.hasReceivedSpawnItems = tag.getBoolean(hasReceivedSpawnItems_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound tag)
    {
        tag.putBoolean(hasReceivedSpawnItems_KEY, this.hasReceivedSpawnItems);
    }

    @Override
    public void setHasReceivedSpawnItems(boolean flag)
    {
        this.hasReceivedSpawnItems = flag;
        ComponentReg.PLAYER_BOOLEANS.sync(this.provider);
    }

    @Override
    public boolean getHasReceivedSpawnItems()
    {
        return this.hasReceivedSpawnItems;
    }
}