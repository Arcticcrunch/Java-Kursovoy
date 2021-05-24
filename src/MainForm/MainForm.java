package MainForm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.sound.sampled.*;

public class MainForm
{

    // Константы
    public final int FIELD_WIDTH = 10;
    public final int FIELD_HEIGHT = 20;
    public final int LINES_TO_SPEED_UP = 2;
    public final String USER_DATA_FILE_NAME = "Userdata.dat";

    public final int ANIMATION_TICKS = 5;

    public final long BASE_GAME_SPEED = 500;

    // Глобальные параметры
    private float cellBorderSize = 2f;
    private Color emptyCellColor = Color.rgb(4, 15, 30);
    private Color cellBorderColor = Color.rgb(132, 132, 132);
    private String cellStyleClass = "Cell";

    private GameLoop gameThread;

    //private long gameLoopSleepTime = 480;
    private long speedUpCoef = 14;
    private boolean isSpeedUp = false;

    private Cell[][] cellsGrid;
    private Cell[][] previeCellsGrid;
    private Color[] blockColors;

    private int filledLinesCount = 0;
    private int[] filledLines = new int[4];

    private boolean isNeedToPlaceNewBlock = false;
    private boolean isAnimating = false;

    private boolean isExitRequested = false;
    private boolean isPause = false;
    private int gameSpeed = 1;
    private long totalLinesFilled = 0;
    private long currentScore = 0;
    private long highScore = 0;
    private boolean isGameOver = false;

    private boolean isPauseEnabled = false;

    private boolean isSoundOn = true;
    private int musicTrackNumber = 0;

    private Sound rowFillSound;
    private Sound pauseSound;
    private ArrayList<Sound> soundtracksList;
    private long musicPosition = 0;

    private Block currentBlock;
    private Block nextBlock;

    @FXML
    private GridPane fieldGrid;

    @FXML
    private GridPane previewGrid;

    @FXML
    private Button newGameButton;

    @FXML
    private Button musicButton;

    @FXML
    private Button pauseButton;

    @FXML
    private GridPane introPanel;

    @FXML
    private Label currentScoreLabel;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label gameSpeedLabel;


    @FXML
    private GridPane pauseOverlay;

    @FXML
    private GridPane gameOverOverlay;

    @FXML
    private Label gameOverScoreLabel;


    private boolean totalyRandom = true;
    private long randomSeed = 12345;
    private static Random random;

    private int animationTick = 0;


    public static Random getRandom()
    {
        return random;
    }


