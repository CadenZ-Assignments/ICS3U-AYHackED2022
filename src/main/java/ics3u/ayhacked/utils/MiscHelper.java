package ics3u.ayhacked.utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

public class MiscHelper {

    public static final double ONEPIXEL = 1D / 16;

    public static double randomInRange(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static float randomInRange(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

    public static int randomInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static <K, V> HashMap<K, V> newHashmap(List<K> keys, List<V> values) {
        HashMap<K, V> map = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            try {
                map.put(keys.get(i), values.get(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                return map;
            }
        }

        return map;
    }

    /**
     * @return returns a new list that contains all the object in the first list that isn't in the second list
     */
    public static <E> List<E> filterList(Collection<E> list, Collection<E> other) {
        return list.stream().filter(obj -> !other.contains(obj)).collect(Collectors.toList());
    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static boolean chanceHandling(int chance) {
        Random rand = new Random();
        double randN = rand.nextDouble();

        return randN < (double) chance / 100;
    }

    public static boolean chanceHandling(float chance) {
        Random rand = new Random();
        double randN = rand.nextDouble();

        return randN < (double) chance;
    }
}
