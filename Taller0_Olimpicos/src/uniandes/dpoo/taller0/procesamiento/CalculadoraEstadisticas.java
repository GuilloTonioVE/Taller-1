package uniandes.dpoo.taller0.procesamiento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uniandes.dpoo.taller0.modelo.Atleta;
import uniandes.dpoo.taller0.modelo.Evento;
import uniandes.dpoo.taller0.modelo.Genero;
import uniandes.dpoo.taller0.modelo.Pais;

/**
 * Esta es la clase que es capaz de calcular estad?sticas sobre los juegos
 * ol?mpicos. Para calcular las estad?sticas, esta clase agrupa la informaci?n
 * sobre los atletas, pa?ses y eventos, pero no tiene informaci?n sobre las
 * participaciones (eso es responsabilidad de los atletas y de los eventos).
 */
public class CalculadoraEstadisticas
{
	// ************************************************************************
	// Atributos
	// ************************************************************************

	/**
	 * Una lista con todos los atletas. En esta lista los atletas no est?n
	 * repetidos.
	 */
	private List<Atleta> atletas;

	/**
	 * Una lista con todos los pa?ses para los cuales hay al menos un atleta.
	 */
	private List<Pais> paises;

	/**
	 * Una lista con los eventos registrados. En esta lista puede aparecer dos veces
	 * el mismo deporte pero s?lo si corresponde a a?os diferentes.
	 */
	private List<Evento> eventos;

	// ************************************************************************
	// Constructores
	// ************************************************************************

	/**
	 * Construye una calculadora de estad?sticas, guardando la informaci?n
	 * proporcionada sobre atletas, pa?ses y eventos.
	 * 
	 * @param atletas Un mapa con los atletas, donde las llaves son los nombres de
	 *                los atletas y los valores son los atletas.
	 * @param paises  Un mapa con los pa?ses, donde las llaves son los nombres de
	 *                los pa?ses y los valores son los pa?ses.
	 * @param eventos Una lista con los eventos.
	 */
	public CalculadoraEstadisticas(Map<String, Atleta> atletas, Map<String, Pais> paises, List<Evento> eventos)
	{
		this.atletas = new ArrayList<Atleta>(atletas.values());
		this.paises = new ArrayList<Pais>(paises.values());
		this.eventos = eventos;
	}

	// ************************************************************************
	// M?todos
	// ************************************************************************

	/**
	 * Calcula cu?les fueron los atletas que participaron en cada evento para el a?o
	 * indicado
	 * 
	 * @param anio En a?o que se quiere consultar
	 * @return Un mapa donde las llaves son los nombres de los eventos y los valores
	 *         son los atletas que participaron en cada evento
	 */
	public Map<String, List<Atleta>> atletasPorAnio(int anio)
	{
		Map<String, List<Atleta>> resultado = new HashMap<String, List<Atleta>>();

		for (Evento unEvento : eventos)
		{
			if (unEvento.darAnio() == anio)
			{
				List<Atleta> atletasEnEvento = unEvento.darAtletasEnEvento();
				resultado.put(unEvento.darDeporte(), atletasEnEvento);
			}
		}

		return resultado;
	}

	/**
	 * Calcula cu?les fueron las medallas que gan? un atleta en un rango de a?os
	 * 
	 * @param anioInicial  El a?o inicial para el rango
	 * @param anioFinal    El a?o final para el rango
	 * @param nombreAtleta El nombre del atleta
	 * @return Una lista con la informaci?n de las medallas que gan? el atleta. Cada
	 *         registro es un mapa con tres llaves: "evento", que tiene asociado el
	 *         nombre del evento en el que el atleta gan? una medalla; "anio", que
	 *         tiene asociado el a?o en el que se llev? a cabo el evento; y
	 *         "medalla", que tiene asociado el tipo de medalla que gan? el atleta
	 *         ("gold", "silver" o "bronze").
	 * 
	 *         Si el nombre no corresponde al de ning?n atleta, retorna null.
	 */
	public List<Map<String, Object>> medallasEnRango(int anioInicial, int anioFinal, String nombreAtleta)
	{
		List<Map<String, Object>> resultado = new ArrayList<Map<String, Object>>();
		Atleta elAtleta = buscarAtleta(nombreAtleta);
		if (elAtleta != null)
		{
			resultado = elAtleta.contarMedallasEnRango(anioInicial, anioFinal);
		}

		return resultado;
	}

