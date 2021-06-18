package ipc_entregable.Controllers;

import DBAcess.ClubDBAccess;
import java.io.File;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Member;

public class RegistroController implements Initializable {

    @FXML
    private Button registro_button;
    @FXML
    private SVGPath back_button;
    @FXML
    private TextField nombre_input;
    @FXML
    private TextField apellidos_input;
    @FXML
    private TextField tlf_input;
    @FXML
    private TextField login_input;
    @FXML
    private PasswordField pass_input;
    @FXML
    private Button selecc_img_opt;
    @FXML
    private TextField numTarjeta_input;
    @FXML
    private Text error_1;
    @FXML
    private Text error_2;
    @FXML
    private Text error_3;
    @FXML
    private TextField svc_input;
    @FXML
    private Text error_2_2;
    @FXML
    private HBox hboxButtons;
    @FXML
    private VBox izqCont;
    @FXML
    private ImageView logo_obj;
    @FXML
    private HBox parentHboxRegister;
    @FXML
    private Button ir_atras_button;
    
    /*Atributos no FXML*/
    private ClubDBAccess clubDB;
    private Stage primaryStage;
    private FileChooser filechooser = new FileChooser();
    Image perfil;
    @FXML
    private ImageView test_img_view;
    
    /*@Params : El stage recibido por la ventana anterior, en este caso, la pestaña del login*/
    public void initStage(Stage stage) {
        clubDB = ClubDBAccess.getSingletonClubDBAccess();
        primaryStage = stage;
        primaryStage.setTitle("Registrar Socio");
        
        /*Filtros para solo imagen!*/
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Archivos JPG (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.PNG");
        filechooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String css = this.getClass().getResource("/ipc_entregable/style/style.css").toExternalForm();
        parentHboxRegister.getStylesheets().add(css);
        /*Responsive*/
        parentHboxRegister.widthProperty().addListener((observable, oldVal, newVal)  ->{
            Double auxnewVal = (Double)newVal / 4;
            logo_obj.fitWidthProperty().setValue((auxnewVal > 230) ? 230 : auxnewVal);
            if((Double) newVal < 800) {
                parentHboxRegister.getChildren().remove(izqCont);
                if(!hboxButtons.getChildren().contains(ir_atras_button))hboxButtons.getChildren().add(0,ir_atras_button);
                ir_atras_button.disableProperty().setValue(false);
                hboxButtons.spacingProperty().setValue(30);
            }else{
                if(!parentHboxRegister.getChildren().contains(izqCont))parentHboxRegister.getChildren().add(0, izqCont);
                hboxButtons.getChildren().remove(ir_atras_button);
                ir_atras_button.disableProperty().setValue(true);
                hboxButtons.spacingProperty().setValue(0);


            }
        });
        /*Logo*/
        parentHboxRegister.heightProperty().addListener((observable, oldVal, newVal)  ->{
            Double auxnewVal = (Double)newVal / 4;
            logo_obj.fitHeightProperty().setValue((auxnewVal > 150) ? 150 : auxnewVal);
        });
        /*Bind al botón de registro!*/
        registro_button.disableProperty().bind(nombre_input.textProperty().isEmpty().
                or(apellidos_input.textProperty().isEmpty()).
                or(tlf_input.textProperty().isEmpty()).
                or(login_input.textProperty().isEmpty()).
                or(pass_input.textProperty().isEmpty()));
        
        /*Configurar FileChooser para la imagen!*/
        selecc_img_opt.setOnAction((ActionEvent e) ->{              
            File file = filechooser.showOpenDialog(primaryStage);
            if(file != null) {
                perfil = new Image(file.toURI().toString());
                test_img_view.setImage(new Image(file.toURI().toString()));
                String nameAvatar = file.getName();
                selecc_img_opt.textProperty().setValue("Imagen: " + 
                        ((nameAvatar.length() > 10) ? nameAvatar.substring(0,10) + "...": nameAvatar));
            }
        });
        /*NO mas de 16 carácteres en numTarjeta y deben ser numericos!*/
        numTarjeta_input.textProperty().addListener((observable, oldVal, newVal)-> {
            if (!((String) newVal).matches("\\d*")) {
                numTarjeta_input.setText(((String) newVal).replaceAll("[^\\d]", ""));
            }
            if(numTarjeta_input.textProperty().getValue().length() > 16){
                String aux = numTarjeta_input.textProperty().getValue().substring(0, 16);
                numTarjeta_input.textProperty().setValue(aux);}
        });
        
        /*NO mas de 3 carácteres en SVG y deben ser numericos!*/
        svc_input.textProperty().addListener((observable, oldVal, newVal) -> {
            if (!((String) newVal).matches("\\d*")) {
            svc_input.setText(((String) newVal).replaceAll("[^\\d]", ""));
        }
            if(svc_input.textProperty().getValue().length() > 3){
                String aux = svc_input.textProperty().getValue().substring(0, 3);
                svc_input.textProperty().setValue(aux);}
        });
        /*No numeros en el tlf*/
        tlf_input.textProperty().addListener((observable, oldVal, newVal) -> {
            if (!((String) newVal).matches("\\d*")) {
                tlf_input.setText(((String) newVal).replaceAll("[^\\d]", ""));
        }
        });
        /*Aunque en el entregable no lo especifíca , prefiero limitar el 
        tamaño del usuario del login para evitar problemas a la hora de
        mostrarlo por pantalla.*/
        login_input.textProperty().addListener((observable, oldVal, newVal)-> {
            if(login_input.textProperty().getValue().length() > 10){
                String aux = login_input.textProperty().getValue().substring(0, 10);
                login_input.textProperty().setValue(aux);}
        });
        
        login_input.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        
    }    

    @FXML
    private void goRegistro(ActionEvent event) {
        String err1  = "", err2 = "", err2_1 = "", err3 = "";
        Boolean seguir = true;
        /*Básicamente para que quede un registro bonito y con mensajes ordenados */
        
        /*Tarjeta completa? NO es un campo obligatorio si esta a 0 , que no salte error!*/
        if(numTarjeta_input.textProperty().getValue().length() < 16 && numTarjeta_input.textProperty().getValue().length() > 0) {
            numTarjeta_input.textProperty().setValue("");
            err3 += "Número de tarjeta incorrecto ";
            seguir &= false;
        }
        /*svg completo? NO es un campo obligatorio si esta a 0 , que no salte error!*/
        if(svc_input.textProperty().getValue().length() < 3 && svc_input.textProperty().getValue().length() > 0) {
            err3 += (!seguir) ? ("y SVC incorrecto") : "SVC incorrecto";
            svc_input.textProperty().setValue("");
            seguir &= false;
        }
        /*Pass segura?*/
        if(pass_input.textProperty().getValue().length() < 8) {
            err2 += "La contraseña debe tener más de 8 carácteres";
            pass_input.textProperty().setValue("");
            seguir &= false;
        }
        /*Existe ya el login?*/
        if(clubDB.existsLogin(login_input.textProperty().getValue())) {
            if(err2.length() != 0) {
                err2_1 += "Este login ya existe!";
            }else {
                err2 += "Este login ya existe!";
            }
            login_input.textProperty().setValue("");
            seguir &= false;
        }
        /*El nombre contiene numeros?*/
        if(nombre_input.textProperty().getValue().matches(".*\\d.*")) {
            err1 += "El nombre y apellidos no pueden contener números";
            nombre_input.textProperty().setValue("");
            seguir &= false;
        }
        /*El apellido contiene numeros?*/
        if(apellidos_input.textProperty().getValue().matches(".*\\d.*")) {
            err1 += (err1.length() != 0) ? "" : "El nombre y apellidos no pueden contener números";
            apellidos_input.textProperty().setValue("");
            seguir &= false;
        }
        /*Mostramos los errores*/
        this.error_1.textProperty().setValue(err1);
        this.error_2.textProperty().setValue(err2);
        this.error_2_2.textProperty().setValue(err2_1);
        this.error_3.textProperty().setValue(err3);
        
        /*Ahora sí, vemos si se ha registrado con éxito, si es asi, de vuelta al login para poder entrar!*/
        if(seguir) {
            /*
            Antes de pasar a la nueva escena, creo el objeto member y lo guardo en el ArrayList de miembros
            PERO, no hago persistencia de datos!
            */
            Member newMember = new Member();
            
            /*Campos obligatorios*/
            newMember.setName(this.nombre_input.textProperty().getValue());
            newMember.setSurname(this.apellidos_input.textProperty().getValue());
            newMember.setTelephone(this.tlf_input.textProperty().getValue());
            newMember.setLogin(this.login_input.textProperty().getValue());
            newMember.setPassword(this.pass_input.textProperty().getValue());
            
            /*Campos no obligatorios*/
            newMember.setCreditCard(this.numTarjeta_input.textProperty().getValue());
            newMember.setSvc( this.svc_input.textProperty().getValue());
            newMember.setImage(perfil);
            
            /*Añadimos al arrayList*/
            clubDB.getMembers().add(newMember);
        try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/login.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<LoginControler>getController().initStage(primaryStage, true);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
            }
        }
    }

    @FXML
    private void back_clicked(MouseEvent event) {
        try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/login.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<LoginControler>getController().initStage(primaryStage, false);
            /*Node toRemove = this.parentContainer.getChildren().get(0);
            
            root.translateXProperty().set(-1* parentContainer.getWidth());
            parentContainer.getChildren().add(root);
            
            KeyValue keyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.setOnFinished(evt -> {
                parentContainer.getChildren().remove(toRemove);
                this.primaryStage.setScene(new Scene(root));

            });
    timeline.play();*/
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
            }
    }

    
}