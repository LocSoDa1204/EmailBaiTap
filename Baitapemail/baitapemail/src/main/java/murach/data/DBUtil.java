package murach.data;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {

    private static final EntityManagerFactory emf = create();

    private static EntityManagerFactory create() {
        Map<String, String> props = new HashMap<>();
        String url = System.getenv("DB_URL");
        if (url != null && !url.isEmpty()) {
            props.put("javax.persistence.jdbc.url", url);
            props.put("javax.persistence.jdbc.user", env("DB_USER"));
            props.put("javax.persistence.jdbc.password", env("DB_PASSWORD"));
            if (url.startsWith("jdbc:postgresql")) {
                props.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            }
        }
        return Persistence.createEntityManagerFactory("emailListPU", props);
    }

    private static String env(String name) {
        String v = System.getenv(name);
        return v == null ? "" : v;
    }

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}