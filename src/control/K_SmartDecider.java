package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.animation.AnimationTypes;
import model.HoleBoard;
import model.KamisadoStageModel;
import model.Pawn;
import model.Tree;
import view.KamisadoBoardLook;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.awt.Point;

public class K_SmartDecider extends Decider {
    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public K_SmartDecider(Model model, Controller controller) {
        super(model, controller);
    }

    public ActionList decide() {
        KamisadoStageModel stage = (KamisadoStageModel) model.getGameStage();
        HoleBoard board = stage.getBoard();
        Tree tree = new Tree();
        Pawn pawn;

        if (stage.getLockedColor() == null) {
            Pawn[] pawns = stage.getXPawns();
            pawn = pawns[loto.nextInt(pawns.length)];
        } else {
            pawn = getPawnFromLockedColor(stage);
        }

        List<Point> valid = board.computeValidCells(pawn);

        for (Point point : valid) {
            int[] to = {point.x, point.y};

            tree.add(loto.nextInt(-5, 20), to);
        }

        int[] to = tree.getMaxTo();

        if (to == null) {
            return null;
        }

        KamisadoBoardLook lookBoard = (KamisadoBoardLook) control.getElementLook(board);

        String color = lookBoard.getColor(to[0], to[1]);
        stage.setLockedColor(color);
        System.out.println("Locked color from to[0] to[1]: " + color.replace("0x", "#"));


        ActionList action = ActionFactory.generateMoveWithinContainer(control, model, pawn, to[1], to[0]);
        action.setDoEndOfTurn(true);

        return action;
    }

    public Pawn getPawnFromLockedColor(KamisadoStageModel stage) {
        Pawn[] pawns;

        if (stage.getCurrentPlayerName().equals(stage.getModel().getPlayers().get(0).getName())) {
            pawns = stage.getXPawns();
        } else {
            pawns = stage.getOPawns();
        }

        String color = stage.getLockedColor();

        assert pawns != null;
        for (Pawn pawn : pawns) {
            if (pawn.getColor().equals(color)) {
                return pawn;
            }
        }

        return null;
    }


    public String toString() {
        return "Smart AI";
    }
}