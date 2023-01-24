package controller;


import org.apache.commons.codec.binary.Base64;
import service.PayInvoiceService;
import service.PayInvoiceServiceImpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection {

    public static void main(String[] args) {

        InputStreamReader inputStream = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();


        HttpURLConnection httpURLConnection;


        URL url;

        {
            try {

                PayInvoiceService payInvoiceService = new PayInvoiceServiceImpl();

                byte[] out = payInvoiceService.toString().getBytes();


                url = new URL("https://test-link.yigim.az/payInvoice");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                String userCredentials = "testlinkusername:testlinkpassword";
                String auth ="Basic " + new String(new Base64().encode(userCredentials.getBytes()));

                httpURLConnection.setRequestProperty("X-Signature",auth);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                httpURLConnection.setConnectTimeout(200);
                httpURLConnection.setReadTimeout(200);

                httpURLConnection.connect();

                try{
                    outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(out);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                int status = httpURLConnection.getResponseCode();

                if (status==HttpURLConnection.HTTP_OK){
                    inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                    bufferedReader = new BufferedReader(inputStream);
                    String line;
                    while ((line=bufferedReader.readLine()) != null){
                        stringBuilder.append(line);
                    }
                }
                System.out.println(status);

                System.out.println(stringBuilder);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try{
                    inputStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
