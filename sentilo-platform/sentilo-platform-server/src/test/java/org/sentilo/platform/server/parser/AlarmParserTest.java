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
package org.sentilo.platform.server.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sentilo.platform.common.domain.AlarmInputMessage;
import org.sentilo.platform.common.exception.JsonConverterException;
import org.sentilo.platform.server.request.SentiloRequest;
import org.sentilo.platform.server.request.SentiloResource;

public class AlarmParserTest {

  private AlarmParser parser;
  @Mock
  private SentiloRequest sentiloRequest;
  @Mock
  private SentiloResource resource;

  @Before
  public void setUp() throws Exception {
    final String identityKeyProvider = "prov1";
    MockitoAnnotations.initMocks(this);
    parser = new AlarmParser();

    when(sentiloRequest.getResource()).thenReturn(resource);
    when(sentiloRequest.getEntitySource()).thenReturn(identityKeyProvider);
  }

  @Test
  public void parsePutRequest() throws Exception {
    final String ownerAlertId = "prov1";
    final String alertId = "alarm1";
    final String messageText = "superado umbral en el sensor sensor1";

    final String json = "{\"message\":\"superado umbral en el sensor sensor1\"}";

    when(sentiloRequest.getBody()).thenReturn(json);
    when(sentiloRequest.getResourcePart(0)).thenReturn(ownerAlertId);
    when(sentiloRequest.getResourcePart(1)).thenReturn(alertId);

    final AlarmInputMessage message = parser.parseRequest(sentiloRequest);

    assertEquals(message.getMessage(), messageText);
  }

  @Test
  public void parsePutRequestWithoutMessage() throws Exception {
    final String alertId = "alarm1";

    when(sentiloRequest.getBody()).thenReturn("");
    when(sentiloRequest.getResourcePart(0)).thenReturn(alertId);

    try {
      parser.parseRequest(sentiloRequest);
    } catch (final JsonConverterException jce) {
      assertEquals("Must return 400 - Bad request", HttpStatus.SC_BAD_REQUEST, jce.getHttpStatus());
    }
  }

  @Test
  public void parseGetRequestWithoutFilter() throws Exception {
    final String alertId = "alarm1";

    when(resource.getResourcePart(0)).thenReturn(alertId);

    final AlarmInputMessage message = parser.parseGetRequest(sentiloRequest);

    assertEquals(message.getAlertId(), alertId);
  }

  @Test
  public void parseGetRequestWithFilter() throws Exception {
    final String alertId = "alarm1";
    final String from = "17/09/2012T12:34:45";
    final String to = null;
    final String limit = "5";

    when(resource.getResourcePart(0)).thenReturn(alertId);
    when(sentiloRequest.getRequestParameter("from")).thenReturn(from);
    when(sentiloRequest.getRequestParameter("to")).thenReturn(to);
    when(sentiloRequest.getRequestParameter("limit")).thenReturn(limit);

    final AlarmInputMessage message = parser.parseGetRequest(sentiloRequest);

    assertEquals(message.getAlertId(), alertId);
    assertTrue(message.hasQueryFilters());
    assertEquals(message.getQueryFilters().getLimit(), parser.parseInteger(limit));
    assertEquals(message.getQueryFilters().getFrom(), parser.parseDate(from));
    assertEquals(message.getQueryFilters().getTo(), parser.parseDate(to));
  }

}
