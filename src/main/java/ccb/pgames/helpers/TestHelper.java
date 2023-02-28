package ccb.pgames.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestHelper {

    static ObjectMapper objectMapper = null;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}
