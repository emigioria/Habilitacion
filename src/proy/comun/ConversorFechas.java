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

public abstract class ConversorFechas {

	public static LocalDate getLocalDate(Date fecha) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		return localDate;
	}

	public static Date getDate(LocalDate fecha) {
		Instant instant = Instant.from(fecha.atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		return date;
	}

	public static String diaMesYAnioToString(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(fecha);
	}
}
