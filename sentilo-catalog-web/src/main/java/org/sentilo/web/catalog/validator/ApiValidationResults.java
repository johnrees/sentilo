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
package org.sentilo.web.catalog.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class ApiValidationResults {

  private static final long serialVersionUID = 1L;

  private List<String> errors;

  public ApiValidationResults() {
    super();
  }

  public void addErrorMessage(final String errorMessage) {
    if (errors == null) {
      errors = new ArrayList<String>();
    }

    errors.add(errorMessage);
  }

  public boolean hasErrors() {
    return !CollectionUtils.isEmpty(errors);
  }

  public int getErrorsCount() {
    return (hasErrors() ? errors.size() : 0);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Bad request data. Please review the following errors:");
    for (final String error : errors) {
      sb.append("\n\t" + error);
    }

    return sb.toString();
  }
}
