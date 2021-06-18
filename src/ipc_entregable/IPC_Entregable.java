package ipc_entregable;

import DBAcess.ClubDBAccess;
import ipc_entregable.Controllers.LoginControler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class IPC_Entregable extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        /*Creamos la instancia del club*/
        ClubDBAccess clubdb =  ClubDBAccess.getSingletonClubDBAccess();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/login.fxml"));
        Parent root = (Parent) loader.load();
        //Le pasamos a la ventana del login la instancia de este Stage, así iremos usando el mismo.
        loader.<LoginControler>getController().initStage(stage, false);
        
        
       stage.setOnCloseRequest((e)->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(clubdb.getClubName());
            alert.setHeaderText("Guardando datos");
            alert.setContentText("Se están guardando los datos, esto puede llevar unos instantes...");
            alert.show();
            clubdb.saveDB();
        });
        
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
