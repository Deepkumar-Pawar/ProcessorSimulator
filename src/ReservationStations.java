import java.util.ArrayList;
import java.util.List;

public class ReservationStations {

    public int aluRSLimit = 3;
    public int buRSLimit = 3;
    public int lsuRSLimit = 3;

    public List<Instruction> aluReservationStation;

    public List<Instruction> lsuReservationStation;

    public List<Instruction> buReservationStation;

    public ReservationStations()
    {
        aluReservationStation = new ArrayList<>();
        lsuReservationStation = new ArrayList<>();
        buReservationStation = new ArrayList<>();
    }

}