    // Инициализация
    @FXML
    void initialize()
    {
        // Инициализация цветов
        InitBlockColors();

        // Загрузка макс счёта
        LoadHighScore();
        String maxScore = String.format("%08d", highScore);
        Platform.runLater(() -> {
            this.highScoreLabel.setText(maxScore);
        });

        // Инициализация рандома
        if (totalyRandom) random = new Random();
        else random = new Random(randomSeed);

        // Подготовка звуков
        this.rowFillSound = new Sound("Row fill.wav");
        this.rowFillSound.SetVolume(0.095f);
        this.pauseSound = new Sound("Pause.wav");
        this.pauseSound.SetVolume(0.22f);

        this.soundtracksList = new ArrayList<Sound>();
        Sound tempSound = new Sound("Tetris music 1.wav");
        tempSound.SetVolume(0.061f);
        this.soundtracksList.add(tempSound);
        tempSound = new Sound("Tetris music 2.wav");
        tempSound.SetVolume(0.142f);
        this.soundtracksList.add(tempSound);
        tempSound = new Sound("Tetris music 3.wav");
        tempSound.SetVolume(0.17f);
        this.soundtracksList.add(tempSound);
        tempSound = new Sound("Tetris music 4.wav");
        tempSound.SetVolume(0.12f);
        this.soundtracksList.add(tempSound);



        // Подготовка кнопок
        newGameButton.setOnAction(actionEvent -> this.NewGame());
        pauseButton.setOnAction(actionEvent -> this.Pause());
        musicButton.setOnAction(actionEvent -> this.ToggleSound());


        // Создание сетки привью для следующего блока
        previeCellsGrid = new Cell[4][4];
        previewGrid.setVgap(2);
        previewGrid.setHgap(2);

        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                Cell c = new Cell(emptyCellColor, true);
                Pane rect = c.getRect();

                previewGrid.getChildren().add(rect);
                GridPane.setRowIndex(rect, y);
                GridPane.setColumnIndex(rect, x);
                GridPane.setRowSpan(rect, 1);
                GridPane.setColumnSpan(rect, 1);

                previeCellsGrid[x][y] = c;
            }
        }

        cellsGrid = new Cell[this.FIELD_WIDTH][this.FIELD_HEIGHT];
        fieldGrid.setVgap(2);
        fieldGrid.setHgap(2);


        for (int x = 0; x < this.FIELD_WIDTH; x++)
        {
            for (int y = 0; y < this.FIELD_HEIGHT; y++)
            {
                Cell c = new Cell(emptyCellColor, true);
                Pane rect = c.getRect();

                fieldGrid.getChildren().add(rect);
                GridPane.setRowIndex(rect, y);
                GridPane.setColumnIndex(rect, x);
                GridPane.setRowSpan(rect, 1);
                GridPane.setColumnSpan(rect, 1);

                cellsGrid[x][y] = c;
            }
        }

        this.musicTrackNumber = random.nextInt(soundtracksList.size());
        this.soundtracksList.get(this.musicTrackNumber).Play();


        this.pauseButton.setDisable(true);

        gameThread = new GameLoop(this);
        gameThread.start();
    }


    public void KeyDown(KeyCode keyCode)
    {
        if (keyCode == KeyCode.A && this.currentBlock != null)
        {
            if (CheckBlockMoveCollision(this.currentBlock, -1, 0) == false)
            {
                MoveBlock(this.currentBlock, this.currentBlock.getPosX() - 1, this.currentBlock.getPosY());
            }
        }
        if (keyCode == KeyCode.W && this.currentBlock != null)
        {
            TryRotateBlock(this.currentBlock);
        } else if (keyCode == KeyCode.NUMPAD0 && this.currentBlock != null)
        {
            TryRotateBlock(this.currentBlock);
        }
        if (keyCode == KeyCode.S && this.currentBlock != null)
        {
            this.isSpeedUp = true;
        }

        if (keyCode == KeyCode.NUMPAD1 && this.currentBlock != null)
        {
            SetNextBlock(BlockTypes.Rod);
        }
        if (keyCode == KeyCode.D && this.currentBlock != null)
        {
            //System.out.println("D");
            if (CheckBlockMoveCollision(this.currentBlock, 1, 0) == false)
            {
                MoveBlock(this.currentBlock, this.currentBlock.getPosX() + 1, this.currentBlock.getPosY());
            }
        }
        if (keyCode == KeyCode.SPACE)
        {
            Pause();
        }

    }

    public void KeyUp(KeyCode keyCode)
    {
        if (keyCode == KeyCode.S)
        {
            this.isSpeedUp = false;
        }
        if (keyCode == KeyCode.ESCAPE)
        {
            Pause();
        }
    }

    public void CheckMusic()
    {
        if (isSoundOn)
        {
            Clip clip = soundtracksList.get(musicTrackNumber).GetClip();
            if (clip.getMicrosecondPosition() >= clip.getMicrosecondLength())
            {
                System.out.println("Трек завершился: " + musicTrackNumber);
                int newTrackNum = random.nextInt(soundtracksList.size());
                if (newTrackNum == musicTrackNumber)
                {
                    newTrackNum++;
                    if (newTrackNum >= soundtracksList.size())
                    {
                        newTrackNum = 0;
                    }
                }
                musicTrackNumber = newTrackNum;
                System.out.println("Играет новый трек: " + newTrackNum);
                clip.stop();
                soundtracksList.get(newTrackNum).Play();
            }
        }
    }

    // Подготовка цветов блоков
    private void InitBlockColors()
    {
        blockColors = new Color[6];
        blockColors[0] = Color.BLUE;
        blockColors[1] = Color.GREEN;
        blockColors[2] = Color.RED;
        blockColors[3] = Color.YELLOW;
        blockColors[4] = Color.PURPLE;
        blockColors[5] = Color.ORANGE;
    }


    // Тик таймера
    public void Tick()
    {
        if (isGameOver) return;
        if (isNeedToPlaceNewBlock)
        {
            isNeedToPlaceNewBlock = false;
            AddScore(filledLinesCount);
            TryDeleteLines();
            TryToPlaceNewBlock();
        }


        if (this.currentBlock != null)
        {
            // Падение основного блока
            boolean isBlockLanded = false;
            if (CheckBlockMoveCollision(this.currentBlock, 0, 1) == true)
            {
                // Если блок при падении столкнулся с уже установленными блоками - установить блок
                BlockLanded(this.currentBlock);
                isBlockLanded = true;
            }

            // Если блок не упал - опустить его вниз
            if (isBlockLanded == false)
            {
                MoveBlock(this.currentBlock, this.currentBlock.getPosX(), this.currentBlock.getPosY() + 1);
            }
        }
    }

    // Тик анимации
    public void AnimationTick(int animationTickNumber)
    {
        this.animationTick = animationTickNumber;

        if (isAnimating)
        {
            for (int i = 0; i < filledLinesCount; i++)
            {
                int row = this.filledLines[i];
                cellsGrid[this.animationTick + (FIELD_WIDTH / 2)][row].setColor(Color.WHITE);
                cellsGrid[(FIELD_WIDTH / 2) - this.animationTick - 1][row].setColor(Color.WHITE);
            }
            if (this.animationTick >= ANIMATION_TICKS - 1)
            {
                isAnimating = false;
            }
        }

        CheckMusic();
    }

    // Попытка установить новый блок в поле - если не удалось - GameOver
    public void TryToPlaceNewBlock()
    {
        int xPos = random.nextInt(6) + 1;
        this.currentBlock = this.nextBlock;
        SetNextBlock();
        //this.nextBlock = new Block(blockColors[random.nextInt(6)]);
        boolean[][] cls = this.currentBlock.getCells();

        // Перемещение блока в точку спауна
        this.currentBlock.setPosX(xPos);
        this.currentBlock.setPosY(0);


        for (int x = 0; x < this.currentBlock.getCells().length; x++)
        {
            for (int y = 0; y < (this.currentBlock.getCells()[x]).length; y++)
            {
                if (cls[x][y] == true)
                {
                    if (cellsGrid[xPos + x][y].isEmpty()) cellsGrid[xPos + x][y].setColor(this.currentBlock.getColor());
                }
            }
        }


        if (CheckBlockMoveCollision(this.currentBlock, 0, 0))
        {
            GameOver();
        }
    }

    private boolean CheckBlockMoveCollision(Block block, int xOffset, int yOffset)
    {
        if (block == null) return false;
        boolean[][] clls = block.getCells();
        for (int x = 0; x < clls.length; x++)
        {
            for (int y = 0; y < (clls[x]).length; y++)
            {
                // Обработка только занятых клеток
                if (clls[x][y] == true)
                {
                    // Проверка не находится ли новая точка за границами игрового поля
                    int newX = block.getPosX() + x + xOffset;
                    int newY = block.getPosY() + y + yOffset;

                    if (newX < 0 || newX >= FIELD_WIDTH)
                    {
                        return true;
                    }
                    if (newY < 0 || newY >= FIELD_HEIGHT)
                    {
                        return true;
                    }

                    // Если самоколлизии нет или клетка на нижней зоне блока, можно начинать проверку на коллизию с уже установленными блоками
                    if (cellsGrid[block.getPosX() + x + xOffset][block.getPosY() + y + yOffset].isEmpty() == false)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean CheckBlockRotationCollision(Block block, BlockRotation rotation)
    {
        boolean[][] clls = block.GetRotatetBlockPattern(rotation);
        for (int x = 0; x < clls.length; x++)
        {
            for (int y = 0; y < (clls[x]).length; y++)
            {
                // Обработка только занятых клеток
                if (clls[x][y] == true)
                {

                    // Проверка не находится ли новая точка за границами игрового поля
                    int newX = block.getPosX() + x;
                    int newY = block.getPosY() + y;

                    if (newX < 0 || newX >= FIELD_WIDTH)
                    {
                        return true;
                    }
                    if (newY < 0 || newY >= FIELD_HEIGHT)
                    {
                        return true;
                    }

                    // Если самоколлизии нет или клетка на нижней зоне блока, можно начинать проверку на коллизию с уже установленными блоками
                    if (cellsGrid[block.getPosX() + x][block.getPosY() + y].isEmpty() == false)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void BlockLanded(Block block)
    {
        boolean[][] clls = block.getCells();
        for (int x = 0; x < block.getCells().length; x++)
        {
            for (int y = 0; y < (block.getCells()[x]).length; y++)
            {
                if (clls[x][y] == true)
                {
                    cellsGrid[block.getPosX() + x][block.getPosY() + y].setColor(block.getColor());
                    cellsGrid[block.getPosX() + x][block.getPosY() + y].setEmpty(false);
                }
            }
        }

        CheckLines();

        isNeedToPlaceNewBlock = true;
    }

    private void TryDeleteLines()
    {
        if (filledLinesCount > 0)
        {
            for (int i = 0; i < filledLines.length; i++)
            {
                // Удаление строки
                for (int x = 0; x < FIELD_WIDTH; x++)
                {
                    cellsGrid[x][filledLines[i]].setEmpty(true);
                    cellsGrid[x][filledLines[i]].setColor(emptyCellColor);
                }
                //System.out.println(filledLines[i]);
                // Опускание всех вышестоящих блоков
                for (int y = filledLines[i]; y > 0; y--)
                {
                    for (int x = 0; x < FIELD_WIDTH; x++)
                    {
                        SwapCellWithCellAbove(x, y);
                    }
                }
                filledLines[i] = 0;
            }
            filledLinesCount = 0;
        }
    }

    private void SwapCellWithCellAbove(int x, int y)
    {
        Cell cell = cellsGrid[x][y];
        Cell topCell = cellsGrid[x][y - 1];

        boolean isEmpty = topCell.isEmpty();
        Color color = topCell.getColor();

        topCell.setEmpty(cell.isEmpty());
        topCell.setColor(cell.getColor());

        cell.setEmpty(isEmpty);
        cell.setColor(color);
    }

    private void CheckLines()
    {
        // Проверка заполненных линий
        boolean isAnyLineFilled = false;
        for (int y = 0; y < FIELD_HEIGHT; y++)
        {
            boolean isLineFilled = true;
            for (int x = 0; x < FIELD_WIDTH; x++)
            {
                if (cellsGrid[x][y].isEmpty() == true)
                {
                    isLineFilled = false;
                    break;
                }
            }

            if (isLineFilled)
            {
                isAnimating = true;
                filledLines[filledLinesCount] = y;
                filledLinesCount++;
                isAnyLineFilled = true;
                totalLinesFilled++;
            }
        }

        if (isAnyLineFilled)
        {
            if (isSoundOn) this.rowFillSound.Play();
            CheckGameSpeed();
        }
    }

    private void AddScore(int rowsFilled)
    {
        if (rowsFilled == 1)
        {
            currentScore += 100;
        } else if (rowsFilled == 2)
        {
            currentScore += 300;
        } else if (rowsFilled == 2)
        {
            currentScore += 700;
        } else if (rowsFilled >= 4)
        {
            currentScore += 1500;
        }

        if (currentScore > highScore)
        {
            highScore = currentScore;
            String highScoreString = String.format("%08d", highScore);
            Platform.runLater(() -> {
                this.highScoreLabel.setText(highScoreString);
            });
        }

        String currentScoreString = String.format("%08d", currentScore);
        Platform.runLater(() -> {
            this.currentScoreLabel.setText(currentScoreString);
        });
    }

    public void MoveBlock(Block block, int newPosX, int newPosY)
    {
        if (isPause) return;
        // Проверка границ поля
        // Bounxing box

        synchronized (block)
        {
            int xMin = block.getCells().length;
            int xMax = 0;
            int yMin = block.getCells()[0].length;
            int yMax = 0;


            boolean[][] cls = block.getCells();
            for (int x = 0; x < block.getCells().length; x++)
            {
                for (int y = 0; y < (block.getCells()[x]).length; y++)
                {
                    if (cls[x][y] == true)
                    {
                        if (x < xMin) xMin = x;
                        if (x > xMax) xMax = x;
                        if (y < yMin) yMin = y;
                        if (y > yMax) yMax = y;
                    }
                }
            }

            // Ограничение координат согласно Bounding box
            if (newPosX - xMin < 0)
            {
                if (block.getBlockType() == BlockTypes.Rod && (block.getRotation() == BlockRotation.Deg90 || block.getRotation() == BlockRotation.Deg270))
                {
                    if (newPosX - xMin == -2)
                    {
                        newPosX = -xMin;
                        System.out.println("Коллизия xMin");
                    }
                } else
                {
                    newPosX = -xMin;
                    System.out.println("Коллизия xMin");
                }
            } else if (newPosX + (xMax - xMin) >= FIELD_WIDTH)
            {
                newPosX = FIELD_WIDTH - (xMax - xMin) - 1;
                System.out.println("Коллизия xMax");
            }
            if (newPosY - yMin < 0)
            {
                newPosX = -yMin;
                System.out.println("Коллизия yMin");
            } else if (newPosY + (yMax - yMin) >= FIELD_HEIGHT)
            {
                newPosY = FIELD_HEIGHT - (yMax - yMin) - 1;
                System.out.println("Коллизия yMax");
            }


            // Изменение координаты блока
            block.setPosX(newPosX);
            block.setPosY(newPosY);

        }
        Redraw(block);

    }

    public void Redraw(Block block)
    {
        // Очистка всего поля нестатичного поля
        synchronized (block)
        {
            Cell c;
            for (int x = 0; x < FIELD_WIDTH; x++)
            {
                for (int y = 0; y < FIELD_HEIGHT; y++)
                {
                    c = cellsGrid[x][y];
                    if (c.isEmpty())
                    {
                        c.setColor(emptyCellColor);
                    }
                }
            }

            // Перерисовка клеток в новой позиции блока
            boolean[][] cls = block.getCells();
            for (int x = 0; x < block.getCells().length; x++)
            {
                for (int y = 0; y < (block.getCells()[x]).length; y++)
                {
                    if (cls[x][y] == true)
                    {
                        cellsGrid[block.getPosX() + x][block.getPosY() + y].setColor(block.getColor());
                    }
                }
            }
        }
    }

    public void TryRotateBlock(Block block)
    {
        synchronized (block)
        {
            if (CheckBlockRotationCollision(block, block.GetNextRotation()) == false && isPause == false)
            {
                block.setRotation(block.GetNextRotation());
                Redraw(this.currentBlock);
            }
        }
    }

    public void NewGame()
    {
        if (totalyRandom == false)
            random.setSeed(randomSeed);
        this.isGameOver = false;
        this.isPauseEnabled = true;
        this.pauseButton.setDisable(false);
        SetNextBlock();
        for (int y = 0; y < FIELD_HEIGHT; y++)
        {
            for (int x = 0; x < FIELD_WIDTH; x++)
            {
                cellsGrid[x][y].setEmpty(true);
                cellsGrid[x][y].setColor(emptyCellColor);
            }
        }
        SaveHighScore(currentScore);
        currentScore = 0;
        gameSpeed = 1;
        totalLinesFilled = 0;

        filledLinesCount = 0;
        for (int i = 0; i < filledLines.length; i++)
        {
            filledLines[i] = 0;
        }


        this.gameOverOverlay.setVisible(false);
        String currentScoreString = String.format("%08d", currentScore);
        Platform.runLater(() -> {
            this.currentScoreLabel.setText(currentScoreString);
        });
        Platform.runLater(() -> {
            gameSpeedLabel.setText(Integer.toString(gameSpeed));
        });
        Platform.runLater(() -> {
            introPanel.setVisible(false);
        });
        TryToPlaceNewBlock();
    }

    public void SaveHighScore(long highScore)
    {
        System.out.println("Сохранение рекорда...");
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE_NAME));
            writer.write(Long.toString(highScore));
            writer.close();
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void LoadHighScore()
    {
        try
        {
            File myObj = new File(USER_DATA_FILE_NAME);
            Scanner myReader = new Scanner(myObj);
            highScore = myReader.nextInt();
            myReader.close();
        } catch (Exception e)
        {
            System.out.println("Ощибка при загрузке счёта: " + e.getMessage());
        }
    }

    public void SetNextBlock()
    {
        this.nextBlock = new Block(blockColors[random.nextInt(6)]);

        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                previeCellsGrid[x][y].setColor(emptyCellColor);
            }
        }

        boolean[][] cls = this.nextBlock.getCells();
        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                int xOffset = 0;
                int yOffset = 0;
                if (this.nextBlock.getBlockType() != BlockTypes.Rod)
                {
                    xOffset = 1;
                    yOffset = 1;
                } else
                {
                    if (this.nextBlock.getRotation() == BlockRotation.Deg0 || this.nextBlock.getRotation() == BlockRotation.Deg180)
                    {
                        yOffset = 1;
                    } else
                    {
                        xOffset = 1;
                    }
                }

                if (x + xOffset < 4 && y + yOffset < 4)
                {
                    if (cls[x][y] == true)
                    {
                        previeCellsGrid[x + xOffset][y + yOffset].setColor(this.nextBlock.getColor());
                    }
                }
            }
        }
    }


    public void SetNextBlock(BlockTypes blockType)
    {
        this.nextBlock = new Block(blockColors[random.nextInt(6)], blockType);

        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                previeCellsGrid[x][y].setColor(emptyCellColor);
            }
        }

        boolean[][] cls = this.nextBlock.getCells();
        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                int xOffset = 0;
                int yOffset = 0;
                if (this.nextBlock.getBlockType() != BlockTypes.Rod)
                {
                    xOffset = 1;
                    yOffset = 1;
                } else
                {
                    if (this.nextBlock.getRotation() == BlockRotation.Deg0 || this.nextBlock.getRotation() == BlockRotation.Deg180)
                    {
                        yOffset = 1;
                    } else
                    {
                        xOffset = 1;
                    }
                }

                if (x + xOffset < 4 && y + yOffset < 4)
                {
                    if (cls[x][y] == true)
                    {
                        previeCellsGrid[x + xOffset][y + yOffset].setColor(this.nextBlock.getColor());
                    }
                }
            }
        }
    }

    public void Pause()
    {

        if (isPauseEnabled == false) return;
        if (isPause == false)
        {
            isPause = true;
            Platform.runLater(() -> {
                this.pauseButton.getStyleClass().add("ButtonActivated");
            });
            pauseOverlay.setVisible(true);
            if (isSoundOn) pauseSound.Play();
        } else
        {
            isPause = false;
            Platform.runLater(() -> {
                pauseButton.getStyleClass().removeAll("ButtonActivated");
            });
            pauseOverlay.setVisible(false);
            if (isSoundOn) pauseSound.Play();
        }
    }


    public long GetSpeedPenalty(int speed)
    {
        return (long) (BASE_GAME_SPEED * (Math.sqrt(((float) speed) / 26)));
    }

    private void GameOver()
    {
        this.isGameOver = true;
        this.isPauseEnabled = false;
        this.gameOverOverlay.setVisible(true);
        String currentScoreString = String.format("%08d", currentScore);
        Platform.runLater(() -> {
            this.gameOverScoreLabel.setText(currentScoreString);
        });
        Platform.runLater(() -> {
            this.pauseButton.setDisable(true);
        });
        SaveHighScore(currentScore);
        System.out.println("Game Over!");
    }

    public void ToggleSound()
    {
        System.out.println("Music is on: " + isSoundOn + ". Music track number: " + musicTrackNumber);
        if (isSoundOn)
        {
            isSoundOn = false;
            Platform.runLater(() -> {
                this.musicButton.getStyleClass().add("ButtonActivated");
            });
            soundtracksList.get(musicTrackNumber).Stop();
        } else
        {
            isSoundOn = true;
            soundtracksList.get(musicTrackNumber).ContinuePlay();
            Platform.runLater(() -> {
                this.musicButton.getStyleClass().removeAll("ButtonActivated");
            });
        }
    }

    public void Exit()
    {
        SaveHighScore(highScore);
        Platform.exit();
        System.exit(0);
    }

    private void CheckGameSpeed()
    {
        if (totalLinesFilled >= LINES_TO_SPEED_UP)
        {
            totalLinesFilled = totalLinesFilled % LINES_TO_SPEED_UP;
            gameSpeed = Utilities.Clamp(gameSpeed + 1, 1, 20);
            Platform.runLater(() -> {
                gameSpeedLabel.setText(Integer.toString(gameSpeed));
            });
        }
    }

    public long getGameLoopSleepTime()
    {
        if (isAnimating)
        {
            return (BASE_GAME_SPEED - GetSpeedPenalty(gameSpeed)) / ANIMATION_TICKS;
        } else
        {
            if (isSpeedUp)
            {
                return (BASE_GAME_SPEED - GetSpeedPenalty(gameSpeed)) / (speedUpCoef * ANIMATION_TICKS);
            } else
            {
                return (BASE_GAME_SPEED - GetSpeedPenalty(gameSpeed)) / ANIMATION_TICKS;
            }
        }
    }

    public boolean getIsExitRequested()
    {
        return isExitRequested;
    }

    public void RequestExit()
    {
        this.isExitRequested = true;
    }

    public boolean isPause()
    {
        return isPause;
    }

    public void setPause(boolean pause)
    {
        isPause = pause;
    }
}


