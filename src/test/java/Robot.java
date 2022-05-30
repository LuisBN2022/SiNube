
/* Temas: Selenium - TestNG,
    Lectura y escritura de Archivos,
	Cajas de texto, 
	Lista desplegable
	Manejo de calendario,
	Screenshot
*/
import org.testng.annotations.Test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Keys;

import org.testng.annotations.AfterClass;

public class Robot {

	WebDriver driver;
	Archivos archivo;
	Screenshot screen;
	Robot r;

	public By campo = By.xpath("//*[@id=\"igx-input-group-5\"]/div[1]/div/input");

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/Drivers/chromedriver.exe"); // Se setea el
																										// ruta del
																										// Chrome Driver
		driver = new ChromeDriver(); // El driver vacio ahora es un Driver de tipo ChromeDriver
		driver.get("https://www.sinube.mx/calcula-tu-rfc-y-curp");
		// driver.manage().window().maximize();
		Thread.sleep(2000);
	}

	@Test // HappyPath
	public void test_PruebaCompleta() throws InterruptedException, IOException {

		System.out.println(" * * Empieza prueba Completa * *\n\n");

		String rutaDinamica = "PruebaCompleta";
		int cont = 0;

		String rutaArch_Origen = "./Archivos/Sinube_Origen.txt";
		String rutaArch_Destino = "./Archivos/Sinube_Final.txt";
		archivo = new Archivos();
		ArrayList<Persona> lista;
		screen = new Screenshot();

		lista = new ArrayList<Persona>();
		lista = archivo.leerArchivo(rutaArch_Origen);// Llamas la funcion leer archivos y recuperas la lista

		System.out.println("lista ya en robot: " + lista + "\n");

		for (Persona p : lista) {
			cont++;
			driver.navigate().refresh(); // Te actualiza la pagina
			System.out.println(

					"------------------------\n" + "Nombre: " + p.getNombre() + "\nAp pat: " + p.getAp_paterno()
							+ "\nAp mat: " + p.getAp_materno() + "\nAño: " + p.getAño_nac() + "\nMes: " + p.getMes_nac()
							+ "\nDia: " + p.getDia_nac() + "\nEstado: " + p.getEstado_nac() + "\nSexo: " + p.getSexo()
							+ "\n"

			);

			// Código para dar un scroll a la pagina al siguiente frame
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,300)");
			driver.switchTo().frame(1);

			// Nombre
			
			driver.findElement(
					By.xpath("/html/body/app-root/app-calcula-curp/div/div[1]/igx-input-group/div[1]/div/input"))
					.sendKeys(p.getNombre());

			// Apellido Paterno
			driver.findElement(By.xpath("//*[@id=\"igx-input-group-1\"]/div[1]/div/input")).sendKeys(p.getAp_paterno());

			// Apellido Materno
			driver.findElement(By.xpath("//*[@id=\"igx-input-group-2\"]/div[1]/div/input")).sendKeys(p.getAp_materno());

			// Interactuar con Calendario

			driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

			// Abre calendario (año)
			driver.findElement(By.xpath("//*[@id=\"igx-calendar-0\"]/div/div[1]/div[2]/span[2]")).click();
			Thread.sleep(2000);

			By yearWindow = By.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view");
			By currentAñoLocator = By
					.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view/div/div/span[4]");

			String añocurrent = driver.findElement(currentAñoLocator).getText();
			int añonc = Integer.parseInt(añocurrent);

			for (int i = añonc; i >= (p.getAño_nac() + 1); --i) {
				driver.findElement(yearWindow).sendKeys(Keys.ARROW_UP);
				Thread.sleep(10);
			}

			driver.findElement(yearWindow).sendKeys(Keys.ENTER);
			Thread.sleep(1000);

			// Abre calendario (mes)
			WebElement btn_mes = driver.findElement(
					By.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/div/div[1]/div[2]/span[1]"));
			btn_mes.click(); // Abre despliegue de meses
			Thread.sleep(1000);

			WebElement mes_elegido = driver.findElement(By.xpath("//div[contains(text(),' " + p.getMes_nac() + " ')]"));

			Thread.sleep(1000);
			mes_elegido.click(); // Elige mes
			Thread.sleep(1000);

			// Elige día

			driver.findElement(By.xpath("//span[contains(text(),' " + p.getDia_nac() + " ')]")).click();

			driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

			// Lugar de Nacimiento

			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id=\"igx-input-group-5\"]/div[1]/div/input")).sendKeys(p.getEstado_nac());

			// Sexo

			if (p.getSexo().equals("hombre")) {
				driver.findElement(By.id("igx-radio-1")).click();
			} else {
				driver.findElement(By.id("igx-radio-0")).click();
			}

			// Generar CURP y RFC

			String CURP, RFC = "";
			driver.findElement(By.xpath("/html/body/app-root/app-calcula-curp/div/div[5]/button")).click();
			Thread.sleep(1000);

			CURP = driver.findElement(By.xpath("//*[@id=\"igx-input-group-3\"]/div[1]/div/input"))
					.getAttribute("value");
			p.setCURP(CURP);

			RFC = driver.findElement(By.xpath("//*[@id=\"igx-input-group-4\"]/div[1]/div/input")).getAttribute("value");
			p.setRFC(RFC);

			System.out.println("CURP: " + CURP + "\nRFC: " + RFC);

			try {
				archivo.escribirArchivo(rutaArch_Destino, p); // Llama a la función Escribir Archivo
				screen.tomarScreen(driver, rutaDinamica, cont); // Llama a la función tomar Screenshot
			} catch (Exception e) {
				System.out.println(e);
			}

		} // Cierra ForEach

	}

	 @Test // Flujo alterno
	public void test_CamposVacios() throws InterruptedException, IOException {

		System.out.println(" * * Empieza Flujo alterno - Campos Vacios * *\n");

		String rutaDinamica = "CamposVacios";
		int cont = 0;
		
		String rutaArch_Alt = "./Archivos/Sinube_CamposVacios.txt";
		String rutaArch_Destino = "./Archivos/Sinube_Final.txt";
		
		archivo = new Archivos();
		ArrayList<Persona> lista;
		screen = new Screenshot();
		try {

			lista = new ArrayList<Persona>();
			lista = archivo.leerArchivo(rutaArch_Alt);// Llamas la funcion leer archivos y recuperas la lista

			for (Persona p : lista) {
				cont++;
				driver.navigate().refresh(); // Te actualiza la pagina
				System.out.println(

						"\n------------------------\n" + "Nombre: " + p.getNombre() + "\nAp pat: " + p.getAp_paterno()
								+ "\nAp mat: " + p.getAp_materno() + "\nAño: " + p.getAño_nac() + "\nMes: "
								+ p.getMes_nac() + "\nDia: " + p.getDia_nac() + "\nEstado: " + p.getEstado_nac()
								+ "\nSexo: " + p.getSexo() + "\n"

				);

				// Código para dar un scroll a la pagina al siguiente frame
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollBy(0,300)");
				driver.switchTo().frame(1);

				// Nombre
				driver.findElement(
						By.xpath("/html/body/app-root/app-calcula-curp/div/div[1]/igx-input-group/div[1]/div/input"))
						.sendKeys(p.getNombre());

				// Apellido Paterno
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-1\"]/div[1]/div/input"))
						.sendKeys(p.getAp_paterno());

				// Apellido Materno
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-2\"]/div[1]/div/input"))
						.sendKeys(p.getAp_materno());

				String mensajeNombre = "Capture el nombre";
				String mensajeAp_pat = "Capture el apellido paterno";

				if (p.getNombre().equals("")) {
					driver.findElement(By.xpath("/html/body/app-root/app-calcula-curp/div/div[5]/button")).click(); // Click
																													// generar
					Thread.sleep(1000);
					String mensajeObtenido = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[2]"))
							.getText();
					System.out.println("El mensaje es: " + mensajeObtenido);
					assertEquals(mensajeNombre, mensajeObtenido);
					screen.tomarScreen(driver, rutaDinamica, cont); // Llama a la función tomar Screenshot
				}

				if (p.getAp_paterno().equals("")) {
					driver.findElement(By.xpath("/html/body/app-root/app-calcula-curp/div/div[5]/button")).click(); // Click
																													// generar
					Thread.sleep(1000);
					String mensajeObtenido = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[2]"))
							.getText();
					System.out.println("El mensaje es: " + mensajeObtenido);
					assertEquals(mensajeAp_pat, mensajeObtenido);
					screen.tomarScreen(driver, rutaDinamica, cont); // Llama a la función tomar Screenshot
				}

				if (p.getAp_materno().equals("")) {

					// Interactuar con Calendario

					driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

					// Abre calendario (año)
					driver.findElement(By.xpath("//*[@id=\"igx-calendar-0\"]/div/div[1]/div[2]/span[2]")).click();
					Thread.sleep(2000);

					By yearWindow = By
							.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view");
					By currentAñoLocator = By.xpath(
							"/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view/div/div/span[4]");

					String añocurrent = driver.findElement(currentAñoLocator).getText();
					int añonc = Integer.parseInt(añocurrent);

					for (int i = añonc; i >= (p.getAño_nac() + 1); --i) {
						driver.findElement(yearWindow).sendKeys(Keys.ARROW_UP);
						Thread.sleep(10);
					}

					driver.findElement(yearWindow).sendKeys(Keys.ENTER);
					Thread.sleep(1000);

					// Abre calendario (mes)
					WebElement btn_mes = driver.findElement(By.xpath(
							"/html/body/div/div/div/igx-calendar-container/igx-calendar/div/div[1]/div[2]/span[1]"));
					btn_mes.click(); // Abre despliegue de meses
					Thread.sleep(1000);

					WebElement mes_elegido = driver
							.findElement(By.xpath("//div[contains(text(),' " + p.getMes_nac() + " ')]"));

					Thread.sleep(1000);
					mes_elegido.click(); // Elige mes
					Thread.sleep(1000);

					// Elige día

					driver.findElement(By.xpath("//span[contains(text(),' " + p.getDia_nac() + " ')]")).click();

					driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

					// Lugar de Nacimiento

					Thread.sleep(1000);
					driver.findElement(By.xpath("//*[@id=\"igx-input-group-5\"]/div[1]/div/input"))
							.sendKeys(p.getEstado_nac());

					// Sexo

					if (p.getSexo().equals("hombre")) {
						driver.findElement(By.id("igx-radio-1")).click();
					} else {
						driver.findElement(By.id("igx-radio-0")).click();
					}

					// Generar CURP y RFC

					String CURP, RFC = "";
					driver.findElement(By.xpath("/html/body/app-root/app-calcula-curp/div/div[5]/button")).click();
					Thread.sleep(1000);

					CURP = driver.findElement(By.xpath("//*[@id=\"igx-input-group-3\"]/div[1]/div/input"))
							.getAttribute("value");
					p.setCURP(CURP);

					RFC = driver.findElement(By.xpath("//*[@id=\"igx-input-group-4\"]/div[1]/div/input"))
							.getAttribute("value");
					p.setRFC(RFC);

					System.out.println("CURP: " + CURP + "\nRFC: " + RFC);

					try {
						archivo.escribirArchivo(rutaArch_Destino, p); // Llama a la función Escribir Archivo
						screen.tomarScreen(driver, rutaDinamica, cont); // Llama a la función tomar Screenshot
					} catch (Exception e) {
						System.out.println(e);
					}

				}

			} // Cierra ForEach

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Test // Flujo alterno
	public void test_CamposIncorrectos() throws InterruptedException, IOException {

		System.out.println(" * * Empieza Flujo alterno - Campos incorrectos * *\n\n");

		String rutaDinamica = "CamposIncorrectos";
		int cont = 0;
		
		String rutaArch_Origen = "./Archivos/Sinube_CamposIncorrectos.txt";
		String rutaArch_Destino = "./Archivos/Sinube_Final.txt";
		archivo = new Archivos();
		ArrayList<Persona> lista;
		screen = new Screenshot();
		try {

			lista = new ArrayList<Persona>();
			lista = archivo.leerArchivo(rutaArch_Origen);// Llamas la funcion leer archivos y recuperas la lista

			System.out.println("lista ya en robot: " + lista + "\n");

			for (Persona p : lista) {
				cont++;
				driver.navigate().refresh(); // Te actualiza la pagina
				System.out.println(

						"------------------------\n" + "Nombre: " + p.getNombre() + "\nAp pat: " + p.getAp_paterno()
								+ "\nAp mat: " + p.getAp_materno() + "\nAño: " + p.getAño_nac() + "\nMes: "
								+ p.getMes_nac() + "\nDia: " + p.getDia_nac() + "\nEstado: " + p.getEstado_nac()
								+ "\nSexo: " + p.getSexo() + "\n"

				);

				// Código para dar un scroll a la pagina al siguiente frame
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollBy(0,300)");
				driver.switchTo().frame(1);

				// Nombre
				driver.findElement(
						By.xpath("/html/body/app-root/app-calcula-curp/div/div[1]/igx-input-group/div[1]/div/input"))
						.sendKeys(p.getNombre());

				// Apellido Paterno
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-1\"]/div[1]/div/input"))
						.sendKeys(p.getAp_paterno());

				// Apellido Materno
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-2\"]/div[1]/div/input"))
						.sendKeys(p.getAp_materno());

				// Interactuar con Calendario

				driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

				// Abre calendario (año)
				driver.findElement(By.xpath("//*[@id=\"igx-calendar-0\"]/div/div[1]/div[2]/span[2]")).click();
				Thread.sleep(2000);

				By yearWindow = By.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view");
				By currentAñoLocator = By.xpath(
						"/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view/div/div/span[4]");

				String añocurrent = driver.findElement(currentAñoLocator).getText();
				int añonc = Integer.parseInt(añocurrent);

				for (int i = añonc; i >= (p.getAño_nac() + 1); --i) {
					driver.findElement(yearWindow).sendKeys(Keys.ARROW_UP);
					Thread.sleep(10);
				}

				driver.findElement(yearWindow).sendKeys(Keys.ENTER);
				Thread.sleep(1000);

				// Abre calendario (mes)
				WebElement btn_mes = driver.findElement(By
						.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/div/div[1]/div[2]/span[1]"));
				btn_mes.click(); // Abre despliegue de meses
				Thread.sleep(1000);

				WebElement mes_elegido = driver
						.findElement(By.xpath("//div[contains(text(),' " + p.getMes_nac() + " ')]"));

				Thread.sleep(1000);
				mes_elegido.click(); // Elige mes
				Thread.sleep(1000);

				// Elige día

				driver.findElement(By.xpath("//span[contains(text(),' " + p.getDia_nac() + " ')]")).click();

				driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

				// Lugar de Nacimiento

				Thread.sleep(1000);
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-5\"]/div[1]/div/input"))
						.sendKeys(p.getEstado_nac());

				// Sexo

				if (p.getSexo().equals("hombre")) {
					driver.findElement(By.id("igx-radio-1")).click();
				} else {
					driver.findElement(By.id("igx-radio-0")).click();
				}

				// Generar CURP y RFC

				String CURP, RFC = "";
				driver.findElement(By.xpath("/html/body/app-root/app-calcula-curp/div/div[5]/button")).click();
				Thread.sleep(1000);

				CURP = driver.findElement(By.xpath("//*[@id=\"igx-input-group-3\"]/div[1]/div/input"))
						.getAttribute("value");
				p.setCURP(CURP);

				RFC = driver.findElement(By.xpath("//*[@id=\"igx-input-group-4\"]/div[1]/div/input"))
						.getAttribute("value");
				p.setRFC(RFC);

				System.out.println("CURP: " + CURP + "\nRFC: " + RFC);

				screen.tomarScreen(driver, rutaDinamica, cont); // Llama a la función tomar Screenshot

			} // Cierra ForEach

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

	}

	@Test // Flujo alterno
	public void test_FechaIncorrecta() throws InterruptedException, IOException {

		System.out.println(" * * Empieza Flujo alterno - Campos incorrectos * *\n\n");

		String rutaDinamica = "FechaIncorrecta";
		int cont = 0;
		
		String rutaArch_Origen = "./Archivos/Sinube_FechaIncorrecta.txt";
		String rutaArch_Destino = "./Archivos/Sinube_Final.txt";
		archivo = new Archivos();
		ArrayList<Persona> lista;
		screen = new Screenshot();
		
		try {
	
			lista = new ArrayList<Persona>();
			lista = archivo.leerArchivo(rutaArch_Origen);// Llamas la funcion leer archivos y recuperas la lista

			System.out.println("lista ya en robot: " + lista + "\n");

			for (Persona p : lista) {
				cont++;
				driver.navigate().refresh(); // Te actualiza la pagina
				System.out.println(

						"------------------------\n" + "Nombre: " + p.getNombre() + "\nAp pat: " + p.getAp_paterno()
								+ "\nAp mat: " + p.getAp_materno() + "\nAño: " + p.getAño_nac() + "\nMes: "
								+ p.getMes_nac() + "\nDia: " + p.getDia_nac() + "\nEstado: " + p.getEstado_nac()
								+ "\nSexo: " + p.getSexo() + "\n"

				);

				// Código para dar un scroll a la pagina al siguiente frame
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollBy(0,300)");
				driver.switchTo().frame(1);

				// Nombre
				driver.findElement(
						By.xpath("/html/body/app-root/app-calcula-curp/div/div[1]/igx-input-group/div[1]/div/input"))
						.sendKeys(p.getNombre());

				// Apellido Paterno
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-1\"]/div[1]/div/input"))
						.sendKeys(p.getAp_paterno());

				// Apellido Materno
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-2\"]/div[1]/div/input"))
						.sendKeys(p.getAp_materno());

				// Interactuar con Calendario

				driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

				driver.findElement(By.xpath("//*[@id=\"igx-calendar-0\"]/div/div[1]/div[2]/span[2]")).click(); // Abre
																												// calendario
																												// (año)
				Thread.sleep(2000);

				By yearWindow = By.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view");
				By currentAñoLocator = By.xpath(
						"/html/body/div/div/div/igx-calendar-container/igx-calendar/igx-years-view/div/div/span[4]");

				String añocurrent = driver.findElement(currentAñoLocator).getText();
				int añoActual = Integer.parseInt(añocurrent);

				if (p.getAño_nac() < añoActual) {
					System.out.println("El año elegido es menor que el actual");

					for (int i = añoActual; i >= (p.getAño_nac() + 1); --i) {
						driver.findElement(yearWindow).sendKeys(Keys.ARROW_UP);
						Thread.sleep(10);
					}
				} else {
					System.out.println("El año elegido es mayor que el actual");
				}

				driver.findElement(yearWindow).sendKeys(Keys.ENTER);
				Thread.sleep(1000);
				// Abre calendario (mes)
				WebElement btn_mes = driver.findElement(By
						.xpath("/html/body/div/div/div/igx-calendar-container/igx-calendar/div/div[1]/div[2]/span[1]"));
				btn_mes.click(); // Abre despliegue de meses
				Thread.sleep(1000);

				WebElement mes_elegido = driver
						.findElement(By.xpath("//div[contains(text(),' " + p.getMes_nac() + " ')]"));

				Thread.sleep(1000);
				mes_elegido.click(); // Elige mes
				Thread.sleep(1000);

				// Elige día

				driver.findElement(By.xpath("//span[contains(text(),' " + p.getDia_nac() + " ')]")).click();

				driver.findElement(By.id("igx-icon-0")).click();// Click al calendario

				// Lugar de Nacimiento

				Thread.sleep(1000);
				driver.findElement(By.xpath("//*[@id=\"igx-input-group-5\"]/div[1]/div/input"))
						.sendKeys(p.getEstado_nac());

				// Sexo

				if (p.getSexo().equals("hombre")) {
					driver.findElement(By.id("igx-radio-1")).click();
				} else {
					driver.findElement(By.id("igx-radio-0")).click();
				}

				// Generar CURP y RFC

				String CURP, RFC = "";
				driver.findElement(By.xpath("/html/body/app-root/app-calcula-curp/div/div[5]/button")).click();
				Thread.sleep(1000);

				CURP = driver.findElement(By.xpath("//*[@id=\"igx-input-group-3\"]/div[1]/div/input"))
						.getAttribute("value");
				p.setCURP(CURP);

				RFC = driver.findElement(By.xpath("//*[@id=\"igx-input-group-4\"]/div[1]/div/input"))
						.getAttribute("value");
				p.setRFC(RFC);

				System.out.println("CURP: " + CURP + "\nRFC: " + RFC);

				screen.tomarScreen(driver, rutaDinamica, cont); // Llama a la función tomar Screenshot

			} // Cierra ForEach

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

	}

	@AfterClass
	public void afterClass() {
		System.out.println("\n\n\n* * Se acaba prueba * *\n\n");
		driver.close();
	}

}
