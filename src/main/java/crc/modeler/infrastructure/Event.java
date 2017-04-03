package crc.modeler.infrastructure;

import java.util.Objects;

public class Event {

    private final Object eventType;

    private final Object aggregateId;

    private final Object data;

    private Event(Object eventType, Object aggregateId, Object data) {
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.data = data;
    }

    public static <T, U> Event createEvent(T eventType, U aggregateId) {
        return createEvent(eventType, aggregateId, null);
    }

    public static <T, U, V> Event createEvent(T eventType, U aggregateId, V data) {
        return new Event(eventType, aggregateId, data);
    }

    public Object getEventType() {
        return eventType;
    }

    public <T> T getEventType(Class<T> eventTypeClass) {
        return eventTypeClass.cast(getEventType());
    }

    public Object getAggregateId() {
        return aggregateId;
    }

    public <T> T getAggregateId(Class<T> aggregateIdClass) {
        return aggregateIdClass.cast(aggregateId);
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventType, event.eventType) &&
                Objects.equals(aggregateId, event.aggregateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, aggregateId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventType +
                ", aggregateId=" + aggregateId +
                ", data=" + data +
                '}';
    }
}
