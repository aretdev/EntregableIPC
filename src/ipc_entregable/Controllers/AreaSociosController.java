/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc_entregable.Controllers;

import DBAcess.ClubDBAccess;
import ipc_entregable.Utils.UtilsPaddle;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Booking;
import model.Member;

public class AreaSociosController implements Initializable {

    @FXML
    private HBox user_data_box;
    @FXML
    private Text text_header;
    @FXML
    private Circle pic_login;
    @FXML
    private Label Login_name;
    @FXML
    private HBox area_socios_box;
    private SVGPath close_session_svg;
    @FXML
    private Button reservar_pista;
    @FXML
    private Button mis_reservas;
    @FXML
    private Button consultar_pistas;
    @FXML
    private VBox parentBoxLogged;
    @FXML
    private VBox contentAct;
    @FXML
    private HBox ParentContent;
    
    /*Parte de mis reservas*/
    @FXML
    private VBox contentMisReserv;
    @FXML
    private TableView<Booking> misReservasView;
    @FXML
    private TableColumn<Booking, String> numeroPistaColumn;
    @FXML
    private TableColumn<Booking, String> diaPistaColumn;
    @FXML
    private TableColumn<Booking, String> horaInicioColumn;
    @FXML
    private TableColumn<Booking, String> finReservaColumn;
    @FXML
    private TableColumn<Booking, String> pagadaColumn;
    @FXML
    private Button close_misReservas;
    @FXML
    private Button eliminarReserva;
    @FXML
    private Label reserva_canceled_label;
    
    /*Atributos que no son FXML*/
    private ObservableList<Booking> observableBookingUsuario = FXCollections.observableArrayList();
    private List<Booking> listMisReservas ;
    private Stage primaryStage;
    private Member memberLogged;
    private ClubDBAccess clubDB;

    
    public void initStage(Stage stage, Member member) {
        this.memberLogged = member;
        this.primaryStage = stage;
        this.clubDB = ClubDBAccess.getSingletonClubDBAccess();
        this.Login_name.textProperty().setValue(this.memberLogged.getLogin());
        this.pic_login.setFill((member.getImage() == null) ? new ImagePattern(new Image("/ipc_entregable/imgs/userIcon.png")) : new ImagePattern(memberLogged.getImage()));
        listMisReservas = (ArrayList<Booking>)clubDB.getUserBookings(member.getLogin()).clone();
        Collections.sort(listMisReservas);
        Collections.reverse(listMisReservas);
        //Cada elemento sale por el stream -> limitamos a 10 -> los recogemos y los convertimos a lista
        observableBookingUsuario.addAll(listMisReservas.stream().limit(10).collect(Collectors.toList()));
        
        Tooltip.install(close_session_svg, new Tooltip("Cerrar Sesión!"));
        ParentContent.prefHeightProperty().bind(primaryStage.heightProperty());
        close_misReservas.tooltipProperty().setValue(new Tooltip("Volver al menú"));
            
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String css = this.getClass().getResource("/ipc_entregable/style/style.css").toExternalForm();
        parentBoxLogged.getStylesheets().add(css);
        eliminarReserva.disableProperty().bind(Bindings.size(misReservasView.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
        contentMisReserv.visibleProperty().bind(Bindings.not(contentAct.visibleProperty()));
        contentMisReserv.disableProperty().bind(contentAct.visibleProperty());
        close_misReservas.visibleProperty().bind(contentMisReserv.visibleProperty());
        close_misReservas.disableProperty().bind(contentMisReserv.disableProperty());

        /*Inicio de la table View*/
        
        numeroPistaColumn.setCellValueFactory(c ->  new SimpleStringProperty(UtilsPaddle.getPistaNumber(c.getValue().getCourt().getName())));
        diaPistaColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMadeForDay().toString()));
        horaInicioColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFromTime().toString()));
        finReservaColumn.setCellValueFactory(c -> new SimpleStringProperty(UtilsPaddle.getFinReserva(c.getValue().getFromTime())));
        pagadaColumn.setCellValueFactory(c -> new SimpleStringProperty( (c.getValue().getPaid()) ? "Sí" : "No" ));
        
        pagadaColumn.setCellFactory(v ->  new TableCell<Booking, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item ==  null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setStyle((item.equals("No") ? "-fx-text-fill: red;" : "-fx-text-fill: green;"));
                    setText(item);
                }
            }

        });
        
        misReservasView.setRowFactory(row -> new TableRow<Booking>(){
            @Override
            public void updateItem(Booking item, boolean empty){
                super.updateItem(item, empty);
                if(!empty){
                    disableProperty().setValue( UtilsPaddle.superaEldia(item.getBookingDate(), LocalDateTime.now()) ||
                            UtilsPaddle.yaHanJugado(item.getMadeForDay(), LocalDateTime.now()));
                }
            }
        });
        
        misReservasView.setItems(observableBookingUsuario);
        misReservasView.getSortOrder().clear();
        misReservasView.getSortOrder().add(diaPistaColumn);
    }    

    @FXML
    private void cerrar_sesion(MouseEvent event) {
        try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/login.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<LoginControler>getController().initStage(primaryStage, false);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void eliminarReserva(ActionEvent event) {
        Booking toDelete = misReservasView.getFocusModel().getFocusedItem();
        observableBookingUsuario.remove(toDelete);
        clubDB.getBookings().remove(toDelete);
        misReservasView.getSelectionModel().clearSelection();
        reserva_canceled_label.visibleProperty().setValue(Boolean.TRUE);
        
    }

    @FXML
    private void close_misReservas(ActionEvent event) {
        this.text_header.textProperty().setValue("ÁREA SOCIOS");
        this.contentAct.disableProperty().setValue(Boolean.FALSE);
        this.contentAct.visibleProperty().setValue(Boolean.TRUE);
        reserva_canceled_label.visibleProperty().setValue(Boolean.FALSE);
    }
    
    @FXML
    private void irAmisReservas(ActionEvent event) {
        this.text_header.textProperty().setValue("RESERVAS");
        this.contentAct.disableProperty().setValue(Boolean.TRUE);
        this.contentAct.visibleProperty().setValue(Boolean.FALSE);
    }

    @FXML
    private void irReservarPistas(ActionEvent event) {
        try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/pistas.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<PistasController>getController().initStage(primaryStage, this.memberLogged, false);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
                
        }
    }

    @FXML
    private void irConsultarPistas(ActionEvent event) {
         try{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/ipc_entregable/fxmlDocs/pistas.fxml"));
            Parent root = (Parent) miCargador.load();
            miCargador.<PistasController>getController().initStage(primaryStage, this.memberLogged, true);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
                
        }
    }
    
}
