package bot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;


/**

 * Скрипт отправки SMS на JAVA

 **/
public class Smsq {
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		sendSms("79031234567", "text", "TEST-SMS");
//	}



// Округление до 3 знаков

    public static void roundOut(double number, int scale) {

        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
// Заремил математическое округление
//		return (double) (int) ((tmp - (int) tmp) >= 0.5 ? tmp + 1 : tmp) / pow;
        System.out.println("Результат округления = " + ((double) (int) (tmp) / pow));

    }



    public static void sendSms(String phone, String text, String sender){
        try {
            String name = "freegastello";
            String password = "fksddgs2484";
            String authString = name + ":" + password;
            String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
            URL url = new URL("http","api.smsfeedback.ru",80,"/messages/v2/send/?phone=%2B"+phone+"&text="+ URLEncoder.encode(text, "UTF-8")+"&sender="+sender);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }

            String result = sb.toString();


            System.out.println("*** BEGIN send SMS ***");
            System.out.println(result);
            System.out.println("*** END send SMS ***");


        } catch (MalformedURLException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
}
