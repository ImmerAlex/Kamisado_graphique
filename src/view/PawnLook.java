package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Pawn;

public class PawnLook extends ElementLook {
    private final int radius;

    public PawnLook(int radius, GameElement element) {
        super(element);

        this.radius = radius;
        render();
    }

    /**
     * render() is used to create the visual shape of the element
     * It is normally called only once, when the look is added to the GameStageView
     */
    @Override
    protected void render() {
        Pawn pawn = (Pawn)element;
        Circle circle = new Circle();
        circle.setRadius(radius);

        circle.setFill(Color.valueOf(pawn.getColor()));
        if (pawn.getSymbol() == 'O') {
            circle.setStroke(Color.WHITE);
        } else {
            circle.setStroke(Color.BLACK);
        }

//        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);
        circle.setSmooth(true);
        addShape(circle);

//        // Add pawn symbol
//        Text pawnSymbol = new Text("" + pawn.getSymbol());
//        pawnSymbol.setFont(new Font(radius)); // Adjust font size based on the radius
//        pawnSymbol.setFill(Color.BLACK); // Set color of the pawn symbol
//        pawnSymbol.setX(-radius / 2f + 3.8); // Center the pawn symbol
//        pawnSymbol.setY(radius / 2f - 5); // Center the pawn symbol
//        addShape(pawnSymbol); // Add the pawn symbol to the shapes
    }
}
