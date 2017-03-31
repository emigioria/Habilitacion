/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.filtros.Filtro;
import proy.excepciones.PersistenciaException;

public interface TallerService {

	public ArrayList<Maquina> obtenerMaquinas(Filtro<Maquina> filtro) throws PersistenciaException;

	public void guardarMaquina(Maquina maquina) throws PersistenciaException;

	public void actualizarMaquina(Maquina maquina) throws PersistenciaException;

	public void bajaMaquina(Maquina maquina) throws PersistenciaException;

	public ArrayList<Parte> obtenerPartes(Filtro<Parte> filtro) throws PersistenciaException;

	public void actualizarParte(Parte parte) throws PersistenciaException;

	public void bajaParte(Parte parte) throws PersistenciaException;

	public ArrayList<Pieza> obtenerPiezas(Filtro<Pieza> filtro) throws PersistenciaException;

	public void actualizarPieza(Pieza pieza) throws PersistenciaException;

	public void bajaPieza(Pieza pieza) throws PersistenciaException;
}
