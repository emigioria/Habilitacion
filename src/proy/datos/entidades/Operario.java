/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@NamedQuery(name = "listarOperarios", query = "SELECT o FROM Operario o WHERE o.estado.nombre = :est ORDER BY o.nombre ASC")
@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "operario_codusuario_fk"))
@Table(name = "operario")
public class Operario extends Usuario {

	public Operario() {
		super();
	}
}
