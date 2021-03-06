/*
 * Sentilo
 * 
 * Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de Barcelona.
 * 
 * This program is licensed and may be used, modified and redistributed under the terms of the
 * European Public License (EUPL), either version 1.1 or (at your option) any later version as soon
 * as they are approved by the European Commission.
 * 
 * Alternatively, you may redistribute and/or modify this program under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * 
 * See the licenses for the specific language governing permissions, limitations and more details.
 * 
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along with this program;
 * if not, you may find them at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl http://www.gnu.org/licenses/ and
 * https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.web.catalog.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FormatUtils {

  public static String label(final String message) {
    return String.format("<span class=\"label\">%s</label>", message);
  }

  public static String formatCurrentDate() {
    return formatDateTime(new Date());
  }

  public static String formatDate(final Date date) {
    return formatDate(date, Constants.DATE_FORMAT);
  }

  public static String formatDateTime(final Date date) {
    return formatDate(date, Constants.DATETIME_FORMAT);
  }

  private static String formatDate(final Date date, final String format) {
    if (date != null) {
      final DateFormat df = new SimpleDateFormat(format);
      return df.format(date);
    }
    return null;
  }

  private FormatUtils() {
    // this prevents even the native class from calling this ctor as well :
    throw new AssertionError();
  }
}
