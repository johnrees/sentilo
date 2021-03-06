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
package org.sentilo.platform.client.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sentilo.common.domain.AuthorizedProvider;
import org.sentilo.common.domain.CatalogComponent;
import org.sentilo.common.domain.CatalogSensor;
import org.sentilo.platform.client.core.PlatformClientOperations;
import org.sentilo.platform.client.core.domain.CatalogDeleteInputMessage;
import org.sentilo.platform.client.core.domain.CatalogInputMessage;
import org.sentilo.platform.client.core.domain.CatalogOutputMessage;
import org.sentilo.platform.client.core.exception.PlatformClientAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/sentilo-platform-client-integration.xml")
@TestExecutionListeners(listeners = {BackupAlertsCollectionHook.class, DependencyInjectionTestExecutionListener.class})
public class CatalogServiceOperationsIntegrationTest {

  static String PROVIDER_ID = "testApp_provider";
  static String APP_ID = "testApp";
  static String tokenApp = "e41fe42e03ca8d12d98e924a85eed0e883e6223228e5f8f86f3dda30d4a31ae1";
  static String tokenProv = "4d578b2c0a47130bd2c9fe80801eccdefebd7f34be725f2cb8fb5a5472f6824a";

  @Autowired
  protected PlatformClientOperations platformTemplate;

  @Test(expected = PlatformClientAccessException.class)
  public void registerWithoutSensors() throws Exception {
    final CatalogInputMessage message = new CatalogInputMessage(PROVIDER_ID);
    platformTemplate.getCatalogOps().registerSensors(message);
  }

  @Test
  public void doRegisterGetAndDelete() {
    deleteSensors();
    getSensors(0, 0);
    registerSensors();
    getSensors(1, 5);
    getSensors(1, 2, "temperature");
    getSensors(0, 0, "unknownType");
    deleteSensors();
    getSensors(0, 0);
  }

  @Test
  public void doRegisterUpdateGetAndDelete() {
    deleteSensors();
    getSensors(0, 0);
    registerSensors();
    getSensors(1, 5);
    getSensors(1, 2, "temperature");
    getSensors(1, 3, "wind");
    getSensors(0, 0, "noise");
    updateSensors();
    getSensors(1, 2, "temperature");
    getSensors(1, 1, "wind");
    getSensors(1, 2, "noise");
    getSensors(0, 0, "unknownType");
    validateComponentState("generic", null);
    updateComponents();
    validateComponentState("meteo", "New desc");
    deleteSensors();
    getSensors(0, 0);
  }

  private void registerSensors() {
    final CatalogInputMessage message = new CatalogInputMessage(PROVIDER_ID, buildSensorsToRegister());
    message.setIdentityToken(tokenProv);
    platformTemplate.getCatalogOps().registerSensors(message);
  }

  private void updateSensors() {
    final CatalogInputMessage message = new CatalogInputMessage(PROVIDER_ID, buildSensorsToUpdate());
    message.setIdentityToken(tokenProv);
    platformTemplate.getCatalogOps().updateComponents(message);
  }

  private void updateComponents() {
    final CatalogInputMessage message = new CatalogInputMessage(PROVIDER_ID);
    message.setComponents(buildComponentsToUpdate());
    message.setIdentityToken(tokenProv);
    platformTemplate.getCatalogOps().updateComponents(message);
  }

  private void validateComponentState(final String type, final String desc) {
    final CatalogInputMessage message = new CatalogInputMessage();
    message.setIdentityToken(tokenApp);
    final CatalogOutputMessage outputMessage = platformTemplate.getCatalogOps().getSensors(message);
    final CatalogSensor sensor = outputMessage.getProviders().get(0).getSensors().get(0);
    assertEquals("Expected component type " + type + " but found " + sensor.getComponentType(), type, sensor.getComponentType());
    if (desc == null) {
      assertNull(sensor.getComponentDesc());
    } else {
      assertEquals("Expected component desc " + desc + " but found " + sensor.getComponentDesc(), desc, sensor.getComponentDesc());
    }
  }

  private void getSensors(final int expectedProvidersSize, final int expectedSensorsSize) {
    getSensors(expectedProvidersSize, expectedSensorsSize, null);
  }

