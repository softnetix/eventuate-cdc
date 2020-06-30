package io.eventuate.local.test.util;

import io.eventuate.local.PublishedEvent;

public interface CdcProcessorCommon {
  default void onEventSent(PublishedEvent publishedEvent) {}
}
