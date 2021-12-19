package files;

public class Structure extends Displayable {
    private String name;

    public Structure() {
        System.out.println("Creating a Structure");
    }

    public void setName(String name) {
        System.out.println("Setting name: " + name);
        this.name = name;
    }
}
