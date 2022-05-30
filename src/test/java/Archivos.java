
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Archivos {

	public ArrayList leerArchivo(String ruta) throws IOException {

		String del = ",";
		ArrayList<Persona> lista_per = new ArrayList<Persona>();
		Persona p;
		try {
			BufferedReader br = new BufferedReader(new FileReader(ruta));
			String linea = br.readLine();
			while (null != linea) {
				String[] datos = linea.split(del);

				p = new Persona(datos[0], datos[1], datos[2], Integer.parseInt(datos[3]), datos[4], datos[5], datos[6],
						datos[7]);
				lista_per.add(p);

				linea = br.readLine();
			}

		} catch (Exception e) {
			System.out.println("Error en el metodo leerArchivo: " + e);
		}
		return lista_per;
	}

	public void escribirArchivo(String ruta, Persona per) {

		Scanner sc = new Scanner(System.in);
		File f = new File(ruta);

		try {
			FileWriter fw = new FileWriter(f.getAbsoluteFile(), true); // opción append habilitada

			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(per.getNombre() + "," + per.getAp_paterno() + "," + per.getAp_materno() + ", " + per.getAño_nac() + "," + 
							per.getMes_nac() + "," + per.getDia_nac() + "," + per.getEstado_nac() + "," + per.getSexo() + "," + 
							per.getCURP() + "," + per.getRFC() + "\n");
			bw.close();
		} catch (Exception e) {
			System.out.println("Error en el metodo escribirArchivo: " + e);
		}

	}

}
