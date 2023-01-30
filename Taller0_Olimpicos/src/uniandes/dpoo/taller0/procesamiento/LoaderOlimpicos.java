package uniandes.dpoo.taller0.procesamiento;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.taller0.modelo.Atleta;
import uniandes.dpoo.taller0.modelo.Evento;
import uniandes.dpoo.taller0.modelo.Genero;
import uniandes.dpoo.taller0.modelo.Medalla;
import uniandes.dpoo.taller0.modelo.Pais;
import uniandes.dpoo.taller0.modelo.Participacion;

public class LoaderOlimpicos
{

	public static CalculadoraEstadisticas cargarArchivo(String nombreArchivo) throws FileNotFoundException, IOException
	{
		Map<String, Atleta> atletas = new HashMap<>();
		Map<String, Pais> paises = new HashMap<>();
		List<Evento> eventos = new ArrayList<>();

		// Abrir el archivo y leerlo l�nea por l�nea usando un BufferedReader
		BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
		String linea = br.readLine(); // La primera l�nea del archivo se ignora porque �nicamente tiene los t�tulos de
										// las columnas
		linea = br.readLine();
		while (linea != null) // Cuando se llegue al final del archivo, linea tendr� el valor null
		{
			// Separar los valores que estaban en una l�nea
			String[] partes = linea.split(",");
			String nombreAtleta = partes[0];
			Genero genero = partes[1].equals("m") ? Genero.MASCULINO : Genero.FEMENINO;
			int edad = Integer.parseInt(partes[2]);
			String nombrePais = partes[3];
			int anio = Integer.parseInt(partes[4]);
			String nombreEvento = partes[5];
			Medalla laMedalla = Medalla.valueOf(partes[6].toUpperCase());

			// Si el pa�s no existe, lo agrega a la lista de paises
			Pais elPais = paises.get(nombrePais);
			if (elPais == null)
			{
				elPais = new Pais(nombrePais);
				paises.put(nombrePais, elPais);
			}

			// Si no se hab�a encontrado antes a ese atleta, se agrega como un nuevo atleta.
			// Ac� suponemos que no hay atletas con el mismo nombre
			Atleta elAtleta = atletas.get(nombreAtleta);
			if (elAtleta == null)
			{
				elAtleta = new Atleta(nombreAtleta, genero, elPais);
				elPais.agregarAtleta(elAtleta);
				atletas.put(nombreAtleta, elAtleta);
			}

			// Si no se hab�a encontrado antes este evento, se agrega como uno nuevo.
			// Los eventos se identifican por su nombre y el a�o.
			Evento elEvento = buscarEvento(eventos, nombreEvento, anio);
			if (elEvento == null)
			{
				elEvento = new Evento(nombreEvento, anio);
				eventos.add(elEvento);
			}

			// Registra la participaci�n del atleta en el evento, incluyendo el resultado
			// alcanzado (medalla de oro, plata, bronce o ninguna - na).
			Participacion laParticipacion = new Participacion(elAtleta, edad, elEvento, laMedalla);
			elAtleta.agregarParticipacion(laParticipacion);
			elEvento.agregarParticipacion(laParticipacion);

			linea = br.readLine(); // Leer la siguiente l�nea
		}

		br.close();

		System.out.println(eventos);
		CalculadoraEstadisticas calculadora = new CalculadoraEstadisticas(atletas, paises, eventos);
		return calculadora;
	}

	/**
	 * Busca en la lista de eventos un evento con el nombre y el a�o indicado.
	 * 
	 * Si no se encuentra un evento con esas caracter�sticas, retorna null.
	 * 
	 * @param eventos      Una lista de eventos donde se va a buscar
	 * @param nombreEvento El nombre del evento que se est� buscando
	 * @param anioEvento   El a�o del evento que se est� buscando
	 * @return El evento que se encontraba en la lista, o null.
	 */
	private static Evento buscarEvento(List<Evento> eventos, String nombreEvento, int anioEvento)
	{
		Evento elEvento = null;

		for (int i = eventos.size() - 1; i >= 0 && elEvento == null; i--)
		{
			Evento unEvento = eventos.get(i);
			if (unEvento.darDeporte().equals(nombreEvento) && unEvento.darAnio() == anioEvento)
			{
				elEvento = unEvento;
			}
		}

		return elEvento;
	}

}
