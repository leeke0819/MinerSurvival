package org.example.code.rpg.Event;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.Directional;
import org.bukkit.block.BlockFace;

public class StructureBuilding {

    private final Material[][][] structure = {
            { // 1층
                    {Material.STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB},
                    {Material.STONE_BRICK_WALL, Material.AIR, Material.AIR, Material.AIR, Material.STONE_BRICK_WALL},
                    {Material.MOSSY_STONE_BRICK_WALL, Material.AIR, Material.AIR, Material.AIR, Material.STONE_BRICK_WALL},
                    {Material.MOSSY_STONE_BRICK_WALL, Material.END_ROD, Material.AIR, Material.END_ROD, Material.MOSSY_STONE_BRICK_WALL},
                    {Material.END_ROD, Material.AIR, Material.AIR, Material.AIR, Material.END_ROD},
            },
            { // 2층
                    {Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICK_SLAB},
                    {Material.AIR, Material.STONE_BRICKS, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICKS, Material.AIR},
                    {Material.AIR, Material.END_ROD, Material.AIR, Material.END_ROD, Material.AIR},
                    {Material.END_ROD, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB, Material.END_ROD},
                    {Material.AIR, Material.AIR, Material.STONE_BRICK_SLAB, Material.AIR, Material.AIR}
            },
            { // 3층
                    {Material.MOSSY_STONE_BRICK_SLAB, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICK_SLAB},
                    {Material.AIR, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_WALL, Material.STONE_BRICK_SLAB, Material.AIR},
                    {Material.AIR, Material.AIR, Material.BEACON, Material.AIR, Material.AIR},
                    {Material.AIR, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_WALL, Material.STONE_BRICK_SLAB, Material.AIR},
                    {Material.AIR, Material.MOSSY_STONE_BRICK_SLAB, Material.CHISELED_STONE_BRICKS, Material.STONE_BRICK_SLAB, Material.AIR},
                    {Material.AIR, Material.AIR, Material.END_ROD, Material.AIR, Material.AIR}
            },
            { // 4층
                    {Material.STONE_BRICK_SLAB, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICK_SLAB},
                    {Material.AIR, Material.MOSSY_STONE_BRICKS, Material.STONE_BRICK_SLAB, Material.STONE_BRICKS, Material.AIR},
                    {Material.AIR, Material.END_ROD, Material.AIR, Material.END_ROD, Material.AIR},
                    {Material.END_ROD, Material.MOSSY_STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB, Material.END_ROD},
                    {Material.AIR, Material.AIR, Material.MOSSY_STONE_BRICK_SLAB, Material.AIR, Material.AIR}
            },
            { // 5층
                    {Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB},
                    {Material.STONE_BRICK_WALL, Material.AIR, Material.AIR, Material.AIR, Material.MOSSY_STONE_BRICK_WALL},
                    {Material.MOSSY_STONE_BRICK_WALL, Material.AIR, Material.AIR, Material.AIR, Material.STONE_BRICK_WALL},
                    {Material.STONE_BRICK_WALL, Material.END_ROD, Material.AIR, Material.END_ROD, Material.STONE_BRICK_WALL},
                    {Material.END_ROD, Material.AIR, Material.AIR, Material.AIR, Material.END_ROD}
            }
    };

    // 반블럭 유형 및 기타 블록 데이터를 지정하는 메타데이터 배열
    private final String[][][] metadata = {
            // 1층
            {
                    {"bottom", "bottom", "bottom", "bottom", "bottom"},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, "east", null, "west", null},
                    {"up", null, null, null, "up"}
            },
            // 2층
            {
                    {"bottom", null, null, null, "bottom"},
                    {null, null, "bottom", null, null},
                    {null, "up", null, "up", null},
                    {"south", "top", "bottom", "top", "south"},
                    {null, null, "bottom", null, null}
            },
            // 3층
            {
                    {"bottom", null, null, null, "bottom"},
                    {null, "bottom", null, "bottom", null},
                    {null, null, null, null, null},
                    {null, "bottom", null, "bottom", null},
                    {null, "bottom", null, "bottom", null},
                    {null, null, "up", null, null}
            },
            // 4층
            {
                    {"bottom", null, null, null, "bottom"},
                    {null, null, "bottom", null, null},
                    {null, "up", null, "up", null},
                    {"north", "top", "bottom", "top", "north"},
                    {null, null, "bottom", null, null}
            },
            // 5층
            {
                    {"bottom", "bottom", "bottom", "bottom", "bottom"},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, "east", null, "west", null},
                    {"up", null, null, null, "up"}
            }
    };

    public void generate(World world, int x, int y, int z) {
        for (int i = 0; i < structure.length; i++) {
            for (int j = 0; j < structure[i].length; j++) {
                for (int k = 0; k < structure[i][j].length; k++) {
                    Block block = world.getBlockAt(x + i, y + j, z + k);
                    Material material = structure[i][j][k];
                    block.setType(material);
                    String data = metadata[i][j][k];

                    // 메타데이터를 기반으로 블록 방향 조정
                    if ((material == Material.STONE_BRICK_SLAB || material == Material.MOSSY_STONE_BRICK_SLAB) && data != null) {
                        Slab slab = (Slab) block.getBlockData();
                        if ("top".equals(data)) {
                            slab.setType(Slab.Type.TOP);
                        } else if("bottom".equals(data)) {
                            slab.setType(Slab.Type.BOTTOM);
                        }
                        block.setBlockData(slab);
                    } else if (material == Material.END_ROD && data != null) {
                        Directional directional = (Directional) block.getBlockData();
                        switch (data) {
                            case "up":
                                directional.setFacing(BlockFace.UP);
                                break;
                            case "down":
                                directional.setFacing(BlockFace.DOWN);
                                break;
                            case "north":
                                directional.setFacing(BlockFace.NORTH);
                                break;
                            case "south":
                                directional.setFacing(BlockFace.SOUTH);
                                break;
                            case "east":
                                directional.setFacing(BlockFace.EAST);
                                break;
                            case "west":
                                directional.setFacing(BlockFace.WEST);
                                break;
                        }
                        block.setBlockData(directional);
                    }
                }
            }
        }
    }
}
