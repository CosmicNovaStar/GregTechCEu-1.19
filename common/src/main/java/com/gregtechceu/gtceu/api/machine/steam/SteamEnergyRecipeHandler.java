package com.gregtechceu.gtceu.api.machine.steam;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author KilaBash
 * @date 2023/3/15
 * @implNote SteamEnergyRecipeHandler
 */
public class SteamEnergyRecipeHandler implements IRecipeHandler<Long> {

    private final NotifiableFluidTank steamTank;
    private final double conversionRate; //energy units per millibucket

    public SteamEnergyRecipeHandler(NotifiableFluidTank steamTank, double conversionRate) {
        this.steamTank = steamTank;
        this.conversionRate = conversionRate;
    }

    @Override
    public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, @Nullable String slotName, boolean simulate) {
        long sum = left.stream().reduce(0L, Long::sum);
        long realSum = (long) Math.ceil(sum / conversionRate);
        if (realSum > 0) {
            var steam = GTMaterials.Steam.getFluid(realSum);
            var list = new ArrayList<FluidStack>();
            list.add(steam);
            var leftSteam = steamTank.handleRecipeInner(io, recipe, list, slotName, simulate);
            if (leftSteam == null || leftSteam.isEmpty()) return null;
            sum = (long) (leftSteam.get(0).getAmount() * conversionRate);
        }
        return sum <= 0 ? null : Collections.singletonList(sum);
    }

    @Override
    public long getTimeStamp() {
        return steamTank.getTimeStamp();
    }

    @Override
    public void setTimeStamp(long timeStamp) {
        steamTank.setTimeStamp(timeStamp);
    }

    @Override
    public RecipeCapability<Long> getCapability() {
        return EURecipeCapability.CAP;
    }
}
