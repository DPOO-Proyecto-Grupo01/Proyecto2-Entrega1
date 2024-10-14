package Actividades;

import java.util.List;

public abstract class Actividades {

	protected String actividadID;
    protected String descripcion;
    protected String objetivo;
    protected int nivelDificultad;
    protected int duracionEsperada;
    protected boolean esObligatoria;
    protected int fechaLimite;
    protected String resenas;
    protected double calificacion;
    protected int resultado;
    protected String preguntas;
    protected List<String> respuestasUsuario;
    
	public Actividades(String actividadID, String descripcion, String objetivo, int nivelDificultad,
			int duracionEsperada, boolean esObligatoria, int fechaLimite, String resenas, double calificacion,
			int resultado) {
		this.actividadID = actividadID;
		this.descripcion = descripcion;
		this.objetivo = objetivo;
		this.nivelDificultad = nivelDificultad;
		this.duracionEsperada = duracionEsperada;
		this.esObligatoria = esObligatoria;
		this.fechaLimite = fechaLimite;
		this.resenas = resenas;
		this.calificacion = calificacion;
		this.resultado = resultado;
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


	public abstract String getTipoActividad();
	
	public abstract void agregarContenido(String pregunta);

	public abstract String EstadoActual(String estado);
		
	
	
	

	
	
	
	
}
