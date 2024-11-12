package org.rhm.milkable_blazes.fabric;

import net.fabricmc.api.ModInitializer;
import org.rhm.milkable_blazes.MilkableBlazesModCommon;

public class MilkableBlazesModFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		MilkableBlazesModCommon.init();
	}
}
