package MainForm;

import javafx.scene.paint.Color;

public class Block
{
    // Паттерны блоков Тетриса
    public static final boolean[][][] BlockPatterns;

    static
    {
        BlockPatterns = new boolean[][][]{
                // Кквадрат
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Т-образный
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{true, true, true, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Палка
                new boolean[][]{new boolean[]{true, true, true, true,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // L-образный
                new boolean[][]{new boolean[]{false, false, true, false,}, new boolean[]{true, true, true, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // J-образный
                new boolean[][]{new boolean[]{true, false, false, false,}, new boolean[]{true, true, true, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Z-образный
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{false, true, true, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // S-образный
                new boolean[][]{new boolean[]{false, true, true, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},};
    }


    public static final boolean[][][] BlockPatterns90;

    static
    {
        BlockPatterns90 = new boolean[][][]{
                // Квадрат
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Т-образный
                new boolean[][]{new boolean[]{true, false, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{true, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Палка
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,},},
                // L-образный
                new boolean[][]{new boolean[]{true, false, false, false,}, new boolean[]{true, false, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,},},
                // J-образный
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{true, false, false, false,}, new boolean[]{true, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Z-образный
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{true, false, false, false,}, new boolean[]{false, false, false, false,},},
                // S-образный
                new boolean[][]{new boolean[]{true, false, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, false, false, false,},},};
    }


    public static final boolean[][][] BlockPatterns180;

    static
    {
        BlockPatterns180 = new boolean[][][]{
                // Кквадрат
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Т-образный
                new boolean[][]{new boolean[]{true, true, true, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Палка
                new boolean[][]{new boolean[]{true, true, true, true,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // L-образный
                new boolean[][]{new boolean[]{true, true, true, false,}, new boolean[]{true, false, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // J-образный
                new boolean[][]{new boolean[]{true, true, true, false,}, new boolean[]{false, false, true, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Z-образный
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{false, true, true, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // S-образный
                new boolean[][]{new boolean[]{false, true, true, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},};
    }

    public static final boolean[][][] BlockPatterns270;

    static
    {
        BlockPatterns270 = new boolean[][][]{
                // Кквадрат
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,}, new boolean[]{false, false, false, false,},},
                // Т-образный
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, false, false, false,},},
                // Палка
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,},},
                // L-образный
                new boolean[][]{new boolean[]{true, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, false, false, false,},},
                // J-образный
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, false, false, false,},},
                // Z-образный
                new boolean[][]{new boolean[]{false, true, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{true, false, false, false,}, new boolean[]{false, false, false, false,},},
                // S-образный
                new boolean[][]{new boolean[]{true, false, false, false,}, new boolean[]{true, true, false, false,}, new boolean[]{false, true, false, false,}, new boolean[]{false, false, false, false,},},};
    }


    private int posX = 0;
    private int posY = 0;
    private BlockTypes blockType = BlockTypes.Square;
    private BlockRotation rotation = BlockRotation.Deg0;


    private Color color;


    public Block(Color color)
    {
        this.setColor(color);

        GenerateBlock();
    }
    public Block (Color color, BlockTypes type)
    {
        this.setColor(color);

        GenerateBlock(type);
    }


    // "Свап" осей X и Y для удобства разметки паттернов блоков
    public boolean[][] SwapAxies(boolean[][] grid)
    {
        boolean[][] resultCells = new boolean[4][4];
        for (int x = 0; x < grid.length; x++)
        {
            for (int y = 0; y < (grid[x]).length; y++)
            {
                resultCells[y][x] = grid[x][y];
            }
        }
        return resultCells;
    }

    private void GenerateBlock()
    {
        int rotation = MainForm.getRandom().nextInt(BlockRotation.values().length);
        int newBlockType = MainForm.getRandom().nextInt(BlockTypes.values().length);

        switch (rotation)
        {
            case 0:
                this.rotation = BlockRotation.Deg0;
                break;
            case 1:
                this.rotation = BlockRotation.Deg90;
                break;
            case 2:
                this.rotation = BlockRotation.Deg180;
                break;
            case 3:
                this.rotation = BlockRotation.Deg270;
                break;
        }

        switch (newBlockType)
        {
            case 0 -> {
                this.blockType = BlockTypes.Square;
            }
            case 1 -> {
                this.blockType = BlockTypes.TShape;
            }
            case 2 -> {
                this.blockType = BlockTypes.Rod;
            }
            case 3 -> {
                this.blockType = BlockTypes.LShape;
            }
            case 4 -> {
                this.blockType = BlockTypes.JShape;
            }
            case 5 -> {
                this.blockType = BlockTypes.ZShape;
            }
            case 6 -> {
                this.blockType = BlockTypes.SShape;
            }
        }
    }

    private void GenerateBlock(BlockTypes type)
    {
        int rotation = MainForm.getRandom().nextInt(BlockRotation.values().length);
        int newBlockType = MainForm.getRandom().nextInt(BlockTypes.values().length);

        switch (rotation)
        {
            case 0:
                this.rotation = BlockRotation.Deg0;
                break;
            case 1:
                this.rotation = BlockRotation.Deg90;
                break;
            case 2:
                this.rotation = BlockRotation.Deg180;
                break;
            case 3:
                this.rotation = BlockRotation.Deg270;
                break;
        }

        this.blockType = type;
    }


    public BlockRotation GetNextRotation()
    {
        switch (rotation)
        {
            case Deg0 -> {
                return BlockRotation.Deg90;
            }
            case Deg90 -> {
                return BlockRotation.Deg180;
            }
            case Deg180 -> {
                return BlockRotation.Deg270;
            }
            case Deg270 -> {
                return BlockRotation.Deg0;
            }
        }

        return BlockRotation.Deg0;
    }

    public boolean[][] GetRotatetBlockPattern(BlockRotation rot)
    {
        boolean[][] result = new boolean[4][4];
        switch (rot)
        {
            case Deg0 -> {
                switch (blockType)
                {
                    case Square -> {
                        result = BlockPatterns[0];
                    }
                    case TShape -> {
                        result = BlockPatterns[1];
                    }
                    case Rod -> {
                        result = BlockPatterns[2];
                    }
                    case LShape -> {
                        result = BlockPatterns[3];
                    }
                    case JShape -> {
                        result = BlockPatterns[4];
                    }
                    case ZShape -> {
                        result = BlockPatterns[5];
                    }
                    case SShape -> {
                        result = BlockPatterns[6];
                    }
                }
            }
            case Deg90 -> {
                switch (blockType)
                {
                    case Square -> {
                        result = BlockPatterns90[0];
                    }
                    case TShape -> {
                        result = BlockPatterns90[1];
                    }
                    case Rod -> {
                        result = BlockPatterns90[2];
                    }
                    case LShape -> {
                        result = BlockPatterns90[3];
                    }
                    case JShape -> {
                        result = BlockPatterns90[4];
                    }
                    case ZShape -> {
                        result = BlockPatterns90[5];
                    }
                    case SShape -> {
                        result = BlockPatterns90[6];
                    }
                }
            }
            case Deg180 -> {
                switch (blockType)
                {
                    case Square -> {
                        result = BlockPatterns180[0];
                    }
                    case TShape -> {
                        result = BlockPatterns180[1];
                    }
                    case Rod -> {
                        result = BlockPatterns180[2];
                    }
                    case LShape -> {
                        result = BlockPatterns180[3];
                    }
                    case JShape -> {
                        result = BlockPatterns180[4];
                    }
                    case ZShape -> {
                        result = BlockPatterns180[5];
                    }
                    case SShape -> {
                        result = BlockPatterns180[6];
                    }
                }
            }
            case Deg270 -> {
                switch (blockType)
                {
                    case Square -> {
                        result = BlockPatterns270[0];
                    }
                    case TShape -> {
                        result = BlockPatterns270[1];
                    }
                    case Rod -> {
                        result = BlockPatterns270[2];
                    }
                    case LShape -> {
                        result = BlockPatterns270[3];
                    }
                    case JShape -> {
                        result = BlockPatterns270[4];
                    }
                    case ZShape -> {
                        result = BlockPatterns270[5];
                    }
                    case SShape -> {
                        result = BlockPatterns270[6];
                    }
                }
            }
        }

        return SwapAxies(result);
    }


    public boolean[][] getCells()
    {
        return GetRotatetBlockPattern(rotation);
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public int getPosX()
    {
        return posX;
    }

    public void setPosX(int posX)
    {
        this.posX = posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public void setPosY(int posY)
    {
        this.posY = posY;
    }

    public BlockRotation getRotation()
    {
        return rotation;
    }

    public void setRotation(BlockRotation rotation)
    {
        this.rotation = rotation;
    }

    public BlockTypes getBlockType()
    {
        return blockType;
    }

    public void setBlockType(BlockTypes blockType)
    {
        this.blockType = blockType;
    }
}


