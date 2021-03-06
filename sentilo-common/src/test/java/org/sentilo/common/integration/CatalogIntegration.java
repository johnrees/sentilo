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
package org.sentilo.common.integration;

import static org.junit.Assert.assertTrue;

import org.sentilo.common.rest.RESTClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

public class CatalogIntegration {

  protected RESTClient httpRestClient;

  public CatalogIntegration() {
    init();
  }

  protected void init() {
    final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/catalog-integration-context.xml");
    httpRestClient = context.getBean(RESTClient.class);
  }

  public void validateApiPermissions() {
    final String uri = "api/permissions";
    final String response = httpRestClient.get(uri);
    // Un ejemplo de respuesta es
    // {"permissions":[{"source":"prov1","target":"prov1","type":"WRITE"},{"source":"prov2","target":"prov2","type":"WRITE"}]}
    System.out.println(response);
    assertTrue("No se ha realizado correctamente la llamada al catalogo", StringUtils.hasText(response));
    assertTrue("La respuesta no tiene el formato esperado", response.contains("permissions") && response.contains("source") && response.contains("target") && response.contains("type"));
  }

  public void validateApiAuth() {
    final String uri = "api/credentials";
    final String response = httpRestClient.get(uri);
    // Un ejemplo de respuesta es
    // {"authorizations":[{"entity":"app1","token":"token3"},{"entity":"app2","token":"token4"},{"entity":"app3","token":"token5"}]}
    System.out.println(response);
    assertTrue("No se ha realizado correctamente la llamada al catalogo", StringUtils.hasText(response));
    assertTrue("La respuesta no tiene el formato esperado", response.contains("authorizations") && response.contains("entity") && response.contains("token"));
  }

  public static void main(final String[] args) {
    final CatalogIntegration driver = new CatalogIntegration();

    driver.validateApiAuth();
    driver.validateApiPermissions();
  }

}
