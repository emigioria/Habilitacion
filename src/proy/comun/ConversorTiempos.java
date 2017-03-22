/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.comun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * Clase encargada de la conversion de fechas
 */
@Service
public class ConversorTiempos {

	/**
	 * Convierte de Date a LocalDate
	 *
	 * @param fecha
	 *            a convertir
	 * @return la fecha convertida a LocalDate
	 */
	public LocalDate getLocalDate(Date fecha) {
		if(fecha == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		return localDate;
	}

	/**
	 * Convierte de LocalDate a Date
	 *
	 * @param fecha
	 *            a convertir
	 * @return la fecha convertida a Date
	 */
	public Date getDate(LocalDate fecha) {
		if(fecha == null){
			return null;
		}
		Instant instant = Instant.from(fecha.atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		return date;
	}

	/**
	 * Convierte de Date a un String con el formato dd/MM/yyyy
	 *
	 * @param fecha
	 *            a convertir
	 * @return la fecha convertida a String dd/MM/yyyy
	 */
	public String diaMesYAnioToString(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(fecha);
	}

	/**
	 * Convierte de Date a un String con el formato HH:mm
	 *
	 * @param fecha
	 *            de la que se va a obtener la hora y minutos
	 * @return un string con la hora y minutos de la fecha que se le pasa
	 */
	public String horaYMinutosToString(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(fecha);
	}

	/**
	 * Convierte de Date a un String con el formato dd/MM/yyyy HH:mm
	 *
	 * @param fecha
	 *            de la que se va a obtener la hora y minutos
	 * @return un string con la hora y minutos de la fecha que se le pasa
	 */
	public String diaMesAnioHoraYMinutosToString(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formatter.format(fecha);
	}

	/**
	 * Convierte de Long que representan milisegundos a un String con el formato horas "hs " mins "ms " segs "ss"
	 *
	 * @param milis
	 *            de la que se va a obtener las horas, minutos y segundos
	 * @return un string con las horas, minutos y segundos de los milisegundos que se le pasa
	 */
	public String milisAHsMsSsConTexto(Long milis) {
		long segs = (milis / 1000) % 60;
		long mins = (milis / 60000) % 60;
		long horas = milis / 3600000;
		return horas + "hs " + mins + "ms " + segs + "ss";
	}

	/**
	 * Convierte de Long que representan milisegundos a un String con el formato HH:mm:ss
	 *
	 * @param milis
	 *            de la que se va a obtener las horas, minutos y segundos
	 * @return un string con las horas, minutos y segundos de los milisegundos que se le pasa
	 */
	public String milisAHsMsSsConDosPuntos(Long milis) {
		long segs = (milis / 1000) % 60;
		long mins = (milis / 60000) % 60;
		long horas = milis / 3600000;
		return (horas < 10 ? "0" : "") + horas + ":" + (mins < 10 ? "0" : "") + mins + ":" + (segs < 10 ? "0" : "") + segs;
	}
}
