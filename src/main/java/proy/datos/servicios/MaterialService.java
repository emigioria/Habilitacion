/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Material;
import proy.datos.filtros.Filtro;
import proy.excepciones.PersistenciaException;

public interface MaterialService {

	public ArrayList<Herramienta> obtenerHerramientas(Filtro<Herramienta> filtro) throws PersistenciaException;

	public void guardarHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException;

	public void actualizarHerramienta(Herramienta herramienta) throws PersistenciaException;

	public void bajaHerramienta(Herramienta herramienta) throws PersistenciaException;

	public ArrayList<Material> obtenerMateriales(Filtro<Material> filtro) throws PersistenciaException;

	public void guardarMateriales(ArrayList<Material> materiales) throws PersistenciaException;

	public void actualizarMaterial(Material material) throws PersistenciaException;

	public void bajaMaterial(Material material) throws PersistenciaException;

}
