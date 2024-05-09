package all.provider;

import java.util.List;

public abstract class Provider {

    private String id; // Unique identifier for the provider

    public Provider(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract List<Claim> retrieveClaims(ClaimFilter filter); // Implement filtering logic

    public abstract void logsActions(String message); // Implement logging mechanism
}
