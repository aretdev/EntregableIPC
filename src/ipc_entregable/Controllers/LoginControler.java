package ipc_entregable.Controllers;

import DBAcess.ClubDBAccess;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Member;

public class LoginControler implements Initializable {

    @FXML
    private TextField login_input;
    @FXML
    private PasswordField contra_input;
    @FXML
    private Button acceder_button;
    @FXML
    private Button registro_button;
    @FXML
    private Button consultpista_button;
    @FXML
    private Text error_msg; 
    @FXML
    private ImageView logo_obj;
    @FXML
    private VBox izqCont;
    @FXML
    private HBox parentHboxLogin;
    @FXML
    private Button consultarpistas_responsive;
    
    private Stage primaryStage;
    private ClubDBAccess clubDB;

    /*@Params : El stage recibido por la ventana anterior, en este caso, el inicilizador de la Aplicación*/
    public void initStage(Stage stage, boolean registrado) {
        primaryStage = stage;
        primaryStage.setTitle("Inicio");
        clubDB = ClubDBAccess.getSingletonClubDBAccess();
        if(registrado) {
            error_msg.fillProperty().setValue(Color.GREEN);
            error_msg.textProperty().set("Te has registrado con éxito!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String css = this.getClass().getResource("/ipc_entregable/style/style.css").toExternalForm();
        parentHboxLogin.getStylesheets().add(css);
        acceder_button.disableProperty().bind(login_input.textProperty().isEmpty().or(contra_input.textProperty().isEmpty()));
        /*Bindings :) y Listeners :D*/
        
        /*Cuando se pulse enter, comprobar acceso*/
        parentHboxLogin.setOnKeyPressed((ke) ->{
            if(ke.getCode() == KeyCode.ENTER &&
                    !contra_input.textProperty().isEmpty().getValue() &&
                    !login_input.textProperty().isEmpty().getValue() ) {
                this.comprobarAcceso(null);
            }
        });
        /*Responsive!*/
        parentHboxLogin.widthProperty().addListener((observable, oldVal, newVal)  ->{
            Double auxnewVal = (Double)newVal / 4;
            logo_obj.fitWidthProperty().setValue((auxnewVal > 230) ? 230 : auxnewVal);
            if((Double) newVal < 600) {
                parentHboxLogin.getChildren().remove(izqCont);
                consultarpistas_responsive.disableProperty().setValue(false);
                consultarpistas_responsive.visibleProperty().setValue(true);
            }else{
                if(!parentHboxLogin.getChildren().contains(izqCont))parentHboxLogin.getChildren().add(0, izqCont);
                consultarpistas_responsive.disableProperty().setValue(true);
                consultarpistas_responsive.visibleProperty().setValue(false);
            }
        });
        /*Logo*/
        parentHboxLogin.heightProperty().addListener((observable, oldVal, newVal)  ->{
            Double auxnewVal = (Double)newVal / 4;
            logo_obj.fitHeightProperty().setValue((auxnewVal > 150) ? 150 : auxnewVal);
        });
        
    }    

    @FXML
    private void comprobarAcceso(ActionEvent event) {
        Member attemptingToLog = clubDB.getMemberByCredentials(login_input.textProperty().getValue(), contra_input.textProperty().getValue());
        if(attemptingToLog == null) {
            contra_input.textProperty().setValue("");
            login_input.textProperty().setValue("");
            error_msg.fillProperty().setValue(Color.RED);
            this.error_msg.textProperty().setValue("Usuario o contraseña incorrectos");
        }else{
            try{
                FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/areasocios.fxml"));
                Parent root = (Parent) miCargador.load();
                miCargador.<AreaSociosController>getController().initStage(primaryStage, attemptingToLog);
                /* 
                 Sistema de animación, lo añadiré?¿
                Node toRemove = this.parentContainerLogin.getChildren().remove(0);
            
                root.translateXProperty().set( parentContainerLogin.getWidth());
                parentContainerLogin.getChildren().add(root);
            
                KeyValue keyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(600), keyValue);
                Timeline timeline = new Timeline(keyFrame);
                timeline.setOnFinished(evt -> {
                    parentContainerLogin.getChildren().remove(toRemove);
                    this.primaryStage.setScene(new Scene(root));
                });
                timeline.play();*/
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch(IOException e){
                
            }
        }
    }

    @FXML
    private void irARegistro(ActionEvent event) {
        try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/registro.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<RegistroController>getController().initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            
        } 
    }

    @FXML
    private void irAConsultPistas(ActionEvent event) {
        try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/pistas.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<PistasController>getController().initStage(primaryStage, null, true);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
                
        }
    }
    
}
