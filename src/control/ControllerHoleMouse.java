package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.Coord2D;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.HoleBoard;
import model.KamisadoStageModel;
import model.Pawn;
import view.KamisadoBoardLook;

import java.util.List;

/**
 * A mouse controller that handles mouse clicks, selects pawns, and moves them.
 */
public class ControllerHoleMouse extends ControllerMouse implements EventHandler<MouseEvent> {
    private Pawn selectedPawn = null;

    public ControllerHoleMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    @Override
    public void handle(MouseEvent event) {
        if (!model.isCaptureMouseEvent()) return; // Check if mouse event capture is enabled

        Coord2D clickPosition = new Coord2D(event.getSceneX(), event.getSceneY());
        List<GameElement> clickedElements = control.elementsAt(clickPosition);

        KamisadoStageModel stageModel = (KamisadoStageModel) model.getGameStage();
        HoleBoard board = (HoleBoard) stageModel.getBoard();


        if (stageModel.getState() == KamisadoStageModel.STATE_SELECTPAWN && model.getLockedColor() == null) {
            handleSelectPawnState(clickedElements, board, stageModel);
        } else if (stageModel.getState() == KamisadoStageModel.STATE_SELECTDEST){
            handleSelectDestState(clickedElements, board, stageModel, clickPosition);
        }
    }

    private void handleSelectPawnState(List<GameElement> clickedElements, HoleBoard board, KamisadoStageModel stageModel) {
        for (GameElement element : clickedElements) {
            if (element.getType() == ElementTypes.getType("pawn")) {
                if (selectedPawn != null) {
                    selectedPawn.toggleSelected(); // Unselect previously selected pawn
                }
                selectedPawn = (Pawn) element;
                selectedPawn.toggleSelected(); // Select new pawn
                stageModel.setState(KamisadoStageModel.STATE_SELECTDEST);
                board.setValidCells(selectedPawn);
                return;
            }
        }
    }

    private void handleSelectDestState(List<GameElement> clickedElements, HoleBoard board, KamisadoStageModel stageModel, Coord2D clickPosition) {
        for (GameElement element : clickedElements) {
            if (element.isSelected() && stageModel.getLockedColor() == null) {
                resetSelection(board);
                return;
            } else if (element.getType() == ElementTypes.getType("pawn") && stageModel.getLockedColor() == null) {
                handleSelectPawnState(clickedElements, board, stageModel);
                return;
            }
        }

        if (selectedPawn == null) return; // If no pawn is selected, return

        KamisadoBoardLook lookBoard = (KamisadoBoardLook) control.getElementLook(board);
        int[] targetCell = lookBoard.getCellFromSceneLocation(clickPosition);

        if (targetCell != null && board.canReachCell(targetCell[0], targetCell[1])) {
            // Get the color of the board cell
            String color = lookBoard.getColor(targetCell[0], targetCell[1]);
            stageModel.setLockedColor(color);

            performMoveAction(stageModel, targetCell);
        }
    }

    private void resetSelection(HoleBoard board) {
        selectedPawn.toggleSelected(); // Unselect the pawn
        selectedPawn = null;
        board.resetReachableCells(false);
    }

    public void setPawnFromLockedColor(KamisadoStageModel stageModel, HoleBoard board) {
        Pawn[] pawns = null;

        if (stageModel.getCurrentPlayerName().equals("Player X") || stageModel.getCurrentPlayerName().equals("Computer X")) {
            pawns = stageModel.getXPawns();
        } else if (stageModel.getCurrentPlayerName().equals("Player O") || stageModel.getCurrentPlayerName().equals("Computer O")) {
            pawns = stageModel.getOPawns();
        }

        String color = stageModel.getLockedColor();

        assert pawns != null;
        for (Pawn pawn : pawns) {
            if (pawn.getColor().equals(color)) {
                selectedPawn = pawn;
                selectedPawn.toggleSelected();
                stageModel.setState(KamisadoStageModel.STATE_SELECTDEST);
                board.setValidCells(selectedPawn);
                return;
            }
        }

    }

    private void performMoveAction(KamisadoStageModel stageModel, int[] targetCell) {
        ActionList actions = ActionFactory.generateMoveWithinContainer(control, model, selectedPawn, targetCell[0], targetCell[1]);
        actions.setDoEndOfTurn(true);

        resetSelection((HoleBoard) stageModel.getBoard());

        ActionPlayer actionPlayer = new ActionPlayer(model, control, actions);
        actionPlayer.start();
    }
}