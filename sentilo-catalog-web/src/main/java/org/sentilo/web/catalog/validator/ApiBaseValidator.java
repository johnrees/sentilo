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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sentilo.web.catalog.domain.CatalogDocument;
import org.sentilo.web.catalog.exception.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;


public abstract class ApiBaseValidator<T extends CatalogDocument> {

  protected void validate(final ApiValidationResults results, final Object obj, final String objId, final String objName) {
    final DataBinder binder = new DataBinder(obj);
    binder.setValidator(getValidator());
    binder.validate();

    final BindingResult result = binder.getBindingResult();

    if (result.hasErrors()) {
      final String errorMessage = String.format("%s %s has required fields not filled in.", objName, objId);
      results.addErrorMessage(errorMessage);
    }
  }

  protected void validateKeys(final ApiValidationResults results, final List<T> objects) {
    final EntityKeyValidator keyValidator = buildEntityKeyValidator();

    // Constraints to validate:
    // 1. all object keys are different
    // 2. Not exists an internal resource with the same key in the catalog
    validateUnicityKeys(results, objects);

    if (!results.hasErrors()) {
      for (final T obj : objects) {
        validateIntegrityKey(results, keyValidator, obj);
      }
    }
  }

  protected void validateUnicityKeys(final ApiValidationResults results, final List<T> objects) {
    // Group by key ...
    final Map<String, List<T>> groupsByKey = new HashMap<String, List<T>>();

    // Group by name ...
    for (final T obj : objects) {
      final String key = getGroupKey(obj);
      if (groupsByKey.get(key) == null) {
        groupsByKey.put(key, new ArrayList<T>());
      }
      groupsByKey.get(key).add(obj);
    }

    // ... and count keys elements
    final Iterator<String> it = groupsByKey.keySet().iterator();
    while (it.hasNext()) {
      final String key = it.next();
      if (groupsByKey.get(key).size() > 1) {
        final String errorMessage = String.format("There are more than one resource with key %s .", key);
        results.addErrorMessage(errorMessage);
      }
    }
  }

  protected void validateIntegrityKey(final ApiValidationResults results, final EntityKeyValidator keyValidator, final T object) {
    try {
      keyValidator.checkIntegrityKey(object.getId());
    } catch (final DuplicateKeyException dke) {
      final String errorMessage = buildIntegrityKeyErrorMessage(object);
      results.addErrorMessage(errorMessage);
    }
  }

  protected abstract EntityKeyValidator buildEntityKeyValidator();

  protected abstract String buildIntegrityKeyErrorMessage(final T object);

  protected abstract Validator getValidator();

  protected abstract String getGroupKey(final T object);


}
