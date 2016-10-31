/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.excepciones.PersistenciaException;

public interface TallerService {

	public ArrayList<Maquina> obtenerMaquinas(Filtro filtro) throws PersistenciaException;

	public void guardarMaquina(Maquina maquina) throws PersistenciaException;

	public void actualizarMaquina(Maquina maquina) throws PersistenciaException;

	public void bajaMaquina(Maquina maquina) throws PersistenciaException;

	public ArrayList<Parte> obtenerPartes(Filtro filtro) throws PersistenciaException;

	public void guardarParte(Parte parte) throws PersistenciaException;

	public void actualizarPartes(ArrayList<Parte> partes) throws PersistenciaException;

	public void bajaPartes(ArrayList<Parte> partes) throws PersistenciaException;

	public ArrayList<Pieza> obtenerPiezas(Filtro filtro) throws PersistenciaException;

	public void guardarPieza(Pieza pieza) throws PersistenciaException;

	public void actualizarPiezas(ArrayList<Pieza> piezas) throws PersistenciaException;

	public void bajaPieza(Pieza pieza) throws PersistenciaException;

	public ArrayList<Herramienta> obtenerHerramientas(Filtro filtro) throws PersistenciaException;

	public void guardarHerramienta(Herramienta herramienta) throws PersistenciaException;

	public void actualizarHerramienta(Herramienta herramienta) throws PersistenciaException;

	public void bajaHerramienta(Herramienta herramienta) throws PersistenciaException;

	public ArrayList<Material> obtenerMateriales(Filtro filtro) throws PersistenciaException;

	public void guardarMateriales(ArrayList<Material> materiales) throws PersistenciaException;

	public void actualizarMateriales(ArrayList<Material> materiales) throws PersistenciaException;

	public void bajaMateriales(ArrayList<Material> materiales) throws PersistenciaException;

}
