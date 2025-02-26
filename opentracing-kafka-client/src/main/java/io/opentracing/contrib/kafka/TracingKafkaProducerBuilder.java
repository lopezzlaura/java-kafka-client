package io.opentracing.contrib.kafka;

import io.opentracing.Tracer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class TracingKafkaProducerBuilder<K, V> {
    private List<SpanDecorator> spanDecorators;
    private Producer<K, V> producer;
    private Tracer tracer;
    private BiFunction<String, ProducerRecord, String> producerSpanNameProvider;

    public TracingKafkaProducerBuilder(Producer<K, V> producer, Tracer tracer) {
        this.tracer = tracer;
        this.producer = producer;
        this.spanDecorators = Collections.singletonList(SpanDecorator.STANDARD_TAGS);
        this.producerSpanNameProvider = ClientSpanNameProvider.PRODUCER_OPERATION_NAME;
    }

    public TracingKafkaProducerBuilder withDecorators(List<SpanDecorator> spanDecorators) {
        this.spanDecorators = spanDecorators;
        return this;
    }

    public TracingKafkaProducerBuilder withSpanNameProvider(BiFunction<String, ProducerRecord, String> producerSpanNameProvider) {
        this.producerSpanNameProvider = producerSpanNameProvider;
        return this;
    }

    public TracingKafkaProducer<K, V> build() {
        return new TracingKafkaProducer<>(producer, tracer, spanDecorators, producerSpanNameProvider);
    }
}
