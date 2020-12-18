package game.boucleur;

public class BoucleurSimple extends Boucleur{

    private boolean actif = true;

    public boolean isActif() { return actif; }

    public void setActif(boolean actif) { this.actif = actif; }

    @Override
    public void run() {
        while(actif)
        {
            try {
                Thread.sleep(16);
            }
            catch (InterruptedException e)
            {
                actif = false;
            }
        }

    }
}
