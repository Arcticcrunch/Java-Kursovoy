package MainForm;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell
{
    public Cell(Color color, boolean isEmpty)
    {
        this.rect = new Pane();
        this.setColor(color);
        this.setEmpty(isEmpty);
    }

    private boolean isEmpty = true;
    private Pane rect;
    public Color getColor()
    {
        return (Color) this.rect.getBackground().getFills().get(0).getFill();
    }
    public void setColor(Color color)
    {
        this.rect.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public boolean isEmpty()
    {
        return isEmpty;
    }

    public void setEmpty(boolean empty)
    {
        isEmpty = empty;
    }

    public Pane getRect()
    {
        return rect;
    }
}
