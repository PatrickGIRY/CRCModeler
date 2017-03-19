package crc.modeler.infrastructure;

import java.util.Objects;

public class Event {

    private final Object eventType;

    private final Object aggregateId;

    private Event(Object eventType, Object aggregateId) {
        this.eventType = eventType;
        this.aggregateId = aggregateId;
    }

    public static <T, U> Event createEvent(T eventType, U aggregateId) {
        return new Event(eventType, aggregateId);
    }

    public <T> boolean hasAggregateId(T value) {
        return Objects.equals(this.aggregateId, value);
    }

    public <T> boolean hasType(T value) {
        return Objects.equals(this.eventType, value);
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
                '}';
    }
}
