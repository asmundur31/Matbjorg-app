package is.hi.hbv601g.matbjorg_app.models;

public class Buyer {
    /**
     * id sem auðkennir kaupanda
     * name sem tilgreinir nafn kaupanda
     * password sem er lykilorð kaupanda
     * email sem er netfang kaupanda
     */
    private long id;
    private String name;
    private String password;
    private String email;

    public Buyer() {
    }

    /**
     * Smiður fyrir klasann Buyer
     * @param name nafn nýs kaupanda
     * @param email netfang nýs kaupanda
     * @param password lykilorð nýs kaupanda
     */
    public Buyer(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
