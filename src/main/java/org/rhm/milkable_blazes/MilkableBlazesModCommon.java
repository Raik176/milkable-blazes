package org.rhm.milkable_blazes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Random;

public class MilkableBlazesModCommon {
	public static final String MOD_ID = "template";
	public static final TagKey<Item> SHEAR_TAG = TagKey.create(Registries.ITEM, getLocation("c", "tools/shear"));
	public static final TagKey<Item> SHEARS_TAG = TagKey.create(Registries.ITEM, getLocation("c", "tools/shears"));
	public static final TagKey<Item> EMPTY_BUCKET_TAG = TagKey.create(Registries.ITEM, getLocation("c", "buckets/empty"));


	public static void init() {

	}

	public static ResourceLocation getLocation(String namespace, String id) {

		return ResourceLocation.tryBuild(namespace, id);
	}
}
