import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

public class Screenshot {
	
	//prueba Repositorio  

	public String traerFecha() { // Funcion para traerme la fecha
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String fecha = dateFormat.format(date);
		System.out.println(fecha);
		return fecha;
	}

	public String traerHora() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		String hora = formatter.format(calendar.getTime());
		System.out.println(hora);
		return hora;
	}

	public void tomarScreen(WebDriver driver, String rutaDin, int cont) {
		System.out.println("\nEntro al metodo tomarScreen");

		String dia = "";
		if (traerHora().equals("17")) {
			dia = "05pm";
		}
		
		
		File archivo_screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(archivo_screen, new File("./Capturas/CP_" + rutaDin + "/" + traerFecha() + "/" + dia + "/" + cont + ".png"));
		} catch (Exception e) {
			System.out.println("Hubo un error en tomarScreen: " + e);
		}
	}

}
