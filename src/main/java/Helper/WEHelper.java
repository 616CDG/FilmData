package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

/**
 * Created by cdn on 17/6/8.
 */
public class WEHelper {

    public static WebElement getByXPath(String path, WebDriver driver) {
        WebElement element = null;

        try {
            element = driver.findElement(By.xpath(path));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return null;
        }
        return element;
    }

    public static WebElement getBySelector(String path, WebDriver driver) {
        WebElement element = null;

        try {
            element = driver.findElement(By.cssSelector(path));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return null;
        }
        return element;
    }

    public static ArrayList<WebElement> getElements(String before, String end, WebDriver driver) {

        int type = 0;
        if (before.charAt(0) != '/')
            type = 1;
        int i = 1;
        String path = before + i + end;
        ArrayList<WebElement> ret = new ArrayList<WebElement>();
        WebElement element;

        switch (type) {
            case 0:
                while ((element = getByXPath(path, driver)) != null) {
                    ret.add(element);
                    i++;
                    path = before + i + end;
                }
                break;
            case 1:
                while ((element = getBySelector(path, driver)) != null) {
                    ret.add(element);
                    i++;
                    path = before + i + end;
                }
                break;
        }

        return ret;
    }



}
