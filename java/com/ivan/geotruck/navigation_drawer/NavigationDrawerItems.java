package com.ivan.geotruck.navigation_drawer;

/**
 * Objeto que establece las propiedades de cada elemento de la lista dentro de un objeto del tipo Navigation Drawer.
 */
public class NavigationDrawerItems {
    private String titulo;
    private int icono;

    public NavigationDrawerItems(String titulo, int icono) {
        this.titulo = titulo;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