	/**
	 * Compila la informaci?n de los atletas de un pa?s.
	 * 
	 * @param nombrePais El nombre del pa?s de inter?s.
	 * @return Una lista de mapas con la informaci?n de todos los atletas del pa?s.
	 *         Cada registro de un atleta queda en un mapa que tiene tres llaves:
	 *         "evento", que tiene asociado el nombre del evento en el que particip?
	 *         el atleta; "anio", que tiene asociado el a?o en el que el atleta
	 *         particip? en el evento; y "nombre" que tiene asociado el nombre del
	 *         atleta.
	 * 
	 *         Si no se encuentra el pa?s con el nombre indicado, se retorna null.
	 */
	public List<Map<String, Object>> atletasPorPais(String nombrePais)
	{
		List<Map<String, Object>> resultado = null;
		Pais elPais = buscarPais(nombrePais);
		if (elPais != null)
		{
			resultado = new ArrayList<Map<String, Object>>();
			resultado = elPais.consultarAtletas();
		}
		return resultado;
	}
	
	/**
	 * Calcula cu?l es el pa?s con m?s medallistas en los juegos ol?mpicos. Si hay
	 * m?s de un pa?s con la mayor cantidad de medallistas, los encuentra a todos.
	 * 
	 * Este m?todo se basa en la cantidad de medallistas diferentes (atletas que han
	 * ganado medallas) y no en la cantidad de medallas.
	 * 
	 * @return Un mapa que tiene la informaci?n de los pa?ses con m?s medallistas.
	 *         Las llaves en el mapa son los nombres de los pa?ses. Los valores son
	 *         la cantidad de medallistas. Si hay s?lo un pa?s que sea el que m?s
	 *         medallas tenga, el mapa tiene s?lo una llave.
	 */
	public Map<String, Integer> paisConMasMedallistas()
	{
		Map<String, Integer> resultado = new HashMap<String, Integer>();

		int mayorCantidadMedallistas = -1;

		for (Pais unPais : paises)
		{
			int cantidadMedallistasPais = unPais.contarMedallistas();

			if (cantidadMedallistasPais >= mayorCantidadMedallistas)
			{
				if (cantidadMedallistasPais > mayorCantidadMedallistas)
				{
					resultado.clear();
					mayorCantidadMedallistas = cantidadMedallistasPais;
				}
				resultado.put(unPais.darNombre(), mayorCantidadMedallistas);
			}
		}

		return resultado;
	}

	/**
	 * Consulta cu?les son los atletas que han ganado al menos una medalla en el
	 * evento indicado, en cualquier a?o.
	 * 
	 * @param nombreEvento El nombre del evento de inter?s.
	 * @return Una lista con los atletas que han ganado al menos una medalla en el
	 *         evento. Si ning?n atleta ha ganado una medalla en el evento o si el
	 *         nombre no corresponde el de ning?n evento, retorna una lista vac?a.
	 */
	public List<Atleta> medallistasPorEvento(String nombreEvento)
	{
		Set<Atleta> medallistas = new HashSet<Atleta>();

		for (Evento evento : eventos)
		{
			if (evento.darDeporte().equals(nombreEvento))
			{
				List<Atleta> medallistasEvento = evento.darMedallistas();
				medallistas.addAll(medallistasEvento);
			}
		}
		return new ArrayList<>(medallistas);
	}

	/**
	 * Calcula cu?les son los atletas que han ganado m?s medallas que la cantidad
	 * m?nima indicada
	 * 
	 * @param cantidadMinimaMedallas La cantidad de medallas que se quiere usar para
	 *                               filtrar los atletas.
	 * @return Un mapa donde aparece la informaci?n de todos los atletas que han
	 *         ganado m?s que la cantidad m?nima de medallas indicada. En este mapa
	 *         las llaves son los nombres de los atletas y los valores son las
	 *         cantidades de medallas ganadas por el atleta correspondiente.
	 */
	public Map<String, Integer> atletasConMasMedallas(int cantidadMinimaMedallas)
	{
		Map<String, Integer> medallistas = new HashMap<>();
		for (Atleta atleta : atletas)
		{
			int cantidadMedallas = atleta.contarMedallas();
			if (cantidadMedallas > cantidadMinimaMedallas)
				medallistas.put(atleta.darNombre(), cantidadMedallas);
		}

		return medallistas;
	}

