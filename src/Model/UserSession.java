package Model;

/**
 * Singleton class to store current logged-in user session
 */
public class UserSession {
    private static UserSession instance;
    private String username;
    private String email;
    private String role;
    private int userId;
    
    private UserSession() {}
    
    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setUserData(UserData user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.userId = user.getId();
        
        System.out.println("=== UserSession.setUserData() ===");
        System.out.println("Set username: " + this.username);
        System.out.println("Set email: " + this.email);
        System.out.println("Set role: " + this.role);
        System.out.println("Set userId: " + this.userId);
    }
    
    public void clearSession() {
        this.username = null;
        this.email = null;
        this.role = null;
        this.userId = 0;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        System.out.println("=== UserSession.getEmail() called === Returning: " + email);
        return email;
    }
    
    public String getRole() {
        return role;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }
}
