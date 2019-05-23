package it.polito.tdp.newufosightings.model;

public class Confine {

	private String stato1;
	private String stato2;
	private int peso;
	
	public Confine(String stato1, String stato2, int peso) {
		super();
		this.stato1 = stato1;
		this.stato2 = stato2;
		this.peso = peso;
	}

	public String getStato1() {
		return stato1;
	}

	public void setStato1(String stato1) {
		this.stato1 = stato1;
	}

	public String getStato2() {
		return stato2;
	}

	public void setStato2(String stato2) {
		this.stato2 = stato2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stato1 == null) ? 0 : stato1.hashCode());
		result = prime * result + ((stato2 == null) ? 0 : stato2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Confine other = (Confine) obj;
		if (stato1 == null) {
			if (other.stato1 != null)
				return false;
		} else if (!stato1.equals(other.stato1))
			return false;
		if (stato2 == null) {
			if (other.stato2 != null)
				return false;
		} else if (!stato2.equals(other.stato2))
			return false;
		return true;
	}
	
	
}
