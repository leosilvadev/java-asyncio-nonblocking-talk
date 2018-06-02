package com.github.leosilvadev.core.consumers;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by leonardo on 6/2/18.
 */
public class DeadEventConsumer {

  private static final Logger logger = LoggerFactory.getLogger(DeadEventConsumer.class);

  @Subscribe
  public void handleDeadEvent(final DeadEvent event) {
    logger.warn("Event without consumer. {}", event);
  }

}
