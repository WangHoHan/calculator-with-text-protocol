import java.net.ServerSocket;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.log;
import static java.lang.Math.pow;


public class Server
{


    private ServerSocket server_socket;
    static public boolean condition=true;
    static public boolean closed=false;





    private Server() {} //konstruktor domyślny klasy Server


    private int generateSessionId()//funkcja generująca id sesji dla klientów
    {
        int session_id;

        Random random = new Random();

        session_id = random.nextInt(1023) + 1;

        return session_id;
    }

    private void startConnection()//metoda inicjująca połączenie serwera z klientem
    {
        try
        {



            int session_id = generateSessionId();//generowanie ID dla klienta

            server_socket = new ServerSocket(1234);

            Client client = new Client(server_socket, session_id,this);//utworzenie obieku CLIENT

            Thread thread = new Thread(client);//utworzenie wątku, który bedzie nasłuchwiał pakiety od klienta

            thread.start();

            Scanner read=new Scanner(System.in);

            int number;
            int choice = 0;

            String temporary="";
            while(true)//pętla,która reaguje na odpowiednie komunikaty wpisywane przez użytkownika
            {




                temporary=read.nextLine();



                if(temporary.equals("!history"))//wyświetlenie historii
                {

                    System.out.println("Choose a way of searching: \n1.SessionID\n2.OperationID\n3.Display all");
                    choice=read.nextInt();
                    if(choice==1)//wyświetlenie historii po konkretnym ID sesji
                    {
                        System.out.println("Type a sessionID");
                        System.out.println("Your session ID = "+session_id);

                        number=read.nextInt();



                        for(History element: client.operation_history)//instrukcja warunkowa, która sprawdza rodzaj operacji w historii do wyświetlenia
                        {
                            if(element.type.equals("AD")&& element.session_ID==number) {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = element.number+element.number1;
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }else if(element.type.equals("SB")&& element.session_ID==number)
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = element.number-element.number1;
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                            else if(element.type.equals("PW")&& element.session_ID==number)
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = pow(element.number,element.number1);
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                            else if(element.type.equals("LG")&& element.session_ID==number)
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = log(element.number1) / log(element.number);
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                        }


                    }
                    else if(choice==2)
                    {

                        System.out.println("Type a operationID");
                        number=read.nextInt();
                        //Wyswietlenie historii dla konkretnego działania

                        for(History element: client.operation_history)//instrukcja warunkowa, która sprawdza rodzaj operacji w historii do wyświetlenia
                        {
                            if(element.type.equals("AD")&& element.operation_ID==number) {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = element.number+element.number1;
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }else if(element.type.equals("SB")&& element.operation_ID==number)
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = element.number-element.number1;
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                            else if(element.type.equals("PW")&& element.operation_ID==number)
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = pow(element.number,element.number1);
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                            else if(element.type.equals("LG")&& element.operation_ID==number)
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = log(element.number1) / log(element.number);
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }}





                    }
                    else if(choice==3)//wyświetlenie całej zapisanej historii
                    {
                        for(History element: client.operation_history)//instrukcja warunkowa, która sprawdza rodzaj operacji w historii do wyświetlenia
                        {
                            if(element.type.equals("AD")) {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = element.number+element.number1;
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }else if(element.type.equals("SB"))
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = element.number-element.number1;
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                            else if(element.type.equals("PW"))
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = pow(element.number,element.number1);
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }
                            else if(element.type.equals("LG"))
                            {
                                System.out.println("Operation: " + element.type);
                                System.out.println("Number1: " + element.number);
                                System.out.println("Number2: " + element.number1);
                                double result = log(element.number1) / log(element.number);
                                System.out.println("Result: " + result);
                                System.out.println("Operation_ID: " + element.operation_ID);
                                System.out.println("Session_ID: " + session_id);
                                System.out.println("TimeStamp: " + element.timeStamp);
                            }


                        }
                    }

                }


            }


        }
        catch (InputMismatchException e )
        {
            System.out.println("Wrong data. Try again." );

        }
        catch(Exception exception) {}

    }

    private void stopConnection()//metoda zamykająca serwer
    {
        try
        {


            server_socket.close();

        }
        catch(Exception exception) {}
    }


    public static void main(String[] args)
    {

        System.setProperty("line.separator", "");
        Server server = new Server();//tworzenie obiektu typu Serwer


        while(condition) {
            server.startConnection();//wystartowanie serwera i łącznie klientów
            server.stopConnection();//wyłączenie serwera
        }

    }
}