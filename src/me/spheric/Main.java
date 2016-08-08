package me.spheric;

import com.ciscospark.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
	// write your code here

        while(true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.print(e);
            }

            parseMsg();
        }
    }

    private static void parseMsg()
    {
        String rcv = recvMsg();

        if(rcv == null) return;

        StringTokenizer st = new StringTokenizer(rcv);

        if(st.nextToken().equals("glow"))
        {
            String t1 = st.nextToken();
            if(t1.equals("term")) {
                sendMsg(executeShell(st.nextToken()));
            }
            else if(t1.equals("shuttle")) {
               /* try{
                    executeShell("screencapture ~/Desktop/scr.jpg");
                    Thread.sleep(1000);
                }catch (Exception e){
                    return;
                }*/
                sendFile("http://cdn8.openculture.com/wp-content/uploads/2011/07/spaceshuttle.jpg");
            }
            else if(t1.equals("iotd")){
                sendFile("http://lorempixel.com/640/480/");
            }
            else
            {
                sendMsg("invaild input");
            }
        }

        //if (rcv.equals("hello")) sendMsg("world");
    }

    private static String recvMsg()
    {
        String accessToken = "MDllMTM2ZDItMmUyNC00ZTc4LWI3YmUtMTg2ZTVhZjUwMWI2ZWEyZGVmMmQtZWU4";

        //init client
        Spark spark = Spark.builder()
                .baseUrl(URI.create("https://api.ciscospark.com/v1"))
                .accessToken(accessToken)
                .build();

        Room room = new Room();

        room.setId(spark.rooms()
                .iterate()
                .next()
                .getId());

        String a = spark.messages()
                .queryParam("roomId", room.getId())
                .iterate()
                .next()
                .getText();

        System.out.println("last message : " + a);

        return a;
    }

    private static void sendMsg(String msg)
    {
        String accessToken = "MDllMTM2ZDItMmUyNC00ZTc4LWI3YmUtMTg2ZTVhZjUwMWI2ZWEyZGVmMmQtZWU4";

        //init client
        Spark spark = Spark.builder()
                .baseUrl(URI.create("https://api.ciscospark.com/v1"))
                .accessToken(accessToken)
                .build();

        Room room = new Room();

        room.setId(spark.rooms()
                .iterate()
                .next()
                .getId());

        //message post
        Message message = new Message();
        message.setRoomId(room.getId());
        message.setText(msg);
        spark.messages().post(message);
    }

    private static void sendFile(String path)
    {
        String accessToken = "MDllMTM2ZDItMmUyNC00ZTc4LWI3YmUtMTg2ZTVhZjUwMWI2ZWEyZGVmMmQtZWU4";

        //init client
        Spark spark = Spark.builder()
                .baseUrl(URI.create("https://api.ciscospark.com/v1"))
                .accessToken(accessToken)
                .build();

        Room room = new Room();

        room.setId(spark.rooms()
                .iterate()
                .next()
                .getId());

        //message post
        Message message = new Message();
        message.setRoomId(room.getId());
        message.setFile(path);
        spark.messages().post(message);
    }


    private static String executeShell(String cmd)
    {
        StringBuffer output = new StringBuffer();

        Process p;
        try{
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null){
                output.append(line + "\n");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return output.toString();
    }
}
