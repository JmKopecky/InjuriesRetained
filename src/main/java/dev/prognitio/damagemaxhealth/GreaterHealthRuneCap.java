package dev.prognitio.damagemaxhealth;

import net.minecraft.nbt.CompoundTag;

public class GreaterHealthRuneCap {

    private int healthBonus = 0;

    public void setHealthBonus(int bonus) {
        this.healthBonus = bonus;
    }

    public int getHealthBonus() {
        return healthBonus;
    }


    public void copyFrom(GreaterHealthRuneCap source) {
        this.healthBonus = source.healthBonus;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("healthbonus", this.healthBonus);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.healthBonus = nbt.getInt("healthbonus");
    }

}