	/**
	 * Calcula cu?l o cu?les son los atletas estrella, es decir los que m?s medallas
	 * hayan ganado (independientemente del tipo de medalla).
	 * 
	 * Si hay un atleta que ha ganado m?s medallas que todos los dem?s, el resultado
	 * s?lo tiene un elemento. De lo contrario aparecen todos los atletas que est?n
	 * empatados en el primer lugar.
	 * 
	 * @return Un diccionario donde aparecen los atletas estrella. Por cada atleta
	 *         hay una entrada en el mapa donde la llave es el nombre del atleta y
	 *         el valor es la cantidad de medallas que gan?.
	 */
	public Map<String, Integer> atletaEstrella()
	{
		Map<String, Integer> estrellas = new HashMap<>();
		int mayorCantidad = 0;

		for (Atleta atleta : atletas)
		{
			int cantidadMedallas = atleta.contarMedallas();
			if (cantidadMedallas >= mayorCantidad)
			{
				if (cantidadMedallas > mayorCantidad)
				{
					estrellas.clear();
					mayorCantidad = cantidadMedallas;
				}
				estrellas.put(atleta.darNombre(), cantidadMedallas);
			}
		}

		return estrellas;
	}

	/**
	 * Calcula cu?l ha sido el pa?s con el mejor desempe?o en el evento.
	 * 
	 * El mejor desempe?o se calcula con base en la cantidad de medallas ganadas y
	 * su tipo. Es decir, que el mejor pa?s es aquel que tenga m?s medallas de oro,
	 * en caso de empate con otro pa?s, ser? mejor el que tenga m?s medallas de
	 * plata entre estos, y si el empate persiste, se definir? por el n?mero de
	 * medallas de bronce.
	 * 
	 * Si el empate persiste, en el resultado aparecer? m?s de un pa?s.
	 * 
	 * @param nombreEvento El nombre del evento de inter?s.
	 * @return Un mapa donde las llaves son nombres de pa?ses y los valores son
	 *         arreglos con tres enteros: la cantidad de medallas de oro, la
	 *         cantidad de medallas de plata y la cantidad de medallas de bronce.
	 */
	public Map<String, int[]> mejorPaisEvento(String nombreEvento)
	{
		int[] mejorResultado = { -1, -1, -1 };
		Map<String, int[]> resultado = new HashMap<>();

		for (Pais pais : paises)
		{
			int[] medallasPais = pais.calcularMedallasEvento(nombreEvento);

			if (medallasPais[0] > mejorResultado[0])
			{
				mejorResultado = medallasPais;
				resultado.clear();
				resultado.put(pais.darNombre(), medallasPais);
			}
			else if (medallasPais[0] == mejorResultado[0])
			{
				if (medallasPais[1] > mejorResultado[1])
				{
					mejorResultado = medallasPais;
					resultado.clear();
					resultado.put(pais.darNombre(), medallasPais);
				}
				else if (medallasPais[1] == mejorResultado[1])
				{
					if (medallasPais[2] > mejorResultado[2])
					{
						mejorResultado = medallasPais;
						resultado.clear();
						resultado.put(pais.darNombre(), medallasPais);
					}
					else if (medallasPais[2] == mejorResultado[2])
					{
						resultado.put(pais.darNombre(), medallasPais);
					}
				}
			}

		}

		return resultado;
	}

	/**
	 * Consulta cu?l es el atleta que ha participado en m?s deportes diferentes.
	 * 
	 * Si un atleta ha participado en el mismo deporte en a?os diferentes, s?lo se
	 * cuenta una vez.
	 * 
	 * Si hay m?s de un atleta empatado por el primer lugar, retorna el primero
	 * alfab?ticamente de acuerdo al nombre.
	 * 
	 * @return El Atleta que ha participado en m?s deportes diferentes.
	 */
	public Atleta buscarAtletaTodoterreno()
	{
		Atleta todoterreno = null;
		int mayorCantidadDeportes = -1;

		for (Atleta unAtleta : atletas)
		{
			int cantidadDeportes = unAtleta.contarDeportes();
			if (cantidadDeportes > mayorCantidadDeportes || (cantidadDeportes == mayorCantidadDeportes
					&& unAtleta.darNombre().compareTo(todoterreno.darNombre()) > 0))
			{
				todoterreno = unAtleta;
				mayorCantidadDeportes = cantidadDeportes;
			}
		}

		return todoterreno;
	}

