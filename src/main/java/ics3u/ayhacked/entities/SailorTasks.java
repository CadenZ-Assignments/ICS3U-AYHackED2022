package ics3u.ayhacked.entities;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WalkTowardsPosTask;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;

public class SailorTasks {
    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> idle(VillagerProfession profession, float p_220641_1_) {
        return ImmutableList.of(new Pair<>(0, new WalkTowardsPosTask()));
    }
}
