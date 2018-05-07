# Examples

```java
// Ensure you have some means of having a Jackson ObjectMapper
// available in your CDI ecosystem.  One way to do this is to
// place the microBean Jackson CDI project on your classpath
// (https://github.com/microbean/microbean-jackson-cdi).

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import org.microbean.cloudevents.jackson.cdi.CloudEventBroadcaster;

@ApplicationScoped // or whatever you like
final class MyCloudEventPump {

  @Inject
  private CloudEventBroadcaster broadcaster;
  
  MyCloudEventPump() {
    super();
  }
  
  void broadcast() throws IOException {
    // See https://github.com/cloudevents/spec/blob/master/json-format.md
    final InputStream stream = acquireAnInputStreamOfJsonFormattedCloudEvents();
    this.broadcaster.fire(stream);
    stream.close();
  }

}

// Then, elsewhere...

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.event.Observes;

import javax.inject.Inject;

import org.microbean.cloudevents.CloudEvent;

@ApplicationScoped
public class MyCloudEventObserver {

  public MyCloudEventObserver() {
    super();
  }
  
  private final void onCloudEvent(@Observes final CloudEvent event) {
    // do something useful
  }

}

```