  private void getSensors(final int expectedProvidersSize, final int expectedSensorsSize, final String type) {
    final CatalogInputMessage message = new CatalogInputMessage();
    message.setIdentityToken(tokenApp);
    if (StringUtils.hasText(type)) {
      final Map<String, String> parameters = new HashMap<String, String>();
      parameters.put("type", type);
      message.setParameters(parameters);
    }

    final CatalogOutputMessage outputMessage = platformTemplate.getCatalogOps().getSensors(message);
    assertNotNull(outputMessage);
    if (expectedSensorsSize == 0) {
      assertTrue(CollectionUtils.isEmpty(outputMessage.getProviders()));
    } else {
      assertTrue("Expected " + expectedProvidersSize + " providers but found " + outputMessage.getProviders().size(), outputMessage.getProviders().size() == expectedProvidersSize);
      int sensorsSize = 0;
      for (final AuthorizedProvider provider : outputMessage.getProviders()) {
        sensorsSize += provider.getSensors().size();
      }

      assertTrue("Expected " + expectedSensorsSize + " sensors but found " + sensorsSize, sensorsSize == expectedSensorsSize);
    }

  }

  private void deleteSensors() {
    final CatalogDeleteInputMessage message = new CatalogDeleteInputMessage(PROVIDER_ID);
    message.setIdentityToken(tokenProv);
    platformTemplate.getCatalogOps().deleteProvider(message);
  }

  private List<CatalogSensor> buildSensorsToRegister() {
    final List<CatalogSensor> sensors = new ArrayList<CatalogSensor>();
    final CatalogSensor sensor0 = buildSensor("TEST_REC0122", "TESTAPP_COMPONENT", PROVIDER_ID, "sensor 122", "number", "wind", "km/h");
    final CatalogSensor sensor1 = buildSensor("TEST_REC0123", "TESTAPP_COMPONENT", PROVIDER_ID, "sensor 123", "number", "43.39950387509218 5.1809202294998613", "temperature", "C");
    final CatalogSensor sensor2 = buildSensor("TEST_REC0124", "TESTAPP_COMPONENT", PROVIDER_ID, "sensor 124", "number", "43.39950387509218 5.1809202294998613", "temperature", "C");
    final CatalogSensor sensor3 = buildSensor("TEST_REC0125", "TESTAPP_COMPONENT", PROVIDER_ID, "sensor 125", "number", "wind", "km/h");
    final CatalogSensor sensor4 = buildSensor("TEST_REC0126", "TESTAPP_COMPONENT", PROVIDER_ID, "sensor 126", "number", "wind", "km/h");
    sensors.add(sensor0);
    sensors.add(sensor1);
    sensors.add(sensor2);
    sensors.add(sensor3);
    sensors.add(sensor4);

    return sensors;
  }

  private List<CatalogSensor> buildSensorsToUpdate() {
    final List<CatalogSensor> sensors = new ArrayList<CatalogSensor>();
    final CatalogSensor sensor0 = buildSensor("TEST_REC0122", "TESTAPP_COMPONENT", PROVIDER_ID, null, null, "noise", "db");
    final CatalogSensor sensor3 = buildSensor("TEST_REC0125", "TESTAPP_COMPONENT", PROVIDER_ID, null, null, "noise", "db");
    final CatalogSensor sensor4 = buildSensor("TEST_REC0126", "TESTAPP_COMPONENT", PROVIDER_ID, "desc del sensor 126", null, null, null);
    sensors.add(sensor0);
    sensors.add(sensor3);
    sensors.add(sensor4);

    return sensors;
  }

  private List<CatalogComponent> buildComponentsToUpdate() {
    final List<CatalogComponent> components = new ArrayList<CatalogComponent>();
    final CatalogComponent component = new CatalogComponent();
    component.setComponent("TESTAPP_COMPONENT");
    component.setComponentType("meteo");
    component.setComponentDesc("New desc");

    components.add(component);

    return components;
  }

  private CatalogSensor buildSensor(final String sensor, final String component, final String provider, final String description, final String dataType, final String type, final String unit) {
    return buildSensor(sensor, component, provider, description, dataType, null, type, unit);
  }

  private CatalogSensor buildSensor(final String sensor, final String component, final String provider, final String description, final String dataType, final String location, final String type, final String unit) {
    final CatalogSensor catalogSensor = new CatalogSensor();
    catalogSensor.setSensor(sensor);
    catalogSensor.setComponent(component);
    catalogSensor.setProvider(provider);
    catalogSensor.setDescription(description);
    catalogSensor.setDataType(dataType);
    catalogSensor.setLocation(location);
    catalogSensor.setType(type);
    catalogSensor.setUnit(unit);

    return catalogSensor;
  }
}
