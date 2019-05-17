package c.mileset.gateapp.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendSms extends AsyncTask<String, String, String> {

    URL url;
    URLConnection urlConnection;
    String number;
    String message;
    OutputStream outputStream;
    byte[] sss;

    final String uid = "73776170707932353033";
    final String pin = "538c71180d662";
    String sender = "GateApp";
    final int route = 4;
    final int pushid = 1;

    public SendSms(String number, String message) {
        this.number = number;
        this.message = message;
    }

    public int send() {

        int flag = 0;

        try {
            String parameters = "uid=" + uid + "&pin=" + pin + "&sender=" + sender + "&route=" + route + "&mobile=" + number + "&message=" + URLEncoder.encode(message, "UTF-8") + "&pushid=1";
            System.out.println("pa" + parameters);
            sss = parameters.getBytes();

            url = new URL("http://smsalertbox.com/api/sms.php");
            urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content=length", String.valueOf(sss.length));
            outputStream = urlConnection.getOutputStream();
            outputStream.write(sss);
            outputStream.flush();

            String inp;
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((inp = in.readLine()) != null) {
                System.out.println("" + inp);
                try {
                    long l = Long.parseLong(inp);
                    flag = 1;
                } catch (Exception ex) {
                    flag = 2;//sms cannot be sent betwwn 9pm to 9am
                }
            }
            // flag = 1;
        } catch (MalformedURLException ex) {
            Logger.getLogger(SendSms.class.getName()).log(Level.SEVERE, null, ex);
            flag = 0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            flag = 0;
            Logger.getLogger(SendSms.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flag;
    }

    public int send1() {
        int flag = 0;
        try {
            String parameters = "uid=" + uid + "&pin=" + pin + "&sender=" + sender + "&route=" + route + "&mobile=" + number + "&message=" + URLEncoder.encode(message, "UTF-8") + "&pushid=1";
            System.out.println("pa" + parameters);
            sss = parameters.getBytes();

            URL yahoo = new URL("http://smsalertbox.com/api/sms.php?" + parameters);

            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                try {
                    long l = Long.parseLong(inputLine);
                    flag = 1;
                    //            new updateSMS().changeCount();
                } catch (Exception ex) {
                    flag = 2;//sms cannot be sent betwwn 9pm to 9am
                }
            }
            in.close();

        } catch (Exception ex) {
            flag = 0;

            Logger.getLogger(SendSms.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    @Override
    protected String doInBackground(String... strings) {
        send();
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}