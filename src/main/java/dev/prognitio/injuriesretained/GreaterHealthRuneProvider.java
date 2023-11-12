package dev.prognitio.injuriesretained;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GreaterHealthRuneProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<GreaterHealthRuneCap> GHRBonus = CapabilityManager.get(new CapabilityToken<>() {});

    private GreaterHealthRuneCap gHRBonusCap = null;

    private final LazyOptional<GreaterHealthRuneCap> optional = LazyOptional.of(this::createAttributes);

    private GreaterHealthRuneCap createAttributes() {
        if (this.gHRBonusCap == null) {
            this.gHRBonusCap = new GreaterHealthRuneCap();
        }
        return this.gHRBonusCap;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == GHRBonus) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createAttributes().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createAttributes().loadNBTData(nbt);
    }
}
