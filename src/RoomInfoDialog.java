import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class RoomInfoDialog extends Dialog {
    public RoomInfoDialog(Stage owner, String title, Room room, FloorPlan plan) {
        setTitle(title);

        // set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // create the labels, fields and button
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        TextField occupantField = new TextField(room.getOccupant());
        occupantField.setPromptText("Person who occupies this room");
        occupantField.setMinWidth(300);

        TextField positionField = new TextField(room.getPosition());
        positionField.setPromptText("Job position/title of this person");
        positionField.setMinWidth(300);

        TextField numberField = new TextField(room.getNumber());
        numberField.setPromptText("The room number");
        numberField.setMinWidth(140);

        TextField floorField = new TextField(plan.getName());
        floorField.setEditable(false);
        floorField.setMinWidth(300);

        TextField sizeField = new TextField(room.getNumberOfTiles() + " Sq. Ft.");
        sizeField.setEditable(false);
        sizeField.setMinWidth(300);

        Button colorButton = new Button();
        colorButton.setStyle(String.format("-fx-base: %s;", FloorBuilderView.ROOM_COLORS[room.getColorIndex()]));
        colorButton.setFocusTraversable(false);
        colorButton.setMinWidth(140);

        grid.add(new Label("Occupant:"), 0, 0);
        grid.add(occupantField, 1, 0, 2, 1);
        grid.add(new Label("Position:"), 0, 1);
        grid.add(positionField, 1, 1, 2, 1);
        grid.add(new Label("Number:"), 0, 2);
        grid.add(numberField, 1, 2);
        grid.add(colorButton, 2, 2);
        grid.add(new Label("Floor:"), 0, 3);
        grid.add(floorField, 1, 3, 2, 1);
        grid.add(new Label("Size:"), 0, 4);
        grid.add(sizeField, 1, 4, 2, 1);
        getDialogPane().setContent(grid);

        // convert the result
        setResultConverter(new Callback<ButtonType, String[]>() {
            public String[] call(ButtonType b) {
                if (b == okButtonType) {
                    String[] str = new String[3];
                    if (occupantField.getText() != null)
                        str[0] = occupantField.getText().trim();
                    if (positionField.getText() != null)
                        str[1] = positionField.getText().trim();
                    if (numberField.getText() != null)
                        str[2] = numberField.getText().trim();
                    return str;
                }
                return null;
            }
        });
    }
}
