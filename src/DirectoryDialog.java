import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

public class DirectoryDialog extends Dialog {
    public DirectoryDialog(Stage owner, String title, Building model, int roomCount) {
        setTitle(title);

        // set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType);

        // create the ListView<String> and button
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        ListView<String> roomList = new ListView<String>();
        roomList.setPrefSize(300, 200);

        String[] rooms = new String[roomCount];
        String[] list;
        roomCount = 0;
        for (int i = 0; i < model.getFloorPlans().length; i++) {
            if (model.getFloorPlan(i) != null) {
                for (int j = 0; j < model.getFloorPlan(i).getNumberOfRooms(); j++) {
                    if (model.getFloorPlan(i).getRooms()[j] != null) {
                        Room room = model.getFloorPlan(i).getRooms()[j];
                        if (!room.getNumber().isEmpty() && !room.getOccupant().isEmpty() && !room.getPosition().isEmpty())
                            rooms[roomCount++] = room.getNumber() + " - " + room.getOccupant() + " (" + room.getPosition() + ")";
                    }
                }
            }
        }

        list = new String[roomCount];
        if (roomCount != 0)
            for (int i = 0; i < roomCount; i++)
                list[i] = rooms[i];
            roomList.setItems(FXCollections.observableArrayList(list));

        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(300);

        grid.add(roomList, 0, 0);
        grid.add(searchButton, 0, 1);
        getDialogPane().setContent(grid);

        // searchButton event handler
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // bring up TextInputDialog
                TextInputDialog input = new TextInputDialog("Sam Pull");
                input.setTitle("Input Required");
                input.setHeaderText(null);
                input.setContentText("Please enter the full name of the person that you are searching for:");

                Optional<String> result = input.showAndWait();
                if (result.isPresent()) {
                    FloorPlan plan = null;
                    Room room = null;
                    boolean found = false;

                    for (int i = 0; i < model.getFloorPlans().length; i++) {
                        if (model.getFloorPlan(i) != null) {
                            for (int j = 0; j < model.getFloorPlan(i).getNumberOfRooms(); j++) {
                                if (model.getFloorPlan(i).getRooms()[j] != null) {
                                    if (model.getFloorPlan(i).getRooms()[j].getOccupant().equals(result.get().trim())) {
                                        plan = model.getFloorPlan(i);
                                        room = plan.getRooms()[j];
                                        found = true;
                                    }
                                }
                            }
                        }
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Search Results");
                    alert.setHeaderText(null);

                    if (found)
                        alert.setContentText(room.getOccupant() + " is our " + room.getPosition() +
                                " and can be located on the " + plan.getName() + " in room " + room.getNumber());
                    else
                        alert.setContentText("That name does not match anyone in our records, please try again.");

                    alert.showAndWait();
                }
            }
        });
    }
}
