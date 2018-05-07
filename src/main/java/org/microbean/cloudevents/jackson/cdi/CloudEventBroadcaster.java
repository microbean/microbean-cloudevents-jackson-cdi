/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2018 microBean.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.cloudevents.jackson.cdi;

import java.io.IOException;
import java.io.InputStream;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.event.Event;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.MappingIterator;

import org.microbean.cloudevents.CloudEvent;

@ApplicationScoped
public class CloudEventBroadcaster {

  private final ObjectReader objectReader;
  
  private final Event<CloudEvent> broadcaster;

  @Inject
  public CloudEventBroadcaster(final ObjectMapper objectMapper,
                               final Event<CloudEvent> broadcaster) {
    super();
    this.objectReader = Objects.requireNonNull(objectMapper).readerFor(CloudEvent.class);
    this.broadcaster = Objects.requireNonNull(broadcaster);
  }

  public void fire(final InputStream stream) throws IOException {
    Objects.requireNonNull(stream);
    try (final MappingIterator<CloudEvent> iterator = this.objectReader.readValues(stream)) {
      assert iterator != null;
      while (iterator.hasNext()) {
        this.broadcaster.fire(iterator.next());
      }
    }
  }

  public void fireAsync(final InputStream stream) throws IOException {
    Objects.requireNonNull(stream);
    try (final MappingIterator<CloudEvent> iterator = this.objectReader.readValues(stream)) {
      assert iterator != null;
      while (iterator.hasNext()) {
        this.broadcaster.fireAsync(iterator.next());
      }
    }
  }
  
}
