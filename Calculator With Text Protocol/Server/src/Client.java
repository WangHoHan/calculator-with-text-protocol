import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import static java.lang.Math.log;
import static java.lang.Math.pow;


public class Client implements Runnable
{
    private boolean statement = true;
    private BufferedReader buffered_reader;
    private PrintWriter print_writer;
    private Socket client_socket;
    public ArrayList<History> operation_history=new ArrayList<>();
    public Server server;




    public Client(ServerSocket server_socket, int session_id,Server serverserver)//konstruktor klasy CLIENT
    {
        try
        {

            client_socket = server_socket.accept();
            server=serverserver;

            buffered_reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            print_writer = new PrintWriter(client_socket.getOutputStream(), true);



            System.out.println("Session ID has been generated for the Client: " + session_id + ".");

            System.out.println("Separator:");
            System.out.println(System.getProperty("line.separator"));

            writePacket("IN", "NO", 0.0, 0.0, 0, session_id);//wyslanie pakietu inicjalizacyjnego

            System.out.println("Initialization packet has been sent.");
        }
        catch(Exception exception) {}

    }




    @Override
    public void run() //metoda czytająca pakiety tak długo jak nadchodzą
    {
        try
        {
            while(statement)//warunek odczytywania pakietów
            {
                readPacket();

            }
        }
        catch(Exception exception) {}
    }


    private void decodePacket(String packet)//funkcja dekokdująca pakietu oraz wykomnująca odpowiednie czynności
    {
        double number;
        double number1;
        int operation_id;
        int session_id;
        long time_stamp;

        String[] message_table = packet.split("[$]");//Podzielenie calego pakietu na częsci rozdzielone symbolem $

        Hashtable<String, String> command_split = new Hashtable<>();//Stworzenie tablicy z dwoma Stringami, która przechowywać będzie rodzaj pola oraz jego komunikat np. OP AD

        ArrayList<String> command_list = new ArrayList<>();//Stworzenie tablicy przechowującej przeysłane pola, dzięki której możliwa jest odpowiednia reakcja na przesyłany komunikat



        for(String element : message_table)
        {
            String[] temporary = element.split("[=]");;//Rozdzielenie przesyłanego pakietu

            if(temporary.length == 2)
            {
                command_split.put(temporary[0], temporary[1]);

                command_list.add(temporary[0]);
                command_list.add(temporary[1]);
            }
        }

        number = Double.parseDouble(command_split.get("N1"));//przypisanie odpowiednim zmiennym odebranych danych
        number1 = Double.parseDouble(command_split.get("N2"));
        operation_id = Integer.parseInt(command_split.get("OI"));
        session_id = Integer.parseInt(command_split.get("ID"));
        time_stamp= Long.parseLong(command_split.get("TS"));


        //dodanie elementu do historii
        History temporary1= new History(command_list.get(1),number,number1,operation_id,session_id,time_stamp);


        operation_history.add(temporary1);

        System.out.println("<DECODE> N1: " + number);
        System.out.println("<DECODE> N2: " + number1);
        System.out.println("<DECODE> OI: " + operation_id);
        System.out.println("<DECODE> SI: " + session_id);
        System.out.println("<DECODE> TS: " + time_stamp);


        if(command_list.get(1).equals("AD") && command_list.get(3).equals("WR"))//odebranie pakietu od klienta, który ma wykonac operacje dodawania
        {
            double result;

            result = number + number1;

            writePacket("AD", "RS", result, -1, operation_id, session_id);
        }
        else if(command_list.get(1).equals("EX") && command_list.get(3).equals("NO"))//odebranie pakietu, wyłączający programy
        {
            System.exit(0);

        }
        else if(command_list.get(1).equals("DC") && command_list.get(3).equals("NO"))//odebranie pakietu, iformującego o odłączeniu klienta
        {

            System.out.println("Client "+ session_id+" disconnected! Waiting for another client");



        }
        else if(command_list.get(1).equals("LG") && command_list.get(3).equals("WR"))//odebranie pakietu, z danymi do logarytmowania
        {
            double result;

            result = log(number1)/log(number);

            writePacket("LG", "RS", result, -1, operation_id, session_id);
        }
        else if(command_list.get(1).equals("PW") && command_list.get(3).equals("WR"))//odebranie pakietu z danymi do operacji potęgowania
        {
            double result;

            result = pow(number, number1);

            writePacket("PW", "RS", result, -1, operation_id, session_id);
        }
        else if(command_list.get(1).equals("SB") && command_list.get(3).equals("WR"))//odebranie pakietu z danymi do odejmowania
        {
            double result;

            result = number - number1;

            writePacket("SB", "RS", result, -1, operation_id, session_id);
        }
        else if(command_list.get(1).equals("HS") && command_list.get(3).equals("WH"))//odebranie pakietu z prosbą o przeslanie historii operacji na podstawie ID sesji
        {
            if(session_id==number)
            {
                for(History element: operation_history)
                {
                    if(element.type.equals("AD")||element.type.equals("SB")||element.type.equals("PW")||element.type.equals("LG"))
                        writePacket1("HS",element.type,element.number,element.number1,element.operation_ID,element.session_ID,element.timeStamp);

                }
            }
            else
            {

                writePacket("HS","DC",-1,-1,operation_id,session_id);
            }
        }
        else if(command_list.get(1).equals("HO") && command_list.get(3).equals("WH"))// //odebranie pakietu z prosbą o przeslanie historii operacji na podstawie ID operacji
        {

            for(History element:operation_history)
            {
                if(number==element.operation_ID)
                {
                    writePacket1("HS",element.type,element.number,element.number1,element.operation_ID,element.session_ID,element.timeStamp);
                    return;

                }

            }
            writePacket("HS","DT",-1,-1,operation_id,session_id);


        }
    }

