
public class User_Data {
    private String username;
    private Integer admin_rights;
    
    public User_Data(String username, Integer admin_rights){
        this.username = username;
        this.admin_rights = admin_rights;
    }
    public String get_username(){
        return this.username;
    }
    public void set_username(String username){
        this.username = username;
    }
    public Integer get_admin_rights(){
        return this.admin_rights;
    }
    public void set_admin_rights(Integer admin_rights){
        this.admin_rights = admin_rights;
    }
    @Override
    public String toString(){
        return "Username " + this.get_username() + " has admin rights equal to " + this.get_admin_rights();
    }
    
}
