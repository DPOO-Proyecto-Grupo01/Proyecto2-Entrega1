package Actividades;

import java.util.List;

public class Tarea extends Actividades {
	
	public static final String TAREA = "Tarea";
	private String estado;
	private String Instrucciones;
	private List<String> preguntas;
	
	public Tarea(String actividadID, String descripcion, String objetivo, int nivelDificultad, int duracionEsperada,
			boolean esObligatoria, int fechaLimite, String resenas, double calificacion, int resultado, String estado, String Instrucciones) {
		super(actividadID, descripcion, objetivo, nivelDificultad, duracionEsperada, esObligatoria, fechaLimite, resenas,
				calificacion, resultado);
		this.estado = estado;
		this.Instrucciones = Instrucciones;

	}
	
	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getObjetivo() {
		return objetivo;
	}



	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}



	public int getDuracionEsperada() {
		return duracionEsperada;
	}



	public void setDuracionEsperada(int duracionEsperada) {
		this.duracionEsperada = duracionEsperada;
	}



	public int getFechaLimite() {
		return fechaLimite;
	}



	public void setFechaLimite(int fechaLimite) {
		this.fechaLimite = fechaLimite;
	}



	public String getActividadID() {
		return actividadID;
	}



	public int getNivelDificultad() {
		return nivelDificultad;
	}



	public boolean isEsObligatoria() {
		return esObligatoria;
	}



	public String getResenas() {
		return resenas;
	}



	public double getCalificacion() {
		return calificacion;
	}



	public int getResultado() {
		return resultado;
	}
	

	@Override
	public String getTipoActividad() {
		return TAREA;
	}

	@Override
	public void agregarContenido(String pregunta) {
		this.preguntas.add(pregunta);
	}

	public String getInstrucciones() {
		return Instrucciones;
	}
	
	public void setInstrucciones(String Instrucciones) {
		this.Instrucciones = Instrucciones;
	}
	
	public String registrarEntrega(String medioEntrega) {
		return medioEntrega; 
		
	}
	
	@Override
	public String EstadoActual(String estado) {
		this.estado = estado;
		return estado;
	}
	
	

}
