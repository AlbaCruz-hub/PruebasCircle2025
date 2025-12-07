package com.calidad.unittest.dependencia;

public class SubDependency {

	public String getClassName() {
		return this.getClass().getSimpleName();
	}

	public void suma(int num){
		System.out.println("Suma: " + (num + 2));
	}

	public Integer addTwo(Integer i) {
		return i + 2;
	}
}
