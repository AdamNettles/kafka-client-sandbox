package pkgggg

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.schemaregistry.client.rest.RestService
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException
import org.apache.avro.Schema

class CachedSchemaRegistryClientEx extends CachedSchemaRegistryClient {

    @Override
    List<Integer> getAllVersions(String subject) throws IOException, RestClientException {
        super.getAllVersions(subject.replace('-dummy',''))
    }

    @Override
    synchronized int getId(String subject, Schema schema) throws IOException, RestClientException {
        super.getId(subject.replace('-dummy',''), schema)
    }

    CachedSchemaRegistryClientEx(String baseUrl, int identityMapCapacity) {
        super(baseUrl, identityMapCapacity)
    }

    CachedSchemaRegistryClientEx(List<String> baseUrls, int identityMapCapacity) {
        super(baseUrls, identityMapCapacity)
    }

    CachedSchemaRegistryClientEx(RestService restService, int identityMapCapacity) {
        super(restService, identityMapCapacity)
    }

    CachedSchemaRegistryClientEx(String baseUrl, int identityMapCapacity, Map<String, ?> originals) {
        super(baseUrl, identityMapCapacity, originals)
    }

    CachedSchemaRegistryClientEx(List<String> baseUrls, int identityMapCapacity, Map<String, ?> originals) {
        super(baseUrls, identityMapCapacity, originals)
    }

    CachedSchemaRegistryClientEx(RestService restService, int identityMapCapacity, Map<String, ?> configs) {
        super(restService, identityMapCapacity, configs)
    }

    CachedSchemaRegistryClientEx(String baseUrl, int identityMapCapacity, Map<String, ?> originals, Map<String, String> httpHeaders) {
        super(baseUrl, identityMapCapacity, originals, httpHeaders)
    }

    CachedSchemaRegistryClientEx(List<String> baseUrls, int identityMapCapacity, Map<String, ?> originals, Map<String, String> httpHeaders) {
        super(baseUrls, identityMapCapacity, originals, httpHeaders)
    }

    CachedSchemaRegistryClientEx(RestService restService, int identityMapCapacity, Map<String, ?> configs, Map<String, String> httpHeaders) {
        super(restService, identityMapCapacity, configs, httpHeaders)
    }
}
