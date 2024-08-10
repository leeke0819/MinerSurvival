package org.example.code.rpg.Event;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class StructureBuilding{

    private Material[][][] structure = {
            // 그냥 각 층마다 블럭으로 탑 쌓는다고 생각하면 될듯
            {       // 1층
                    {Material.STONE_BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.STONE_BRICK_STAIRS},
                    {Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICK_STAIRS},
                    {Material.MOSSY_STONE_BRICK_STAIRS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICK_STAIRS},
                    {Material.STONE_BRICK_STAIRS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICK_STAIRS},
                    {Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS}
            },
            {       // 2층
                    {Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR},
                    {Material.AIR, Material.STONE, Material.STONE, Material.STONE,Material.AIR},
                    {Material.AIR, Material.STONE, Material.STONE, Material.STONE,Material.AIR},
                    {Material.AIR, Material.STONE, Material.STONE, Material.STONE,Material.AIR},
                    {Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR}
            },
            {       // 3층
                    {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS}
            },
            {       // 4층
                    {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS}
            },
            {       // 5층
                    {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE, Material.STONE, Material.STONE,Material.STONE_BRICKS},
                    {Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS}
            }
    };

    public void generate(World world, int x, int y, int z) {
        for (int i = 0; i < structure.length; i++) {
            for (int j = 0; j < structure[i].length; j++) {
                for (int k = 0; k < structure[i][j].length; k++) {
                    Block block = world.getBlockAt(x + i, y + j, z + k);
                    block.setType(structure[i][j][k]);
                }
            }
        }
    }
}
