import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;

public class menu {
    private VBox menuScreen;
    private MenuItem reset;

    menu(){
        Menu file = new Menu("File");
        reset = new MenuItem("Reset (Ctrl-R)");
        MenuItem quit = new MenuItem("Quit (Ctrl-Q)");

        SeparatorMenuItem separator = new SeparatorMenuItem();
        file.getItems().addAll(reset,separator, quit);
        MenuBar menuBar = new MenuBar(file);
        menuScreen = new VBox(menuBar);

    //    menuScreen = new Scene(vBox, 1280, 400);

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

     /*   reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reset();
            }
        });*/

    };

    public VBox getScene(){ return this.menuScreen; }

    public MenuItem getReset(){ return this.reset; }


}
