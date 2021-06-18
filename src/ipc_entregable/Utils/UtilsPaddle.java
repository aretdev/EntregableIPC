
package ipc_entregable.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class UtilsPaddle {
    public static String getPistaNumber(String nombrePista) {
        return nombrePista.split(" ")[1];
    }
   
    public static String getFinReserva(LocalTime startTime) {
        return startTime.plusMinutes(90).toString();
    }
    
    public static Boolean superaEldia(LocalDateTime diaDeresrva, LocalDateTime diaActual) {
        return ChronoUnit.DAYS.between(diaDeresrva, diaActual) >= 1; 
    }
    
    public static Boolean yaHanJugado(LocalDate diaCuandoSeJuega, LocalDateTime diaActual) {
        return ChronoUnit.DAYS.between(diaCuandoSeJuega, diaActual) >= 1; 
    }
    
   
}