	/**
	 * Consulta cu?les han sido los medallistas de un determinado pa?s y de un
	 * determinado g?nero.
	 * 
	 * @param nombrePais   El nombre del pa?s de inter?s.
	 * @param generoAtleta El g?nero de inter?s.
	 * @return Retorna un mapa donde las llaves son los nombres de los atletas del
	 *         pa?s y del g?nero que han sido medallistas y los valores son una
	 *         lista con informaci?n de sus medallas. La informaci?n de cada medalla
	 *         tambi?n es un mapa que tiene tres llaves: "evento", que tiene
	 *         asociado el nombre del evento; "anio", que tiene asociado el a?o en
	 *         el que el atleta gan? la medalla; y "medalla" que tiene asociado el
	 *         tipo de medalla.
	 */
	public Map<String, List<Map<String, Object>>> medallistasPorNacionGenero(String nombrePais, Genero generoAtleta)
	{
		Map<String, List<Map<String, Object>>> resultado = null;
		Pais elPais = buscarPais(nombrePais);
		if (elPais != null)
		{
			resultado = elPais.consultarMedallistasGenero(generoAtleta);
		}
		return resultado;
	}

	/**
	 * Calcula qu? porcentaje de los atletas ha sido medallista (ha ganado al menos
	 * una medalla).
	 * 
	 * @return Un n?mero entre 0 y 1 que indica el porcentaje de atletas que ha sido
	 *         medallista
	 */
	public double porcentajeMedallistas()
	{
		double cantidadAtletas = atletas.size();
		double cantidadMedallistas = 0;
		for (Atleta unAtleta : atletas)
		{
			if (unAtleta.esMedallista())
				cantidadMedallistas++;
		}
		return cantidadMedallistas / cantidadAtletas;
	}
	
	/**
	 * Retorna el pa?s al que representa el atleta indicado
	 * 
	 * @param nombreAtleta El nombre del atleta que se est? buscando
	 * @return El pa?s con del nombre dado
	 */
	public Pais paisQueRepresentaUnAtleta(String nombreAtleta)
	{
		
		Atleta elAtleta = buscarAtleta(nombreAtleta);
		Pais pais = elAtleta.darPais();
		return pais;
	}
	

	/**
	 * Retorna el pa?s con el nombre indicado
	 * 
	 * @param nombrePais El nombre del pa?s que se est? buscando
	 * @return El pa?s con el nombre dado o null si no se encuentra.
	 */
	private Pais buscarPais(String nombrePais)
	{
		Pais elPais = null;
		for (int i = 0; i < paises.size() && elPais == null; i++)
		{
			if (paises.get(i).darNombre().equals(nombrePais))
				elPais = paises.get(i);
		}
		return elPais;
	}

	/**
	 * Retorna el atleta con el nombre indicado
	 * 
	 * @param nombreAtleta El nombre del atleta que se est? buscando
	 * @return El atleta con el nombre dado o null si no se encuentra.
	 */
	private Atleta buscarAtleta(String nombreAtleta)
	{
		Atleta elAtleta = null;
		for (int i = 0; i < atletas.size() && elAtleta == null; i++)
		{
			if (atletas.get(i).darNombre().equals(nombreAtleta))
				elAtleta = atletas.get(i);
		}
		return elAtleta;
	}

	/**
	 * Retorna una colecci?n con los nombres de los eventos
	 * 
	 * @return Colecci?n con los nombres de los eventos, sin repetir
	 */
	public Collection<String> darNombresDeportes()
	{
		Collection<String> nombres = new HashSet<String>();
		for (Evento evento : eventos)
		{
			nombres.add(evento.darDeporte());
		}

		return nombres;
	}

}
