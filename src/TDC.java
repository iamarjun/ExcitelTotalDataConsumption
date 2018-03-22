import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;



public class TDC {


    public static void main(String[] args){


        File file = new File("C:/Users/arjun/Downloads/selenium-java-3.11.0/geckodriver.exe");
        System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());


        FirefoxDriver firefoxDriver = new FirefoxDriver();
        firefoxDriver.get("https://my.excitel.com/login?returnUrl=%2F");

        WebElement usernameElement = firefoxDriver.findElement(By.xpath("//*[@id=\"login\"]/div[2]/form/div[1]/input"));
        usernameElement.sendKeys("arjun160");

        WebElement passwordElement = firefoxDriver.findElement(By.xpath("//*[@id=\"login\"]/div[2]/form/div[2]/div[1]/input"));
        passwordElement.sendKeys("123456");

        WebElement loginButton = firefoxDriver.findElement(By.xpath("//*[@id=\"login\"]/div[2]/form/div[3]/button"));

        loginButton.click();


        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if (month == 0){

            year -= 1;
        }

        String monthYear = month + "-" + year;

        firefoxDriver.get("https://my.excitel.com/api/selfcare/public/index.php/subscriber/showSessions?monthId=" + monthYear);


        String pageSource = firefoxDriver.getPageSource();


        int jsonStart = pageSource.indexOf("{");
        int jsonEnd = pageSource.lastIndexOf("}") + 1;

        String JSONData = pageSource.substring(jsonStart, jsonEnd);


        JSONObject jsonObject = new JSONObject(JSONData);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray session = result.getJSONArray("sessions");

        float totalVolume = 0;

        NumberFormat nf = NumberFormat.getInstance();


        for (int i = 0; i < session.length(); i++) {
            JSONObject jsonobject = session.getJSONObject(i);
            String name = jsonobject.getString("usageVolume");

            try {

                totalVolume += nf.parse(name).floatValue();

            } catch (ParseException e) {

                e.printStackTrace();
            }

        }

        System.out.println("Total Data Consumed in GB: " + totalVolume/1000 + " during " + monthYear);

        try {
            Thread.sleep(2500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        firefoxDriver.close();
    }
}