package com.gregtechceu.gtceu.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;
import java.util.function.Predicate;


/**
 * @author KilaBash
 * @date 2023/2/18
 * @implNote MachineTrait represents an abstract capability held by machine. Such as item, fluid, energy, etc.
 * All trait should be added while MetaMachine is creating. you cannot modify it on the fly。
 */
public abstract class MachineTrait implements IManaged {
    @Getter
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

    @Getter
    protected final MetaMachine machine;
    @Setter
    protected Predicate<Direction> capabilityValidator;

    public MachineTrait(MetaMachine machine) {
        this.machine = machine;
        this.capabilityValidator = side -> true;
        machine.attachTraits(this);
    }

    public final boolean hasCapability(@Nullable Direction side) {
        return capabilityValidator.test(side);
    }

    @Override
    public void onChanged() {
        machine.markDirty();
    }

    public void onMachineLoad() {

    }

    public void onMachineUnLoad() {

    }

}
