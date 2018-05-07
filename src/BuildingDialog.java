import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BuildingDialog extends Dialog {
    private int roomCount = 0;

    public BuildingDialog(Stage owner, String title, Building model) {
        setTitle(title);

        // set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType);

        // create the labels, fields and button
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // count floors
        int count = 0;
        for (int i = 0; i < model.getFloorPlans().length; i++) {
            if (model.getFloorPlan(i) != null)
                count++;
        }

        TextField floorsField = new TextField("" + count);
        floorsField.setEditable(false);
        floorsField.setPrefWidth(100);

        // count exits
        count = 0;
        for (int i = 0; i < model.getExits().length; i++) {
            if (model.getExit(i) != null)
                count++;
        }

        TextField exitsField = new TextField("" + count);
        exitsField.setEditable(false);
        exitsField.setPrefWidth(100);

        // count rooms
        for (int i = 0; i < model.getFloorPlans().length; i++) {
            if (model.getFloorPlan(i) != null)
                roomCount += model.getFloorPlan(i).getNumberOfRooms();
        }

        TextField roomsField = new TextField("" + roomCount);
        roomsField.setEditable(false);
        roomsField.setPrefWidth(100);

        // count non-wall floor tiles
        count = 0;
        for (int i = 0; i < model.getFloorPlans().length; i++) {
            if (model.getFloorPlan(i) != null) {
                for (int r = 0; r < model.getFloorPlan(i).size(); r++) {
                    for (int c = 0; c < model.getFloorPlan(i).size(); c++) {
                        if (!model.getFloorPlan(i).wallAt(r, c)) {
                            count++;
                        }
                    }
                }
            }
        }

        TextField sizeField = new TextField(count + " Sq. Ft.");
        sizeField.setEditable(false);
        sizeField.setPrefWidth(100);

        Button directoryButton = new Button("Directory Listing");
        directoryButton.setPrefWidth(200);

        grid.add(new Label("Num Floors:"), 0, 0);
        grid.add(floorsField, 1, 0);
        grid.add(new Label("Num Exits:"), 0, 1);
        grid.add(exitsField, 1, 1);
        grid.add(new Label("Num Rooms:"), 0, 2);
        grid.add(roomsField, 1, 2);
        grid.add(new Label("Total Size:"), 0, 3);
        grid.add(sizeField, 1, 3);
        grid.add(directoryButton, 0, 4, 2, 1);
        getDialogPane().setContent(grid);

        // directoryButton event handler
        directoryButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // bring up 'Directory Listing' Dialog
                DirectoryDialog directoryDialog = new DirectoryDialog(owner, "Directory Listing", model, roomCount);
                directoryDialog.show();
            }
        });
    }
}
