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
package org.sentilo.platform.server.request;

import org.junit.Assert;
import org.junit.Test;

public class RequestUtilsTest {

  @Test
  public void splitPath() {
    String path = "/token1/token2/token3/token4";

    String[] tokens = RequestUtils.splitPath(path);

    Assert.assertEquals(4 + 1, tokens.length);
  }

  @Test(expected = IllegalArgumentException.class)
  public void splitEmptyPath() {
    String path = "";

    String[] tokens = RequestUtils.splitPath(path);

    Assert.assertEquals(4 + 1, tokens.length);
  }

  @Test
  public void buildPath() {
    String[] tokens = {"token1", "token2", "token3", "token4"};

    String path = RequestUtils.buildPath(tokens[0], tokens[1], tokens[2]);

    Assert.assertEquals("/token1/token2/token3", path);
  }

  @Test
  public void extractResource() {
    String path = "/token1/token2/token3/token4";
    String handlerPath = "/token1/token2";

    String resourcePath = RequestUtils.extractResource(path, handlerPath);

    Assert.assertEquals("token3/token4", resourcePath);
  }

}
