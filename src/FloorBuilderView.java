import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FloorBuilderView extends GridPane {
    public static final String[]    ROOM_COLORS = {"ORANGE", "YELLOW", "LIGHTGREEN", "DARKGREEN",
                                                   "LIGHTBLUE", "BLUE", "CYAN", "DARKCYAN",
                                                   "PINK", "DARKRED", "PURPLE", "GRAY"};

    private Building    model;

    // window components
    private Button[][]      buttonTiles;
    private RadioButton     walls, exits, roomTiles, selectRoom;
    private Button          overviewButton, colorButton;
    private TextField       summaryField;

    // get methods
    public Button           getButtonTile(int r, int c) { return buttonTiles[r][c]; }
    public RadioButton      getWalls() { return walls; }
    public RadioButton      getExits() { return exits; }
    public RadioButton      getRoomTiles() {return roomTiles; }
    public RadioButton      getSelectRoom() { return selectRoom; }
    public Button           getOverviewButton() { return overviewButton; }
    public Button           getColorButton() { return colorButton; }

    public FloorBuilderView(Building m) {
        model = m;

        setPadding(new Insets(10));

        // add the 3 Label objects: "FLOOR LAYOUT", "SELECT/EDIT" and "FLOOR SUMMARY"
        Label label = new Label("FLOOR LAYOUT");
        setMargin(label, new Insets(0, 0, 10, 0));
        add(label, 0, 0);
        label = new Label("SELECT/EDIT:");
        setMargin(label, new Insets(0, 0, 10, 0));
        add(label, 1, 0);
        label = new Label("FLOOR SUMMARY:");
        setMargin(label, new Insets(0, 0, 10, 0));
        add(label, 0, 7);

        // add the GridPane of Button objects representing the FloorPlan
        GridPane aPane = new GridPane();
        aPane.setHgap(1);
        aPane.setVgap(1);
        buttonTiles = new Button[model.getFloorPlan(0).size()][model.getFloorPlan(0).size()];
        for (int r = 0; r < model.getFloorPlan(0).size(); r++) {
            for (int c = 0; c < model.getFloorPlan(0).size(); c++) {
                Button tile = new Button();
                tile.setMinSize(5, 5);
                tile.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
                buttonTiles[r][c] = tile;
                aPane.add(tile, c, r);
            }
        }
        setMargin(aPane, new Insets(0, 0, 10, 0));
        add(aPane, 0, 1, 1, 6);

        // add the 4 RadioButton objects representing selections
        ToggleGroup selection = new ToggleGroup();

        walls = new RadioButton("Walls");
        walls.setToggleGroup(selection);
        setMargin(walls, new Insets(5, 0, 10, 25));
        add(walls, 1, 2);
        selection.selectToggle(walls);

        exits = new RadioButton("Exits");
        exits.setToggleGroup(selection);
        setMargin(exits, new Insets(5, 0, 10, 25));
        add(exits, 1, 3);

        roomTiles = new RadioButton("Room Tiles");
        roomTiles.setToggleGroup(selection);
        setMargin(roomTiles, new Insets(5, 0, 10, 25));
        add(roomTiles, 1, 4);

        selectRoom = new RadioButton("Select Room");
        selectRoom.setToggleGroup(selection);
        setMargin(selectRoom, new Insets(5, 0, 10, 25));
        add(selectRoom, 1, 5);

        // add the overview and color Button objects
        overviewButton = new Button("Building Overview");
        overviewButton.setMinWidth(160);
        setValignment(overviewButton, VPos.TOP);
        setMargin(overviewButton, new Insets(20, 0, 0, 10));
        add(overviewButton, 1, 6, 2, 1);

        colorButton = new Button();
        colorButton.setMinSize(30, 30);
        colorButton.setDisable(true);
        setMargin(colorButton, new Insets(0, 0, 0, 10));
        add(colorButton, 2, 4);

        // add the summary TextField object
        summaryField = new TextField();
        summaryField.setEditable(false);
        setMargin(summaryField, new Insets(0));
        add(summaryField, 0, 8, 3, 1);

        // specify the size and growth for each column
        ColumnConstraints column0 = new ColumnConstraints(300, 300, Integer.MAX_VALUE);
        ColumnConstraints column1 = new ColumnConstraints(130);
        ColumnConstraints column2 = new ColumnConstraints(40);
        column0.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column0, column1, column2);

        update(0, 0);
    }

    // update the components so that they reflect the contents of the model
    public void update(int plan, int color) {
        // update color of the FloorPlan tiles accordingly
        for (int r = 0; r < model.getFloorPlan(plan).size(); r++) {
            for (int c = 0; c < model.getFloorPlan(plan).size(); c++) {
                if (model.hasExitAt(r, c)) {
                    buttonTiles[r][c].setStyle("-fx-base: RED; -fx-text-fill: WHITE;");
                    buttonTiles[r][c].setText("EXIT");
                }
                else if (model.getFloorPlan(plan).wallAt(r, c)) {
                    buttonTiles[r][c].setStyle("-fx-base: BLACK;");
                    buttonTiles[r][c].setText("");
                }
                else if (model.getFloorPlan(plan).roomAt(r, c) != null) {
                    buttonTiles[r][c].setStyle(String.format("-fx-base: %s;", ROOM_COLORS[model.getFloorPlan(plan).roomAt(r, c).getColorIndex()]));
                    buttonTiles[r][c].setText("");
                }
                else {
                    buttonTiles[r][c].setStyle("-fx-base: WHITE;");
                    buttonTiles[r][c].setText("");
                }
            }
        }

        // enable / disable color Button, and change its color accordingly
        colorButton.setDisable(!roomTiles.isSelected());
        colorButton.setStyle(String.format("-fx-base: %s;", ROOM_COLORS[color]));

        // update summary field
        summaryField.setText(model.getFloorPlan(plan).getName() + " with " + model.getFloorPlan(plan).getNumberOfRooms() + " rooms.");
    }
}
