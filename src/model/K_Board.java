package model;

import boardifier.model.ContainerElement;
import boardifier.model.ElementTypes;
import boardifier.model.GameStageModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class K_Board extends ContainerElement {

    public K_Board(int x, int y, GameStageModel gameStageModel) {
        // Call the super-constructor to create an 8x8 grid, named "holeboard", at coordinates (x, y) in space
        super("holeboard", x, y, 8, 8, gameStageModel);

        ElementTypes.register("holeBoard", 60);
        type = ElementTypes.getType("holeBoard");
        resetReachableCells(false);
    }

    public void setValidCells(Pawn pawn) {
        resetReachableCells(false);
        List<Point> validCells = computeValidCells(pawn);
        if (validCells != null) {
            for (Point p : validCells) {
                reachableCells[p.y][p.x] = true;
            }
        }
        addChangeFaceEvent();
    }

    public List<Point> computeValidCells(Pawn pawn) {
        K_StageModel stage = (K_StageModel) gameStageModel;
        List<Point> validCells = new ArrayList<>();
        int[][] directions = getPlayerDirections(stage);

//        int[][] directions = pawn.getSymbol() == 'X' ? new int[][]{{0, -1}, {1, -1}, {-1, -1}} : new int[][]{{0, 1}, {-1, 1}, {1, 1}};

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = getPawnGridCoordinate(pawn.getX(), stage.getBoard().getNbRows()) + dx;
            int y = getPawnGridCoordinate(pawn.getY(), stage.getBoard().getNbCols()) + dy;

            while (isWithinBounds(x, y)) {
                if (stage.getBoard().getElement(y, x) == null) {
                    validCells.add(new Point(x, y));
                } else {
                    break;
                }
                x += dx;
                y += dy;
            }
        }

        return validCells;
    }

    private int[][] getPlayerDirections(K_StageModel stage) {
        if (isFirstPlayer(stage)) {
            // Up, Up-Right, Up-Left
            return new int[][]{{0, -1}, {1, -1}, {-1, -1}};
        } else {
            // Down, Down-Right, Down-Left
            return new int[][]{{0, 1}, {-1, 1}, {1, 1}};
        }
    }

    private boolean isFirstPlayer(K_StageModel stage) {
        return stage.getCurrentPlayerName().equals(stage.getModel().getPlayers().get(0).getName());
    }

    public int getPawnGridCoordinate(double coordinate, int gridSize) {
        return (int) (coordinate / (600f / gridSize));
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
