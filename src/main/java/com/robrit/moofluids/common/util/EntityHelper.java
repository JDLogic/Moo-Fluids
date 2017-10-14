/*
 * EntityHelper.java
 *
 * Copyright (c) 2014-2017 TheRoBrit
 *
 * Moo-Fluids is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Moo-Fluids is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robrit.moofluids.common.util;

import com.google.common.base.CaseFormat;

import com.robrit.moofluids.common.MooFluids;
import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.ref.ModInformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.ArrayList;
import java.util.TreeMap;

public class EntityHelper {

  private static TreeMap<String, Fluid> containableFluids = new TreeMap<String, Fluid>();
  private static TreeMap<String, EntityTypeData> entityDataMap =
      new TreeMap<String, EntityTypeData>();
  private static int registeredEntityId = 0;

  public static TreeMap<String, Fluid> getContainableFluids() {
    return containableFluids;
  }

  public static Fluid[] getContainableFluidsArray() {
    return containableFluids.values().toArray(new Fluid[containableFluids.values().size()]);
  }

  public static Fluid getContainableFluid(final String fluidName) {
    if (containableFluids.containsKey(fluidName)) {
      return containableFluids.get(fluidName);
    }

    return null;
  }

  public static void setContainableFluid(final String fluidName, final Fluid fluid) {
    containableFluids.put(fluidName, fluid);
  }

  public static boolean hasContainableFluid(final String fluidName) {
    return containableFluids.containsKey(fluidName);
  }

  public static TreeMap<String, EntityTypeData> getDataForEntities() {
    return entityDataMap;
  }

  public static void setEntityData(final String fluidName, final EntityTypeData entityTypeData) {
    entityDataMap.put(fluidName, entityTypeData);
  }

  public static boolean hasEntityData(final String fluidName) {
    return entityDataMap.containsKey(fluidName);
  }

  public static EntityTypeData getEntityData(final String fluidName) {
    if (entityDataMap.containsKey(fluidName)) {
      return entityDataMap.get(fluidName);
    }

    return null;
  }

  public static void registerEntity(final Class<? extends Entity> entityClass,
                                    final String entityName, final int trackingRange,
                                    final int updateFrequency, final boolean sendsVelocityUpdates) {
    EntityRegistry.registerModEntity(new ResourceLocation(ModInformation.MOD_ID, entityName),
                                     entityClass, ModInformation.MOD_ID + "." + entityName,
                                     getRegisteredEntityId(), MooFluids.getInstance(),
                                     updateFrequency, trackingRange, sendsVelocityUpdates);
  }

  public static void registerEntity(final Class<? extends Entity> entityClass,
                                    final String entityName, final int trackingRange,
                                    final int updateFrequency, final boolean sendsVelocityUpdates,
                                    final int eggPrimary, final int eggSecondary) {
    EntityRegistry.registerModEntity(new ResourceLocation(ModInformation.MOD_ID, entityName),
                                     entityClass, ModInformation.MOD_ID + "." + entityName,
                                     getRegisteredEntityId(), MooFluids.getInstance(),
                                     trackingRange, updateFrequency, sendsVelocityUpdates,
                                     eggPrimary, eggSecondary);
  }

  public static void addSpawnFromType(final Class<? extends EntityLiving> entityClass,
                                      final int weightedProb, final int min, final int max,
                                      final EnumCreatureType typeOfCreature,
                                      final BiomeDictionary.Type... biomeTypes) {
    final ArrayList<Biome> biomes = new ArrayList<Biome>();
    for (final BiomeDictionary.Type biomeType : biomeTypes) {
      biomes.addAll(BiomeDictionary.getBiomes(biomeType));
    }

    EntityRegistry.addSpawn(entityClass, weightedProb, min, max, typeOfCreature,
                            biomes.toArray(new Biome[biomes.size()]));
  }

  public static void addSpawnAllBiomes(final Class<? extends EntityLiving> entityClass,
                                      final int weightedProb, final int min, final int max,
                                      final EnumCreatureType typeOfCreature) {
    IForgeRegistry<Biome> biomeReg = GameRegistry.findRegistry(Biome.class);
    Biome[] biomes = biomeReg.getValues().toArray(new Biome[biomeReg.getValues().size()]);

    EntityRegistry.addSpawn(entityClass, weightedProb, min, max, typeOfCreature, biomes);
  }

  public static void registerEntityLootTable(final String entityName) {
    LootTableList.register(getLootTable(entityName));
  }

  public static ResourceLocation getLootTable(final String entityName) {
    final String entityNameLocalized =
        CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityName);

    return new ResourceLocation(ModInformation.MOD_ID, "entities/" + entityNameLocalized);
  }

  public static int getRegisteredEntityId() {
    return registeredEntityId++;
  }
}