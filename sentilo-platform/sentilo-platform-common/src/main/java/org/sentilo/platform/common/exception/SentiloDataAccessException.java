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
package org.sentilo.platform.common.exception;

import org.springframework.core.NestedRuntimeException;

public class SentiloDataAccessException extends NestedRuntimeException {

  /** Default serial version uid */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for SentiloDataAccessException.
   * 
   * @param msg the detail message
   */
  public SentiloDataAccessException(final String msg) {
    super(msg);
  }

  /**
   * Constructor for SentiloDataAccessException.
   * 
   * @param msg the detail message
   * @param cause the root cause (usually from using a underlying data access API such as JDBC)
   */
  public SentiloDataAccessException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

}
