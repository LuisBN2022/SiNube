
public class Persona {
	
	private String nombre;
	private String ap_paterno;
	private String ap_materno;
	private int año_nac;
	private String mes_nac;
	private String dia_nac;
	private String estado_nac;
	private String sexo;
	private String CURP;
	private String RFC;
	
	public Persona (String nombre, String ap_paterno, String ap_materno, int año_nac,
							String mes_nac, String dia_nac, String estado_nac, String sexo) {
		
		this.nombre = nombre;
		this.ap_paterno = ap_paterno;
		this.ap_materno = ap_materno;
		this.año_nac = año_nac;
		this.mes_nac = mes_nac;
		this.dia_nac = dia_nac;
		this.estado_nac = estado_nac;
		this.sexo = sexo;		
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public String getAp_paterno() {
		return ap_paterno;
	}
	public void setAp_paterno(String ap_paterno) {
		this.ap_paterno = ap_paterno;
	}
	
	
	public String getAp_materno() {
		return ap_materno;
	}
	public void setAp_materno(String ap_materno) {
		this.ap_materno = ap_materno;
	}
	
	
	public int getAño_nac() {
		return año_nac;
	}
	public void setAño_nac(int año_nac) {
		this.año_nac = año_nac;
	}
	
	
	public String getMes_nac() {
		return mes_nac;
	}
	public void setMes_nac(String mes_nac) {
		this.mes_nac = mes_nac;
	}
	
	
	public String getDia_nac() {
		return dia_nac;
	}
	public void setDia_nac(String dia_nac) {
		this.dia_nac = dia_nac;
	}
	
	
	public String getEstado_nac() {
		return estado_nac;
	}
	public void setEstado_nac(String estado_nac) {
		this.estado_nac = estado_nac;
	}
	
	
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getCURP() {
		return CURP;
	}
	public void setCURP(String cURP) {
		CURP = cURP;
	}


	public String getRFC() {
		return RFC;
	}
	public void setRFC(String rFC) {
		RFC = rFC;
	}
	
	
	

}
