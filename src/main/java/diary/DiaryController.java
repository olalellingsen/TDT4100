package diary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class DiaryController implements Initializable {

    private Diary diary = new Diary("Diary controller");

    private FileHandlerText textFiles = new FileHandlerText();

    // input fields
    @FXML
    private TextField sessionName, sessionDuration, sessionCategory, goalInputHour, goalInputMin, filename;

    @FXML
    private DatePicker date;

    // output fields
    @FXML
    private Pane statsOutput;

    @FXML
    private ListView<String> sessionListOutput = new ListView<>();

    @FXML
    private ListView<String> categoryDistributionList = new ListView<>();

    @FXML
    private Label title, totalDuration, goalOutput, completion, goalInputException, fileInputException,
            sessionInputException;

    // graphical output
    @FXML
    private PieChart categoryPieChart;

    @FXML
    private ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateDiary();
    }

    @FXML
    public void setGoal() {
        goalInputException.setText("");
        try {
            diary.setGoal(this.goalInputHour.getText(), this.goalInputMin.getText());
        } catch (Exception e) {
            goalInputException.setText(e.getMessage());
        }
        updateStats();
    }

    @FXML
    public void addSession() {
        sessionInputException.setText("");

        try {
            String sessionName = this.sessionName.getText();
            String sessionDuration = this.sessionDuration.getText();
            String categoryName = this.sessionCategory.getText();

            diary.validateSessionInput(sessionName, date.getValue(), sessionDuration);
            diary.addSession(sessionName, categoryName, date.getValue(), Integer.parseInt(sessionDuration));

            updateDiary();
            clearInputFields();

        } catch (Exception e) {
            sessionInputException.setText(e.getMessage());
        }
    }

    @FXML
    public void deleteSession() {
        sessionInputException.setText("");
        try {
            int i = sessionListOutput.getSelectionModel().getSelectedIndex();
            sessionListOutput.getItems().remove(i);
            diary.deleteSession(i);
            updateDiary();

        } catch (Exception e) {
            sessionInputException.setText("Please select a session");
        }
    }

    public void clearInputFields() {
        sessionName.clear();
        sessionDuration.clear();
        sessionCategory.clear();
        date.setValue(null);
    }

    // filhåndtering
    @FXML
    private void saveDiary() {
        title.setText("My Diary: " + filename.getText());
        try {
            textFiles.writeDiaryToFile(filename.getText(), diary);
        } catch (Exception e) {
            fileInputException.setText(e.getMessage());
        }
    }

    @FXML
    private void loadDiary() throws ClassNotFoundException, IOException {
        fileInputException.setText("");
        try {
            diary = textFiles.readFromDiaryFile(filename.getText());
            title.setText("My Diary: " + filename.getText());
            updateDiary();
        } catch (Exception e) {
            fileInputException.setText("File not found:\n" + e.getMessage());
        }
    }

    private void updateDiary() {
        updateSessionList();
        updateCategoryDistribution();
        updateStats();
    }

    @FXML
    private void updateSessionList() {
        sessionListOutput.getItems().clear();
        for (Session session : diary.getSessions()) {
            sessionListOutput.getItems().add(session.toString());
        }
    }

    @FXML
    public void updateCategoryDistribution() {

        // list
        categoryDistributionList.getItems().clear();
        diary.getDistributionStatus().forEach((category, percentage) -> {
            categoryDistributionList.getItems()
                    .add(category.getName() + ": " + Math.round(percentage * 100) + "% ");
        });

        // pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Category category : diary.getCategories()) {
            pieChartData.add(new Data(category.getName(), category.getDuration()));
        }

        pieChartData.forEach(data -> data.nameProperty()
                .bind(Bindings.concat(data.getName(), ": ", data.pieValueProperty().intValue(), "min")));

        categoryPieChart.getData().clear();
        categoryPieChart.getData().addAll(pieChartData);
    }

    @FXML
    private void updateStats() {
        totalDuration.setText("Total duration: " + diary.getMinToHour(diary.getTotalDuration()));

        if (diary.getGoal() == 0) {
            goalOutput.setText("Goal: No goal set");
        } else {
            goalOutput.setText("Goal: " + diary.getMinToHour(diary.getGoal()));
            completion.setText(diary.getCompletionStatus() + "% completed");
            progressBar.setProgress(diary.getCompletionDbl());
        }
    }

    // sorting sessions
    @FXML
    public void sortSessionsByDate() {
        sessionListOutput.getItems().clear();
        diary.setSessionsByDate();
        for (Session session : diary.getSessions()) {
            sessionListOutput.getItems().add(session.toString());
        }
    }

    @FXML
    public void sortSessionsByName() {
        sessionListOutput.getItems().clear();
        diary.setSessionsByName();
        for (Session session : diary.getSessions()) {
            sessionListOutput.getItems().add(session.toString());
        }
    }

    @FXML
    public void sortSessionsByCategoryName() {
        sessionListOutput.getItems().clear();
        diary.setSessionsByCategoryName();
        for (Session session : diary.getSessions()) {
            sessionListOutput.getItems().add(session.toString());
        }
    }

    @FXML
    public void sortSessionsByDuration() {
        sessionListOutput.getItems().clear();
        diary.setSessionsByDuration();
        for (Session session : diary.getSessions()) {
            sessionListOutput.getItems().add(session.toString());
        }
    }
}
