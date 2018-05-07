import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class FloorBuilderApp extends Application {
    private Building            model;
    private FloorBuilderView    view;
    private int                 currentRoomColor = 0;
    private int                 currentFloorPlan = 0;

    public void start(Stage primaryStage) {
        model = Building.example();
        view = new FloorBuilderView(model);

        // add a MenuBar with a single menu named Select Floor
        // menu should contain 5 MenuItems, as well as a SeparatorMenuItem
        Menu selectFloor = new Menu("_Select Floor");
        MenuItem floorOption0 = new MenuItem(model.getFloorPlan(5).getName());
        MenuItem floorOption1 = new MenuItem(model.getFloorPlan(3).getName());
        MenuItem floorOption2 = new MenuItem(model.getFloorPlan(2).getName());
        MenuItem floorOption3 = new MenuItem(model.getFloorPlan(1).getName());
        MenuItem floorOption4 = new MenuItem(model.getFloorPlan(0).getName());
        MenuItem floorOption5 = new MenuItem(model.getFloorPlan(4).getName());
        selectFloor.getItems().addAll(floorOption0, new SeparatorMenuItem(), floorOption1, floorOption2, floorOption3, floorOption4, new SeparatorMenuItem(), floorOption5);

        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(selectFloor);

        // define 'menu' event handlers
        floorOption0.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloorPlan = 5;
                view.update(currentFloorPlan, currentRoomColor);
            }
        });
        floorOption1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloorPlan = 3;
                view.update(currentFloorPlan, currentRoomColor);
            }
        });
        floorOption2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloorPlan = 2;
                view.update(currentFloorPlan, currentRoomColor);
            }
        });
        floorOption3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloorPlan = 1;
                view.update(currentFloorPlan, currentRoomColor);
            }
        });
        floorOption4.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloorPlan = 0;
                view.update(currentFloorPlan, currentRoomColor);
            }
        });
        floorOption5.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloorPlan = 4;
                view.update(currentFloorPlan, currentRoomColor);
            }
        });

        // define 'view' event handlers
        view.getColorButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { handleColorButtonPressed(); }
        });
        view.getOverviewButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { handleOverviewButtonPressed(primaryStage); }
        });
        view.getWalls().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { view.update(currentFloorPlan, currentRoomColor); }
        });
        view.getExits().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { view.update(currentFloorPlan, currentRoomColor); }
        });
        view.getRoomTiles().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { view.update(currentFloorPlan, currentRoomColor); }
        });
        view.getSelectRoom().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { view.update(currentFloorPlan, currentRoomColor); }
        });

        for (int r = 0; r < (model.getFloorPlan(currentFloorPlan).size()); r++) {
            for (int c = 0; c < (model.getFloorPlan(currentFloorPlan).size()); c++) {
                view.getButtonTile(r, c).setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) { handleButtonTilePressed(event, primaryStage); }
                });
            }
        }

        VBox aPane = new VBox();
        aPane.getChildren().addAll(menubar, view);

        primaryStage.setTitle("Floor Plan Builder");
        primaryStage.setScene(new Scene(aPane, 490,460));
        primaryStage.setMinWidth(490);
        primaryStage.setMinHeight(482);
        primaryStage.show();
    }

    // colorButton event handler
    private void handleColorButtonPressed() {
        if (currentRoomColor < FloorBuilderView.ROOM_COLORS.length - 1)
            currentRoomColor++;
        else
            currentRoomColor = 0;
        view.update(currentFloorPlan, currentRoomColor);
    }

    // overviewButton event handler
    private void handleOverviewButtonPressed(Stage stage) {
        // bring up 'Building Overview' Dialog
        BuildingDialog buildingDialog = new BuildingDialog(stage, "Building Overview", model);
        buildingDialog.show();
    }

    // tileButton event handler
    private void handleButtonTilePressed(ActionEvent event, Stage stage) {
        Button selectedButton = (Button)event.getSource();

        for (int r = 0; r < (model.getFloorPlan(currentFloorPlan).size()); r++) {
            for (int c = 0; c < (model.getFloorPlan(currentFloorPlan).size()); c++) {
                if (selectedButton == view.getButtonTile(r, c)) {
                    Room room = model.getFloorPlan(currentFloorPlan).roomAt(r, c);
                    // if the walls RadioButton is selected
                    if (view.getWalls().isSelected()) {
                        boolean wall = model.getFloorPlan(currentFloorPlan).wallAt(r, c);
                        model.getFloorPlan(currentFloorPlan).setWallAt(r, c, !wall);
                        if (room != null) {
                            room.removeTile(r, c);
                            if (room.getNumberOfTiles() == 0)
                                model.getFloorPlan(currentFloorPlan).removeRoom(room);
                        }
                        else if (model.hasExitAt(r, c))
                            model.removeExit(r, c);
                    }
                    // if the exits RadioButton is selected
                    else if (view.getExits().isSelected()) {
                        if (!model.hasExitAt(r, c))
                            model.addExit(r, c);
                        else
                            model.removeExit(r, c);
                        if (room != null) {
                            room.removeTile(r, c);
                            if (room.getNumberOfTiles() == 0)
                                model.getFloorPlan(currentFloorPlan).removeRoom(room);
                        }
                    }
                    // if the roomTiles RadioButton is selected
                    else if (view.getRoomTiles().isSelected()) {
                        if (!(model.getFloorPlan(currentFloorPlan).wallAt(r, c) || model.hasExitAt(r, c))) {
                            // tile is not yet part of a Room
                            if (room == null) {
                                if (model.getFloorPlan(currentFloorPlan).roomWithColor(currentRoomColor) != null)
                                    model.getFloorPlan(currentFloorPlan).roomWithColor(currentRoomColor).addTile(r, c);
                                else {
                                    room = model.getFloorPlan(currentFloorPlan).addRoomAt(r, c);
                                    if (room != null)
                                        room.setColorIndex(currentRoomColor);
                                }
                            }
                            else {
                                room.removeTile(r, c);
                                if (room.getNumberOfTiles() == 0)
                                    model.getFloorPlan(currentFloorPlan).removeRoom(room);
                                // tile does NOT match the current room color
                                if (room.getColorIndex() != currentRoomColor) {
                                    if (model.getFloorPlan(currentFloorPlan).roomWithColor(currentRoomColor) != null)
                                        model.getFloorPlan(currentFloorPlan).roomWithColor(currentRoomColor).addTile(r, c);
                                    else {
                                        room = model.getFloorPlan(currentFloorPlan).addRoomAt(r, c);
                                        if (room != null)
                                            room.setColorIndex(currentRoomColor);
                                    }
                                }
                            }
                        }
                    }
                    // if the selectRoom RadioButton is selected
                    else {
                        // if grid location does NOT correspond to a room tile
                        if (model.getFloorPlan(currentFloorPlan).roomAt(r, c) == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Invalid Selection");
                            alert.setHeaderText(null);
                            alert.setContentText("You must select a tile that belongs to a room.");
                            alert.showAndWait();
                        }
                        else {
                            // bring up 'Room Details' Dialog
                            FloorPlan selectedPlan = model.getFloorPlan(currentFloorPlan);
                            Room selectedRoom = selectedPlan.roomAt(r, c);

                            RoomInfoDialog roomDialog = new RoomInfoDialog(stage, "Room Details", selectedRoom, selectedPlan);
                            Optional<String[]> result = roomDialog.showAndWait();

                            if (result.isPresent()) {
                                selectedRoom.setOccupant(result.get()[0]);
                                selectedRoom.setPosition(result.get()[1]);
                                selectedRoom.setNumber(result.get()[2]);
                            }
                        }
                    }
                }
            }
        }
        view.update(currentFloorPlan, currentRoomColor);
    }

    public static void main(String[] args) { launch(args); }
}
