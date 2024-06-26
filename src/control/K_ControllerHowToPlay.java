package control;

import boardifier.control.ControllerAction;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import view.K_DiagramsPane;
import view.K_GameRulesPane;
import view.K_HomeRootPane;
import view.K_View;

public class K_ControllerHowToPlay extends ControllerAction {
    private K_View view;
    private final view.K_HowToPlayPane rootPane;
    private final Stage stage;

    public K_ControllerHowToPlay(Model model, View view, K_Controller control, Stage stage) {
        super(model, view, control);
        this.view = (K_View) view;
        this.stage = stage;

        // get root pane
        this.rootPane = (view.K_HowToPlayPane) this.view.getRootPane();

        this.rootPane.getGameRulesButton().setOnAction(this);
        this.rootPane.getDiagramsButton().setOnAction(this);
        this.rootPane.getBackToHomeButton().setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == rootPane.getBackToHomeButton()) {
            view = new K_View(model, stage, new K_HomeRootPane(rootPane.getWidth(), rootPane.getHeight()));
            control.setControlAction(new K_ControllerMenueAction(model, view, (K_Controller) control, stage));
        }
        else if (event.getSource() == rootPane.getGameRulesButton()) {
            view = new K_View(model, stage, new K_GameRulesPane(rootPane.getWidth(), rootPane.getHeight()));
            control.setControlAction(new K_ControllerGamesRules(model, view, (K_Controller) control, stage));
        }
        else if (event.getSource() == rootPane.getDiagramsButton()) {
            view = new K_View(model, stage, new K_DiagramsPane(rootPane.getWidth(), rootPane.getHeight()));
            control.setControlAction(new K_ControllerDiagrams(model, view, (K_Controller) control, stage));
        }
    }
}
