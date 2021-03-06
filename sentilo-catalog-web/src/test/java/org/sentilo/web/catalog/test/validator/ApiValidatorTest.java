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
package org.sentilo.web.catalog.test.validator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sentilo.web.catalog.domain.Component;
import org.sentilo.web.catalog.domain.ComponentType;
import org.sentilo.web.catalog.domain.Sensor;
import org.sentilo.web.catalog.domain.SensorType;
import org.sentilo.web.catalog.search.SearchFilter;
import org.sentilo.web.catalog.search.SearchFilterResult;
import org.sentilo.web.catalog.service.ComponentTypesService;
import org.sentilo.web.catalog.service.SensorService;
import org.sentilo.web.catalog.service.SensorTypesService;
import org.sentilo.web.catalog.test.AbstractBaseTest;
import org.sentilo.web.catalog.validator.ApiValidationResults;
import org.sentilo.web.catalog.validator.ApiValidator;
import org.springframework.validation.Validator;

public class ApiValidatorTest extends AbstractBaseTest {

  private final String providerId = "provider1";
  private final String componentName = "component1";
  private final String sensor1Id = "sensor1";
  private final String sensor2Id = "sensor2";
  private final String temperatureType = "temperature";

  @Mock
  private SensorTypesService sensorTypesService;

  @Mock
  private ComponentTypesService componentTypesService;

  @Mock
  private SensorService sensorService;

  @Mock
  private Validator validator;

  private ApiValidator apiValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    apiValidator = new ApiValidator();
    injectMocks();

    when(sensorTypesService.findAll()).thenReturn(getSensorTypes());
    when(componentTypesService.findAll()).thenReturn(Collections.<ComponentType>emptyList());
    when(validator.supports(Sensor.class)).thenReturn(true);
    when(sensorService.search(any(SearchFilter.class))).thenReturn(new SearchFilterResult<Sensor>(Collections.<Sensor>emptyList(), new SearchFilter(), 0));
  }

  @Test
  public void validateNokSensors() {
    final List<Sensor> sensors = getSensorsWithErrors();
    final ApiValidationResults result = apiValidator.validateSensorsAndComponents(sensors, null, false);
    assertTrue(result.hasErrors());
  }

  @Test
  public void validateOkSensors() {
    final List<Sensor> sensors = getSensors();
    final ApiValidationResults result = apiValidator.validateSensorsAndComponents(sensors, null, false);
    assertTrue(!result.hasErrors());
  }

  private List<Sensor> getSensorsWithErrors() {
    final List<Sensor> sensors = new ArrayList<Sensor>();

    final String componentId = Component.buildId(providerId, componentName);

    final Sensor sensor = new Sensor(providerId, componentId, sensor1Id);
    sensor.setDescription("Lorem ipsum dolor sit amet");
    sensor.setUnit("C");
    sensor.setDataType(Sensor.DataType.BOOLEAN);
    sensor.setCreatedAt(new Date());
    sensors.add(sensor);

    final Sensor sensor2 = new Sensor(providerId, componentId, sensor2Id);
    sensor2.setDescription("Sed ut perspiciatis unde omnis");
    sensor2.setType(temperatureType);
    sensor2.setDataType(Sensor.DataType.BOOLEAN);
    sensor2.setCreatedAt(new Date());
    sensors.add(sensor2);

    return sensors;
  }

  private List<Sensor> getSensors() {
    final List<Sensor> sensors = new ArrayList<Sensor>();

    final String componentId = Component.buildId(providerId, componentName);

    final Sensor sensor = new Sensor(providerId, componentId, sensor1Id);
    sensor.setDescription("Lorem ipsum dolor sit amet");
    sensor.setUnit("C");
    sensor.setType(temperatureType);
    sensor.setDataType(Sensor.DataType.BOOLEAN);
    sensor.setCreatedAt(new Date());
    sensors.add(sensor);

    return sensors;
  }

  private List<SensorType> getSensorTypes() {
    final SensorType[] sensorTypes = {new SensorType(temperatureType)};
    return Arrays.asList(sensorTypes);
  }

  private void injectMocks() throws Exception {
    injectField("sensorService", sensorService, apiValidator, ApiValidator.class);
    injectField("sensorTypesService", sensorTypesService, apiValidator, ApiValidator.class);
    injectField("componentTypesService", componentTypesService, apiValidator, ApiValidator.class);
    injectField("validator", validator, apiValidator, ApiValidator.class);
  }
}
