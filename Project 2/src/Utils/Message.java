package Utils;

import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Message {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    //1 => true
    // 0 -> false
    public boolean sequenceNo;
    public byte id; //starts at 1
    public int checkSum;
    public String packetContent;

    public Message(boolean sequenceNo, byte id, int checkSum, String packetContent){
        this.sequenceNo = sequenceNo;
        this.id = id;
        this.checkSum = checkSum;
        this.packetContent = packetContent;
    }

    public Message(byte[] bytes) {
       setUpMessage(bytes);
    }

    public Message(String message){
        String[] split = message.split(" ");
        byte[] bytes = new byte[split.length];
        try {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = Byte.parseByte(split[i]);
            }
            setUpMessage(bytes);
        }catch(Exception e){
           setUpMessage();
        }
    }

    public  void setUpMessage(){
        this.sequenceNo = false;
        this.id = -1;
        this.checkSum = -1;
        this.packetContent = null;
    }

    public void setUpMessage(byte[] bytes){
        sequenceNo = (bytes[0] == 1);
        id = bytes[1];

        checkSum = bytes[5] & 0xFF | (bytes[4] & 0xFF) << 8 |
                (bytes[3] & 0xFF) << 16 | (bytes[2] & 0xFF) << 24;

        byte[] text = Arrays.copyOfRange(bytes,6,bytes.length);
        packetContent = new String(text, UTF_8);
    }

    public byte[] getByteMessage(){
        byte[] text = packetContent.getBytes(UTF_8);
        byte[] num = ByteBuffer.allocate(4).putInt(checkSum).array();
        int size = 1 + 1 + num.length + text.length;

        byte[] output = new byte[size];

        output[0] = (sequenceNo)?(byte)1:(byte)0;
        output[1] = id;

        for(int i =0; i < num.length; i++){
            output[i+2] = num[i];
        }

        int pos = 2 + num.length;
        for(int i =0; i < text.length; i++){
            output[i+pos] = text[i];
        }
        return output;
    }

    @Override
    public String toString(){
        String output ="";

        output += this.sequenceNo + " || ";
        output += this.id + " || ";
        output += this.checkSum + " || ";
        output += this.packetContent;

        return output;
    }

    public String getByte(){
        byte[] m = getByteMessage();

        String output = "";

        for (byte g :m ) {
            output += g + " ";
        }

        return output.trim();
    }

//    public static void main(String[] args) {
//        System.out.println(true);
//        Message m = new Message(true,(byte)4,555,"Hello");
//        System.out.println(m);
//
//        System.out.println(new Message(m.getByte()));
//    }
}
