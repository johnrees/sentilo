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
package org.sentilo.platform.client.core.service.impl;

import org.sentilo.platform.client.core.domain.SubscribeInputMessage;
import org.sentilo.platform.client.core.domain.SubscriptionsOutputMessage;
import org.sentilo.platform.client.core.parser.SubscribeMessageConverter;
import org.sentilo.platform.client.core.service.SubscribeServiceOperations;
import org.sentilo.platform.client.core.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultSubscribeServiceOperationsImpl extends AbstractServiceOperationsImpl implements SubscribeServiceOperations {

  private final Logger logger = LoggerFactory.getLogger(DefaultSubscribeServiceOperationsImpl.class);

  private final SubscribeMessageConverter converter = new SubscribeMessageConverter();

  @Override
  public void remove(final SubscribeInputMessage message) {
    logger.debug("Removing subscription message {}", message);
    restClient.delete(RequestUtils.buildPath(message), message.getIdentityToken());
    logger.debug("Subscription removed");
  }

  @Override
  public SubscriptionsOutputMessage get(final SubscribeInputMessage message) {
    logger.debug("Retrieving subscriptions {}", message);
    final String response = restClient.get(RequestUtils.buildPath(message), message.getIdentityToken());
    logger.debug("Subscriptions retrieved");
    return converter.unmarshall(response);

  }

  @Override
  public void subscribe(final SubscribeInputMessage message) {
    logger.debug("Adding subscription message {}", message);
    restClient.put(RequestUtils.buildPath(message), converter.marshall(message), message.getIdentityToken());
    logger.debug("Subscription added");
  }

}
