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
package org.sentilo.platform.server.handler;

import org.apache.http.HttpStatus;
import org.sentilo.platform.common.exception.PlatformException;
import org.sentilo.platform.server.auth.AuthorizationService;
import org.sentilo.platform.server.exception.ForbiddenAccessException;
import org.sentilo.platform.server.request.SentiloRequest;
import org.sentilo.platform.server.response.SentiloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

public abstract class AbstractHandler {

  private final Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

  @Autowired
  protected AuthorizationService authorizationService;

  @Value("${catalog.id}")
  protected String catalogId;

  private static final String DEFAULT_CATALOG_ID = "sentilo-catalog";

  public abstract void onDelete(SentiloRequest request, SentiloResponse response) throws PlatformException;

  public abstract void onGet(SentiloRequest request, SentiloResponse response) throws PlatformException;

  public abstract void onPost(SentiloRequest request, SentiloResponse response) throws PlatformException;

  public abstract void onPut(SentiloRequest request, SentiloResponse response) throws PlatformException;

  public final void manageRequest(final SentiloRequest request, final SentiloResponse response) throws PlatformException {

    getLogger().debug("Manage {} request", request.getMethod());

    switch (request.getMethod()) {
      case DELETE:
        onDelete(request, response);
        break;
      case GET:
        onGet(request, response);
        break;
      case POST:
        onPost(request, response);
        break;
      case PUT:
        onPut(request, response);
        break;
    }
  }

  protected void validateReadAccess(final String source, final String target) throws ForbiddenAccessException {
    if (!authorizationService.hasAccessToRead(source, target)) {
      throw new ForbiddenAccessException(source, target, "READ");
    }
  }

  protected void validateWriteAccess(final String source, final String target) throws ForbiddenAccessException {
    if (!authorizationService.hasAccessToWrite(source, target)) {
      throw new ForbiddenAccessException(source, target, "WRITE");
    }
  }

  protected void validateSourceCanAdminTarget(final String source, final String target) throws ForbiddenAccessException {
    // source could admin target's resources iff source is equals to catalog app or source and
    // target are equals
    if (!getCatalogId().equals(source) && !source.equals(target)) {
      final String errorMessage = String.format("You are not %s entity. Review token identity or request path content.", target);
      throw new ForbiddenAccessException(errorMessage);
    }

  }

  protected void validateAdminAccess(final String source) throws ForbiddenAccessException {
    // solo la aplicacion catalogo puede invocar a este servicio
    if (!StringUtils.hasText(source) || !source.equals(getCatalogId())) {
      throw new ForbiddenAccessException(String.format("Entity %s has not permission to call admin service", source));
    }
  }

  protected void validateResourceNumberParts(final SentiloRequest request, final int min, final int max) throws PlatformException {
    // Validamos que el path tenga el numero de tokens que corresponde a la accion invocada
    if (!numberArgumentsValid(request.getResource().getParts(), min, max)) {
      throw new PlatformException(HttpStatus.SC_BAD_REQUEST, "Invalid path was requested:" + request.getUri());
    }
  }

  protected boolean numberArgumentsValid(final String[] arguments, final int min, final int max) {
    return (arguments == null ? min == 0 : (arguments.length >= min && arguments.length <= max));
  }

  protected String getCatalogId() {
    // Default value is sentilo-catalog
    return (StringUtils.hasText(catalogId) ? catalogId : DEFAULT_CATALOG_ID);
  }

  protected void debug(final SentiloRequest request) {
    getLogger().debug(request.toString());
  }

  public void setAuthorizationService(final AuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }

  public Logger getLogger() {
    return logger;
  }
}
