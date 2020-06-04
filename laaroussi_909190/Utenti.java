public class Utenti {
    private String contenuto;
    private String user; //salvai info dell'ultimo utente a modificare;
    private String stato;
    public Utenti(){
        this.contenuto = "";
        this.user = "";
        this.stato = "";
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getContenuto() {
        return contenuto;
    }

    public String getUltimoUser() {
        return user;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public void setUltimoUser(String ultimoUser) {
        user = ultimoUser;
    }

}
