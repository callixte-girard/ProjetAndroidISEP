package fr.isep.c.projetandroidisep.myCustomTypes;

public class User
{
    private String name ;
    private String email ;
    private String password ;

    public User(String name, String email, String password) {
        this.name = name ;
        this.email = email ;
        this.password = password ;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return this.email;}
    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return this.password;}

}