    private String generatePacket(String operation, String status, double number, double number1, int operation_id, int session_id)//funkcja generująca i kodująca pakiet do zadanej postaci
    {
        String packet = "";

        packet += "OP=" + operation + "$";
        packet += "ST=" + status + "$";
        packet += "N1=" + number + "$";
        packet += "N2=" + number1 + "$";
        packet += "OI=" + operation_id + "$";
        packet += "ID=" + session_id + "$";
        packet += "TS=" + (System.currentTimeMillis()/1000) + "$";

        System.out.println("<CODE>: " + packet);

        return packet;
    }
    private String generatePacket1(String operation, String status, double number, double number1, int operation_id, int session_id,long timeStamp)//funkcja generująca i kodująca pakiet do zadanej postaci do wysyłania historii
    {
        String packet = "";

        packet += "OP=" + operation + "$";
        packet += "ST=" + status + "$";
        packet += "N1=" + number + "$";
        packet += "N2=" + number1 + "$";
        packet += "OI=" + operation_id + "$";
        packet += "ID=" + session_id + "$";
        packet += "TS=" + timeStamp + "$";

        System.out.println("<CODE>: " + packet);

        return packet;
    }

    private void readPacket() throws Exception//metoda odczytująca pakiet
    {
        char[] packet =new char[1024];

        int length= buffered_reader.read(packet);
        String packet1=new String(packet);

        String vpacket=packet1.substring(0,length);




        decodePacket(vpacket);
    }

    private void writePacket(String operation, String status, double number, double number1, int operation_id, int session_id)//metoda wysyłająca pakiet
    {


        print_writer.println(generatePacket(operation, status, number, number1, operation_id, session_id));//metoda wysyłająca pakiet z historia
    }
    private void writePacket1(String operation, String status, double number, double number1, int operation_id, int session_id,long timeStamp)
    {


        print_writer.println(generatePacket1(operation, status, number, number1, operation_id, session_id,timeStamp));
    }
}