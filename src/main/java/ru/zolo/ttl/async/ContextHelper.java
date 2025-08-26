package ru.zolo.ttl.async;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHelper {

    private static final TransmittableThreadLocal<String> runId = new TransmittableThreadLocal <>();


    public static void addRunIdFromContext(String context) {
        runId.set(context);
    }


    public static String getRunId() {
        return runId.get();
    }


    public static void clear() {
        runId.remove();
    }
}
