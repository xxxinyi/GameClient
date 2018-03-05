/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsdemo;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import simulation.Simulation;

public class PhysicsDemo extends Application {
    
   private Gateway gateway;
   private String handle;
    @Override
    public void start(Stage primaryStage) {
        gateway = new Gateway();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Start Chat");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter a handle:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(handle -> gateway.sendHandle(handle));
        
        GamePane root = new GamePane();
        Simulation sim = new Simulation(300, 250, 2, 2);
        root.setShapes(sim.setUpShapes());
        
        Scene scene = new Scene(root, 300, 250);
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN:
                    gateway.sendControl(handle, 1);
                    break;
                case UP:
                    gateway.sendControl(handle, 2);
                    break;
                case LEFT:
                    gateway.sendControl(handle, 3);
                    break;
                case RIGHT:
                    gateway.sendControl(handle, 4);
                    break;
            }
        });
        root.requestFocus(); 

        primaryStage.setTitle("Game Physics");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((event)->System.exit(0));
        primaryStage.show();
        
        new Thread(() -> {
            while(true){
                int control = 0;
                control = gateway.getControl();
                switch(control){
                    case(1):
                        sim.moveInner(0, 3);
                        break;
                    case(2):
                        sim.moveInner(0, -3);
                        break;
                    case(3):
                        sim.moveInner(-3, 0);
                        break;
                    case(4):
                        sim.moveInner(3, 0);
                        break;
                }
            }
        }).start();

        // This is the main animation thread
        new Thread(() -> {
            while (true) {
                int e = gateway.getEvolve();
                sim.evolve(e);
                Platform.runLater(()->sim.updateShapes());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
