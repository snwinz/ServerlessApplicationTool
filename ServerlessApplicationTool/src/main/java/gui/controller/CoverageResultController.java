package gui.controller;

import gui.view.CoverageResultView;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CoverageResultController {


    @FXML
    TextArea coverageResults;



    public void setView(CoverageResultView view) {

    }


    public void setup(String coverage) {
        coverageResults.textProperty().setValue(coverage);
    }


}
