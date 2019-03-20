public class History {//klasa wykorzystywana przy tworzeniu historii operacji

    public String type;
    public double number;
    public double number1;
    public int session_ID;
    public int operation_ID;
    public long timeStamp;


    History(String type,double number,double number1,int operation_ID,int session_ID,long timeStamp)
    {
        this.type=type;
        this.number=number;
        this.number1=number1;
        this.session_ID=session_ID;
        this.operation_ID=operation_ID;
        this.timeStamp=timeStamp;
    }
}